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
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;

import com.quantis_intl.lcigenerator.LoginServiceImpl.ChangePasswordFailed;
import com.quantis_intl.lcigenerator.LoginServiceImpl.ChangePasswordFailedReason;
import com.quantis_intl.lcigenerator.LoginServiceImpl.EmailAlreadyExists;
import com.quantis_intl.lcigenerator.LoginServiceImpl.EmailNotFound;
import com.quantis_intl.lcigenerator.LoginServiceImpl.InvalidEmail;
import com.quantis_intl.lcigenerator.LoginServiceImpl.ResetPasswordFailed;
import com.quantis_intl.lcigenerator.LoginServiceImpl.ResetPasswordFailedReason;
import com.quantis_intl.lcigenerator.LoginServiceImpl.UserActivationPending;
import com.quantis_intl.lcigenerator.LoginServiceImpl.UserAlreadyActivated;
import com.quantis_intl.lcigenerator.LoginServiceImpl.UsernameAlreadyExists;
import com.quantis_intl.lcigenerator.LoginServiceImpl.WrongRegistrationCode;
import com.quantis_intl.lcigenerator.dao.LoginDao;
import com.quantis_intl.lcigenerator.mails.MailSender;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.lcigenerator.model.UserPwd;
import com.quantis_intl.login.authentication.LoginService;

public class LoginServiceImplTest
{
    private LoginDaoForTest loginDao;
    private MailSenderForTest mailSender;
    private LoginServiceImpl loginService;

    static private final int DEFAULT_USER_ID = 1;
    static private final int FORCE_PWD_USER_ID = 7;

    static private final String CORRECT_BASE64_SALT = "hQbHYPFuOxvlKDO1tIeN9IloV3Rtt8EHt1Z2SSGX1CV1";
    static private final String CORRECT_HASHED_PWD = "sha512:ef2c6b46d5d9de57feb791942de4b3f34cbc0a659aee2471abe1d145e9d521e6be37597c1a42d3b68d9c8cc3c3468c022e741d054983ff0a9eaf4129acab8f0a";
    static private final String DEFAULT_HASHED_VALIDATION_CODE = "sha512:da0bc1be2f537e217ab317153f47ed91390f37684952953ddb784cf9c001fbb2fae476bb3a572779a9edc590e3b56a0a58fbd0475dab8912f453680f6a0cf5db";

    @Before
    public void beforeEachTest()
    {
        loginDao = new LoginDaoForTest();
        mailSender = new MailSenderForTest("", "", "");
        loginService = new LoginServiceImpl(new Provider<LoginDao>()
        {
            @Override
            public LoginDao get()
            {
                return loginDao;
            }
        }, new Provider<MailSender>()
        {
            @Override
            public MailSender get()
            {
                return mailSender;
            }
        });
    }

    @Test
    public void normalUser_ShouldLogin()
    {
        Object userId = loginService.getAuthenticUserId("existingUsername", "correctPwd");
        assertEquals(DEFAULT_USER_ID, userId);
    }

