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

import com.quantis_intl.commons.scsv.Uncertainty;
import com.quantis_intl.commons.scsv.beans.UncertaintyBean;

public class PesticideEmissions extends PesticideProductUsage
{
    private final OutputTarget target;

    public PesticideEmissions(String variable, String amount, String comment, OutputTarget target)
    {
        super(variable, amount, comment);
        this.target = target;
    }

    @Override
    public String getName()
    {
        String dbName = target == OutputTarget.WFLDB ? "WFLDB 3.3" : "ALCIG";
        if (variable.startsWith("pesti_herbicides"))
            return "Emissions from herbicides, unspecified (" + dbName + ")/GLO S";
        else if (variable.startsWith("pesti_fungicides"))
            return "Emissions from fungicides, unspecified (" + dbName + ")/GLO S";
        else if (variable.startsWith("pesti_insecticides"))
            return "Emissions from insecticides, unspecified (" + dbName + ")/GLO S";
        else
            throw new IllegalStateException();
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
        if (!comment.isEmpty())
            res += " - " + comment;
        return res;
    }
}
