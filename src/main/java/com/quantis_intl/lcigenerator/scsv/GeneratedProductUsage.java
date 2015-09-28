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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Strings;
import com.quantis_intl.commons.scsv.Uncertainty;
import com.quantis_intl.commons.scsv.beans.UncertaintyBean;
import com.quantis_intl.commons.scsv.processes.ProductUsage;
import com.quantis_intl.commons.scsv.processes.WasteTreatmentUsage;
import com.quantis_intl.lcigenerator.imports.SingleValue;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.scsv.TemplateProductUsages.TemplateProductUsage;

public class GeneratedProductUsage implements ProductUsage, WasteTreatmentUsage
{
    private final TemplateProductUsage template;
    private final Map<String, String> modelOutputs;
    private final ValueGroup extractedInputs;

    public GeneratedProductUsage(TemplateProductUsage template, Map<String, String> modelOutputs,
            ValueGroup extractedInputs)
    {
        this.template = template;
        this.modelOutputs = modelOutputs;
        this.extractedInputs = extractedInputs;
    }

    public Optional<List<String>> getRequiredDep()
    {
        return template.provideRequiredDep(modelOutputs);
    }

    @Override
    public String getName()
    {
        return template.provideName(modelOutputs);
    }

    @Override
    public String getUnit()
    {
        return template.unit;
    }

    @Override
    public String getAmount()
    {
        return template.provideValue(modelOutputs);
    }

    @Override
    public Uncertainty getUncertainty()
    {
        return UncertaintyBean.LognormalBean.of(template.uncertainty.standardDeviation);
    }

    @Override
    public String getComment()
    {
        String res = template.uncertainty.pedigreeMatrix;
        if (template.commentVariable.isEmpty())
            return res;

        SingleValue<?> vg = extractedInputs.getDeepSingleValue(template.commentVariable);
        if (vg != null && !Strings.isNullOrEmpty(vg.getComment()))
            res += " - " + vg.getComment();
        return res;
    }

    @Override
    public String[] asArray()
    {
        return ProductUsage.super.asArray();
    }
}
