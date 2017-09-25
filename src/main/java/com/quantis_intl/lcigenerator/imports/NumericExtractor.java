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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;

import com.google.common.collect.ImmutableMap;
import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;
import com.quantis_intl.lcigenerator.imports.SingleValue.DoubleSingleValue;

public class NumericExtractor
{
    public static final Set<String> TAGS_FOR_NUMERIC = new HashSet<>();
    public static final Set<String> TAGS_FOR_RATIO = new HashSet<>();
    static
    {
        TAGS_FOR_NUMERIC.add("average_annual_precipitation");
        TAGS_FOR_NUMERIC.add("nb_wet_days_per_year");
        TAGS_FOR_NUMERIC.add("mean_elevation_m");
        TAGS_FOR_NUMERIC.add("seeds");
        TAGS_FOR_NUMERIC.add("nb_seedlings");
        TAGS_FOR_NUMERIC.add("nb_planted_trees");
        TAGS_FOR_NUMERIC.add("orchard_lifetime");
        TAGS_FOR_NUMERIC.add("total_wateruse");
        TAGS_FOR_NUMERIC.add("yield_main_product_per_crop_cycle");
        TAGS_FOR_NUMERIC.add("total_manureliquid");
        TAGS_FOR_NUMERIC.add("total_manuresolid");
        TAGS_FOR_NUMERIC.add("total_composttype");
        TAGS_FOR_NUMERIC.add("total_sewagesludge");
        TAGS_FOR_NUMERIC.add("total_fertnmin");
        TAGS_FOR_NUMERIC.add("total_fertpmin");
        TAGS_FOR_NUMERIC.add("total_fertkmin");
        TAGS_FOR_NUMERIC.add("fert_other_total_mg");
        TAGS_FOR_NUMERIC.add("total_fertotherca");
        TAGS_FOR_NUMERIC.add("total_fertotherzn");
        TAGS_FOR_NUMERIC.add("fert_other_kaolin");
        TAGS_FOR_NUMERIC.add("fert_other_manganese_sulfate");
        TAGS_FOR_NUMERIC.add("fert_other_gypsum");
        TAGS_FOR_NUMERIC.add("fert_other_sulfite");
        TAGS_FOR_NUMERIC.add("fert_other_portafer");
        TAGS_FOR_NUMERIC.add("fert_other_borax");
        TAGS_FOR_NUMERIC.add("pest_total");
        TAGS_FOR_NUMERIC.add("total_herbicides");
        TAGS_FOR_NUMERIC.add("total_fungicides");
        TAGS_FOR_NUMERIC.add("total_insecticides");
        TAGS_FOR_NUMERIC.add("pest_horticultural_oil");
        TAGS_FOR_NUMERIC.add("total_machinery_diesel");
        TAGS_FOR_NUMERIC.add("total_plantprotection");
        TAGS_FOR_NUMERIC.add("total_soilcultivation");
        TAGS_FOR_NUMERIC.add("total_sowingplanting");
        TAGS_FOR_NUMERIC.add("total_fertilisation");
        TAGS_FOR_NUMERIC.add("total_harvesting");
        TAGS_FOR_NUMERIC.add("total_otherworkprocesses");
        TAGS_FOR_NUMERIC.add("total_machinery_gazoline");
        TAGS_FOR_NUMERIC.add("energy_electricity_low_voltage_at_grid");
        TAGS_FOR_NUMERIC.add("energy_electricity_photovoltaic_produced_locally");
        TAGS_FOR_NUMERIC.add("energy_electricity_wind_power_produced_locally");
        TAGS_FOR_NUMERIC.add("energy_diesel_excluding_diesel_used_in_tractor");
        TAGS_FOR_NUMERIC.add("energy_lignite_briquette");
        TAGS_FOR_NUMERIC.add("energy_hard_coal_briquette");
        TAGS_FOR_NUMERIC.add("energy_fuel_oil_light");
        TAGS_FOR_NUMERIC.add("energy_fuel_oil_heavy");
        TAGS_FOR_NUMERIC.add("energy_natural_gas");
        TAGS_FOR_NUMERIC.add("energy_wood_pellets_humidity_10_percent");
        TAGS_FOR_NUMERIC.add("energy_wood_chips_fresh_humidity_40_percent");
        TAGS_FOR_NUMERIC.add("energy_wood_logs");
        TAGS_FOR_NUMERIC.add("energy_heat_district_heating");
        TAGS_FOR_NUMERIC.add("energy_heat_solar_collector");
        TAGS_FOR_NUMERIC.add("utilities_wateruse_ground");
        TAGS_FOR_NUMERIC.add("utilities_wateruse_surface");
        TAGS_FOR_NUMERIC.add("utilities_wateruse_non_conventional_sources");
        TAGS_FOR_NUMERIC.add("materials_fleece");
        TAGS_FOR_NUMERIC.add("materials_silage_foil");
        TAGS_FOR_NUMERIC.add("materials_covering_sheet");
        TAGS_FOR_NUMERIC.add("materials_bird_net");
        TAGS_FOR_NUMERIC.add("greenhouse_plastic_tunnel");
        TAGS_FOR_NUMERIC.add("greenhouse_glass_roof");
        TAGS_FOR_NUMERIC.add("greenhouse_plastic_roof");
        TAGS_FOR_NUMERIC.add("total_eol_plastic_disposal_fleece_and_other");
        TAGS_FOR_NUMERIC.add("total_biowaste");
        TAGS_FOR_NUMERIC.add("eol_waste_water_to_treatment_facility");
        TAGS_FOR_NUMERIC.add("eol_waste_water_to_nature");
        TAGS_FOR_NUMERIC.add("cod_in_waste_water");

        TAGS_FOR_NUMERIC.add("data_quality_techno_representativeness");
        TAGS_FOR_NUMERIC.add("data_quality_geo_representativeness");
        TAGS_FOR_NUMERIC.add("data_quality_time_related_representativeness");
        TAGS_FOR_NUMERIC.add("data_quality_precision_uncertainty");
        TAGS_FOR_NUMERIC.add("data_quality_completeness");
        TAGS_FOR_NUMERIC.add("data_quality_methodo_appropriateness_consistency");

        TAGS_FOR_RATIO.add("yearly_precipitation_as_snow");
        TAGS_FOR_RATIO.add("drainage");
        TAGS_FOR_RATIO.add("ratio_irr_surface_no_energy");
        TAGS_FOR_RATIO.add("ratio_irr_surface_electricity");
        TAGS_FOR_RATIO.add("ratio_irr_surface_diesel");
        TAGS_FOR_RATIO.add("ratio_irr_sprinkler_electricity");
        TAGS_FOR_RATIO.add("ratio_irr_sprinkler_diesel");
        TAGS_FOR_RATIO.add("ratio_irr_drip_electricity");
        TAGS_FOR_RATIO.add("ratio_irr_drip_diesel");
        TAGS_FOR_RATIO.add("ratio_wateruse_ground");
        TAGS_FOR_RATIO.add("ratio_wateruse_surface");
        TAGS_FOR_RATIO.add("ratio_wateruse_non_conventional_sources");
        TAGS_FOR_RATIO.add("ratio_manureliquid_cattle");
        TAGS_FOR_RATIO.add("ratio_manureliquid_pig");
        TAGS_FOR_RATIO.add("ratio_manureliquid_laying_hen");
        TAGS_FOR_RATIO.add("ratio_manureliquid_other");
        TAGS_FOR_RATIO.add("ratio_manuresolid_cattle");
        TAGS_FOR_RATIO.add("ratio_manuresolid_pig");
        TAGS_FOR_RATIO.add("ratio_manuresolid_sheep_goat");
        TAGS_FOR_RATIO.add("ratio_manuresolid_laying_hen");
        TAGS_FOR_RATIO.add("ratio_manuresolid_horses");
        TAGS_FOR_RATIO.add("ratio_manuresolid_other");
        TAGS_FOR_RATIO.add("ratio_fertnmin_ammonium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_urea");
        TAGS_FOR_RATIO.add("ratio_fertnmin_urea_an");
        TAGS_FOR_RATIO.add("ratio_fertnmin_mono_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_di_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_an_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_lime_ammonium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_ammonium_sulphate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_potassium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fertnmin_ammonia_liquid");
        TAGS_FOR_RATIO.add("ratio_fertpmin_triple_superphosphate");
        TAGS_FOR_RATIO.add("ratio_fertpmin_superphosphate");
        TAGS_FOR_RATIO.add("ratio_fertpmin_mono_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertpmin_di_ammonium_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertpmin_an_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertpmin_hypophosphate_raw_phosphate");
        TAGS_FOR_RATIO.add("ratio_fertpmin_ground_basic_slag");
        TAGS_FOR_RATIO.add("ratio_fertkmin_potassium_salt_kcl");
        TAGS_FOR_RATIO.add("ratio_fertkmin_potassium_sulphate_k2so4");
        TAGS_FOR_RATIO.add("ratio_fertkmin_potassium_nitrate");
        TAGS_FOR_RATIO.add("ratio_fertkmin_patent_potassium");
        TAGS_FOR_RATIO.add("ratio_fertotherca_limestone");
        TAGS_FOR_RATIO.add("ratio_fertotherca_carbonation_limestone");
        TAGS_FOR_RATIO.add("ratio_fertotherca_seaweed_limestone");
        TAGS_FOR_RATIO.add("ratio_fertotherzn_zinc_sulfate");
        TAGS_FOR_RATIO.add("ratio_fertotherzn_zinc_oxide");
        TAGS_FOR_RATIO.add("ratio_fertotherzn_other");
        TAGS_FOR_RATIO.add("fert_other_dolomite_in_mg");
        TAGS_FOR_RATIO.add("drying_yield_to_be_dryied");
        TAGS_FOR_RATIO.add("drying_humidity_before_drying");
        TAGS_FOR_RATIO.add("drying_humidity_after_drying");
        TAGS_FOR_RATIO.add("ratio_eol_landfill");
        TAGS_FOR_RATIO.add("ratio_eol_incineration");
        TAGS_FOR_RATIO.add("ratio_biowaste_incineration");
        TAGS_FOR_RATIO.add("ratio_biowaste_anae_digestion");
        TAGS_FOR_RATIO.add("ratio_biowaste_composting");
        TAGS_FOR_RATIO.add("ratio_biowaste_other");
    }

