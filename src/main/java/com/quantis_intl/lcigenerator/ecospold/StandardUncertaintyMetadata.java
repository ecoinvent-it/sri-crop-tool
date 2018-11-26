package com.quantis_intl.lcigenerator.ecospold;

import com.quantis_intl.commons.ecospold2.ecospold02.TUncertainty;

public enum StandardUncertaintyMetadata
{
    SEEDS(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    FERTILISERS(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    PESTICIDES_MANUFACTURING(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.000587459301620604, 0.0012874593016206),
    PESTICIDES_EMISSION_TO_SOIL(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.00830539995827002,
                                0.00900539995827002),
    LAND_OCCUPATION(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.00226541124008129, 0.00286541124008129),
    LAND_TRANSFORMATION(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.00830359486616652, 0.00890359486616651),
    ENERGY_CARRIERS_FUEL_WORK(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482,
                              0.00118908433956748),
    ELECTRICITY(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    IRRIGATION_WATER(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    CO2_ENERGY_BIOMASS(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.000587459301620604, 0.0012874593016206),
    CO2_EMISSIONS(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.000489084339567482, 0.00118908433956748),
    WATER_EMISSIONS(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.000587459301620604, 0.0012874593016206),
    CO(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.647564544123952, 0.648264544123952),
    CH4_NH3_TO_AIR(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.00830539995827002, 0.00900539995827002),
    N2O_NOX_TO_AIR(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.0282910895535666, 0.0289910895535666),
    NO3_PO4_TO_WATER(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.041091490216755, 0.041791490216755),
    HM_TO_WATER(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.0863596011095821, 0.0870596011095821),
    HM_TO_SOIL(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.041091490216755, 0.041791490216755),
    UTILITIES_FUELS(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    UTILITIES_ELECTRICITY(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    UTILITIES_WATER(new TUncertainty.PedigreeMatrix(2, 1, 1, 1, 1), 0.000589084339567482, 0.00118908433956748),
    OTHER_MATERIALS(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.000587459301620604, 0.0012874593016206),
    OTHER_GREENHOUSES(new TUncertainty.PedigreeMatrix(2, 2, 1, 1, 1), 0.000587459301620604, 0.0012874593016206),
    WASTE_MANAGEMENT(new TUncertainty.PedigreeMatrix(4, 2, 1, 1, 1), 0.000905399958270018, 0.00900539995827002),
    COD_IN_WASTEWATER(new TUncertainty.PedigreeMatrix(4, 2, 1, 1, 1), 0.0414076479170032, 0.0495076479170032);

    public final TUncertainty.PedigreeMatrix pedigreeMatrix;
    public final double variance;
    public final double varianceWithPedigreeUncertainty;

    private StandardUncertaintyMetadata(TUncertainty.PedigreeMatrix pedigreeMatrix, double variance,
                                        double varianceWithPedigreeUncertainty)
    {
        this.pedigreeMatrix = pedigreeMatrix;
        this.variance = variance;
        this.varianceWithPedigreeUncertainty = varianceWithPedigreeUncertainty;
    }
}
