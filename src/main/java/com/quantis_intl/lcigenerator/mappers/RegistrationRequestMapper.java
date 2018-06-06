/*
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2018 Quantis SARL, All Rights Reserved.
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.quantis_intl.lcigenerator.model.RegistrationRequest;
import com.quantis_intl.stack.mybatis.QsSQL;

public interface RegistrationRequestMapper
{

    @InsertProvider(type = RegistrationRequestQueryBuilder.class, method = "insertRequest")
    @Options(useGeneratedKeys = true, keyProperty = RegistrationRequestQueryBuilder.FIELD_ID)
    void insertRequest(RegistrationRequest r);


    class RegistrationRequestQueryBuilder
    {
        static final String TABLE_NAME = "registrationRequest";
        static final String FIELD_ID = "id";
        static final String FIELD_NAME = "name";
        static final String FIELD_USERNAME = "username";
        static final String FIELD_COMPANY = "company";
        static final String FIELD_ADDRESS = "address";
        static final String FIELD_MAIL = "mail";

        static final ImmutableList<String> ALL_FIELDS_EXCEPT_ID = ImmutableList.of(FIELD_NAME, FIELD_USERNAME,
                                                                                   FIELD_COMPANY, FIELD_ADDRESS,
                                                                                   FIELD_MAIL);

        static final ImmutableList<String> ALL_FIELDS = ImmutableList
                .copyOf(Iterables.concat(ImmutableList.of(FIELD_ID), ALL_FIELDS_EXCEPT_ID));

        public String insertRequest()
        {
            return new QsSQL().INSERT_INTO(TABLE_NAME)
                              .VALUES_PARAMS(ALL_FIELDS)
                              .toString();
        }
    }
}
