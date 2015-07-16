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
package com.quantis_intl.lcigenerator.dao;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

import com.quantis_intl.lcigenerator.mappers.LoginMapper;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.lcigenerator.model.UserPwd;

public class LoginDaoImpl implements LoginDao
{
    private LoginMapper mapper;

    @Inject
    public LoginDaoImpl(LoginMapper mapper)
    {
        this.mapper = mapper;
    }

    @Transactional
    public User getUserFromId(int id)
    {
        return mapper.getUserFromId(id);
    }

    @Transactional
    public User getUserFromUsername(String username)
    {
        return mapper.getUserFromUsername(username);
    }

    @Transactional
    public void createUser(User user, UserPwd userPwd)
    {
        mapper.insertUser(user);
        userPwd.setUserId(user.getId());
        mapper.insertUserPwd(userPwd);
    }

    @Transactional
    public void updateUserInfo(User user)
    {
        mapper.updateUserInfo(user);
    }

    @Transactional
    public UserPwd getUserPwdFromUserId(int id)
    {
        return mapper.getUserPwdFromUserId(id);
    }

    @Transactional
    public void updateLockedState(UserPwd userPwd)
    {
        mapper.updateLockedState(userPwd);
    }

    @Transactional
    public void updatePassword(UserPwd userPwd)
    {
        mapper.updatePassword(userPwd);
    }
}
