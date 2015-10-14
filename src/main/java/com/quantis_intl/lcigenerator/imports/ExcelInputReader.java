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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.POIHelper;
import com.quantis_intl.lcigenerator.imports.Origin.ExcelUserInput;
import com.quantis_intl.lcigenerator.imports.SingleValue.DoubleSingleValue;
import com.quantis_intl.lcigenerator.imports.ValueGroup.DoubleValueGroup;
import com.quantis_intl.lcigenerator.imports.ValueGroup.PartValueGroup;
import com.quantis_intl.lcigenerator.imports.ValueGroup.RatioValueGroup;

public class ExcelInputReader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelInputReader.class);

    // FIXME: Ugly hardcode
    private static final String TOTAL_PESTICIDES = "pest_total";
    private static final String[] PARTS_PESTICIDES = { "herbicides", "fungicides", "insecticides" };
    private static final String TOTAL_MACHINERIES = "total_machinery_diesel";
    private static final String[] PARTS_MACHINERIES = { "plantprotection", "soilcultivation",
            "sowingplanting", "fertilisation", "harvesting", "otherworkprocesses" };

    public ValueGroup getInputDataFromFile(InputStream is, ErrorReporter errorReporter)
    {
        try
        {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = IntStream.range(0, workbook.getNumberOfSheets()).mapToObj(workbook::getSheetAt)
                    .filter(s -> s.getSheetName().startsWith("Template")).findFirst().orElse(null);
            if (sheet != null)
            {
                DataFileReader dataFileReader = new DataFileReader(sheet, errorReporter);
                return dataFileReader.getExtractedInputs();
            }
            else
            {
                errorReporter.error("The file must contain a tab named \"Template\". Please check your Excel file");
                return null;
            }
        }
        catch (InvalidFormatException e)
        {
            errorReporter.error("Sorry, we cannot read this file. Please use the Excel formats (.xls or .xlsx)");
        }
        catch (IOException e)
        {
            errorReporter.error("Sorry, an error happened when reading your file. Please contact us");
        }
        catch (IllegalArgumentException e)
        {
            errorReporter.error("Sorry, an error happened when reading your file. Please contact us");
        }
        return null;
    }

    private static class DataFileReader
    {
        private static final int METADATA_ROW_INDEX = 0;
        private static final int METADATA_COLUMN_INDEX = 0;

        private final Sheet sheet;
        private final ErrorReporter errorReporter;
        private final ValueGroup extractedInputs = new ValueGroup("");

        private final StringFromListExtractor stringFromListExtractor;
        private final StringExtractor stringExtractor;
        private final DateExtractor dateExtractor;
        private final NumericExtractor numericExtractor;

        private String fileVersion;

        private int labelColumnIndex = -1;
        private int countryColumnIndex = -1;
        private int dataColumnIndex = -1;
        private int commentColumnIndex = -1;
        private int sourceColumnIndex = -1;

        private Row currentRow;
        private String currentTag;
        private Cell currentDataCell;
        private Cell currentLabelCell;
        private Cell currentCommentCell;
        private Cell currentSourceCell;

        public DataFileReader(Sheet sheet, ErrorReporter errorReporter)
        {
            this.sheet = sheet;
            this.errorReporter = errorReporter;
            this.stringFromListExtractor = new StringFromListExtractor(errorReporter);
            this.stringExtractor = new StringExtractor();
            this.dateExtractor = new DateExtractor(errorReporter);
            this.numericExtractor = new NumericExtractor(errorReporter);

            readHiddenHeaderRow();
            if (!errorReporter.hasErrors())
            {
                readCropAndCountry();
                readOtherRows();
                gatherBlocklessRatiosAndParts();
                validateSumsAndRatios();
                validateDates();
            }
        }

        public ValueGroup getExtractedInputs()
        {
            return extractedInputs;
        }

        private void readHiddenHeaderRow()
        {
            Row headerRow = sheet.getRow(METADATA_ROW_INDEX);
            currentRow = headerRow;
            if (headerRow == null)
            {
                errorReporter.error("The structure of the template has been changed, please use the original template");
                return;
            }

            this.fileVersion = POIHelper.getCellStringValue(headerRow, METADATA_COLUMN_INDEX, null);
            // TODO: validate file version

            int nbFilledColumns = 0;
            boolean haveDuplicates = false;
            for (int columnIndex = METADATA_COLUMN_INDEX + 1; columnIndex < headerRow.getLastCellNum(); columnIndex++)
            {
                Cell cell = headerRow.getCell(columnIndex);
                if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING)
                    continue;

                switch (cell.getStringCellValue())
                {
                    case "label_column":
                    {
                        if (this.labelColumnIndex != -1)
                            haveDuplicates = true;
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
                            haveDuplicates = true;
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
                            haveDuplicates = true;
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
                            haveDuplicates = true;
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
                            haveDuplicates = true;
                        else
                        {
                            this.sourceColumnIndex = columnIndex;
                            nbFilledColumns++;
                        }
                        break;
                    }
                    default:
                        errorReporter
                                .warning(
                                        "The structure of the template has been changed, the imported data may not be accurate");
                }

            }

            if (nbFilledColumns < 5 || haveDuplicates)
                errorReporter.error("The structure of the template has been changed, please use the original template");
        }

        private void readCropAndCountry()
        {
            readCropOrCountry("crop");
            readCropOrCountry("country");
        }

        private void readCropOrCountry(String expectedTag)
        {
            loadNextMeaningfulRow(countryColumnIndex);
            Cell cell = currentRow.getCell(countryColumnIndex);
            Cell labelCell = currentRow.getCell(labelColumnIndex, Row.RETURN_BLANK_AS_NULL);
            if (expectedTag.equals(POIHelper.getCellStringValue(currentRow, METADATA_COLUMN_INDEX, null)))
            {
                String value = stringFromListExtractor.extractMandatory(expectedTag, labelCell, cell);
                if (value != null)
                {
                    extractedInputs.addValue(new SingleValue<String>(expectedTag, value, "", "",
                            new Origin.ExcelUserInput(POIHelper.getCellCoordinates(cell), POIHelper.getCellStringValue(
                                    labelCell, expectedTag))));
                }
            }
            else
                errorReporter.error("The structure of the template has been changed, please use the original template");
        }

        private void readOtherRows()
        {
            while (loadNextUsualDataCells())
            {
                if (LabelForBlockTags.LABELS_FOR_NUMERIC.containsKey(currentTag))
                    readBlock(LabelForBlockTags.LABELS_FOR_NUMERIC.get(currentTag));
                else if (LabelForBlockTags.LABELS_FOR_RATIO.containsKey(currentTag))
                    readRatioBlock(LabelForBlockTags.LABELS_FOR_RATIO.get(currentTag));
                // TODO: Propagate the info about the availability of a default value, useful for the generated warnings
                if (dataCellIsFilled())
                {
                    if (StringFromListExtractor.TAGS_TO_MAP.containsKey(currentTag))
                        stringFromListExtractor.extract(currentTag, currentLabelCell, currentDataCell,
                                currentCommentCell, currentSourceCell).ifPresent(extractedInputs::addValue);
                    else if (StringExtractor.TAGS_FOR_STRING.contains(currentTag))
                        stringExtractor.extract(currentTag, currentLabelCell, currentDataCell,
                                currentCommentCell, currentSourceCell).ifPresent(extractedInputs::addValue);
                    else if (DateExtractor.TAGS_FOR_STRING.contains(currentTag))
                        dateExtractor.extract(currentTag, currentLabelCell, currentDataCell,
                                currentCommentCell, currentSourceCell).ifPresent(extractedInputs::addValue);
                    else if (NumericExtractor.TAGS_FOR_NUMERIC.contains(currentTag))
                        numericExtractor.extractNumeric(currentTag, currentLabelCell, currentDataCell,
                                currentCommentCell, currentSourceCell).ifPresent(extractedInputs::addValue);
                    else if (NumericExtractor.TAGS_FOR_RATIO.contains(currentTag))
                        numericExtractor.extractRatio(currentTag, currentLabelCell, currentDataCell,
                                currentCommentCell, currentSourceCell).ifPresent(extractedInputs::addValue);
                    else if (!"Data".equals(POIHelper.getCellStringValue(currentDataCell, "Data")))
                        errorReporter
                                .warning(
                                        ImmutableMap.of("cell", POIHelper.getCellCoordinates(currentDataCell)),
                                        "This cell will be ignored, as we cannot find the corresponding variable. The structure of the template has probably been modified. Please use the original template");
                }
                // TODO: Not a good way to handle blocks, should be entierelly handled before the previous line-by-line
                // handling
            }

        }

        private boolean dataCellIsFilled()
        {
            String value = POIHelper.getCellStringValue(currentDataCell, "");
            if ("".equals(value) || (value.startsWith("<") && value.endsWith(">")) || "-".equals(value))
            {
                warnIfDefaultExists(currentTag);
                return false;
            }
            return true;
        }

        private void warnIfDefaultExists(String currentTag)
        {
            // TODO: Implement
        }

        private void readBlock(Map<String, String> dropDownValues)
        {
            DoubleSingleValue totalHolder = null;
            if (dataCellIsFilled())
            {
                totalHolder = numericExtractor.extractNumeric("total", currentLabelCell, currentDataCell,
                        currentCommentCell, currentSourceCell).orElse(null);
            }
            // FIXME: Not a good way
            ValueGroup group = new PartValueGroup(currentTag.replace("total_", ""), totalHolder);
            extractedInputs.addGroup(group);

            Row previousRow = currentRow;

            while (loadNextUsualDataCells())
            {
                if ("end".equals(currentTag))
                    break;

                if (!"".equals(currentTag) && !currentTag.equals("part_" + group.getLocalKey() + "_"))
                {
                    currentRow = previousRow;
                    break;
                }

                if (dataCellIsFilled())
                {
                    boolean keyIsMissing = false;
                    String key = dropDownValues.get(POIHelper.getCellStringValue(currentLabelCell, ""));
                    if (key == null)
                    {
                        keyIsMissing = true;
                        key = LabelForBlockTags.DEFAULT_VALUE;
                    }
                    final String fKey = key;
                    final boolean fKeyIsMissing = keyIsMissing;
                    numericExtractor
                            .extractNumeric(key, currentLabelCell, currentDataCell,
                                    currentCommentCell, currentSourceCell)
                            .ifPresent(
                                    sv ->
                                        {
                                            group.addValue(sv);
                                            if (fKeyIsMissing)
                                            {
                                                errorReporter.warning(ImmutableMap.of("cell",
                                                        POIHelper.getCellCoordinates(currentLabelCell), "label",
                                                        POIHelper.getCellStringValue(currentLabelCell, fKey)),
                                                        "Your type selection is not in the list. The value you entered will be considered as 'Other' if available, or ignored");
                                            }
                                        });
                }
            }

            previousRow = currentRow;
        }

        private void readRatioBlock(Map<String, String> dropDownValues)
        {
            DoubleSingleValue totalHolder = null;
            if (dataCellIsFilled())
            {
                totalHolder = numericExtractor.extractNumeric("total", currentLabelCell, currentDataCell,
                        currentCommentCell, currentSourceCell).orElse(null);
            }
            // FIXME: Not a good way
            ValueGroup group = new RatioValueGroup(currentTag.replace("total_", ""), totalHolder);
            extractedInputs.addGroup(group);

            Row previousRow = currentRow;

            while (loadNextUsualDataCells())
            {
                if ("end".equals(currentTag))
                    break;

                if (!"".equals(currentTag) && !currentTag.equals("ratio_" + group.getLocalKey() + "_"))
                {
                    currentRow = previousRow;
                    break;
                }

                if (dataCellIsFilled())
                {
                    boolean keyIsMissing = false;
                    String key = dropDownValues.get(POIHelper.getCellStringValue(currentLabelCell, ""));
                    if (key == null)
                    {
                        keyIsMissing = true;
                        key = LabelForBlockTags.DEFAULT_VALUE;
                    }
                    final String fKey = key;
                    final boolean fKeyIsMissing = keyIsMissing;
                    numericExtractor
                            .extractRatio(key, currentLabelCell, currentDataCell,
                                    currentCommentCell, currentSourceCell)
                            .ifPresent(
                                    sv ->
                                        {
                                            group.addValue(sv);
                                            if (fKeyIsMissing)
                                            {
                                                errorReporter.warning(
                                                        ImmutableMap.of("cell",
                                                                POIHelper.getCellCoordinates(currentLabelCell), "label",
                                                                POIHelper.getCellStringValue(currentLabelCell, fKey)),
                                                        "Your type selection is not in the list. The value you entered will be considered as \"Other\" if available, or ignored");
                                            }
                                        });
                }

                previousRow = currentRow;
            }
        }

        private void loadNextMeaningfulRow(int meaningFulColumn)
        {
            if (currentRow == null)
                return;

            Row tmpRow = null;
            Cell keyCell = null;
            Cell dataCell = null;
            int rowIndex = currentRow.getRowNum();
            int maxRow = sheet.getLastRowNum();
            while (rowIndex < maxRow && dataCell == null && keyCell == null)
            {
                rowIndex++;
                tmpRow = sheet.getRow(rowIndex);
                if (tmpRow == null)
                    continue;

                keyCell = tmpRow.getCell(METADATA_COLUMN_INDEX, Row.RETURN_BLANK_AS_NULL);
                dataCell = tmpRow.getCell(meaningFulColumn, Row.RETURN_BLANK_AS_NULL);
            }

            if (dataCell != null || keyCell != null)
                currentRow = tmpRow;
            else
                currentRow = null;
        }

        private boolean loadNextUsualDataCells()
        {
            loadNextMeaningfulRow(dataColumnIndex);
            if (currentRow != null)
            {
                currentTag = POIHelper.getCellStringValue(currentRow, METADATA_COLUMN_INDEX, "");
                currentDataCell = currentRow.getCell(dataColumnIndex, Row.RETURN_BLANK_AS_NULL);
                currentLabelCell = currentRow.getCell(labelColumnIndex, Row.RETURN_BLANK_AS_NULL);
                currentCommentCell = currentRow.getCell(commentColumnIndex, Row.RETURN_BLANK_AS_NULL);
                currentSourceCell = currentRow.getCell(sourceColumnIndex, Row.RETURN_BLANK_AS_NULL);
                return true;
            }
            return false;
        }

        private void gatherBlocklessRatiosAndParts()
        {
            extractedInputs.groupValues();
        }

        private void validateSumsAndRatios()
        {
            for (ValueGroup group : extractedInputs.getSubGroups().values())
            {
                if (group instanceof RatioValueGroup)
                {
                    DoubleValueGroup dvgroup = (DoubleValueGroup) group;
                    dvgroup.validateValues(
                            r -> errorReporter
                                    .warning(
                                            ImmutableMap.of("cell",
                                                    extractExcelOriginalCellCoordinates(dvgroup.getTotalHolder()),
                                                    "label",
                                                    extractExcelOriginalLabel(dvgroup.getTotalHolder())),
                                            "The sum of the entered proportions is not equals to 1. Please check again your repartition. For now we will normalize the values proportionally"),
                            total -> errorReporter.warning(
                                    ImmutableMap.of("cell",
                                            extractExcelOriginalCellCoordinates(dvgroup.getTotalHolder()), "label",
                                            extractExcelOriginalLabel(dvgroup.getTotalHolder())),
                                    "The sum of the entered proportions is not equals to 1. Please check again your repartition. For now we will normalize the values proportionally"),
                            // TODO: For now, only warn, as the default ratio (including "all in other" for
                            // machineries) is handled by python. Watch out!
                            z -> errorReporter.warning(
                                    ImmutableMap.of("cell",
                                            extractExcelOriginalCellCoordinates(dvgroup.getTotalHolder()), "label",
                                            extractExcelOriginalLabel(dvgroup.getTotalHolder())),
                                    "No repartition has been entered. For now, the default repartition will be used"));

                }
                else if (group instanceof PartValueGroup)
                    validateGroupAggregation((PartValueGroup) group);
            }
            // FIXME: Ugly hardcode

            // FIXME: This doesn't work, "other" addition or total modifications are not reflected in extractedInputs
            // FIXME: There are maybe memory leaks as well
            DoubleSingleValue pestiTotal = (DoubleSingleValue) extractedInputs.getSingleValue(TOTAL_PESTICIDES);
            PartValueGroup pestiGroup = new PartValueGroup("", pestiTotal);
            Arrays.stream(PARTS_PESTICIDES).forEach(
                    s ->
                        {
                            DoubleSingleValue dv = ((PartValueGroup) extractedInputs.getSubGroups().get(s))
                                    .getTotalHolder();
                            if (dv != null)
                                pestiGroup.addValue(dv);
                        });
            validateGroupAggregation(pestiGroup);
            SingleValue<?> other = pestiGroup.getSingleValue("other");
            if (other != null)
                extractedInputs.addValue(other.rename("pest_remains"));
            else if (!Objects.equals(pestiGroup.getTotalHolder(), pestiTotal))
                extractedInputs.replaceValue(new DoubleSingleValue(TOTAL_PESTICIDES, pestiGroup.getTotalHolder()
                        .getValue(), pestiTotal == null ? "" : pestiTotal.getComment(), pestiTotal == null ? ""
                                : pestiTotal.getSource(),
                        Origin.GENERATED_VALUE,
                        pestiTotal == null ? "" : pestiTotal.getUnit()));

            DoubleSingleValue machineriesTotal = (DoubleSingleValue) extractedInputs.getSingleValue(TOTAL_MACHINERIES);
            PartValueGroup machineriesGroup = new PartValueGroup("", machineriesTotal);
            Arrays.stream(PARTS_MACHINERIES).forEach(
                    s ->
                        {
                            DoubleSingleValue dv = (DoubleSingleValue) ((RatioValueGroup) extractedInputs.getSubGroups()
                                    .get(s)).getTotalHolder();
                            if (dv != null)
                                machineriesGroup.addValue(dv);
                        });
            validateGroupAggregation(machineriesGroup);
            SingleValue<?> otherMachineries = machineriesGroup.getSingleValue("other");
            if (otherMachineries != null)
                extractedInputs.addValue(otherMachineries.rename("remains_machinery_diesel"));
            else if (!Objects.equals(machineriesGroup.getTotalHolder(), machineriesTotal))
                extractedInputs.replaceValue(new DoubleSingleValue(TOTAL_MACHINERIES, machineriesGroup.getTotalHolder()
                        .getValue(), machineriesTotal == null ? "" : machineriesTotal.getComment(),
                        machineriesTotal == null ? "" : machineriesTotal.getSource(),
                        Origin.GENERATED_VALUE,
                        machineriesTotal == null ? "" : machineriesTotal.getUnit()));
        }

        private void validateGroupAggregation(PartValueGroup group)
        {
            group.validateValues(
                    remains ->
                        {
                            group.addValue(new DoubleSingleValue("other", remains,
                                    "Unaffected portion of the total", "", Origin.GENERATED_VALUE,
                                    group.getTotalHolder().getUnit()));
                            errorReporter
                                    .warning(
                                            ImmutableMap.of("cell",
                                                    extractExcelOriginalCellCoordinates(group.getTotalHolder()),
                                                    "label",
                                                    extractExcelOriginalLabel(group.getTotalHolder())),
                                            "The sum of the decomposition is lower than the entered total. The unaffected value will be considered as \"Other\"");
                        } ,
                    total ->
                        {
                            errorReporter.warning(
                                    ImmutableMap.of("cell", extractExcelOriginalCellCoordinates(group.getTotalHolder()),
                                            "label",
                                            extractExcelOriginalLabel(group.getTotalHolder())),
                                    "The sum of the decomposition is higher than the entered total. Please check again your value. For now the higher total will be considered");
                            group.replaceTotalValue(total);
                        } ,
                    z ->
                        {
                            group.addValue(new DoubleSingleValue("other", group.getTotalHolder().getValue(),
                                    "Unaffected portion of the total", "", Origin.GENERATED_VALUE,
                                    group.getTotalHolder()
                                            .getUnit()));
                            errorReporter.warning(
                                    ImmutableMap.of("cell", extractExcelOriginalCellCoordinates(group.getTotalHolder()),
                                            "label",
                                            extractExcelOriginalLabel(group.getTotalHolder())),
                                    "The total has not been detailed. The value will be entirely considered as \"Other\"");
                        });
        }

        private String extractExcelOriginalLabel(SingleValue<?> value)
        {
            ExcelUserInput origin = extractExcelOriginalInput(value);
            return origin != null ? origin.label : "";
        }

        private String extractExcelOriginalCellCoordinates(SingleValue<?> value)
        {
            ExcelUserInput origin = extractExcelOriginalInput(value);
            return origin != null ? origin.cellCoordinates : "";
        }

        private ExcelUserInput extractExcelOriginalInput(SingleValue<?> value)
        {
            if (value != null && value.getOrigin() instanceof ExcelUserInput)
                return (ExcelUserInput) value.getOrigin();

            return null;
        }

        private void validateDates()
        {
            LocalDate previous_harvest = (LocalDate) Optional
                    .ofNullable(extractedInputs.getDeepSingleValue("harvest_date_previous_crop"))
                    .map(SingleValue::getValue).orElse(null);
            LocalDate harvest = (LocalDate) Optional
                    .ofNullable(extractedInputs.getDeepSingleValue("harvesting_date_main_crop"))
                    .map(SingleValue::getValue).orElse(null);

            if (previous_harvest != null && harvest != null && previous_harvest.plusDays(15).isAfter(harvest))
            {
                errorReporter.warning(
                        "The entered harvest dates are not coherents. The harvest date must be at least 15 days after the previous harvest date. We will consider a crop cycle of 1 year.");
                // TODO: We should remove the two dates
            }
        }
    }
}
