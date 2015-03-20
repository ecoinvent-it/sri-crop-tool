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

import com.google.common.base.Strings;
import com.google.common.collect.EnumHashBiMap;

public class ScsvProcessEnums
{
    private ScsvProcessEnums()
    {
    }

    private static final String MATERIAL_STR = "material";
    private static final String ENERGY_STR = "energy";
    private static final String TRANSPORT_STR = "transport";
    private static final String PROCESSING_STR = "processing";
    private static final String USE_STR = "use";
    private static final String WASTE_SCENARIO_STR = "waste scenario";
    private static final String WASTE_TREATMENT_STR = "waste treatment";

    public static enum CategoryType
    {
        UNREAD("Unread"),
        Material(MATERIAL_STR),
        Energy(ENERGY_STR),
        Transport(TRANSPORT_STR),
        Processing(PROCESSING_STR),
        Use(USE_STR),
        WasteScenario(WASTE_SCENARIO_STR),
        WasteTreatment(WASTE_TREATMENT_STR);

        private final String name;

        private CategoryType(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static CategoryType fromString(String str)
        {
            switch (str)
            {
                case MATERIAL_STR:
                    return Material;
                case ENERGY_STR:
                    return Energy;
                case TRANSPORT_STR:
                    return Transport;
                case PROCESSING_STR:
                    return Processing;
                case USE_STR:
                    return Use;
                case WASTE_SCENARIO_STR:
                    return WasteScenario;
                case WASTE_TREATMENT_STR:
                    return WasteTreatment;
                default:
                    return UNREAD;
            }
        }
    }

    private static final String UNIT_STR = "Unit process";
    private static final String SYSTEM_STR = "System";

    public static enum Type
    {
        UNREAD("Unread"),
        Empty(""),
        Unit(UNIT_STR),
        System(SYSTEM_STR);

        private final String name;

        private Type(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static Type fromString(String str)
        {
            switch (str)
            {
                case UNIT_STR:
                    return Unit;
                case SYSTEM_STR:
                    return System;
                case "":
                    return Empty;
                default:
                    return UNREAD;
            }
        }
    }

    private static final String NONE_STR = "";
    private static final String TEMPORARY_STR = "Temporary";
    private static final String DRAFT_STR = "Draft";
    private static final String TO_BE_REVISED_STR = "To be revised";
    private static final String TO_BE_REVIEWED_STR = "To be reviewed";
    private static final String FINISHED_STR = "Finished";

    public static enum Status
    {
        UNREAD("Unread"),
        None(NONE_STR),
        Temporary(TEMPORARY_STR),
        Draft(DRAFT_STR),
        ToBeRevised(TO_BE_REVISED_STR),
        ToBeReviewed(TO_BE_REVIEWED_STR),
        Finished(FINISHED_STR);

        private final String name;

        private Status(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static Status fromString(String str)
        {
            switch (str)
            {
                case NONE_STR:
                    return None;
                case TEMPORARY_STR:
                    return Temporary;
                case DRAFT_STR:
                    return Draft;
                case TO_BE_REVISED_STR:
                    return ToBeRevised;
                case TO_BE_REVIEWED_STR:
                    return ToBeReviewed;
                case FINISHED_STR:
                    return Finished;
                default:
                    return UNREAD;
            }
        }
    }

    public static final EnumHashBiMap<TimePeriod, String> timePeriods;
    static
    {
        timePeriods = EnumHashBiMap.create(TimePeriod.class);
        timePeriods.put(TimePeriod.Unspecified, "Unspecified");
        timePeriods.put(TimePeriod.Unknown, "Unknown");
        timePeriods.put(TimePeriod.MixedData, "Mixed data");
        timePeriods.put(TimePeriod._2010AndAfter, "2010 and after");
        timePeriods.put(TimePeriod._2005_2009, "2005-2009");
        timePeriods.put(TimePeriod._2000_2004, "2000-2004");
        timePeriods.put(TimePeriod._1995_1999, "1995-1999");
        timePeriods.put(TimePeriod._1990_1994, "1990-1994");
        timePeriods.put(TimePeriod._1985_1989, "1985-1989");
        timePeriods.put(TimePeriod._1980_1984, "1980-1984");
        timePeriods.put(TimePeriod.Before1980, "Before 1980");
    }

    public static enum TimePeriod
    {
        UNREAD,
        Unspecified,
        Unknown,
        MixedData,
        _2010AndAfter,
        _2005_2009,
        _2000_2004,
        _1995_1999,
        _1990_1994,
        _1985_1989,
        _1980_1984,
        Before1980,
        Finished;

        @Override
        public String toString()
        {
            return Strings.nullToEmpty(timePeriods.get(this));
        }