    private final ErrorReporter errorReporter;

    public NumericExtractor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
    }

    public Optional<DoubleSingleValue> extractNumeric(String key, Cell labelCell, Cell dataCell, Cell commentCell,
            Cell sourceCell)
    {
        Double value = getPositiveValue(labelCell, dataCell);
        if (value == null)
            return Optional.empty();

        return Optional.of(new DoubleSingleValue(key, value, POIHelper.getCellStringValue(commentCell, ""), POIHelper
                .getCellStringValue(sourceCell, ""), new Origin.ExcelUserInput(POIHelper.getCellCoordinates(dataCell),
                POIHelper.getCellStringValue(labelCell, "")), "TODO"));
    }

    private Double getPositiveValue(Cell labelCell, Cell dataCell)
    {
        Double value = POIHelper.getCellDoubleValue(dataCell, null);
        if (value == null)
        {
            errorReporter
                    .warning(
                            ImmutableMap.of("cell", POIHelper.getCellCoordinates(dataCell), "label",
                                    POIHelper.getCellStringValue(labelCell, "")),
                            "Your value is not numeric. Please format your cell to a numeric value. For now, we will use the default value, if available");

            return null;
        }
        else if (value < 0.0)
        {
            errorReporter
                    .warning(
                            ImmutableMap.of("cell", POIHelper.getCellCoordinates(dataCell), "label",
                                    POIHelper.getCellStringValue(labelCell, "")),
                            "Negative values are not authorized. Please use a non-negative value. For now, we will use the default value, if available");
            return null;
        }
        return value;
    }

    public Optional<DoubleSingleValue> extractRatio(String key, Cell labelCell, Cell dataCell, Cell commentCell,
            Cell sourceCell)
    {
        Double value = getPositiveValue(labelCell, dataCell);
        if (value == null)
            return Optional.empty();

        if (value > 1.0)
        {
            errorReporter
                    .warning(
                            ImmutableMap.of("cell", POIHelper.getCellCoordinates(dataCell), "label",
                                    POIHelper.getCellStringValue(labelCell, key)),
                            "Your value is too high, we are expecting a ratio between 0 and 1. Please use a value smaller than 1. For now, we will use the default value, if available");
            return Optional.empty();
        }

        return Optional.of(new DoubleSingleValue(key, value, POIHelper.getCellStringValue(commentCell, ""), POIHelper
                .getCellStringValue(sourceCell, ""), new Origin.ExcelUserInput(POIHelper.getCellCoordinates(dataCell),
                POIHelper.getCellStringValue(labelCell, "")), "TODO"));
    }
}
