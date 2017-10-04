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

public class GlobalTemplateSubstanceUsages implements TemplateSubstanceUsages
{
    @Override
    public TemplateSubstanceUsage[] getResources()
    {
        return resources;
    }

    @Override
    public TemplateSubstanceUsage[] getToAir()
    {
        return toAir;
    }

    @Override
    public TemplateSubstanceUsage[] getToWater()
    {
        return toWater;
    }

    @Override
    public TemplateSubstanceUsage[] getToSoil()
    {
        return toSoil;
    }

    private static final TemplateSubstanceUsage[] resources = {

            new TemplateSubstanceUsage("Carbon dioxide, in air", SubCompartment.RAW_IN_AIR, "kg",
                    "CO2_from_yield", StandardUncertaintyMetadata.CO2_ENERGY_BIOMASS, ""),
            new TemplateSubstanceUsage("Energy, gross calorific value, in biomass", SubCompartment.RAW_UNSPECIFIED,
                    "MJ", "energy_gross_calorific_value", StandardUncertaintyMetadata.CO2_ENERGY_BIOMASS, ""),

            new TemplateSubstanceUsage("Water, well, in ground, {country}", SubCompartment.RAW_IN_WATER,
                    "m3", "wateruse_ground", StandardUncertaintyMetadata.IRRIGATION_WATER, "wateruse_ground"),
            new TemplateSubstanceUsage("Water, river, {country}", SubCompartment.RAW_IN_WATER,
                    "m3", "wateruse_surface", StandardUncertaintyMetadata.IRRIGATION_WATER, "wateruse_surface"),

            new TemplateSubstanceUsage("Water, well, in ground, {country}", SubCompartment.RAW_IN_WATER,
                    "m3", "utilities_wateruse_ground", StandardUncertaintyMetadata.UTILITIES_WATER,
                    "utilities_wateruse_ground"),
            new TemplateSubstanceUsage("Water, river, {country}", SubCompartment.RAW_IN_WATER,
                    "m3", "utilities_wateruse_surface", StandardUncertaintyMetadata.UTILITIES_WATER,
                    "utilities_wateruse_surface"),

            new TemplateSubstanceUsage("Occupation, annual crop, organic", SubCompartment.RAW_LAND, "m2a",
                                       "occupation_annual_organic", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, annual crop, non-irrigated", SubCompartment.RAW_LAND, "m2a",
                                       "occupation_annual_non-irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, annual crop, irrigated", SubCompartment.RAW_LAND, "m2a",
                                       "occupation_annual_irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, annual crop, greenhouse", SubCompartment.RAW_LAND, "m2a",
                                       "occupation_annual_greenhouse", StandardUncertaintyMetadata.LAND_OCCUPATION,
                                       ""),
            new TemplateSubstanceUsage("Occupation, permanent crop, irrigated", SubCompartment.RAW_LAND, "m2a",
                                       "occupation_peren_irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, permanent crop, non-irrigated", SubCompartment.RAW_LAND, "m2a",
                                       "occupation_peren_non-irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Transformation, from annual crop, irrigated", SubCompartment.RAW_LAND, "m2",
                                       "transformation_from_annual_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from annual crop, non-irrigated", SubCompartment.RAW_LAND, "m2",
                                       "transformation_from_annual_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from annual crop, greenhouse", SubCompartment.RAW_LAND, "m2",
                                       "transformation_from_annual_greenhouse",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from permanent crop, irrigated", SubCompartment.RAW_LAND, "m2",
                                       "transformation_from_peren_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from permanent crop, non-irrigated", SubCompartment.RAW_LAND,
                                       "m2", "transformation_from_peren_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to annual crop, irrigated", SubCompartment.RAW_LAND, "m2",
                                       "transformation_to_annual_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to annual crop, non-irrigated", SubCompartment.RAW_LAND, "m2",
                                       "transformation_to_annual_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to annual crop, greenhouse", SubCompartment.RAW_LAND, "m2",
                                       "transformation_to_annual_greenhouse",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to permanent crop, irrigated", SubCompartment.RAW_LAND, "m2",
                                       "transformation_to_peren_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to permanent crop, non-irrigated", SubCompartment.RAW_LAND,
                                       "m2", "transformation_to_peren_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, "")
    };

    private static final TemplateSubstanceUsage[] toAir = {
            new TemplateSubstanceUsage("Ammonia", SubCompartment.AIR_LOW_POP, "kg", "ammonia_total",
                    StandardUncertaintyMetadata.CH4_NH3_TO_AIR, ""),
            new TemplateSubstanceUsage("Carbon dioxide, fossil", SubCompartment.AIR_LOW_POP, "kg",
                    "CO2_from_fertilisers", StandardUncertaintyMetadata.CO2_EMISSIONS, ""),
            new TemplateSubstanceUsage("Nitrogen oxides", SubCompartment.AIR_LOW_POP, "kg", "Nox_as_n2o_air",
                    StandardUncertaintyMetadata.N2O_NOX_TO_AIR, ""),
            new TemplateSubstanceUsage("Dinitrogen monoxide", SubCompartment.AIR_LOW_POP, "kg", "N2o_air",
                    StandardUncertaintyMetadata.N2O_NOX_TO_AIR, ""),
            // FIXME: Not the right StandardUncertaintyMetadata
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.AIR_LOW_POP, "ton", "water_to_air",
                    StandardUncertaintyMetadata.CH4_NH3_TO_AIR, "")
    };

