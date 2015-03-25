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
package com.quantis_intl.lcigenerator.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.quantis_intl.lcigenerator.ErrorReporter;

public class NumericExtractor
{
    private static List<String> TAGS_FOR_NUMERIC = new ArrayList<>();
    private static List<String> TAGS_FOR_RATIO = new ArrayList<>();
    static
    {
        TAGS_FOR_NUMERIC.add("average_annual_precipitation");
        TAGS_FOR_NUMERIC.add("seeds");
        TAGS_FOR_NUMERIC.add("nb_seedlings");
        TAGS_FOR_NUMERIC.add("nb_planted_trees");
        TAGS_FOR_NUMERIC.add("nb_crop_cycles_per_year");
        TAGS_FOR_NUMERIC.add("irr_total_water_use");
        TAGS_FOR_NUMERIC.add("irr_total_electricity");
        TAGS_FOR_NUMERIC.add("irr_total_diesel");
        TAGS_FOR_NUMERIC.add("yield_main_product_per_year");
        TAGS_FOR_NUMERIC.add("yield_main_product_per_crop_cycle");
        TAGS_FOR_NUMERIC.add("yield_main_product_price");
        TAGS_FOR_NUMERIC.add("yield_co_product_price");
        TAGS_FOR_NUMERIC.add("manure_liquid_total");
        TAGS_FOR_NUMERIC.add("manure_solid_total");
        TAGS_FOR_NUMERIC.add("compost_other_org_fert");
        TAGS_FOR_NUMERIC.add("sewage_sludge");
        TAGS_FOR_NUMERIC.add("fert_n_total");
        TAGS_FOR_NUMERIC.add("fert_n_total_mineral");
        TAGS_FOR_NUMERIC.add("fert_n_total_organic");
        TAGS_FOR_NUMERIC.add("fert_p_total");
        TAGS_FOR_NUMERIC.add("fert_p_total_organic");
        TAGS_FOR_NUMERIC.add("fert_p_total_mineral");
        TAGS_FOR_NUMERIC.add("fert_k_total");
        TAGS_FOR_NUMERIC.add("fert_k_total_organic");
        TAGS_FOR_NUMERIC.add("fert_k_total_mineral");
        TAGS_FOR_NUMERIC.add("fert_other_total_mg");
        TAGS_FOR_NUMERIC.add("fert_other_total_ca");
        TAGS_FOR_NUMERIC.add("pest_total");
        TAGS_FOR_NUMERIC.add("pest_total_herbicides");
        TAGS_FOR_NUMERIC.add("pest_total_fungicides");
        TAGS_FOR_NUMERIC.add("pest_total_insecticides");
        TAGS_FOR_NUMERIC.add("pest_horticultural_oil");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel_plant_protection");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel_soil_cultivation");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel_sowing_planting");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel_fertilisation");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel_harvesting");
        TAGS_FOR_NUMERIC.add("machinery_total_diesel_other");
        TAGS_FOR_NUMERIC.add("materials_fleece");
        TAGS_FOR_NUMERIC.add("greenhouse_plastic_tunnel");
        TAGS_FOR_NUMERIC.add("greenhouse_glass_roof_metal_tubes");
        TAGS_FOR_NUMERIC.add("greenhouse_glass_roof_plastic_tubes");
        TAGS_FOR_NUMERIC.add("greenhouse_plastic_roof_metal_tubes");
        TAGS_FOR_NUMERIC.add("greenhouse_plastic_roof_plastic_tubes");

        TAGS_FOR_RATIO.add("yearly_precipitation_as_snow");
        TAGS_FOR_RATIO.add("drainage");
        TAGS_FOR_RATIO.add("ratio_irr_surface_no_energy");
        TAGS_FOR_RATIO.add("ratio_irr_surface_electricity");
        TAGS_FOR_RATIO.add("ratio_irr_surface_diesel");
        TAGS_FOR_RATIO.add("ratio_irr_sprinkler_electricity");
        TAGS_FOR_RATIO.add("ratio_irr_sprinkler_diesel");
        TAGS_FOR_RATIO.add("ratio_irr_drip_electricity");
        TAGS_FOR_RATIO.add("ratio_irr_drip_diesel");
        TAGS_FOR_RATIO.add("ratio_water_use_ground");
        TAGS_FOR_RATIO.add("ratio_water_use_surface");
        TAGS_FOR_RATIO.add("ratio_water_use_non_conventional_sources");
        TAGS_FOR_RATIO.add("yield_main_product_water_content");
        TAGS_FOR_RATIO.add("yield_co_product_water_content");
        TAGS_FOR_RATIO.add("ratio_manure_liquid_cattle");
        TAGS_FOR_RATIO.add("ratio_manure_liquid_fattening_pig");
        TAGS_FOR_RATIO.add("ratio_manure_liquid_sows_piglet");
        TAGS_FOR_RATIO.add("ratio_manure_liquid_laying_hen");
        TAGS_FOR_RATIO.add("ratio_manure_liquid_other");
        TAGS_FOR_RATIO.add("ratio_manure_liquid_dilution");
        TAGS_FOR_RATIO.add("ratio_manure_solid_cattle");
        TAGS_FOR_RATIO.add("ratio_manure_solid_pig");
        TAGS_FOR_RATIO.add("ratio_manure_solid_sheep_goat");
        TAGS_FOR_RATIO.add("ratio_manure_solid_laying_hen");
        TAGS_FOR_RATIO.add("ratio_manure_solid_broiler");
        TAGS_FOR_RATIO.add("ratio_manure_solid_other_poultry");
        TAGS_FOR_RATIO.add("ratio_manure_solid_other");
        TAGS_FOR_RATIO.add("ratio_fert_n_ammonium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fert_n_urea");
        TAGS_FOR_RATIO.add("ratio_fert_n_urea_an");
        TAGS_FOR_RATIO.add("ratio_fert_n_mono_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_n_di_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_n_an_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_n_lime_ammonium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fert_n_ammonium_sulphate");
        TAGS_FOR_RATIO.add("ratio_fert_n_potassium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fert_n_ammonia_liquid");
        TAGS_FOR_RATIO.add("ratio_fert_p_triple_superphosphate");
        TAGS_FOR_RATIO.add("ratio_fert_p_superphosphate");
        TAGS_FOR_RATIO.add("ratio_fert_p_mono_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_p_di_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_p_an_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_p_hypophosphate_raw_phosphate");
        TAGS_FOR_RATIO.add("ratio_fert_p_ground_basic_slag");
        TAGS_FOR_RATIO.add("ratio_fert_k_potassium_salt_kcl");
        TAGS_FOR_RATIO.add("ratio_fert_k_potassium_sulphate_k2so4");
        TAGS_FOR_RATIO.add("ratio_fert_k_potassium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fert_k_patent_potassium");
        TAGS_FOR_RATIO.add("ratio_fert_other_ca_limestone");
        TAGS_FOR_RATIO.add("ratio_fert_other_ca_carbonation_limestone");
        TAGS_FOR_RATIO.add("ratio_fert_other_ca_seaweed_limestone");
        TAGS_FOR_RATIO.add("drying_yield_to_be_dryied");
        TAGS_FOR_RATIO.add("drying_humidity_before_drying");
        TAGS_FOR_RATIO.add("drying_humidity_after_drying");
    }

    private ErrorReporter errorReporter;

    public NumericExtractor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
    }

    private Double extractNumeric(RawInputLine line)
    {
        Optional<Double> optDouble = line.getValueAsDouble();
        if (optDouble.isPresent())
        {
            Double doubleValue = optDouble.get();
            if (doubleValue < 0.0)
            {
                errorReporter
                        .warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                                "Can't read value, use default");
                return null;
            }
            return doubleValue;
        }
        else
        {
            errorReporter
                    .warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                            "Can't read value, use default");
            return null;
        }
    }

    private Double extractRatio(RawInputLine line)
    {
        Optional<Double> optDouble = line.getValueAsDouble();
        if (optDouble.isPresent())
        {
            Double doubleValue = optDouble.get();
            if (doubleValue < 0.0 || doubleValue > 1.0)
            {
                errorReporter
                        .warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                                "Can't read value, use default");
                return null;
            }
            return doubleValue;
        }
        else
        {
            errorReporter
                    .warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                            "Can't read value, use default");
            return null;
        }
    }

}
