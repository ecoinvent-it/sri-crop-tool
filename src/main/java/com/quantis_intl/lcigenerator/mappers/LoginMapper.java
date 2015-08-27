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
package com.quantis_intl.lcigenerator.mappers;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.google.common.collect.ImmutableList;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.lcigenerator.model.UserPwd;
import com.quantis_intl.stack.mybatis.QsSQL;

public interface LoginMapper
{
    @SelectProvider(type = UserQueryBuilder.class, method = "selectFromId")
    User getUserFromId(@Param(UserQueryBuilder.FIELD_ID) int id);

    @SelectProvider(type = UserQueryBuilder.class, method = "selectFromUsername")
    User getUserFromUsername(@Param(UserQueryBuilder.FIELD_USERNAME) String username);

    @SelectProvider(type = UserQueryBuilder.class, method = "selectFromEmail")
    User getUserFromEmail(@Param(UserQueryBuilder.FIELD_EMAIL) String email);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = UserQueryBuilder.class, method = "insertUser")
    void insertUser(User user);

    @UpdateProvider(type = UserQueryBuilder.class, method = "updateUserInfo")
    void updateUserInfo(User user);

    @SelectProvider(type = UserPwdQueryBuilder.class, method = "selectFromUserId")
    UserPwd getUserPwdFromUserId(@Param(UserPwdQueryBuilder.FIELD_USERID) int id);

    @InsertProvider(type = UserPwdQueryBuilder.class, method = "insertUserPwd")
    void insertUserPwd(UserPwd userPwd);

    @UpdateProvider(type = UserPwdQueryBuilder.class, method = "updateLockedState")
    void updateLockedState(UserPwd userPwd);

    @UpdateProvider(type = UserPwdQueryBuilder.class, method = "updatePassword")
    void updatePassword(UserPwd userPwd);

    @UpdateProvider(type = UserPwdQueryBuilder.class, method = "updateRegistrationCode")
    void updateRegistrationCode(UserPwd userPwd);

    @UpdateProvider(type = UserPwdQueryBuilder.class, method = "updateValidationCode")
    void updateValidationCode(UserPwd userPwd);

    class UserQueryBuilder
    {
        static final String TABLE_NAME = "user_std";
        static final String FIELD_ID = "id";
        static final String FIELD_USERNAME = "username";
        static final String FIELD_EMAIL = "email";

        static final ImmutableList<String> ALL_EXCEPT_ID_FIELDS = ImmutableList.of(
                FIELD_USERNAME, FIELD_EMAIL);

        static final ImmutableList<String> ALL_FIELDS = ImmutableList.of(
                FIELD_ID,
                FIELD_USERNAME, FIELD_EMAIL);

        public String selectFromId()
        {
            return new QsSQL()
                    .SELECT("*")
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }

        public String selectFromUsername()
        {
            return new QsSQL()
                    .SELECT("*")
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_USERNAME)
                    .toString();
        }

        public String selectFromEmail()
        {
            return new QsSQL()
                    .SELECT("*")
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_EMAIL)
                    .toString();
        }

        public String insertUser()
        {
            return new QsSQL().INSERT_INTO(TABLE_NAME)
                    .VALUES_PARAMS(ALL_EXCEPT_ID_FIELDS)
                    .toString();
        }

        public String updateUserInfo()
        {
            return new QsSQL().UPDATE(TABLE_NAME)
                    .SET_PARAMS(FIELD_USERNAME)
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }
    }

    class UserPwdQueryBuilder
    {
        static final String TABLE_NAME = "user_pwd";
        static final String FIELD_USERID = "userId";
        static final String FIELD_BASE64SALT = "base64salt";
        static final String FIELD_PASSWORD = "password";
        static final String FIELD_FORCECHANGEPASSWORD = "forceChangePassword";
        static final String FIELD_FAILEDATTEMPS = "failedAttemps";
        static final String FIELD_LOCKEDSINCE = "lockedSince";
        static final String FIELD_REGISTERATIONCODE = "registrationCode";
        static final String FIELD_VALIDATIONCODE = "validationCode";
        static final String FIELD_CODEGENERATION = "codeGeneration";

        static final ImmutableList<String> ALL_EXCEPT_ID_FIELDS = ImmutableList.of(
                FIELD_USERID,
                FIELD_BASE64SALT,
                FIELD_PASSWORD,
                FIELD_FORCECHANGEPASSWORD,
                FIELD_FAILEDATTEMPS,
                FIELD_LOCKEDSINCE,
                FIELD_REGISTERATIONCODE,
                FIELD_VALIDATIONCODE,
                FIELD_CODEGENERATION);

        public String selectFromUserId()
        {
            return new QsSQL()
                    .SELECT("*")
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_USERID)
                    .toString();
        }

        public String insertUserPwd()
        {
            return new QsSQL().INSERT_INTO(TABLE_NAME)
                    .VALUES_PARAMS(ALL_EXCEPT_ID_FIELDS)
                    .toString();
        }

        public String updateLockedState()
        {
            return new QsSQL().UPDATE(TABLE_NAME)
                    .SET_PARAMS(FIELD_FAILEDATTEMPS, FIELD_LOCKEDSINCE)
                    .WHERE_PARAM(FIELD_USERID)
                    .toString();
        }

        public String updatePassword()
        {
            return new QsSQL().UPDATE(TABLE_NAME)
                    .SET_PARAMS(FIELD_PASSWORD, FIELD_FORCECHANGEPASSWORD)
                    .WHERE_PARAM(FIELD_USERID)
                    .toString();
        }

        public String updateRegistrationCode()
        {
            return new QsSQL().UPDATE(TABLE_NAME)
                    .SET_PARAM(FIELD_REGISTERATIONCODE)
                    .WHERE_PARAM(FIELD_USERID)
                    .toString();
        }

        public String updateValidationCode()
        {
            return new QsSQL().UPDATE(TABLE_NAME)
                    .SET_PARAMS(FIELD_VALIDATIONCODE, FIELD_CODEGENERATION)
                    .WHERE_PARAM(FIELD_USERID)
                    .toString();
        }
    }
}
