/***************************************************************************
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2014 Quantis SARL, All Rights Reserved.
 * NOTICE: All information contained herein is, and remains the property of Quantis Sàrl. The intellectual and
 * technical concepts contained herein are proprietary to Quantis Sàrl and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Quantis Sàrl. Access to the source code contained herein is hereby forbidden to anyone
 * except current Quantis Sàrl employees, managers or contractors who have executed Confidentiality and Non-disclosure
 * agreements explicitly covering such access.
 * The copyright notice above does not evidence any actual or intended publication or disclosure of this source code,
 * which includes information that is confidential and/or proprietary, and is a trade secret, of Quantis Sàrl.
 * ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE
 * CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF Quantis Sàrl IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE LAWS
 * AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY
 * OR IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT
 * IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.quantis_intl.lcigenerator;

import java.security.SecureRandom;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.quantis_intl.lcigenerator.dao.LoginDao;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.lcigenerator.model.UserPwd;
import com.quantis_intl.stack.authentication.LoginService;

public class LoginServiceImpl implements LoginService
{
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static final int MAX_ATTEMPS = 8;
    private static final long LOCKED_DURATION_IN_MILLISECONDS = 20 * 60 * 1000; // 20 min

    // NOTE: Used to add a bit of security to avoid bruteforce without compromising the source code.
    // TODO: Using HMAC would be even better
    private String secret_salt = "Jo5xNFdSPUmc4ijk2euM";

    private Provider<LoginDao> dao;

    @Inject
    public LoginServiceImpl(Provider<LoginDao> dao)
    {
        this.dao = dao;
    }

    @Override
    public Object getAuthenticUserId(String username, String password)
    {
        User user = getExistingUser(username);

        UserPwd userPwd = dao.get().getUserPwdFromUserId(user.getId());
        checkNotValidatedAccount(userPwd);
        checkLockedAccount(userPwd);
        checkPassword(password, userPwd);

        clearLockedState(userPwd);

        return user.getId();
    }

    private User getExistingUser(String username)
    {
        User user = dao.get().getUserFromUsername(username);
        if (user == null)
            throw new LoginFailed(LoginFailedReason.USER_NOT_FOUND, -1, false);

        return user;
    }

    private void checkNotValidatedAccount(UserPwd userPwd)
    {
        if (userPwd.getRegistrationCode() != null)
            failAttemptOfRegisteredUser(userPwd, LoginFailedReason.NON_VALIDATED_USER);
    }

    private void checkLockedAccount(UserPwd userPwd)
    {
        if (isUserLocked(userPwd))
            failAttemptOfRegisteredUser(userPwd, LoginFailedReason.LOCKED_USER);
    }

    private void checkPassword(String password, UserPwd userPwd)
    {
        if (!isRightPassword(password, userPwd))
            failAttemptOfRegisteredUser(userPwd, LoginFailedReason.WRONG_CREDENTIALS);
    }

    private boolean isRightPassword(String password, UserPwd userPwd)
    {
        return hashString(password, userPwd.getBase64salt()).equals(userPwd.getPassword());
    }

    private boolean isUserLocked(UserPwd userPwd)
    {
        if (userPwd.getLockedSince() != null)
        {
            long diffInMilliSeconds = (new Date()).getTime() - userPwd.getLockedSince().getTime();
            return diffInMilliSeconds <= LOCKED_DURATION_IN_MILLISECONDS;
        }
        return false;
    }

    // TODO: The account locking is used to prevent brute-force attacks. But it's not the best solution
    // Think about other solutions.
    private void failAttemptOfRegisteredUser(UserPwd userPwd, LoginFailedReason reason)
    {
        boolean accountHasBeenLocked = false;
        int nbAttemps = userPwd.getFailedAttemps() + 1;
        userPwd.setFailedAttemps(nbAttemps);
        if (nbAttemps >= MAX_ATTEMPS && !isUserLocked(userPwd))
        {
            userPwd.setLockedSince(new Date());
            accountHasBeenLocked = true;
        }

        dao.get().updateLockedState(userPwd);

        throw new LoginFailed(reason, userPwd.getUserId(), accountHasBeenLocked);
    }

    private void clearLockedState(UserPwd userPwd)
    {
        if (userPwd.getFailedAttemps() != 0 || userPwd.getLockedSince() != null)
        {
            userPwd.setFailedAttemps(0);
            userPwd.setLockedSince(null);
            dao.get().updateLockedState(userPwd);
        }
    }

    /**
     * Hash the given string using the given salt and an internal secret salt
     * 
     * @param stringToHash
     *            The string to hash
     * @param base64salt
     *            The salt to used, represented in a base64 string (should be 33 bytes after decoding)
     * @return a concatenation of "sha512:" and the sha512 hashed string
     */
    private String hashString(String stringToHash, String base64salt)
    {
        String hexaHash = Hashing.sha512().newHasher().putBytes(BaseEncoding.base64().decode(base64salt))
                .putString(stringToHash, Charsets.UTF_8)
                .putUnencodedChars(secret_salt)
                .hash()
                .toString();
        return "sha512:" + hexaHash;
    }

    public String createUser(User user)
    {
        String username = user.getUsername();
        User existingUser = dao.get().getUserFromUsername(username);
        if (existingUser != null)
        {
            LOG.warn("Trying to create a user with an already created username: {}", username);
            throw new UsernameAlreadyExists(username);
        }

        String email = user.getEmail();
        if (!isEmailComplient(email))
            throw new InvalidEmail(email);

        User existingEmailUser = dao.get().getUserFromEmail(email);
        if (existingEmailUser != null)
        {
            LOG.warn("Trying to create a user with an already created email: {}", email);
            throw new EmailAlreadyExists(email);
        }

        final String password = generatePassword();
        byte[] randomSalt = new byte[33];
        new SecureRandom().nextBytes(randomSalt);
        String base64salt = BaseEncoding.base64().encode(randomSalt);
        UserPwd userPwd = new UserPwd(0, base64salt, hashString(password, base64salt), false, 0, null, null, null, null);

        dao.get().createUser(user, userPwd);
        User savedUser = dao.get().getUserFromUsername(username);

        LOG.info("New user created ({}) with username: {}", savedUser.getId(), username);

        return password;
    }

    private boolean isEmailComplient(String email)
    {
        // FIXME Find a better regExp or another email validation solution
        return email.matches("^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,})+$");
    }

    static final String ALLOWED_SYMBOLS = "!@#$%&()*+,-./:;=?[]^_{}~";
    static final String CORPUS = "abcdefghijkmnopqrstuvwxyz123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private String generatePassword()
    {
        SecureRandom secureRandom = new SecureRandom();
        char[] password = new char[11];
        for (int i = 0; i < 11; i++)
            password[i] = CORPUS.charAt(secureRandom.nextInt(CORPUS.length()));

        password[secureRandom.nextInt(9) + 1] = ALLOWED_SYMBOLS.charAt(secureRandom.nextInt(ALLOWED_SYMBOLS.length()));

        return new String(password);
    }

    @Override
    public void confirmAuthenticationSuccess(Object userId)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void notifyUserDeauthenticated(Object userId)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void notifyUserTimedOut(Object userId)
    {
        // TODO Auto-generated method stub
    }

    public static class UsernameAlreadyExists extends RuntimeException
    {
        private static final long serialVersionUID = -8416202217479939684L;

        public UsernameAlreadyExists(String username)
        {
            super(username);
        }
    }

    public static class EmailAlreadyExists extends RuntimeException
    {
        private static final long serialVersionUID = 5919844603813070902L;

        public EmailAlreadyExists(String email)
        {
            super(email);
        }
    }

    public static class InvalidEmail extends RuntimeException
    {
        private static final long serialVersionUID = 5765614455487162295L;

        public InvalidEmail(String email)
        {
            super(email);
        }
    }
}