    @Test
    public void wrongPassword_ShouldNotLogin()
    {
        final String username = "withNoFailedAttemp";
        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.WRONG_CREDENTIALS, e.reason);
        }
    }

    @Test
    public void wrongPassword_ShouldUpdateLockedStateOnLogin()
    {
        final String username = "withNoFailedAttemp";
        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            User persistedUser = loginDao.getUserFromUsername(username);
            UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
            assertEquals(1, persistedPwd.getFailedAttemps());
            assertEquals(null, persistedPwd.getLockedSince());
        }
    }

    @Test
    public void wrongPasswordWithAlmostMaxAttemps_ShouldLockOnLogin()
    {
        final String username = "userWithAlmostMaxAttemps";

        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            User persistedUser = loginDao.getUserFromUsername(username);
            UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
            assertEquals("fail attemps must be incremented", 8, persistedPwd.getFailedAttemps());
            assertNotNull("locked since must not be null", persistedPwd.getLockedSince());
        }
    }

    @Test
    public void lockedUserRecent_ShouldNotLogin()
    {
        final String username = "userNewlyLocked";

        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.LOCKED_USER, e.reason);
        }
    }

    @Test
    public void lockedUserRecent_ShouldUpdateLockedStateOnLogin()
    {
        final String username = "userNewlyLocked";

        try
        {
            loginService.getAuthenticUserId(username, "wrongpassword");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            User persistedUser = loginDao.getUserFromUsername(username);
            UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
            assertEquals("fail attemps must be incremented", 9, persistedPwd.getFailedAttemps());
            assertNotNull("locked since must not be null", persistedPwd.getLockedSince());
        }
    }

    @Test
    public void lockedUserOld_ShouldLogin()
    {
        final String username = "userLockedSinceALongTime";

        loginService.getAuthenticUserId(username, "correctPwd");
    }

    @Test
    public void lockedUserOld_ShouldUnlockOnLogin()
    {
        final String username = "userLockedSinceALongTime";

        loginService.getAuthenticUserId(username, "correctPwd");

        User persistedUser = loginDao.getUserFromUsername(username);
        UserPwd persistedPwd = loginDao.getUserPwdFromUserId(persistedUser.getId());
        assertEquals("failed attemps must be reset to zero", 0, persistedPwd.getFailedAttemps());
        assertEquals("locked since must be reset to null", null, persistedPwd.getLockedSince());
    }

    @Test
    public void unknownUsername_ShouldNotLogin()
    {
        try
        {
            loginService.getAuthenticUserId("test", "test");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.USER_NOT_FOUND, e.reason);
        }
    }

    @Test
    public void nonActivatedUser_ShouldNotLogin()
    {
        final String username = "notActivatedUser";

        try
        {
            loginService.getAuthenticUserId(username, "correctPwd");
            fail();
        }
        catch (LoginService.LoginFailed e)
        {
            assertEquals("exception with wrong reason", LoginService.LoginFailedReason.NON_ACTIVATED_USER, e.reason);
        }
    }

    @Test
    public void normalUser_ShouldBePersistedOnUserCreation()
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
    public void invalidEmail_ShouldNotCreateUser()
    {
        final String username = "newUserWithInvalidEmail";
        User newUser = new User(1, username, "notValidEmail");
        loginService.createUser(newUser);
    }

    @Test(expected = UsernameAlreadyExists.class)
    public void existingUsername_ShouldNotCreateUser()
    {
        User newUser = new User(2, "existingUsername", "wert@dsfg.co");
        loginService.createUser(newUser);
    }

    @Test(expected = EmailAlreadyExists.class)
    public void existingEmail_ShouldNotCreateUser()
    {
        final String username = "newUserWithExistingEmail";
        User newUser = new User(2, username, "existing@mail.com");
        loginService.createUser(newUser);
    }

    @Test
    public void userWithForcePassword_ForcePasswordShouldBeTrue()
    {
        assertTrue("should be true", loginService.mustForcePasswordForUser(FORCE_PWD_USER_ID));
    }

    @Test
    public void normalUser_ForcePasswordShouldBeFalse()
    {
        assertFalse("should be true", loginService.mustForcePasswordForUser(DEFAULT_USER_ID));
    }

    @Test
    public void normalUser_ShouldChangePassword()
    {
        loginService.changePassword(DEFAULT_USER_ID, "correctPwd", "correctNewPassword");

        UserPwd newUserPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertNotEquals("hashedPassword should have changed", CORRECT_HASHED_PWD, newUserPwd.getPassword());
    }

    @Test
    public void userWithForcePassword_ShouldChangePassword()
    {
        loginService.changePassword(FORCE_PWD_USER_ID, "correctPwd", "correctNewPassword");

        UserPwd newUserPwd = loginDao.getUserPwdFromUserId(FORCE_PWD_USER_ID);
        assertFalse("hashedPassword should have changed", newUserPwd.getForceChangePassword());
    }

    @Test
    public void tooShortNewPassword_ShouldNotChangePassword()
    {
        try
        {
            loginService.changePassword(DEFAULT_USER_ID, "correctPwd", "badPwd");
            fail();
        }
        catch (ChangePasswordFailed e)
        {
            assertEquals("wrong reason", ChangePasswordFailedReason.INVALID_NEW_PASSWORD, e.reason);
        }
    }

    @Test
    public void wrongPassword_ShouldNotChangePassword()
    {
        try
        {
            loginService.changePassword(DEFAULT_USER_ID, "wrongPassword", "correctNewPassword");
            fail();
        }
        catch (ChangePasswordFailed e)
        {
            assertEquals("wrong reason", ChangePasswordFailedReason.WRONG_CURRENT_PASSWORD, e.reason);
        }
    }

    @Test(expected = EmailNotFound.class)
    public void unknownEmail_ShouldNotActivateUser()
    {
        loginService.activateUser("unknown", "registrationCode", "correctPwd");
    }

    @Test(expected = UserAlreadyActivated.class)
    public void alreadyActivatedUser_ShouldNotActivateUser()
    {
        loginService.activateUser("existing@mail.com", "registrationCode", "correctPwd");
    }

    @Test(expected = WrongRegistrationCode.class)
    public void unknownRegistrationCode_ShouldNotActivateUser()
    {
        loginService.activateUser("notActivatedUser@a.com", "unknownRegistrationCode", "correctPwd");
    }

    @Test(expected = ChangePasswordFailed.class)
    public void tooShortNewPassword_ShouldNotActivateUser()
    {
        loginService.activateUser("notActivatedUser@a.com", "registrationCode", "pwd");
    }

    @Test
    public void nonActivatedUser_ShouldActivateUser()
    {
        loginService.activateUser("notActivatedUser@a.com", "registrationCode", "correctPwd");

        UserPwd persistedUserPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertEquals("registrationCode must be reset", null, persistedUserPwd.getRegistrationCode());
        assertEquals("password must be changed", CORRECT_HASHED_PWD, persistedUserPwd.getPassword());
    }

    @Test
    public void nonActivatedUser_ShouldChangeActivationStatusOnUserActivation()
    {
        loginService.activateUser("notActivatedUser@a.com", "registrationCode", "correctPwd");

        UserPwd persistedUserPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertEquals("registrationCode must be reset", null, persistedUserPwd.getRegistrationCode());
        assertEquals("password must be changed", CORRECT_HASHED_PWD, persistedUserPwd.getPassword());
    }

    @Test(expected = EmailNotFound.class)
    public void unknownEmail_ShouldNotEnableForgotPasswordFeature()
    {
        loginService.forgotPassword("unknown@a.com", "");
    }

    @Test(expected = UserActivationPending.class)
    public void nonActivatedUser_ShouldNotEnableForgotPasswordFeature()
    {
        loginService.forgotPassword("notActivatedUser@a.com", "");
    }

    @Test
    public void nonActivatedUser_ShouldResendActivationEmailWhenForgotPassword()
    {
        try
        {
            loginService.forgotPassword("notActivatedUser@a.com", "");
            fail();
        }
        catch (UserActivationPending e)
        {
            assertEquals("email not sent", 1, mailSender.messages.size());
            String[] message = mailSender.messages.get(0);
            assertEquals("wrong recipient", "notActivatedUser@a.com", message[0]);
            assertTrue("Wrong title", message[1].contains("Activate"));
        }
    }

    @Test
    public void normalUser_ShouldEnableForgotPasswordFeature()
    {
        final String email = "existing@mail.com";
        loginService.forgotPassword(email, "");
    }

    @Test
    public void normalUser_ShouldSendValidationEmailWhenForgotPassword()
    {
        final String email = "existing@mail.com";
        loginService.forgotPassword(email, "");

        assertEquals("email not sent", 1, mailSender.messages.size());
        String[] message = mailSender.messages.get(0);
        assertEquals("wrong recipient", email, message[0]);
        assertTrue("Wrong title", message[1].contains("Password reset"));
    }

    @Test
    public void normalUser_ShouldChangeValidationStatusWhenForgotPassword()
    {
        final String email = "existing@mail.com";
        loginService.forgotPassword(email, "");

        UserPwd persistedPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertNotNull("validation code should not be null", persistedPwd.getValidationCode());
        assertNotNull("code generation should not be null", persistedPwd.getCodeGeneration());
    }

    @Test(expected = EmailNotFound.class)
    public void unknownEmail_ShouldNotResetPassword()
    {
        loginService.resetPassword("unknownMail", "correctValidationCode", "correctPwd");
    }

    @Test(expected = UserActivationPending.class)
    public void nonActivatedUser_ShouldNotResetPassword()
    {
        loginService.resetPassword("notActivatedUser@a.com", "correctValidationCode", "correctPwd");
    }

    @Test
    public void wrongValidationCode_ShouldNotResetPassword()
    {
        try
        {
            loginService.resetPassword("userWithValidationCode@a.com", "wrongValidationCode", "correctPwd");
            fail();
        }
        catch (ResetPasswordFailed e)
        {
            assertEquals("wrong reason", ResetPasswordFailedReason.WRONG_VALIDATION_CODE, e.reason);
        }
    }

    @Test
    public void tooShortNewPassword_ShouldNotResetPassword()
    {
        try
        {
            loginService.resetPassword("userWithValidationCode@a.com", "correctValidationCode", "badPwd");
            fail();
        }
        catch (ResetPasswordFailed e)
        {
            assertEquals("wrong reason", ResetPasswordFailedReason.INVALID_NEW_PASSWORD, e.reason);
        }
    }

    @Test
    public void expiredValidationCode_ShouldNotResetPassword()
    {
        try
        {
            loginService.resetPassword("validationCodeTimeOut@c.com", "correctValidationCode", "correctPwd");
            fail();
        }
        catch (ResetPasswordFailed e)
        {
            assertEquals("wrong reason", ResetPasswordFailedReason.EXPIRED_VALIDATION_CODE, e.reason);
        }
    }

    @Test
    public void normalUser_ShouldResetPasswordStatusOnResetPassword()
    {
        loginService.resetPassword("userWithValidationCode@a.com", "correctValidationCode", "correctPwd");

        UserPwd persistedPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertEquals("password wasn't changed", CORRECT_HASHED_PWD, persistedPwd.getPassword());
        assertFalse("forceChangePassword should be reset", persistedPwd.getForceChangePassword());
    }

    @Test
    public void normalUser_ShouldResetValidationCodeStatusOnResetPassword()
    {
        loginService.resetPassword("userWithValidationCode@a.com", "correctValidationCode", "correctPwd");

        UserPwd persistedPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertEquals("validation code should be reset", null, persistedPwd.getValidationCode());
        assertEquals("code generation should be reset", null, persistedPwd.getCodeGeneration());
        assertEquals("password wasn't changed", CORRECT_HASHED_PWD, persistedPwd.getPassword());
        assertEquals("failed attemps should be reset", 0, persistedPwd.getFailedAttemps());
        assertEquals("locked since should be reset", null, persistedPwd.getLockedSince());
        assertFalse("forceChangePassword should be reset", persistedPwd.getForceChangePassword());
    }

    @Test
    public void lockedUser_ShouldResetLockedStatusOnResetPassword()
    {
        loginService.resetPassword("lockedUserWithValidationCode@a.com", "correctValidationCode", "correctPwd");

        UserPwd persistedPwd = loginDao.getUserPwdFromUserId(DEFAULT_USER_ID);
        assertEquals("failed attemps should be reset", 0, persistedPwd.getFailedAttemps());
        assertEquals("locked since should be reset", null, persistedPwd.getLockedSince());
    }

    private class LoginDaoForTest implements LoginDao
    {
        private Map<String, User> usersFromUsername = new HashMap<>();
        private Map<Integer, User> usersFromId = new HashMap<>();
        private Map<String, User> usersFromEmail = new HashMap<>();
        private Map<Integer, UserPwd> userPwdsFormId = new HashMap<>();

        public LoginDaoForTest()
        {
            createUser(new User(DEFAULT_USER_ID, "existingUsername", "existing@mail.com"),
                    buildDefaultUserPwd(DEFAULT_USER_ID));
            createUser(new User(2, "withNoFailedAttemp", "withNoFailedAttemp@a.com"), buildDefaultUserPwd(2));
            createUser(new User(3, "userWithAlmostMaxAttemps", "userWithAlmostMaxAttemps@a.com"),
                    buildAlmostMaxAttempsPwd(3));
            createUser(new User(4, "userNewlyLocked", "userNewlyLocked@a.com"),
                    buildLockedPwd(4, LocalDateTime.now(ZoneOffset.UTC).minusMinutes(5)));
            createUser(new User(5, "userLockedSinceALongTime", "userLockedSinceALongTime@a.com"),
                    buildLockedPwd(5, LocalDateTime.now(ZoneOffset.UTC).minusMinutes(21)));
            createUser(new User(6, "notActivatedUser", "notActivatedUser@a.com"), buildNotActivatedPwd(6));
            createUser(new User(FORCE_PWD_USER_ID, "userWithForcePwd", "userWithForcePwd@a.com"),
                    buildForceChangePwd(FORCE_PWD_USER_ID));
            createUser(new User(8, "validationCodeTimeOut", "validationCodeTimeOut@c.com"),
                    buildDefaultUserPwdToReset(8, LocalDateTime.now(ZoneOffset.UTC).minusMinutes(16)));
            createUser(new User(9, "userWithValidationCode", "userWithValidationCode@a.com"),
                    buildDefaultUserPwdToReset(9, LocalDateTime.now(ZoneOffset.UTC)));
            createUser(new User(10, "lockedUserWithValidationCode", "lockedUserWithValidationCode@a.com"),
                    buildDefaultUserPwdToReset(10, LocalDateTime.now(ZoneOffset.UTC)));
        }

        private UserPwd buildDefaultUserPwd(int id)
        {
            return new UserPwd(id, CORRECT_BASE64_SALT, CORRECT_HASHED_PWD,
                    false, 0, null, null, null, null);
        }

        private UserPwd buildAlmostMaxAttempsPwd(int id)
        {
            UserPwd userPwd = buildDefaultUserPwd(id);
            userPwd.setFailedAttemps(7);
            return userPwd;
        }

        private UserPwd buildLockedPwd(int id, LocalDateTime date)
        {
            UserPwd userPwd = buildDefaultUserPwd(id);
            userPwd.setFailedAttemps(8);
            userPwd.setLockedSince(date);
            return userPwd;
        }

        private UserPwd buildNotActivatedPwd(int id)
        {
            UserPwd userPwd = buildDefaultUserPwd(id);
            userPwd.setRegistrationCode("registrationCode");
            return userPwd;
        }

        private UserPwd buildForceChangePwd(int id)
        {
            UserPwd userPwd = buildDefaultUserPwd(id);
            userPwd.setForceChangePassword(true);
            return userPwd;
        }

        private UserPwd buildDefaultUserPwdToReset(int id, LocalDateTime codeGenerationDate)
        {
            UserPwd userPwd = buildDefaultUserPwd(id);
            userPwd.setFailedAttemps(9);
            userPwd.setLockedSince(LocalDateTime.of(2014, 1, 1, 0, 0, 0));
            userPwd.setValidationCode(DEFAULT_HASHED_VALIDATION_CODE);
            userPwd.setCodeGeneration(codeGenerationDate);
            return userPwd;
        }

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
            User persistedUser = new User(user.getId(), user.getUsername(), user.getEmail());
            UserPwd persistedUserPwd = new UserPwd(userPwd.getUserId(), userPwd.getBase64salt(),
                    userPwd.getPassword(), userPwd.getForceChangePassword(),
                    userPwd.getFailedAttemps(), userPwd.getLockedSince(),
                    userPwd.getRegistrationCode(), userPwd.getValidationCode(),
                    userPwd.getCodeGeneration());
            usersFromUsername.put(user.getUsername(), persistedUser);
            usersFromId.put(user.getId(), persistedUser);
            usersFromEmail.put(user.getEmail(), persistedUser);
            userPwdsFormId.put(user.getId(), persistedUserPwd);
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

        @Override
        public void updateValidationCode(UserPwd userPwd)
        {
            UserPwd persistedUserPwd = getUserPwdFromUserId(userPwd.getUserId());
            persistedUserPwd.setValidationCode(userPwd.getValidationCode());
            persistedUserPwd.setCodeGeneration(userPwd.getCodeGeneration());
        }

        @Override
        public void updatePasswordRegistrationStateAndLockedState(UserPwd userPwd)
        {
            UserPwd persistedUserPwd = getUserPwdFromUserId(userPwd.getUserId());
            persistedUserPwd.setPassword(userPwd.getPassword());
            persistedUserPwd.setRegistrationCode(userPwd.getRegistrationCode());
            persistedUserPwd.setFailedAttemps(userPwd.getFailedAttemps());
            persistedUserPwd.setLockedSince(userPwd.getLockedSince());
        }

        @Override
        public void updatePasswordValidationAndLockedStatus(UserPwd userPwd)
        {
            UserPwd persistedUserPwd = getUserPwdFromUserId(userPwd.getUserId());
            persistedUserPwd.setPassword(userPwd.getPassword());
            persistedUserPwd.setForceChangePassword(userPwd.getForceChangePassword());
            persistedUserPwd.setCodeGeneration(userPwd.getCodeGeneration());
            persistedUserPwd.setValidationCode(userPwd.getValidationCode());
            persistedUserPwd.setFailedAttemps(userPwd.getFailedAttemps());
            persistedUserPwd.setLockedSince(userPwd.getLockedSince());
        }
    }

    private class MailSenderForTest extends MailSender
    {
        List<String[]> messages = new ArrayList<>();

        public MailSenderForTest(String mailFrom, String mailerUsername, String mailerPassword)
        {
            super(mailFrom, mailerUsername, mailerPassword);
        }

        @Override
        public void sendMail(String recipients, String title, String text)
        {
            String[] message = { recipients, title, text };
            messages.add(message);
        }
    }
}
