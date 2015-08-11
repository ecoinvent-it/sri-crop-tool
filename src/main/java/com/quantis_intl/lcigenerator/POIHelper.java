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
package com.quantis_intl.lcigenerator;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.base.CharMatcher;

public class POIHelper
{
    private POIHelper()
    {
    }

    public static String getCellStringValue(Sheet sheet, int rowIndex, int index, String defaultValue)
    {
        Row row = sheet.getRow(rowIndex);
        if (row == null)
            return defaultValue;
        else
            return getCellStringValue(row, index, defaultValue);
    }

    public static String getCellStringValue(Row row, int index, String defaultValue)
    {
        Cell cell = row.getCell(index, Row.RETURN_BLANK_AS_NULL);
        return getCellStringValue(cell, defaultValue);
    }

    public static String getCellStringValue(Cell cell, String defaultValue)
    {
        if (cell != null)
        {
            int cellType = cell.getCellType();
            if (cellType == Cell.CELL_TYPE_NUMERIC
                    || (cellType == Cell.CELL_TYPE_FORMULA && cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC))
                return Double.toString(cell.getNumericCellValue());
            else if (cellType == Cell.CELL_TYPE_STRING
                    || (cellType == Cell.CELL_TYPE_FORMULA && cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING))
            {
                String val = cell.getStringCellValue();
                String trimedValue = CharMatcher.WHITESPACE.trimFrom(val);
                return trimedValue.isEmpty() ? defaultValue : trimedValue;
            }
            return defaultValue;
        }
        else
            return defaultValue;
    }

    public static Double getCellDoubleValue(Sheet sheet, int rowIndex, int index, Double defaultValue)
    {
        Row row = sheet.getRow(rowIndex);
        if (row == null)
            return defaultValue;
        else
            return getCellDoubleValue(row, index, defaultValue);
    }

    public static Double getCellDoubleValue(Row row, int index, Double defaultValue)
    {
        return getCellDoubleValue(row.getCell(index, Row.RETURN_BLANK_AS_NULL), defaultValue);
    }

    public static Double getCellDoubleValue(Cell cell, Double defaultValue)
    {
        if (cell == null)
            return defaultValue;
        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_NUMERIC
                || (cellType == Cell.CELL_TYPE_FORMULA && cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC))
            return cell.getNumericCellValue();
        return defaultValue;
    }

    public static Date getCellDateValue(Cell cell, Date defaultValue)
    {
        if (cell == null)
            return defaultValue;
        if (cell.getCellType() == Cell.CELL_TYPE_STRING)
            return defaultValue;
        return cell.getDateCellValue();
    }

    public static String getCellCoordinates(Cell cell)
    {
        if (cell == null)
            return "Cell is absent";
        return CellReference.convertNumToColString(cell.getColumnIndex()) + (cell.getRowIndex() + 1);
    }
}
