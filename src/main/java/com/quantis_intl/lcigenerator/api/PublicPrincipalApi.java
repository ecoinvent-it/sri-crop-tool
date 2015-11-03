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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.lcigenerator.license.LicenseService;
import com.quantis_intl.login.business.LoginService;
import com.quantis_intl.login.business.LoginService.ChangePasswordFailed;
import com.quantis_intl.login.business.LoginService.EmailNotFound;
import com.quantis_intl.login.business.LoginService.ExpiredRegistrationCode;
import com.quantis_intl.login.business.LoginService.ResetPasswordFailed;
import com.quantis_intl.login.business.LoginService.UserActivationPending;
import com.quantis_intl.login.business.LoginService.UserAlreadyActivated;
import com.quantis_intl.login.business.LoginService.WrongRegistrationCode;
import com.quantis_intl.login.business.User;
import com.quantis_intl.stack.utils.Qid;

@Path("pub/principal/")
public class PublicPrincipalApi
{
    private static final Logger LOG = LoggerFactory.getLogger(PublicPrincipalApi.class);

    private final LoginService loginService;
    private final LicenseService licenseService;

    @Inject
    public PublicPrincipalApi(LoginService loginService, LicenseService licenseService)
    {
        this.loginService = loginService;
        this.licenseService = licenseService;
    }

    @POST
    @Path("checkRegistrationCode")
    public Response checkRegistrationCode(@FormParam("registrationCode") String registrationCode)
    {
        try
        {
            User user = loginService.checkRegistrationCode(registrationCode);
            LOG.info("User registration code checked: {}", user.getId());
            return Response.ok(user.getUsername()).build();
        }
        catch (WrongRegistrationCode e)
        {
            LOG.warn("No user found for registrationCode: {}", registrationCode);
            return Response.status(Response.Status.BAD_REQUEST).entity("WRONG_REGISTRATION_CODE").build();
        }
        catch (ExpiredRegistrationCode e)
        {
            LOG.warn("User {} tries to check expired registration code", e.userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("EXPIRED_REGISTRATION_CODE").build();
        }
        catch (UserAlreadyActivated e)
        {
            LOG.error("Already activated user {} tries to use registration code", e.userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("WRONG_REGISTRATION_CODE").build();
        }
    }

    @POST
    @Path("activateUser")
    // FIXME: String stays in memory, so passwords are in memory until the next GC. Use char[] and find a way to avoid
    // JAX-RS to store them as strings (use multi-part form with inputstream?)
    public Response activateUser(@FormParam("registrationCode") String registrationCode,
            @FormParam("acceptTermsAndConditions") boolean acceptTermsAndConditions,
            @FormParam("newPassword") String newPassword)
    {
        try
        {
            User user = loginService.activateUser(registrationCode, acceptTermsAndConditions,
                    newPassword.toCharArray());
            // Arrays.fill(newPassword, (char) 0x00);
            LOG.info("User activated: {}", user.getId());
            return Response.ok(user.getUsername()).build();

        }
        catch (WrongRegistrationCode e)
        {
            LOG.error("User tries to activate with wrong registrationCode: {}", registrationCode);
            return Response.status(Response.Status.BAD_REQUEST).entity("WRONG_REGISTRATION_CODE").build();
        }
        catch (ExpiredRegistrationCode e)
        {
            LOG.warn("Registration code timeout for user {}", e.userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("EXPIRED_REGISTRATION_CODE").build();
        }
        catch (ChangePasswordFailed e)
        {
            LOG.error("User ({}) tries to activate with invalid new password", e.userId);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.reason.toString()).build();
        }
        catch (UserAlreadyActivated e)
        {
            LOG.error("Security issue: user already activated tries to activate: {}", e.userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("WRONG_REGISTRATION_CODE").build();
        }
    }

    @POST
    @Path("forgotPassword")
    public Response forgotPassword(@FormParam("email") String email)
    {
        try
        {
            Object userId = loginService.forgotPassword(email);
            LOG.info("Reset password request received for user: {}", userId);
        }
        catch (EmailNotFound e)
        {
            LOG.error("User tries to request reset password of non existing user: {}", email);
            // NOTE: Don't throw BAD_REQUEST
        }
        catch (UserActivationPending e)
        {
            LOG.info("Non activated user ({}) trying to reset password. Re-sending activation mail {}", e.userId,
                    email);
            return Response.status(Response.Status.BAD_REQUEST).entity("USER_ACTIVATION_PENDING").build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("resetPassword")
    // FIXME: String stays in memory, so passwords are in memory until the next GC. Use char[] and find a way to avoid
    // JAX-RS to store them as strings (use multi-part form with inputstream?)
    public Response resetPassword(@FormParam("validationCode") String validationCode,
            @FormParam("newPassword") String newPassword)
    {
        try
        {
            Object userId = loginService.resetPassword(validationCode, newPassword.toCharArray());
            // Arrays.fill(newPassword, (char) 0x00);
            LOG.info("Password reset for user {}", userId);
        }
        catch (UserActivationPending e)
        {
            LOG.error("Non activated user trying to reset password: {}", e.userId);
            return Response.status(Response.Status.BAD_REQUEST).entity("USER_ACTIVATION_PENDING").build();
        }
        catch (ResetPasswordFailed e)
        {
            LOG.error("Reset password failed for user {}, reason: {}", e.userId, e.reason.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.reason.toString()).build();
        }

        return Response.ok().build();
    }

    @GET
    @Path("createAccountForLicense")
    // FIXME: Put it in license dedicated API, use POST
    public Response createAccountForLicense(@QueryParam("licenseId") String licenseIdRepresentation,
            @QueryParam("username") String username, @QueryParam("email") String userEmail)
    {
        try
        {
            Qid userId = licenseService.createUserFromLicense(Qid.fromRepresentation(licenseIdRepresentation),
                    username, userEmail);
            LOG.info("Create account {} for license : {}", userId, licenseIdRepresentation);
        }
        catch (RuntimeException e)
        {
            LOG.warn("createAccountForLicense call failed", e);
        }
        return Response.ok().build();
    }

    @GET
    @Path("sendActivationLink")
    // FIXME: Put it in license dedicated API, use POST
    public Response sendActivationLink(@QueryParam("userId") String userIdRepresentation)
    {
        try
        {
            loginService.sendActivationRequest(Qid.fromRepresentation(userIdRepresentation));
            LOG.info("Activation mail sent for user {}", userIdRepresentation);
        }
        catch (RuntimeException e)
        {
            LOG.warn("sendActivationLink call failed", e);
        }
        return Response.ok().build();
    }
}
