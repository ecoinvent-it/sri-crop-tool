package com.quantis_intl.lcigenerator.dao;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

import com.quantis_intl.lcigenerator.mappers.RegistrationRequestMapper;
import com.quantis_intl.lcigenerator.model.RegistrationRequest;

public class RegistrationRequestDao
{
    private RegistrationRequestMapper mapper;

    @Inject
    public RegistrationRequestDao(RegistrationRequestMapper mapper)
    {
        this.mapper = mapper;
    }

    @Transactional
    public void insertRequest(RegistrationRequest r)
    {
        mapper.insertRequest(r);
    }
}
