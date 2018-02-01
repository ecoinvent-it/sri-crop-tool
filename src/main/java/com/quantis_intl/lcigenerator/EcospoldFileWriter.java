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
package com.quantis_intl.lcigenerator;

import java.io.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.xml.bind.JAXB;

import com.google.common.base.Charsets;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.quantis_intl.commons.ecospold2.ecospold02.*;
import com.quantis_intl.lcigenerator.ecospold.*;
import com.quantis_intl.lcigenerator.imports.Origin;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;
import com.quantis_intl.lcigenerator.imports.SingleValue;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.scsv.OutputTarget;

public class EcospoldFileWriter
{
    private static final TMacroEconomicScenario MACRO_ECONOMIC_SCENARIO;

    private static final UUID ECOINVENT_SYSTEM_ID = UUID.fromString("8b738ea0-f89e-4627-8679-433616064e82");
    private static final TDataEntryBy NOBODY;
    private static final TDataGeneratorAndPublication NOGENERATOR;
    private static final TModellingAndValidation MODELLING_AND_VALIDATION;
    private static final TFileAttributes FILE_ATTRIBUTES;
    private static final Map<String, String> PESTICIDE_PRODUCT_MAPPING =
            Maps.fromProperties(PropertiesLoader.loadProperties("/ecospold_pesticides_product_mapping.properties"));
    private static final Map<String, String> PESTICIDE_SUBS_MAPPING =
            Maps.fromProperties(PropertiesLoader.loadProperties("/ecospold_pesticides_substance_mapping.properties"));
    private static final String ORGANIC_SUFFIX = ", organic";

    static
    {
        MACRO_ECONOMIC_SCENARIO = new TMacroEconomicScenario();
        MACRO_ECONOMIC_SCENARIO.setMacroEconomicScenarioId(UUID.fromString("d9f57f0a-a01f-42eb-a57b-8f18d6635801"));
        MACRO_ECONOMIC_SCENARIO.setName(TString80.ofEn("Business-as-Usual"));

        NOBODY = new TDataEntryBy();
        NOBODY.setPersonId(UUID.fromString("788d0176-a69c-4de0-a5d3-259866b6b100"));
        NOBODY.setPersonName("[Current User]");
        NOBODY.setPersonEmail("no@email.com");

        NOGENERATOR = new TDataGeneratorAndPublication();
        NOGENERATOR.setPersonId(UUID.fromString("788d0176-a69c-4de0-a5d3-259866b6b100"));
        NOGENERATOR.setPersonName("[Current User]");
        NOGENERATOR.setPersonEmail("no@email.com");
        NOGENERATOR.setIsCopyrightProtected(true);
        NOGENERATOR.setAccessRestrictedTo(1);

        TRepresentativeness representativeness = new TRepresentativeness();
        representativeness.setSystemModelId(ECOINVENT_SYSTEM_ID);
        representativeness.setSystemModelName(TString120.ofEn("Undefined"));
        representativeness.setSamplingProcedure(TString32000.ofEn("Literature data and manufacturer information"));
        representativeness.setExtrapolations(TString32000.ofEn("none"));
        MODELLING_AND_VALIDATION = new TModellingAndValidation();
        MODELLING_AND_VALIDATION.setRepresentativeness(representativeness);

        FILE_ATTRIBUTES = new TFileAttributes();
        FILE_ATTRIBUTES.setMajorRelease(3);
        FILE_ATTRIBUTES.setMinorRelease(4);
        FILE_ATTRIBUTES.setMajorRevision(BigInteger.ONE);
        FILE_ATTRIBUTES.setMinorRevision(BigInteger.ONE);
        FILE_ATTRIBUTES.setContextId(UUID.fromString("de659012-50c4-4e96-b54a-fc781bf987ab"));
        FILE_ATTRIBUTES.setContextName(TString80.ofEn("ecoinvent"));
    }

    private final GeographyMappingCache geographyMappingCache;
    private final PossibleActivityNamesCache possibleActivityNames;
    private final PossibleIntermediateExchangesCache possibleExchanges;
    private final PossibleElementaryExchangesCache possibleSubstances;
    private final PossiblePropertyCache possibleProperties;
    private final PossibleActivityLinkCache possibleActivityLinks;
    private final PossibleParametersCache possibleParameters;
    private final CropsEcospoldRefsCache cropsEcospoldRefsCache;

