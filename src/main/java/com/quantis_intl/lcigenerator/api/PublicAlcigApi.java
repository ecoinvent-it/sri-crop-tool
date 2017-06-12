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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.lcigenerator.license.LicenseService;
import com.quantis_intl.login.api.PublicPrincipalApi;
import com.quantis_intl.login.business.UserService;
import com.quantis_intl.stack.utils.Qid;

//FIXME: All this should be protected in adminApi
@Path("pub/principal/")
public class PublicAlcigApi
{
    private static final Logger LOG = LoggerFactory.getLogger(PublicPrincipalApi.class);

    private final UserService userService;
    private final LicenseService licenseService;

    @Inject
    public PublicAlcigApi(UserService userService, LicenseService licenseService)
    {
        this.userService = userService;
        this.licenseService = licenseService;
    }

    @GET
    @Path("createAccountForLicense")
    // FIXME: Put it in license dedicated API, use POST
    public Response createAccountForLicense(@QueryParam("licenseId") String licenseIdRepresentation,
                                            @QueryParam("username") String username,
                                            @QueryParam("email") String userEmail)
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
        Qid userId = Qid.fromRepresentation(userIdRepresentation);
        try
        {
            userService.sendActivationRequest(userId);
            LOG.info("Admin user has successfully sent activation request for user: {}", userId);
        }
        catch (UserService.UserAlreadyActivated e)
        {
            LOG.error("Admin user can't send activation request to an already activated user: {}", userId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
}
