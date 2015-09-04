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
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.net.UrlEscapers;
import com.google.inject.Inject;
import com.quantis_intl.lcigenerator.dao.LoginDao;
import com.quantis_intl.lcigenerator.mails.MailSender;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.lcigenerator.model.UserPwd;
import com.quantis_intl.login.authentication.LoginService;

public class LoginServiceImpl implements LoginService
{
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static final int MAX_ATTEMPS = 8;
    private static final long LOCKED_DURATION_IN_MINUTES = 20;

    // NOTE: Used to add a bit of security to avoid bruteforce without compromising the source code.
    // TODO: Using HMAC would be even better
    private String secret_salt = "Jo5xNFdSPUmc4ijk2euM";

    private final Provider<LoginDao> dao;

    private final Provider<MailSender> mailSender;

    @Inject
    public LoginServiceImpl(Provider<LoginDao> dao, Provider<MailSender> mailSender)
    {
        this.dao = dao;
        this.mailSender = mailSender;
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

    public boolean mustForcePasswordForUser(int userId)
    {
        UserPwd userPwd = dao.get().getUserPwdFromUserId(userId);
        return userPwd.getForceChangePassword();
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
            failAttemptOfRegisteredUser(userPwd, LoginFailedReason.NON_ACTIVATED_USER);
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
            return userPwd.getLockedSince().plusMinutes(LOCKED_DURATION_IN_MINUTES)
                    .isAfter(LocalDateTime.now(ZoneOffset.UTC));
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
            userPwd.setLockedSince(LocalDateTime.now(ZoneOffset.UTC));
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
        UserPwd userPwd = new UserPwd(0, base64salt, hashString(password, base64salt), false, 0, null, null, null,
                null);

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

    public void changePassword(int userId, String currentPassword, String newPassword)
    {
        UserPwd userPwd = dao.get().getUserPwdFromUserId(userId);
        if (!isRightPassword(currentPassword, userPwd))
        {
            LOG.warn("Change password failed for user {}, wrong current password", userId);
            throw new ChangePasswordFailed(ChangePasswordFailedReason.WRONG_CURRENT_PASSWORD);
        }

        if (!isPasswordComplient(newPassword))
        {
            LOG.warn("Change password failed for user {}, invalid new password", userId);
            throw new ChangePasswordFailed(ChangePasswordFailedReason.INVALID_NEW_PASSWORD);
        }

        userPwd.setPassword(hashString(newPassword, userPwd.getBase64salt()));
        userPwd.setForceChangePassword(false);
        dao.get().updatePassword(userPwd);
        LOG.info("Password changed for user {}", userId);
    }

    protected boolean isPasswordComplient(String password)
    {
        // TODO: Implement password rules
        return password.length() >= 8;
    }

    public void activateUser(String email, String registrationCode, String newPassword)
    {
        User user = dao.get().getUserFromEmail(email);
        if (user == null)
        {
            LOG.error("User tries to activate non existing user: {}", email);
            throw new EmailNotFound(email);
        }

        UserPwd userPwd = dao.get().getUserPwdFromUserId(user.getId());

        if (userPwd.getRegistrationCode() == null)
        {
            LOG.warn("User already activated: {}", user.getId());
            throw new UserAlreadyActivated(user.getId());
        }

        if (!userPwd.getRegistrationCode().equals(registrationCode))
        {
            LOG.error("User tries to activate with wrong registrationCode: {}", registrationCode);
            throw new WrongRegistrationCode();
        }

        if (!isPasswordComplient(newPassword))
        {
            LOG.warn("Change password failed for user {}, invalid new password", user.getId());
            throw new ChangePasswordFailed(ChangePasswordFailedReason.INVALID_NEW_PASSWORD);
        }

        userPwd.setRegistrationCode(null);
        userPwd.setPassword(hashString(newPassword, userPwd.getBase64salt()));
        userPwd.setFailedAttemps(0);
        userPwd.setLockedSince(null);
        dao.get().updatePasswordRegistrationStateAndLockedState(userPwd);

        LOG.info("User activated: {}", user.getId());
    }

    public void forgotPassword(final String email, final String activationUrl)
    {
        User user = dao.get().getUserFromEmail(email);
        if (user == null)
        {
            LOG.error("User tries to request reset password of non existing user: {}", email);
            throw new EmailNotFound(email);
        }

        UserPwd userPwd = dao.get().getUserPwdFromUserId(user.getId());

        if (userPwd.getRegistrationCode() != null)
        {
            LOG.info("Non activated user ({}) trying to reset password. Re-sending activation mail {}", user.getId(),
                    email);
            generateAndSendActivationMail(email, activationUrl, userPwd.getRegistrationCode());
            throw new UserActivationPending();
        }

        String validationCode = generatePassword();
        userPwd.setValidationCode(hashString(validationCode, userPwd.getBase64salt()));
        userPwd.setCodeGeneration(LocalDateTime.now(ZoneOffset.UTC));
        dao.get().updateValidationCode(userPwd);

        mailSender.get().sendMail(email, "[ALCIG] Password reset request",
                getTextForResetPasswordEmail(validationCode));

        LOG.info("Reset password request received for user: {}", user.getId());
    }

    protected void generateAndSendActivationMail(String email, String activationUrl, String registrationCode)
    {
        String activationLink = activationUrl + "?email=" + UrlEscapers.urlFormParameterEscaper().escape(email)
                + "&registrationCode=" + registrationCode;
        // TODO: Future to know if mail was sent or not, change request status if error
        mailSender.get().sendMail(email,
                "[ALCIG] Activate your access",
                getTextForActivationEmail(activationLink));
    }

    // TODO: Use a template to fill
    protected String getTextForActivationEmail(String activationLink)
    {
        StringBuilder sb = new StringBuilder("Dear user,<br/><br/>")
                .append("Thank you for your interest in ALCIG.<br/>Please activate your access by clicking on the link below:<br/>")
                .append("<a href='")
                .append(activationLink)
                .append("' target='_blank'>")
                .append(activationLink)
                .append("</a><br/><br/>")
                .append("Best regards,<br/><br/>")
                .append("The ALCIG team");

        return sb.toString();
    }

    // TODO: Use a template to fill
    protected String getTextForResetPasswordEmail(String validationCode)
    {
        StringBuilder sb = new StringBuilder("Dear user,<br/><br/>")
                .append("We received a request to reset the password associated with this email address. If you are at the origin of this request, please follow the instructions below.<br/>")
                .append("Enter this validation code to the displayed pop-up in ALCIG: <b>")
                .append(validationCode)
                .append("</b><br/>")
                .append("After 15 minutes, this code will expire and you will have to restart the procedure.<br/><br/>")
                .append("If you did not request to reset your password, you can ignore this email. The security of your account is insured.");

        return sb.toString();
    }

    public void resetPassword(String email, String validationCode, String newPassword)
    {
        User user = dao.get().getUserFromEmail(email);
        if (user == null)
        {
            LOG.error("User tries to reset password of non existing user: {}", email);
            throw new EmailNotFound(email);
        }

        UserPwd userPwd = dao.get().getUserPwdFromUserId(user.getId());

        if (userPwd.getRegistrationCode() != null)
        {
            LOG.error("Non activated user trying to reset password: {}", user.getId());
            throw new UserActivationPending();
        }

        if (validationCode == null
                || !hashString(validationCode, userPwd.getBase64salt()).equals(userPwd.getValidationCode()))
        {
            LOG.warn("Invalid validation code for user {}", user.getId());
            throw new ResetPasswordFailed(ResetPasswordFailedReason.WRONG_VALIDATION_CODE);
        }

        final long VALIDATION_CODE_VALIDITY_IN_MINUTES = 15;
        if (userPwd.getCodeGeneration().plusMinutes(VALIDATION_CODE_VALIDITY_IN_MINUTES)
                .isBefore(LocalDateTime.now(ZoneOffset.UTC)))
        {
            LOG.warn("Code validation timeout for user {}", user.getId());
            throw new ResetPasswordFailed(ResetPasswordFailedReason.EXPIRED_VALIDATION_CODE);
        }

        if (!isPasswordComplient(newPassword))
        {
            LOG.error("Reset password failed for user {}, invalid new password", user.getId());
            throw new ResetPasswordFailed(ResetPasswordFailedReason.INVALID_NEW_PASSWORD);
        }

        userPwd.setPassword(hashString(newPassword, userPwd.getBase64salt()));
        userPwd.setForceChangePassword(false);
        userPwd.setCodeGeneration(null);
        userPwd.setValidationCode(null);
        userPwd.setFailedAttemps(0);
        userPwd.setLockedSince(null);
        dao.get().updatePasswordValidationAndLockedStatus(userPwd);
        LOG.info("Password reset for user {}", user.getId());
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

    public static class EmailNotFound extends RuntimeException
    {
        private static final long serialVersionUID = 4878827001016417035L;

        public EmailNotFound(String email)
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

    public static enum ChangePasswordFailedReason
    {
        WRONG_CURRENT_PASSWORD, INVALID_NEW_PASSWORD
    }

    public static class ChangePasswordFailed extends RuntimeException
    {
        private static final long serialVersionUID = 3828020110649046469L;
        public final ChangePasswordFailedReason reason;

        public ChangePasswordFailed(ChangePasswordFailedReason reason)
        {
            super(reason.toString());
            this.reason = reason;
        }
    }

    public static class UserAlreadyActivated extends RuntimeException
    {
        private static final long serialVersionUID = -533241253171479362L;

        public UserAlreadyActivated(int userId)
        {
            super(Integer.toString(userId));
        }
    }

    public static class WrongRegistrationCode extends RuntimeException
    {
        private static final long serialVersionUID = -4665610085469753248L;
    }

    public static class UserActivationPending extends RuntimeException
    {
        private static final long serialVersionUID = 4155124872930311659L;
    }

    public static enum ResetPasswordFailedReason
    {
        WRONG_VALIDATION_CODE, EXPIRED_VALIDATION_CODE, INVALID_NEW_PASSWORD
    }

    public static class ResetPasswordFailed extends RuntimeException
    {
        private static final long serialVersionUID = 3424809375916565777L;
        public final ResetPasswordFailedReason reason;

        public ResetPasswordFailed(ResetPasswordFailedReason reason)
        {
            super(reason.toString());
            this.reason = reason;
        }
    }
}
