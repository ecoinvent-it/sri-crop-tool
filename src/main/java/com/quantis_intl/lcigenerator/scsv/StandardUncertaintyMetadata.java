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
package com.quantis_intl.lcigenerator.scsv;

public enum StandardUncertaintyMetadata
{
    SEEDS("1.0714", "(2,1,1,1,1,na)"),
    FERTILISERS("1.0714", "(2,1,1,1,1,na)"),
    PESTICIDES_MANUFACTURING("1.0744", "(2,2,1,1,1,na)"),
    PESTICIDES_EMISSION_TO_SOIL("1.2090", "(2,2,1,1,1,na)"),
    LAND_OCCUPATION("1.1130", "(2,1,1,1,1,na)"),
    LAND_TRANSFORMATION("1.2077", "(2,1,1,1,1,na)"),
    ENERGY_CARRIERS_FUEL_WORK("1.0714", "(2,1,1,1,1,na)"),
    ELECTRICITY("1.0714", "(2,1,1,1,1,na)"),
    IRRIGATION_WATER("1.0714", "(2,1,1,1,1,na)"),
    CO2_ENERGY_BIOMASS("1.0744", "(2,2,1,1,1,na)"),
    CO2_EMISSIONS("1.0714", "(2,2,1,1,1,na)"),
    WATER_EMISSIONS("1.0744", "(2,2,1,1,1,na)"),
    CO("5.0043", "(2,2,1,1,1,na)"),
    CH4_NH3_TO_AIR("1.2090", "(2,2,1,1,1,na)"),
    N2O_NOX_TO_AIR("1.4057", "(2,2,1,1,1,na)"),
    NO3_PO4_TO_WATER("1.5051", "(2,2,1,1,1,na)"),
    HM_TO_WATER("1.8042", "(2,2,1,1,1,na)"),
    HM_TO_SOIL("1.5051", "(2,2,1,1,1,na)"),
    UTILITIES_FUELS("1.0714", "(2,1,1,1,1,na)"),
    UTILITIES_ELECTRICITY("1.0714", "(2,1,1,1,1,na)"),
    UTILITIES_WATER("1.0714", "(2,1,1,1,1,na)"),
    OTHER_MATERIALS("1.0744", "(2,2,1,1,1,na)"),
    OTHER_GREENHOUSES("1.0744", "(2,2,1,1,1,na)"),
    WASTE_MANAGEMENT("1.2090", "(4,2,1,1,1,na)"),
    COD_IN_WASTEWATER("1.5605", "(4,2,1,1,1,na)");

    public final String standardDeviation;
    public final String pedigreeMatrix;

    private StandardUncertaintyMetadata(String standardDeviation, String pedigreeMatrix)
    {
        this.standardDeviation = standardDeviation;
        this.pedigreeMatrix = pedigreeMatrix;
    }
}
