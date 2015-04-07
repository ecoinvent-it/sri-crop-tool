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

            new TemplateProductUsage("Limestone, milled, packed, at plant/CH U", "kg", "fert_ca_limestone", ""),
            new TemplateProductUsage("Lime, from carbonation, at regional storehouse/CH U", "kg",
                    "fert_ca_carbonation_linestone", ""),
            new TemplateProductUsage("Lime, algae, at regional storehouse/CH U", "kg", "fert_ca_seaweed_limestone",
                    ""),

            new TemplateProductUsage("Magnesium sulphate, at plant/RER U", "kg", "fert_other_total_mg", ""),

            new TemplateProductUsage("Packaging, per kg of dry fertilizers or pesticides (WFLDB 2.0)/GLO U", "kg",
                    "TODO", ""),
            new TemplateProductUsage("Packaging, per kg of liquid fertilizers or pesticides (WFLDB 2.0)/GLO U", "kg",
                    "TODO", ""),

            new TemplateProductUsage("Compost, at plant/CH U", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            new TemplateProductUsage("Vinasse, at fermentation plant/CH U", "kg", "", ""),
            new TemplateProductUsage("Stone meal, at regional storehouse/CH U", "kg", "", ""),
            new TemplateProductUsage("chemicals inorganic, at plant/GLO U", "kg", "", ""), // FIXME: MISSING OR NOT?
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("horn meal, at regional storehouse/CH U", "kg", "", ""), // FIXME: MISSING
            // new TemplateProductUsage("horn meal, at regional storehouse/CH U", "kg", "", ""), // FIXME: MISSING
            new TemplateProductUsage("poultry manure, dried, at regional storehouse/CH U", "kg", "", ""),

            // FIXME: Some of them should be in electricity heat
            new TemplateProductUsage("Grain drying, high temperature/CH U", "kg", "", ""),
            new TemplateProductUsage("Grain drying, low temperature/CH U", "kg", "", ""),

            new TemplateProductUsage("Polypropylene, granulate, at plant/RER U", "TODO", "", ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "TODO", "", ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "TODO", "", ""),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "TODO", "", ""),

            // FIXME: MISSING are probably from agrybalise
            // new TemplateProductUsage("Plastic tunnel/FR U", "m2", "", ""),// FIXME: MISSING
            new TemplateProductUsage("Greenhouse, glass walls and roof, metal tubes/FR/I U", "m2", "", ""),
            // new TemplateProductUsage("Greenhouse, glass walls and roof, plastic tubes/FR/I U", "m2", "", ""), //
            // FIXME:
            // MISSING
            new TemplateProductUsage("Greenhouse, plastic walls and roof, metal tubes/FR/I U", "m2", "", ""),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, plastic tubes/FR/I U", "m2", "", ""),

            new TemplateProductUsage("Soil decompactation, with 4.5m chisel/FR U", "hr", "", ""),
            new TemplateProductUsage("Tillage, cultivating, chiselling/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, currying, by weeder/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, harrowing, by rotary harrow/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, harrowing, by spring tine harrow/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, hoeing and earthing-up, potatoes/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, ploughing/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, rolling/CH U", "ha", "", ""),
            new TemplateProductUsage("Tillage, rotary cultivator/CH U", "ha", "", ""),
            new TemplateProductUsage("Sowing/CH U", "ha", "", ""),
            new TemplateProductUsage("Planting/CH U", "ha", "", ""),
            new TemplateProductUsage("Plantation of trees/p/CH U", "p", "", ""),
            new TemplateProductUsage("Potato planting/CH U", "ha", "", ""),
            new TemplateProductUsage("Fertilising, by broadcaster/CH U", "ha", "", ""),
            new TemplateProductUsage("Slurry spreading, by vacuum tanker/CH U", "m3", "", ""),
            new TemplateProductUsage("Solid manure loading and spreading, by hydraulic loader and spreader/CH U", "t",
                    "", ""),
            new TemplateProductUsage("Baling/CH U", "unit", "", ""),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "", ""),
            new TemplateProductUsage("Combine harvesting/CH U", "ha", "", ""),
            new TemplateProductUsage("Fodder loading, by self-loading trailer/CH U", "m3", "", ""),
            new TemplateProductUsage("Harvesting, by complete harvester, beets/CH U", "ha", "", ""),
            new TemplateProductUsage("Harvesting, by complete harvester, potatoes/CH U", "ha", "", ""),
            new TemplateProductUsage("Haying, by rotary tedder/CH U", "ha", "", ""),
            new TemplateProductUsage("Loading bales/CH U", "unit", "", ""),
            new TemplateProductUsage("Mowing, by motor mower/CH U", "ha", "", ""),
            new TemplateProductUsage("Mowing, by rotary mower/CH U", "ha", "", ""),
            new TemplateProductUsage("Potato grading/CH U", "kg", "", ""),
            new TemplateProductUsage("Potato haulm cutting/CH U", "ha", "", ""),
            new TemplateProductUsage("Swath, by rotary windrower/CH U", "ha", "", ""),
            // new TemplateProductUsage("Flaming/CH U", "ha", "", ""), // FIXME: MISSING
            new TemplateProductUsage("Application of plant protection products, by field sprayer/CH U", "ha", "", ""),
            new TemplateProductUsage("Mulching/CH U", "ha", "", ""),
            new TemplateProductUsage("transport, tractor and trailer/CH U", "tkm", "", ""),

            new TemplateProductUsage("Diesel combustion, in tractor/kg/CH U", "kg", "", ""),
            new TemplateProductUsage("lignite briquette, burned in stove 5-15kW/RER U", "MJ", "", ""),
            new TemplateProductUsage("Hard coal briquette, burned in stove 5-15kW/RER U", "MJ", "", ""),
            new TemplateProductUsage("light fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "", ""),
            new TemplateProductUsage("heavy fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "", ""),
            new TemplateProductUsage("natural gas, burned in industrial furnace >100kW/RER U", "MJ", "", ""),
            new TemplateProductUsage("Pellets, mixed, burned in furnace 50kW/CH U", "MJ", "", ""),
            new TemplateProductUsage("Wood chips, from industry, mixed, burned in furnace 50kW/CH U", "MJ", "", ""),
            new TemplateProductUsage("Logs, mixed, burned in furnace 100kW/CH U", "MJ", "", ""),
            // new TemplateProductUsage("electricity, low voltage, at grid/{TODO} U", "kWh", "", ""),
            // new TemplateProductUsage("electricity, production mix photovoltaic, at plant/{TODO} U", "kWh", "", ""),
            new TemplateProductUsage("electricity, at wind power plant/RER U", "kWh", "", ""),
            new TemplateProductUsage("heat, natural gas, at industrial furnace >100kW/RER U", "MJ", "", ""),
            // new TemplateProductUsage("heat, at flat plate collector, multiple dwelling, for hot water/CH U", "MJ",
            // "",
            // ""), // FIXME: MISSING

            // TODO: Transport
            // TODO: Pesticides, seeds

            new TemplateProductUsage("lubricating oil, at plant/RER U", "kg", "", ""),

    };

    public static final TemplateProductUsage[] electricityHeat = {

            };

    public static final TemplateProductUsage[] wastes = {
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to sanitary landfill/CH U", "kg", "", ""),
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to municipal incineration/CH U", "kg",
                    "", ""),
            new TemplateProductUsage("Treatment, sewage, to wastewater treatment, class 4/CH U", "m3", "", ""),
    };
}
