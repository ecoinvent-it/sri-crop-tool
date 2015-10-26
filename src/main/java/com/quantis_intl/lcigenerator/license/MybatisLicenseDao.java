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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

import com.quantis_intl.lcigenerator.mappers.LicenseMapper;
import com.quantis_intl.stack.utils.Qid;

public class MybatisLicenseDao implements LicenseDao
{
    private LicenseMapper mapper;

    @Inject
    public MybatisLicenseDao(LicenseMapper mapper)
    {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public License getExistingLicenseById(Qid licenseId)
    {
        License res = mapper.getLicenseById(licenseId);
        if (res == null)
            throw new LicenseNotFound(licenseId);

        return res;
    }

    // Get the first still active non depleted license, or null if none are found
    @Override
    @Transactional
    public License getCurrentActiveLicenseForUser(Qid userId)
    {
        List<License> licenses = mapper.getNonDepletedLicensesForUser(userId);
        LocalDate now = LocalDate.now();
        for (License l : licenses)
        {
            if (!l.isExpiredAt(now))
                return l;
        }
        return null;
    }

    // Get the still active non depleted licenses
    @Override
    @Transactional
    public List<License> getActiveLicensesForUser(Qid userId)
    {
        LocalDate now = LocalDate.now();
        return mapper.getNonDepletedLicensesForUser(userId).stream().filter(l -> !l.isExpiredAt(now))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserId(License license)
    {
        mapper.updateUserId(license);
    }

    @Override
    @Transactional
    public void setLicenseAsDepleted(Qid licenseId)
    {
        mapper.setLicenseAsDepleted(licenseId);
    }
}
