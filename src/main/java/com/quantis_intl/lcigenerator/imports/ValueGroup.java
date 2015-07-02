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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.DoubleConsumer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.quantis_intl.lcigenerator.imports.SingleValue.DoubleSingleValue;

public class ValueGroup
{
    protected String localKey;
    protected Multimap<String, SingleValue<?>> values;
    protected Map<String, ValueGroup> subGroups;

    public ValueGroup(String localKey)
    {
        this.localKey = localKey;
        this.values = ArrayListMultimap.create(256, 2);
        this.subGroups = new HashMap<>();
    }

    public ValueGroup addValue(SingleValue<?> value)
    {
        String key = value.getLocalKey();
        if (subGroups.containsKey(key))
            throw new IllegalStateException();
        values.put(key, value);
        return this;
    }

    public ValueGroup replaceValue(SingleValue<?> value)
    {
        String key = value.getLocalKey();
        if (subGroups.containsKey(key))
            throw new IllegalStateException();
        values.replaceValues(key, ImmutableList.of(value));
        return this;
    }

    public ValueGroup addGroup(ValueGroup group)
    {
        String key = group.getLocalKey();
        if (values.containsKey(key) || subGroups.put(key, group) != null)
            throw new IllegalStateException();
        return this;
    }

    public void convertAllValues(double factor)
    {
        Multimap<String, SingleValue<?>> newValues = ArrayListMultimap.create();
        values.values().stream().map(v -> ((DoubleSingleValue) v).convert(factor))
                .forEach(v -> newValues.put(v.getLocalKey(), v));
        values = newValues;
    }

    public void groupValues()
    {
        Iterator<String> it = values.keySet().iterator();
        while (it.hasNext())
        {
            String value = it.next();
            if (value.startsWith("ratio_"))
            {
                int secondUnderscore = value.indexOf('_', 6);
                String groupingKey = value.substring(6, secondUnderscore);
                ValueGroup newGroup = subGroups.computeIfAbsent(
                        groupingKey,
                        key -> {
                            RatioValueGroup vg = new RatioValueGroup(groupingKey,
                                    (DoubleSingleValue) getSingleValue("total_" + groupingKey));
                            subGroups.put(key, vg);
                            return vg;
                        });
                newGroup.addValue(getSingleValue(value).rename(value.substring(secondUnderscore + 1)));
                it.remove();
            }
            else if (value.startsWith("part_"))
            {
                int secondUnderscore = value.indexOf('_', 5);
                String groupingKey = value.substring(6, secondUnderscore);
                ValueGroup newGroup = subGroups.computeIfAbsent(
                        groupingKey,
                        key -> {
                            PartValueGroup vg = new PartValueGroup(groupingKey,
                                    (DoubleSingleValue) getSingleValue("total_" + groupingKey));
                            subGroups.put(key, vg);
                            return vg;
                        });
                newGroup.addValue(getSingleValue(value).rename(value.substring(secondUnderscore + 1)));
                it.remove();
            }
        }
    }

    public Map<String, Object> flattenValues()
    {
        return this.flattenValues("");
    }

    public Map<String, Object> flattenValues(String prefix)
    {
        Map<String, Object> res = Maps.newHashMapWithExpectedSize(values.size() + subGroups.size() * 8);
        values.values().forEach(sv -> res.put(prefix + localKey + sv.getLocalKey(), sv.getValue()));
        subGroups.values().forEach(sg -> res.putAll(sg.flattenValues(prefix + localKey)));
        return res;
    }

    public String getLocalKey()
    {
        return localKey;
    }

    public Map<String, ValueGroup> getSubGroups()
    {
        return subGroups;
    }

    public boolean hasValues()
    {
        return !values.isEmpty();
    }

    public Collection<SingleValue<?>> getValue(String key)
    {
        return values.get(key);
    }

    public SingleValue<?> getSingleValue(String key)
    {
        Collection<SingleValue<?>> res = getValue(key);
        if (res.size() != 1)
            return null;
        return Iterables.getFirst(values.get(key), null);
    }

    public SingleValue<?> getDeepSingleValue(String key)
    {
        SingleValue<?> res = getSingleValue(key);
        if (res == null)
            res = subGroups.values().stream()
                    .filter(vg -> key.startsWith(vg.localKey))
                    .map(vg -> vg.getDeepSingleValue(key.substring(vg.localKey.length() + 1)))
                    .filter(sv -> sv != null).findAny().orElse(null);

        return res;
    }

