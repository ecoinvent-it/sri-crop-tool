package com.quantis_intl.lcigenerator.ecospold;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface TemplateIntermediaryExchanges
{
    TemplateIntermediaryExchange[] getMaterialsFuels();

    TemplateIntermediaryExchange[] getElectricityHeat();

    TemplateIntermediaryExchange[] getWastes();

    class TemplateIntermediaryExchange
    {
        private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.+?)\\}");

        private final String name;
        public final AvailableUnit unit;
        private final String amountVariable;
        public final StandardUncertaintyMetadata uncertainty;
        public final String commentVariable;
        //private final Optional<List<String>> requiredDep;
        private final double factor;

        public TemplateIntermediaryExchange(String name, AvailableUnit unit, String amountVariable,
                                            StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            this(name, unit, amountVariable, uncertainty, commentVariable, /*null,*/ 1.0d);
        }

        public TemplateIntermediaryExchange(String name, AvailableUnit unit, String amountVariable,
                                            StandardUncertaintyMetadata uncertainty, String commentVariable,
                                    /*List<String> requiredDep,*/ double factor)
        {
            this.name = name;
            this.unit = unit;
            this.amountVariable = amountVariable;
            this.uncertainty = uncertainty;
            this.commentVariable = commentVariable;
            //this.requiredDep = Optional.ofNullable(requiredDep);
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

        /*public Optional<List<String>> provideRequiredDep(Map<String, String> modelOutputs)
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
        }*/

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
