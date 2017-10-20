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
package com.quantis_intl.lcigenerator.ecospold;

import java.util.UUID;

import com.quantis_intl.lcigenerator.scsv.StandardUncertaintyMetadata;

public class EcospoldTemplateSubstanceUsages implements TemplateSubstanceUsages
{
    private static final UUID SOIL_UNSPECIFID = UUID.fromString("dbeb0ac7-0dec-439e-887a-9924cc8005dd");
    private static final UUID SOIL_FORESTRY = UUID.fromString("15f47463-77ea-40d0-bfe8-ca632819f556");
    private static final UUID SOIL_AGRICULTURAL = UUID.fromString("e1bc9a16-5b6a-494f-98ef-49f461b1a11e");
    private static final UUID SOIL_INSUTRIAL = UUID.fromString("912f1ae3-734e-4cc6-bbf7-0f36843cd7de");
    private static final UUID RESOURCE_IN_AIR = UUID.fromString("45bb416c-a63b-429f-8754-b3f76a069c43");
    private static final UUID RESOURCE_LAND = UUID.fromString("7d704b6f-d455-4f41-9c28-50b4f372f315");
    private static final UUID RESOURCE_IN_GROUND = UUID.fromString("6a098164-9f04-4f65-8104-ffab7f2677f3");
    private static final UUID RESOURCE_IN_WATER = UUID.fromString("30347aef-a90b-46ba-8746-b53741aa779d");
    private static final UUID RESOURCE_BIOTIC = UUID.fromString("2d0acbd3-2083-4011-9a29-20c626b23dc3");
    private static final UUID AIR_NON_URBAN = UUID.fromString("be7e06e9-0bf5-462e-99dc-fe4aee383c48");
    private static final UUID AIR_URBAN = UUID.fromString("e8d7772c-55ca-4dd7-b605-fee5ae764578");
    private static final UUID AIR_LOW_POP_LONG_TERM = UUID.fromString("23dbff79-8037-43e7-b270-5a3da416a284");
    private static final UUID AIR_INDOOR = UUID.fromString("6890faa1-312e-427d-9ca8-42309afc8866");
    private static final UUID AIR_LOWER_STRATO = UUID.fromString("f335ce0e-b830-475a-adab-03858d9cbdaf");
    private static final UUID AIR_UNSPECIFIED = UUID.fromString("7011f0aa-f5f9-4901-8c10-884ad8296812");
    private static final UUID WATER_GROUND = UUID.fromString("a119c440-7e83-4655-a874-97fe1468315a");
    private static final UUID WATER_UNSPECIFID = UUID.fromString("e47f0a6c-3be8-4027-9eee-de251784f708");
    private static final UUID WATER_SURFACE = UUID.fromString("963f8022-3e2e-4be9-ad4d-b3b7a2282099");
    private static final UUID WATER_GROUND_LONG_TERM = UUID.fromString("aa4362e0-b20a-448b-b2a0-261f4510deb5");
    private static final UUID WATER_OCEAN = UUID.fromString("65f8d2a1-63ed-479c-b86c-3bcf38e86320");


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

            new TemplateSubstanceUsage("Carbon dioxide, in air", RESOURCE_IN_AIR, AvailableUnit.KG,
                                       "CO2_from_yield", StandardUncertaintyMetadata.CO2_ENERGY_BIOMASS, ""),
            new TemplateSubstanceUsage("Energy, gross calorific value, in biomass", RESOURCE_BIOTIC,
                                       AvailableUnit.MJ, "energy_gross_calorific_value",
                                       StandardUncertaintyMetadata.CO2_ENERGY_BIOMASS, ""),

            new TemplateSubstanceUsage("Water, river", RESOURCE_IN_WATER,
                                       AvailableUnit.M3, "wateruse_ground",
                                       StandardUncertaintyMetadata.IRRIGATION_WATER,
                                       "wateruse_ground"),
            new TemplateSubstanceUsage("Water, river", RESOURCE_IN_WATER,
                                       AvailableUnit.M3, "wateruse_surface",
                                       StandardUncertaintyMetadata.IRRIGATION_WATER,
                                       "wateruse_surface"),

            new TemplateSubstanceUsage("Water, river", RESOURCE_IN_WATER,
                                       AvailableUnit.M3, "utilities_wateruse_ground",
                                       StandardUncertaintyMetadata.UTILITIES_WATER,
                                       "utilities_wateruse_ground"),
            new TemplateSubstanceUsage("Water, river", RESOURCE_IN_WATER,
                                       AvailableUnit.M3, "utilities_wateruse_surface",
                                       StandardUncertaintyMetadata.UTILITIES_WATER,
                                       "utilities_wateruse_surface"),

