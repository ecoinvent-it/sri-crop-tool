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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;

public class CellResolverService
{
    public Map<String, String> getValidatedData(Map<String, Cell> cells, ErrorReporter errorReporter)
    {
        Map<String, String> validatedData = new HashMap<>();

        validatedData.putAll(new NumericExtractor(cells, errorReporter).extract());
        validatedData.putAll(new StringExtractor(cells, errorReporter).extract());
        validatedData.putAll(new StringFromListExtractor(cells, errorReporter).extract());
        manageUnresolvedCells(cells, errorReporter);

        return validatedData;
    }

    private void manageUnresolvedCells(Map<String, Cell> cells, ErrorReporter errorReporter)
    {
        for (Entry<String, Cell> cellEntry : cells.entrySet())
        {
            if (cellEntry.getValue() == null)
                errorReporter.warning(cellEntry.getKey(), "", "Unresolved empty property");
            else
                errorReporter.warning(cellEntry.getKey(), POIHelper.getCellLocationForLogs(cellEntry.getValue()),
                        "Unresolved property");
        }
    }
}
