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

public class TemplateProductUsage
{
    public final String name;
    public final String unit;
    public final String amountVariable;
    public final String commentVariable;

    public TemplateProductUsage(String name, String unit, String amountVariable, String commentVariable)
    {
        this.name = name;
        this.unit = unit;
        this.amountVariable = amountVariable;
        this.commentVariable = commentVariable;
    }

    public static final TemplateProductUsage[] materialsFuels = {
            new TemplateProductUsage("Packaging, per kg of dry fertilizers or pesticides (WFLDB 3.0)/GLO U", "kg",
                    "packaging_solid_fertilisers_and_pesticides", ""),
            new TemplateProductUsage("Packaging, per kg of liquid fertilizers or pesticides (WFLDB 3.0)/GLO U", "kg",
                    "packaging_liquid_fertilisers_and_pesticides", ""),

            new TemplateProductUsage("Grain drying, high temperature/CH U", "kg", "drying_heating", ""),
            new TemplateProductUsage("Grain drying, low temperature/CH U", "kg", "drying_ambient_air", ""),

            new TemplateProductUsage("Irrigating, surface, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "surface_irrigation_electricity", ""),
            new TemplateProductUsage("Irrigating, surface, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "surface_irrigation_diesel", ""),
            // new TemplateProductUsage("Irrigating, surface, gravity/GLO U", "m3", "surface_irrigation_no_energy",
            // ""),// FIXME: Missing
            new TemplateProductUsage("Irrigating, sprinkler, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "sprinkler_irrigation_electricity", ""),
            new TemplateProductUsage("Irrigating, sprinkler, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "sprinkler_irrigation_diesel", ""),
            new TemplateProductUsage("Irrigating, drip, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "drip_irrigation_electricity", ""),
            new TemplateProductUsage("Irrigating, drip, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "drip_irrigation_diesel", ""),

            new TemplateProductUsage("Tap water, at user/RER U", "t", "wateruse_non_conventional_sources", ""),

            new TemplateProductUsage("Ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_nitrate", ""),
            new TemplateProductUsage("Urea, as N, at regional storehouse/RER U", "kg", "fert_n_urea", ""),
            new TemplateProductUsage("Urea ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ureaAN", ""),
            new TemplateProductUsage("Monoammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_mono_ammonium_phosphate", ""),
            new TemplateProductUsage("Diammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_di_ammonium_phosphate", ""),
            new TemplateProductUsage("Ammonium nitrate phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_an_phosphate", ""),
            new TemplateProductUsage("Calcium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_lime_ammonium_nitrate", ""),
            new TemplateProductUsage("Ammonium sulphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_sulphate", ""),
            new TemplateProductUsage("Potassium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_potassium_nitrate", ""),
            new TemplateProductUsage("Ammonia, liquid, at regional storehouse/RER U", "kg",
                    "fert_n_ammonia_liquid_as_nh3", ""),

            new TemplateProductUsage("Triple superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_triple_superphosphate", ""),
            new TemplateProductUsage("Single superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_superphosphate", ""),
            new TemplateProductUsage("Monoammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_mono_ammonium_phosphate", ""),
            new TemplateProductUsage("Diammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_di_ammonium_phosphate", ""),
            new TemplateProductUsage("Ammonium nitrate phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_an_phosphate", ""),
            new TemplateProductUsage("Phosphate rock, as P2O5, beneficiated, dry, at plant/MA U", "kg",
                    "fert_p_hypophosphate_raw_phosphate", ""),
            new TemplateProductUsage("Thomas meal, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_ground_basic_slag", ""),

            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_salt", ""),
            new TemplateProductUsage("Potassium sulphate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_sulphate", ""),
            new TemplateProductUsage("Potassium nitrate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_nitrate", ""),
            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_patent_potassium", ""),

            new TemplateProductUsage("Magnesium sulphate, at plant/RER U", "kg", "magnesium_from_fertilizer", ""),
            new TemplateProductUsage("Limestone, milled, packed, at plant/CH U", "kg", "fert_ca_limestone", ""),
            new TemplateProductUsage("Lime, from carbonation, at regional storehouse/CH U", "kg",
                    "fert_ca_carbonation_linestone", ""),
            new TemplateProductUsage("Lime, algae, at regional storehouse/CH U", "kg", "fert_ca_seaweed_limestone",
                    ""),

            new TemplateProductUsage("Compost, at plant/CH U", "kg", "composttype_compost", ""),
            new TemplateProductUsage("Vinasse, at fermentation plant/CH U", "kg", "composttype_vinasse", ""),
            new TemplateProductUsage("poultry manure, dried, at regional storehouse/CH U", "kg",
                    "composttype_dried_poultry_manure", ""),
            new TemplateProductUsage("Stone meal, at regional storehouse/CH U", "kg", "composttype_stone_meal", ""),
            new TemplateProductUsage("Horn meal, at regional storehouse/CH U", "kg", "composttype_horn_meal", ""),
            new TemplateProductUsage("horn meal, at regional storehouse/CH U", "kg", "composttype_horn_shavings_fine",
                    ""),

            // TODO: Pesticides

            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "remains_machinery_diesel", ""),
            new TemplateProductUsage("Application of plant protection products, by field sprayer/CH U", "ha",
                    "plantprotection_spraying", ""),
            new TemplateProductUsage("Natural gas, burned in boiler modulating <100kW/RER U", "MJ",
                    "plantprotection_flaming", ""),
            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "plantprotection_other", ""),
            new TemplateProductUsage("Soil decompactation, with 4.5m chisel/FR U", "hr",
                    "soilcultivation_decompaction", ""),
            // FIXME: Confirm processes name (two in WFLDB)
            new TemplateProductUsage("Tillage, cultivating, chiselling/CH U", "ha", "soilcultivation_tillage_chisel",
                    ""),
            new TemplateProductUsage("Tillage, currying, by weeder/CH U", "ha",
                    "soilcultivation_tillage_spring_tine_weeder", ""),
            new TemplateProductUsage("Tillage, harrowing, by rotary harrow/CH U", "ha",
                    "soilcultivation_tillage_rotary_harrow", ""),
            new TemplateProductUsage("Tillage, harrowing, by spring tine harrow/CH U", "ha",
                    "soilcultivation_tillage_sprint_tine_harrow", ""),
            new TemplateProductUsage("Tillage, hoeing and earthing-up, potatoes/CH U", "ha",
                    "soilcultivation_tillage_hoeing_earthing_up", ""),
            new TemplateProductUsage("Tillage, ploughing/CH U", "ha", "soilcultivation_tillage_plough", ""),
            new TemplateProductUsage("Tillage, rolling/CH U", "ha", "soilcultivation_tillage_roll", ""),
            new TemplateProductUsage("Tillage, rotary cultivator/CH U", "ha",
                    "soilcultivation_tillage_rotary_cultivator", ""),
            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "soilcultivation_other", ""),
            new TemplateProductUsage("Sowing/CH U", "ha", "sowingplanting_sowing", ""),
            new TemplateProductUsage("Planting/CH U", "ha", "sowingplanting_planting_seedlings", ""),
            new TemplateProductUsage("Plantation of trees (WFLDB 3.0)/p/CH U", "p", "sowingplanting_planting_trees", ""),
            new TemplateProductUsage("Potato planting/CH U", "ha", "sowingplanting_planting_potatoes", ""),
            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "sowingplanting_other", ""),
            new TemplateProductUsage("Fertilising, by broadcaster/CH U", "ha", "fertilisation_fertilizing_broadcaster",
                    ""),
            new TemplateProductUsage("Slurry spreading, by vacuum tanker/CH U", "m3",
                    "fertilisation_liquid_manure_vacuum_tanker", ""),
            new TemplateProductUsage("Solid manure loading and spreading, by hydraulic loader and spreader/CH U", "t",
                    "fertilisation_solid_manure", ""),
            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "fertilisation_other", ""),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "harvesting_chopping_maize", ""),
            new TemplateProductUsage("Combine harvesting/CH U", "ha", "harvesting_threshing_combine_harvester", ""),
            new TemplateProductUsage("Fodder loading, by self-loading trailer/CH U", "m3",
                    "harvesting_picking_up_forage_self_propelled_loader", ""),
            new TemplateProductUsage("Harvesting, by complete harvester, beets/CH U", "ha",
                    "harvesting_beets_complete_havester", ""),
            new TemplateProductUsage("Harvesting, by complete harvester, potatoes/CH U", "ha",
                    "harvesting_potatoes_complete_havester", ""),
            new TemplateProductUsage("Haying, by rotary tedder/CH U", "ha", "harvesting_making_hay_rotary_tedder", ""),
            new TemplateProductUsage("Loading bales/CH U", "unit", "harvesting_loading_bales", ""),
            new TemplateProductUsage("Mowing, by motor mower/CH U", "ha", "harvesting_mowing_motor_mower", ""),
            new TemplateProductUsage("Mowing, by rotary mower/CH U", "ha", "harvesting_mowing_rotary_mower", ""),
            new TemplateProductUsage("Potato haulm cutting/CH U", "ha", "harvesting_removing_potatoes_haulms", ""),
            new TemplateProductUsage("Swath, by rotary windrower/CH U", "ha", "harvesting_windrowing_rotary_swather",
                    ""),
            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "harvesting_other", ""),

