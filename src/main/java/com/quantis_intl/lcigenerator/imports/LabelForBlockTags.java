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

public class LabelForBlockTags
{
    public static final Map<String, Map<String, String>> LABELS_FOR_NUMERIC = new HashMap<>();
    public static final Map<String, Map<String, String>> LABELS_FOR_RATIO = new HashMap<>();

    public static final String DEFAULT_VALUE = "other";
    public static final String END_BLOCK_TAG = "end";

    private static final Map<String, String> HERBICIDES = PropertiesLoader.reverseProperties("/herbicides.properties");
    private static final Map<String, String> FUNGICIDES = PropertiesLoader.reverseProperties("/fungicides.properties");
    private static final Map<String, String> INSECTICIDES = PropertiesLoader
            .reverseProperties("/insecticides.properties");

    private static final Map<String, String> COMPOST_TYPE = new HashMap<>();
    private static final Map<String, String> SEWAGE_SLUDGE = new HashMap<>();
    private static final Map<String, String> PLANT_PROTECTION = new HashMap<>();
    private static final Map<String, String> SOIL_CULTIVATION = new HashMap<>();
    private static final Map<String, String> SOWING_PLANTING = new HashMap<>();
    private static final Map<String, String> FERTILISATION = new HashMap<>();
    private static final Map<String, String> HARVESTING = new HashMap<>();
    private static final Map<String, String> OTHER_WORK_PROCESSES = new HashMap<>();

    static
    {
        LABELS_FOR_NUMERIC.put("total_herbicides", HERBICIDES);
        LABELS_FOR_NUMERIC.put("total_fungicides", FUNGICIDES);
        LABELS_FOR_NUMERIC.put("total_insecticides", INSECTICIDES);

        COMPOST_TYPE.put("Compost", "compost");
        COMPOST_TYPE.put("Meat-and-bone meal", "meat_and_bone_meal");
        COMPOST_TYPE.put("Castor-oil shell coarse", "castor_oil_shell_coarse");
        COMPOST_TYPE.put("Vinasse", "vinasse");
        COMPOST_TYPE.put("Dried poultry manure", "dried_poultry_manure");
        COMPOST_TYPE.put("Stone meal", "stone_meal");
        COMPOST_TYPE.put("Feather meal", "feather_meal");
        COMPOST_TYPE.put("Horn meal", "horn_meal");
        COMPOST_TYPE.put("Horn shavings fine", "horn_shavings_fine");
        COMPOST_TYPE.put("Other", "other");
        LABELS_FOR_RATIO.put("total_composttype", COMPOST_TYPE);

        SEWAGE_SLUDGE.put("Sewage sludge liquid", "liquid");
        SEWAGE_SLUDGE.put("Sewage sludge dehydrated", "dehydrated");
        SEWAGE_SLUDGE.put("Sewage sludge dried", "dried");
        SEWAGE_SLUDGE.put("Other", "other");
        LABELS_FOR_RATIO.put("total_sewagesludge", SEWAGE_SLUDGE);

        PLANT_PROTECTION.put("Spraying", "spraying");
        PLANT_PROTECTION.put("Flaming", "flaming");
        PLANT_PROTECTION.put("Other", "other");
        LABELS_FOR_RATIO.put("total_plantprotection", PLANT_PROTECTION);

        SOIL_CULTIVATION.put("Soil decompactation", "decompactation");
        SOIL_CULTIVATION.put("Soil tillage, chisel", "tillage_chisel");
        SOIL_CULTIVATION.put("Soil tillage, spring-tine weeder", "tillage_spring_tine_weeder");
        SOIL_CULTIVATION.put("Soil tillage, rotary harrow", "tillage_rotary_harrow");
        SOIL_CULTIVATION.put("Soil tillage, spring-tine harrow", "tillage_sprint_tine_harrow");
        SOIL_CULTIVATION.put("Soil tillage, hoeing and earthing up, potatoes", "tillage_hoeing_earthing_up");
        SOIL_CULTIVATION.put("Soil tillage, plough", "tillage_plough");
        SOIL_CULTIVATION.put("Soil tillage, roll", "tillage_roll");
        SOIL_CULTIVATION.put("Soil tillage, rotary cultivator", "tillage_rotary_cultivator");
        SOIL_CULTIVATION.put("Other", "other");
        LABELS_FOR_RATIO.put("total_soilcultivation", SOIL_CULTIVATION);

        SOWING_PLANTING.put("Sowing", "sowing");
        SOWING_PLANTING.put("Planting seedlings", "planting_seedlings");
        SOWING_PLANTING.put("Planting potatoes", "planting_potatoes");
        SOWING_PLANTING.put("Other", "other");
        LABELS_FOR_RATIO.put("total_sowingplanting", SOWING_PLANTING);

        FERTILISATION.put("Fertilizing, with broadcaster", "fertilizing_broadcaster");
        FERTILISATION.put("Liquid manure application, with vacuum tanker", "liquid_manure_vacuum_tanker");
        FERTILISATION.put("Solid manure application", "solid_manure");
        FERTILISATION.put("Other", "other");
        LABELS_FOR_RATIO.put("total_fertilisation", FERTILISATION);

        HARVESTING.put("Chopping maize", "chopping_maize");
        HARVESTING.put("Threshing, with combine harvester", "threshing_combine_harvester");
        HARVESTING.put("Picking-up the forage, with self-propelled loader", "picking_up_forage_self_propelled_loader");
        HARVESTING.put("Harvesting beets, with complete harvester", "beets_complete_havester");
        HARVESTING.put("Harvesting potatoes, with complete harvester", "potatoes_complete_havester");
        HARVESTING.put("Making hay, with rotary tedder", "making_hay_rotary_tedder");
        HARVESTING.put("Loading bales", "loading_bales");
        HARVESTING.put("Mowing, with motor mower", "mowing_motor_mower");
        HARVESTING.put("Mowing, with rotary mower", "mowing_rotary_mower");
        HARVESTING.put("Removing potato haulms", "removing_potatoes_haulms");
        HARVESTING.put("Windrowing, with rotary swather", "windrowing_rotary_swather");
        HARVESTING.put("Other", "other");
        LABELS_FOR_RATIO.put("total_harvesting", HARVESTING);

        OTHER_WORK_PROCESSES.put("Baling", "baling");
        OTHER_WORK_PROCESSES.put("Chopping", "chopping");
        OTHER_WORK_PROCESSES.put("Mulching", "mulching");
        OTHER_WORK_PROCESSES.put("Transport, tractor and trailer", "transport_tractor_trailer");
        OTHER_WORK_PROCESSES.put("Other", "other");
        LABELS_FOR_RATIO.put("total_otherworkprocesses", OTHER_WORK_PROCESSES);
    }

    private LabelForBlockTags()
    {
    }
}
