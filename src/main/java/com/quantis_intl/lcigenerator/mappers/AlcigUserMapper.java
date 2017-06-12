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

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.quantis_intl.login.business.User;
import com.quantis_intl.stack.mybatis.QsSQL;
import com.quantis_intl.stack.utils.Qid;

public interface AlcigUserMapper
{
    @SelectProvider(type = AlcigUserQueryBuilder.class, method = "selectFromId")
    User getUserFromId(@Param(AlcigUserQueryBuilder.FIELD_ID) Qid id);

    @SelectProvider(type = AlcigUserQueryBuilder.class, method = "selectFromEmail")
    User getUserFromEmail(@Param(AlcigUserQueryBuilder.FIELD_EMAIL) String email);

    @SelectProvider(type = AlcigUserQueryBuilder.class, method = "selectUsers")
    List<User> getAllUsers();

    /*@InsertProvider(type = PeterUserQueryBuilder.class, method = "insertUser")
    void insertUser(User user);

    @Insert("INSERT INTO user_pwd values (#{id}, 'None', 0, NOW(), NULL, 0, NULL, NULL, NULL)")
    void insertPwdUser(User user);*/

    class AlcigUserQueryBuilder
    {
        static final String TABLE_NAME = "user_std";
        static final String FIELD_ID = "id";
        static final String FIELD_USERNAME = "username";
        static final String FIELD_EMAIL = "email";

        static final ImmutableList<String> ALL_EXCEPT_ID_FIELDS = ImmutableList.of(
                FIELD_USERNAME, FIELD_EMAIL);

        static final ImmutableList<String> ALL_FIELDS = ImmutableList
                .copyOf(Iterables.concat(ImmutableList.of(FIELD_ID), ALL_EXCEPT_ID_FIELDS));

        public String selectFromId()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }

        public String selectFromEmail()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_EMAIL)
                    .toString();
        }

        public String selectUsers()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .toString();
        }

        /*public String insertUser()
        {
            return new QsSQL().INSERT_INTO(TABLE_NAME)
                              .VALUES_PARAMS(ALL_FIELDS)
                              .toString();
        }*/
    }
}
