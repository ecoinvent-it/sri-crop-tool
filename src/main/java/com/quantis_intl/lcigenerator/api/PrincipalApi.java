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

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;

import com.quantis_intl.login.business.LoginService;
import com.quantis_intl.login.business.LoginService.ChangePasswordFailed;

@Path("principal/")
public class PrincipalApi
{
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
        boolean status = !loginService.mustForcePasswordForUser(getUserId());
        return Response.ok(Boolean.toString(status)).build();
    }

    @POST
    @Path("changePassword")
    public Response changePassword(@FormParam("oldPassword") String oldPassword,
            @FormParam("newPassword") String newPassword)
    {
        final int userId = getUserId();
        try
        {
            loginService.changePassword(userId, oldPassword, newPassword);
            return Response.ok().build();
        }
        catch (ChangePasswordFailed e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.reason.toString()).build();
        }
    }

    private int getUserId()
    {
        return (Integer) SecurityUtils.getSubject().getPrincipal();
    }
}
