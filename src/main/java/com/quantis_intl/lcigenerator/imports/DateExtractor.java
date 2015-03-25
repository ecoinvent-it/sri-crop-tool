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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.quantis_intl.lcigenerator.ErrorReporter;

public class DateExtractor
{
    public static Set<String> TAGS_FOR_STRING = new HashSet<>();

    // NOTE: Use fixed template as the cell format is specified by user in Excel
    // NOTE: Java considers 80 years in the past and 20 years in the future for 2-letter years
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.mm.yy");

    static
    {
        TAGS_FOR_STRING.add("harvest_date_previous_crop");
        TAGS_FOR_STRING.add("soil_cultivating_date_main_crop");
        TAGS_FOR_STRING.add("sowing_date_main_crop");
        TAGS_FOR_STRING.add("harvesting_date_main_crop");
    }

    private ErrorReporter errorReporter;

    public DateExtractor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
    }

    public LocalDate extract(RawInputLine line)
    {
        Optional<String> stringValue = line.getValueAsString();
        if (stringValue.isPresent())

            try
            {
                return LocalDate.parse(stringValue.get(), DATE_FORMATTER);
            }
            catch (DateTimeParseException e)
            {
                errorReporter.warning(line.getLineTitle(), Integer.toString(line.getLineNum()),
                        "Can't read value, use default");
                return null;
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
