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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.quantis_intl.commons.scsv.ScsvEnums.SubCompartment;
import com.quantis_intl.commons.scsv.ScsvLineSerializer;
import com.quantis_intl.commons.scsv.ScsvLinesWriter;
import com.quantis_intl.commons.scsv.beans.UncertaintyBean;
import com.quantis_intl.commons.scsv.beans.UncertaintyBean.LognormalBean;
import com.quantis_intl.commons.scsv.beans.processes.ProductUsageBean;
import com.quantis_intl.commons.scsv.beans.processes.SubstanceUsageBean;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataWriter;
import com.quantis_intl.commons.scsv.processes.LiteratureReferenceUsage;
import com.quantis_intl.commons.scsv.processes.Product;
import com.quantis_intl.commons.scsv.processes.ProductUsage;
import com.quantis_intl.commons.scsv.processes.ScsvProcess;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.CategoryType;
import com.quantis_intl.commons.scsv.processes.ScsvProcessEnums.Type;
import com.quantis_intl.commons.scsv.processes.ScsvProcessWriter;
import com.quantis_intl.commons.scsv.processes.SubstanceUsage;
import com.quantis_intl.commons.scsv.processes.SystemDescriptionUsage;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.scsv.GeneratedMetadata;
import com.quantis_intl.lcigenerator.scsv.GeneratedProcess;

public class ScsvFileWriter
{
    private final ScsvLineSerializer serializer;

    private final ScsvMetadataWriter metadataWriter;

    public ScsvFileWriter()
    {
        this.serializer = new ScsvLineSerializer(';');
        this.metadataWriter = new ScsvMetadataWriter();
    }

