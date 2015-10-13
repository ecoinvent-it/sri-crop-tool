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

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.quantis_intl.lcigenerator.ErrorReporterImpl.ErrorReporterResult;
import com.quantis_intl.lcigenerator.model.Generation;
import com.quantis_intl.stack.mybatis.JsonTypeHandler;
import com.quantis_intl.stack.mybatis.QsSQL;
import com.quantis_intl.stack.utils.Qid;

public interface GenerationMapper
{
    // TODO: use @Results id property instead of ugly methodname-argType when upgrading to mybatis 3.4
    @SelectProvider(type = GenerationQueryBuilder.class, method = "selectAllFromUserId")
    @Results({
            @Result(property = "warnings", column = "warnings", typeHandler = WarningJsonTypeHandler.class)
    })
    List<Generation> getAllGenerationsFromUserId(@Param(GenerationQueryBuilder.FIELD_USER_ID) Qid userId);

    @SelectProvider(type = GenerationQueryBuilder.class, method = "selectFromId")
    @ResultMap("getAllGenerationsFromUserId-Qid")
    Generation getGenerationFromId(@Param(GenerationQueryBuilder.FIELD_ID) Qid id);

    @SelectProvider(type = GenerationQueryBuilder.class, method = "countFromLicense")
    int countGenerationForLicense(@Param(GenerationQueryBuilder.FIELD_LICENSE_ID) Qid licenseId);

    @InsertProvider(type = GenerationQueryBuilder.class, method = "insertGeneration")
    void insertGeneration(Generation generation);

    @UpdateProvider(type = GenerationQueryBuilder.class, method = "updateTry")
    void updateGenerationTry(Generation generation);

    class WarningJsonTypeHandler extends JsonTypeHandler<Collection<ErrorReporterResult>>
    {
        @Inject
        public WarningJsonTypeHandler()
        {
            super(new TypeReference<Collection<ErrorReporterResult>>()
            {});
        }
    }

    class GenerationQueryBuilder
    {
        static final String TABLE_NAME = "generation";
        static final String FIELD_ID = "id";
        static final String FIELD_USER_ID = "userId";
        static final String FIELD_LICENSE_ID = "licenseId";
        static final String FIELD_CAN_USE_FOR_TESTING = "canUseForTesting";
        static final String FIELD_LAST_TRY_NUMBER = "lastTryNumber";
        static final String FIELD_LAST_TRY_DATE = "lastTryDate";
        static final String FIELD_APP_VERSION = "appVersion";
        static final String FIELD_CROP = "crop";
        static final String FIELD_COUNTRY = "country";
        static final String FIELD_FILENAME = "filename";
        static final String FIELD_WARNINGS = "warnings";

        static final ImmutableList<String> ALL_FIELDS_EXCEPT_WARNINGS = ImmutableList.of(
                FIELD_ID, FIELD_USER_ID, FIELD_LICENSE_ID,
                FIELD_CAN_USE_FOR_TESTING,
                FIELD_LAST_TRY_NUMBER, FIELD_LAST_TRY_DATE,
                FIELD_APP_VERSION, FIELD_CROP, FIELD_COUNTRY,
                FIELD_FILENAME);

        static final ImmutableList<String> ALL_FIELDS = ImmutableList
                .copyOf(Iterables.concat(ALL_FIELDS_EXCEPT_WARNINGS, ImmutableList.of(FIELD_WARNINGS)));

        public String selectAllFromUserId()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_USER_ID)
                    .ORDER_BY(FIELD_LAST_TRY_DATE + " DESC")
                    .toString();
        }

        public String selectFromId()
        {
            return new QsSQL()
                    .SELECT(ALL_FIELDS)
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }

        public String countFromLicense()
        {
            return new QsSQL()
                    .SELECT("COUNT (*)")
                    .FROM(TABLE_NAME)
                    .WHERE_PARAM(FIELD_LICENSE_ID)
                    .toString();
        }

        public String insertGeneration()
        {
            return new QsSQL()
                    .INSERT_INTO(TABLE_NAME)
                    .VALUES_PARAMS(ALL_FIELDS_EXCEPT_WARNINGS)
                    .VALUES_PARAM(FIELD_WARNINGS, WarningJsonTypeHandler.class.getName())
                    .toString();
        }

        public String updateTry()
        {
            return new QsSQL()
                    .UPDATE(TABLE_NAME)
                    .SET_PARAMS(FIELD_LAST_TRY_NUMBER, FIELD_LAST_TRY_DATE, FIELD_APP_VERSION)
                    .SET_PARAM(FIELD_WARNINGS, WarningJsonTypeHandler.class.getName())
                    .WHERE_PARAM(FIELD_ID)
                    .toString();
        }
    }
}
