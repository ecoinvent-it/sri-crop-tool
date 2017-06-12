/*
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2017 Quantis SARL, All Rights Reserved.
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
package com.quantis_intl.lcigenerator.dao;

import java.util.Collection;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

import com.quantis_intl.lcigenerator.mappers.AlcigUserMapper;
import com.quantis_intl.login.business.User;
import com.quantis_intl.login.business.UserDao;
import com.quantis_intl.stack.utils.Qid;

public class MybatisAlcigUserDao implements UserDao<User>
{
    private final AlcigUserMapper mapper;

    @Inject
    public MybatisAlcigUserDao(AlcigUserMapper mapper)
    {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Collection<User> getUsers()
    {
        return mapper.getAllUsers();
    }

    @Override
    @Transactional
    public Collection<User> getNonActivatedUsers()
    {
        return mapper.getAllUsers();
    }

    @Override
    @Transactional
    public Collection<User> getActiveUsers()
    {
        return mapper.getAllUsers();
    }

    @Override
    @Transactional
    public User getPostLoginUser(Qid userId)
    {
        return mapper.getUserFromId(userId);
    }

    /*@Override
    @Transactional
    public User getUserFromId(Qid id)
    {
        return mapper.getUserFromId(id);
    }*/

    /*@Override
    @Transactional
    public PeterUser getUserFromEmail(String email)
    {
        return mapper.getUserFromEmail(email);
    }

    @Override
    @Transactional
    public void createUser(PeterUser user)
    {
        mapper.insertUser(user);
        mapper.insertPwdUser(user);
    }*/
}
