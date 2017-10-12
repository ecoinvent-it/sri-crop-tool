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
package com.quantis_intl.lcigenerator.ecospold;

import java.util.UUID;

import com.quantis_intl.lcigenerator.scsv.StandardUncertaintyMetadata;

public interface TemplateSubstanceUsages
{
    TemplateSubstanceUsage[] getResources();

    TemplateSubstanceUsage[] getToAir();

    TemplateSubstanceUsage[] getToWater();

    TemplateSubstanceUsage[] getToSoil();

    public static class TemplateSubstanceUsage
    {
        public final String name;
        public final UUID subCompartment;
        public final AvailableUnit unit;
        public final String amountVariable;
        public final StandardUncertaintyMetadata uncertainty;
        public final String commentVariable;

        public TemplateSubstanceUsage(String name, UUID subCompartment, AvailableUnit unit, String amountVariable,
                                      StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            this.name = name;
            this.subCompartment = subCompartment;
            this.unit = unit;
            this.amountVariable = amountVariable;
            this.uncertainty = uncertainty;
            this.commentVariable = commentVariable;
        }
    }
}
