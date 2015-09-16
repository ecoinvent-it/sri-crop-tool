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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.quantis_intl.commons.scsv.processes.Product;
import com.quantis_intl.commons.scsv.processes.ProductUsage;
import com.quantis_intl.commons.scsv.processes.ScsvProcess.ProductScsvProcess;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.CategoryType;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.Status;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.Type;
import com.quantis_intl.commons.scsv.processes.SubstanceUsage;
import com.quantis_intl.commons.scsv.processes.WasteTreatmentUsage;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;
import com.quantis_intl.lcigenerator.imports.SingleValue;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.scsv.TemplateProductUsages.TemplateProductUsage;
import com.quantis_intl.lcigenerator.scsv.TemplateSubstanceUsages.TemplateSubstanceUsage;

public class GeneratedProcess implements ProductScsvProcess
{
    private final Map<String, String> modelOutputs;
    private final ValueGroup extractedInputs;
    // TODO: Would be good to split deps requirements and process generation
    private final TemplateProductUsages templateProductUsages;
    private final TemplateSubstanceUsages templateSubstanceUsages;
    private final Set<String> requiredAlcigProcesses;
    private final OutputTarget outputTarget;

    public GeneratedProcess(Map<String, String> modelOutputs, ValueGroup extractedInputs, OutputTarget outputTarget)
    {
        this.modelOutputs = modelOutputs;
        this.extractedInputs = extractedInputs;
        this.templateProductUsages = outputTarget.templateProductUsages;
        this.templateSubstanceUsages = outputTarget.templateSubstanceUsages;
        this.outputTarget = outputTarget;
        this.requiredAlcigProcesses = new HashSet<>();
    }

    public Set<String> getRequiredAlcigProcesses()
    {
        return requiredAlcigProcesses;
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
        return Optional.of("Generator/publicator: Quantis ALCIG");
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
                sb.append(", ");
            sb.append("Uncertainty adjustments: ").append(modelOutputs.get("data_treatment_uncertainty"));
        }

