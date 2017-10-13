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

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;
import com.quantis_intl.lcigenerator.scsv.StandardUncertaintyMetadata;

public class EcospoldTemplateIntermediaryExchanges implements TemplateIntermediaryExchanges
{
    @Override
    public TemplateIntermediaryExchange[] getMaterialsFuels()
    {
        return materialsFuels;
    }

    @Override
    public TemplateIntermediaryExchange[] getElectricityHeat()
    {
        return electricityHeat;
    }

    @Override
    public TemplateIntermediaryExchange[] getWastes()
    {
        return wastes;
    }

    private static final TemplateIntermediaryExchange[] materialsFuels = {
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT, "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "asparagus seedling, for planting", AvailableUnit.UNIT, "seeds_asparagus",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "onion seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_bellpepper", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange(
                    "onion seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_cabbage", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange("carrot seed, for sowing",
                                             AvailableUnit.KG, "seeds_carrot",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("potato seed, at farm",
                                             AvailableUnit.KG, "seeds_cassava",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_cashew",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_cashew",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_cashew",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "onion seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_chilli", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT, "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("cotton seed", AvailableUnit.KG,
                                             "seeds_cotton",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "onion seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_eggplant", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange("linseed seed, at farm",
                                             AvailableUnit.KG, "seeds_flax",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_grape",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_grape",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_grape",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("soybean seed, for sowing",
                                             AvailableUnit.KG, "seeds_guar",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("linseed seed, at farm",
                                             AvailableUnit.KG, "seeds_hemp",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("peanut seed, for sowing",
                                             AvailableUnit.KG, "seeds_lentil",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("linseed seed, at farm",
                                             AvailableUnit.KG, "seeds_linseed",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("maize seed, for sowing", AvailableUnit.KG,
                                             "seeds_maizegrain",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_mango",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_mango",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_mango",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "mint seedling, for planting", AvailableUnit.UNIT,
                    "seeds_mint", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_mulberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_mulberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_mulberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("oat seed, for sowing", AvailableUnit.KG,
                                             "seeds_oat",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT, "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "onion seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_onion", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT, "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("peanut seed, for sowing",
                                             AvailableUnit.KG, "seeds_peanut",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT, "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("potato seed, at farm", AvailableUnit.KG,
                                             "seeds_potato",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("rape seed, for sowing", AvailableUnit.KG,
                                             "seeds_rapeseed",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("rice seed, for sowing", AvailableUnit.KG, "seeds_rice",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("soybean seed, for sowing", AvailableUnit.KG, "seeds_soybean",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "strawberry seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_strawberry", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange("sugar beet seed, for sowing", AvailableUnit.KG,
                                             "seeds_sugarbeet", StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange("sunflower seed, for sowing", AvailableUnit.KG,
                                             "seeds_sunflower",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange("maize seed, for sowing", AvailableUnit.KG,
                                             "seeds_sweetcorn",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateIntermediaryExchange(
                    "fruit tree seedling, for planting",
                    AvailableUnit.UNIT, "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "planting tree",
                    AvailableUnit.UNIT,
                    "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "establishing orchard",
                    AvailableUnit.UNIT,
                    "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees"),
            new TemplateIntermediaryExchange(
                    "tomato seedling, for planting",
                    AvailableUnit.UNIT,
                    "seeds_tomato", StandardUncertaintyMetadata.SEEDS, "nb_seedlings"),
            new TemplateIntermediaryExchange("wheat seed, for sowing", AvailableUnit.KG, "seeds_wheat",
                                             StandardUncertaintyMetadata.SEEDS, "seeds"),

            /*new WithLookupTemplateIntermediaryExchange("Irrigating, surface, electricity powered (WFLDB 3.0)
            /{country} U",
                                               AvailableUnit.M3, "surface_irrigation_electricity",
                                               StandardUncertaintyMetadata.ELECTRICITY,
                                               "irr_surface_electricity", buildBiFun(SURFACE_REMAP)),
            new TemplateIntermediaryExchange("Irrigating, surface, diesel powered (WFLDB 3.0)/GLO U", AvailableUnit.M3,
                                     "surface_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_surface_diesel"),
            new TemplateIntermediaryExchange("Irrigating, surface, gravity (ALCIG)/GLO U", AvailableUnit.M3,
                                     "surface_irrigation_no_energy",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_surface_no_energy", ImmutableList.of("irrigation_surface_gravity.csv")),
            new WithLookupTemplateIntermediaryExchange("Irrigating, sprinkler, electricity powered (WFLDB 3.0)
            /{country} U",
                                               AvailableUnit.M3, "sprinkler_irrigation_electricity",
                                               StandardUncertaintyMetadata.ELECTRICITY,
                                               "irr_sprinkler_electricity", buildBiFun(SPRINKLER_REMAP)),
            new TemplateIntermediaryExchange("Irrigating, sprinkler, diesel powered (WFLDB 3.0)/GLO U", AvailableUnit
            .M3,
                                     "sprinkler_irrigation_diesel",
                                     StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_sprinkler_diesel"),
            new WithLookupTemplateIntermediaryExchange("Irrigating, drip, electricity powered (WFLDB 3.0)/{country}
            U", AvailableUnit.M3,
                                               "drip_irrigation_electricity", StandardUncertaintyMetadata.ELECTRICITY,
                                               "irr_drip_electricity", buildBiFun(DRIP_REMAP)),
            new TemplateIntermediaryExchange("Irrigating, drip, diesel powered (WFLDB 3.0)/GLO U", AvailableUnit.M3,
                                     "drip_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                     "irr_drip_diesel"),*/

            new TemplateIntermediaryExchange("ammonium nitrate, as N", AvailableUnit.KG,
                                             "fert_n_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertnmin_ammonium_nitrate"),
            new TemplateIntermediaryExchange("urea, as N", AvailableUnit.KG, "fert_n_urea",
                                             StandardUncertaintyMetadata.FERTILISERS, "fertnmin_urea"),
            new TemplateIntermediaryExchange("nitrogen fertiliser, as N",
                                             AvailableUnit.KG, "fert_n_ureaAN", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertnmin_urea_an"),
            new TemplateIntermediaryExchange(
                    "nitrogen fertiliser, as N", AvailableUnit.KG,
                    "fert_n_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_mono_ammonium_phosphate"),
            new TemplateIntermediaryExchange("nitrogen fertiliser, as N",
                                             AvailableUnit.KG, "fert_n_di_ammonium_phosphate",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "fertnmin_di_ammonium_phosphate"),
            new TemplateIntermediaryExchange(
                    "nitrogen fertiliser, as N", AvailableUnit.KG,
                    "fert_n_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertnmin_an_phosphate"),
            new TemplateIntermediaryExchange(
                    "nitrogen fertiliser, as N", AvailableUnit.KG,
                    "fert_n_lime_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_lime_ammonium_nitrate"),
            new TemplateIntermediaryExchange("ammonium sulfate, as N", AvailableUnit.KG,
                                             "fert_n_ammonium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertnmin_ammonium_sulphate"),
            new TemplateIntermediaryExchange(
                    "nitrogen fertiliser, as N", AvailableUnit.KG,
                    "fert_n_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_potassium_nitrate"),
            new TemplateIntermediaryExchange("ammonia, liquid", AvailableUnit.KG,
                                             "fert_n_ammonia_liquid_as_nh3", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertnmin_ammonia_liquid"),

            new TemplateIntermediaryExchange(
                    "phosphate fertiliser, as P2O5", AvailableUnit.KG,
                    "fert_p_triple_superphosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_triple_superphosphate"),
            new TemplateIntermediaryExchange(
                    "phosphate fertiliser, as P2O5", AvailableUnit.KG,
                    "fert_p_superphosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_superphosphate"),
            new TemplateIntermediaryExchange(
                    "phosphate fertiliser, as P2O5", AvailableUnit.KG,
                    "fert_p_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_mono_ammonium_phosphate"),
            new TemplateIntermediaryExchange(
                    "phosphate fertiliser, as P2O5", AvailableUnit.KG,
                    "fert_p_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_di_ammonium_phosphate"),
            new TemplateIntermediaryExchange(
                    "phosphate fertiliser, as P2O5", AvailableUnit.KG,
                    "fert_p_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_an_phosphate"),
            new TemplateIntermediaryExchange("phosphate rock, as P2O5, beneficiated, dry",
                                             AvailableUnit.KG, "fert_p_hypophosphate_raw_phosphate",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "fertpmin_hypophosphate_raw_phosphate"),
            new TemplateIntermediaryExchange("phosphate fertiliser, as P2O5", AvailableUnit.KG,
                                             "fert_p_ground_basic_slag", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertpmin_ground_basic_slag"),

            new TemplateIntermediaryExchange("potassium chloride, as K2O", AvailableUnit.KG,
                                             "fert_k_potassium_salt", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertkmin_potassium_salt_kcl"),
            new TemplateIntermediaryExchange("potassium sulfate, as K2O", AvailableUnit.KG,
                                             "fert_k_potassium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertkmin_potassium_sulphate_k2so4"),
            new TemplateIntermediaryExchange(
                    "potassium fertiliser, as K2O", AvailableUnit.KG,
                    "fert_k_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_nitrate"),
            new TemplateIntermediaryExchange("potassium chloride, as K2O", AvailableUnit.KG,
                                             "fert_k_patent_potassium", StandardUncertaintyMetadata.FERTILISERS,
                                             "fertkmin_patent_potassium"),

            new TemplateIntermediaryExchange("magnesium sulfate", AvailableUnit.KG,
                                             "magnesium_from_fertilizer_as_mgso4",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "fert_other_total_mg"),
            new TemplateIntermediaryExchange("dolomite", AvailableUnit.KG,
                                             "magnesium_from_fertilizer_as_dolomite",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_total_mg"),
            new TemplateIntermediaryExchange("lime", AvailableUnit.KG, "fert_ca_limestone_as_limestone",
                                             StandardUncertaintyMetadata.FERTILISERS, "fertotherca_limestone"),
            new TemplateIntermediaryExchange("lime", AvailableUnit.KG,
                                             "fert_ca_carbonation_limestone_as_limestone",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "fertotherca_carbonation_limestone"),
            new TemplateIntermediaryExchange("lime", AvailableUnit.KG,
                                             "fert_ca_seaweed_limestone_as_seaweed_lime",
                                             StandardUncertaintyMetadata.FERTILISERS, "fertotherca_seaweed_limestone"),
            new TemplateIntermediaryExchange("zinc monosulfate", AvailableUnit.KG,
                                             "fert_zn_zinc_sulfate_as_zincsulfate",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "fertotherzn_zinc_sulfate"),
            new TemplateIntermediaryExchange("zinc oxide", AvailableUnit.KG,
                                             "fert_zn_zinc_oxide_as_zincoxide",
                                             StandardUncertaintyMetadata.FERTILISERS, "fertotherzn_zinc_oxide"),
            new TemplateIntermediaryExchange("zinc monosulfate", AvailableUnit.KG,
                                             "fert_zn_other_as_zincsulfate",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "fertotherzn_other"),

            new TemplateIntermediaryExchange("kaolin", AvailableUnit.KG,
                                             "fert_other_kaolin",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_kaolin"),
            new TemplateIntermediaryExchange("manganese sulfate", AvailableUnit.KG,
                                             "fert_other_manganese_sulfate",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_manganese_sulfate"),
            new TemplateIntermediaryExchange("gypsum, mineral", AvailableUnit.KG,
                                             "fert_other_gypsum",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_gypsum"),
            new TemplateIntermediaryExchange("sulfite", AvailableUnit.KG,
                                             "fert_other_sulfite",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_sulfite"),
            new TemplateIntermediaryExchange("portafer", AvailableUnit.KG,
                                             "fert_other_portafer",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_portafer"),
            new TemplateIntermediaryExchange("borax, anhydrous, powder", AvailableUnit.KG,
                                             "fert_other_borax",
                                             StandardUncertaintyMetadata.FERTILISERS, "fert_other_borax"),

            new TemplateIntermediaryExchange("manure, liquid, cattle", AvailableUnit.KG,
                                             "liquid_manure_cattle", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manureliquid_cattle"),
            new TemplateIntermediaryExchange("manure, liquid, swine", AvailableUnit.KG,
                                             "liquid_manure_pig", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manureliquid_pig"),
            new TemplateIntermediaryExchange("poultry manure, fresh", AvailableUnit.KG,
                                             "liquid_manure_laying_hen", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manureliquid_laying_hen"),
            new TemplateIntermediaryExchange("manure, liquid, cattle", AvailableUnit.KG,
                                             "liquid_manure_other", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manureliquid_other"),

            new TemplateIntermediaryExchange("manure, solid, cattle", AvailableUnit.KG,
                                             "solid_manure_cattle", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manuresolid_cattle"),
            new TemplateIntermediaryExchange("manure, liquid, swine", AvailableUnit.KG,
                                             "solid_manure_pig", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manuresolid_pig"),
            new TemplateIntermediaryExchange("manure, solid, cattle", AvailableUnit.KG,
                                             "solid_manure_sheep_goat", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manuresolid_sheep_goat"),
            new TemplateIntermediaryExchange("poultry manure, dried", AvailableUnit.KG,
                                             "solid_manure_laying_hen", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manuresolid_laying_hen"),
            new TemplateIntermediaryExchange("manure, solid, cattle", AvailableUnit.KG,
                                             "solid_manure_horses", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manuresolid_horses"),
            new TemplateIntermediaryExchange("manure, solid, cattle", AvailableUnit.KG,
                                             "solid_manure_other", StandardUncertaintyMetadata.FERTILISERS,
                                             "ratio_manuresolid_other"),

            new TemplateIntermediaryExchange("compost", AvailableUnit.KG,
                                             "composttype_compost",
                                             StandardUncertaintyMetadata.FERTILISERS, "composttype_compost"),
            new TemplateIntermediaryExchange("vinasse, from fermentation of sugar beet", AvailableUnit.KG,
                                             "composttype_vinasse",
                                             StandardUncertaintyMetadata.FERTILISERS, "composttype_vinasse"),
            new TemplateIntermediaryExchange("poultry manure, dried", AvailableUnit.KG,
                                             "composttype_dried_poultry_manure",
                                             StandardUncertaintyMetadata.FERTILISERS,
                                             "composttype_dried_poultry_manure"),
            new TemplateIntermediaryExchange("stone meal", AvailableUnit.KG, "composttype_stone_meal",
                                             StandardUncertaintyMetadata.FERTILISERS, "composttype_stone_meal"),
            new TemplateIntermediaryExchange("horn meal", AvailableUnit.KG, "composttype_horn_meal",
                                             StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_meal"),
            new TemplateIntermediaryExchange("horn meal", AvailableUnit.KG,
                                             "composttype_horn_shavings_fine",
                                             StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_shavings_fine"),

            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "remains_machinery_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    ""),
            new TemplateIntermediaryExchange(
                    "application of plant protection product, by field sprayer", AvailableUnit.HA,
                    "plantprotection_spraying", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_spraying"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, natural gas",
                    AvailableUnit.MJ,
                    "plantprotection_flaming", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_flaming"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "plantprotection_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_other"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "soilcultivation_decompactation",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_decompactation"),
            new TemplateIntermediaryExchange("tillage, cultivating, chiselling", AvailableUnit.HA,
                                             "soilcultivation_tillage_chisel",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_chisel"),
            new TemplateIntermediaryExchange("tillage, currying, by weeder", AvailableUnit.HA,
                                             "soilcultivation_tillage_spring_tine_weeder",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_spring_tine_weeder"),
            new TemplateIntermediaryExchange("tillage, harrowing, by rotary harrow", AvailableUnit.HA,
                                             "soilcultivation_tillage_rotary_harrow",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_rotary_harrow"),
            new TemplateIntermediaryExchange("tillage, harrowing, by spring tine harrow", AvailableUnit.HA,
                                             "soilcultivation_tillage_sprint_tine_harrow",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_sprint_tine_harrow"),
            new TemplateIntermediaryExchange("tillage, hoeing and earthing-up, potatoes", AvailableUnit.HA,
                                             "soilcultivation_tillage_hoeing_earthing_up",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_hoeing_earthing_up"),
            new TemplateIntermediaryExchange("tillage, ploughing", AvailableUnit.HA,
                                             "soilcultivation_tillage_plough",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_plough"),
            new TemplateIntermediaryExchange("tillage, rolling", AvailableUnit.HA,
                                             "soilcultivation_tillage_roll",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_roll"),
            new TemplateIntermediaryExchange("tillage, rotary cultivator", AvailableUnit.HA,
                                             "soilcultivation_tillage_rotary_cultivator",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "soilcultivation_tillage_rotary_cultivator"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "soilcultivation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_other"),
            new TemplateIntermediaryExchange("sowing", AvailableUnit.HA, "sowingplanting_sowing",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "sowingplanting_sowing"),
            new TemplateIntermediaryExchange("planting", AvailableUnit.HA,
                                             "sowingplanting_planting_seedlings",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "sowingplanting_planting_seedlings"),
            new TemplateIntermediaryExchange("potato planting", AvailableUnit.HA,
                                             "sowingplanting_planting_potatoes",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "sowingplanting_planting_potatoes"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "sowingplanting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "sowingplanting_other"),
            new TemplateIntermediaryExchange("fertilising, by broadcaster", AvailableUnit.HA,
                                             "fertilisation_fertilizing_broadcaster",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "fertilisation_fertilizing_broadcaster"),
            new TemplateIntermediaryExchange("liquid manure spreading, by vacuum tanker", AvailableUnit.M3,
                                             "fertilisation_liquid_manure_vacuum_tanker",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "fertilisation_liquid_manure_vacuum_tanker"),
            new TemplateIntermediaryExchange(
                    "solid manure loading and spreading, by hydraulic loader and spreader",
                    AvailableUnit.KG, "fertilisation_solid_manure",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_solid_manure"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "fertilisation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_other"),
            new TemplateIntermediaryExchange("chopping, maize", AvailableUnit.HA,
                                             "harvesting_chopping_maize",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_chopping_maize"),
            new TemplateIntermediaryExchange("combine harvesting", AvailableUnit.HA,
                                             "harvesting_threshing_combine_harvester",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_threshing_combine_harvester"),
            new TemplateIntermediaryExchange("fodder loading, by self-loading trailer", AvailableUnit.M3,
                                             "harvesting_picking_up_forage_self_propelled_loader",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_picking_up_forage_self_propelled_loader"),
            new TemplateIntermediaryExchange(
                    "harvesting, by complete harvester, ground crops",
                    AvailableUnit.HA, "harvesting_beets_complete_havester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_beets_complete_havester"),
            new TemplateIntermediaryExchange(
                    "harvesting, by complete harvester, ground crops",
                    AvailableUnit.HA, "harvesting_potatoes_complete_havester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_potatoes_complete_havester"),
            new TemplateIntermediaryExchange("haying, by rotary tedder", AvailableUnit.HA,
                                             "harvesting_making_hay_rotary_tedder",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_making_hay_rotary_tedder"),
            new TemplateIntermediaryExchange("bale loading", AvailableUnit.UNIT,
                                             "harvesting_loading_bales",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_loading_bales"),
            new TemplateIntermediaryExchange("mowing, by motor mower", AvailableUnit.HA,
                                             "harvesting_mowing_motor_mower",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_mowing_motor_mower"),
            new TemplateIntermediaryExchange("mowing, by rotary mower", AvailableUnit.HA,
                                             "harvesting_mowing_rotary_mower",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_mowing_rotary_mower"),
            new TemplateIntermediaryExchange("potato haulm cutting", AvailableUnit.HA,
                                             "harvesting_removing_potatoes_haulms",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_removing_potatoes_haulms"),
            new TemplateIntermediaryExchange("swath, by rotary windrower", AvailableUnit.HA,
                                             "harvesting_windrowing_rotary_swather",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "harvesting_windrowing_rotary_swather"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "harvesting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_other"),
            new TemplateIntermediaryExchange("baling", AvailableUnit.UNIT, "otherworkprocesses_baling",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "otherworkprocesses_baling"),
            new TemplateIntermediaryExchange("chopping, maize", AvailableUnit.HA,
                                             "otherworkprocesses_chopping",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "otherworkprocesses_chopping"),
            new TemplateIntermediaryExchange("mulching", AvailableUnit.HA, "otherworkprocesses_mulching",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "otherworkprocesses_mulching"),
            new TemplateIntermediaryExchange("transport, tractor and trailer, agricultural",
                                             AvailableUnit.T_KM, "otherworkprocesses_transport_tractor_trailer",
                                             StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                                             "otherworkprocesses_transport_tractor_trailer"),
            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "otherworkprocesses_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "otherworkprocesses_other"),
            new TemplateIntermediaryExchange(
                    "petrol, unleaded, burned in machinery",
                    AvailableUnit.MJ, "total_machinery_gazoline", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "total_machinery_gazoline"),

            new TemplateIntermediaryExchange(
                    "diesel, burned in agricultural machinery",
                    AvailableUnit.MJ, "energy_diesel_excluding_diesel_used_in_tractor",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_diesel_excluding_diesel_used_in_tractor"),

            new TemplateIntermediaryExchange("tap water", AvailableUnit.KG,
                                             "utilities_wateruse_non_conventional_sources",
                                             StandardUncertaintyMetadata.UTILITIES_WATER,
                                             "utilities_wateruse_non_conventional_sources"),

            new TemplateIntermediaryExchange("horticultural fleece", AvailableUnit.M2,
                                             "materials_fleece",
                                             StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_fleece"),
            new TemplateIntermediaryExchange("polyethylene, high density, granulate", AvailableUnit.KG,
                                             "materials_silage_foil",
                                             StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_silage_foil"),
            new TemplateIntermediaryExchange("polyethylene, high density, granulate", AvailableUnit.KG,
                                             "materials_covering_sheet",
                                             StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_covering_sheet"),
            new TemplateIntermediaryExchange("polyethylene, high density, granulate", AvailableUnit.KG,
                                             "materials_bird_net",
                                             StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_bird_net"),

            new TemplateIntermediaryExchange("plastic tunnel", AvailableUnit.M2,
                                             "greenhouse_plastic_tunnel",
                                             StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                                             "greenhouse_plastic_tunnel"),
            new TemplateIntermediaryExchange(
                    "greenhouse, glass walls and roof",
                    AvailableUnit.M2_YEAR, "greenhouse_glass_roof", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_glass_roof"),
            new TemplateIntermediaryExchange(
                    "greenhouse, plastic walls and roof",
                    AvailableUnit.M2_YEAR, "greenhouse_plastic_roof", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_plastic_roof"),
            new TemplateIntermediaryExchange(
                    "trellis system, wooden poles, soft wood, tar impregnated",
                    AvailableUnit.HA, "need_trellis", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),

            /*new LucTemplateIntermediaryExchange(AvailableUnit.HA, "luc_formula", StandardUncertaintyMetadata
            .LAND_TRANSFORMATION,
             ""),*/

            new TemplateIntermediaryExchange(
                    "packaging, for fertilisers or pesticides",
                    AvailableUnit.KG, "wfldb_packaging_solid", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),
            new TemplateIntermediaryExchange(
                    "packaging, for fertilisers or pesticides",
                    AvailableUnit.KG, "wfldb_packaging_liquid", StandardUncertaintyMetadata.OTHER_MATERIALS, ""),

            new TemplateIntermediaryExchange("naphtha", AvailableUnit.KG, "pest_horticultural_oil",
                                             StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING,
                                             "pest_horticultural_oil"),

            //FIXME: Should be g for all the 4 below
            new TemplateIntermediaryExchange("pesticide, unspecified", AvailableUnit.KG, "pest_remains",
                                             StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            /*new TemplateIntermediaryExchange("Emissions from pesticides, unspecified (WFLDB 3.3)/GLO S", "g",
                                     "pest_remains",
                                     StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, ""),*/
            new TemplateIntermediaryExchange("pesticide, unspecified", AvailableUnit.KG,
                                             "remains_herbicides",
                                             StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            /*new TemplateIntermediaryExchange("Emissions from herbicides, unspecified (WFLDB 3.3)/GLO S", "g",
                                     "remains_herbicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL,
                                     ""),*/
            new TemplateIntermediaryExchange("pesticide, unspecified", AvailableUnit.KG,
                                             "remains_fungicides",
                                             StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            /*new TemplateIntermediaryExchange("Emissions from fungicides, unspecified (WFLDB 3.3)/GLO S", "g",
                                     "remains_fungicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL,
                                     ""),*/
            new TemplateIntermediaryExchange("pesticide, unspecified", AvailableUnit.KG,
                                             "remains_insecticides",
                                             StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            /*new TemplateIntermediaryExchange("Emissions from insecticides, unspecified (WFLDB 3.3)/GLO S", "g",
                                     "remains_insecticides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL,
                                     "")*/
    };

    private static final TemplateIntermediaryExchange[] electricityHeat = {
            /*new LowVoltageTemplateIntermediaryExchange("kWh",
                                               "energy_electricity_low_voltage_at_grid",
                                               StandardUncertaintyMetadata.ELECTRICITY,
                                               "energy_electricity_low_voltage_at_grid"),
            new WithLookupTemplateIntermediaryExchange("Electricity, production mix photovoltaic, at plant/{country}
            U", "kWh",
                                               "energy_electricity_photovoltaic_produced_locally",
                                               StandardUncertaintyMetadata.ELECTRICITY,
                                               "energy_electricity_photovoltaic_produced_locally",
                                               buildBiFun(PHOTOVOLTAIC_REMAP)),
            new TemplateIntermediaryExchange("Electricity, at wind power plant/RER U", "kWh",
                                     "energy_electricity_wind_power_produced_locally",
                                     StandardUncertaintyMetadata.ELECTRICITY,
                                     "energy_electricity_wind_power_produced_locally"),*/
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas",
                    AvailableUnit.MJ, "energy_lignite_briquette", StandardUncertaintyMetadata
                            .ENERGY_CARRIERS_FUEL_WORK,
                    "energy_lignite_briquette"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas",
                    AvailableUnit.MJ, "energy_hard_coal_briquette",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_hard_coal_briquette"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas",
                    AvailableUnit.MJ, "energy_fuel_oil_light", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_light"),
            new TemplateIntermediaryExchange(
                    "heat, district or industrial, other than natural gas",
                    AvailableUnit.MJ, "energy_fuel_oil_heavy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_heavy"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, natural gas",
                    AvailableUnit.MJ, "energy_natural_gas", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_natural_gas"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas",
                    AvailableUnit.MJ, "energy_wood_pellets_humidity_10_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_wood_pellets_humidity_10_percent"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas",
                    AvailableUnit.MJ, "energy_wood_chips_fresh_humidity_40_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_wood_chips_fresh_humidity_40_percent"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas", AvailableUnit.MJ, "energy_wood_logs",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_wood_logs"),
            new TemplateIntermediaryExchange(
                    "heat, district or industrial, natural gas",
                    AvailableUnit.MJ, "energy_heat_district_heating",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_district_heating"),
            new TemplateIntermediaryExchange(
                    "heat, central or small-scale, other than natural gas",
                    AvailableUnit.MJ, "energy_heat_solar_collector",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_solar_collector")
    };

    private static final TemplateIntermediaryExchange[] wastes = {
            new TemplateIntermediaryExchange("waste polyethylene", AvailableUnit.KG,
                                             "eol_plastic_landfill", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                             "eol_landfill"),
            new TemplateIntermediaryExchange("waste polyethylene",
                                             AvailableUnit.KG, "eol_plastic_incineration",
                                             StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                             "eol_incineration"),
            new TemplateIntermediaryExchange("biowaste",
                                             AvailableUnit.KG, "eol_biowaste_incineration",
                                             StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                             "biowaste_incineration"),
            new TemplateIntermediaryExchange("biowaste",
                                             AvailableUnit.KG, "eol_biowaste_anae_digestion",
                                             StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                             "biowaste_anae_digestion"),
            new TemplateIntermediaryExchange("biowaste",
                                             AvailableUnit.KG, "eol_biowaste_composting", StandardUncertaintyMetadata
                                                     .WASTE_MANAGEMENT,
                                             "biowaste_composting"),
            new TemplateIntermediaryExchange("biowaste",
                                             AvailableUnit.KG, "eol_biowaste_other",
                                             StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                             "biowaste_other"),
            new TemplateIntermediaryExchange("wastewater, average", AvailableUnit.M3,
                                             "eol_waste_water_to_treatment_facility",
                                             StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                                             "eol_waste_water_to_treatment_facility"),
            };


    /*private static class LowVoltageTemplateIntermediaryExchange extends TemplateIntermediaryExchange
    {
        public LowVoltageTemplateIntermediaryExchange(String unit, String amountVariable,
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
                return "Electricity, low voltage, production " + country + ", at grid (WFLDB 3.0)/" + country + " U";
            else
                return "Electricity, low voltage, at grid/" + country + " U";
        }
    }*/

    private static class LucTemplateIntermediaryExchange extends TemplateIntermediaryExchange
    {
        public LucTemplateIntermediaryExchange(AvailableUnit unit, String amountVariable,
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

        /*public Optional<List<String>> provideRequiredDep(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");
            String db = findDb(cropType, country);

            if ("ALCIG".equals(db))
                return Optional.of(ImmutableList.of("luc_" + cropType + "_" + country + "_wfldb.csv"));

            return Optional.empty();
        }*/

        private static final List<String> ANNUAL_ALCIG_LUC_COUNTRY = Lists.newArrayList("BE", "CI", "CL", "CO", "CR",
                                                                                        "EC", "GH", "ID", "KE", "LK",
                                                                                        "PH", "PL", "TR", "VN", "ZA");
        private static final List<String> PERENNIAL_ALCIG_LUC_COUNTRY = Lists.newArrayList("AU", "CA", "CH", "DE",
                                                                                           "FI", "HU", "IL", "NL", "NZ",
                                                                                           "PL", "RU", "TH", "UA");

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
