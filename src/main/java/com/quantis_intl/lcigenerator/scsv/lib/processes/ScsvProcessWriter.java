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
package com.quantis_intl.lcigenerator.scsv.lib.processes;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.quantis_intl.lcigenerator.scsv.lib.KnownLines;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvLineSerializer;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvLinesWriter;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcess.NonScenarioProcess;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcess.ProductScsvProcess;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcess.WasteScsvProcess;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcess.WasteScsvScenario;

public class ScsvProcessWriter
{
    private final ScsvLinesWriter writer;

    private final DateTimeFormatter dateFormatter;

    public ScsvProcessWriter(ScsvLinesWriter writer, DateTimeFormatter dateFormatter)
    {
        this.writer = writer;
        this.dateFormatter = dateFormatter;
    }

    public void write(ScsvProcess process)
    {
        writer.writeKnownLine(KnownLines.Process);
        writer.writeNewLine();
        writer.writeFragment(KnownLines.CategoryType, process.getCategoryType().toString());
        writer.writeFragment(KnownLines.ProcessIdentifier, process.getId());
        writer.writeFragment(KnownLines.Type, process.getType().toString());
        writeStringIfPresent(KnownLines.ProcessName, process.getName());
        writer.writeFragment(KnownLines.Status, process.getStatus().toString());
        writer.writeFragment(KnownLines.TimePeriod, process.getTimePeriod().toString());
        writer.writeFragment(KnownLines.Geography, process.getGeography().toString());
        writer.writeFragment(KnownLines.Technology, process.getTechnology().toString());
        writer.writeFragment(KnownLines.Representativeness, process.getRepresentativeness().toString());
        if (process instanceof ProductScsvProcess)
        {
            ProductScsvProcess product = (ProductScsvProcess) process;
            writer.writeFragment(KnownLines.MultipleOutputAllocation, product.getMultipleOutputAllocation().toString());
            writer.writeFragment(KnownLines.SubstitutionAllocation, product.getSubstitutionAllocation().toString());
        }
        else if (process instanceof WasteScsvProcess)
        {
            WasteScsvProcess waste = (WasteScsvProcess) process;
            writer.writeFragment(KnownLines.WasteTreatmentAllocation, waste.getWasteTreatmentAllocation().toString());
        }
        writer.writeFragment(KnownLines.CutOffRules, process.getCutOffRules().toString());
        // NOTE: Caveat: Simapro renamed "Capital goods" to "System boundary" in the soft, but not in the export format
        writer.writeFragment(KnownLines.CapitalGoods, process.getSystemBoundary().toString());
        writer.writeFragment(KnownLines.BoundaryWithNature, process.getBoundaryWithNature().toString());
        writer.writeFragment(KnownLines.Infrastructure, ScsvLineSerializer.booleanToString(process.getInfrastructure()));
        writer.writeFragment(KnownLines.Date, dateFormatter.format(process.getDate()));
        writeStringIfPresent(KnownLines.Record, process.getRecord());
        writeStringIfPresent(KnownLines.Generator, process.getGenerator());
        writeLinesIfPresent(KnownLines.LiteratureReferences, process.getLiteratureReferences(),
                LiteratureReferenceUsage::asArray);
        writeStringIfPresent(KnownLines.CollectionMethod, process.getCollectionMethod());
        writeStringIfPresent(KnownLines.DataTreatment, process.getDataTreatment());
        writeStringIfPresent(KnownLines.Verification, process.getVerification());
        writeStringIfPresent(KnownLines.Comment, process.getComment());
        writeStringIfPresent(KnownLines.AllocationRules, process.getAllocationRules());
        writeLineIfPresent(KnownLines.SystemDescription, process.getSystemDescription(),
                SystemDescriptionUsage::asArray);
        if (process instanceof ProductScsvProcess)
        {
            ProductScsvProcess product = (ProductScsvProcess) process;
            writer.writeFragment(KnownLines.Products, product.getProducts().stream().map(Product::asArray));
        }
        else if (process instanceof WasteScsvProcess)
        {
            WasteScsvProcess waste = (WasteScsvProcess) process;
            writer.writeFragment(KnownLines.WasteTreatment, waste.getWasteTreatment().asArray());
        }
        else if (process instanceof WasteScsvScenario)
        {
            WasteScsvScenario scenario = (WasteScsvScenario) process;
            writer.writeFragment(KnownLines.WasteScenario, scenario.getWasteScenario());
        }
        if (process instanceof NonScenarioProcess)
        {
            NonScenarioProcess nonScenarioProcess = (NonScenarioProcess) process;
            writeProductUsage(KnownLines.AvoidedProducts, nonScenarioProcess.getAvoidedProducts());
            writeSubstanceUsage(KnownLines.Resources, nonScenarioProcess.getResources());
        }
        writeProductUsage(KnownLines.MaterialsFuels, process.getMaterialsFuels());
        writeProductUsage(KnownLines.ElectricityHeat, process.getElectricityHeat());
        if (process instanceof NonScenarioProcess)
        {
            NonScenarioProcess nonScenarioProcess = (NonScenarioProcess) process;
            writeSubstanceUsage(KnownLines.EmissionsToAir, nonScenarioProcess.getEmissionsToAir());
            writeSubstanceUsage(KnownLines.EmissionsToWater, nonScenarioProcess.getEmissionsToWater());
            writeSubstanceUsage(KnownLines.EmissionsToSoil, nonScenarioProcess.getEmissionsToSoil());
            writeSubstanceUsage(KnownLines.FinalWasteFlows, nonScenarioProcess.getFinalWasteFlows());
            writeSubstanceUsage(KnownLines.NonMaterialEmissions, nonScenarioProcess.getNonMaterialEmissions());
            writeSubstanceUsage(KnownLines.SocialIssues, nonScenarioProcess.getSocialIssues());
            writeSubstanceUsage(KnownLines.EconomicIssues, nonScenarioProcess.getEconomicIssues());
            writeProductUsage(KnownLines.WasteToTreatment, nonScenarioProcess.getWasteToTreatment());
        }
        else if (process instanceof WasteScsvScenario)
        {
            WasteScsvScenario scenario = (WasteScsvScenario) process;
            writer.writeFragment(KnownLines.SeparatedWaste,
                    scenario.getSeparatedWaste().stream().map(s -> new String[] { s }));
            writer.writeFragment(KnownLines.RemainingWaste,
                    scenario.getRemainingWaste().stream().map(s -> new String[] { s }));
        }
        writeLinesIfPresent(KnownLines.InputParameters, process.getInputParameters(), InputParameter::asArray);
        writeLinesIfPresent(KnownLines.CalculatedParameters, process.getCalculatedParameters(),
                CalculatedParameter::asArray);

        writer.writeKnownLine(KnownLines.End);
    }

    private void writeStringIfPresent(KnownLines header, Optional<String> optionalValue)
    {
        if (optionalValue.isPresent())
            writer.writeFragment(header, optionalValue.get());
    }

    private <T> void writeLineIfPresent(KnownLines header, Optional<T> optionalValue, Function<T, String[]> asArray)
    {
        if (optionalValue.isPresent())
            writer.writeFragment(header, asArray.apply(optionalValue.get()));
    }

    private <T> void writeLinesIfPresent(KnownLines header, Optional<List<T>> optionalValue,
            Function<T, String[]> asArray)
    {
        if (optionalValue.isPresent())
            writer.writeFragment(header, optionalValue.get().stream().map(asArray));
    }

    private void writeProductUsage(KnownLines header, List<ProductUsage> list)
    {
        writer.writeFragment(header, list.stream().map(ProductUsage::asArray));
    }

    private void writeSubstanceUsage(KnownLines header, List<SubstanceUsage> list)
    {
        writer.writeFragment(header, list.stream().map(SubstanceUsage::asArray));
    }
}
