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
import org.apache.ibatis.annotations.UpdateProvider;

import com.google.common.collect.ImmutableList;
import com.quantis_intl.lcigenerator.license.License;
import com.quantis_intl.stack.mybatis.QsSQL;
import com.quantis_intl.stack.utils.Qid;

public interface LicenseMapper
{
    @SelectProvider(type = LicenseQueryBuilder.class, method = "selectFromId")
    License getLicenseById(@Param(LicenseQueryBuilder.FIELD_ID) Qid id);

    @SelectProvider(type = LicenseQueryBuilder.class, method = "selectNonDepletedLicensesFromUser")
    List<License> getNonDepletedLicensesForUser(@Param(LicenseQueryBuilder.FIELD_USER_ID) Qid userId);

    @SelectProvider(type = LicenseQueryBuilder.class, method = "selectAllLicensesFromUser")
    List<License> getAllLicensesForUser(@Param(LicenseQueryBuilder.FIELD_USER_ID) Qid userId);

    @UpdateProvider(type = LicenseQueryBuilder.class, method = "updateUserId")
    void updateUserId(License license);

    @UpdateProvider(type = LicenseQueryBuilder.class, method = "setLicenseAsDepleted")
    void setLicenseAsDepleted(@Param(LicenseQueryBuilder.FIELD_ID) Qid licenseId);

    class LicenseQueryBuilder
    {
        static final String TABLE_NAME = "license";
        static final String FIELD_ID = "id";
        static final String FIELD_USER_ID = "userId";
        static final String FIELD_LICENSE_TYPE = "licenseType";
        static final String FIELD_START_DATE = "startDate";
        static final String FIELD_RENTAL_ITEM = "rentalItem";
        static final String FIELD_PRICE = "price";
        static final String FIELD_COMMENT = "comment";
        static final String FIELD_ADDITIONAL_GENERATIONS = "additionalGenerations";
        static final String FIELD_IS_DEPLETED = "isDepleted";

        static final ImmutableList<String> ALL_FIELDS = ImmutableList.of(
                FIELD_ID, FIELD_USER_ID, FIELD_LICENSE_TYPE, FIELD_START_DATE,
                FIELD_RENTAL_ITEM, FIELD_PRICE,
                FIELD_COMMENT, FIELD_ADDITIONAL_GENERATIONS, FIELD_IS_DEPLETED);

        public String selectFromId()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }

        public String selectNonDepletedLicensesFromUser()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_USER_ID)
                    .WHERE(FIELD_IS_DEPLETED + " = 0")
                    .ORDER_BY(FIELD_START_DATE)
                    .toString();
        }

        public String selectAllLicensesFromUser()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_USER_ID)
                    .ORDER_BY(FIELD_START_DATE + " DESC")
                    .toString();
        }

        public String updateUserId()
        {
            return new QsSQL()
                    .UPDATE(TABLE_NAME)
                    .SET_PARAM(FIELD_USER_ID)
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }

        public String setLicenseAsDepleted()
        {
            return new QsSQL()
                    .UPDATE(TABLE_NAME)
                    .SET(FIELD_IS_DEPLETED + " = 1")
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }
    }
}
