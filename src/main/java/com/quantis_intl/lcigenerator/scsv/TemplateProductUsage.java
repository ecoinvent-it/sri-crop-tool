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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class TemplateProductUsage
{
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.+?)\\}");

    private final String name;
    public final String unit;
    public final String amountVariable;
    public final StandardUncertaintyMetadata uncertainty;
    public final String commentVariable;
    private final Optional<String> requiredDep;

    public TemplateProductUsage(String name, String unit, String amountVariable,
            StandardUncertaintyMetadata uncertainty, String commentVariable)
    {
        this(name, unit, amountVariable, uncertainty, commentVariable, null);
    }

    public TemplateProductUsage(String name, String unit, String amountVariable,
            StandardUncertaintyMetadata uncertainty, String commentVariable, String requiredDep)
    {
        this.name = name;
        this.unit = unit;
        this.amountVariable = amountVariable;
        this.uncertainty = uncertainty;
        this.commentVariable = commentVariable;
        this.requiredDep = Optional.ofNullable(requiredDep);
    }

    public String provideName(Map<String, String> modelOutputs)
    {
        Matcher matcher = VARIABLE_PATTERN.matcher(name);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find())
        {
            String replacement = lookupVariable(modelOutputs, matcher.group(1));
            builder.append(name.substring(i, matcher.start()));
            if (replacement == null)
                // TODO: Warn
                builder.append(matcher.group(0));
            else
                builder.append(replacement);
            i = matcher.end();
        }
        builder.append(name.substring(i, name.length()));
        return builder.toString();
    }

    public Optional<String> provideRequiredDep(Map<String, String> modelOutputs)
    {
        if (requiredDep.isPresent())
        {
            Matcher matcher = VARIABLE_PATTERN.matcher(requiredDep.get());
            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (matcher.find())
            {
                String replacement = lookupVariable(modelOutputs, matcher.group(1));
                builder.append(requiredDep.get().substring(i, matcher.start()));
                if (replacement == null)
                    // TODO: Warn
                    builder.append(matcher.group(0));
                else
                    builder.append(replacement);
                i = matcher.end();
            }
            builder.append(requiredDep.get().substring(i, requiredDep.get().length()));
            return Optional.of(builder.toString());
        }
        return requiredDep;
    }

    protected String lookupVariable(Map<String, String> modelOutputs, String variable)
    {
        return modelOutputs.get(variable);
    }

    private static Map<String, String> PHOTOVOLTAIC_REMAP;
    private static Map<String, String> LOW_VOLTAGE_REMAP;
    private static List<String> LOW_VOLTAGE_WFLDB = Lists.newArrayList("AR", "CA", "CL", "CO", "CR", "CI", "EC", "GH",
            "IN", "ID", "MX", "PH", "RU", "TR", "VN");
    private static Map<String, String> DRIP_REMAP;
    private static Map<String, String> SURFACE_REMAP;
    private static Map<String, String> SPRINKLER_REMAP;

    static
    {
        ImmutableMap.Builder<String, String> photoBuilder = ImmutableMap.builder();
        photoBuilder.put("AR", "US");
        photoBuilder.put("BR", "US");
        photoBuilder.put("CL", "US");
        photoBuilder.put("CN", "TR");
        photoBuilder.put("CO", "US");
        photoBuilder.put("CR", "US");
        photoBuilder.put("CI", "TR");
        photoBuilder.put("EC", "US");
        photoBuilder.put("GH", "TR");
        photoBuilder.put("IN", "TR");
        photoBuilder.put("ID", "TR");
        photoBuilder.put("IL", "TR");
        photoBuilder.put("KE", "TR");
        photoBuilder.put("MX", "US");
        photoBuilder.put("PE", "US");
        photoBuilder.put("PH", "AU");
        photoBuilder.put("PL", "DE");
        photoBuilder.put("RU", "DE");
        photoBuilder.put("ZA", "AU");
        photoBuilder.put("LK", "TR");
        photoBuilder.put("TH", "TR");
        photoBuilder.put("UA", "DE");
        photoBuilder.put("VN", "TR");
        PHOTOVOLTAIC_REMAP = photoBuilder.build();

        ImmutableMap.Builder<String, String> lowBuilder = ImmutableMap.builder();
        lowBuilder.put("AU", "CH");
        lowBuilder.put("IL", "CH");
        lowBuilder.put("KE", "CH");
        lowBuilder.put("NZ", "CH");
        lowBuilder.put("PE", "CH");
        lowBuilder.put("ZA", "CH");
        lowBuilder.put("LK", "CH");
        lowBuilder.put("TH", "CH");
        lowBuilder.put("UA", "CH");
        LOW_VOLTAGE_REMAP = lowBuilder.build();

        ImmutableMap.Builder<String, String> dripBuilder = ImmutableMap.builder();
        dripBuilder.put("AU", "CH");
        dripBuilder.put("CO", "CH");
        dripBuilder.put("IL", "CH");
        dripBuilder.put("KE", "CH");
        dripBuilder.put("NZ", "CH");
        dripBuilder.put("PE", "CH");
        dripBuilder.put("RU", "CH");
        dripBuilder.put("ZA", "CH");
        dripBuilder.put("LK", "CH");
        dripBuilder.put("TH", "CH");
        dripBuilder.put("UA", "CH");
        dripBuilder.put("VN", "CH");
        DRIP_REMAP = dripBuilder.build();

        ImmutableMap.Builder<String, String> surfaceBuilder = ImmutableMap.builder();
        surfaceBuilder.put("FI", "CH");
        surfaceBuilder.put("IL", "CH");
        surfaceBuilder.put("KE", "CH");
        surfaceBuilder.put("NL", "CH");
        surfaceBuilder.put("NZ", "CH");
        surfaceBuilder.put("PE", "CH");
        surfaceBuilder.put("PL", "CH");
        surfaceBuilder.put("RU", "CH");
        surfaceBuilder.put("ZA", "CH");
        surfaceBuilder.put("LK", "CH");
        surfaceBuilder.put("TH", "CH");
        surfaceBuilder.put("UA", "CH");
        SURFACE_REMAP = surfaceBuilder.build();

        ImmutableMap.Builder<String, String> sprinklerBuilder = ImmutableMap.builder();
        sprinklerBuilder.put("IL", "CH");
        sprinklerBuilder.put("KE", "CH");
        sprinklerBuilder.put("NZ", "CH");
        sprinklerBuilder.put("PE", "CH");
        sprinklerBuilder.put("ZA", "CH");
        sprinklerBuilder.put("LK", "CH");
        sprinklerBuilder.put("TH", "CH");
        sprinklerBuilder.put("UA", "CH");
        sprinklerBuilder.put("VN", "CH");
        SPRINKLER_REMAP = sprinklerBuilder.build();
    }

    public static final TemplateProductUsage[] materialsFuels = {
            // NOTE: There is one process per crop. No other process is added (rooting trees, oilseed processing, etc)
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Asparagus seedling, at farm (WFLDB 3.0)/FR U", "p", "seeds_asparagus",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Carrot seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_carrot",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Linseed seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_linseed",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Maize seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_maizegrain",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Mint seedling, at farm (WFLDB 3.0)/US U", "p", "seeds_mint",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Oat seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_oat",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Onion seedling, at farm (WFLDB 3.0)/NZ U", "p", "seeds_onion",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Peanut seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_peanut",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Potato seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_potato",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Rape seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_rapeseed",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Rice seed, at regional storehouse (WFLDB 3.0)/GLO U", "kg", "seeds_rice",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Soybean seed, at farm (WFLDB 3.0)/CH U", "kg", "seeds_soybean",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Strawberry seedling, greenhouse, heated, at farm (WFLDB 3.0)/NL U", "p",
                    "seeds_strawberry", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Sugar beet seed, at regional storehouse (WFLDB 3.0)/CH U", "kg",
                    "seeds_sugarbeet", StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Sunflower seed, at farm (WFLDB 3.0)/CH U", "kg", "seeds_sunflower",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Maize seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_sweetcorn",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Planting and establishing of orchard (WFLDB 3.0)/CH U", "p", "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Tomato seedling, greenhouse, heated, at farm (WFLDB 3.0)/NL U", "p",
                    "seeds_tomato", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Wheat seed, at regional storehouse (WFLDB 3.0)/CH U", "kg", "seeds_wheat",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),

            new WithLookupTemplateProductUsage("Irrigating, surface, electricity powered (WFLDB 3.0)/{country} U",
                    "m3", "surface_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY,
                    "irr_surface_electricity", buildBiFun(SURFACE_REMAP)),
            new TemplateProductUsage("Irrigating, surface, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "surface_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_surface_diesel"),
            new TemplateProductUsage("Irrigating, surface, gravity (ALCIG)/GLO U", "m3",
                    "surface_irrigation_no_energy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_surface_no_energy", "irrigation_surface_gravity.csv"),
            new WithLookupTemplateProductUsage("Irrigating, sprinkler, electricity powered (WFLDB 3.0)/{country} U",
                    "m3", "sprinkler_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY,
                    "irr_sprinkler_electricity", buildBiFun(SPRINKLER_REMAP)),
            new TemplateProductUsage("Irrigating, sprinkler, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "sprinkler_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_sprinkler_diesel"),
            new WithLookupTemplateProductUsage("Irrigating, drip, electricity powered (WFLDB 3.0)/{country} U", "m3",
                    "drip_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY,
                    "irr_drip_electricity", buildBiFun(DRIP_REMAP)),
            new TemplateProductUsage("Irrigating, drip, diesel powered (WFLDB 3.0)/GLO U", "m3",
                    "drip_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_drip_diesel"),

            new TemplateProductUsage("Tap water, at user/RER U", "ton", "wateruse_non_conventional_sources",
                    StandardUncertaintyMetadata.IRRIGATION_WATER, "wateruse_non_conventional_sources"),

            new TemplateProductUsage("Ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonium_nitrate"),
            new TemplateProductUsage("Urea, as N, at regional storehouse/RER U", "kg", "fert_n_urea",
                    StandardUncertaintyMetadata.FERTILISERS, "fertnmin_urea"),
            new TemplateProductUsage("Urea ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ureaAN", StandardUncertaintyMetadata.FERTILISERS, "fertnmin_urea_an"),
            new TemplateProductUsage("Monoammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_mono_ammonium_phosphate"),
            new TemplateProductUsage("Diammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_di_ammonium_phosphate"),
            new TemplateProductUsage("Ammonium nitrate phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertnmin_an_phosphate"),
            new TemplateProductUsage("Calcium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_lime_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_lime_ammonium_nitrate"),
            new TemplateProductUsage("Ammonium sulphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonium_sulphate"),
            new TemplateProductUsage("Potassium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_potassium_nitrate"),
            new TemplateProductUsage("Ammonia, liquid, at regional storehouse/RER U", "kg",
                    "fert_n_ammonia_liquid_as_nh3", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonia_liquid"),

            new TemplateProductUsage("Triple superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_triple_superphosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_triple_superphosphate"),
            new TemplateProductUsage("Single superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_superphosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_superphosphate"),
            new TemplateProductUsage("Monoammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_mono_ammonium_phosphate"),
            new TemplateProductUsage("Diammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_di_ammonium_phosphate"),
            new TemplateProductUsage("Ammonium nitrate phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_an_phosphate"),
            new TemplateProductUsage("Phosphate rock, as P2O5, beneficiated, dry, at plant/MA U", "kg",
                    "fert_p_hypophosphate_raw_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_hypophosphate_raw_phosphate"),
            new TemplateProductUsage("Thomas meal, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_ground_basic_slag", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_ground_basic_slag"),

            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_salt", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_salt_kcl"),
            new TemplateProductUsage("Potassium sulphate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_sulphate_k2so4"),
            new TemplateProductUsage("Potassium nitrate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_nitrate"),
            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_patent_potassium", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_patent_potassium"),

            new TemplateProductUsage("Magnesium sulphate, at plant/RER U", "kg", "magnesium_from_fertilizer",
                    StandardUncertaintyMetadata.FERTILISERS, "fert_other_total_mg"),
            new TemplateProductUsage("Limestone, milled, packed, at plant/CH U", "kg", "fert_ca_limestone",
                    StandardUncertaintyMetadata.FERTILISERS, "fertotherca_limestone"),
            new TemplateProductUsage("Lime, from carbonation, at regional storehouse/CH U", "kg",
                    "fert_ca_carbonation_linestone", StandardUncertaintyMetadata.FERTILISERS,
                    "fertotherca_carbonation_limestone"),
            new TemplateProductUsage("Lime, algae, at regional storehouse/CH U", "kg", "fert_ca_seaweed_limestone",
                    StandardUncertaintyMetadata.FERTILISERS, "fertotherca_seaweed_limestone"),

            new TemplateProductUsage("Compost, at plant/CH U", "kg", "composttype_compost",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_compost"),
            new TemplateProductUsage("Vinasse, at fermentation plant/CH U", "kg", "composttype_vinasse",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_vinasse"),
            new TemplateProductUsage("Poultry manure, dried, at regional storehouse/CH U", "kg",
                    "composttype_dried_poultry_manure", StandardUncertaintyMetadata.FERTILISERS,
                    "composttype_dried_poultry_manure"),
            new TemplateProductUsage("Stone meal, at regional storehouse/CH U", "kg", "composttype_stone_meal",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_stone_meal"),
            new TemplateProductUsage("Horn meal, at regional storehouse/CH U", "kg", "composttype_horn_meal",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_meal"),
            new TemplateProductUsage("Horn meal, at regional storehouse/CH U", "kg", "composttype_horn_shavings_fine",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_shavings_fine"),

            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "remains_machinery_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage("Application of plant protection products, by field sprayer/CH U", "ha",
                    "plantprotection_spraying", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_spraying"),
            new TemplateProductUsage("Natural gas, burned in boiler modulating <100kW/RER U", "MJ",
                    "plantprotection_flaming", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_flaming"),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "plantprotection_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_other"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U"/*"Soil decompactation, with 4.5m chisel/FR U"*/,
                    "kg", "soilcultivation_decompactation", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_decompactation"),
            new TemplateProductUsage("Tillage, cultivating, chiselling (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_chisel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_chisel"),
            new TemplateProductUsage("Tillage, currying, by weeder (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_spring_tine_weeder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_spring_tine_weeder"),
            new TemplateProductUsage("Tillage, harrowing, by rotary harrow (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_rotary_harrow", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_rotary_harrow"),
            new TemplateProductUsage("Tillage, harrowing, by spring tine harrow (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_sprint_tine_harrow",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_sprint_tine_harrow"),
            new TemplateProductUsage("Tillage, hoeing and earthing-up, potatoes (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_hoeing_earthing_up",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_hoeing_earthing_up"),
            new TemplateProductUsage("Tillage, ploughing (WFLDB 3.0)/CH U", "ha", "soilcultivation_tillage_plough",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_plough"),
            new TemplateProductUsage("Tillage, rolling (WFLDB 3.0)/CH U", "ha", "soilcultivation_tillage_roll",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_roll"),
            new TemplateProductUsage("Tillage, rotary cultivator (WFLDB 3.0)/CH U", "ha",
                    "soilcultivation_tillage_rotary_cultivator", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_rotary_cultivator"),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "soilcultivation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_other"),
            new TemplateProductUsage("Sowing/CH U", "ha", "sowingplanting_sowing",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_sowing"),
            new TemplateProductUsage("Planting/CH U", "ha", "sowingplanting_planting_seedlings",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_seedlings"),
            new TemplateProductUsage("Plantation of trees (WFLDB 3.0)/p/CH U", "p", "sowingplanting_planting_trees",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_trees"),
            new TemplateProductUsage("Potato planting/CH U", "ha", "sowingplanting_planting_potatoes",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_potatoes"),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "sowingplanting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "sowingplanting_other"),
            new TemplateProductUsage("Fertilising, by broadcaster/CH U", "ha", "fertilisation_fertilizing_broadcaster",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "fertilisation_fertilizing_broadcaster"),
            new TemplateProductUsage("Slurry spreading, by vacuum tanker/CH U", "m3",
                    "fertilisation_liquid_manure_vacuum_tanker", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_liquid_manure_vacuum_tanker"),
            new TemplateProductUsage("Solid manure loading and spreading, by hydraulic loader and spreader/CH U",
                    "ton",
                    "fertilisation_solid_manure", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_solid_manure"),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "fertilisation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "fertilisation_other"),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "harvesting_chopping_maize",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_chopping_maize"),
            new TemplateProductUsage("Combine harvesting/CH U", "ha", "harvesting_threshing_combine_harvester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_threshing_combine_harvester"),
            new TemplateProductUsage("Fodder loading, by self-loading trailer/CH U", "m3",
                    "harvesting_picking_up_forage_self_propelled_loader",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_picking_up_forage_self_propelled_loader"),
            new TemplateProductUsage("Harvesting, by complete harvester, beets/CH U", "ha",
                    "harvesting_beets_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_beets_complete_havester"),
            new TemplateProductUsage("Harvesting, by complete harvester, potatoes/CH U", "ha",
                    "harvesting_potatoes_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_potatoes_complete_havester"),
            new TemplateProductUsage("Haying, by rotary tedder/CH U", "ha", "harvesting_making_hay_rotary_tedder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_making_hay_rotary_tedder"),
            new TemplateProductUsage("Loading bales/CH U", "unit", "harvesting_loading_bales",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_loading_bales"),
            new TemplateProductUsage("Mowing, by motor mower/CH U", "ha", "harvesting_mowing_motor_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_mowing_motor_mower"),
            new TemplateProductUsage("Mowing, by rotary mower/CH U", "ha", "harvesting_mowing_rotary_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_mowing_rotary_mower"),
            new TemplateProductUsage("Potato haulm cutting/CH U", "ha", "harvesting_removing_potatoes_haulms",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_removing_potatoes_haulms"),
            new TemplateProductUsage("Swath, by rotary windrower/CH U", "ha", "harvesting_windrowing_rotary_swather",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_windrowing_rotary_swather"),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "harvesting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_other"),

            new TemplateProductUsage("Baling/CH U", "unit", "otherworkprocesses_baling",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_baling"),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "otherworkprocesses_chopping",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_chopping"),
            new TemplateProductUsage("Mulching/CH U", "ha", "otherworkprocesses_mulching",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_mulching"),
            new TemplateProductUsage("Transport, tractor and trailer/CH U", "tkm",
                    "otherworkprocesses_transport_tractor_trailer",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "otherworkprocesses_transport_tractor_trailer"),
            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "otherworkprocesses_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "otherworkprocesses_other"),

            new TemplateProductUsage("Diesel, burned in agricultural machinery (WFLDB 3.0)/kg/GLO U", "kg",
                    "energy_diesel_excluding_diesel_used_in_tractor",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_diesel_excluding_diesel_used_in_tractor"),
            new TemplateProductUsage("Lignite briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_lignite_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_lignite_briquette"),
            new TemplateProductUsage("Hard coal briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_hard_coal_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_hard_coal_briquette"),
            new TemplateProductUsage("Light fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "energy_fuel_oil_light", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_light"),
            new TemplateProductUsage("Heavy fuel oil, burned in industrial furnace 1MW, non-modulating/RER U", "MJ",
                    "energy_fuel_oil_heavy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_heavy"),
            new TemplateProductUsage("Natural gas, burned in industrial furnace >100kW/RER U", "MJ",
                    "energy_natural_gas", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_natural_gas"),
            new TemplateProductUsage("Pellets, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_pellets_humidity_10_percent", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_wood_pellets_humidity_10_percent"),
            new TemplateProductUsage("Wood chips, from industry, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_chips_fresh_humidity_40_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_wood_chips_fresh_humidity_40_percent"),
            new TemplateProductUsage("Logs, mixed, burned in furnace 100kW/CH U", "MJ", "energy_wood_logs",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_wood_logs"),

            new TemplateProductUsage("Tap water, at user/RER U", "kg", "utilities_wateruse_non_conventional_sources",
                    StandardUncertaintyMetadata.UTILITIES_WATER, "utilities_wateruse_non_conventional_sources"),

            new TemplateProductUsage("Polypropylene, granulate, at plant/RER U", "kg", "materials_fleece",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_fleece"),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_silage_foil",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_silage_foil"),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_covering_sheet",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_covering_sheet"),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_bird_net",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_bird_net"),

            new TemplateProductUsage("Plastic tunnel (WFLDB 3.0)/FR U", "m2", "greenhouse_plastic_tunnel",
                    StandardUncertaintyMetadata.OTHER_GREENHOUSES, "greenhouse_plastic_tunnel"),
            new TemplateProductUsage("Greenhouse, glass walls and roof, metal tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_glass_roof_metal_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_glass_roof_metal_tubes"),
            new TemplateProductUsage("Greenhouse, glass walls and roof, plastic tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_glass_roof_plastic_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_glass_roof_plastic_tubes"),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, metal tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_plastic_roof_metal_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_plastic_roof_metal_tubes"),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, plastic tubes (WFLDB 3.0)/FR U", "m2",
                    "greenhouse_plastic_roof_plastic_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_plastic_roof_plastic_tubes"),

            new TemplateProductUsage("Crop, default, heavy metals uptake (WFLDB 3.0)/GLO U", "kg",
                    "hm_uptake_formula", StandardUncertaintyMetadata.HM_TO_SOIL, ""),

            new LucTemplateProductUsage("ha", "luc_formula", StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),

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
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, "pest_horticultural_oil"),

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
            new LowVoltageTemplateProductUsage("kWh",
                    "energy_electricity_low_voltage_at_grid", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_low_voltage_at_grid"),
            new WithLookupTemplateProductUsage("Electricity, production mix photovoltaic, at plant/{country} U", "kWh",
                    "energy_electricity_photovoltaic_produced_locally", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_photovoltaic_produced_locally", buildBiFun(PHOTOVOLTAIC_REMAP)),
            new TemplateProductUsage("Electricity, at wind power plant/RER U", "kWh",
                    "energy_electricity_wind_power_produced_locally", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_wind_power_produced_locally"),
            new TemplateProductUsage("Heat, natural gas, at industrial furnace >100kW/RER U", "MJ",
                    "energy_heat_district_heating", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_district_heating"),
            new TemplateProductUsage("Heat, at flat plate collector, multiple dwelling, for hot water/CH U", "MJ",
                    "energy_heat_solar_collector", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_solar_collector")
    };

    public static final TemplateProductUsage[] wastes = {
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to sanitary landfill/CH U", "kg",
                    "eol_plastic_landfill", StandardUncertaintyMetadata.WASTE_MANAGEMENT, "eol_landfill"),
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to municipal incineration/CH U", "kg",
                    "eol_plastic_incineration", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                    "eol_incineration"),
            new TemplateProductUsage("Treatment, sewage, to wastewater treatment, class 4/CH U", "m3",
                    "eol_waste_water_to_treatment_facility", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                    "eol_waste_water_to_treatment_facility"),
    };

    private static class WithLookupTemplateProductUsage extends TemplateProductUsage
    {
        private final BiFunction<Map<String, String>, String, String> variableResolver;

        public WithLookupTemplateProductUsage(String name, String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable,
                BiFunction<Map<String, String>, String, String> variableResolver)
        {
            super(name, unit, amountVariable, uncertainty, commentVariable);
            this.variableResolver = variableResolver;
        }

        protected String lookupVariable(Map<String, String> modelOutputs, String variable)
        {
            return variableResolver.apply(modelOutputs, variable);
        }
    }

    private static class LowVoltageTemplateProductUsage extends TemplateProductUsage
    {
        public LowVoltageTemplateProductUsage(String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            super("{name}", unit, amountVariable, uncertainty, commentVariable);
        }

        protected String lookupVariable(Map<String, String> modelOutputs, String variable)
        {
            String country = modelOutputs.get("country");
            country = LOW_VOLTAGE_REMAP.getOrDefault(country, country);
            if (LOW_VOLTAGE_WFLDB.contains(country))
                return "Electricity, low voltage, production " + country + ", at grid (WFLDB 3.0)/" + country + " U";
            else
                return "Electricity, low voltage, at grid/" + country + " U";
        }
    }

    private static class LucTemplateProductUsage extends TemplateProductUsage
    {
        public LucTemplateProductUsage(String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            super("", unit, amountVariable, uncertainty, commentVariable);
        }

        public String provideName(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");
            String db = findDb(cropType, country);

            return "Land use change, " + cropType + " crop (" + db + ")/" + country + " U";
        }

        public Optional<String> provideRequiredDep(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");
            String db = findDb(cropType, country);

            if ("ALCIG".equals(db))
                return Optional.of("luc_" + cropType + "_" + country + ".csv");

            return Optional.empty();
        }

        private static final List<String> ANNUAL_ALCIG_LUC_COUNTRY = Lists.newArrayList("BE", "CI", "CL", "CO", "CR",
                "EC", "GH", "ID", "KE", "LK", "PH", "PL", "TR", "VN", "ZA");
        private static final List<String> PERENNIAL_ALCIG_LUC_COUNTRY = Lists.newArrayList("AU", "CA", "CH", "DE",
                "FI", "HU", "IL", "NL", "NZ", "PL", "RU", "TH", "UA");

        private String findDb(String cropType, String country)
        {
            List<String> alcigLucCountry;
            if ("annual".equals(cropType))
                alcigLucCountry = ANNUAL_ALCIG_LUC_COUNTRY;
            else if ("perennial".equals(cropType))
                alcigLucCountry = PERENNIAL_ALCIG_LUC_COUNTRY;
            else
                throw new IllegalStateException(cropType);

            if (alcigLucCountry.contains(country))
                return "ALCIG";

            return "WFLDB 3.0";
        }
    }

    private static BiFunction<Map<String, String>, String, String> buildBiFun(Map<String, String> lookupMap)
    {
        return (map, var) -> {
            String res = map.get(var);
            return lookupMap.getOrDefault(res, res);
        };
    }

}