        public static TimePeriod fromString(String str)
        {
            TimePeriod res = timePeriods.inverse().get(str);
            return res == null ? UNREAD : res;
        }
    }

    public static final EnumHashBiMap<Geography, String> geographies;
    static
    {
        geographies = EnumHashBiMap.create(Geography.class);
        geographies.put(Geography.Unspecified, "Unspecified");
        geographies.put(Geography.Unknown, "Unknown");
        geographies.put(Geography.MixedData, "Mixed data");
        geographies.put(Geography.EuropeWestern, "Europe, Western");
        geographies.put(Geography.EuropeEastern, "Europe, Eastern");
        geographies.put(Geography.NorthAmerica, "North America");
        geographies.put(Geography.SouthAndCentralAmerica, "South and Central America");
        geographies.put(Geography.AsiaFormerUssr, "Asia, former USSR");
        geographies.put(Geography.AsiaJapan, "Asia, Japan");
        geographies.put(Geography.AsiaKorea, "Asia, Korea");
        geographies.put(Geography.AsiaMiddleEast, "Asia, Middle East");
        geographies.put(Geography.AsiaSouthEast, "Asia, South East");
        geographies.put(Geography.AsiaChina, "Asia, China");
        geographies.put(Geography.AsiaIndianRegion, "Asia, Indian region");
        geographies.put(Geography.Africa, "Africa");
        geographies.put(Geography.Australia, "Australia");
        geographies.put(Geography.Oceans, "Oceans");
        geographies.put(Geography.ArcticRegions, "Arctic regions");
        geographies.put(Geography.World, "World");
    }

    public static enum Geography
    {
        UNREAD,
        Unspecified,
        Unknown,
        MixedData,
        EuropeWestern,
        EuropeEastern,
        NorthAmerica,
        SouthAndCentralAmerica,
        AsiaFormerUssr,
        AsiaJapan,
        AsiaKorea,
        AsiaMiddleEast,
        AsiaSouthEast,
        AsiaChina,
        AsiaIndianRegion,
        Africa,
        Australia,
        Oceans,
        ArcticRegions,
        World;

        @Override
        public String toString()
        {
            return Strings.nullToEmpty(geographies.get(this));
        }

        public static Geography fromString(String str)
        {
            Geography res = geographies.inverse().get(str);
            return res == null ? UNREAD : res;
        }
    }

    public static final EnumHashBiMap<Technology, String> technologies;
    static
    {
        technologies = EnumHashBiMap.create(Technology.class);
        technologies.put(Technology.Unspecified, "Unspecified");
        technologies.put(Technology.Unknown, "Unknown");
        technologies.put(Technology.MixedData, "Mixed data");
        technologies.put(Technology.WorstCase, "Worst case");
        technologies.put(Technology.OutdatedTechnology, "Outdated technology");
        technologies.put(Technology.AverageTechnology, "Average technology");
        technologies.put(Technology.ModernTechnology, "Modern technology");
        technologies.put(Technology.BestAvailableTechnology, "Best available technology");
        technologies.put(Technology.FutureTechnology, "Future technology");
    }

    public static enum Technology
    {
        UNREAD,
        Unspecified,
        Unknown,
        MixedData,
        WorstCase,
        OutdatedTechnology,
        AverageTechnology,
        ModernTechnology,
        BestAvailableTechnology,
        FutureTechnology;

        @Override
        public String toString()
        {
            return Strings.nullToEmpty(technologies.get(this));
        }

        public static Technology fromString(String str)
        {
            Technology res = technologies.inverse().get(str);
            return res == null ? UNREAD : res;
        }
    }

    public static final EnumHashBiMap<Representativeness, String> representativenesses;
    static
    {
        representativenesses = EnumHashBiMap.create(Representativeness.class);
        representativenesses.put(Representativeness.Unspecified, "Unspecified");
        representativenesses.put(Representativeness.Unknown, "Unknown");
        representativenesses.put(Representativeness.MixedData, "Mixed data");
        representativenesses.put(Representativeness.DataSpecificProcessCompany,
                "Data from a specific process and company");
        representativenesses.put(Representativeness.AverageSpecificProcess, "Average from a specific process");
        representativenesses.put(Representativeness.AverageOfSimilarOutputs,
                "Average from processes with similar outputs");
        representativenesses.put(Representativeness.AverageAllSuppliers, "Average of all suppliers");
        representativenesses.put(Representativeness.TheoreticalCalculation, "Theoretical calculation");
        representativenesses.put(Representativeness.DataBasedOnIOTables, "Data based on input-output tables");
        representativenesses.put(Representativeness.Estimate, "Estimate");
    }

