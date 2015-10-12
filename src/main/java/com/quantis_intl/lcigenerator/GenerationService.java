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
import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.quantis_intl.lcigenerator.ErrorReporterImpl.ErrorReporterResult;
import com.quantis_intl.lcigenerator.dao.GenerationDao;
import com.quantis_intl.lcigenerator.model.Generation;
import com.quantis_intl.stack.utils.Qid;

public class GenerationService
{
    private static final Duration MAX_DURATION_BETWEEN_TWO_TRIES = Duration.ofMinutes(30);

    private final GenerationDao generationDao;

    private final ObjectMapper objectMapper;

    @Inject
    public GenerationService(GenerationDao generationDao, ObjectMapper objectMapper)
    {
        this.generationDao = generationDao;
        this.objectMapper = objectMapper;
    }

    public boolean couldBeTheSameGeneration(Qid generationId, String filename)
    {
        if (generationId == null)
            return false;

        Generation persistedGeneration = generationDao.getGenerationFromId(generationId);
        if (persistedGeneration == null)
            return false;

        if (!persistedGeneration.getFilename().equals(filename))
            return false;

        if (LocalDateTime.now().minus(MAX_DURATION_BETWEEN_TWO_TRIES).isAfter(persistedGeneration.getLastTryDate()))
            return false;

        return true;
    }

    public boolean hasSameCropAndCountry(Qid generationId, String crop, String country)
    {
        if (generationId == null)
            return false;

        Generation persistedGeneration = generationDao.getGenerationFromId(generationId);

        return persistedGeneration.getCrop().equals(crop) && persistedGeneration.getCountry().equals(country);
    }

    public Generation updateGeneration(Qid generationId, Collection<ErrorReporterResult> warnings, String appVersion)
    {
        Generation generation = generationDao.getGenerationFromId(generationId);
        generation.setLastTryNumber(generation.getLastTryNumber() + 1);
        generation.setLastTryDate(LocalDateTime.now());
        generation.setAppVersion(appVersion);
        try
        {
            generation.setWarnings(objectMapper.writeValueAsString(warnings));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }

        generationDao.updateGenerationTry(generation);

        return generation;
    }

    public Generation createNewGeneration(Qid userId, Qid licenseId, String appVersion, boolean canUseForTesting,
            String filename,
            String crop, String country,
            Collection<ErrorReporterResult> warnings)
    {
        Generation generation = new Generation();
        generation.setId(Qid.random());
        generation.setUserId(userId);
        generation.setLicenseId(licenseId);
        generation.setCanUseForTesting(canUseForTesting);
        generation.setLastTryNumber(1);
        generation.setLastTryDate(LocalDateTime.now());
        generation.setAppVersion(appVersion);
        generation.setCrop(crop);
        generation.setCountry(country);
        generation.setFilename(filename);
        try
        {
            generation.setWarnings(objectMapper.writeValueAsString(warnings));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }

        generationDao.createGeneration(generation);

        return generation;
    }
}
