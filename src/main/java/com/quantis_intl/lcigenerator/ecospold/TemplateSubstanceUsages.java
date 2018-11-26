package com.quantis_intl.lcigenerator.ecospold;

import java.util.UUID;

public interface TemplateSubstanceUsages
{
    TemplateSubstanceUsage[] getResources();

    TemplateSubstanceUsage[] getToAir();

    TemplateSubstanceUsage[] getToWater();

    TemplateSubstanceUsage[] getToSoil();

    public static class TemplateSubstanceUsage
    {
        public final String name;
        public final UUID subCompartment;
        public final AvailableUnit unit;
        public final String amountVariable;
        public final StandardUncertaintyMetadata uncertainty;
        public final String commentVariable;

        public TemplateSubstanceUsage(String name, UUID subCompartment, AvailableUnit unit, String amountVariable,
                                      StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            this.name = name;
            this.subCompartment = subCompartment;
            this.unit = unit;
            this.amountVariable = amountVariable;
            this.uncertainty = uncertainty;
            this.commentVariable = commentVariable;
        }
    }
}