    private static final TemplateSubstanceUsage[] toWater = {
            new TemplateSubstanceUsage("Nitrate", SubCompartment.WATER_RIVER, "kg", "nitrate_to_surfacewater",
                    StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Nitrate", SubCompartment.WATER_GROUNDWATER, "kg", "nitrate_to_groundwater",
                    StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Phosphorus", SubCompartment.WATER_RIVER, "kg", "P_surfacewater_erosion",
                    StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Phosphate", SubCompartment.WATER_RIVER, "kg", "PO4_surfacewater",
                    StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Phosphate", SubCompartment.WATER_GROUNDWATER, "kg", "PO4_groundwater",
                    StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Cadmium", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_cd", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Cadmium", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_cd", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Chromium", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_cr", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Chromium", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_cr", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Copper", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_cu", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Copper", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_cu", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Lead", SubCompartment.WATER_RIVER, "kg", "heavymetal_to_surface_water_pb",
                    StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Lead", SubCompartment.WATER_GROUNDWATER, "kg", "heavymetal_to_ground_water_pb",
                    StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Mercury", SubCompartment.WATER_RIVER, "kg", "heavymetal_to_surface_water_hg",
                    StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Mercury", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_hg", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Nickel", SubCompartment.WATER_RIVER, "kg",
                    "heavymetal_to_surface_water_ni", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Nickel", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_ni", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Zinc", SubCompartment.WATER_RIVER, "kg", "heavymetal_to_surface_water_zn",
                    StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Zinc", SubCompartment.WATER_GROUNDWATER, "kg",
                    "heavymetal_to_ground_water_zn", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.WATER_RIVER, "m3", "water_to_water_river",
                    StandardUncertaintyMetadata.WATER_EMISSIONS, ""),
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.WATER_GROUNDWATER, "m3",
                    "water_to_water_groundwater", StandardUncertaintyMetadata.WATER_EMISSIONS, ""),
            new TemplateSubstanceUsage("Water, {country}", SubCompartment.WATER_RIVER, "m3",
                    "eol_waste_water_to_nature", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                    "eol_waste_water_to_nature"),
            new TemplateSubstanceUsage("COD, Chemical Oxygen Demand", SubCompartment.WATER_GROUNDWATER, "g",
                                       "cod_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water"),
            new TemplateSubstanceUsage("DOC, Dissolved Organic Carbon", SubCompartment.WATER_GROUNDWATER, "g",
                                       "cod_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water"),
            new TemplateSubstanceUsage("TOC, Total Organic Carbon", SubCompartment.WATER_GROUNDWATER, "g",
                                       "cod_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water"),
            new TemplateSubstanceUsage("BOD5, Biological Oxygen Demand", SubCompartment.WATER_GROUNDWATER, "g",
                                       "cod_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water")
    };

    private static final TemplateSubstanceUsage[] toSoil = {
            new TemplateSubstanceUsage("Cadmium", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_cd",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Chromium", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_cr",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Copper", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_cu",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Lead", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_pb",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Mercury", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_hg",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Nickel", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_ni",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Zinc", SubCompartment.SOIL_AGRICULTURAL, "kg", "heavymetal_to_soil_zn",
                    StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Mineral oil", SubCompartment.SOIL_AGRICULTURAL, "kg",
                    "pest_horticultural_oil", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL,
                    "pest_horticultural_oil"),
    };
}