    public static enum Representativeness
    {
        UNREAD,
        Unspecified,
        Unknown,
        MixedData,
        DataSpecificProcessCompany,
        AverageSpecificProcess,
        AverageOfSimilarOutputs,
        AverageAllSuppliers,
        TheoreticalCalculation,
        DataBasedOnIOTables,
        Estimate;

        @Override
        public String toString()
        {
            return Strings.nullToEmpty(representativenesses.get(this));
        }

        public static Representativeness fromString(String str)
        {
            Representativeness res = representativenesses.inverse().get(str);
            return res == null ? UNREAD : res;
        }
    }

    private static final String UNSPECIFIED_STR = "Unspecified";
    private static final String UNKNOWN_STR = "Unknown";
    private static final String NOT_APPLICABLE_STR = "Not applicable";
    private static final String PHYSICAL_CAUSALITY_STR = "Physical causality";
    private static final String SOCIO_ECONOMIC_CAUSALITY_STR = "Socio-economic causality";

    public static enum MultipleOutputAlloc
    {
        UNREAD("Unread"),
        Unspecified(UNSPECIFIED_STR),
        Unknown(UNKNOWN_STR),
        NotApplicable(NOT_APPLICABLE_STR),
        PhysicalCausality(PHYSICAL_CAUSALITY_STR),
        SocioEconimicCausality(SOCIO_ECONOMIC_CAUSALITY_STR);

        private final String name;

        private MultipleOutputAlloc(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static MultipleOutputAlloc fromString(String str)
        {
            switch (str)
            {
                case UNSPECIFIED_STR:
                    return Unspecified;
                case UNKNOWN_STR:
                    return Unknown;
                case NOT_APPLICABLE_STR:
                    return NotApplicable;
                case PHYSICAL_CAUSALITY_STR:
                    return PhysicalCausality;
                case SOCIO_ECONOMIC_CAUSALITY_STR:
                    return SocioEconimicCausality;
                default:
                    return UNREAD;
            }
        }
    }

    private static final String ACTUAL_SUBSTITUTION_STR = "Actual substitution";
    private static final String BY_CLOSE_PROXY_STR = "Substitution by close proxy (similar process)";
    private static final String BY_DISTANT_PROXY_STR = "Substitution by distant proxy (different process)";

    public static enum SubstitutionAllocation
    {
        UNREAD("Unread"),
        Unspecified(UNSPECIFIED_STR),
        Unknown(UNKNOWN_STR),
        NotApplicable(NOT_APPLICABLE_STR),
        ActualSubstitution(ACTUAL_SUBSTITUTION_STR),
        ByCloseProxy(BY_CLOSE_PROXY_STR),
        ByDistantProxy(BY_DISTANT_PROXY_STR);

        private final String name;

        private SubstitutionAllocation(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static SubstitutionAllocation fromString(String str)
        {
            switch (str)
            {
                case UNSPECIFIED_STR:
                    return Unspecified;
                case UNKNOWN_STR:
                    return Unknown;
                case NOT_APPLICABLE_STR:
                    return NotApplicable;
                case ACTUAL_SUBSTITUTION_STR:
                    return ActualSubstitution;
                case BY_CLOSE_PROXY_STR:
                    return ByCloseProxy;
                case BY_DISTANT_PROXY_STR:
                    return ByDistantProxy;
                default:
                    return UNREAD;
            }
        }
    }

    private static final String CLOSED_LOOP_STR = "Closed loop assumption";
    private static final String FULL_BY_CLOSE_PROXY_STR = "Full substitution by close proxy (similar process)";
    private static final String FULL_BY_DISTANT_PROXY_STR = "Full substitution by distant proxy (different process)";
    private static final String PARTIAL_PHYSICAL_CUTOFF_STR = "Partial substitution, physical basis for cut off";
    private static final String PARTIAL_SOCIOECO_CUTOFF_STR = "Partial substitution, socio-economic basis for cut off";

    public static enum WasteTreatmentAlloc
    {
        UNREAD("Unread"),
        Unspecified(UNSPECIFIED_STR),
        Unknown(UNKNOWN_STR),
        NotApplicable(NOT_APPLICABLE_STR),
        ClosedLoopAssumption(CLOSED_LOOP_STR),
        FullByCloseProxy(FULL_BY_CLOSE_PROXY_STR),
        FullByDistantProxy(FULL_BY_DISTANT_PROXY_STR),
        PartialPhysicalCutOff(PARTIAL_PHYSICAL_CUTOFF_STR),
        PartialSocioEconomicCutOff(PARTIAL_SOCIOECO_CUTOFF_STR);

        private final String name;