            new TemplateSubstanceUsage("Occupation, annual crop, non-irrigated", RESOURCE_LAND, AvailableUnit.M2_YEAR,
                                       "occupation_annual_organic_non-irr", StandardUncertaintyMetadata.LAND_OCCUPATION,
                                       ""),
            new TemplateSubstanceUsage("Occupation, annual crop, irrigated", RESOURCE_LAND, AvailableUnit.M2_YEAR,
                                       "occupation_annual_organic_irr", StandardUncertaintyMetadata.LAND_OCCUPATION,
                                       ""),
            new TemplateSubstanceUsage("Occupation, annual crop, non-irrigated", RESOURCE_LAND, AvailableUnit.M2_YEAR,
                                       "occupation_annual_non-irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, annual crop, irrigated", RESOURCE_LAND, AvailableUnit.M2_YEAR,
                                       "occupation_annual_irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, annual crop, greenhouse", RESOURCE_LAND, AvailableUnit.M2_YEAR,
                                       "occupation_annual_greenhouse", StandardUncertaintyMetadata.LAND_OCCUPATION,
                                       ""),
            new TemplateSubstanceUsage("Occupation, permanent crop, irrigated", RESOURCE_LAND, AvailableUnit.M2_YEAR,
                                       "occupation_peren_irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Occupation, permanent crop, non-irrigated", RESOURCE_LAND,
                                       AvailableUnit.M2_YEAR,
                                       "occupation_peren_non-irr", StandardUncertaintyMetadata.LAND_OCCUPATION, ""),
            new TemplateSubstanceUsage("Transformation, from annual crop, irrigated", RESOURCE_LAND, AvailableUnit.M2,
                                       "transformation_from_annual_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from annual crop, non-irrigated", RESOURCE_LAND,
                                       AvailableUnit.M2,
                                       "transformation_from_annual_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from annual crop, greenhouse", RESOURCE_LAND, AvailableUnit.M2,
                                       "transformation_from_annual_greenhouse",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from permanent crop, irrigated", RESOURCE_LAND,
                                       AvailableUnit.M2,
                                       "transformation_from_peren_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, from permanent crop, non-irrigated", RESOURCE_LAND,
                                       AvailableUnit.M2, "transformation_from_peren_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to annual crop, irrigated", RESOURCE_LAND, AvailableUnit.M2,
                                       "transformation_to_annual_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to annual crop, non-irrigated", RESOURCE_LAND, AvailableUnit.M2,
                                       "transformation_to_annual_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to annual crop, greenhouse", RESOURCE_LAND, AvailableUnit.M2,
                                       "transformation_to_annual_greenhouse",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to permanent crop, irrigated", RESOURCE_LAND, AvailableUnit.M2,
                                       "transformation_to_peren_irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),
            new TemplateSubstanceUsage("Transformation, to permanent crop, non-irrigated", RESOURCE_LAND,
                                       AvailableUnit.M2, "transformation_to_peren_non-irr",
                                       StandardUncertaintyMetadata.LAND_TRANSFORMATION, "")
    };

    private static final TemplateSubstanceUsage[] toAir = {
            new TemplateSubstanceUsage("Ammonia", AIR_NON_URBAN, AvailableUnit.KG, "ammonia_total",
                                       StandardUncertaintyMetadata.CH4_NH3_TO_AIR, ""),
            new TemplateSubstanceUsage("Carbon dioxide, fossil", AIR_NON_URBAN, AvailableUnit.KG,
                                       "CO2_from_fertilisers", StandardUncertaintyMetadata.CO2_EMISSIONS, ""),
            new TemplateSubstanceUsage("Nitrogen oxides", AIR_NON_URBAN, AvailableUnit.KG, "Nox_as_n2o_air",
                                       StandardUncertaintyMetadata.N2O_NOX_TO_AIR, ""),
            new TemplateSubstanceUsage("Dinitrogen monoxide", AIR_NON_URBAN, AvailableUnit.KG, "N2o_air",
                                       StandardUncertaintyMetadata.N2O_NOX_TO_AIR, ""),
            // FIXME: Not the right StandardUncertaintyMetadata
            new TemplateSubstanceUsage("Water", AIR_NON_URBAN, AvailableUnit.M3, "ecoinvent_water_to_air",
                                       StandardUncertaintyMetadata.CH4_NH3_TO_AIR, "")
    };

