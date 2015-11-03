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

import java.util.Arrays;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.login.business.LoginService;
import com.quantis_intl.login.business.LoginService.ChangePasswordFailed;
import com.quantis_intl.stack.utils.Qid;

@Path("principal/")
public class PrincipalApi
{
    private static final Logger LOG = LoggerFactory.getLogger(PrincipalApi.class);

    private final LoginService loginService;

    @Inject
    public PrincipalApi(LoginService loginService)
    {
        this.loginService = loginService;
    }

    @GET
    @Path("getStatus")
    public Response getStatus()
    {
        final Qid userId = getUserId();
        boolean status = !loginService.mustForcePasswordForUser(userId);
        LOG.info("Get user status");
        return Response.ok(Boolean.toString(status)).build();
    }

    @POST
    @Path("changePassword")
    public Response changePassword(@FormParam("oldPassword") char[] oldPassword,
            @FormParam("newPassword") char[] newPassword)
    {
        final Qid userId = getUserId();
        try
        {
            loginService.changePassword(userId, oldPassword, newPassword);
            Arrays.fill(oldPassword, (char) 0x00);
            Arrays.fill(newPassword, (char) 0x00);
            LOG.info("Password changed");
            return Response.ok().build();
        }
        catch (ChangePasswordFailed e)
        {
            LOG.error("Change password failed , reason: {}", e.reason.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.reason.toString()).build();
        }
    }

    private Qid getUserId()
    {
        return (Qid) SecurityUtils.getSubject().getPrincipal();
    }
}
