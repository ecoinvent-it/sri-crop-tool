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

public class TemplateSubstanceUsage
{
    public final String name;
    public final String subCompartment;
    public final String unit;
    public final String amountVariable;
    public final String commentVariable;

    public TemplateSubstanceUsage(String name, String subCompartment, String unit, String amountVariable,
            String commentVariable)
    {
        this.name = name;
        this.subCompartment = subCompartment;
        this.unit = unit;
        this.amountVariable = amountVariable;
        this.commentVariable = commentVariable;
    }

    public static final TemplateSubstanceUsage[] resources = {
            new TemplateSubstanceUsage("Occupation, arable", "", "m2a", "", ""),
            new TemplateSubstanceUsage("Occupation, permanent crop", "", "m2a", "", ""),
            new TemplateSubstanceUsage("Tranformation, from {TODO}", "", "m2", "", ""),
            new TemplateSubstanceUsage("Tranformation, to {TODO}", "", "m2", "", ""),
            new TemplateSubstanceUsage("Gross energy {TODO}", "", "MJ", "", ""),
            new TemplateSubstanceUsage("Carbon dioxide, in air", "", "kg", "", ""),
            new TemplateSubstanceUsage("Carbon dioxide, in air", "", "kg", "", "")
    };

    public static final TemplateSubstanceUsage[] toAir = {
            new TemplateSubstanceUsage("Carbon dioxide, land transformation", "low. pop.", "kg", "", ""),
            new TemplateSubstanceUsage("Ammonia", "low. pop.", "kg", "ammonia_total", ""),
            new TemplateSubstanceUsage("Carbon dioxide, fossil", "low. pop.", "kg", "CO2_from_fertilisers", ""),
            new TemplateSubstanceUsage("Nitrogen oxides", "low. pop.", "kg", "Nox_as_n2o_air", ""),
            new TemplateSubstanceUsage("Dinitrogen monoxide", "low. pop.", "kg", "N2o_air", ""),
            new TemplateSubstanceUsage("Water, {TODO}", "low. pop.", "kg", "", "")
    };

    public static final TemplateSubstanceUsage[] toWater = {
            new TemplateSubstanceUsage("Nitrate", "river", "kg", "nitrate_to_surfacewater", ""),
            new TemplateSubstanceUsage("Nitrate", "groundwater", "kg", "nitrate_to_groundwater", ""),
            new TemplateSubstanceUsage("Phosphorus", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Phosphate", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Phosphate", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Cadmium, ion", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Cadmium, ion", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Chromium, ion", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Chromium, ion", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Copper, ion", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Copper, ion", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Lead", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Lead", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Mercury", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Mercury", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Nickel, ion", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Nickel, ion", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Zinc, ion", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Zinc, ion", "groundwater", "kg", "", ""),
            new TemplateSubstanceUsage("Water, {TODO}", "river", "kg", "", ""),
            new TemplateSubstanceUsage("Water, {TODO}", "groundwater", "kg", "", "")
    };

    public static final TemplateSubstanceUsage[] toSoil = {
            new TemplateSubstanceUsage("Cadmium", "agricultural", "kg", "", ""),
            new TemplateSubstanceUsage("Chromium", "agricultural", "kg", "", ""),
            new TemplateSubstanceUsage("Copper", "agricultural", "kg", "", ""),
            new TemplateSubstanceUsage("Lead", "agricultural", "kg", "", ""),
            new TemplateSubstanceUsage("Mercury", "agricultural", "kg", "", ""),
            new TemplateSubstanceUsage("Nickel", "agricultural", "kg", "", ""),
            new TemplateSubstanceUsage("Zinc", "agricultural", "kg", "", "")
            // FIXME: Persticides
    };
}
