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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXB;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.quantis_intl.commons.ecospold2.ecospold02.*;
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
        FILE_ATTRIBUTES.setContextId(UUID.fromString("de659012-50c4-4e96-b54a-fc781bf987ab"));
        FILE_ATTRIBUTES.setContextName(TString80.ofEn("ecoinvent"));
    }

    public EcospoldFileWriter()
    {
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
        TActivityDataset dataset = new TActivityDataset();
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
        activity.setActivityNameId(UUID.randomUUID());//TODO
        activity.setIncludedActivitiesEnd(TString32000.ofEn("TODO"));
        //Mettre le Yield + le fait que c'est généré par ALCIG + somme N P K en kg/ha
        activity.setGeneralComment(TTextAndImage.ofEnTexts("TODO", "TODO"));
        activity.setType(1);
        //activity.tags?
        //desc.classification?

        //// !!!!!!!!!!!!!! Output par kg, pas par ha


        TFlowData flowData = new TFlowData();
        dataset.setFlowData(flowData);
        //TODO: Intermediary flows
        //TODO: Elementary flows

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

        JAXB.marshal(dataset, writer);
        writer.flush();
    }

    public static final void main(String[] args) throws IOException
    {
        EcospoldFileWriter w = new EcospoldFileWriter();
        w.writeModelsOutputToEcospoldFile(ImmutableMap.of("country", "FR"), new ValueGroup("empty"),
                                          OutputTarget.ECOINVENT, new PrintWriter(new OutputStreamWriter(System.out)));
    }
}
