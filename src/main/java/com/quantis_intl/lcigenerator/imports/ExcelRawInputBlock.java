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

import org.apache.poi.ss.usermodel.Cell;

import com.quantis_intl.lcigenerator.POIHelper;

public class ExcelRawInputBlock implements RawInputLine
{
    private String lineVariable;
    private String lineTitle;
    private int lineNum;
    private List<Cell> cells;

    public ExcelRawInputBlock(String lineVariable, String lineTitle, int lineNum, Cell cell)
    {
        this.lineVariable = lineVariable;
        this.lineTitle = lineTitle;
        this.lineNum = lineNum;
        this.cells = new ArrayList<>();
        this.cells.add(cell);
    }

    @Override
    public String getLineVariable()
    {
        return lineVariable;
    }

    @Override
    public String getLineTitle()
    {
        return lineTitle;
    }

    @Override
    public int getLineNum()
    {
        return lineNum;
    }

    @Override
    public boolean isValuePresent()
    {
        return !cells.isEmpty();
    }

    @Override
    public Optional<String> getValueAsString()
    {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Optional<Double> getValueAsDouble()
    {
        boolean atLeastOneDoubleFound = false;
        double totalDouble = 0.0;
        for (Cell cell : cells)
        {
            Double doubleValue = POIHelper.getCellDoubleValue(cell, null);
            if (doubleValue != null)
            {
                totalDouble += doubleValue;
                atLeastOneDoubleFound = true;
            }

        }
        if (atLeastOneDoubleFound)
            return Optional.of(totalDouble);
        else
            return Optional.empty();
    }

    public void addCell(Cell cell)
    {
        if (isCellValuePresent(cell))
            this.cells.add(cell);
    }

    private boolean isCellValuePresent(Cell cell)
    {
        return cell != null && !"-".equals(POIHelper.getCellStringValue(cell, "-"));
    }
}
