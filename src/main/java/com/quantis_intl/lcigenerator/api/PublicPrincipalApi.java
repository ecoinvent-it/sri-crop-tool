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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.quantis_intl.login.business.LoginService;
import com.quantis_intl.login.business.LoginService.EmailNotFound;
import com.quantis_intl.login.business.LoginService.ResetPasswordFailed;
import com.quantis_intl.login.business.LoginService.UserActivationPending;
import com.quantis_intl.login.business.LoginService.UserAlreadyActivated;

@Path("pub/principal/")
public class PublicPrincipalApi
{
    private final LoginService loginService;

    @Inject
    public PublicPrincipalApi(LoginService loginService)
    {
        this.loginService = loginService;
    }

    @POST
    @Path("activateUser")
    public Response activateUser(@FormParam("email") String email,
            @FormParam("registrationCode") String registrationCode,
            @FormParam("newPassword") String newPassword
    /*@Context UriInfo uriInfo*/)
    {
        try
        {
            // FIXME: Don't hardcode
            loginService.activateUser(email, registrationCode, newPassword
            /*uriInfo.getBaseUri().toString().replace("app/", "").replace("http:", "https:")*/);

            StringBuilder sb = new StringBuilder("Your access to ALCIG is now activated!");

            return Response.ok(sb.toString()).build();
        }
        catch (UserAlreadyActivated e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Your account is already activated.").build();
        }
    }

    @POST
    @Path("forgotPassword")
    public Response forgotPassword(@FormParam("email") String email, @Context UriInfo uriInfo)
    {
        String activationUrl = uriInfo.getAbsolutePath().toString().replace("http:", "https:")
                .replace("pub/principal/forgotPassword", "pub/principal/activateUser");
        try
        {
            loginService.forgotPassword(email, activationUrl);
        }
        catch (EmailNotFound e)
        {}
        catch (UserActivationPending e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("USER_ACTIVATION_PENDING").build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("resetPassword")
    public Response resetPassword(@FormParam("email") String email,
            @FormParam("validationCode") String validationCode,
            @FormParam("newPassword") String newPassword)
    {
        try
        {
            loginService.resetPassword(email, validationCode, newPassword);
        }
        catch (EmailNotFound e)
        {}
        catch (UserActivationPending e)
        {}
        catch (ResetPasswordFailed e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.reason.toString()).build();
        }

        return Response.ok().build();
    }
}
