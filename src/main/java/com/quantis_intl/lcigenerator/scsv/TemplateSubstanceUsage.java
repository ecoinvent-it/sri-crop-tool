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
package com.quantis_intl.lcigenerator.scsv;

import com.quantis_intl.commons.scsv.ScsvEnums.SubCompartment;

public class TemplateSubstanceUsage
{
    public final String name;
    public final SubCompartment subCompartment;
    public final String unit;
    public final String amountVariable;
    public final String commentVariable;

    public TemplateSubstanceUsage(String name, SubCompartment subCompartment, String unit, String amountVariable,
            String commentVariable)
    {
        this.name = name;
        this.subCompartment = subCompartment;
        this.unit = unit;
        this.amountVariable = amountVariable;
        this.commentVariable = commentVariable;
    }

    public static final TemplateSubstanceUsage[] resources = {
            // LUC: new TemplateSubstanceUsage("Occupation, arable", "", "m2a", "", ""),
            // LUC: new TemplateSubstanceUsage("Occupation, permanent crop", "", "m2a", "", ""),
            // LUC: new TemplateSubstanceUsage("Tranformation, from {TODO}", "", "m2", "", ""),
            // LUC: new TemplateSubstanceUsage("Tranformation, to {TODO}", "", "m2", "", ""),
            // LUC: new TemplateSubstanceUsage("Gross energy {TODO}", "", "MJ", "", ""),
            // LUC: new TemplateSubstanceUsage("Carbon dioxide, in air", "", "kg", "", ""),
            new TemplateSubstanceUsage("Carbon dioxide, in air", SubCompartment.RAW_UNSPECIFIED, "kg",
                    "CO2_from_yield", ""),
            new TemplateSubstanceUsage("Energy, gross calorific value, in biomass", SubCompartment.RAW_UNSPECIFIED,
                    "MJ",
                    "energy_gross_calorific_value", "")
    };

    public static final TemplateSubstanceUsage[] toAir = {
            new TemplateSubstanceUsage("Carbon dioxide, land transformation", SubCompartment.AIR_LOW_POP, "kg", "", ""),
            new TemplateSubstanceUsage("Ammonia", SubCompartment.AIR_LOW_POP, "kg", "ammonia_total", ""),
            new TemplateSubstanceUsage("Carbon dioxide, fossil", SubCompartment.AIR_LOW_POP, "kg",
                    "CO2_from_fertilisers", ""),
            new TemplateSubstanceUsage("Nitrogen oxides", SubCompartment.AIR_LOW_POP, "kg", "Nox_as_n2o_air", ""),
            new TemplateSubstanceUsage("Dinitrogen monoxide", SubCompartment.AIR_LOW_POP, "kg", "N2o_air", ""),
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.AIR_LOW_POP, "kg", "water_to_air", "")
    };

    public static final TemplateSubstanceUsage[] toWater = {
            new TemplateSubstanceUsage("Nitrate", SubCompartment.WATER_RIVER, "kg", "nitrate_to_surfacewater", ""),
            new TemplateSubstanceUsage("Nitrate", SubCompartment.WATER_GROUNDWATER, "kg", "nitrate_to_groundwater", ""),
            new TemplateSubstanceUsage("Phosphorus", SubCompartment.WATER_RIVER, "kg", "P_surfacewater_erosion", ""),
            new TemplateSubstanceUsage("Phosphate", SubCompartment.WATER_RIVER, "kg", "PO4_surfacewater", ""),
            new TemplateSubstanceUsage("Phosphate", SubCompartment.WATER_GROUNDWATER, "kg", "PO4_groundwater", ""),
            new TemplateSubstanceUsage("Cadmium, ion", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_cd", ""),
            new TemplateSubstanceUsage("Cadmium, ion", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_cd", ""),
            new TemplateSubstanceUsage("Chromium, ion", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_cr", ""),
            new TemplateSubstanceUsage("Chromium, ion", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_cr", ""),
            new TemplateSubstanceUsage("Copper, ion", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_cu", ""),
            new TemplateSubstanceUsage("Copper, ion", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_cu", ""),
            new TemplateSubstanceUsage("Lead", SubCompartment.WATER_RIVER, "kg", "heavymetal_to_surface_water_pb", ""),
            new TemplateSubstanceUsage("Lead", SubCompartment.WATER_GROUNDWATER, "kg", "heavymetal_to_ground_water_pb",
                    ""),
            new TemplateSubstanceUsage("Mercury", SubCompartment.WATER_RIVER, "kg", "heavymetal_to_surface_water_hg",
                    ""),
            new TemplateSubstanceUsage("Mercury", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_hg", ""),
            new TemplateSubstanceUsage("Nickel, ion", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_ni", ""),
            new TemplateSubstanceUsage("Nickel, ion", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_ni", ""),
            new TemplateSubstanceUsage("Zinc, ion", SubCompartment.WATER_RIVER, "kg", "heavymetal_to_surface_water_zn",
                    ""),
            new TemplateSubstanceUsage("Zinc, ion", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_zn", ""),
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.WATER_RIVER, "m3", "water_to_water_river", ""),
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.WATER_GROUNDWATER, "m3",
                    "water_to_water_groundwater", "")
    };

    public static final TemplateSubstanceUsage[] toSoil = {
            new TemplateSubstanceUsage("Cadmium", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_cd", ""),
            new TemplateSubstanceUsage("Chromium", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_cr", ""),
            new TemplateSubstanceUsage("Copper", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_cu", ""),
            new TemplateSubstanceUsage("Lead", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_pb", ""),
            new TemplateSubstanceUsage("Mercury", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_hg", ""),
            new TemplateSubstanceUsage("Nickel", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_ni", ""),
            new TemplateSubstanceUsage("Zinc", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_zn", "")
            // FIXME: Pesticides
    };
}