        return Optional.of(sb.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<String> getComment()
    {
        StringBuilder resBuilder = new StringBuilder();

        SingleValue<Double> sTeR = (SingleValue<Double>) extractedInputs
                .getSingleValue("data_quality_techno_representativeness");
        SingleValue<Double> sGR = (SingleValue<Double>) extractedInputs.getSingleValue(
                "data_quality_geo_representativeness");
        SingleValue<Double> sTiR = (SingleValue<Double>) extractedInputs.getSingleValue(
                "data_quality_time_related_representativeness");
        SingleValue<Double> sP = (SingleValue<Double>) extractedInputs.getSingleValue(
                "data_quality_precision_uncertainty");
        SingleValue<Double> sC = (SingleValue<Double>) extractedInputs.getSingleValue("data_quality_completeness");
        SingleValue<Double> sM = (SingleValue<Double>) extractedInputs.getSingleValue(
                "data_quality_methodo_appropriateness_consistency");

        if (sTeR != null || sGR != null || sTiR != null || sP != null || sC != null || sM != null)
        {
            resBuilder.append("TeR: ").append(sTeR == null ? "-" : sTeR.getValue());
            resBuilder.append("\nGR: ").append(sGR == null ? "-" : sGR.getValue());
            resBuilder.append("\nTiR: ").append(sTiR == null ? "-" : sTiR.getValue());
            resBuilder.append("\nP: ").append(sP == null ? "-" : sP.getValue());
            resBuilder.append("\nC: ").append(sC == null ? "-" : sC.getValue());
            resBuilder.append("\nM: ").append(sM == null ? "-" : sM.getValue());
            resBuilder.append("\nDQR: ");
            try
            {
                resBuilder.append(Arrays.stream(new SingleValue[] { sTeR, sGR, sTiR, sP, sC, sM })
                        .mapToDouble(sv -> (Double) sv.getValue()).average().getAsDouble());
            }
            catch (NullPointerException e)
            {
                resBuilder.append("-");
            }

            resBuilder.append("\n\n");
        }

        resBuilder.append(Strings.nullToEmpty(modelOutputs.get("comment")));

        return Optional.ofNullable(Strings.emptyToNull(resBuilder.toString()));
    }

    @Override
    public List<Product> getProducts()
    {
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
                return findComment("yield_main_product_per_crop_cycle");
            }
        });
    }

    private String generateProcessAndProductName()
    {
        StringBuilder sb = new StringBuilder(PropertiesLoader.CROPS.getProperty(modelOutputs.get("crop")))
                .append(", at farm/kg/")
                .append(modelOutputs.get("country"));
        return sb.toString();
    }

    @Override
    public List<SubstanceUsage> getResources()
    {
        return toSubstanceUsage(templateSubstanceUsages.getResources());
    }

    @Override
    public List<ProductUsage> getMaterialsFuels()
    {
        List<ProductUsage> res = toProductUsage(templateProductUsages.getMaterialsFuels());
        res.addAll(buildPesticidesProductUsages());
        return res;
    }

    @Override
    public List<ProductUsage> getElectricityHeat()
    {
        return toProductUsage(templateProductUsages.getElectricityHeat());
    }

    @Override
    public List<SubstanceUsage> getEmissionsToAir()
    {
        return toSubstanceUsage(templateSubstanceUsages.getToAir());
    }

    @Override
    public List<SubstanceUsage> getEmissionsToWater()
    {
        return toSubstanceUsage(templateSubstanceUsages.getToWater());
    }

    @Override
    public List<SubstanceUsage> getEmissionsToSoil()
    {
        List<SubstanceUsage> res = toSubstanceUsage(templateSubstanceUsages.getToSoil());
        res.addAll(buildPesticidesSubstanceUsages());
        return res;
    }

    @Override
    public List<WasteTreatmentUsage> getWasteToTreatment()
    {
        return toWasteTreatmentUsage(templateProductUsages.getWastes());
    }

    // TODO: map modelOutputs to template then sort by category, instead of this?
    private List<SubstanceUsage> toSubstanceUsage(TemplateSubstanceUsage[] templates)
    {
        return Arrays.stream(templates).map(r -> new GeneratedSubstanceUsage(r, modelOutputs, extractedInputs))
                .filter(s -> !"0.0".equals(s.getAmount()) && !"0".equals(s.getAmount()))
                .collect(Collectors.toList());
    }

    // TODO: map modelOutputs to template then sort by category, instead of this?
    private List<ProductUsage> toProductUsage(TemplateProductUsage[] templates)
    {
        List<ProductUsage> res = Arrays.stream(templates)
                .map(r -> new GeneratedProductUsage(r, modelOutputs, extractedInputs))
                .filter(s -> !"0.0".equals(s.getAmount()) && !"0".equals(s.getAmount()))
                .collect(Collectors.toList());
        findRequiredDeps(res);
        return res;
    }

    // TODO: map modelOutputs to template then sort by category, instead of this?
    private List<WasteTreatmentUsage> toWasteTreatmentUsage(TemplateProductUsage[] templates)
    {
        List<WasteTreatmentUsage> res = Arrays.stream(templates)
                .map(r -> new GeneratedProductUsage(r, modelOutputs, extractedInputs))
                .filter(s -> !"0.0".equals(s.getAmount()) && !"0".equals(s.getAmount()))
                .collect(Collectors.toList());
        findWtRequiredDeps(res);
        return res;
    }

    private List<SubstanceUsage> buildPesticidesSubstanceUsages()
    {
        return modelOutputs.entrySet().stream().filter(e -> e.getKey().startsWith("pesti_"))
                .filter(e -> !e.getKey().endsWith("_other"))
                .filter(e -> !e.getKey().endsWith("_unspecified"))
                .map(e -> new PesticideSubstanceUsage(e.getKey(), e.getValue(), findComment(e.getKey().substring(6))))
                .collect(Collectors.toList());
    }

    private List<ProductUsage> buildPesticidesProductUsages()
    {
        List<ProductUsage> res = modelOutputs.entrySet().stream().filter(e -> e.getKey().startsWith("pesti_"))
                .map(e -> new PesticideProductUsage(e.getKey(), e.getValue(), findComment(e.getKey().substring(6))))
                .collect(Collectors.toList());
        List<ProductUsage> undefined = modelOutputs
                .entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("pesti_"))
                .filter(e -> e.getKey().endsWith("_other") || e.getKey().endsWith("_unspecified"))
                .map(e -> new PesticideEmissions(e.getKey(), e.getValue(), findComment(e.getKey().substring(6)),
                        outputTarget))
                .collect(Collectors.toList());
        res.addAll(undefined);
        // FIXME: Ugly
        if (outputTarget == OutputTarget.ECOINVENT)
        {
            for (ProductUsage u : undefined)
            {
                if (u.getName().contains("herbicides"))
                    requiredAlcigProcesses.add("herbicides_undefined_to_soil.csv");
                else if (u.getName().contains("fungicides"))
                    requiredAlcigProcesses.add("fungicides_undefined_to_soil.csv");
                else if (u.getName().contains("insecticides"))
                    requiredAlcigProcesses.add("insecticides_undefined_to_soil.csv");
            }
        }
        return res;
    }

    private String findComment(String key)
    {
        SingleValue<?> sv = extractedInputs.getDeepSingleValue(key);
        return sv == null ? "" : Strings.nullToEmpty(sv.getComment());
    }

    // FIXME: Not the best way to do that
    private void findRequiredDeps(List<ProductUsage> productUsages)
    {
        for (ProductUsage pu : productUsages)
        {
            GeneratedProductUsage gpu = (GeneratedProductUsage) pu;
            gpu.getRequiredDep().ifPresent(requiredAlcigProcesses::addAll);
        }
    }

    // FIXME: Not the best way to do that
    private void findWtRequiredDeps(List<WasteTreatmentUsage> wasteTreatmentUsages)
    {
        for (WasteTreatmentUsage wtu : wasteTreatmentUsages)
        {
            GeneratedProductUsage gpu = (GeneratedProductUsage) wtu;
            gpu.getRequiredDep().ifPresent(requiredAlcigProcesses::addAll);
        }
    }
}
