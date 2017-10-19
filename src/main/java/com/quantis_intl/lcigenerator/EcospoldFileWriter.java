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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXB;

import com.google.common.base.Charsets;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.quantis_intl.commons.ecospold2.ecospold02.*;
import com.quantis_intl.lcigenerator.ecospold.*;
import com.quantis_intl.lcigenerator.imports.Origin;
import com.quantis_intl.lcigenerator.imports.SingleValue;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.scsv.OutputTarget;

public class EcospoldFileWriter
{
    //FIXME: Validate
    private static final Map<String, TGeography> GEOGRAPHY_MAPPING;

    private static final TMacroEconomicScenario MACRO_ECONOMIC_SCENARIO;

    private static final TTimePeriod TIME_PERIOD;

    private static final TDataEntryBy NOBODY;
    private static final TDataGeneratorAndPublication NOGENERATOR;
    private static final TFileAttributes FILE_ATTRIBUTES;

    static
    {
        try
        {
            GEOGRAPHY_MAPPING = new HashMap<>(1024);
            for (String l : Resources
                    .readLines(EcospoldFileWriter.class.getResource("ecospold_geo_mapping.txt"), Charsets.UTF_8))
            {
                String[] split = l.split("=");
                GEOGRAPHY_MAPPING.put(split[1], TGeography.ofEn(split[0].substring(0, 36), split[0].substring(37)));
            }
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }

        MACRO_ECONOMIC_SCENARIO = new TMacroEconomicScenario();
        MACRO_ECONOMIC_SCENARIO.setMacroEconomicScenarioId(UUID.fromString("d9f57f0a-a01f-42eb-a57b-8f18d6635801"));
        MACRO_ECONOMIC_SCENARIO.setName(TString80.ofEn("Business-as-Usual"));

        TIME_PERIOD = new TTimePeriod();
        TIME_PERIOD.setStartDate(LocalDate.of(2014, 1, 1));
        TIME_PERIOD.setEndDate(LocalDate.of(2015, 12, 31));
        TIME_PERIOD.setIsDataValidForEntirePeriod(true);

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

        FILE_ATTRIBUTES = new TFileAttributes();
        FILE_ATTRIBUTES.setMajorRelease(3);
        FILE_ATTRIBUTES.setMinorRelease(4);
        FILE_ATTRIBUTES.setMajorRevision(BigInteger.ONE);
        FILE_ATTRIBUTES.setMinorRevision(BigInteger.ONE);
        FILE_ATTRIBUTES.setContextId(UUID.fromString("de659012-50c4-4e96-b54a-fc781bf987ab"));
        FILE_ATTRIBUTES.setContextName(TString80.ofEn("ecoinvent"));
    }

    private final PossibleIntermediateExchangesCache possibleExchanges;
    private final PossibleElementaryExchangesCache possibleSubstances;
    private final PossiblePropertyCache possibleProperties;

    @Inject
    public EcospoldFileWriter(PossibleIntermediateExchangesCache possibleExchanges,
                              PossibleElementaryExchangesCache possibleSubstances,
                              PossiblePropertyCache possibleProperties)
    {
        this.possibleExchanges = possibleExchanges;
        this.possibleSubstances = possibleSubstances;
        this.possibleProperties = possibleProperties;
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
        desc.setGeography(GEOGRAPHY_MAPPING.get(modelsOutput.get("country")));
        desc.setTechnology(new TTechnology());
        desc.setMacroEconomicScenario(MACRO_ECONOMIC_SCENARIO);
        desc.setTimePeriod(TIME_PERIOD);
        TActivity activity = new TActivity();
        desc.setActivity(activity);
        activity.setId(UUID.randomUUID());//TODO
        activity.setActivityName(TString120.ofEn("TODO"));
        md.setActivityName(ImmutableList.of(TValidActivityName.ofEn(activity.getId(), "TODO")));
        activity.setActivityNameId(UUID.randomUUID());//TODO
        activity.setIncludedActivitiesEnd(TString32000.ofEn("TODO"));
        //Mettre le Yield + le fait que c'est généré par ALCIG + somme N P K en kg/ha
        activity.setGeneralComment(TTextAndImage.ofEnTexts("TODO", "TODO"));
        activity.setType(1);
        //activity.tags?
        //desc.classification?

        TFlowData flowData = new TFlowData();
        dataset.setFlowData(flowData);

        TIntermediateExchange refOutput = new TIntermediateExchange();
        refOutput.setId(UUID.randomUUID()); //FIXME
        refOutput.setUnitId(AvailableUnit.KG.uuid);
        refOutput.setAmount(1.d);
        refOutput.setMathematicalRelation("");
        refOutput.setIntermediateExchangeId(UUID.randomUUID());//FIXME
        refOutput.setName(TString120.ofEn("TODO"));
        refOutput.setUnitName(TString40.ofEn(AvailableUnit.KG.symbol));
        refOutput.setComment(TString32000.ofEn("TODO"));
        //refOutput.getClassification().addAll(tex.getClassification()); FIXME
        refOutput.setOutputGroup((short) 0);
        flowData.getIntermediateExchange().add(refOutput);

        String label = findLabel(extractedInputs, "yield_BP1_per_crop_cycle");
        if (!Strings.isNullOrEmpty(label) && !Strings.isNullOrEmpty(modelsOutput.get("yield_BP1_per_crop_cycle")))
            flowData.getIntermediateExchange()
                    .add(generateCoProduct(modelsOutput, extractedInputs, "TODO", label, "yield_BP1_per_crop_cycle",
                                           dividingValue));

        label = findLabel(extractedInputs, "yield_BP2_per_crop_cycle");
        if (!Strings.isNullOrEmpty(label) && !Strings.isNullOrEmpty(modelsOutput.get("yield_BP2_per_crop_cycle")))
            flowData.getIntermediateExchange()
                    .add(generateCoProduct(modelsOutput, extractedInputs, "TODO", label, "yield_BP2_per_crop_cycle",
                                           dividingValue));

        label = findLabel(extractedInputs, "yield_BP3_per_crop_cycle");
        if (!Strings.isNullOrEmpty(label) && !Strings.isNullOrEmpty(modelsOutput.get("yield_BP3_per_crop_cycle")))
            flowData.getIntermediateExchange()
                    .add(generateCoProduct(modelsOutput, extractedInputs, "TODO", label, "yield_BP3_per_crop_cycle",
                                           dividingValue));


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

        TModellingAndValidation modAndValid = new TModellingAndValidation();
        dataset.setModellingAndValidation(modAndValid);
        TAdministrativeInformation adminInfo = new TAdministrativeInformation();
        dataset.setAdministrativeInformation(adminInfo);
        adminInfo.setDataEntryBy(NOBODY);
        adminInfo.setDataGeneratorAndPublication(NOGENERATOR);
        adminInfo.setFileAttributes(FILE_ATTRIBUTES);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<TActivityDataset>> violations = validator.validate(dataset);
        violations.forEach(System.out::println);

        JAXB.marshal(res, writer);
        writer.flush();
    }

