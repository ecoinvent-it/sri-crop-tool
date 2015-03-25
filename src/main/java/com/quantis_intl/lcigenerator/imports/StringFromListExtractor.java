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

import com.quantis_intl.lcigenerator.ErrorReporter;

public class StringFromListExtractor
{
    private static final Map<String, String> CROPS = PropertiesLoader.reverseProperties("/crops.properties");
    private static final Map<String, String> COUNTRIES = PropertiesLoader.reverseProperties("/countries.properties");
    private static final Map<String, String> FARMING_TYPES = new HashMap<String, String>();
    private static final Map<String, String> PRODUCTS_MAINLY_USED_FOR = new HashMap<String, String>();
    private static final Map<String, String> AVERAGE_FIELD_SIZE = new HashMap<String, String>();
    private static final Map<String, String> HEMISPHERE = new HashMap<String, String>();
    private static final Map<String, String> TILLAGE_METHOD = new HashMap<String, String>();
    private static final Map<String, String> ANTI_EROSION_PRACTICE = new HashMap<String, String>();
    private static final Map<String, String> TYPE_OF_DRYING = new HashMap<String, String>();

    public static final Map<String, Map<String, String>> TAGS_TO_MAP = new HashMap<>();

    static
    {
        TAGS_TO_MAP.put("crop", CROPS);
        TAGS_TO_MAP.put("country", COUNTRIES);

        FARMING_TYPES.put("non-organic", "non_organic");
        FARMING_TYPES.put("organic", "organic");
        TAGS_TO_MAP.put("farming_type", FARMING_TYPES);

        PRODUCTS_MAINLY_USED_FOR.put("international market", "international_market");
        PRODUCTS_MAINLY_USED_FOR.put("demestic market", "demestic_market");
        PRODUCTS_MAINLY_USED_FOR.put("feed on the farm", "feed_on_the_farm");
        PRODUCTS_MAINLY_USED_FOR.put("subsistence farming", "subsistence_farming");
        TAGS_TO_MAP.put("products_mainly_used_for", PRODUCTS_MAINLY_USED_FOR);

        AVERAGE_FIELD_SIZE.put("< 0.1 ha", "ha_under_0_1");
        AVERAGE_FIELD_SIZE.put(">= 0.1 < 0.5 ha", "ha_0_1_to_0_5");
        AVERAGE_FIELD_SIZE.put(">= 0.5 < 1.0 ha", "ha_0_5_to_1");
        AVERAGE_FIELD_SIZE.put(">= 1.0  < 5.0 ha", "ha_1_to_5");
        AVERAGE_FIELD_SIZE.put("> 5.0 ha", "ha_over_5");
        TAGS_TO_MAP.put("average_field_size", AVERAGE_FIELD_SIZE);

        HEMISPHERE.put("North", "north");
        HEMISPHERE.put("South", "south");
        TAGS_TO_MAP.put("hemisphere", HEMISPHERE);

        TILLAGE_METHOD.put("Fall plow", "fall_plaw");
        TILLAGE_METHOD.put("Spring plow", "spring_plow");
        TILLAGE_METHOD.put("Mulch tillage", "mulch_tillage");
        TILLAGE_METHOD.put("Ridge tillage", "ridge_tillage");
        TILLAGE_METHOD.put("Zone tillage", "zone_tillage");
        TILLAGE_METHOD.put("No till", "no_till");
        TAGS_TO_MAP.put("tillage_method", TILLAGE_METHOD);

        ANTI_EROSION_PRACTICE.put("Up & down slope (no practice)", "no_practice");
        ANTI_EROSION_PRACTICE.put("Cross slope", "cross_slope");
        ANTI_EROSION_PRACTICE.put("Contour farming", "contour_farming");
        ANTI_EROSION_PRACTICE.put("Strip cropping, cross slope", "strip_cropping_cross_slope");
        ANTI_EROSION_PRACTICE.put("Strip cropping, contour", "strip_cropping_contour");
        TAGS_TO_MAP.put("anti_erosion_practice", ANTI_EROSION_PRACTICE);

        TYPE_OF_DRYING.put("Ambient air", "ambient_air");
        TYPE_OF_DRYING.put("Heating", "heating");
        TAGS_TO_MAP.put("type_of_drying", TYPE_OF_DRYING);
    }

    private ErrorReporter errorReporter;

    public StringFromListExtractor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
    }

    public String extract(RawInputLine line)
    {
        Optional<String> stringValue = line.getValueAsString();
        if (stringValue.isPresent())
        {
            String mapItem = TAGS_TO_MAP.get(line.getLineVariable()).get(stringValue);
            if (mapItem == null)
            {
                errorReporter.warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                        "Can't read value, use default");
                return null;
            }
            else
            {
                return stringValue.get();
            }
        }
        else
        {
            errorReporter
                    .warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                            "Can't read value, use default");
            return null;
        }
    }
}