    private static final TemplateSubstanceUsage[] toWater = {
            new TemplateSubstanceUsage("Nitrate", WATER_SURFACE, AvailableUnit.KG, "nitrate_to_surfacewater",
                                       StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Nitrate", WATER_GROUND, AvailableUnit.KG, "nitrate_to_groundwater",
                                       StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Phosphorus", WATER_SURFACE, AvailableUnit.KG, "P_surfacewater_erosion",
                                       StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Phosphate", WATER_SURFACE, AvailableUnit.KG, "PO4_surfacewater",
                                       StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Phosphate", WATER_GROUND, AvailableUnit.KG, "PO4_groundwater",
                                       StandardUncertaintyMetadata.NO3_PO4_TO_WATER, ""),
            new TemplateSubstanceUsage("Cadmium, ion", WATER_SURFACE, AvailableUnit.KG,
                                       "heavymetal_to_surface_water_cd", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Cadmium, ion", WATER_GROUND, AvailableUnit.KG,
                                       "heavymetal_to_ground_water_cd", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Chromium, ion", WATER_SURFACE, AvailableUnit.KG,
                                       "heavymetal_to_surface_water_cr", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Chromium, ion", WATER_GROUND, AvailableUnit.KG,
                                       "heavymetal_to_ground_water_cr", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Copper, ion", WATER_SURFACE, AvailableUnit.KG,
                                       "heavymetal_to_surface_water_cu", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Copper, ion", WATER_GROUND, AvailableUnit.KG,
                                       "heavymetal_to_ground_water_cu", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Lead", WATER_SURFACE, AvailableUnit.KG, "heavymetal_to_surface_water_pb",
                                       StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Lead", WATER_GROUND, AvailableUnit.KG, "heavymetal_to_ground_water_pb",
                                       StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Mercury", WATER_SURFACE, AvailableUnit.KG, "heavymetal_to_surface_water_hg",
                                       StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Mercury", WATER_GROUND, AvailableUnit.KG,
                                       "heavymetal_to_ground_water_hg", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Nickel, ion", WATER_SURFACE, AvailableUnit.KG,
                                       "heavymetal_to_surface_water_ni", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Nickel, ion", WATER_GROUND, AvailableUnit.KG,
                                       "heavymetal_to_ground_water_ni", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Zinc, ion", WATER_SURFACE, AvailableUnit.KG, "heavymetal_to_surface_water_zn",
                                       StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Zinc, ion", WATER_GROUND, AvailableUnit.KG,
                                       "heavymetal_to_ground_water_zn", StandardUncertaintyMetadata.HM_TO_WATER, ""),
            new TemplateSubstanceUsage("Water", WATER_SURFACE, AvailableUnit.M3, "ecoinvent_water_to_water_river",
                                       StandardUncertaintyMetadata.WATER_EMISSIONS, ""),
            new TemplateSubstanceUsage("Water", WATER_GROUND, AvailableUnit.M3,
                                       "ecoinvent_water_to_water_groundwater",
                                       StandardUncertaintyMetadata.WATER_EMISSIONS, ""),
            new TemplateSubstanceUsage("Water", WATER_SURFACE, AvailableUnit.M3,
                                       "eol_waste_water_to_nature", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                       "eol_waste_water_to_nature"),
            new TemplateSubstanceUsage("COD, Chemical Oxygen Demand", WATER_GROUND, AvailableUnit.KG,
                                       "cod_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water"),
            new TemplateSubstanceUsage("DOC, Dissolved Organic Carbon", WATER_GROUND, AvailableUnit.KG,
                                       "doc_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water"),
            new TemplateSubstanceUsage("TOC, Total Organic Carbon", WATER_GROUND, AvailableUnit.KG,
                                       "toc_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water"),
            new TemplateSubstanceUsage("BOD5, Biological Oxygen Demand", WATER_GROUND, AvailableUnit.KG,
                                       "bod5_in_waste_water", StandardUncertaintyMetadata.COD_IN_WASTEWATER,
                                       "cod_in_waste_water")
    };

    private static final TemplateSubstanceUsage[] toSoil = {
            new TemplateSubstanceUsage("Cadmium", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_cd",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Chromium", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_cr",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Copper", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_cu",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Lead", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_pb",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Mercury", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_hg",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Nickel", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_ni",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Zinc", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "heavymetal_to_soil_minus_uptake_zn",
                                       StandardUncertaintyMetadata.HM_TO_SOIL, ""),
            new TemplateSubstanceUsage("Mineral oil", SOIL_AGRICULTURAL, AvailableUnit.KG,
                                       "pest_horticultural_oil",
                                       StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL,
                                       "pest_horticultural_oil"),
            };
}