    public void writeModelsOutputToScsvFile(Map<String, String> modelsOutput, ValueGroup extractedInputs,
            OutputStream os)
    {
        try
        {
            writeModelsOutputToScsvFile(modelsOutput, extractedInputs,
                    new BufferedWriter(new OutputStreamWriter(os, Charset.forName("windows-1252"))));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private void writeModelsOutputToScsvFile(Map<String, String> modelsOutput, ValueGroup extractedInputs, Writer writer)
            throws IOException
    {
        ScsvLinesWriter linesWriter = new ScsvLinesWriter(serializer, writer);
        GeneratedMetadata generatedMetadata = new GeneratedMetadata();
        metadataWriter.write(generatedMetadata, linesWriter);
        linesWriter.writeNewLine();
        ScsvProcessWriter processWriter = new ScsvProcessWriter(linesWriter, generatedMetadata.getDateFormatter());
        processWriter.write(new GeneratedProcess(modelsOutput, extractedInputs));
        // FIXME: Not a perfect place for that
        // FIXME: For logged users, do not include this process if it was already included previously?
        String surfaceIrrigation = modelsOutput.get("surface_irrigation_no_energy");
        if (!Strings.isNullOrEmpty(surfaceIrrigation))
        {
            double value = Double.parseDouble(surfaceIrrigation);
            if (value != 0.d)
            {
                linesWriter.writeNewLine();
                processWriter.write(new GravityIrrigationProcess());
            }
        }

        writer.flush();
    }

    private static class GravityIrrigationProcess implements ScsvProcess.ProductScsvProcess
    {
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
            return Optional.of("Irrigating, surface, gravity/GLO");
        }

        @Override
        public LocalDate getDate()
        {
            return LocalDate.of(2015, Month.JULY, 9);
        }

        @Override
        public Optional<String> getGenerator()
        {
            return Optional
                    .of("Christophe Porté\nQuantis\nEPFL Innovation Park, Bat D\nCH-1015 Lausanne\n0041 21 691 91 89");
        }

        @Override
        public Optional<? extends List<? extends LiteratureReferenceUsage>> getLiteratureReferences()
        {
            return Optional.of(ImmutableList.of(new LiteratureReferenceUsage()
            {
                @Override
                public String getLiteratureReference()
                {
                    return "Life Cycle Inventories of Agricultural Production Systems/2007/Nemecek, T.";
                }
            }));
        }

        @Override
        public Optional<String> getComment()
        {
            return Optional
                    .of("Copy of the Surface irrigation powered with a diesel pump dataset present in the WFLDB v3.0, without the diesel burned in water pump.");
        }

        @Override
        public Optional<? extends SystemDescriptionUsage> getSystemDescription()
        {
            return Optional.of(new SystemDescriptionUsage()
            {
                @Override
                public String getSystemDescription()
                {
                    return "Ecoinvent";
                }
            });
        }

        @Override
        public List<? extends ProductUsage> getMaterialsFuels()
        {
            return ImmutableList.of(ProductUsageBean.of("Tractor, production/CH/I U", "kg", "0.00038194*2",
                    UncertaintyBean.UNDEFINED, "(2,1,1,1,1,na)"),
                    ProductUsageBean.of("Agricultural machinery, general, production/CH/I U", "kg", "0.018056",
                            LognormalBean.of("1.0714"), "(2,1,1,1,1,na)"),
                    ProductUsageBean.of("Diesel, at regional storage/CH U", "kg", "0.00315*2",
                            UncertaintyBean.UNDEFINED, "(2,1,1,1,1,na)"),
                    ProductUsageBean.of("Shed/CH/I U", "m2", "0.000049089", LognormalBean.of("1.0714"),
                            "(2,1,1,1,1,na)"),
                    ProductUsageBean.of("Excavation, hydraulic digger/RER U", "m3", "0.0033333*2",
                            UncertaintyBean.UNDEFINED, "(2,1,1,1,1,na)"),
                    ProductUsageBean.of("Cast iron, at plant/RER U", "kg", "0.0035556", LognormalBean.of("1.0714"),
                            "(2,1,1,1,1,na)"));
        }

        @Override
        public List<? extends SubstanceUsage> getResources()
        {
            return ImmutableList.of(SubstanceUsageBean.of("Occupation, construction site", SubCompartment.RAW_LAND,
                    "m2a", "0.0055556", LognormalBean.of("1.5051"), "(2,2,1,1,1,na)"));
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<? extends SubstanceUsage> getEmissionsToAir()
        {
            return (List<? extends SubstanceUsage>) ImmutableList
                    .builder()
                    .add(SubstanceUsageBean.of("NMVOC, non-methane volatile organic compounds, unspecified origin",
                            SubCompartment.AIR_LOW_POP, "kg", "0.000013735*2", UncertaintyBean.UNDEFINED,
                            "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Nitrogen oxides",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.00016065*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Carbon monoxide, fossil",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.00003024*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Carbon dioxide, fossil",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0097886*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Sulfur dioxide",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0000031752*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Methane, fossil",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.00000040635*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Benzene",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.000000022995*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Particulates, < 2.5 um",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.000013039*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Cadmium",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0000000000315*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Chromium",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0000000001575*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Copper",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.000000005355*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Dinitrogen monoxide",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.000000378*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Nickel",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0000000002205*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Zinc",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.00000000315*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("Benzo(a)pyrene",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0000000000945*2", UncertaintyBean.UNDEFINED,
                                    "(2,1,1,1,1,na)"),
                            SubstanceUsageBean.of("PAH, polycyclic aromatic hydrocarbons",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.000000010364*2", UncertaintyBean.UNDEFINED,
                                    "(2,2,1,1,1,na)"),
                            SubstanceUsageBean.of("Heat, waste",
                                    SubCompartment.AIR_LOW_POP, "MJ", "0.90432*2", UncertaintyBean.UNDEFINED,
                                    "(2,2,1,1,1,na)"),
                            SubstanceUsageBean.of("Ammonia",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.000000063*2", UncertaintyBean.UNDEFINED,
                                    "(2,2,1,1,1,na)"),
                            SubstanceUsageBean.of("Selenium",
                                    SubCompartment.AIR_LOW_POP, "kg", "0.0000000000315*2", UncertaintyBean.UNDEFINED,
                                    "(2,2,1,1,1,na)")).build();
        }

        @Override
        public List<? extends SubstanceUsage> getEmissionsToSoil()
        {
            return ImmutableList.of(SubstanceUsageBean.of("Zinc", SubCompartment.SOIL_AGRICULTURAL, "kg",
                    "0.0000022549*2", UncertaintyBean.UNDEFINED, "(2,2,1,1,1,na)"),
                    SubstanceUsageBean.of("Lead", SubCompartment.SOIL_AGRICULTURAL, "kg", "0.0000000044132*2",
                            UncertaintyBean.UNDEFINED, "(2,2,1,1,1,na)"),
                    SubstanceUsageBean.of("Cadmium", SubCompartment.SOIL_AGRICULTURAL, "kg", "0.00000000090556*2",
                            UncertaintyBean.UNDEFINED, "(2,2,1,1,1,na)"));
        }

        @Override
        public List<? extends ProductUsage> getWasteToTreatment()
        {
            return ImmutableList.of(ProductUsageBean.of(
                    "Disposal, building, bulk iron (excluding reinforcement), to sorting plant/CH U", "kg",
                    "0.0000035556", LognormalBean.of("1.0744"), "(2,2,1,1,1,na)"),
                    ProductUsageBean.of("Disposal, building, polyvinylchloride products, to final disposal/CH U", "kg",
                            "0.0000009375", LognormalBean.of("1.0744"), "(2,2,1,1,1,na)"),
                    ProductUsageBean.of(
                            "Disposal, building, polyethylene/polypropylene products, to final disposal/CH U", "kg",
                            "0.0000055556", LognormalBean.of("1.0744"), "(2,2,1,1,1,na)"));
        }

        @Override
        public List<? extends Product> getProducts()
        {
            return ImmutableList.of(new Product()
            {
                @Override
                public String getName()
                {
                    return "Irrigating, surface, gravity (ALCIG)/GLO U";
                }

                @Override
                public String getUnit()
                {
                    return "m3";
                }

                @Override
                public String getCategory()
                {
                    return "_ALCIG import";
                }
            });
        }
    }
}