    private String findLabel(ValueGroup extractedInputs, String key)
    {
        SingleValue<?> val = extractedInputs.getSingleValue(key);
        if (val == null)
            return "";
        return ((Origin.ExcelUserInput) val.getOrigin()).label;
    }

    private TIntermediateExchange generateCoProduct(Map<String, String> modelsOutput, ValueGroup extractedInputs,
                                                    String crop, String label, String key, double dividingValue)
    {
        TIntermediateExchange refOutput = new TIntermediateExchange();
        refOutput.setId(UUID.randomUUID()); //FIXME
        refOutput.setUnitId(AvailableUnit.KG.uuid);
        refOutput.setAmount(Double.parseDouble(modelsOutput.get(key)) / dividingValue);
        refOutput.setMathematicalRelation("");
        refOutput.setIntermediateExchangeId(UUID.randomUUID());//FIXME
        refOutput.setName(TString120.ofEn(crop + ", " + label));
        refOutput.setUnitName(TString40.ofEn(AvailableUnit.KG.symbol));
        refOutput.setComment(TString32000.ofEn(findComment(extractedInputs, key)));
        //refOutput.getClassification().addAll(tex.getClassification()); FIXME
        refOutput.setOutputGroup((short) 2);
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
        ex.setAmount(Double.parseDouble(tu.provideValue(modelsOutput)) / dividingValue);
        if (ex.getAmount() == 0.d)
            return null;
        ex.setMathematicalRelation("");
        TValidIntermediateExchange tex = possibleExchanges.getExchange(tu.provideName(modelsOutput));
        if (tex == null)
        {
            System.out.println("Exchange not found: " + tu.provideName(modelsOutput));
            return null;
        }
        ex.setIntermediateExchangeId(tex.getId());
        ex.setName(tex.getName());
        if (!tex.getUnitName().getValue().equals(tu.unit.symbol))
            System.out.println("Unit is not the same for " + tex.getName().getValue() + ". " +
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
        ex.setMathematicalRelation("");
        TValidElementaryExchange tex = possibleSubstances.getExchange(su.subCompartment, su.name);
        if (tex == null)
        {
            System.out.println("Substance not found: " + su.name);
            return null;
        }
        ex.setElementaryExchangeId(tex.getId());
        ex.setName(tex.getName());
        ex.setCompartment(tex.getCompartment());
        if (!tex.getUnitName().getValue().equals(su.unit.symbol))
            System.out.println("Unit is not the same for " + tex.getName().getValue() + ". " +
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

    public static final void main(String[] args) throws IOException
    {
        EcospoldFileWriter w = new EcospoldFileWriter(
                new PossibleIntermediateExchangesCache("/home/cporte/notbackedup/dbmt_data/MasterData/Production"),
                new PossibleElementaryExchangesCache("/home/cporte/notbackedup/dbmt_data/MasterData/Production"),
                new PossiblePropertyCache("/home/cporte/notbackedup/dbmt_data/MasterData/Production"));
        w.writeModelsOutputToEcospoldFile(
                ImmutableMap.of("country", "FR", "yield_main_product_per_crop_cycle", "1000.0"),
                new ValueGroup("empty"),
                OutputTarget.ECOINVENT, new PrintWriter(new OutputStreamWriter(System.out)));
    }
}