    public static abstract class DoubleValueGroup extends ValueGroup
    {
        public DoubleValueGroup(String localKey)
        {
            super(localKey);
        }

        public ValueGroup addValue(SingleValue<?> value)
        {
            if (!(value instanceof DoubleSingleValue))
                throw new IllegalStateException();
            return super.addValue(value);
        }

        public void validateValues(DoubleConsumer remainsConsumer,
                DoubleConsumer biggerTotalConsumer, DoubleConsumer zeroConsumer)
        {
            // TODO: Handle Unit
            double sum = values.values().stream().map(v -> (DoubleSingleValue) v)
                    .mapToDouble(DoubleSingleValue::getValue)
                    .sum();

            double total = getExpectedTotal();

            if (sum <= 0.001 && total > 0.001)
                zeroConsumer.accept(sum);
            else
            {
                double diff = total - sum;
                if (diff > 0.001)
                    remainsConsumer.accept(diff);
                else if (diff < -0.001)
                    biggerTotalConsumer.accept(sum);
            }
        }

        protected abstract double getExpectedTotal();

        public abstract DoubleSingleValue getTotalHolder();
    }

    public static class RatioValueGroup extends DoubleValueGroup
    {
        private DoubleSingleValue total;

        public RatioValueGroup(String localKey, DoubleSingleValue total)
        {
            super(localKey);
            this.total = total;
        }

        @Override
        public Map<String, Object> flattenValues(String prefix)
        {
            Map<String, Object> res = Maps.newHashMapWithExpectedSize(values.size() + subGroups.size() * 8);
            values.asMap()
                    .values()
                    .forEach(
                            valuesCollection -> {
                                String key = prefix + "ratio_" + localKey + "_"
                                        + Iterables.getFirst(valuesCollection, null).getLocalKey();
                                res.put(key,
                                        valuesCollection.stream().map(sv -> (DoubleSingleValue) sv)
                                                .mapToDouble(DoubleSingleValue::getValue).sum());
                            });
            subGroups.values().forEach(sg -> res.putAll(sg.flattenValues(prefix + localKey)));

            if (total != null)
                res.put(prefix + "total_" + localKey, total.getValue());
            return res;
        }

        @Override
        public void validateValues(DoubleConsumer remainsConsumer,
                DoubleConsumer biggerTotalConsumer, DoubleConsumer zeroConsumer)
        {
            super.validateValues(remainsConsumer.andThen(remains -> convertAllValues(1.d / (1.d - remains)))
                    , biggerTotalConsumer.andThen(total -> convertAllValues(1.d / total)),
                    z -> {
                        if (total != null && total.getValue() > 0.001)
                            zeroConsumer.accept(z);
                    });
        }

        @Override
        protected double getExpectedTotal()
        {
            return 1.d;
        }

        @Override
        public DoubleSingleValue getTotalHolder()
        {
            return total;
        }
    }

    public static class PartValueGroup extends DoubleValueGroup
    {
        private DoubleSingleValue total;

        public PartValueGroup(String localKey, DoubleSingleValue total)
        {
            super(localKey);
            this.total = total;
        }

        @Override
        public Map<String, Object> flattenValues(String prefix)
        {
            Map<String, Object> res = Maps.newHashMapWithExpectedSize(values.size() + subGroups.size() * 8);
            values.asMap()
                    .values()
                    .forEach(
                            valuesCollection -> {
                                String key = prefix + "part_" + localKey + "_"
                                        + Iterables.getFirst(valuesCollection, null).getLocalKey();
                                res.put(key,
                                        valuesCollection.stream().map(sv -> (DoubleSingleValue) sv)
                                                .mapToDouble(DoubleSingleValue::getValue).sum());
                            });
            subGroups.values().forEach(sg -> res.putAll(sg.flattenValues(prefix + localKey)));

            if (total != null)
                res.put(prefix + "total_" + localKey, total.getValue());
            return res;
        }

        @Override
        protected double getExpectedTotal()
        {
            return total == null ? 0.d : total.getValue();
        }

        @Override
        public DoubleSingleValue getTotalHolder()
        {
            return total;
        }

        public void replaceTotalValue(double newTotal)
        {
            total = new DoubleSingleValue("_total", newTotal,
                    total == null ? "" : total.getComment(), total == null ? "" : total.getSource(),
                    Origin.GENERATED_VALUE, total == null ? "TODO" : total.getUnit());
        }
    }
}
