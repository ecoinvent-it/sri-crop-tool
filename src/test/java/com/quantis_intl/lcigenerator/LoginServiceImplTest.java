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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;

import com.quantis_intl.lcigenerator.LoginServiceImpl.ChangePasswordFailed;
import com.quantis_intl.lcigenerator.LoginServiceImpl.ChangePasswordFailedReason;
import com.quantis_intl.lcigenerator.LoginServiceImpl.EmailAlreadyExists;
import com.quantis_intl.lcigenerator.LoginServiceImpl.InvalidEmail;
import com.quantis_intl.lcigenerator.LoginServiceImpl.UsernameAlreadyExists;
import com.quantis_intl.lcigenerator.dao.LoginDao;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.lcigenerator.model.UserPwd;
import com.quantis_intl.stack.authentication.LoginService;

public class LoginServiceImplTest
{
    private LoginDaoForTest loginDao = new LoginDaoForTest();
    private LoginServiceImpl loginService = new LoginServiceImpl(new Provider<LoginDao>()
    {
        @Override
        public LoginDao get()
        {
            return loginDao;
        }
    });

    static final String CORRECT_BASE64_SALT = "hQbHYPFuOxvlKDO1tIeN9IloV3Rtt8EHt1Z2SSGX1CV1";
    static final String CORRECT_HASHED_PWD = "sha512:eb62accaa6af446fff312872cae3e7e0e2616245c3fb964a6c5a89fc275351df9e2392e83ce014de1c42fa47f5e822ae5c2deaf807aa8cb01cc1767c6c180fb2";
    static final String CORRECT_PWD = "A.4VpADL2m8";

    static final int DEFAULT_USER_ID = 1;
    static final String DEFAULT_USERNAME = "existingUsername";
    static final String DEFAULT_EMAIL = "existing@mail.com";

    public User buildDefaultUser()
    {
        return new User(DEFAULT_USER_ID, DEFAULT_USERNAME, DEFAULT_EMAIL);
    }

    public UserPwd buildDefaultUserPwd()
    {
        return new UserPwd(DEFAULT_USER_ID, CORRECT_BASE64_SALT, CORRECT_HASHED_PWD,
                false, 0, null, null, null, null);
    }

    @Before
    public void beforeEachTest()
    {
        loginDao.reset();
    }

