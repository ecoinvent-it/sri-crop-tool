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

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.quantis_intl.lcigenerator.scsv.lib.processes.Product;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ProductUsage;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcess.ProductScsvProcess;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.CategoryType;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Status;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Type;
import com.quantis_intl.lcigenerator.scsv.lib.processes.SubstanceUsage;

public class GeneratedProcess implements ProductScsvProcess
{
    private final Map<String, String> modelOutputs;

    public GeneratedProcess(Map<String, String> modelOutputs)
    {
        this.modelOutputs = modelOutputs;
    }

    @Override
    public CategoryType getCategoryType()
    {
        return CategoryType.Material;
    }

    @Override
    public Type getType()
    {
        return Type.Unit;
    }

    @Override
    public Optional<String> getName()
    {
        // TODO
        return Optional.of("TODO");
    }

    @Override
    public Status getStatus()
    {
        // TODO: Validate
        return Status.ToBeReviewed;
    }

    // TODO: Override other fields?

    @Override
    public LocalDate getDate()
    {
        // TODO: Generate the date based on the user location?
        return LocalDate.now(ZoneOffset.UTC);
    }

    @Override
    public Optional<String> getRecord()
    {
        // TODO
        return Optional.of("TODO");
    }

    @Override
    public Optional<String> getGenerator()
    {
        // TODO: Change
        return Optional.of("Quantis LCI Generator");
    }

    @Override
    public Optional<String> getCollectionMethod()
    {
        // TODO
        return Optional.of("TODO");
    }

    @Override
    public Optional<String> getDataTreatment()
    {
        // TODO
        return Optional.of("TODO");
    }

    @Override
    public Optional<String> getComment()
    {
        // TODO
        return Optional.of("TODO");
    }

    @Override
    public List<Product> getProducts()
    {
        // TODO
        return ImmutableList.of(new Product()
        {
            @Override
            public String getName()
            {
                return "TODO";
            }

            @Override
            public String getUnit()
            {
                return "kg";
            }

            @Override
            public String getAmount()
            {
                return "1.0";
            }

            @Override
            public String getCategory()
            {
                return "TODO";
            }

            @Override
            public String getComment()
            {
                return "TODO";
            }
        });
    }

    @Override
    public List<SubstanceUsage> getResources()
    {
        return toSubstanceUsage(TemplateSubstanceUsage.resources);
    }

    @Override
    public List<ProductUsage> getMaterialsFuels()
    {
        return toProductUsage(TemplateProductUsage.materialsFuels);
    }

    @Override
    public List<ProductUsage> getElectricityHeat()
    {
        return toProductUsage(TemplateProductUsage.electricityHeat);
    }

    @Override
    public List<SubstanceUsage> getEmissionsToAir()
    {
        return toSubstanceUsage(TemplateSubstanceUsage.toAir);
    }

    @Override
    public List<SubstanceUsage> getEmissionsToWater()
    {
        return toSubstanceUsage(TemplateSubstanceUsage.toWater);
    }

    @Override
    public List<SubstanceUsage> getEmissionsToSoil()
    {
        // TODO: Add persiticides
        return toSubstanceUsage(TemplateSubstanceUsage.toSoil);
    }

    @Override
    public List<ProductUsage> getWasteToTreatment()
    {
        return toProductUsage(TemplateProductUsage.wastes);
    }

    // TODO: map modelOutputs to template then sort by category, instead of this?
    private List<SubstanceUsage> toSubstanceUsage(TemplateSubstanceUsage[] templates)
    {
        return Arrays.stream(templates).map(r -> new GeneratedSubstanceUsage(r, modelOutputs))
                // .filter(s -> !s.getAmount().equals("0.0")) FIXME: Re-add
                .collect(Collectors.toList());
    }

    // TODO: map modelOutputs to template then sort by category, instead of this?
    private List<ProductUsage> toProductUsage(TemplateProductUsage[] templates)
    {
        return Arrays.stream(templates).map(r -> new GeneratedProductUsage(r, modelOutputs))
                // .filter(s -> !s.getAmount().equals("0.0")) FIXME: Re-add
                .collect(Collectors.toList());
    }
}
