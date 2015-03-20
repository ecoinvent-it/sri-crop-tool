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
package com.quantis_intl.lcigenerator.scsv.lib.processes;

public interface Uncertainty
{
    public static final Uncertainty UNDEFINED = new Uncertainty()
    {
    };

    default Distribution getDistribution()
    {
        return Distribution.Undefined;
    }

    default String[] asArray()
    {
        return new String[] { getDistribution().toString(), "0", "0", "0" };
    }

    public static enum Distribution
    {
        Undefined,
        Lognormal,
        Normal,
        Triangle,
        Uniform
    }

    public static interface Lognormal extends Uncertainty
    {
        default Distribution getDistribution()
        {
            return Distribution.Lognormal;
        }

        default String getStandardDeviation()
        {
            return "0";
        }

        default String[] asArray()
        {
            return new String[] { getDistribution().toString(), getStandardDeviation(), "0", "0" };
        }
    }

    public static interface Normal extends Uncertainty
    {
        default Distribution getDistribution()
        {
            return Distribution.Normal;
        }

        default String getStandardDeviation()
        {
            return "0";
        }

        default String[] asArray()
        {
            return new String[] { getDistribution().toString(), getStandardDeviation(), "0", "0" };
        }
    }

    public static interface Triangle extends Uncertainty
    {
        default Distribution getDistribution()
        {
            return Distribution.Triangle;
        }

        default String getMin()
        {
            return "0";
        }

        default String getMax()
        {
            return "0";
        }

        default String[] asArray()
        {
            return new String[] { getDistribution().toString(), "0", getMin(), getMax() };
        }
    }

    public static interface Uniform extends Uncertainty
    {
        default Distribution getDistribution()
        {
            return Distribution.Uniform;
        }

        default String getMin()
        {
            return "0";
        }

        default String getMax()
        {
            return "0";
        }

        default String[] asArray()
        {
            return new String[] { getDistribution().toString(), "0", getMin(), getMax() };
        }
    }
}
