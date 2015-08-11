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
package com.quantis_intl.lcigenerator.scsv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface TemplateProductUsages
{
    TemplateProductUsage[] getMaterialsFuels();

    TemplateProductUsage[] getElectricityHeat();

    TemplateProductUsage[] getWastes();

    public static class TemplateProductUsage
    {
        private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.+?)\\}");

        private final String name;
        public final String unit;
        private final String amountVariable;
        public final StandardUncertaintyMetadata uncertainty;
        public final String commentVariable;
        private final Optional<List<String>> requiredDep;
        private final double factor;

        public TemplateProductUsage(String name, String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            this(name, unit, amountVariable, uncertainty, commentVariable, null, 1.0d);
        }

        public TemplateProductUsage(String name, String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable, double factor)
        {
            this(name, unit, amountVariable, uncertainty, commentVariable, null, factor);
        }

        public TemplateProductUsage(String name, String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable, List<String> requiredDep)
        {
            this(name, unit, amountVariable, uncertainty, commentVariable, requiredDep, 1.0d);
        }

        public TemplateProductUsage(String name, String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable, List<String> requiredDep, double factor)
        {
            this.name = name;
            this.unit = unit;
            this.amountVariable = amountVariable;
            this.uncertainty = uncertainty;
            this.commentVariable = commentVariable;
            this.requiredDep = Optional.ofNullable(requiredDep);
            this.factor = factor;
        }

        public String provideName(Map<String, String> modelOutputs)
        {
            Matcher matcher = VARIABLE_PATTERN.matcher(name);
            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (matcher.find())
            {
                String replacement = lookupVariable(modelOutputs, matcher.group(1));
                builder.append(name.substring(i, matcher.start()));
                if (replacement == null)
                    // TODO: Warn
                    builder.append(matcher.group(0));
                else
                    builder.append(replacement);
                i = matcher.end();
            }
            builder.append(name.substring(i, name.length()));
            return builder.toString();
        }

        public Optional<List<String>> provideRequiredDep(Map<String, String> modelOutputs)
        {
            if (requiredDep.isPresent())
            {
                List<String> res = new ArrayList<String>(requiredDep.get().size());
                for (String s : requiredDep.get())
                {
                    Matcher matcher = VARIABLE_PATTERN.matcher(s);
                    StringBuilder builder = new StringBuilder();
                    int i = 0;
                    while (matcher.find())
                    {
                        String replacement = lookupVariable(modelOutputs, matcher.group(1));
                        builder.append(s.substring(i, matcher.start()));
                        if (replacement == null)
                            // TODO: Warn
                            builder.append(matcher.group(0));
                        else
                            builder.append(replacement);
                        i = matcher.end();
                    }
                    builder.append(s.substring(i, s.length()));
                    res.add(builder.toString());
                }
                return Optional.of(res);
            }
            return requiredDep;
        }

        public String provideValue(Map<String, String> modelOutputs)
        {
            // TODO: This is dirty...
            try
            {
                double value = Double.parseDouble(modelOutputs.getOrDefault(amountVariable, "0"));
                return Double.toString(value * factor);
            }
            catch (NumberFormatException e)
            {
                // TODO: ... and potentially without effect
                return modelOutputs.getOrDefault(amountVariable, "0");
            }
        }

        protected String lookupVariable(Map<String, String> modelOutputs, String variable)
        {
            return modelOutputs.get(variable);
        }
    }
}
