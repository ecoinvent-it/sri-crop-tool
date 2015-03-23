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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;

public class FileReaderService
{
    public Map<String, Cell> getInputDataFromFile(InputStream is, ErrorReporter errorReporter)
    {
        try
        {
            Workbook workbook = WorkbookFactory.create(is);
            DataFileReader dataFileReader = new DataFileReader(workbook.getSheet("Template"), errorReporter);
            return dataFileReader.getExtractedCells();
        }
        catch (InvalidFormatException e)
        {
            errorReporter.error("file", "", "invalid format for file");
        }
        catch (IOException e)
        {
            errorReporter.error("file", "", "file reading failed");
        }
        return null;
    }

    private static class DataFileReader
    {
        private static final int METADATA_ROW_INDEX = 0;
        private static final int METADATA_COLUMN_INDEX = 0;

        private final Sheet sheet;
        private final ErrorReporter errorReporter;
        private final Map<String, Cell> extractedCells = new HashMap<>();

        private String fileVersion;

        private Integer labelColumnIndex;
        private Integer countryColumnIndex;
        private Integer dataColumnIndex;
        private Integer commentColumnIndex;
        private Integer sourceColumnIndex;
        private Integer dataLevelColumnIndex;

        private Row currentTaggedRow;
        private String currentTag;

        public DataFileReader(Sheet sheet, ErrorReporter errorReporter)
        {
            this.sheet = sheet;
            this.errorReporter = errorReporter;

            readHiddenHeaderRow();
            if (!errorReporter.hasErrors())
                readOtherRows();
        }

        public Map<String, Cell> getExtractedCells()
        {
            return extractedCells;
        }

        private void readHiddenHeaderRow()
        {
            currentTaggedRow = sheet.getRow(METADATA_ROW_INDEX);
            if (currentTaggedRow == null)
                errorReporter.warning("metadata row", getReportedRowLocation(),
                        "Empty mandatory row");

            fileVersion = POIHelper.getCellStringValue(currentTaggedRow, METADATA_COLUMN_INDEX, null);
            // TODO: validate file version

            Map<String, Integer> metadataColumnIndexes = new HashMap<>();
            for (int columnIndex = METADATA_COLUMN_INDEX + 1; columnIndex < currentTaggedRow.getLastCellNum(); columnIndex++)
            {

                Cell cell = currentTaggedRow.getCell(columnIndex);
                if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING)
                    continue;

                String cellValue = cell.getStringCellValue();
                if (metadataColumnIndexes.containsKey(cellValue))
                    errorReporter.warning(cellValue, getReportedLocationForCell(columnIndex),
                            "Duplicated property (ignored)");
                else
                    metadataColumnIndexes.put(cellValue, columnIndex);
            }

            this.labelColumnIndex = getValidatedColIndexForMetadataProperty(metadataColumnIndexes, "label_column",
                    2);
            this.countryColumnIndex = getValidatedColIndexForMetadataProperty(metadataColumnIndexes, "country_column",
                    labelColumnIndex + 1);
            this.dataColumnIndex = getValidatedColIndexForMetadataProperty(metadataColumnIndexes, "data_column",
                    labelColumnIndex + 2);
            this.commentColumnIndex = getValidatedColIndexForMetadataProperty(metadataColumnIndexes, "comment_column",
                    labelColumnIndex + 3);
            this.sourceColumnIndex = getValidatedColIndexForMetadataProperty(metadataColumnIndexes, "source_column",
                    labelColumnIndex + 4);
            this.dataLevelColumnIndex = getValidatedColIndexForMetadataProperty(metadataColumnIndexes,
                    "data_level_column",
                    labelColumnIndex + 5);

            for (Map.Entry<String, Integer> metadataCellValue : metadataColumnIndexes.entrySet())
            {
                errorReporter.warning(metadataCellValue.getKey(),
                        getReportedLocationForCell(metadataCellValue.getValue()),
                        "Unknown header property");
            }
        }

        private Integer getValidatedColIndexForMetadataProperty(Map<String, Integer> metadataColumnIndexes,
                String nameInFile, Integer defaultValue)
        {
            if (metadataColumnIndexes.containsKey(nameInFile))
                return metadataColumnIndexes.remove(nameInFile);

            errorReporter.error(nameInFile, getReportedRowLocation(),
                    "Missing mandatory header property");
            return defaultValue;
        }

        private void readOtherRows()
        {
            while (loadNextTaggedRowInfo())
            {
                if ("dataset_row".equals(currentTag))
                    readDatasetRow();
                else if (LabelForBlockTags.LABELS_FOR_NUMERIC.containsKey(currentTag))
                    readBlock(LabelForBlockTags.LABELS_FOR_NUMERIC.get(currentTag));
                else if (LabelForBlockTags.LABELS_FOR_RATIO.containsKey(currentTag))
                    readBlock(LabelForBlockTags.LABELS_FOR_RATIO.get(currentTag));
                else
                    readData();
            }
        }

        private void readDatasetRow()
        {
            readCrop();
            readCountry();
            readSystemBoundary();
        }

        private void readCrop()
        {
            extractedCells.put("crop", currentTaggedRow.getCell(labelColumnIndex));
        }

        private void readCountry()
        {
            extractedCells.put("country", currentTaggedRow.getCell(countryColumnIndex));
        }

        private void readSystemBoundary()
        {
            extractedCells.put("system_boundary", currentTaggedRow.getCell(dataColumnIndex));
        }

        private void readBlock(Map<String, String> dropDownValues)
        {
            int rowIndex = currentTaggedRow.getRowNum();
            Row tmpRow;
            String tag;

            while (rowIndex < sheet.getLastRowNum())
            {
                rowIndex++;
                tmpRow = sheet.getRow(rowIndex);
                if (tmpRow == null)
                    continue;

                tag = POIHelper.getCellStringValue(tmpRow, METADATA_COLUMN_INDEX, "");
                if (!tag.isEmpty())
                    break;
                else
                {
                    readBlockData(tmpRow, dropDownValues);
                }
            }
        }

        private void readBlockData(Row blockRow, Map<String, String> dropDownValues)
        {
            String label = POIHelper.getCellStringValue(blockRow, labelColumnIndex, null);
            String validatedLabel = dropDownValues.get(label);
            if (validatedLabel == null)
                errorReporter.warning(currentTag, POIHelper.getCellLocationForLogs(blockRow, labelColumnIndex),
                        "Unknown value");
            else
            {
                extractedCells.put(currentTag + validatedLabel, blockRow.getCell(dataColumnIndex));
            }
        }

        private void readData()
        {
            extractedCells.put(currentTag, currentTaggedRow.getCell(dataColumnIndex));
        }

        private String getReportedLocationForCell(int colIndex)
        {
            return POIHelper.getCellLocationForLogs(currentTaggedRow, colIndex);
        }

        private String getReportedRowLocation()
        {
            return POIHelper.getLocationForLogs(currentTaggedRow);
        }

        private boolean loadNextTaggedRowInfo()
        {
            Row tmpRow = null;
            String cellValue = null;
            int rowIndex = currentTaggedRow.getRowNum();
            while (rowIndex < sheet.getLastRowNum() && cellValue == null)
            {
                rowIndex++;
                tmpRow = sheet.getRow(rowIndex);
                if (tmpRow == null)
                    continue;

                cellValue = POIHelper.getCellStringValue(tmpRow, METADATA_COLUMN_INDEX, null);
            }

            if (cellValue != null)
            {
                currentTaggedRow = tmpRow;
                currentTag = cellValue;
            }
            return cellValue != null;
        }
    }
}
