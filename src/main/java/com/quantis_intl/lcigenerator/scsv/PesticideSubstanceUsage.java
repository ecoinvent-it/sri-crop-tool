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

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Maps;
import com.quantis_intl.commons.scsv.ScsvEnums.SubCompartment;
import com.quantis_intl.commons.scsv.Uncertainty;
import com.quantis_intl.commons.scsv.beans.UncertaintyBean;
import com.quantis_intl.commons.scsv.processes.SubstanceUsage;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;

public class PesticideSubstanceUsage implements SubstanceUsage
{
    private static final Map<String, String> MAPPING = Maps.fromProperties(PropertiesLoader
            .loadProperties("/pesticides_substance_mapping.properties"));

    private final String variable;
    private final String amount;
    private final String comment;

    public PesticideSubstanceUsage(String variable, String amount, String comment)
    {
        this.variable = variable;
        this.amount = amount;
        this.comment = comment;
    }

    @Override
    public String getSubstanceName()
    {
        return Objects.requireNonNull(MAPPING.get(variable));
    }

    @Override
    public SubCompartment getSubCompartment()
    {
        return SubCompartment.SOIL_AGRICULTURAL;
    }

    @Override
    public String getUnit()
    {
        return "g";
    }

    @Override
    public String getAmount()
    {
        return amount;
    }

    @Override
    public Uncertainty getUncertainty()
    {
        return UncertaintyBean.LognormalBean
                .of(StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL.standardDeviation);
    }

    @Override
    public String getComment()
    {
        String res = StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL.pedigreeMatrix;
        res += " - Assumption - 100% of applied pesticides emitted to soil";
        if (!comment.isEmpty())
            res += " - " + comment;
        return res;
    }
}
