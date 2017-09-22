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
package com.quantis_intl.lcigenerator.imports;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;

import com.google.common.collect.ImmutableMap;
import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;

public class StringFromListExtractor
{
    private static final Map<String, String> CROPS = PropertiesLoader.reverse(PropertiesLoader.CROPS);
    private static final Map<String, String> COUNTRIES = PropertiesLoader.reverseProperties("/countries.properties");
    private static final Map<String, String> YES_NO = ImmutableMap.of("yes", "yes", "no", "no");
    private static final Map<String, String> CLIMATE_ZONES = new HashMap<>();
    private static final Map<String, String> CLIMATE_SPECIFIC_ZONES = new HashMap<>();
    private static final Map<String, String> CULTIVATION_TYPE = new HashMap<>();
    private static final Map<String, String> TILLAGE_METHOD = new HashMap<>();
    private static final Map<String, String> ANTI_EROSION_PRACTICE = new HashMap<>();

    public static final Map<String, Map<String, String>> TAGS_TO_MAP = new HashMap<>();
    public static final Map<String, Map<String, String>> MANDATORY_TAGS_TO_MAP = new HashMap<>();

    static
    {
        MANDATORY_TAGS_TO_MAP.put("crop", CROPS);
        MANDATORY_TAGS_TO_MAP.put("country", COUNTRIES);

        TAGS_TO_MAP.put("organic_certified", YES_NO);

        CLIMATE_ZONES.put("Cool climate", "cool_climate");
        CLIMATE_ZONES.put("Temperate climate", "temperate_climate");
        CLIMATE_ZONES.put("Warm climate", "warm_climate");
        TAGS_TO_MAP.put("climate_zone_1", CLIMATE_ZONES);

        CLIMATE_SPECIFIC_ZONES.put("Polar polar frost", "polar_frost");
        CLIMATE_SPECIFIC_ZONES.put("Polar polar tundra", "polar_tundra");
        CLIMATE_SPECIFIC_ZONES.put("Snow dry winter extremely continental", "snow_dry_winter");
        CLIMATE_SPECIFIC_ZONES.put("Snow fully humid extremely continental", "snow_humid_continental");
        CLIMATE_SPECIFIC_ZONES.put("Arid desert cold arid", "arid_desert_cold");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate fully humid hot summer", "warm_humid_hot");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate fully humid warm summer", "warm_humid_warm");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate fully humid cold summer", "warm_humid_cold");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate summer dry hot summer", "warm_summer_dry_hot");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate summer dry warm summer", "warm_summer_dry_warm");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate summer dry cold summer", "warm_summer_dry_cold");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate winter dry hot summer", "warm_winter_dry_hot");
        CLIMATE_SPECIFIC_ZONES.put("Warm temperate winter dry cool summer", "warm_winter_dry_cool");
        CLIMATE_SPECIFIC_ZONES.put("Snow Fully humid hot summer", "snow_humid_hot");
        CLIMATE_SPECIFIC_ZONES.put("Snow Fully humid warm summer", "snow_humid_warm");
        CLIMATE_SPECIFIC_ZONES.put("Snow Fully humid cold summer", "snow_humid_cold");
        CLIMATE_SPECIFIC_ZONES.put("Snow summer dry hot summer", "snow_dry_hot");
        CLIMATE_SPECIFIC_ZONES.put("Snow summer dry warm summer", "snow_dry_warm");
        CLIMATE_SPECIFIC_ZONES.put("Snow summer dry cold summer", "snow_dry_cold");
        CLIMATE_SPECIFIC_ZONES.put("Arid steppe cold arid", "arid_steppe_cold");
        CLIMATE_SPECIFIC_ZONES.put("Equatorial fully humid", "equatorial_humid");
        CLIMATE_SPECIFIC_ZONES.put("Equatorial monsoonal", "equatorial_monsonnal");
        CLIMATE_SPECIFIC_ZONES.put("Equatorial Summer Dry", "equatorial_summer_dry");
        CLIMATE_SPECIFIC_ZONES.put("Equatorial winter dry", "equatorial_winter_dry");
        CLIMATE_SPECIFIC_ZONES.put("Arid steppe hot arid", "arid_steppe_hot");
        CLIMATE_SPECIFIC_ZONES.put("Arid desert hot arid", "arid_desert_hot");
        TAGS_TO_MAP.put("climate_zone_specific", CLIMATE_SPECIFIC_ZONES);

        CULTIVATION_TYPE.put("open ground", "open_ground");
        CULTIVATION_TYPE.put("greenhouse, open ground", "greenhouse_open_ground");
        CULTIVATION_TYPE.put("greenhouse, hydroponic system", "greenhouse_hydroponic");
        TAGS_TO_MAP.put("cultivation_type", CULTIVATION_TYPE);

        TILLAGE_METHOD.put("Fall plow", "fall_plaw");
        TILLAGE_METHOD.put("Spring plow", "spring_plow");
        TILLAGE_METHOD.put("Mulch tillage", "mulch_tillage");
        TILLAGE_METHOD.put("Ridge tillage", "ridge_tillage");
        TILLAGE_METHOD.put("Zone tillage", "zone_tillage");
        TILLAGE_METHOD.put("No till", "no_till");
        TILLAGE_METHOD.put("unknown", "unknown");
        TAGS_TO_MAP.put("tillage_method", TILLAGE_METHOD);

        ANTI_EROSION_PRACTICE.put("Up & down slope (no practice)", "no_practice");
        ANTI_EROSION_PRACTICE.put("Cross slope", "cross_slope");
        ANTI_EROSION_PRACTICE.put("Contour farming", "contour_farming");
        ANTI_EROSION_PRACTICE.put("Strip cropping, cross slope", "strip_cropping_cross_slope");
        ANTI_EROSION_PRACTICE.put("Strip cropping, contour", "strip_cropping_contour");
        ANTI_EROSION_PRACTICE.put("unknown", "unknown");
        TAGS_TO_MAP.put("anti_erosion_practice", ANTI_EROSION_PRACTICE);
    }

    private final ErrorReporter errorReporter;

    public StringFromListExtractor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
    }

    public Optional<SingleValue<String>> extract(String key, Cell labelCell, Cell dataCell, Cell commentCell,
            Cell sourceCell)
    {
        String value = POIHelper.getCellStringValue(dataCell, null);
        String code = TAGS_TO_MAP.get(key).get(value);
        if (code == null)
        {
            errorReporter.warning(
                    ImmutableMap.of("cell", POIHelper.getCellCoordinates(dataCell), "label",
                            POIHelper.getCellStringValue(labelCell, key)),
                    "Your selection is not in the list. Please pick a value from the list");
            return Optional.empty();
        }
        return Optional.of(new SingleValue<String>(key, code, POIHelper.getCellStringValue(commentCell, ""), POIHelper
                .getCellStringValue(sourceCell, ""), new Origin.ExcelUserInput(POIHelper.getCellCoordinates(dataCell),
                POIHelper.getCellStringValue(labelCell, ""))));
    }

    public String extractMandatory(String key, Cell labelCell, Cell dataCell)
    {
        String stringValue = POIHelper.getCellStringValue(dataCell, "");
        String code = MANDATORY_TAGS_TO_MAP.get(key).get(stringValue);
        if (code == null)
        {
            errorReporter.error(
                    ImmutableMap.of("cell", POIHelper.getCellCoordinates(dataCell), "label",
                            POIHelper.getCellStringValue(labelCell, key)),
                    "Your selection is not in the list. Please pick a value from the list");
            return null;
        }

        return code;
    }
}
