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
package com.quantis_intl.lcigenerator.license;

import java.util.OptionalInt;

import javax.inject.Inject;

import com.quantis_intl.lcigenerator.dao.GenerationDao;
import com.quantis_intl.login.business.AdminService;
import com.quantis_intl.stack.utils.Qid;

public class LicenseService
{
    private final LicenseDao dao;
    private final AdminService adminService;
    private final GenerationDao generationDao;

    @Inject
    public LicenseService(LicenseDao dao, AdminService adminService, GenerationDao generationDao)
    {
        this.dao = dao;
        this.adminService = adminService;
        this.generationDao = generationDao;
    }

    public Qid createUserFromLicense(Qid licenseId, String username, String email)
    {
        License license = dao.getExistingLicenseById(licenseId);
        if (license.getUserId() != null)
            throw new IllegalStateException();
        Qid userId = adminService.createNonActivatedUser(username, email);
        license.setUserId(userId);
        dao.updateUserId(license);
        return userId;
    }

    public OptionalInt checkLicenseDepletion(Qid licenseId)
    {
        License license = dao.getExistingLicenseById(licenseId);
        OptionalInt availableNumber = license.getNumberOfGenerations();
        if (!availableNumber.isPresent())
            return availableNumber;

        int generations = generationDao.countGenerationForLicense(licenseId);
        int remaining = Math.max(0, availableNumber.getAsInt() - generations);

        if (remaining == 0)
            dao.setLicenseAsDepleted(licenseId);

        return OptionalInt.of(remaining);
    }

    public License findActiveLicenseOrFail(Qid userId)
    {
        License res = dao.getCurrentActiveLicenseForUser(userId);
        if (res == null)
            throw new NoActiveLicense();

        return res;
    }

    public static class NoActiveLicense extends RuntimeException
    {
        private static final long serialVersionUID = 232728569032899181L;
    }
}
