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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;
import com.quantis_intl.lcigenerator.api.Api;

public class ExcelInputReader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);

    public Map<String, RawInputLine> getInputDataFromFile(InputStream is, ErrorReporter errorReporter)
    {
        try
        {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheet("Template");
            if (sheet != null)
            {
                DataFileReader dataFileReader = new DataFileReader(sheet, errorReporter);
                return dataFileReader.getExtractedInputs();
            }
            else
            {
                errorReporter.error("sheet", "", "invalid file: sheet 'Template' not found");
                return null;
            }
        }
        catch (InvalidFormatException e)
        {
            errorReporter.error("file", "", "invalid format for file");
        }
        catch (IOException e)
        {
            errorReporter.error("file", "", "file reading failed");
        }
        catch (IllegalArgumentException e)
        {
            errorReporter.error("file", "", "invalid format for file");
        }
        return null;
    }

    private static class DataFileReader
    {
        private static final int METADATA_ROW_INDEX = 0;
        private static final int METADATA_COLUMN_INDEX = 0;

        private final Sheet sheet;
        private final ErrorReporter errorReporter;
        private final Map<String, RawInputLine> extractedInputs = new HashMap<>();

        private String fileVersion;

        private int labelColumnIndex = -1;
        private int countryColumnIndex = -1;
        private int dataColumnIndex = -1;
        private int commentColumnIndex = -1;
        private int sourceColumnIndex = -1;
        private int dataLevelColumnIndex = -1;

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

        public Map<String, RawInputLine> getExtractedInputs()
        {
            return extractedInputs;
        }

        private void readHiddenHeaderRow()
        {
            currentTaggedRow = sheet.getRow(METADATA_ROW_INDEX);
            if (currentTaggedRow == null)
                errorReporter.error("", "", "Bad template, please use the original template");

            this.fileVersion = POIHelper.getCellStringValue(currentTaggedRow, METADATA_COLUMN_INDEX, null);
            // TODO: validate file version

            int nbFilledColumns = 0;
            for (int columnIndex = METADATA_COLUMN_INDEX + 1; columnIndex < currentTaggedRow.getLastCellNum(); columnIndex++)
            {

                Cell cell = currentTaggedRow.getCell(columnIndex);
                if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING)
                    continue;

                switch (cell.getStringCellValue())
                {
                    case "label_column":
                    {
                        if (this.labelColumnIndex != -1)
                            manageDuplicateProperty();
                        else
                        {
                            this.labelColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    case "country_column":
                    {
                        if (this.countryColumnIndex != -1)
                            manageDuplicateProperty();
                        else
                        {
                            this.countryColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    case "data_column":
                    {
                        if (this.dataColumnIndex != -1)
                            manageDuplicateProperty();
                        else
                        {
                            this.dataColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    case "comment_column":
                    {
                        if (this.commentColumnIndex != -1)
                            manageDuplicateProperty();
                        else
                        {
                            this.commentColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    case "source_column":
                    {
                        if (this.sourceColumnIndex != -1)
                            manageDuplicateProperty();
                        else
                        {
                            this.sourceColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    case "data_level_column":
                    {
                        if (this.dataLevelColumnIndex != -1)
                            manageDuplicateProperty();
                        else
                        {
                            this.dataLevelColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    default:
                        errorReporter.warning("", "", "Original template has been modified");
                }

            }

            if (nbFilledColumns < 6)
            {
                if (nbFilledColumns < 1)
                    errorReporter.error("", "", "Bad template, please use the original template");
                else
                    errorReporter.error("", "", "Too deep modification of the original template");
            }
        }

        private void manageDuplicateProperty()
        {
            errorReporter.error("", "", "Too deep modification of the original template");
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
            addCellToExtractedInputs("crop", "Crop", currentTaggedRow.getCell(labelColumnIndex));
        }

        private void readCountry()
        {
            addCellToExtractedInputs("country", "Country", currentTaggedRow.getCell(countryColumnIndex));
        }

        private void readSystemBoundary()
        {
            addCellToExtractedInputs("system_boundary", "System boundary", currentTaggedRow.getCell(dataColumnIndex));
        }

        private void readData()
        {
            String title = POIHelper.getCellStringValue(currentTaggedRow, labelColumnIndex, "");
            addCellToExtractedInputs(currentTag, title, currentTaggedRow.getCell(dataColumnIndex));
        }

        private void addCellToExtractedInputs(String key, String title, Cell cell)
        {
            if (cell != null)
                extractedInputs.put(key, new ExcelRawInputLine(key, title, currentTaggedRow.getRowNum(), cell));
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
            Cell cell = blockRow.getCell(dataColumnIndex);
            if (cell != null)
            {
                String title = POIHelper.getCellStringValue(blockRow, labelColumnIndex, "");
                String titleVar = dropDownValues.get(title);
                if (titleVar == null)
                {
                    titleVar = LabelForBlockTags.DEFAULT_VALUE;
                    if (!LabelForBlockTags.DEFAULT_TITLES.contains(title))
                    {
                        errorReporter.warning(title, Integer.toString(cell.getRowIndex()),
                                "Title is not part of choices list, use default");
                        LOGGER.warn("Unknown title for block " + currentTag
                                + ": "
                                + title
                                + ", default will be used");
                    }
                }

                String extractedInputKey = currentTag + titleVar;
                if (extractedInputs.containsKey(extractedInputKey))
                {
                    ExcelRawInputBlock inputLine = (ExcelRawInputBlock) extractedInputs.get(extractedInputKey);
                    inputLine.addCell(cell);
                }
                else
                {
                    ExcelRawInputBlock inputLine = new ExcelRawInputBlock(extractedInputKey, title,
                            currentTaggedRow.getRowNum(), cell);
                    extractedInputs.put(extractedInputKey, inputLine);
                }
            }
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

                // NOTE: don't read end row of block
                if (LabelForBlockTags.END_BLOCK_TAG.equals(cellValue))
                    continue;
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
