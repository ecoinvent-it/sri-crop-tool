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

import com.quantis_intl.lcigenerator.licence.License;
import com.quantis_intl.lcigenerator.licence.LicenseDao;
import com.quantis_intl.lcigenerator.licence.LicenseService;
import com.quantis_intl.stack.utils.Qid;

@Path("/licence")
public class LicenseApi
{
    private static final Logger LOG = LoggerFactory.getLogger(LicenseApi.class);

    private final LicenseService licenseService;
    private final LicenseDao licenseDao;

    @Inject
    public LicenseApi(LicenseService licenseService, LicenseDao licenseDao)
    {
        this.licenseService = licenseService;
        this.licenseDao = licenseDao;
    }

    @GET
    @Path("activeLicenses")
    // FIXME: Only send the usefull data, many things in the class shouldn't leave the server
    public Collection<License> getActiveLicenses()
    {
        return licenseDao.getActiveLicensesForUser(getUserId());
    }

    @POST
    @Path("createAccountForLicense")
    // FIXME: Put it in public API?
    public Response createAccountForLicense(String licenseIdRepresentation, String userEmail)
    {
        licenseService.createUserFromLicense(Qid.fromRepresentation(licenseIdRepresentation), userEmail);
        return Response.ok().build();
    }

    private Qid getUserId()
    {
        return (Qid) SecurityUtils.getSubject().getPrincipal();
    }
}
