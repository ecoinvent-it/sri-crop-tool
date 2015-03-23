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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;

public class DateExtractor
{
    public static List<String> TAGS_FOR_STRING = new ArrayList<>();

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

    private Map<String, Cell> cells;
    private ErrorReporter errorReporter;

    public DateExtractor(Map<String, Cell> cells, ErrorReporter errorReporter)
    {
        this.cells = cells;
        this.errorReporter = errorReporter;
    }

    public Map<String, String> extract()
    {
        Map<String, String> extracted = new HashMap<>();
        for (String tag : TAGS_FOR_STRING)
        {
            Cell cell = cells.get(tag);
            if (cell == null)
                errorReporter.warning(tag, "", "Missing property");
            else
            {
                String stringValue = POIHelper.getCellStringValue(cell, null);
                if (stringValue == null)
                    errorReporter.warning(tag, POIHelper.getCellLocationForLogs(cell),
                            "Empty or wrong value for property");
                else
                {
                    try
                    {
                        LocalDate date = LocalDate.parse(stringValue, DATE_FORMATTER);
                        extracted.put(tag, date.format(DateTimeFormatter.ISO_DATE));
                        cells.remove(tag);
                    }
                    catch (DateTimeParseException e)
                    {
                        errorReporter.warning(tag, POIHelper.getCellLocationForLogs(cell),
                                "Wrong date format for property");
                    }
                }
            }
        }
        return extracted;
    }
}
