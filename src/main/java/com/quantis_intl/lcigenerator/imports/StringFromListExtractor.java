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
    private static final Map<String, String> TILLAGE_METHOD = new HashMap<String, String>();
    private static final Map<String, String> ANTI_EROSION_PRACTICE = new HashMap<String, String>();
    private static final Map<String, String> TYPE_OF_DRYING = new HashMap<String, String>();

    public static final Map<String, Map<String, String>> TAGS_TO_MAP = new HashMap<>();
    public static final Map<String, Map<String, String>> MANDATORY_TAGS_TO_MAP = new HashMap<>();

    static
    {
        MANDATORY_TAGS_TO_MAP.put("crop", CROPS);
        MANDATORY_TAGS_TO_MAP.put("country", COUNTRIES);

        FARMING_TYPES.put("non-organic", "non_organic");
        FARMING_TYPES.put("organic", "organic");
        TAGS_TO_MAP.put("farming_type", FARMING_TYPES);

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
        boolean isMandatory = MANDATORY_TAGS_TO_MAP.containsKey(line.getLineVariable());
        if (isMandatory)
            return extractMandatory(line);
        else
            return extractOptional(line);
    }

    private String extractMandatory(RawInputLine line)
    {
        Optional<String> stringValue = line.getValueAsString();
        if (stringValue.isPresent())
        {
            String mapItem = MANDATORY_TAGS_TO_MAP.get(line.getLineVariable()).get(stringValue.get());
            if (mapItem == null)
            {
                errorReporter.error(line.getLineTitle(), Integer.toString(line.getLineNum()),
                        "Mandatory text is not part of choices list");
                return null;
            }
            else
            {
                return mapItem;
            }
        }
        else
        {
            errorReporter
                    .error(line.getLineTitle(), Integer.toString(line.getLineNum()),
                            "Can't read mandatory text");
            return null;
        }
    }

    private String extractOptional(RawInputLine line)
    {
        Optional<String> stringValue = line.getValueAsString();
        if (stringValue.isPresent())
        {
            String mapItem = TAGS_TO_MAP.get(line.getLineVariable()).get(stringValue.get());
            if (mapItem == null)
            {
                errorReporter.warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                        "Text is not part of choices list, use default");
                return null;
            }
            else
            {
                return mapItem;
            }
        }
        else
        {
            errorReporter
                    .warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                            "Can't read text, use default");
            return null;
        }
    }
}
