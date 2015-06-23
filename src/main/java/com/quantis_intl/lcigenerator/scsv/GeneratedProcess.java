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
import com.quantis_intl.commons.scsv.processes.Product;
import com.quantis_intl.commons.scsv.processes.ProductUsage;
import com.quantis_intl.commons.scsv.processes.ScsvProcess.ProductScsvProcess;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.CategoryType;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.Status;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.Type;
import com.quantis_intl.commons.scsv.processes.SubstanceUsage;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;

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
        return Optional.of(generateProcessAndProductName());
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
        if (modelOutputs.containsKey("record_entry_by"))
            return Optional.of("Data entry by: " + modelOutputs.get("record_entry_by"));
        else
            return Optional.empty();
    }

    @Override
    public Optional<String> getGenerator()
    {
        // TODO: Change
        return Optional.of("Generator/publicator: Quantis LCI Generator");
    }

    @Override
    public Optional<String> getCollectionMethod()
    {
        if (modelOutputs.containsKey("collection_method"))
            return Optional.of("Sampling procedure: " + modelOutputs.get("collection_method"));
        else
            return Optional.empty();
    }

    @Override
    public Optional<String> getDataTreatment()
    {
        StringBuilder sb = new StringBuilder();
        if (modelOutputs.containsKey("data_treatment_extrapolations"))
            sb.append("Extrapolations: ").append(modelOutputs.get("data_treatment_extrapolations"));
        if (modelOutputs.containsKey("data_treatment_uncertainty"))
        {
            if (sb.length() > 0)
                sb.append(" ,");
            sb.append("Uncertainty adjustments: ").append(modelOutputs.get("data_treatment_uncertainty"));
        }

        return Optional.of(sb.toString());
    }

    @Override
    public Optional<String> getComment()
    {
        // TODO
        return Optional.ofNullable(modelOutputs.get("comment"));
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
                return generateProcessAndProductName() + " U";
            }

            @Override
            public String getUnit()
            {
                return "kg";
            }

            @Override
            public String getAmount()
            {
                return modelOutputs.get("yield_main_product_per_crop_cycle");
            }

            @Override
            public String getCategory()
            {
                return "_ALCIG import";
            }

            @Override
            public String getComment()
            {
                return "";
            }
        });
    }

    private String generateProcessAndProductName()
    {
        StringBuilder sb = new StringBuilder(PropertiesLoader.CROPS.getProperty(modelOutputs.get("crop")));
        // FIXME: No system_boundary input anymore, use ecospold name metadata instead?
        /*if (modelOutputs.containsKey("system_boundary"))
            sb.append(", ").append(modelOutputs.get("system_boundary"));*/
        sb.append("/kg");
        sb.append("/").append(modelOutputs.get("country"));
        return sb.toString();
    }

    @Override
    public List<SubstanceUsage> getResources()
    {
        return toSubstanceUsage(TemplateSubstanceUsage.resources);
    }

    @Override
    public List<ProductUsage> getMaterialsFuels()
    {
        List<ProductUsage> res = toProductUsage(TemplateProductUsage.materialsFuels);
        res.addAll(buildPesticidesProductUsages());
        return res;
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
        List<SubstanceUsage> res = toSubstanceUsage(TemplateSubstanceUsage.toSoil);
        res.addAll(buildPesticidesSubstanceUsages());
        return res;
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
                .filter(s -> !s.getAmount().equals("0.0") && !s.getAmount().equals("0")) // FIXME: Re-add
                .collect(Collectors.toList());
    }

    // TODO: map modelOutputs to template then sort by category, instead of this?
    private List<ProductUsage> toProductUsage(TemplateProductUsage[] templates)
    {
        return Arrays.stream(templates).map(r -> new GeneratedProductUsage(r, modelOutputs))
                .filter(s -> !s.getAmount().equals("0.0") && !s.getAmount().equals("0")) // FIXME: Re-add
                .collect(Collectors.toList());
    }

    private List<SubstanceUsage> buildPesticidesSubstanceUsages()
    {
        return modelOutputs.entrySet().stream().filter(e -> e.getKey().startsWith("pesti_"))
                .filter(e -> !e.getKey().endsWith("_other"))
                .filter(e -> !e.getKey().endsWith("_unspecified"))
                .map(e -> new PesticideSubstanceUsage(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    private List<ProductUsage> buildPesticidesProductUsages()
    {
        List<ProductUsage> res = modelOutputs.entrySet().stream().filter(e -> e.getKey().startsWith("pesti_"))
                .map(e -> new PesticideProductUsage(e.getKey(), e.getValue())).collect(Collectors.toList());
        res.addAll(modelOutputs.entrySet().stream().filter(e -> e.getKey().startsWith("pesti_"))
                .filter(e -> e.getKey().endsWith("_other") || e.getKey().endsWith("_unspecified"))
                .map(e -> new PesticideEmissions(e.getKey(), e.getValue())).collect(Collectors.toList()));
        return res;
    }
}
