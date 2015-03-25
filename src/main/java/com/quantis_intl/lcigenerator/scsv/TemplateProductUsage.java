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
            new TemplateProductUsage("ammonium nitrate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("urea, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("urea ammonium nitrate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("monoammonium phosphate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("diammonium phosphate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("ammonium nitrate phosphate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("calcium nitrate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("ammonium sulphate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("potassium nitrate, as N, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Ammonia, liquid, at regional storehouse/RER U", "kg", "", ""),

            new TemplateProductUsage("Triple superphosphate, as P2O5, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Single superphosphate, as P2O5, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Monoammonium phosphate, as P2O5, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Diammonium phosphate, as P2O5, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Ammonium nitrate phosphate, as P2O5, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Phosphate rock, as P2O5, beneficiated, dry, at plant/MA U", "kg", "", ""),
            new TemplateProductUsage("Thomas meal, as P2O5, at regional storehouse/RER U", "kg", "", ""),

            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Potassium sulphate, as K2O, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Potassium nitrate, as K2O, at regional storehouse/RER U", "kg", "", ""),
            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg", "", ""),

            new TemplateProductUsage("Limestone, milled, packed, at plant/CH U", "kg", "", ""),
            new TemplateProductUsage("lime, from carbonation, at regional storehouse/CH U", "kg", "", ""),
            // new TemplateProductUsage("lime, algae, at regional storehouse/CH U", "kg", "", ""), // FIXME: MISSING

            new TemplateProductUsage("magnesium sulphate, at plant/RER U", "kg", "", ""),

            new TemplateProductUsage("Packaging, per kg of dry fertilizers or pesticides (WFLDB 2.0)/GLO U", "kg", "",
                    ""),
            new TemplateProductUsage("Packaging, per kg of liquid fertilizers or pesticides (WFLDB 2.0)/GLO U", "kg",
                    "", ""),

            new TemplateProductUsage("compost, at plant/CH U", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("vinasse, at fermentation plant/CH U", "kg", "", ""), // FIXME: MISSING
            // new TemplateProductUsage("stone meal, at regional storehouse/RER U", "kg", "", ""), // FIXME: MISSING
            new TemplateProductUsage("chemicals inorganic, at plant/GLO U", "kg", "", ""), // FIXME: MISSING OR NOT?
            // new TemplateProductUsage("TODO", "kg", "", ""),
            // new TemplateProductUsage("horn meal, at regional storehouse/CH U", "kg", "", ""), // FIXME: MISSING
            // new TemplateProductUsage("horn meal, at regional storehouse/CH U", "kg", "", ""), // FIXME: MISSING
            new TemplateProductUsage("poultry manure, dried, at regional storehouse/CH U", "kg", "", ""),

            // FIXME: Some of them should be in electricity heat
            new TemplateProductUsage("Grain drying, high temperature/CH U", "kg", "", ""),
            new TemplateProductUsage("Grain drying, low temperature/CH U", "kg", "", ""),
            // new TemplateProductUsage("Grass drying/CH U", "kg", "", ""), // FIXME: MISSING
            new TemplateProductUsage("Maize drying/CH U", "kg", "", ""),

            // RED PART
            new TemplateProductUsage("Diesel combustion, in tractor/kg/CH U", "kg", "", ""),
            new TemplateProductUsage("lignite briquette, burned in stove 5-15kW/RER U", "MJ", "", ""),
            // new TemplateProductUsage("hard coal briquette, burned in stove 5-15kW/RER U", "MJ", "", ""), // FIXME:
            // MISSING
            new TemplateProductUsage("light fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "",
                    ""),
            new TemplateProductUsage("heavy fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "",
                    ""),
            new TemplateProductUsage("natural gas, burned in industrial furnace >100kW/RER U", "MJ", "", ""),
            // new TemplateProductUsage("pellets, mixed, burned in furnace 50kW/CH U", "MJ", "", ""), // FIXME: MISSING
            // new TemplateProductUsage("wood chips, from industry, mixed, burned in furnace 50kW/CH U", "MJ", "", ""),
            // // FIXME:
            // MISSING
            // new TemplateProductUsage("logs, mixed, burned in furnace 100kW/CH U", "MJ", "", ""), // FIXME: MISSING
            // new TemplateProductUsage("electricity, low voltage, at grid/{TODO} U", "kWh", "", ""),
            // new TemplateProductUsage("electricity, production mix photovoltaic, at plant/{TODO} U", "kWh", "", ""),
            new TemplateProductUsage("electricity, at wind power plant/RER U", "kWh", "", ""),
            new TemplateProductUsage("heat, natural gas, at industrial furnace >100kW/RER U", "MJ", "", ""),
            // new TemplateProductUsage("heat, at flat plate collector, multiple dwelling, for hot water/CH U", "MJ",
            // "",
            // ""), // FIXME: MISSING

            // TODO: l103, polypropylene, granulate, at plant
            new TemplateProductUsage("polypropylene, granulate, at plant/RER U", "TODO", "", ""), // FIXME: MISSING OR
                                                                                                  // NOT?
            // new TemplateProductUsage("Plastic tunnel/FR U", "m2", "", ""),// FIXME: MISSING
            new TemplateProductUsage("Greenhouse, glass walls and roof, metal tubes/FR/I U", "m2", "", ""),
            // new TemplateProductUsage("Greenhouse, glass walls and roof, plastic tubes/FR/I U", "m2", "", ""), //
            // FIXME:
            // MISSING
            new TemplateProductUsage("Greenhouse, plastic walls and roof, metal tubes/FR/I U", "m2", "", ""),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, plastic tubes/FR/I U", "m2", "", ""),

            // new TemplateProductUsage("Soil separation/CH U", "ha", "", ""), // FIXME: MISSING
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
            // new TemplateProductUsage("Slurry application with spreader with trailed hoses, per m3/CH U", "m3", "",
            // ""), // FIXME: MISSING
            new TemplateProductUsage("Slurry spreading, by vacuum tanker/CH U", "m3", "", ""),
            new TemplateProductUsage("Solid manure loading and spreading, by hydraulic loader and spreader/CH U", "t",
                    "", ""),
            new TemplateProductUsage("Baling/CH U", "unit", "", ""),
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
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "", ""),
            new TemplateProductUsage("Mulching/CH U", "ha", "", ""),
            new TemplateProductUsage("transport, tractor and trailer/CH U", "tkm", "", ""),

            // TODO: Transport
            // TODO: Pesticides, seeds

            new TemplateProductUsage("lubricating oil, at plant/RER U", "kg", "", ""),
            new TemplateProductUsage("polyethylene, HDPE, granulate, at plant/RER U", "kg", "", ""),
            new TemplateProductUsage("polyethylene, HDPE, granulate, at plant/RER U", "kg", "", ""),
            new TemplateProductUsage("polyethylene, HDPE, granulate, at plant/RER U", "kg", "", ""),
            new TemplateProductUsage("polyethylene, LDPE, granulate, at plant/RER U", "kg", "", ""),
            new TemplateProductUsage("polyethylene, LDPE, granulate, at plant/RER U", "kg", "", ""),

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