    @Test
    public void getAuthenticUserId_ShouldPass()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        Object userId = loginService.getAuthenticUserId(DEFAULT_USERNAME, CORRECT_PWD);
        assertEquals(DEFAULT_USER_ID, userId);
    }

    @Test
    public void getAuthenticUserId_ShouldForbidWrongPassword()
    {
        final String username = "withNoFailedAttemp";
        User user = new User(1, username, "");
        UserPwd userPwd = buildDefaultUserPwd();
        loginDao.createUser(user, userPwd);

        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            assertNotNull(null);
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.WRONG_CREDENTIALS, e.reason);
            User persistedUser = loginDao.getUserFromUsername(username);
            UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
            assertEquals(1, persistedPwd.getFailedAttemps());
            assertEquals(null, persistedPwd.getLockedSince());
        }
    }

    @Test
    public void getAuthenticUserId_ShouldLock()
    {
        final String username = "userWithAlmostMaxAttemps";
        User user = new User(1, username, "");
        UserPwd userPwd = buildDefaultUserPwd();
        userPwd.setFailedAttemps(7);
        loginDao.createUser(user, userPwd);

        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            assertNotNull(null);
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.WRONG_CREDENTIALS, e.reason);
            User persistedUser = loginDao.getUserFromUsername(username);
            UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
            assertEquals("fail attemps must be incremented", 8, persistedPwd.getFailedAttemps());
            assertNotNull("locked since must not be null", persistedPwd.getLockedSince());
        }
    }

    @Test
    public void getAuthenticUserId_ShouldFail_AlreadyLocked()
    {
        final String username = "userNewlyLocked";
        User user = new User(1, username, "");
        UserPwd userPwd = buildDefaultUserPwd();
        userPwd.setFailedAttemps(8);
        userPwd.setLockedSince(new Date());
        loginDao.createUser(user, userPwd);

        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            assertNotNull(null);
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.LOCKED_USER, e.reason);
            User persistedUser = loginDao.getUserFromUsername(username);
            UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
            assertEquals("fail attemps must be incremented", 9, persistedPwd.getFailedAttemps());
            assertNotNull("locked since must not be null", persistedPwd.getLockedSince());
        }
    }

    @Test
    public void getAuthenticUserId_ShouldPassAndUnlock()
    {
        final String username = "userLockedSinceALongTime";
        User user = new User(1, username, "");
        UserPwd userPwd = buildDefaultUserPwd();
        userPwd.setFailedAttemps(8);
        userPwd.setLockedSince(new Date(123));
        loginDao.createUser(user, userPwd);

        loginService.getAuthenticUserId(username, CORRECT_PWD);

        User persistedUser = loginDao.getUserFromUsername(username);
        UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
        assertEquals("failed attemps must be reset to zero", 0, persistedPwd.getFailedAttemps());
        assertEquals("locked since must be reset to null", null, persistedPwd.getLockedSince());
    }

    @Test
    public void getAuthenticUserId_ShouldForbidUnknownUsername()
    {
        try
        {
            loginService.getAuthenticUserId("test", "test");
            assertNotNull(null);
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.USER_NOT_FOUND, e.reason);
        }
    }

    @Test
    public void getAuthenticUserId_ShouldForbidNotValidatedUser()
    {
        final String username = "userLockedSinceALongTime";
        User user = new User(1, username, "");
        UserPwd userPwd = buildDefaultUserPwd();
        userPwd.setRegistrationCode("registrationCode");
        loginDao.createUser(user, userPwd);

        try
        {
            loginService.getAuthenticUserId(username, CORRECT_PWD);
            assertNotNull(null);
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.NON_VALIDATED_USER, e.reason);
        }
    }

    @Test
    public void createUser_ShouldPass()
    {
        final String username = "newUser";
        User newUser = new User(1, username, "ewrt@ewrt.com");
        loginService.createUser(newUser);

        User persistedUser = loginDao.getUserFromUsername(username);
        assertNotNull("user not persisted", persistedUser);
        assertEquals("emails not equal", newUser.getEmail(), persistedUser.getEmail());
        assertNotNull("pwd not persisted", loginDao.getUserPwdFromUserId(persistedUser.getId()));
    }

    @Test(expected = InvalidEmail.class)
    public void createUser_shouldForbidInvalidEmail()
    {
        final String username = "newUserWithInvalidEmail";
        User newUser = new User(1, username, "notValidEmail");
        loginService.createUser(newUser);
    }

    @Test(expected = UsernameAlreadyExists.class)
    public void createUser_shouldForbidExistingUsername()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        User newUser = new User(2, DEFAULT_USERNAME, "wert@dsfg.co");
        loginService.createUser(newUser);
    }

    @Test(expected = EmailAlreadyExists.class)
    public void createUser_shouldForbidExistingEmail()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        final String username = "newUserWithExistingEmail";
        User newUser = new User(2, username, DEFAULT_EMAIL);
        loginService.createUser(newUser);
    }

    @Test
    public void mustForcePasswordForUser_ShouldBeTrue()
    {
        UserPwd userPwd = buildDefaultUserPwd();
        userPwd.setForceChangePassword(true);
        loginDao.createUser(buildDefaultUser(), userPwd);

        assertTrue("should be true", loginService.mustForcePasswordForUser(DEFAULT_USER_ID));
    }

    @Test
    public void mustForcePasswordForUser_ShouldBeFalse()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        assertFalse("should be true", loginService.mustForcePasswordForUser(DEFAULT_USER_ID));
    }

    @Test
    public void changePassword_ShouldPass()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        loginService.changePassword(DEFAULT_USER_ID, CORRECT_PWD, "12345678");

        UserPwd newUserPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertNotEquals("hashedPassword should have changed", CORRECT_HASHED_PWD, newUserPwd.getPassword());
    }

    @Test
    public void changePassword_ShouldPass_ForcedPassword()
    {
        UserPwd userPwd = buildDefaultUserPwd();
        userPwd.setForceChangePassword(true);
        loginDao.createUser(buildDefaultUser(), userPwd);

        loginService.changePassword(DEFAULT_USER_ID, CORRECT_PWD, "12345678");

        UserPwd newUserPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertFalse("hashedPassword should have changed", newUserPwd.getForceChangePassword());
    }

    @Test
    public void changePassword_ShouldForbidTooShortNewPassword()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        try
        {
            loginService.changePassword(DEFAULT_USER_ID, CORRECT_PWD, "1234567");
            assertNotNull(null);
        }
        catch (ChangePasswordFailed e)
        {
            assertEquals("wrong reason", ChangePasswordFailedReason.INVALID_NEW_PASSWORD, e.reason);
        }
    }

    @Test
    public void changePassword_ShouldFail_WrongCurrentPassword()
    {
        loginDao.createUser(buildDefaultUser(), buildDefaultUserPwd());

        try
        {
            loginService.changePassword(DEFAULT_USER_ID, "wrongPassword", "12345678");
            assertNotNull(null);
        }
        catch (ChangePasswordFailed e)
        {
            assertEquals("wrong reason", ChangePasswordFailedReason.WRONG_CURRENT_PASSWORD, e.reason);
        }
    }

    private class LoginDaoForTest implements LoginDao
    {
        private Map<String, User> usersFromUsername = new HashMap<>();
        private Map<Integer, User> usersFromId = new HashMap<>();
        private Map<String, User> usersFromEmail = new HashMap<>();
        private Map<Integer, UserPwd> userPwdsFormId = new HashMap<>();

        @Override
        public User getUserFromId(int id)
        {
            return usersFromId.get(id);
        }

        @Override
        public User getUserFromUsername(String username)
        {
            return usersFromUsername.get(username);
        }

        @Override
        public void createUser(User user, UserPwd userPwd)
        {
            usersFromUsername.put(user.getUsername(), user);
            usersFromId.put(user.getId(), user);
            usersFromEmail.put(user.getEmail(), user);
            userPwdsFormId.put(user.getId(), userPwd);
        }

        @Override
        public void updateUserInfo(User user)
        {
            User persistedUser = getUserFromId(user.getId());
            persistedUser.setEmail(user.getEmail());
        }

        @Override
        public UserPwd getUserPwdFromUserId(int id)
        {
            return userPwdsFormId.get(id);
        }

        @Override
        public void updateLockedState(UserPwd userPwd)
        {
            UserPwd persistedUserPwd = getUserPwdFromUserId(userPwd.getUserId());
            persistedUserPwd.setFailedAttemps(userPwd.getFailedAttemps());
            persistedUserPwd.setLockedSince(userPwd.getLockedSince());
        }

        @Override
        public void updatePassword(UserPwd userPwd)
        {
            UserPwd persistedUserPwd = getUserPwdFromUserId(userPwd.getUserId());
            persistedUserPwd.setPassword(userPwd.getPassword());
            persistedUserPwd.setForceChangePassword(userPwd.getForceChangePassword());
        }

        @Override
        public User getUserFromEmail(String email)
        {
            return usersFromEmail.get(email);
        }

        public void reset()
        {
            usersFromUsername.clear();
            usersFromId.clear();
            usersFromEmail.clear();
            userPwdsFormId.clear();
        }
    }
}
