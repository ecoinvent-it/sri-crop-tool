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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;
import com.quantis_intl.lcigenerator.ErrorReporter;

public class RawInputToPyCompatibleConvertor
{
    private final ErrorReporter errorReporter;
    private final StringFromListExtractor stringFromListExtractor;
    private final StringExtractor stringExtractor;
    private final DateExtractor dateExtractor;
    private final NumericExtractor numericExtractor;

    public RawInputToPyCompatibleConvertor(ErrorReporter errorReporter)
    {
        this.errorReporter = errorReporter;
        this.stringFromListExtractor = new StringFromListExtractor(errorReporter);
        this.stringExtractor = new StringExtractor(errorReporter);
        this.dateExtractor = new DateExtractor(errorReporter);
        this.numericExtractor = new NumericExtractor(errorReporter);
    }

    public Map<String, Object> getValidatedData(Map<String, RawInputLine> cells, ErrorReporter errorReporter)
    {
        Map<String, Object> refinedInputs = Maps.newHashMapWithExpectedSize(cells.size());
        cells.entrySet().stream().filter(e -> e.getValue().isValuePresent())
                .forEach(e -> {
                    Object o = this.rawInputLineToObject(e);
                    if (o != null)
                        refinedInputs.put(e.getKey(), o);
                });

        AllValidatorsAndNormalizers allValidators = new AllValidatorsAndNormalizers(refinedInputs, errorReporter);
        allValidators.validateAndNormalize();

        return refinedInputs;
    }

    private Object rawInputLineToObject(Map.Entry<String, RawInputLine> lineEntry)
    {
        String varName = lineEntry.getKey();
        RawInputLine line = lineEntry.getValue();
        if (StringFromListExtractor.TAGS_TO_MAP.containsKey(varName)
                || StringFromListExtractor.MANDATORY_TAGS_TO_MAP.containsKey(varName))
            return stringFromListExtractor.extract(line);
        else if (StringExtractor.TAGS_FOR_STRING.contains(varName))
            return stringExtractor.extract(line);
        else if (DateExtractor.TAGS_FOR_STRING.contains(varName))
            return dateExtractor.extract(line);
        else if (NumericExtractor.TAGS_FOR_NUMERIC.contains(varName))
            return numericExtractor.extractNumeric(line);
        else if (NumericExtractor.TAGS_FOR_RATIO.contains(varName))
            return numericExtractor.extractRatio(line);
        else
        {
            String prefix = varName.substring(0, varName.indexOf('_', varName.indexOf('_') + 1) + 1);
            if (LabelForBlockTags.LABELS_FOR_NUMERIC.containsKey(prefix))
                return numericExtractor.extractNumeric(line);
            else if (LabelForBlockTags.LABELS_FOR_RATIO.containsKey(prefix))
                return numericExtractor.extractRatio(line);
            else
            {
                // TODO: Log
                return null;
            }
        }
    }

    // TODO: Refactor, have multiples validators instances. Take care of validator order and deps
    private static class AllValidatorsAndNormalizers
    {
        private final Map<String, Object> inputs;
        private final ErrorReporter errorReporter;

        private final Map<String, Map<String, Double>> ratiosPerGroup = new HashMap<>();
        private final Map<String, Map<String, Double>> partsPerGroup = new HashMap<>();

        public AllValidatorsAndNormalizers(Map<String, Object> inputs, ErrorReporter errorReporter)
        {
            this.inputs = inputs;
            this.errorReporter = errorReporter;
        }

        public void validateAndNormalize()
        {
            Iterator<Map.Entry<String, Object>> it = inputs.entrySet().iterator();
            while (it.hasNext())
            {
                if (!validate(it.next()))
                    it.remove();
            }

            finish();
        }

