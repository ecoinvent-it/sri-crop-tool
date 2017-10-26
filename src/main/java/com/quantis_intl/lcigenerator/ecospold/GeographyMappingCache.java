/*
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2017 Quantis SARL, All Rights Reserved.
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

package com.quantis_intl.lcigenerator.ecospold;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.quantis_intl.commons.ecospold2.ecospold02.TGeography;

@Singleton
public class GeographyMappingCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GeographyMappingCache.class);

    //FIXME: Validate
    private static final Map<String, TGeography> GEOGRAPHY_MAPPING = new HashMap<>(1024);

    @Inject
    public GeographyMappingCache()
    {
        try
        {
            for (String l : Resources
                    .readLines(GeographyMappingCache.class.getResource("ecospold_geo_mapping.txt"), Charsets.UTF_8))
            {
                String[] split = l.split("=");
                GEOGRAPHY_MAPPING.put(split[1], TGeography.ofEn(split[0].substring(0, 36), split[0].substring(37)));
            }
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public TGeography getGeography(String country)
    {
        return GEOGRAPHY_MAPPING.get(country);
    }
}
