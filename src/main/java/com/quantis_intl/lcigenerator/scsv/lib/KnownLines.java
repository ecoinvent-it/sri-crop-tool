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
package com.quantis_intl.lcigenerator.scsv.lib;

public enum KnownLines implements ScsvLine
{
    EMPTY(""),
    End("End"),
    Process("Process"),
    CategoryType("Category type"),
    ProcessIdentifier("Process identifier"),
    Type("Type"),
    ProcessName("Process name"),
    Status("Status"),
    TimePeriod("Time period"),
    Geography("Geography"),
    Technology("Technology"),
    Representativeness("Representativeness"),
    WasteTreatmentAllocation("Waste treatment allocation"),
    MultipleOutputAllocation("Multiple output allocation"),
    SubstitutionAllocation("Substitution allocation"),
    CutOffRules("Cut off rules"),
    CapitalGoods("Capital goods"),
    BoundaryWithNature("Boundary with nature"),
    Infrastructure("Infrastructure"),
    Date("Date"),
    Record("Record"),
    Generator("Generator"),
    LiteratureReferences("Literature references"),
    CollectionMethod("Collection method"),
    DataTreatment("Data treatment"),
    Verification("Verification"),
    Comment("Comment"),
    AllocationRules("Allocation rules"),
    SystemDescription("System description"),
    WasteScenario("Waste scenario"),
    WasteTreatment("Waste treatment"),
    Products("Products"),
    AvoidedProducts("Avoided products"),
    Resources("Resources"),
    MaterialsFuels("Materials/fuels"),
    ElectricityHeat("Electricity/heat"),
    SeparatedWaste("Separated waste"),
    RemainingWaste("Remaining waste"),
    EmissionsToAir("Emissions to air"),
    EmissionsToWater("Emissions to water"),
    EmissionsToSoil("Emissions to soil"),
    FinalWasteFlows("Final waste flows"),
    NonMaterialEmissions("Non material emissions"),
    SocialIssues("Social issues"),
    EconomicIssues("Economic issues"),
    WasteToTreatment("Waste to treatment"),
    InputParameters("Input parameters"),
    CalculatedParameters("Calculated parameters"),
    Name("Name"),
    Category("Category"),
    Description("Description"),
    SubSystems("Sub-systems"),
    Cut_OffRules("Cut-off rules"),
    EnergyModel("Energy model"),
    TransportModel("Transport model"),
    WasteModel("Waste model"),
    OtherAssumptions("Other assumptions"),
    OtherInformation("Other information"),
    LiteratureReference("Literature reference"),
    DocumentationLink("Documentation link"),
    Quantities("Quantities"),
    Units("Units"),
    RawMaterials("Raw materials"),
    AirborneEmissions("Airborne emissions"),
    WaterborneEmissions("Waterborne emissions");

    private final String header;

    private KnownLines(String header)
    {
        this.header = header;
    }

    @Override
    public String asString()
    {
        return header;
    }
}