        private boolean validate(Map.Entry<String, Object> entry)
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null)
                return false;
            else
            {
                String[] splited = key.split("_");

                if ("ratio".equals(splited[0]))
                    ratiosPerGroup.computeIfAbsent(splited[1], k -> new HashMap<>()).put(key, (Double) value);
                else if ("part".equals(splited[0]))
                    partsPerGroup.computeIfAbsent(splited[1], k -> new HashMap<>()).put(key, (Double) value);

                return true;
            }
        }

        private void finish()
        {
            for (Map.Entry<String, Map<String, Double>> entry : ratiosPerGroup.entrySet())
                validateAndNormalizeRatios(entry);
            for (Map.Entry<String, Map<String, Double>> entry : partsPerGroup.entrySet())
                validateAggregations(entry);

            // TODO: Ugly hardcode
            validateMandatoryFields("crop", "country");
            validateSum("total_machinery_diesel", "total_plantprotection", "total_soilcultivation",
                    "total_sowingplanting", "total_fertilisation", "total_harvesting", "total_otherworkprocesses");
            validateSum("pest_total", "total_herbicides", "total_fungicides", "total_insecticides");
            validateDatesInOrder("harvest_date_previous_crop", "soil_cultivating_date_main_crop",
                    "sowing_date_main_crop", "harvesting_date_main_crop");
        }

        private void validateAndNormalizeRatios(Map.Entry<String, Map<String, Double>> ratiosEntry)
        {
            Map<String, Double> ratios = ratiosEntry.getValue();
            double sum = ratios.values().stream().mapToDouble(Double::doubleValue).sum();
            if (sum <= 0.001)
            {
                // FIXME: Warn
                inputs.keySet().removeAll(ratios.keySet());
            }
            else
            {
                if (0.999 < sum || sum > 1.001)
                {
                    // TODO: Warn
                    inputs.putAll(Maps.transformValues(ratios, v -> v / sum));
                }
            }
        }

        private void validateAggregations(Map.Entry<String, Map<String, Double>> partsEntry)
        {
            Map<String, Double> parts = partsEntry.getValue();
            double sum = parts.values().stream().mapToDouble(Double::doubleValue).sum();
            validateSum("total_" + partsEntry.getKey(), sum);
        }

        private void validateMandatoryFields(String... mandatoryKeys)
        {
            for (String key : mandatoryKeys)
            {
                if (!inputs.containsKey(key))
                    errorReporter.error(key, "", "Mandatory field not found");
            }
        }

        private void validateSum(String totalKey, String... partKeys)
        {
            double sum = Arrays.stream(partKeys).mapToDouble(k -> (Double) inputs.getOrDefault(k, 0.0)).sum();
            validateSum(totalKey, sum);
        }

        private <T> void validateSum(String totalKey, double sum)
        {
            double enteredTotal = (Double) inputs.getOrDefault(totalKey, -1.0);
            // FIXME: what about "else" case? There is a thing about "other"
            if (Math.abs(sum - enteredTotal) > 0.000001)
            {
                if (sum > enteredTotal)
                {
                    // FIXME: Warn
                    inputs.put(totalKey, sum);
                }
                else
                {
                    // FIXME: Warn
                    inputs.put(totalKey.replace("total", "remains"), enteredTotal - sum);
                }
            }
        }

        // FIXME: Warn
        private void validateDatesInOrder(String... variables)
        {
            LocalDate previousDate = (LocalDate) inputs.get(variables[0]);
            if (previousDate == null)
                removeVariables(variables);
            for (int i = 1; i < variables.length; i++)
            {
                LocalDate otherDate = (LocalDate) inputs.get(variables[i]);
                if (otherDate == null || previousDate.isAfter(otherDate))
                {
                    removeVariables(variables);
                    break;
                }
                previousDate = otherDate;
            }
        }

        private void removeVariables(String... variables)
        {
            inputs.keySet().removeAll(Arrays.asList(variables));
        }
    }

    /*private void manageUnresolvedCells(Map<String, Cell> cells, ErrorReporter errorReporter)
    {
        for (Entry<String, Cell> cellEntry : cells.entrySet())
        {
            if (cellEntry.getValue() == null)
                errorReporter.warning(cellEntry.getKey(), "", "Unresolved empty property");
            else
                errorReporter.warning(cellEntry.getKey(), POIHelper.getCellLocationForLogs(cellEntry.getValue()),
                        "Unresolved property");
        }
    }*/
}
