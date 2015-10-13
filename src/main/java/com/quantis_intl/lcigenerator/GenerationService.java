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
package com.quantis_intl.lcigenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

import com.google.inject.Inject;
import com.quantis_intl.lcigenerator.ErrorReporterImpl.ErrorReporterResult;
import com.quantis_intl.lcigenerator.dao.GenerationDao;
import com.quantis_intl.lcigenerator.model.Generation;
import com.quantis_intl.stack.utils.Qid;

public class GenerationService
{
    private static final Duration MAX_DURATION_BETWEEN_TWO_TRIES = Duration.ofMinutes(30);

    private final GenerationDao generationDao;

    private Qid presumedGenerationId;
    private String filename;
    private String crop;
    private String country;
    private Qid userId;
    private Qid licenseId;
    private String appVersion;
    private boolean canUseForTesting;
    private Collection<ErrorReporterResult> warnings;

    private Generation generation;

    @Inject
    public GenerationService(GenerationDao generationDao)
    {
        this.generationDao = generationDao;
    }

    public Generation linkFileToGeneration(Qid presumedGenerationId, String filename, String crop,
            String country, Qid userId, Qid licenseId, String appVersion, boolean canUseForTesting,
            Collection<ErrorReporterResult> warnings)
    {
        this.presumedGenerationId = presumedGenerationId;
        this.filename = filename;
        this.crop = crop;
        this.country = country;
        this.userId = userId;
        this.licenseId = licenseId;
        this.appVersion = appVersion;
        this.canUseForTesting = canUseForTesting;
        this.warnings = warnings;

        generation = presumedGenerationId == null ? null : generationDao.getGenerationFromId(presumedGenerationId);

        if (isSameGeneration())
            updateGeneration();
        else
            createNewGeneration();

        return generation;
    }

    private boolean isSameGeneration()
    {
        if (presumedGenerationId == null || generation == null)
            return false;

        if (!generation.getFilename().equals(filename))
            return false;

        if (LocalDateTime.now(ZoneOffset.UTC).minus(MAX_DURATION_BETWEEN_TWO_TRIES)
                .isAfter(generation.getLastTryDate()))
            return false;

        return generation.getCrop().equals(crop) && generation.getCountry().equals(country);
    }

    private Generation updateGeneration()
    {
        generation.setLastTryNumber(generation.getLastTryNumber() + 1);
        generation.setLastTryDate(LocalDateTime.now(ZoneOffset.UTC));
        generation.setAppVersion(appVersion);
        generation.setWarnings(warnings);

        generationDao.updateGenerationTry(generation);

        return generation;
    }

    private Generation createNewGeneration()
    {
        generation = new Generation();
        generation.setId(Qid.random());
        generation.setUserId(userId);
        generation.setLicenseId(licenseId);
        generation.setCanUseForTesting(canUseForTesting);
        generation.setLastTryNumber(1);
        generation.setLastTryDate(LocalDateTime.now(ZoneOffset.UTC));
        generation.setAppVersion(appVersion);
        generation.setCrop(crop);
        generation.setCountry(country);
        generation.setFilename(filename);
        generation.setWarnings(warnings);

        generationDao.createGeneration(generation);

        return generation;
    }
}
