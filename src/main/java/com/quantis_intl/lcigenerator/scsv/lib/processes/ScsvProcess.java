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

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.BoundaryWithNature;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.CategoryType;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.CutOffRules;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Geography;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.MultipleOutputAlloc;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Representativeness;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Status;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.SubstitutionAllocation;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.SystemBoundary;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Technology;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.TimePeriod;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.Type;
import com.quantis_intl.lcigenerator.scsv.lib.processes.ScsvProcessEnums.WasteTreatmentAlloc;

public interface ScsvProcess
{
    CategoryType getCategoryType();

    default String getId()
    {
        return "";
    }

    default Type getType()
    {
        return Type.Empty;
    }

    default Optional<String> getName()
    {
        return Optional.empty();
    }

    default Status getStatus()
    {
        return Status.None;
    }

    default TimePeriod getTimePeriod()
    {
        return TimePeriod.Unspecified;
    }

    default Geography getGeography()
    {
        return Geography.Unspecified;
    }

    default Technology getTechnology()
    {
        return Technology.Unspecified;
    }

    default Representativeness getRepresentativeness()
    {
        return Representativeness.Unspecified;
    }

    default CutOffRules getCutOffRules()
    {
        return CutOffRules.Unspecified;
    }

    default SystemBoundary getSystemBoundary()
    {
        return SystemBoundary.Unspecified;
    }

    default BoundaryWithNature getBoundaryWithNature()
    {
        return BoundaryWithNature.Unspecified;
    }

    default boolean getInfrastructure()
    {
        return false;
    }

    default LocalDate getDate()
    {
        return LocalDate.of(1899, Month.DECEMBER, 30);
    }

    default Optional<String> getRecord()
    {
        return Optional.empty();
    }

    default Optional<String> getGenerator()
    {
        return Optional.empty();
    }

    default Optional<List<LiteratureReferenceUsage>> getLiteratureReferences()
    {
        return Optional.empty();
    }

    default Optional<String> getCollectionMethod()
    {
        return Optional.empty();
    }

    default Optional<String> getDataTreatment()
    {
        return Optional.empty();
    }

    default Optional<String> getVerification()
    {
        return Optional.empty();
    }

    default Optional<String> getComment()
    {
        return Optional.empty();
    }

    default Optional<String> getAllocationRules()
    {
        return Optional.empty();
    }

    default Optional<SystemDescriptionUsage> getSystemDescription()
    {
        return Optional.empty();
    }

    default List<ProductUsage> getMaterialsFuels()
    {
        return Collections.emptyList();
    }

    default List<ProductUsage> getElectricityHeat()
    {
        return Collections.emptyList();
    }

    default Optional<List<InputParameter>> getInputParameters()
    {
        return Optional.empty();
    }

    default Optional<List<CalculatedParameter>> getCalculatedParameters()
    {
        return Optional.empty();
    }

    public static interface NonScenarioProcess extends ScsvProcess
    {
        default List<ProductUsage> getAvoidedProducts()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getResources()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getEmissionsToAir()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getEmissionsToWater()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getEmissionsToSoil()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getFinalWasteFlows()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getNonMaterialEmissions()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getSocialIssues()
        {
            return Collections.emptyList();
        }

        default List<SubstanceUsage> getEconomicIssues()
        {
            return Collections.emptyList();
        }

        default List<ProductUsage> getWasteToTreatment()
        {
            return Collections.emptyList();
        }
    }

    public static interface ProductScsvProcess extends NonScenarioProcess
    {
        default MultipleOutputAlloc getMultipleOutputAllocation()
        {
            return MultipleOutputAlloc.Unspecified;
        }

        default SubstitutionAllocation getSubstitutionAllocation()
        {
            return SubstitutionAllocation.Unspecified;
        }

        List<Product> getProducts();
    }

    public static interface WasteScsvProcess extends NonScenarioProcess
    {
        default CategoryType getCategoryType()
        {
            return CategoryType.WasteTreatment;
        }

        default WasteTreatmentAlloc getWasteTreatmentAllocation()
        {
            return WasteTreatmentAlloc.Unspecified;
        }

        WasteTreatment getWasteTreatment();
    }

    public static interface WasteScsvScenario extends ScsvProcess
    {
        default CategoryType getCategoryType()
        {
            return CategoryType.WasteScenario;
        }

        // TODO: analyse and objectify
        String getWasteScenario();

        // TODO: analyse and objectify
        default List<String> getSeparatedWaste()
        {
            return Collections.emptyList();
        }

        // TODO: analyse and objectify
        default List<String> getRemainingWaste()
        {
            return Collections.emptyList();
        }
    }
}
