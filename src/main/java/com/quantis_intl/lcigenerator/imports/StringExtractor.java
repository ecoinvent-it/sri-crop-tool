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

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;

public class StringExtractor
{
    public static Set<String> TAGS_FOR_STRING = new HashSet<>();

    static
    {
        // FIXME: No system_boundary input anymore, use ecospold name metadata instead?
        /*TAGS_FOR_STRING.add("system_boundary");*/
        TAGS_FOR_STRING.add("record_entry_by");
        TAGS_FOR_STRING.add("collection_method");
        TAGS_FOR_STRING.add("data_treatment_extrapolations");
        TAGS_FOR_STRING.add("data_treatment_uncertainty");
        TAGS_FOR_STRING.add("comment");
        TAGS_FOR_STRING.add("ecospold_name");
        TAGS_FOR_STRING.add("ecopsold_location");
        TAGS_FOR_STRING.add("ecospold_infra_process");
        TAGS_FOR_STRING.add("ecospold_unit");
        TAGS_FOR_STRING.add("ecospold_type");
        TAGS_FOR_STRING.add("ecospold_version");
        TAGS_FOR_STRING.add("ecospold_energy_values");
        TAGS_FOR_STRING.add("ecospold_language_code");
        TAGS_FOR_STRING.add("ecospold_local_language_code");
        TAGS_FOR_STRING.add("ecospold_quality_network");
        TAGS_FOR_STRING.add("ecospold_dataset_related_to_product");
        TAGS_FOR_STRING.add("ecospold_included_processes");
        TAGS_FOR_STRING.add("ecospold_amount");
        TAGS_FOR_STRING.add("ecospold_local_name");
        TAGS_FOR_STRING.add("ecospold_synonyms");
        TAGS_FOR_STRING.add("ecospold_general_comment");
        TAGS_FOR_STRING.add("ecospold_infra_included");
        TAGS_FOR_STRING.add("ecospold_category");
        TAGS_FOR_STRING.add("ecospold_subcategory");
        TAGS_FOR_STRING.add("ecospold_local_category");
        TAGS_FOR_STRING.add("ecospold_local_subcategory");
        TAGS_FOR_STRING.add("ecospold_formula");
        TAGS_FOR_STRING.add("ecospold_statistical_classification");
        TAGS_FOR_STRING.add("ecospold_cas_number");
        TAGS_FOR_STRING.add("ecospold_start_date");
        TAGS_FOR_STRING.add("ecospold_end_date");
        TAGS_FOR_STRING.add("ecospold_date_valid_for_entire_period");
        TAGS_FOR_STRING.add("ecospold_other_period_text");
        TAGS_FOR_STRING.add("ecospold_text1");
        TAGS_FOR_STRING.add("ecospold_text2");
        TAGS_FOR_STRING.add("ecospold_percent");
        TAGS_FOR_STRING.add("ecospold_product_volume");
        TAGS_FOR_STRING.add("ecospold_sampling_procedure");
        TAGS_FOR_STRING.add("ecospold_extrapolations");
        TAGS_FOR_STRING.add("ecospold_uncertainty_adjustments");
        TAGS_FOR_STRING.add("ecospold_person1");
        TAGS_FOR_STRING.add("ecospold_person2");
        TAGS_FOR_STRING.add("ecospold_date_published_in");
        TAGS_FOR_STRING.add("ecospold_ref_to_published_src");
        TAGS_FOR_STRING.add("ecospold_copyright");
        TAGS_FOR_STRING.add("ecospold_access_restricted_to");
        TAGS_FOR_STRING.add("ecospold_company_code");
        TAGS_FOR_STRING.add("ecospold_country_code");
        TAGS_FOR_STRING.add("ecospold_page_numbers");
        TAGS_FOR_STRING.add("ecospold_impact_assessment_result");
        TAGS_FOR_STRING.add("ecospold_validator");
        TAGS_FOR_STRING.add("ecospold_details");
        TAGS_FOR_STRING.add("ecospold_other_details");
    }

    private ErrorReporter errorReporter;

    public StringExtractor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
    }

    public Optional<SingleValue<String>> extract(String key, Cell labelCell, Cell dataCell, Cell commentCell,
            Cell sourceCell)
    {
        return Optional.of(new SingleValue<String>(key, POIHelper.getCellStringValue(dataCell, ""), POIHelper
                .getCellStringValue(commentCell, ""), POIHelper.getCellStringValue(sourceCell, ""),
                new Origin.ExcelUserInput(POIHelper.getCellCoordinates(dataCell), POIHelper.getCellStringValue(
                        labelCell,
                        ""))));
    }
}
