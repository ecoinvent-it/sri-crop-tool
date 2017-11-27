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

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;

@Singleton
public class CropsEcospoldRefsCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CropsEcospoldRefsCache.class);

    private final Map<String, String> cropActivityNames;
    private final Map<String, String> cropExchangeNames;

    @Inject
    public CropsEcospoldRefsCache()
    {
        this.cropActivityNames = Maps.fromProperties(
                PropertiesLoader.loadProperties("ecospold_activity_name_mapping.properties"));
        this.cropExchangeNames = Maps.fromProperties(
                PropertiesLoader.loadProperties("ecospold_main_output_mapping.properties"));
    }

    public String activityNameOfCrop(String crop)
    {
        return cropActivityNames.get(crop);
    }

    public String exchangeNameOfCrop(String crop)
    {
        return cropExchangeNames.get(crop);
    }
}
