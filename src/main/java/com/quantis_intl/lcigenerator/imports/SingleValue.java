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

public class SingleValue<T>
{
    private String localKey;

    private T value;

    private String comment;

    private String source;

    private Origin origin;

    @SuppressWarnings("unused")
    private SingleValue()
    {
    }

    public SingleValue(String localKey, T value, String comment, String source, Origin origin)
    {
        this.localKey = localKey;
        this.value = value;
        this.comment = comment;
        this.source = source;
        this.origin = origin;
    }

    public String getLocalKey()
    {
        return localKey;
    }

    public T getValue()
    {
        return value;
    }

    public String getComment()
    {
        return comment;
    }

    public String getSource()
    {
        return source;
    }

    public Origin getOrigin()
    {
        return origin;
    }

    public SingleValue<T> rename(String newKey)
    {
        return new SingleValue<T>(newKey, value, comment, source, origin);
    }

    public static class DoubleSingleValue extends SingleValue<Double>
    {
        private String unit;

        public DoubleSingleValue(String localKey, Double value, String comment, String source, Origin origin,
                String unit)
        {
            super(localKey, value, comment, source, origin);
            this.unit = unit;
        }

        public String getUnit()
        {
            return unit;
        }

        @Override
        public SingleValue<Double> rename(String newKey)
        {
            return new DoubleSingleValue(newKey, getValue(), getComment(), getSource(), getOrigin(), unit);
        }

        public DoubleSingleValue convert(double factor)
        {
            return new DoubleSingleValue(getLocalKey(), getValue() * factor, getComment(), getSource(),
                    Origin.GENERATED_VALUE, unit);
        }
    }

}