            new TemplateProductUsage("Baling/CH U", "unit", "otherworkprocesses_baling", ""),// in other work processes
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "otherworkprocesses_chopping", ""),
            new TemplateProductUsage("Mulching/CH U", "ha", "otherworkprocesses_mulching", ""),
            new TemplateProductUsage("Transport, tractor and trailer/CH U", "tkm",
                    "otherworkprocesses_transport_tractor_trailer", ""),
            // FIXME: This process has maybe been renamed
            new TemplateProductUsage("Diesel combusted in agricultural machinery (WFLDB 3.0)/GLO U", "kg",
                    "otherworkprocesses_other", ""),

            new TemplateProductUsage("Electricity, low voltage, at grid/{country} U", "kWh",
                    "energy_electricity_low_voltage_at_grid", ""),
            new TemplateProductUsage("Electricity, production mix photovoltaic, at plant/{country} U", "kWh",
                    "energy_electricity_photovoltaic_produced_locally", ""),
            new TemplateProductUsage("Electricity, at wind power plant/RER U", "kWh",
                    "energy_electricity_wind_power_produced_locally", ""),
            new TemplateProductUsage("Diesel combustion, in tractor/kg/CH U", "kg",
                    "energy_diesel_excluding_diesel_used_in_tractor", ""),
            new TemplateProductUsage("Lignite briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_lignite_briquette", ""),
            new TemplateProductUsage("Hard coal briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_hard_coal_briquette", ""),
            new TemplateProductUsage("Light fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "energy_fuel_oil_light", ""),
            new TemplateProductUsage("Heavy fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "energy_fuel_oil_heavy", ""),
            new TemplateProductUsage("Natural gas, burned in industrial furnace >100kW/RER U", "MJ",
                    "energy_natural_gas", ""),
            new TemplateProductUsage("Pellets, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_pellets_humidity_10_percent", ""),
            new TemplateProductUsage("Wood chips, from industry, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_chips_fresh_humidity_40_percent", ""),
            new TemplateProductUsage("Logs, mixed, burned in furnace 100kW/CH U", "MJ", "energy_wood_logs", ""),
            new TemplateProductUsage("Heat, natural gas, at industrial furnace >100kW/RER U", "MJ",
                    "energy_heat_district_heating", ""),
            new TemplateProductUsage("Heat, at flat plate collector, multiple dwelling, for hot water/CH U", "MJ",
                    "energy_heat_solar_collector", ""),

            new TemplateProductUsage("Tap water, at user/RER U", "kg", "utilities_wateruse_non_conventional_sources",
                    ""),

            new TemplateProductUsage("Polypropylene, granulate, at plant/RER U", "kg", "materials_fleece", ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_silage_foil", ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_covering_sheet",
                    ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_bird_net", ""),

            new TemplateProductUsage("Plastic tunnel/FR U", "m2", "greenhouse_plastic_tunnel", ""),
            new TemplateProductUsage("Greenhouse, glass walls and roof, metal tubes (WFLDB 3.0)/FR/I U", "m2",
                    "greenhouse_glass_roof_metal_tubes", ""),
            new TemplateProductUsage("Greenhouse, glass walls and roof, plastic tubes (WFLDB 3.0)/FR/I U", "m2",
                    "greenhouse_glass_roof_plastic_tubes", ""),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, metal tubes (WFLDB 3.0)/FR/I U", "m2",
                    "greenhouse_plastic_roof_metal_tubes", ""),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, plastic tubes (WFLDB 3.0)/FR/I U", "m2",
                    "greenhouse_plastic_roof_plastic_tubes", ""),

            new TemplateProductUsage("Crop, default, heavy metals uptake (WFLDB 3.0)/GLO U", "kg",
                    "yield_main_product_dry_per_crop_cycle", ""),

            new TemplateProductUsage("lubricating oil, at plant/RER U", "kg", "pest_horticultural_oil", ""),

            new TemplateProductUsage("Land use change, {luc_crop_type} crop (WFLDB 3.0)/{country} U", "ha",
                    "luc_formula", ""),
            // TODO: Transport
            // TODO: Seeds
    };

    public static final TemplateProductUsage[] electricityHeat = {

            };

    public static final TemplateProductUsage[] wastes = {
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to sanitary landfill/CH U", "kg",
                    "eol_plastic_landfill", ""),
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to municipal incineration/CH U", "kg",
                    "eol_plastic_incineration", ""),
            new TemplateProductUsage("Treatment, sewage, to wastewater treatment, class 4/CH U", "m3",
                    "eol_waste_water_to_treatment_facility", ""),
    };
}
