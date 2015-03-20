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
package com.quantis_intl.lcigenerator.scsv.processes;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvLineSerializer;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvLinesWriter;
import com.quantis_intl.lcigenerator.scsv.lib.processes.InputParameter;
import com.quantis_intl.lcigenerator.scsv.lib.processes.LiteratureReferenceUsage;
import com.quantis_intl.lcigenerator.scsv.lib.processes.Product;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ProductUsage;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcess;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.CategoryType;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Type;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessWriter;
import com.quantis_intl.lcigenerator.scsv.lib.processes.SubstanceUsage;
import com.quantis_intl.lcigenerator.scsv.lib.processes.SystemDescriptionUsage;
import com.quantis_intl.lcigenerator.scsv.lib.processes.Uncertainty;

public class ScsvProcessWriterTest
{

    @Test
    public void test() throws IOException
    {
        String expectedString = CharStreams.toString(new InputStreamReader(ScsvProcessWriterTest.class
                .getResourceAsStream("process.csv"), Charsets.UTF_8)).replace("\n", "\r\n");

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(o, Charsets.UTF_8);
        ScsvProcessWriter writer = new ScsvProcessWriter(new ScsvLinesWriter(new ScsvLineSerializer(';'), ow),
                DateTimeFormatter.ofPattern("M/d/yyyy"));
        writer.write(new ScsvProcess.ProductScsvProcess()
        {
            @Override
            public CategoryType getCategoryType()
            {
                return CategoryType.Material;
            }

            @Override
            public String getId()
            {
                return "EIN_UNIT06567700393";
            }

            @Override
            public Type getType()
            {
                return Type.Unit;
            }

            @Override
            public Optional<String> getName()
            {
                return Optional.of("cumene, at plant/kg/RER");
            }

            @Override
            public LocalDate getDate()
            {
                return LocalDate.of(2003, Month.JUNE, 19);
            }

            @Override
            public Optional<String> getRecord()
            {
                return Optional
                        .of("Data entry by: Roland HischierTelephone: 0041 71 274 78 47; E-mail: empa@ecoinvent.org; Company: EMPA; Country: CH");
            }

            @Override
            public Optional<String> getGenerator()
            {
                return Optional
                        .of("Generator/publicator: Roland HischierTelephone: 0041 71 274 78 47; E-mail: empa@ecoinvent.org; Company: EMPA; Country: CH");
            }

            @Override
            public Optional<List<LiteratureReferenceUsage>> getLiteratureReferences()
            {
                return Optional.of(ImmutableList.of(new LiteratureReferenceUsage()
                {
                    @Override
                    public String getLiteratureReference()
                    {
                        return "Life Cycle Inventories of Packaging and Graphical Paper/2007/Hischier R.";
                    }

                    @Override
                    public String getComment()
                    {
                        return "Data has been published entirely inCopyright: true";
                    }
                }));
            }

            @Override
            public Optional<String> getCollectionMethod()
            {
                return Optional
                        .of("Sampling procedure: Process data based on stoechiometric calculations of few literature sources. Energy demand based on approximation from large chemical plant. Process emissions based on estimations only.");
            }

            @Override
            public Optional<String> getVerification()
            {
                return Optional
                        .of("Proof reading validation: passedValidator: Mischa ClassenTelephone: 0041 44 823 4937; E-mail: empa@ecoinvent.org; Company: EMPA; Country: CH");
            }

            @Override
            public Optional<String> getComment()
            {
                return Optional
                        .of("Translated name: Cumene, ab WerkIncluded processes: Raw materials and chemicals used for production, transport of materials to manufacturing plant, estimated emissions to air and water from production (incomplete), estimation of energy demand and infrastructure of the plant (approximation). Solid wastes omitted.Remark: The functional unit represent 1 kg of liquid cumene. Large uncertainty of the process data due to weak data on the production process and missing data on process emissions.CAS number: 000098-82-8; Formula: C9H12; Geography: Data used has no specific geographical origin (stoechiometry). Average europenan processes for raw materials, transport requirements and electricity mix used.Technology: Production by alkylation of benzene and propene with a yield of 95%. Inventory bases on stoechiometric calculations. The emissions to air (0.2 wt.% of raw material input) and water were estimated using mass balance. Treatment of the waste water in a internal waste water treatment plant assumed (elimination efficiency of 90% for C).Time period: date of published literatureVersion: 2.2Synonyms: (1-Methylethyl)benzene, 2-PhenylpropaneEnergy values: UndefinedProduction volume: unknownLocal category: ChemikalienLocal subcategory: OrganischSource file: 00389.XML");
            }

            @Override
            public Optional<String> getAllocationRules()
            {
                return Optional.of("");
            }

            @Override
            public Optional<SystemDescriptionUsage> getSystemDescription()
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
            public List<Product> getProducts()
            {
                return ImmutableList.of(new Product()
                {
                    @Override
                    public String getName()
                    {
                        return "Cumene, at plant/RER U";
                    }

                    @Override
                    public String getUnit()
                    {
                        return "kg";
                    }

                    @Override
                    public String getAmount()
                    {
                        return "1";
                    }

                    @Override
                    public String getAllocationPercent()
                    {
                        return "80";
                    }

                    @Override
                    public String getCategory()
                    {
                        return "Chemicals\\Organic";
                    }

                    @Override
                    public String getComment()
                    {
                        return "Europe";
                    }
                }, new Product()
                {
                    @Override
                    public String getName()
                    {
                        return "Cumene, at plant/GLO U";
                    }

                    @Override
                    public String getUnit()
                    {
                        return "kg";
                    }

                    @Override
                    public String getAmount()
                    {
                        return "1";
                    }

                    @Override
                    public String getAllocationPercent()
                    {
                        return "20";
                    }

                    @Override
                    public String getCategory()
                    {
                        return "Chemicals\\Organic";
                    }

                    @Override
                    public String getComment()
                    {
                        return "Global";
                    }
                });
            }

            @Override
            public List<SubstanceUsage> getResources()
            {
                return ImmutableList.of(new SubstanceUsage()
                {
                    @Override
                    public String getName()
                    {
                        return "Water, cooling, unspecified natural origin/m3";
                    }

                    @Override
                    public String getSubCompartment()
                    {
                        return "in water";
                    }

                    @Override
                    public String getUnit()
                    {
                        return "m3";
                    }

                    @Override
                    public String getAmount()
                    {
                        return "0.024";
                    }

                    @Override
                    public Uncertainty getUncertainty()
                    {
                        return new Uncertainty.Lognormal()
                        {
                            @Override
                            public String getStandardDeviation()
                            {
                                return "1.9";
                            }
                        };
                    }

                    @Override
                    public String getComment()
                    {
                        return "(5,5,1,1,4,5); estimated with data from a large chem. plant ";
                    }
                });
            }

            @Override
            public List<ProductUsage> getMaterialsFuels()
            {
                return ImmutableList.of(new ProductUsage()
                {
                    @Override
                    public String getName()
                    {
                        return "Benzene, at plant/RER U";
                    }

                    @Override
                    public String getUnit()
                    {
                        return "kg";
                    }

                    @Override
                    public String getAmount()
                    {
                        return "0.684";
                    }

                    @Override
                    public String getComment()
                    {
                        return "(4,na,na,na,na,na); estimation based on process yield 90-99.8%";
                    }
                });
            }

            @Override
            public Optional<List<InputParameter>> getInputParameters()
            {
                return Optional.of(ImmutableList.of());
            }
        });
        ow.flush();

        assertEquals(expectedString, new String(o.toByteArray(), Charsets.UTF_8));
    }
}
