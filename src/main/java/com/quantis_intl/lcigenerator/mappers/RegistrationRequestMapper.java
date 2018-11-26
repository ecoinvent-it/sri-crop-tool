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
