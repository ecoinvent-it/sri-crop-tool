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
    public final StandardUncertaintyMetadata uncertainty;
    public final String commentVariable;

    public TemplateProductUsage(String name, String unit, String amountVariable,
            StandardUncertaintyMetadata uncertainty, String commentVariable)
    {
        this.name = name;
        this.unit = unit;
        this.amountVariable = amountVariable;
        this.uncertainty = uncertainty;
        this.commentVariable = commentVariable;
    }

    public static final TemplateProductUsage[] materialsFuels = {
            // NOTE: There is one process per crop. No other process is added (rooting trees, oilseed processing, etc)
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Asparagus seedling, at farm (WFLDB 3.0)/FR U", "p", "seeds_asparagus",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Carrot seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_carrot",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Linseed seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_linseed",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Maize seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_maizegrain",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Mint seedling, at farm (WFLDB 3.0)/US U", "p", "seeds_mint",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Oat seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_oat",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Onion seedling, at farm (WFLDB 3.0)/NZ U", "p", "seeds_onion",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Peanut seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_peanut",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Potato seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_potato",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Rape seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_rapeseed",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Rice seed, at regional storehouse (WFLDB 3.0)/GLO U", "kg", "seeds_rice",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Soybean seed, at farm (WFLDB 3.0)/CH U", "kg", "seeds_soybean",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Strawberry seedling, greenhouse, heated, at farm (WFLDB 3.0)/NL U", "p",
                    "seeds_strawberry", StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Sugar beet seed, at regional storehouse (WFLDB 3.0)/CH U", "kg",
                    "seeds_sugarbeet", StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Sunflower seed, at farm (WFLDB 3.0)/CH U", "kg", "seeds_sunflower",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Maize seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_sweetcorn",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Tomato seedling, greenhouse, heated, at farm (WFLDB 3.0)/NL U", "p",
                    "seeds_tomato", StandardUncertaintyMetadata.SEEDS, ""),
            new TemplateProductUsage("Wheat seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_wheat",
                    StandardUncertaintyMetadata.SEEDS, ""),

            new TemplateProductUsage("Irrigating, surface, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "surface_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY, ""),
            new TemplateProductUsage("Irrigating, surface, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "surface_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            // new TemplateProductUsage("Irrigating, surface, gravity/GLO U", "m3", "surface_irrigation_no_energy",
            // StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),// FIXME: Missing
            new TemplateProductUsage("Irrigating, sprinkler, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "sprinkler_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY, ""),
            new TemplateProductUsage("Irrigating, sprinkler, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "sprinkler_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Irrigating, drip, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "drip_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY, ""),
            new TemplateProductUsage("Irrigating, drip, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "drip_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),

            new TemplateProductUsage("Tap water, at user/RER U", "t", "wateruse_non_conventional_sources",
                    StandardUncertaintyMetadata.IRRIGATION_WATER, ""),

            new TemplateProductUsage("Ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Urea, as N, at regional storehouse/RER U", "kg", "fert_n_urea",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Urea ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ureaAN", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Monoammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Diammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Ammonium nitrate phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Calcium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_lime_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Ammonium sulphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_sulphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Potassium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Ammonia, liquid, at regional storehouse/RER U", "kg",
                    "fert_n_ammonia_liquid_as_nh3", StandardUncertaintyMetadata.FERTILISERS, ""),

            new TemplateProductUsage("Triple superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_triple_superphosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Single superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_superphosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Monoammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Diammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Ammonium nitrate phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Phosphate rock, as P2O5, beneficiated, dry, at plant/MA U", "kg",
                    "fert_p_hypophosphate_raw_phosphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Thomas meal, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_ground_basic_slag", StandardUncertaintyMetadata.FERTILISERS, ""),

            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_salt", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Potassium sulphate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_sulphate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Potassium nitrate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_patent_potassium", StandardUncertaintyMetadata.FERTILISERS, ""),

            new TemplateProductUsage("Magnesium sulphate, at plant/RER U", "kg", "magnesium_from_fertilizer",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Limestone, milled, packed, at plant/CH U", "kg", "fert_ca_limestone",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Lime, from carbonation, at regional storehouse/CH U", "kg",
                    "fert_ca_carbonation_linestone", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Lime, algae, at regional storehouse/CH U", "kg", "fert_ca_seaweed_limestone",
                    StandardUncertaintyMetadata.FERTILISERS, ""),

            new TemplateProductUsage("Compost, at plant/CH U", "kg", "composttype_compost",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Vinasse, at fermentation plant/CH U", "kg", "composttype_vinasse",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("poultry manure, dried, at regional storehouse/CH U", "kg",
                    "composttype_dried_poultry_manure", StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Stone meal, at regional storehouse/CH U", "kg", "composttype_stone_meal",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("Horn meal, at regional storehouse/CH U", "kg", "composttype_horn_meal",
                    StandardUncertaintyMetadata.FERTILISERS, ""),
            new TemplateProductUsage("horn meal, at regional storehouse/CH U", "kg", "composttype_horn_shavings_fine",
                    StandardUncertaintyMetadata.FERTILISERS, ""),

            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "remains_machinery_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Application of plant protection products, by field sprayer/CH U", "ha",
                    "plantprotection_spraying", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Natural gas, burned in boiler modulating <100kW/RER U", "MJ",
                    "plantprotection_flaming", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "plantprotection_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U"/*"Soil decompactation, with 4.5m chisel/FR U"*/,
                    "kg", "soilcultivation_decompactation", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, cultivating, chiselling (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_chisel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, currying, by weeder (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_spring_tine_weeder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, harrowing, by rotary harrow (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_rotary_harrow", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, harrowing, by spring tine harrow (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_sprint_tine_harrow",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, hoeing and earthing-up, potatoes (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_hoeing_earthing_up",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, ploughing (WFLDB 3.0)/CH U", "ha", "soilcultivation_tillage_plough",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, rolling (WFLDB 3.0)/CH U", "ha", "soilcultivation_tillage_roll",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Tillage, rotary cultivator (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_rotary_cultivator", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "soilcultivation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Sowing/CH U", "ha", "sowingplanting_sowing",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Planting/CH U", "ha", "sowingplanting_planting_seedlings",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Plantation of trees (WFLDB 3.0)/p/CH U", "p", "sowingplanting_planting_trees",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Potato planting/CH U", "ha", "sowingplanting_planting_potatoes",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "sowingplanting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Fertilising, by broadcaster/CH U", "ha", "fertilisation_fertilizing_broadcaster",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Slurry spreading, by vacuum tanker/CH U", "m3",
                    "fertilisation_liquid_manure_vacuum_tanker", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    ""),
            new TemplateProductUsage("Solid manure loading and spreading, by hydraulic loader and spreader/CH U", "t",
                    "fertilisation_solid_manure", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "fertilisation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "harvesting_chopping_maize",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Combine harvesting/CH U", "ha", "harvesting_threshing_combine_harvester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Fodder loading, by self-loading trailer/CH U", "m3",
                    "harvesting_picking_up_forage_self_propelled_loader",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Harvesting, by complete harvester, beets/CH U", "ha",
                    "harvesting_beets_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Harvesting, by complete harvester, potatoes/CH U", "ha",
                    "harvesting_potatoes_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Haying, by rotary tedder/CH U", "ha", "harvesting_making_hay_rotary_tedder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Loading bales/CH U", "unit", "harvesting_loading_bales",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Mowing, by motor mower/CH U", "ha", "harvesting_mowing_motor_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Mowing, by rotary mower/CH U", "ha", "harvesting_mowing_rotary_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Potato haulm cutting/CH U", "ha", "harvesting_removing_potatoes_haulms",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Swath, by rotary windrower/CH U", "ha", "harvesting_windrowing_rotary_swather",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "harvesting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),

            new TemplateProductUsage("Baling/CH U", "unit", "otherworkprocesses_baling",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "otherworkprocesses_chopping",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Mulching/CH U", "ha", "otherworkprocesses_mulching",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Transport, tractor and trailer/CH U", "tkm",
                    "otherworkprocesses_transport_tractor_trailer",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "otherworkprocesses_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),

            new TemplateProductUsage("Electricity, low voltage, at grid/{country} U", "kWh",
                    "energy_electricity_low_voltage_at_grid", StandardUncertaintyMetadata.ELECTRICITY, ""),
            new TemplateProductUsage("Electricity, production mix photovoltaic, at plant/{country} U", "kWh",
                    "energy_electricity_photovoltaic_produced_locally", StandardUncertaintyMetadata.ELECTRICITY, ""),
            new TemplateProductUsage("Electricity, at wind power plant/RER U", "kWh",
                    "energy_electricity_wind_power_produced_locally", StandardUncertaintyMetadata.ELECTRICITY, ""),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "energy_diesel_excluding_diesel_used_in_tractor",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Lignite briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_lignite_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Hard coal briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_hard_coal_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Light fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "energy_fuel_oil_light", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Heavy fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "energy_fuel_oil_heavy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Natural gas, burned in industrial furnace >100kW/RER U", "MJ",
                    "energy_natural_gas", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Pellets, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_pellets_humidity_10_percent", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    ""),
            new TemplateProductUsage("Wood chips, from industry, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_chips_fresh_humidity_40_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Logs, mixed, burned in furnace 100kW/CH U", "MJ", "energy_wood_logs",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Heat, natural gas, at industrial furnace >100kW/RER U", "MJ",
                    "energy_heat_district_heating", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Heat, at flat plate collector, multiple dwelling, for hot water/CH U", "MJ",
                    "energy_heat_solar_collector", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),

            new TemplateProductUsage("Tap water, at user/RER U", "kg", "utilities_wateruse_non_conventional_sources",
                    StandardUncertaintyMetadata.UTILITIES_WATER, ""),

            new TemplateProductUsage("Polypropylene, granulate, at plant/RER U", "kg", "materials_fleece",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_silage_foil",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_covering_sheet",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_bird_net",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, ""),

            new TemplateProductUsage("Plastic tunnel (WFLDB 3.0)/FR U", "m2", "greenhouse_plastic_tunnel",
                    StandardUncertaintyMetadata.OTHER_GREEHOUSES, ""),
            new TemplateProductUsage("Greenhouse, glass walls and roof, metal tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_glass_roof_metal_tubes", StandardUncertaintyMetadata.OTHER_GREEHOUSES, ""),
            new TemplateProductUsage("Greenhouse, glass walls and roof, plastic tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_glass_roof_plastic_tubes", StandardUncertaintyMetadata.OTHER_GREEHOUSES, ""),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, metal tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_plastic_roof_metal_tubes", StandardUncertaintyMetadata.OTHER_GREEHOUSES, ""),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, plastic tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_plastic_roof_plastic_tubes", StandardUncertaintyMetadata.OTHER_GREEHOUSES, ""),

            new TemplateProductUsage("Crop, default, heavy metals uptake (WFLDB 3.0)/GLO U", "kg",
                    "hm_uptake_formula", StandardUncertaintyMetadata.HM_TO_SOIL, ""),

            new TemplateProductUsage("Land use change, {luc_crop_type} crop (WFLDB 3.0)/{country} U", "ha",
                    "luc_formula", StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),

            new TemplateProductUsage("Transport, lorry >16t, fleet average/RER U", "tkm",
                    "transport_lorry_sup_16t_fleet_average_RER", StandardUncertaintyMetadata.TRANSPORTS, ""),
            new TemplateProductUsage("Transport, transoceanic freight ship/OCE U", "tkm",
                    "transport_transoceanic_freight_ship_OCE", StandardUncertaintyMetadata.TRANSPORTS, ""),
            new TemplateProductUsage("Transport, freight, rail/RER U", "tkm", "transport_freight_rail_RER",
                    StandardUncertaintyMetadata.TRANSPORTS, ""),
            new TemplateProductUsage("Transport, freight, rail, diesel/US U", "tkm",
                    "transport_freight_rail_diesel_US", StandardUncertaintyMetadata.TRANSPORTS, ""),

            new TemplateProductUsage("Packaging, per kg of dry fertilisers or pesticides (WFLDB 3.0)/GLO U", "kg",
                    "packaging_solid_fertilisers_and_pesticides", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),
            new TemplateProductUsage("Packaging, per kg of liquid fertilisers or pesticides (WFLDB 3.0)/GLO U", "kg",
                    "packaging_liquid_fertilisers_and_pesticides", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),

            new TemplateProductUsage("Lubricating oil, at plant/RER U", "kg", "pest_horticultural_oil",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),

            new TemplateProductUsage("Pesticide unspecified, at regional storehouse/RER U", "g", "pest_remains",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from pesticides, unspecified (WFLDB 3.0)/GLO S", "g", "pest_remains",
                    StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),
            new TemplateProductUsage("Herbicides, at regional storehouse/RER U", "g", "remains_herbicides",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from herbicides, unspecified (WFLDB 3.0)/GLO S", "g",
                    "remains_herbicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),
            new TemplateProductUsage("Fungicides, at regional storehouse/RER U", "g", "remains_fungicides",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from fungicides, unspecified (WFLDB 3.0)/GLO S", "g",
                    "remains_fungicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),
            new TemplateProductUsage("Insecticides, at regional storehouse/RER U", "g", "remains_insecticides",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from insecticides, unspecified (WFLDB 3.0)/GLO S", "g",
                    "remains_insecticides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, "")
    };

    public static final TemplateProductUsage[] electricityHeat = {

            };

    public static final TemplateProductUsage[] wastes = {
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to sanitary landfill/CH U", "kg",
                    "eol_plastic_landfill", StandardUncertaintyMetadata.WASTE_MANAGEMENT, ""),
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to municipal incineration/CH U", "kg",
                    "eol_plastic_incineration", StandardUncertaintyMetadata.WASTE_MANAGEMENT, ""),
            new TemplateProductUsage("Treatment, sewage, to wastewater treatment, class 4/CH U", "m3",
                    "eol_waste_water_to_treatment_facility", StandardUncertaintyMetadata.WASTE_MANAGEMENT, ""),
    };
}
