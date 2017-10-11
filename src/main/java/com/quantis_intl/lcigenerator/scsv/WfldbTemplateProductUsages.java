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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class WfldbTemplateProductUsages implements TemplateProductUsages
{
    @Override
    public TemplateProductUsage[] getMaterialsFuels()
    {
        return materialsFuels;
    }

    @Override
    public TemplateProductUsage[] getElectricityHeat()
    {
        return electricityHeat;
    }

    @Override
    public TemplateProductUsage[] getWastes()
    {
        return wastes;
    }

    private static Map<String, String> PHOTOVOLTAIC_REMAP;
    private static Map<String, String> LOW_VOLTAGE_REMAP;
    private static List<String> LOW_VOLTAGE_WFLDB = ImmutableList.of("AR", "CO", "CR", "CI", "EC", "GH", "PH", "VN");
    private static List<String> LOW_VOLTAGE_GROUP = ImmutableList.of("CA", "CN", "US");
    private static Map<String, String> DRIP_REMAP;
    private static Map<String, String> SURFACE_REMAP;
    private static Map<String, String> SPRINKLER_REMAP;

    static
    {
        ImmutableMap.Builder<String, String> photoBuilder = ImmutableMap.builder();
        photoBuilder.put("AR", "MX");
        photoBuilder.put("BR", "MX");
        photoBuilder.put("CA", "CA-ON");
        photoBuilder.put("CL", "MX");
        photoBuilder.put("CN", "CN-SH");
        photoBuilder.put("CO", "MX");
        photoBuilder.put("CR", "MX");
        photoBuilder.put("CI", "IN");
        photoBuilder.put("EC", "MX");
        photoBuilder.put("GH", "IN");
        photoBuilder.put("IL", "IN");
        photoBuilder.put("KE", "IN");
        photoBuilder.put("PE", "MX");
        photoBuilder.put("PH", "AU");
        photoBuilder.put("RU", "PL");
        photoBuilder.put("NZ", "AU");
        photoBuilder.put("LK", "IN");
        photoBuilder.put("TR", "IN");
        photoBuilder.put("VN", "IN");
        photoBuilder.put("US", "WECC, US only");
        PHOTOVOLTAIC_REMAP = photoBuilder.build();

        ImmutableMap.Builder<String, String> lowBuilder = ImmutableMap.builder();
        lowBuilder.put("IL", "CH");
        lowBuilder.put("KE", "CH");
        lowBuilder.put("NZ", "CH");
        lowBuilder.put("LK", "CH");
        LOW_VOLTAGE_REMAP = lowBuilder.build();

        ImmutableMap.Builder<String, String> dripBuilder = ImmutableMap.builder();
        dripBuilder.put("AU", "CH");
        dripBuilder.put("CO", "CH");
        dripBuilder.put("IL", "CH");
        dripBuilder.put("KE", "CH");
        dripBuilder.put("NZ", "CH");
        dripBuilder.put("PE", "CH");
        dripBuilder.put("ZA", "CH");
        dripBuilder.put("LK", "CH");
        dripBuilder.put("TH", "CH");
        dripBuilder.put("UA", "CH");
        dripBuilder.put("VN", "CH");
        DRIP_REMAP = dripBuilder.build();

        ImmutableMap.Builder<String, String> surfaceBuilder = ImmutableMap.builder();
        surfaceBuilder.put("AU", "CH");
        surfaceBuilder.put("FI", "CH");
        surfaceBuilder.put("DE", "CH");
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
        sprinklerBuilder.put("AU", "CH");
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

    private static final TemplateProductUsage[] materialsFuels = {
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p", "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Asparagus seedling, for planting {GLO}| market for asparagus seedling, for planting | Alloc Rec," +
                            " U", "p", "seeds_asparagus",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Onion seedling, for planting {GLO}| market for onion seedling, for planting | Alloc Rec, U", "p",
                    "seeds_bellpepper", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage(
                    "Onion seedling, for planting {GLO}| market for onion seedling, for planting | Alloc Rec, U", "p",
                    "seeds_cabbage", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Carrot seed, for sowing {GLO}| market for carrot seed, for sowing | Alloc Rec, U",
                                     "kg", "seeds_carrot",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Potato seed, at farm {GLO}| market for | Alloc Rec, U",
                                     "kg", "seeds_cassava",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_cashew",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_cashew",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_cashew",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Onion seedling, for planting {GLO}| market for onion seedling, for planting | Alloc Rec, U", "p",
                    "seeds_chilli", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p", "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Cotton seed {GLO}| market for | Alloc Rec, U", "kg",
                                     "seeds_cotton",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Onion seedling, for planting {GLO}| market for onion seedling, for planting | Alloc Rec, U", "p",
                    "seeds_eggplant", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Linseed seed, at farm {GLO}| market for linseed seed, at farm | Alloc Rec, U",
                                     "kg", "seeds_flax",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_grape",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_grape",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_grape",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Soybean seed, for sowing {GLO}| market for | Alloc Rec, U",
                                     "kg", "seeds_guar",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Linseed seed, at farm {GLO}| market for linseed seed, at farm | Alloc Rec, U",
                                     "kg", "seeds_hemp",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Peanut seed, for sowing {GLO}| market for peanut seed, for sowing | Alloc Rec, U",
                                     "kg", "seeds_lentil",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Linseed seed, at farm {GLO}| market for linseed seed, at farm | Alloc Rec, U",
                                     "kg", "seeds_linseed",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Maize seed, for sowing {GLO}| market for | Alloc Rec, U", "kg",
                                     "seeds_maizegrain",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_mango",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_mango",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_mango",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Mint seedling, for planting {GLO}| market for mint seedling, for planting | Alloc Rec, U", "p",
                    "seeds_mint", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_mulberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_mulberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_mulberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Oat seed, for sowing {GLO}| market for oat seed, for sowing | Alloc Rec, U", "kg",
                                     "seeds_oat",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p", "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Onion seedling, for planting {GLO}| market for onion seedling, for planting | Alloc Rec, U", "p",
                    "seeds_onion", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p", "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Peanut seed, for sowing {GLO}| market for peanut seed, for sowing | Alloc Rec, U",
                                     "kg", "seeds_peanut",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p", "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Potato seed, at farm {GLO}| market for | Alloc Rec, U", "kg",
                                     "seeds_potato",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Rape seed, for sowing {GLO}| market for | Alloc Rec, U", "kg",
                    "seeds_rapeseed",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Rice seed, for sowing {GLO}| market for | Alloc Rec, U", "kg", "seeds_rice",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Soybean seed, for sowing {GLO}| market for | Alloc Rec, U", "kg", "seeds_soybean",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Strawberry seedling, for planting {GLO}| market for strawberry seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_strawberry", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Sugar beet seed, for sowing {GLO}| market for | Alloc Rec, U", "kg",
                                     "seeds_sugarbeet", StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p",
                    "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage("Sunflower seed, for sowing {GLO}| market for | Alloc Rec, U", "kg",
                                     "seeds_sunflower",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Maize seed, for sowing {GLO}| market for | Alloc Rec, U", "kg",
                    "seeds_sweetcorn",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage(
                    "Fruit tree seedling, for planting {GLO}| market for fruit tree seedling, for planting | Alloc " +
                            "Rec, U",
                    "p", "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Planting tree {GLO}| market for planting tree | Alloc Rec, U",
                    "p",
                    "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Establishing orchard {GLO}| market for establishing orchard | Alloc Rec, U",
                    "p",
                    "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateProductUsage(
                    "Tomato seedling, for planting {GLO}| market for tomato seedling, for planting | Alloc Rec, U", "p",
                    "seeds_tomato", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateProductUsage("Wheat seed, for sowing {GLO}| market for | Alloc Rec, U", "kg", "seeds_wheat",
                                     StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Orchard, rooting up trees (WFLDB 3.3)/GLO U", "ha", "rooting_up_trees",
                                     StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),

            new WithLookupTemplateProductUsage("Irrigating, surface, electricity powered (WFLDB 3.3)/{country} U",
                                               "m3", "surface_irrigation_electricity",
                                               StandardUncertaintyMetadata.ELECTRICITY,
                                               "irr_surface_electricity", buildBiFun(SURFACE_REMAP)),
            new TemplateProductUsage("Irrigating, surface, diesel powered (WFLDB 3.3)/GLO U", "m3",
                                     "surface_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_surface_diesel"),
            new TemplateProductUsage("Irrigating, surface, gravity (WFLDB 3.3)/GLO U", "m3",
                                     "surface_irrigation_no_energy",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_surface_no_energy"),
            new WithLookupTemplateProductUsage("Irrigating, sprinkler, electricity powered (WFLDB 3.3)/{country} U",
                                               "m3", "sprinkler_irrigation_electricity",
                                               StandardUncertaintyMetadata.ELECTRICITY,
                                               "irr_sprinkler_electricity", buildBiFun(SPRINKLER_REMAP)),
            new TemplateProductUsage("Irrigating, sprinkler, diesel powered (WFLDB 3.3)/GLO U", "m3",
                    "sprinkler_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_sprinkler_diesel"),
            new WithLookupTemplateProductUsage("Irrigating, drip, electricity powered (WFLDB 3.3)/{country} U", "m3",
                                               "drip_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY,
                                               "irr_drip_electricity", buildBiFun(DRIP_REMAP)),
            new TemplateProductUsage("Irrigating, drip, diesel powered (WFLDB 3.3)/GLO U", "m3",
                                     "drip_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_drip_diesel"),

            new TemplateProductUsage("Tap water {GLO}| market group for | Alloc Rec, U", "ton",
                                     "wateruse_non_conventional_sources",
                                     StandardUncertaintyMetadata.IRRIGATION_WATER, "wateruse_non_conventional_sources"),

            new TemplateProductUsage("Ammonium nitrate, as N {GLO}| market for | Alloc Rec, U", "kg",
                    "fert_n_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonium_nitrate"),
            new TemplateProductUsage("Urea, as N {GLO}| market for | Alloc Rec, U", "kg", "fert_n_urea",
                                     StandardUncertaintyMetadata.FERTILISERS, "fertnmin_urea"),
            new TemplateProductUsage("Nitrogen fertiliser, as N {RER}| urea ammonium nitrate production | Alloc Rec, U",
                                     "kg", "fert_n_ureaAN", StandardUncertaintyMetadata.FERTILISERS,
                                     "fertnmin_urea_an"),
            new TemplateProductUsage(
                    "Nitrogen fertiliser, as N {RER}| monoammonium phosphate production | Alloc Rec, U", "kg",
                    "fert_n_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_mono_ammonium_phosphate"),
            new TemplateProductUsage("Nitrogen fertiliser, as N {RER}| diammonium phosphate production | Alloc Rec, U",
                                     "kg", "fert_n_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                                     "fertnmin_di_ammonium_phosphate"),
            new TemplateProductUsage(
                    "Nitrogen fertiliser, as N {RER}| ammonium nitrate phosphate production | Alloc Rec, U", "kg",
                    "fert_n_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertnmin_an_phosphate"),
            new TemplateProductUsage(
                    "Nitrogen fertiliser, as N {RER}| calcium ammonium nitrate production | Alloc Rec, U", "kg",
                    "fert_n_lime_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_lime_ammonium_nitrate"),
            new TemplateProductUsage("Ammonium sulfate, as N {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_n_ammonium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                                     "fertnmin_ammonium_sulphate"),
            new TemplateProductUsage(
                    "Nitrogen fertiliser, as N {GLO}| field application of potassium nitrate | Alloc Rec, U", "kg",
                    "fert_n_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_potassium_nitrate"),
            new TemplateProductUsage("Ammonia, liquid {RER}| market for | Alloc Rec, U", "kg",
                                     "fert_n_ammonia_liquid_as_nh3", StandardUncertaintyMetadata.FERTILISERS,
                                     "fertnmin_ammonia_liquid"),

            new TemplateProductUsage(
                    "Phosphate fertiliser, as P2O5 {RER}| triple superphosphate production | Alloc Rec, U", "kg",
                    "fert_p_triple_superphosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_triple_superphosphate"),
            new TemplateProductUsage(
                    "Phosphate fertiliser, as P2O5 {RER}| single superphosphate production | Alloc Rec, U", "kg",
                    "fert_p_superphosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_superphosphate"),
            new TemplateProductUsage(
                    "Phosphate fertiliser, as P2O5 {RER}| monoammonium phosphate production | Alloc Rec, U", "kg",
                    "fert_p_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_mono_ammonium_phosphate"),
            new TemplateProductUsage(
                    "Phosphate fertiliser, as P2O5 {RER}| diammonium phosphate production | Alloc Rec, U", "kg",
                    "fert_p_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_di_ammonium_phosphate"),
            new TemplateProductUsage(
                    "Phosphate fertiliser, as P2O5 {RER}| ammonium nitrate phosphate production | Alloc Rec, U", "kg",
                    "fert_p_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_an_phosphate"),
            new TemplateProductUsage("Phosphate rock, as P2O5, beneficiated, dry {GLO}| market for | Alloc Rec, U",
                                     "kg", "fert_p_hypophosphate_raw_phosphate",
                                     StandardUncertaintyMetadata.FERTILISERS, "fertpmin_hypophosphate_raw_phosphate"),
            new TemplateProductUsage("Phosphate fertiliser, as P2O5 {GLO}| market for | Alloc Rec, U", "kg",
                    "fert_p_ground_basic_slag", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_ground_basic_slag"),

            new TemplateProductUsage("Potassium chloride, as K2O {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_k_potassium_salt", StandardUncertaintyMetadata.FERTILISERS,
                                     "fertkmin_potassium_salt_kcl"),
            new TemplateProductUsage("Potassium sulfate, as K2O {GLO}| market for | Alloc Rec, U", "kg",
                    "fert_k_potassium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_sulphate_k2so4"),
            new TemplateProductUsage(
                    "Potassium fertiliser, as K2O {GLO}| field application of potassium nitrate | Alloc Rec, U", "kg",
                    "fert_k_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_nitrate"),
            new TemplateProductUsage("Potassium chloride, as K2O {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_k_patent_potassium", StandardUncertaintyMetadata.FERTILISERS,
                                     "fertkmin_patent_potassium"),

            new TemplateProductUsage("Magnesium sulfate {GLO}| market for | Alloc Rec, U", "kg",
                                     "magnesium_from_fertilizer_as_mgso4", StandardUncertaintyMetadata.FERTILISERS,
                                     "fert_other_total_mg"),
            new TemplateProductUsage("Dolomite {GLO}| market for | Alloc Rec, U", "kg",
                                     "magnesium_from_fertilizer_as_dolomite",
                    StandardUncertaintyMetadata.FERTILISERS, "fert_other_total_mg"),
            new TemplateProductUsage("Lime {GLO}| market for | Alloc Rec, U", "kg", "fert_ca_limestone_as_limestone",
                                     StandardUncertaintyMetadata.FERTILISERS, "fertotherca_limestone"),
            new TemplateProductUsage("Lime {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_ca_carbonation_limestone_as_limestone",
                                     StandardUncertaintyMetadata.FERTILISERS,
                                     "fertotherca_carbonation_limestone"),
            new TemplateProductUsage("Lime {FR}| production, algae | Alloc Rec, U", "kg",
                                     "fert_ca_seaweed_limestone_as_seaweed_lime",
                                     StandardUncertaintyMetadata.FERTILISERS, "fertotherca_seaweed_limestone"),
            new TemplateProductUsage("Zinc monosulfate {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_zn_zinc_sulfate_as_zincsulfate",
                                     StandardUncertaintyMetadata.FERTILISERS,
                                     "fertotherzn_zinc_sulfate"),
            new TemplateProductUsage("Zinc oxide {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_zn_zinc_oxide_as_zincoxide",
                                     StandardUncertaintyMetadata.FERTILISERS, "fertotherzn_zinc_oxide"),
            new TemplateProductUsage("Zinc monosulfate {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_zn_other_as_zincsulfate",
                                     StandardUncertaintyMetadata.FERTILISERS,
                                     "fertotherzn_other"),

            new TemplateProductUsage("Kaolin {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_other_kaolin",
                                     StandardUncertaintyMetadata.FERTILISERS, "fert_other_kaolin"),
            new TemplateProductUsage("Manganese sulfate {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_other_manganese_sulfate",
                                     StandardUncertaintyMetadata.FERTILISERS, "fert_other_manganese_sulfate"),
            new TemplateProductUsage("Gypsum, mineral {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_other_gypsum",
                                     StandardUncertaintyMetadata.FERTILISERS, "fert_other_gypsum"),
            new TemplateProductUsage("Sulfite {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_other_sulfite",
                                     StandardUncertaintyMetadata.FERTILISERS, "fert_other_sulfite"),
            new TemplateProductUsage("Portafer {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_other_portafer",
                                     StandardUncertaintyMetadata.FERTILISERS, "fert_other_portafer"),
            new TemplateProductUsage("Borax, anhydrous, powder {GLO}| market for | Alloc Rec, U", "kg",
                                     "fert_other_borax",
                                     StandardUncertaintyMetadata.FERTILISERS, "fert_other_borax"),

            new TemplateProductUsage("Manure, liquid, cattle {GLO}| market for | Alloc Rec, U", "kg",
                                     "liquid_manure_cattle", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manureliquid_cattle"),
            new TemplateProductUsage("Manure, liquid, swine {GLO}| market for | Alloc Rec, U", "kg",
                                     "liquid_manure_pig", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manureliquid_pig"),
            new TemplateProductUsage("Poultry manure, fresh {GLO}| market for | Alloc Rec, U", "kg",
                                     "liquid_manure_laying_hen", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manureliquid_laying_hen"),
            new TemplateProductUsage("Manure, liquid, cattle {GLO}| market for | Alloc Rec, U", "kg",
                                     "liquid_manure_other", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manureliquid_other"),

            new TemplateProductUsage("Manure, solid, cattle {GLO}| market for | Alloc Rec, U", "kg",
                                     "solid_manure_cattle", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manuresolid_cattle"),
            new TemplateProductUsage("Manure, liquid, swine {GLO}| market for | Alloc Rec, U", "kg",
                                     "solid_manure_pig", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manuresolid_pig"),
            new TemplateProductUsage("Manure, solid, cattle {GLO}| market for | Alloc Rec, U", "kg",
                                     "solid_manure_sheep_goat", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manuresolid_sheep_goat"),
            new TemplateProductUsage("Poultry manure, dried {GLO}| market for | Alloc Rec, U", "kg",
                                     "solid_manure_laying_hen", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manuresolid_laying_hen"),
            new TemplateProductUsage("Manure, solid, cattle {GLO}| market for | Alloc Rec, U", "kg",
                                     "solid_manure_horses", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manuresolid_horses"),
            new TemplateProductUsage("Manure, solid, cattle {GLO}| market for | Alloc Rec, U", "kg",
                                     "solid_manure_other", StandardUncertaintyMetadata.FERTILISERS,
                                     "ratio_manuresolid_other"),

            new TemplateProductUsage("Compost {CH}| treatment of biowaste, composting | Alloc Rec, U", "kg",
                                     "composttype_compost",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_compost"),
            new TemplateProductUsage("Vinasse, from fermentation of sugar beet {GLO}| market for | Alloc Rec, U", "kg",
                                     "composttype_vinasse",
                                     StandardUncertaintyMetadata.FERTILISERS, "composttype_vinasse"),
            new TemplateProductUsage("Poultry manure, dried {GLO}| market for | Alloc Rec, U", "kg",
                    "composttype_dried_poultry_manure", StandardUncertaintyMetadata.FERTILISERS,
                    "composttype_dried_poultry_manure"),
            new TemplateProductUsage("Stone meal {GLO}| market for | Alloc Rec, U", "kg", "composttype_stone_meal",
                                     StandardUncertaintyMetadata.FERTILISERS, "composttype_stone_meal"),
            new TemplateProductUsage("Horn meal {GLO}| market for | Alloc Rec, U", "kg", "composttype_horn_meal",
                                     StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_meal"),
            new TemplateProductUsage("Horn meal {GLO}| market for | Alloc Rec, U", "kg",
                                     "composttype_horn_shavings_fine",
                                     StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_shavings_fine"),

            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "remains_machinery_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, ""),
            new TemplateProductUsage(
                    "Application of plant protection product, by field sprayer {CH}| processing | Alloc Rec, U", "ha",
                    "plantprotection_spraying", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_spraying"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, natural gas {Europe without Switzerland}| heat production, natural" +
                            " gas, at boiler modulating <100kW | Alloc Rec, U",
                    "MJ",
                    "plantprotection_flaming", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_flaming"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "plantprotection_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_other"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "soilcultivation_decompactation", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_decompactation"),
            new TemplateProductUsage("Tillage, cultivating, chiselling {GLO}| market for | Alloc Rec, U", "ha",
                                     "soilcultivation_tillage_chisel",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "soilcultivation_tillage_chisel"),
            new TemplateProductUsage("Tillage, currying, by weeder {GLO}| market for | Alloc Rec, U", "ha",
                    "soilcultivation_tillage_spring_tine_weeder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_spring_tine_weeder"),
            new TemplateProductUsage("Tillage, harrowing, by rotary harrow {GLO}| market for | Alloc Rec, U", "ha",
                                     "soilcultivation_tillage_rotary_harrow",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "soilcultivation_tillage_rotary_harrow"),
            new TemplateProductUsage("Tillage, harrowing, by spring tine harrow {GLO}| market for | Alloc Rec, U", "ha",
                    "soilcultivation_tillage_sprint_tine_harrow",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_sprint_tine_harrow"),
            new TemplateProductUsage("Tillage, hoeing and earthing-up, potatoes {GLO}| market for | Alloc Rec, U", "ha",
                                     "soilcultivation_tillage_hoeing_earthing_up",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "soilcultivation_tillage_hoeing_earthing_up"),
            new TemplateProductUsage("Tillage, ploughing {GLO}| market for | Alloc Rec, U", "ha",
                    "soilcultivation_tillage_plough",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_plough"),
            new TemplateProductUsage("Tillage, rolling {GLO}| market for | Alloc Rec, U", "ha",
                                     "soilcultivation_tillage_roll",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "soilcultivation_tillage_roll"),
            new TemplateProductUsage("Tillage, rotary cultivator {GLO}| market for | Alloc Rec, U", "ha",
                    "soilcultivation_tillage_rotary_cultivator", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_rotary_cultivator"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "soilcultivation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_other"),
            new TemplateProductUsage("Sowing {GLO}| market for | Alloc Rec, U", "ha", "sowingplanting_sowing",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_sowing"),
            new TemplateProductUsage("Planting {GLO}| market for | Alloc Rec, U", "ha",
                                     "sowingplanting_planting_seedlings",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_seedlings"),
            new TemplateProductUsage("Potato planting {GLO}| market for | Alloc Rec, U", "ha",
                                     "sowingplanting_planting_potatoes",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "sowingplanting_planting_potatoes"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "sowingplanting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "sowingplanting_other"),
            new TemplateProductUsage("Fertilising, by broadcaster {GLO}| market for | Alloc Rec, U", "ha",
                                     "fertilisation_fertilizing_broadcaster",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "fertilisation_fertilizing_broadcaster"),
            new TemplateProductUsage("Liquid manure spreading, by vacuum tanker {GLO}| market for | Alloc Rec, U", "m3",
                    "fertilisation_liquid_manure_vacuum_tanker", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_liquid_manure_vacuum_tanker"),
            new TemplateProductUsage(
                    "Solid manure loading and spreading, by hydraulic loader and spreader {GLO}| market for | Alloc " +
                            "Rec, U",
                    "kg", "fertilisation_solid_manure", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_solid_manure"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "fertilisation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_other"),
            new TemplateProductUsage("Chopping, maize {GLO}| market for | Alloc Rec, U", "ha",
                                     "harvesting_chopping_maize",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "harvesting_chopping_maize"),
            new TemplateProductUsage("Combine harvesting {GLO}| market for | Alloc Rec, U", "ha",
                                     "harvesting_threshing_combine_harvester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_threshing_combine_harvester"),
            new TemplateProductUsage("Fodder loading, by self-loading trailer {GLO}| market for | Alloc Rec, U", "m3",
                                     "harvesting_picking_up_forage_self_propelled_loader",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "harvesting_picking_up_forage_self_propelled_loader"),
            new TemplateProductUsage(
                    "Harvesting, by complete harvester, ground crops {GLO}| market for harvesting, by complete " +
                            "harvester, ground crops | Alloc Rec, U",
                    "ha", "harvesting_beets_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_beets_complete_havester"),
            new TemplateProductUsage(
                    "Harvesting, by complete harvester, ground crops {GLO}| market for harvesting, by complete " +
                            "harvester, ground crops | Alloc Rec, U",
                    "ha", "harvesting_potatoes_complete_havester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_potatoes_complete_havester"),
            new TemplateProductUsage("Haying, by rotary tedder {GLO}| market for | Alloc Rec, U", "ha",
                    "harvesting_making_hay_rotary_tedder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_making_hay_rotary_tedder"),
            new TemplateProductUsage("Bale loading {GLO}| market for | Alloc Rec, U", "unit",
                                     "harvesting_loading_bales",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_loading_bales"),
            new TemplateProductUsage("Mowing, by motor mower {GLO}| market for | Alloc Rec, U", "ha",
                                     "harvesting_mowing_motor_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_mowing_motor_mower"),
            new TemplateProductUsage("Mowing, by rotary mower {GLO}| market for | Alloc Rec, U", "ha",
                                     "harvesting_mowing_rotary_mower",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "harvesting_mowing_rotary_mower"),
            new TemplateProductUsage("Potato haulm cutting {GLO}| market for | Alloc Rec, U", "ha",
                                     "harvesting_removing_potatoes_haulms",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_removing_potatoes_haulms"),
            new TemplateProductUsage("Swath, by rotary windrower {GLO}| market for | Alloc Rec, U", "ha",
                                     "harvesting_windrowing_rotary_swather",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "harvesting_windrowing_rotary_swather"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "harvesting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_other"),
            new TemplateProductUsage("Baling {GLO}| market for | Alloc Rec, U", "unit", "otherworkprocesses_baling",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_baling"),
            new TemplateProductUsage("Chopping, maize {GLO}| market for | Alloc Rec, U", "ha",
                                     "otherworkprocesses_chopping",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "otherworkprocesses_chopping"),
            new TemplateProductUsage("Mulching {GLO}| market for | Alloc Rec, U", "ha", "otherworkprocesses_mulching",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_mulching"),
            new TemplateProductUsage("Transport, tractor and trailer, agricultural {GLO}| market for | Alloc Rec, U",
                                     "tkm", "otherworkprocesses_transport_tractor_trailer",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "otherworkprocesses_transport_tractor_trailer"),
            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "otherworkprocesses_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "otherworkprocesses_other"),
            new TemplateProductUsage(
                    "Petrol, unleaded, burned in machinery {GLO}| market for petrol, unleaded, burned in machinery | " +
                            "Alloc Rec, U",
                    "MJ", "total_machinery_gazoline", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "total_machinery_gazoline"),

            new TemplateProductUsage(
                    "Diesel, burned in agricultural machinery {GLO}| diesel, burned in agricultural machinery | Alloc" +
                            " Rec, U",
                    "MJ", "energy_diesel_excluding_diesel_used_in_tractor",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_diesel_excluding_diesel_used_in_tractor"),

            new TemplateProductUsage("Tap water {GLO}| market group for | Alloc Rec, U", "kg",
                                     "utilities_wateruse_non_conventional_sources",
                                     StandardUncertaintyMetadata.UTILITIES_WATER,
                                     "utilities_wateruse_non_conventional_sources"),

            new TemplateProductUsage("Horticultural fleece {GLO}| market for horticultural fleece | Alloc Rec, U",
                                     "m2", "materials_fleece",
                                     StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_fleece"),
            new TemplateProductUsage("Polyethylene, high density, granulate {GLO}| market for | Alloc Rec, U", "kg",
                                     "materials_silage_foil",
                                     StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_silage_foil"),
            new TemplateProductUsage("Polyethylene, high density, granulate {GLO}| market for | Alloc Rec, U", "kg",
                    "materials_covering_sheet",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_covering_sheet"),
            new TemplateProductUsage("Polyethylene, high density, granulate {GLO}| market for | Alloc Rec, U", "kg",
                                     "materials_bird_net",
                                     StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_bird_net"),

            new TemplateProductUsage("Plastic tunnel {GLO}| market for plastic tunnel | Alloc Rec, U", "m2",
                                     "greenhouse_plastic_tunnel",
                    StandardUncertaintyMetadata.OTHER_GREENHOUSES, "greenhouse_plastic_tunnel"),
            new TemplateProductUsage(
                    "Greenhouse, glass walls and roof {GLO}| market for greenhouse, glass walls and roof | Alloc Rec," +
                            " U",
                    "m2a", "greenhouse_glass_roof", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_glass_roof"),
            new TemplateProductUsage(
                    "Greenhouse, plastic walls and roof {GLO}| market for greenhouse, plastic walls and roof | Alloc " +
                            "Rec, U",
                    "m2a", "greenhouse_plastic_roof", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_plastic_roof"),
            new TemplateProductUsage(
                    "Trellis system, wooden poles, soft wood, tar impregnated {GLO}| market for trellis system, " +
                            "wooden poles, soft wood, tar impregnated | Alloc Rec, U",
                    "ha", "need_trellis", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),

            new TemplateProductUsage("Crop, default, heavy metals uptake (WFLDB 3.3)/GLO U", "kg",
                                     "hm_uptake_formula", StandardUncertaintyMetadata.HM_TO_SOIL, ""),

            new LucTemplateProductUsage("ha", "luc_formula", StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),

            new TemplateProductUsage(
                    "Packaging, for fertilisers or pesticides {GLO}| packaging production for solid fertiliser or " +
                            "pesticide, per kilogram of packed product | Alloc Rec, U",
                    "kg", "wfldb_packaging_solid", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),
            new TemplateProductUsage(
                    "Packaging, for fertilisers or pesticides {GLO}| packaging production for liquid fertiliser or " +
                            "pesticide, per kilogram of packed product | Alloc Rec, U",
                    "kg", "wfldb_packaging_liquid", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),

            new TemplateProductUsage("Naphtha {RER}| market for | Alloc Rec, U", "kg", "pest_horticultural_oil",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, "pest_horticultural_oil"),

            new TemplateProductUsage("Pesticide, unspecified {GLO}| market for | Alloc Rec, U", "g", "pest_remains",
                                     StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from pesticides, unspecified (WFLDB 3.3)/GLO S", "g",
                    "pest_remains",
                    StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),
            new TemplateProductUsage("Pesticide, unspecified {GLO}| market for | Alloc Rec, U", "g",
                                     "remains_herbicides",
                                     StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from herbicides, unspecified (WFLDB 3.3)/GLO S", "g",
                    "remains_herbicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),
            new TemplateProductUsage("Pesticide, unspecified {GLO}| market for | Alloc Rec, U", "g",
                                     "remains_fungicides",
                                     StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from fungicides, unspecified (WFLDB 3.3)/GLO S", "g",
                    "remains_fungicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),
            new TemplateProductUsage("Pesticide, unspecified {GLO}| market for | Alloc Rec, U", "g",
                                     "remains_insecticides",
                                     StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from insecticides, unspecified (WFLDB 3.3)/GLO S", "g",
                    "remains_insecticides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, "")
    };

    private static final TemplateProductUsage[] electricityHeat = {
            new LowVoltageTemplateProductUsage("kWh",
                    "energy_electricity_low_voltage_at_grid", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_low_voltage_at_grid"),
            new WithLookupTemplateProductUsage(
                    "Electricity, low voltage {{country}}| electricity production, photovoltaic, 3kWp slanted-roof " +
                            "installation, multi-Si, panel, mounted | Alloc Rec, U",
                    "kWh",
                    "energy_electricity_photovoltaic_produced_locally", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_photovoltaic_produced_locally", buildBiFun(PHOTOVOLTAIC_REMAP)),
            new TemplateProductUsage(
                    "Electricity, high voltage {DE}| electricity production, wind, <1MW turbine, onshore | Alloc Rec," +
                            " U",
                    "kWh",
                    "energy_electricity_wind_power_produced_locally", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_wind_power_produced_locally"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {Europe without Switzerland}| heat " +
                            "production, lignite briquette, at stove 5-15kW | Alloc Rec, U",
                    "MJ", "energy_lignite_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_lignite_briquette"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {Europe without Switzerland}| heat " +
                            "production, hard coal briquette, stove 5-15kW | Alloc Rec, U",
                    "MJ", "energy_hard_coal_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_hard_coal_briquette"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {Europe without Switzerland}| heat " +
                            "production, light fuel oil, at boiler 100kW, non-modulating | Alloc Rec, U",
                    "MJ", "energy_fuel_oil_light", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_light"),
            new TemplateProductUsage(
                    "Heat, district or industrial, other than natural gas {CH}| heat production, heavy fuel oil, at " +
                            "industrial furnace 1MW | Alloc Rec, U",
                    "MJ", "energy_fuel_oil_heavy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_heavy"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, natural gas {Europe without Switzerland}| market for heat, central" +
                            " or small-scale, natural gas | Alloc Rec, U",
                    "MJ", "energy_natural_gas", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_natural_gas"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {Europe without Switzerland}| heat " +
                            "production, wood pellet, at furnace 9kW | Alloc Rec, U",
                    "MJ", "energy_wood_pellets_humidity_10_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_wood_pellets_humidity_10_percent"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {CH}| heat production, wood chips from " +
                            "industry, at furnace 50kW | Alloc Rec, U",
                    "MJ", "energy_wood_chips_fresh_humidity_40_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_wood_chips_fresh_humidity_40_percent"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {CH}| heat production, mixed logs, at " +
                            "furnace 100kW | Alloc Rec, U", "MJ", "energy_wood_logs",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_wood_logs"),
            new TemplateProductUsage(
                    "Heat, district or industrial, natural gas {Europe without Switzerland}| heat production, natural" +
                            " gas, at industrial furnace >100kW | Alloc Rec, U",
                    "MJ", "energy_heat_district_heating", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_district_heating"),
            new TemplateProductUsage(
                    "Heat, central or small-scale, other than natural gas {CH}| operation, solar collector system, Cu" +
                            " flat plate collector, multiple dwelling, for hot water | Alloc Rec, U",
                    "MJ", "energy_heat_solar_collector", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_solar_collector")
    };

    private static final TemplateProductUsage[] wastes = {
            new TemplateProductUsage("Waste polyethylene {CH}| treatment of, sanitary landfill | Alloc Rec, U", "kg",
                                     "eol_plastic_landfill", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                     "eol_landfill"),
            new TemplateProductUsage("Waste polyethylene {CH}| treatment of, municipal incineration | Alloc Rec, U",
                                     "kg", "eol_plastic_incineration", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                    "eol_incineration"),
            new TemplateProductUsage("Biowaste {GLO} | treatment of biowaste, municipal incineration | Alloc Rec, U",
                                     "kg", "eol_biowaste_incineration", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                     "biowaste_incineration"),
            new TemplateProductUsage("Biowaste {GLO} | treatment of biowaste by anaerobic digestion | Alloc Rec, U",
                                     "kg", "eol_biowaste_anae_digestion", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                     "biowaste_anae_digestion"),
            new TemplateProductUsage("Biowaste {GLO} | treatment of, composting | Alloc Rec, U",
                                     "kg", "eol_biowaste_composting", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                     "biowaste_composting"),
            new TemplateProductUsage("Biowaste {GLO}| market for | Alloc Rec, U",
                                     "kg", "eol_biowaste_other", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                     "biowaste_other"),
            new TemplateProductUsage("Wastewater, average {CH}| treatment of, capacity 1E9l/year | Alloc Rec, U", "m3",
                                     "eol_waste_water_to_treatment_facility",
                                     StandardUncertaintyMetadata.WASTE_MANAGEMENT,
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

        @Override
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

        @Override
        protected String lookupVariable(Map<String, String> modelOutputs, String variable)
        {
            String country = modelOutputs.get("country");
            country = LOW_VOLTAGE_REMAP.getOrDefault(country, country);
            if (LOW_VOLTAGE_WFLDB.contains(country))
                return "Electricity, low voltage, production " + country + ", at grid (WFLDB 3.3)/" + country + " U";
            else if (LOW_VOLTAGE_GROUP.contains(country))
                return "Electricity, low voltage {" + country + "}| market group for | Alloc Rec, U";
            else
                return "Electricity, low voltage {" + country + "}| market for | Alloc Rec, U";
        }
    }

    private static class LucTemplateProductUsage extends TemplateProductUsage
    {
        public LucTemplateProductUsage(String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            super("", unit, amountVariable, uncertainty, commentVariable);
        }

        @Override
        public String provideName(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");
            String db = findDb(cropType, country);

            return "Land use change, " + cropType + " crop (" + db + ")/" + country + " U";
        }

        @Override
        public Optional<List<String>> provideRequiredDep(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");
            String db = findDb(cropType, country);

            if ("ALCIG".equals(db))
                return Optional.of(ImmutableList.of("luc_" + cropType + "_" + country + "_wfldb.csv"));

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

            return "WFLDB 3.3";
        }
    }

    private static BiFunction<Map<String, String>, String, String> buildBiFun(Map<String, String> lookupMap)
    {
        return (map, var) ->
            {
                String res = map.get(var);
                return lookupMap.getOrDefault(res, res);
            };
    }

}
