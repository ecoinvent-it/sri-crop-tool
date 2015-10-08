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
package com.quantis_intl.lcigenerator.api;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.login.business.AclUser;
import com.quantis_intl.login.business.LoginDao;
import com.quantis_intl.login.business.LoginService;
import com.quantis_intl.login.business.LoginService.EmailAlreadyExists;
import com.quantis_intl.login.business.LoginService.InvalidEmail;
import com.quantis_intl.login.business.LoginService.TooLongEmail;
import com.quantis_intl.login.business.LoginService.UserAlreadyActivated;
import com.quantis_intl.stack.utils.Qid;

@Path("/admin")
public class AdminApi
{
    private static final Logger LOG = LoggerFactory.getLogger(AdminApi.class);

    private final LoginDao dao;
    private final LoginService loginService;

    @Inject
    public AdminApi(LoginDao dao, LoginService loginService)
    {
        this.dao = dao;
        this.loginService = loginService;
    }

    @GET
    @Path("users")
    public Collection<AclUser> getUsers()
    {
        Collection<AclUser> users = dao.getAclUsers();
        LOG.info("Get list of ACL users");
        return users;
    }

    @POST
    @Path("sendActivationRequest")
    public Response sendActivationRequest(String userIdRepresentation)
    {
        Qid userId = Qid.fromRepresentation(userIdRepresentation);
        try
        {
            loginService.sendActivationRequest(userId);
            LOG.info("Admin user {} has successfully sent activation request for user: {}", getAdminId(), userId);
        }
        catch (UserAlreadyActivated e)
        {
            LOG.error("Admin user {}  can't send activation request to an already activated user: {}", getAdminId(),
                    userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("changeUserEmail")
    public Response changeUserEmail(String userIdRepresentation, String newEmail)
    {
        Qid userId = Qid.fromRepresentation(userIdRepresentation);
        try
        {
            loginService.changeEmail(userId, newEmail);
            LOG.info("Admin user {} has successfully changed email for user {}", getAdminId(), userId);
            return Response.ok().build();
        }
        catch (TooLongEmail e)
        {
            LOG.error("Admin user {} tries to change email for user {} by a too long email", getAdminId(), userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (InvalidEmail e)
        {
            LOG.error("Admin user {} tries to change email for user {} by an invalid email", getAdminId(), userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (EmailAlreadyExists e)
        {
            LOG.error("Admin user {} tries to change email for user {} by an existing email", getAdminId(), userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    private Qid getAdminId()
    {
        return (Qid) SecurityUtils.getSubject().getPrincipal();
    }
}