    @Inject
    public EcospoldFileWriter(GeographyMappingCache geographyMappingCache,
                              PossibleActivityNamesCache possibleActivityNames,
                              PossibleIntermediateExchangesCache possibleExchanges,
                              PossibleElementaryExchangesCache possibleSubstances,
                              PossiblePropertyCache possibleProperties,
                              PossibleActivityLinkCache possibleActivityLinks,
                              PossibleParametersCache possibleParameters,
                              CropsEcospoldRefsCache cropsEcospoldRefsCache)
    {
        this.geographyMappingCache = geographyMappingCache;
        this.possibleActivityNames = possibleActivityNames;
        this.possibleExchanges = possibleExchanges;
        this.possibleSubstances = possibleSubstances;
        this.possibleProperties = possibleProperties;
        this.possibleActivityLinks = possibleActivityLinks;
        this.possibleParameters = possibleParameters;
        this.cropsEcospoldRefsCache = cropsEcospoldRefsCache;
    }

    public void writeModelsOutputToEcospoldFile(Map<String, String> modelsOutput, ValueGroup extractedInputs,
                                                OutputTarget outputTarget, OutputStream os)
    {
        try
        {
            writeModelsOutputToEcospoldFile(modelsOutput, extractedInputs, outputTarget,
                                            new BufferedWriter(
                                                    new OutputStreamWriter(os, Charsets.UTF_8)));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private void writeModelsOutputToEcospoldFile(Map<String, String> modelsOutput, ValueGroup extractedInputs,
                                                 OutputTarget outputTarget, Writer writer)
            throws IOException
    {
        double dividingValue = Double.parseDouble(modelsOutput.get("yield_main_product_per_crop_cycle"));

        EcoSpold res = new EcoSpold();
        UsedUserMasterData md = new UsedUserMasterData();
        res.setUsedUserMasterData(md);
        TActivityDataset dataset = new TActivityDataset();
        res.setActivityDataset(dataset);
        TActivityDescription desc = new TActivityDescription();
        dataset.setActivityDescription(desc);
        desc.setGeography(geographyMappingCache.getGeography(modelsOutput.get("country")));
        desc.setTechnology(new TTechnology());
        desc.setMacroEconomicScenario(MACRO_ECONOMIC_SCENARIO);
        TTimePeriod timePeriod = new TTimePeriod();
        int year = Year.now(ZoneOffset.UTC).getValue();
        timePeriod.setStartDate(LocalDate.of(year - 3, 1, 1));
        timePeriod.setEndDate(LocalDate.of(year, 12, 31));
        timePeriod.setIsDataValidForEntirePeriod(true);
        desc.setTimePeriod(timePeriod);
        TActivity activity = new TActivity();
        desc.setActivity(activity);
        String activityName = cropsEcospoldRefsCache.activityNameOfCrop(modelsOutput.get("crop"));

        //FIXME: add suffixes on activityName depending on greenhouse and stuff?
        if (isOrganic(extractedInputs))
            activityName += ORGANIC_SUFFIX;

        activity.setId(UUIDType5.nameUUIDFromNamespaceAndString(
                UUIDType5.NAMESPACE_DNS,
                activityName + modelsOutput.get("country")
                        + timePeriod.getStartDate().getYear() + "-01-01"
                        + timePeriod.getEndDate().getYear() + "-12-31"));
        activity.setActivityName(TString120.ofEn(activityName));
        UUID activityNameId = possibleActivityNames.getActivityUUID(activityName);
        if (activityNameId != null)
            activity.setActivityNameId(activityNameId);
        else
        {
            activity.setActivityNameId(UUIDType5.nameUUIDFromNamespaceAndString(UUIDType5.NAMESPACE_DNS, activityName));
            md.setActivityName(ImmutableList.of(TValidActivityName.ofEn(activity.getActivityNameId(), activityName)));
        }
        md.setActivityIndexEntry(ImmutableList.of(
                TValidActivityIndexEntry
                        .of(activity.getId(), activity.getActivityNameId(), desc.getGeography().getGeographyId(),
                            ECOINVENT_SYSTEM_ID, timePeriod.getStartDate(), timePeriod.getEndDate())));
        activity.setIncludedActivitiesEnd(TString32000.ofEn(""));
        //Mettre le Yield + le fait que c'est généré par ALCIG + somme N P K en kg/ha
        activity.setGeneralComment(TTextAndImage.ofEnTexts("Yield (kg): " + dividingValue, "Generated by ?"));
        activity.setType(1);
        //activity.tags?
        //desc.classification?

        TFlowData flowData = new TFlowData();
        dataset.setFlowData(flowData);

        String outputName = cropsEcospoldRefsCache.exchangeNameOfCrop(modelsOutput.get("crop"));
        if (isOrganic(extractedInputs))
            outputName += ORGANIC_SUFFIX;

        TValidIntermediateExchange tex = possibleExchanges.getExchange(outputName);


        TIntermediateExchange refOutput = new TIntermediateExchange();
        refOutput.setId(UUID.randomUUID());
        refOutput.setUnitId(AvailableUnit.KG.uuid);
        refOutput.setAmount(1.d);
        refOutput.setMathematicalRelation("");
        refOutput.setUnitName(TString40.ofEn(AvailableUnit.KG.symbol));
        refOutput.setComment(TString32000.ofEn(""));
        refOutput.setOutputGroup((short) 0);
        flowData.getIntermediateExchange().add(refOutput);
        if (tex != null)
        {
            refOutput.setIntermediateExchangeId(tex.getId());
            refOutput.setName(tex.getName());
            refOutput.getClassification().addAll(tex.getClassification());
        }
        else
        {
            refOutput.setIntermediateExchangeId(UUIDType5.nameUUIDFromNamespaceAndString(
                    UUIDType5.NAMESPACE_DNS, outputName));
            refOutput.setName(TString120.ofEn(outputName));
            md.getIntermediateExchange().add(TValidIntermediateExchange.ofEn(refOutput.getIntermediateExchangeId(),
                                                                             outputName,
                                                                             AvailableUnit.KG.uuid,
                                                                             AvailableUnit.KG.symbol));
        }

        String label = findLabel(extractedInputs, "yield_BP1_per_crop_cycle");
        if (!Strings.isNullOrEmpty(label) && !Strings.isNullOrEmpty(modelsOutput.get("yield_BP1_per_crop_cycle")))
            flowData.getIntermediateExchange()
                    .add(generateCoProduct(modelsOutput, extractedInputs, label, "yield_BP1_per_crop_cycle",
                                           dividingValue, md));

        label = findLabel(extractedInputs, "yield_BP2_per_crop_cycle");
        if (!Strings.isNullOrEmpty(label) && !Strings.isNullOrEmpty(modelsOutput.get("yield_BP2_per_crop_cycle")))
            flowData.getIntermediateExchange()
                    .add(generateCoProduct(modelsOutput, extractedInputs, label, "yield_BP2_per_crop_cycle",
                                           dividingValue, md));

        label = findLabel(extractedInputs, "yield_BP3_per_crop_cycle");
        if (!Strings.isNullOrEmpty(label) && !Strings.isNullOrEmpty(modelsOutput.get("yield_BP3_per_crop_cycle")))
            flowData.getIntermediateExchange()
                    .add(generateCoProduct(modelsOutput, extractedInputs, label, "yield_BP3_per_crop_cycle",
                                           dividingValue, md));


        EcospoldTemplateIntermediaryExchanges usages = new EcospoldTemplateIntermediaryExchanges();
        flowData.getIntermediateExchange().addAll(
                Stream.concat(Stream.concat(
                        Arrays.stream(usages.getMaterialsFuels())
                              .map(tu -> buildIntermediateExchange(modelsOutput, extractedInputs, tu, dividingValue)),
                        Arrays.stream(usages.getElectricityHeat())
                              .map(tu -> buildIntermediateExchange(modelsOutput, extractedInputs, tu, dividingValue))),
                              Arrays.stream(usages.getWastes())
                                    .map(tu -> buildIntermediateExchange(modelsOutput, extractedInputs, tu,
                                                                         dividingValue))
                                    .filter(Predicates.notNull())
                                    .map(tu -> {
                                        tu.setInputGroup(null);
                                        tu.setOutputGroup((short) 3);
                                        return tu;
                                    }))
                      .filter(Predicates.notNull())
                      .collect(Collectors.toList()));

        EcospoldTemplateSubstanceUsages subUsages = new EcospoldTemplateSubstanceUsages();
        flowData.getElementaryExchange().addAll(
                Stream.concat(Stream.concat(Stream.concat(
                        Arrays.stream(subUsages.getResources())
                              .map(su -> buildElementaryExchange(modelsOutput, extractedInputs, su, dividingValue)),
                        Arrays.stream(subUsages.getToWater())
                              .map(su -> buildElementaryExchange(modelsOutput, extractedInputs, su, dividingValue))),
                                            Arrays.stream(subUsages.getToAir())
                                                  .map(su -> buildElementaryExchange(modelsOutput, extractedInputs,
                                                                                     su, dividingValue))),
                              Arrays.stream(subUsages.getToSoil())
                                    .map(su -> buildElementaryExchange(modelsOutput, extractedInputs, su,
                                                                       dividingValue)))
                      .filter(Predicates.notNull())
                      .collect(Collectors.toList()));

        modelsOutput.entrySet().stream().filter(e -> e.getKey().startsWith("pestikg_"))
                    .forEach(e ->
                             {
                                 TIntermediateExchange ex = buildPesticideExchange(e.getKey(), e.getValue(),
                                                                                   extractedInputs, dividingValue);
                                 if (ex != null)
                                     flowData.getIntermediateExchange().add(ex);

                                 if (!e.getKey().endsWith("_other") && !e.getKey().endsWith("_unspecified"))
                                 {
                                     TElementaryExchange ee = buildPesticideSubstance(e.getKey(), e.getValue(),
                                                                                      extractedInputs, dividingValue);
                                     if (ex != null)
                                         flowData.getElementaryExchange().add(ee);
                                 }
                             }
                            );

        squashIntermediateExchanges(flowData.getIntermediateExchange());
        squashElementaryExchanges(flowData.getElementaryExchange());

        flowData.getParameter().add(buildLucParameter(modelsOutput));

        dataset.setModellingAndValidation(MODELLING_AND_VALIDATION);
        TAdministrativeInformation adminInfo = new TAdministrativeInformation();
        dataset.setAdministrativeInformation(adminInfo);
        adminInfo.setDataEntryBy(NOBODY);
        adminInfo.setDataGeneratorAndPublication(NOGENERATOR);
        adminInfo.setFileAttributes(FILE_ATTRIBUTES);

        /*Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<TActivityDataset>> violations = validator.validate(dataset);
        violations.forEach(System.out::println);*/

        JAXB.marshal(res, writer);
        writer.flush();
    }

    private String findLabel(ValueGroup extractedInputs, String key)
    {
        SingleValue<?> val = extractedInputs.getSingleValue(key);
        if (val == null)
            return "";
        String label = ((Origin.ExcelUserInput) val.getOrigin()).label;
        if (label.contains("<select"))
            return "";
        return label;
    }

    private TIntermediateExchange generateCoProduct(Map<String, String> modelsOutput, ValueGroup extractedInputs,
                                                    String label, String key, double dividingValue,
                                                    UsedUserMasterData md)
    {
        TValidIntermediateExchange tex = possibleExchanges.getExchange(label);

        TIntermediateExchange refOutput = new TIntermediateExchange();
        refOutput.setId(UUID.randomUUID());
        refOutput.setUnitId(AvailableUnit.KG.uuid);
        refOutput.setAmount(Double.parseDouble(modelsOutput.get(key)) / dividingValue);
        refOutput.setMathematicalRelation("");

        if (isOrganic(extractedInputs))
            label += ORGANIC_SUFFIX;
        refOutput.setName(TString120.ofEn(label));

        refOutput.setUnitName(TString40.ofEn(AvailableUnit.KG.symbol));
        refOutput.setComment(TString32000.ofEn(findComment(extractedInputs, key)));
        refOutput.setOutputGroup((short) 2);

        if (tex != null)
        {
            refOutput.setIntermediateExchangeId(tex.getId());
            refOutput.setName(tex.getName());
            refOutput.getClassification().addAll(tex.getClassification());
        }
        else
        {
            refOutput.setIntermediateExchangeId(UUIDType5.nameUUIDFromNamespaceAndString(
                    UUIDType5.NAMESPACE_DNS, label));
            refOutput.setName(TString120.ofEn(label));
            md.getIntermediateExchange().add(TValidIntermediateExchange.ofEn(refOutput.getIntermediateExchangeId(),
                                                                             label,
                                                                             AvailableUnit.KG.uuid,
                                                                             AvailableUnit.KG.symbol));
        }

        return refOutput;
    }

    private String findComment(ValueGroup extractedInputs, String key)
    {
        SingleValue<?> sv = extractedInputs.getDeepSingleValue(key);
        return sv == null ? "" : Strings.nullToEmpty(sv.getComment());
    }

    private TIntermediateExchange buildIntermediateExchange(
            Map<String, String> modelsOutput, ValueGroup extractedInputs, TemplateIntermediaryExchanges
            .TemplateIntermediaryExchange tu,
            double dividingValue)
    {
        TIntermediateExchange ex = new TIntermediateExchange();
        ex.setId(UUID.randomUUID());
        ex.setUnitId(tu.unit.uuid);
        if (tu instanceof EcospoldTemplateIntermediaryExchanges.LucTemplateIntermediaryExchange)
        {
            ex.setAmount(0.d);
            ex.setIsCalculatedAmount(true);
            ex.setMathematicalRelation(((EcospoldTemplateIntermediaryExchanges.LucTemplateIntermediaryExchange) tu)
                                               .provideMathematicalRelation(modelsOutput));
        }
        else
        {
            ex.setAmount(Double.parseDouble(tu.provideValue(modelsOutput)) / dividingValue);
            if (ex.getAmount() == 0.d)
                return null;
            ex.setMathematicalRelation("");
        }

        TValidIntermediateExchange tex = possibleExchanges.getExchange(tu.provideName(modelsOutput));
        if (tex == null)
        {
            System.out.println("ERROR: exchange not found: " + tu.provideName(modelsOutput));
            return null;
        }
        ex.setIntermediateExchangeId(tex.getId());
        ex.setName(tex.getName());
        if (!tex.getUnitName().getValue().equals(tu.unit.symbol))
            System.out.println("ERROR: unit is not the same for " + tex.getName().getValue() + ". " +
                                       tex.getUnitName().getValue() + ". " + tu.unit.symbol);
        ex.setUnitName(tex.getUnitName());
        ex.setComment(TString32000.ofEn(findComment(extractedInputs, tu.commentVariable)));
        ex.getClassification().addAll(tex.getClassification());
        ex.getProperty().addAll(tex.getProperty());
        ex.getProperty().forEach(p -> {
            TValidProperty vp = possibleProperties.getProperty(p.getPropertyId());
            p.setName(vp.getName());
            p.setUnitId(vp.getUnitId());
            p.setUnitName(vp.getUnitName());
        });
        ex.setInputGroup((short) 5);

        if (tu instanceof EcospoldTemplateIntermediaryExchanges.WithActivityIdTemplateIntermediaryExchange)
        {
            ex.setActivityLinkId(possibleActivityLinks.getActivityLink(
                    ((EcospoldTemplateIntermediaryExchanges.WithActivityIdTemplateIntermediaryExchange)
                            tu).activityNameId, modelsOutput.get("country")));
        }

        return ex;
    }

    private TIntermediateExchange buildPesticideExchange(String key, String value, ValueGroup extractedInputs,
                                                         double dividingValue)
    {
        key = key.replaceFirst("pestikg_", "pesti_");
        TIntermediateExchange ex = new TIntermediateExchange();
        ex.setId(UUID.randomUUID());
        ex.setUnitId(AvailableUnit.KG.uuid);
        ex.setAmount(Double.parseDouble(value) / dividingValue);
        if (ex.getAmount() == 0.d)
            return null;
        ex.setMathematicalRelation("");
        TValidIntermediateExchange tex = possibleExchanges.getExchange(PESTICIDE_PRODUCT_MAPPING.get(key));
        if (tex == null)
        {
            System.out.println(
                    "ERROR: exchange not found for pesticide " + key + ": " + PESTICIDE_PRODUCT_MAPPING.get(key));
            return null;
        }
        ex.setIntermediateExchangeId(tex.getId());
        ex.setName(tex.getName());
        ex.setUnitName(tex.getUnitName());
        ex.setComment(TString32000.ofEn(findComment(extractedInputs, key.substring(6))));
        ex.getClassification().addAll(tex.getClassification());
        ex.getProperty().addAll(tex.getProperty());
        ex.getProperty().forEach(p -> {
            TValidProperty vp = possibleProperties.getProperty(p.getPropertyId());
            p.setName(vp.getName());
            p.setUnitId(vp.getUnitId());
            p.setUnitName(vp.getUnitName());
        });
        ex.setInputGroup((short) 5);

        return ex;
    }

    private TElementaryExchange buildPesticideSubstance(String key, String value, ValueGroup extractedInputs,
                                                        double dividingValue)
    {
        key = key.replaceFirst("pestikg_", "pesti_");
        TElementaryExchange ex = new TElementaryExchange();
        ex.setId(UUID.randomUUID());
        ex.setUnitId(AvailableUnit.KG.uuid);
        ex.setAmount(Double.parseDouble(value) / dividingValue);
        if (ex.getAmount() == 0.d)
            return null;
        ex.setMathematicalRelation("");
        TValidElementaryExchange tex =
                possibleSubstances.getExchange(UUID.fromString("e1bc9a16-5b6a-494f-98ef-49f461b1a11e"),
                                               PESTICIDE_SUBS_MAPPING.get(key));
        if (tex == null)
        {
            System.out.println("ERROR: substance not found: " + PESTICIDE_SUBS_MAPPING.get(key));
            return null;
        }
        ex.setElementaryExchangeId(tex.getId());
        ex.setName(tex.getName());
        ex.setCompartment(tex.getCompartment());
        ex.setUnitName(tex.getUnitName());
        ex.setComment(TString32000.ofEn(findComment(extractedInputs, key.substring(6))));
        ex.getProperty().addAll(tex.getProperty());
        ex.getProperty().forEach(p -> {
            TValidProperty vp = possibleProperties.getProperty(p.getPropertyId());
            p.setName(vp.getName());
            p.setUnitId(vp.getUnitId());
            p.setUnitName(vp.getUnitName());
        });
        ex.setOutputGroup((short) 4);
        return ex;
    }

    private TElementaryExchange buildElementaryExchange(
            Map<String, String> modelsOutput, ValueGroup extractedInputs, TemplateSubstanceUsages
            .TemplateSubstanceUsage su, double dividingValue)
    {
        TElementaryExchange ex = new TElementaryExchange();
        ex.setId(UUID.randomUUID());
        ex.setUnitId(su.unit.uuid);
        String amount = modelsOutput.get(su.amountVariable);
        if (amount == null)
            return null;
        ex.setAmount(Double.parseDouble(amount) / dividingValue);
        if (ex.getAmount() == 0.d)
            return null;
        ex.setMathematicalRelation("");
        TValidElementaryExchange tex = possibleSubstances.getExchange(su.subCompartment, su.name);
        if (tex == null)
        {
            System.out.println("ERROR: substance not found: " + su.name);
            return null;
        }
        ex.setElementaryExchangeId(tex.getId());
        ex.setName(tex.getName());
        ex.setCompartment(tex.getCompartment());
        if (!tex.getUnitName().getValue().equals(su.unit.symbol))
            System.out.println("ERROR: unit is not the same for " + tex.getName().getValue() + ". " +
                                       tex.getUnitName().getValue() + ". " + su.unit.symbol);
        ex.setUnitName(tex.getUnitName());
        ex.setComment(TString32000.ofEn(findComment(extractedInputs, su.commentVariable)));
        ex.getProperty().addAll(tex.getProperty());
        ex.getProperty().forEach(p -> {
            TValidProperty vp = possibleProperties.getProperty(p.getPropertyId());
            p.setName(vp.getName());
            p.setUnitId(vp.getUnitId());
            p.setUnitName(vp.getUnitName());
        });
        if (tex.getCompartment().getCompartment().getValue().equals("natural resource"))
            ex.setInputGroup((short) 4);
        else
            ex.setOutputGroup((short) 4);
        return ex;
    }

    private void squashIntermediateExchanges(List<TIntermediateExchange> exchanges)
    {
        Set<UUID> alreadyMergedIds = new HashSet<>();
        Map<UUID, TIntermediateExchange> existingExchanges = new HashMap<>();
        Iterator<TIntermediateExchange> it = exchanges.iterator();
        while (it.hasNext())
        {
            TIntermediateExchange ex = it.next();
            TIntermediateExchange other;
            if (ex.getActivityLinkId() == null)
            {
                if (null != (other = existingExchanges.putIfAbsent(ex.getIntermediateExchangeId(), ex)))
                {
                    if (!Objects.equals(ex.getInputGroup(), other.getInputGroup()) ||
                            !Objects.equals(ex.getOutputGroup(), other.getOutputGroup()))
                    {
                        existingExchanges.put(ex.getIntermediateExchangeId(), ex);
                        alreadyMergedIds.remove(ex.getIntermediateExchangeId());
                    }
                    else
                    {
                        if (alreadyMergedIds.add(ex.getIntermediateExchangeId()))
                            other.setComment(TString32000.ofEn(
                                    "This exchange is an aggregation of amounts: \n" + other.getAmount() + ". " +
                                            "Comment: " +
                                            other.getComment().getValue()));

                        other.setComment(
                                TString32000.ofEn(other.getComment().getValue() + "\n" + ex.getAmount() + ". " +
                                                          "Comment: " + ex.getComment().getValue()));
                        other.setAmount(other.getAmount() + ex.getAmount());
                        it.remove();
                    }
                }
            }
        }
    }

    private boolean isOrganic(ValueGroup extractedInputs)
    {
        return "yes".equals(extractedInputs.getDeepSingleValue("organic_certified").getValue());
    }

    private void squashElementaryExchanges(List<TElementaryExchange> exchanges)
    {
        Set<UUID> alreadyMergedIds = new HashSet<>();
        Map<UUID, TElementaryExchange> existingExchanges = new HashMap<>();
        Iterator<TElementaryExchange> it = exchanges.iterator();
        while (it.hasNext())
        {
            TElementaryExchange ex = it.next();
            TElementaryExchange other;
            if (null != (other = existingExchanges.putIfAbsent(ex.getElementaryExchangeId(), ex)))
            {
                if (alreadyMergedIds.add(ex.getElementaryExchangeId()))
                    other.setComment(TString32000.ofEn(
                            "This exchange is an aggregation of amounts: \n" + other.getAmount() + ". " + "Comment: " +
                                    other.getComment().getValue()));

                other.setComment(TString32000.ofEn(other.getComment().getValue() + "\n" + ex.getAmount() + ". " +
                                                           "Comment: " + ex.getComment().getValue()));
                other.setAmount(other.getAmount() + ex.getAmount());
                it.remove();
            }
        }
    }

    private TParameter buildLucParameter(Map<String, String> modelsOutput)
    {
        TValidParameter validParameter = possibleParameters.getParameter("LUC_crop_specific");
        TParameter p = new TParameter();
        p.setParameterId(validParameter.getId());
        p.setVariableName(validParameter.getDefaultVariableName());
        p.setAmount(1.0);
        p.setName(validParameter.getName());
        p.setComment(TString32000.ofEn("This parameter allows to switch between a \"crop specific\" and a \"country " +
                                               "specific\" approach when calculating the LUC of a crop. By default, " +
                                               "the chosen approach is \"crop specific\", so the value of the " +
                                               "parameter is 1. If you want to apply a \"country specific\" approach," +
                                               " turn the parameter to 0."));
        return p;
    }

}