        private WasteTreatmentAlloc(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static WasteTreatmentAlloc fromString(String str)
        {
            switch (str)
            {
                case UNSPECIFIED_STR:
                    return Unspecified;
                case UNKNOWN_STR:
                    return Unknown;
                case NOT_APPLICABLE_STR:
                    return NotApplicable;
                case CLOSED_LOOP_STR:
                    return ClosedLoopAssumption;
                case FULL_BY_CLOSE_PROXY_STR:
                    return FullByCloseProxy;
                case FULL_BY_DISTANT_PROXY_STR:
                    return FullByDistantProxy;
                case PARTIAL_PHYSICAL_CUTOFF_STR:
                    return PartialPhysicalCutOff;
                case PARTIAL_SOCIOECO_CUTOFF_STR:
                    return PartialSocioEconomicCutOff;
                default:
                    return UNREAD;
            }
        }
    }

    public static final EnumHashBiMap<CutOffRules, String> cutOffRulesMap;
    static
    {
        cutOffRulesMap = EnumHashBiMap.create(CutOffRules.class);
        cutOffRulesMap.put(CutOffRules.Unspecified, "Unspecified");
        cutOffRulesMap.put(CutOffRules.Unknown, "Unknown");
        cutOffRulesMap.put(CutOffRules.NotApplicable, "Not applicable");
        cutOffRulesMap.put(CutOffRules.PhysicalLessThan1, "Less than 1% (physical criteria)");
        cutOffRulesMap.put(CutOffRules.PhysicalLessThan5, "Less than 5% (physical criteria)");
        cutOffRulesMap.put(CutOffRules.SocioEcoLessThan1, "Less than 1% (socio economic)");
        cutOffRulesMap.put(CutOffRules.SocioEcoLessThan5, "Less than 5% (socio economic)");
        cutOffRulesMap.put(CutOffRules.EnvironmentalLessThan1, "Less than 1% (environmental relevance)");
        cutOffRulesMap.put(CutOffRules.EnvironmentalLessThan5, "Less than 5% (environmental relevance)");
    }

    public static enum CutOffRules
    {
        UNREAD,
        Unspecified,
        Unknown,
        NotApplicable,
        PhysicalLessThan1,
        PhysicalLessThan5,
        SocioEcoLessThan1,
        SocioEcoLessThan5,
        EnvironmentalLessThan1,
        EnvironmentalLessThan5;

        @Override
        public String toString()
        {
            return Strings.nullToEmpty(cutOffRulesMap.get(this));
        }

        public static CutOffRules fromString(String str)
        {
            CutOffRules res = cutOffRulesMap.inverse().get(str);
            return res == null ? UNREAD : res;
        }
    }

    private static final String FIRST_ORDER_STR = "First order (only primary flows)";
    private static final String SECOND_ORDER_STR = "Second order (material/energy flows including operations)";
    private static final String THIRD_ORDER_STR = "Third order (including capital goods)";

    public static enum SystemBoundary
    {
        UNREAD("Unread"),
        Unspecified(UNSPECIFIED_STR),
        Unknown(UNKNOWN_STR),
        FirstOrder(FIRST_ORDER_STR),
        SecondOrder(SECOND_ORDER_STR),
        ThirdOrder(THIRD_ORDER_STR);

        private final String name;

        private SystemBoundary(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static SystemBoundary fromString(String str)
        {
            switch (str)
            {
                case UNSPECIFIED_STR:
                    return Unspecified;
                case UNKNOWN_STR:
                    return Unknown;
                case FIRST_ORDER_STR:
                    return FirstOrder;
                case SECOND_ORDER_STR:
                    return SecondOrder;
                case THIRD_ORDER_STR:
                    return ThirdOrder;
                default:
                    return UNREAD;
            }
        }
    }

    private static final String AGRICULTURE_PART_OF_PROD_STR = "Agricultural production is part of production system";
    private static final String AGRICULTURE_PART_OF_NATURE_STR = "Agricultural production is part of natural systems";

    public static enum BoundaryWithNature
    {
        UNREAD("Unread"),
        Unspecified(UNSPECIFIED_STR),
        Unknown(UNKNOWN_STR),
        NotApplicable(NOT_APPLICABLE_STR),
        AgriculturePartOfProduction(AGRICULTURE_PART_OF_PROD_STR),
        AgriculturePartOfNature(AGRICULTURE_PART_OF_NATURE_STR);

        private final String name;

        private BoundaryWithNature(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }

        public static BoundaryWithNature fromString(String str)
        {
            switch (str)
            {
                case UNSPECIFIED_STR:
                    return Unspecified;
                case UNKNOWN_STR:
                    return Unknown;
                case NOT_APPLICABLE_STR:
                    return NotApplicable;
                case AGRICULTURE_PART_OF_PROD_STR:
                    return AgriculturePartOfProduction;
                case AGRICULTURE_PART_OF_NATURE_STR:
                    return AgriculturePartOfNature;
                default:
                    return UNREAD;
            }
        }
    }
}
