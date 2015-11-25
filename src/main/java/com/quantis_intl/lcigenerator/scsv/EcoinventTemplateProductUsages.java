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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class EcoinventTemplateProductUsages implements TemplateProductUsages
{
    @Override
    public TemplateProductUsage[] getMaterialsFuels()
    {
        return materialsFuels;
    }

    @Override
    public TemplateProductUsage[] getElectricityHeat()
    {
        return electricityHeat;
    }

    @Override
    public TemplateProductUsage[] getWastes()
    {
        return wastes;
    }

    private static Map<String, String> PHOTOVOLTAIC_REMAP;
    private static ItemOfGroupedTemplate DRIP_CH = ItemOfGroupedTemplate.of("CH",
            "Irrigating, drip, electricity powered (ALCIG)/CH U", "irrigation_drip_CH.csv");
    private static List<ItemOfGroupedTemplate> IRR_DRIP_MAP = ImmutableList.of(
            ItemOfGroupedTemplate
                    .of("AR", "Irrigating, drip, electricity powered (ALCIG)/AR U",
                            ImmutableList.of("irrigation_drip_AR.csv", "electricity_AR.csv")),
            ItemOfGroupedTemplate
                    .of("BE", "Irrigating, drip, electricity powered (ALCIG)/BE U", "irrigation_drip_BE.csv"),
            ItemOfGroupedTemplate
                    .of("BR", "Irrigating, drip, electricity powered (ALCIG)/BR U", "irrigation_drip_BR.csv"),
            ItemOfGroupedTemplate
                    .of("CA", "Irrigating, drip, electricity powered (ALCIG)/CA U",
                            ImmutableList.of("irrigation_drip_CA.csv", "electricity_CA.csv")),
            DRIP_CH,
            ItemOfGroupedTemplate
                    .of("CI", "Irrigating, drip, electricity powered (ALCIG)/CI U",
                            ImmutableList.of("irrigation_drip_CI.csv", "electricity_CI.csv")),
            ItemOfGroupedTemplate
                    .of("CL", "Irrigating, drip, electricity powered (ALCIG)/CL U",
                            ImmutableList.of("irrigation_drip_CL.csv", "electricity_CL.csv")),
            ItemOfGroupedTemplate
                    .of("CN", "Irrigating, drip, electricity powered (ALCIG)/CN U", "irrigation_drip_CN.csv"),
            ItemOfGroupedTemplate
                    .of("CR", "Irrigating, drip, electricity powered (ALCIG)/CR U",
                            ImmutableList.of("irrigation_drip_CR.csv", "electricity_CR.csv")),
            ItemOfGroupedTemplate
                    .of("DE", "Irrigating, drip, electricity powered (ALCIG)/DE U", "irrigation_drip_DE.csv"),
            ItemOfGroupedTemplate
                    .of("EC", "Irrigating, drip, electricity powered (ALCIG)/EC U",
                            ImmutableList.of("irrigation_drip_EC.csv", "electricity_EC.csv")),
            ItemOfGroupedTemplate
                    .of("ES", "Irrigating, drip, electricity powered (ALCIG)/ES U", "irrigation_drip_ES.csv"),
            ItemOfGroupedTemplate
                    .of("FI", "Irrigating, drip, electricity powered (ALCIG)/FI U", "irrigation_drip_FI.csv"),
            ItemOfGroupedTemplate
                    .of("FR", "Irrigating, drip, electricity powered (ALCIG)/FR U", "irrigation_drip_FR.csv"),
            ItemOfGroupedTemplate
                    .of("GH", "Irrigating, drip, electricity powered (ALCIG)/GH U",
                            ImmutableList.of("irrigation_drip_GH.csv", "electricity_GH.csv")),
            ItemOfGroupedTemplate
                    .of("HU", "Irrigating, drip, electricity powered (ALCIG)/HU U", "irrigation_drip_HU.csv"),
            ItemOfGroupedTemplate
                    .of("ID", "Irrigating, drip, electricity powered (ALCIG)/ID U",
                            ImmutableList.of("irrigation_drip_ID.csv", "electricity_ID.csv")),
            ItemOfGroupedTemplate
                    .of("IN", "Irrigating, drip, electricity powered (ALCIG)/IN U",
                            ImmutableList.of("irrigation_drip_IN.csv", "electricity_IN.csv")),
            ItemOfGroupedTemplate
                    .of("IT", "Irrigating, drip, electricity powered (ALCIG)/IT U", "irrigation_drip_IT.csv"),
            ItemOfGroupedTemplate
                    .of("MX", "Irrigating, drip, electricity powered (ALCIG)/MX U",
                            ImmutableList.of("irrigation_drip_MX.csv", "electricity_MX.csv")),
            ItemOfGroupedTemplate
                    .of("NL", "Irrigating, drip, electricity powered (ALCIG)/NL U", "irrigation_drip_NL.csv"),
            ItemOfGroupedTemplate
                    .of("PH", "Irrigating, drip, electricity powered (ALCIG)/PH U",
                            ImmutableList.of("irrigation_drip_PH.csv", "electricity_PH.csv")),
            ItemOfGroupedTemplate
                    .of("PL", "Irrigating, drip, electricity powered (ALCIG)/PL U", "irrigation_drip_PL.csv"),
            ItemOfGroupedTemplate
                    .of("TR", "Irrigating, drip, electricity powered (ALCIG)/TR U",
                            ImmutableList.of("irrigation_drip_TR.csv", "electricity_TR.csv")),
            ItemOfGroupedTemplate
                    .of("US", "Irrigating, drip, electricity powered (ALCIG)/US U", "irrigation_drip_US.csv"));

    private static ItemOfGroupedTemplate SURFACE_CH = ItemOfGroupedTemplate.of("CH",
            "Irrigating, surface, electricity powered (ALCIG)/CH U", "irrigation_surface_CH.csv");
    private static List<ItemOfGroupedTemplate> IRR_SURFACE_MAP = ImmutableList.of(
            ItemOfGroupedTemplate
                    .of("AR", "Irrigating, surface, electricity powered (ALCIG)/AR U",
                            ImmutableList.of("irrigation_surface_AR.csv", "electricity_AR.csv")),
            ItemOfGroupedTemplate
                    .of("BE", "Irrigating, surface, electricity powered (ALCIG)/BE U", "irrigation_surface_BE.csv"),
            ItemOfGroupedTemplate
                    .of("BR", "Irrigating, surface, electricity powered (ALCIG)/BR U", "irrigation_surface_BR.csv"),
            ItemOfGroupedTemplate
                    .of("CA", "Irrigating, surface, electricity powered (ALCIG)/CA U",
                            ImmutableList.of("irrigation_surface_CA.csv", "electricity_CA.csv")),
            SURFACE_CH,
            ItemOfGroupedTemplate
                    .of("CI", "Irrigating, surface, electricity powered (ALCIG)/CI U",
                            ImmutableList.of("irrigation_surface_CI.csv", "electricity_CI.csv")),
            ItemOfGroupedTemplate
                    .of("CL", "Irrigating, surface, electricity powered (ALCIG)/CL U",
                            ImmutableList.of("irrigation_surface_CL.csv", "electricity_CL.csv")),
            ItemOfGroupedTemplate
                    .of("CN", "Irrigating, surface, electricity powered (ALCIG)/CN U", "irrigation_surface_CN.csv"),
            ItemOfGroupedTemplate
                    .of("CO", "Irrigating, surface, electricity powered (ALCIG)/CO U",
                            ImmutableList.of("irrigation_surface_CO.csv", "electricity_CO.csv")),
            ItemOfGroupedTemplate
                    .of("CR", "Irrigating, surface, electricity powered (ALCIG)/CR U",
                            ImmutableList.of("irrigation_surface_CR.csv", "electricity_CR.csv")),
            ItemOfGroupedTemplate
                    .of("EC", "Irrigating, surface, electricity powered (ALCIG)/EC U",
                            ImmutableList.of("irrigation_surface_EC.csv", "electricity_EC.csv")),
            ItemOfGroupedTemplate
                    .of("ES", "Irrigating, surface, electricity powered (ALCIG)/ES U", "irrigation_surface_ES.csv"),
            ItemOfGroupedTemplate
                    .of("FR", "Irrigating, surface, electricity powered (ALCIG)/FR U", "irrigation_surface_FR.csv"),
            ItemOfGroupedTemplate
                    .of("GH", "Irrigating, surface, electricity powered (ALCIG)/GH U",
                            ImmutableList.of("irrigation_surface_GH.csv", "electricity_GH.csv")),
            ItemOfGroupedTemplate
                    .of("HU", "Irrigating, surface, electricity powered (ALCIG)/HU U", "irrigation_surface_HU.csv"),
            ItemOfGroupedTemplate
                    .of("ID", "Irrigating, surface, electricity powered (ALCIG)/ID U",
                            ImmutableList.of("irrigation_surface_ID.csv", "electricity_ID.csv")),
            ItemOfGroupedTemplate
                    .of("IN", "Irrigating, surface, electricity powered (ALCIG)/IN U",
                            ImmutableList.of("irrigation_surface_IN.csv", "electricity_IN.csv")),
            ItemOfGroupedTemplate
                    .of("IT", "Irrigating, surface, electricity powered (ALCIG)/IT U", "irrigation_surface_IT.csv"),
            ItemOfGroupedTemplate
                    .of("MX", "Irrigating, surface, electricity powered (ALCIG)/MX U",
                            ImmutableList.of("irrigation_surface_MX.csv", "electricity_MX.csv")),
            ItemOfGroupedTemplate
                    .of("PH", "Irrigating, surface, electricity powered (ALCIG)/PH U",
                            ImmutableList.of("irrigation_surface_PH.csv", "electricity_PH.csv")),
            ItemOfGroupedTemplate
                    .of("TR", "Irrigating, surface, electricity powered (ALCIG)/TR U",
                            ImmutableList.of("irrigation_surface_TR.csv", "electricity_TR.csv")),
            ItemOfGroupedTemplate
                    .of("US", "Irrigating, surface, electricity powered (ALCIG)/US U", "irrigation_surface_US.csv"),
            ItemOfGroupedTemplate
                    .of("VN", "Irrigating, surface, electricity powered (ALCIG)/VN U",
                            ImmutableList.of("irrigation_surface_VN.csv", "electricity_VN.csv")));

    private static ItemOfGroupedTemplate SPRINKLER_CH = ItemOfGroupedTemplate.of("CH",
            "Irrigating, sprinkler, electricity powered (ALCIG)/CH U", "irrigation_sprinkler_CH.csv");
    private static List<ItemOfGroupedTemplate> IRR_SPRINKLER_MAP = ImmutableList.of(
            ItemOfGroupedTemplate
                    .of("AR", "Irrigating, sprinkler, electricity powered (ALCIG)/AR U",
                            ImmutableList.of("irrigation_sprinkler_AR.csv", "electricity_AR.csv")),
            ItemOfGroupedTemplate
                    .of("BE", "Irrigating, sprinkler, electricity powered (ALCIG)/BE U",
                            "irrigation_sprinkler_BE.csv"),
            ItemOfGroupedTemplate
                    .of("BR", "Irrigating, sprinkler, electricity powered (ALCIG)/BR U",
                            "irrigation_sprinkler_BR.csv"),
            ItemOfGroupedTemplate
                    .of("CA", "Irrigating, sprinkler, electricity powered (ALCIG)/CA U",
                            ImmutableList.of("irrigation_sprinkler_CA.csv", "electricity_CA.csv")),
            SPRINKLER_CH,
            ItemOfGroupedTemplate
                    .of("CI", "Irrigating, sprinkler, electricity powered (ALCIG)/CI U",
                            ImmutableList.of("irrigation_sprinkler_CI.csv", "electricity_CI.csv")),
            ItemOfGroupedTemplate
                    .of("CL", "Irrigating, sprinkler, electricity powered (ALCIG)/CL U",
                            ImmutableList.of("irrigation_sprinkler_CL.csv", "electricity_CL.csv")),
            ItemOfGroupedTemplate
                    .of("CN", "Irrigating, sprinkler, electricity powered (ALCIG)/CN U",
                            "irrigation_sprinkler_CN.csv"),
            ItemOfGroupedTemplate
                    .of("CO", "Irrigating, sprinkler, electricity powered (ALCIG)/CO U",
                            ImmutableList.of("irrigation_sprinkler_CO.csv", "electricity_CO.csv")),
            ItemOfGroupedTemplate
                    .of("CR", "Irrigating, sprinkler, electricity powered (ALCIG)/CR U",
                            ImmutableList.of("irrigation_sprinkler_CR.csv", "electricity_CR.csv")),
            ItemOfGroupedTemplate
                    .of("DE", "Irrigating, sprinkler, electricity powered (ALCIG)/DE U",
                            "irrigation_sprinkler_DE.csv"),
            ItemOfGroupedTemplate
                    .of("EC", "Irrigating, sprinkler, electricity powered (ALCIG)/EC U",
                            ImmutableList.of("irrigation_sprinkler_EC.csv", "electricity_EC.csv")),
            ItemOfGroupedTemplate
                    .of("ES", "Irrigating, sprinkler, electricity powered (ALCIG)/ES U",
                            "irrigation_sprinkler_ES.csv"),
            ItemOfGroupedTemplate
                    .of("FI", "Irrigating, sprinkler, electricity powered (ALCIG)/FI U",
                            "irrigation_sprinkler_FI.csv"),
            ItemOfGroupedTemplate
                    .of("FR", "Irrigating, sprinkler, electricity powered (ALCIG)/FR U",
                            "irrigation_sprinkler_FR.csv"),
            ItemOfGroupedTemplate
                    .of("GH", "Irrigating, sprinkler, electricity powered (ALCIG)/GH U",
                            ImmutableList.of("irrigation_sprinkler_GH.csv", "electricity_GH.csv")),
            ItemOfGroupedTemplate
                    .of("HU", "Irrigating, sprinkler, electricity powered (ALCIG)/HU U",
                            "irrigation_sprinkler_HU.csv"),
            ItemOfGroupedTemplate
                    .of("ID", "Irrigating, sprinkler, electricity powered (ALCIG)/ID U",
                            ImmutableList.of("irrigation_sprinkler_ID.csv", "electricity_ID.csv")),
            ItemOfGroupedTemplate
                    .of("IN", "Irrigating, sprinkler, electricity powered (ALCIG)/IN U",
                            ImmutableList.of("irrigation_sprinkler_IN.csv", "electricity_IN.csv")),
            ItemOfGroupedTemplate
                    .of("IT", "Irrigating, sprinkler, electricity powered (ALCIG)/IT U",
                            "irrigation_sprinkler_IT.csv"),
            ItemOfGroupedTemplate
                    .of("MX", "Irrigating, sprinkler, electricity powered (ALCIG)/MX U",
                            ImmutableList.of("irrigation_sprinkler_MX.csv", "electricity_MX.csv")),
            ItemOfGroupedTemplate
                    .of("NL", "Irrigating, sprinkler, electricity powered (ALCIG)/NL U",
                            "irrigation_sprinkler_NL.csv"),
            ItemOfGroupedTemplate
                    .of("PH", "Irrigating, sprinkler, electricity powered (ALCIG)/PH U",
                            ImmutableList.of("irrigation_sprinkler_PH.csv", "electricity_PH.csv")),
            ItemOfGroupedTemplate
                    .of("PL", "Irrigating, sprinkler, electricity powered (ALCIG)/PL U",
                            "irrigation_sprinkler_PL.csv"),
            ItemOfGroupedTemplate
                    .of("RU", "Irrigating, sprinkler, electricity powered (ALCIG)/RU U",
                            ImmutableList.of("irrigation_sprinkler_RU.csv", "electricity_RU.csv")),
            ItemOfGroupedTemplate
                    .of("TR", "Irrigating, sprinkler, electricity powered (ALCIG)/TR U",
                            ImmutableList.of("irrigation_sprinkler_TR.csv", "electricity_TR.csv")),
            ItemOfGroupedTemplate
                    .of("US", "Irrigating, sprinkler, electricity powered (ALCIG)/US U",
                            "irrigation_sprinkler_US.csv"));

    private static ItemOfGroupedTemplate ELEC_LOW_CH = ItemOfGroupedTemplate.of("CH",
            "Electricity, low voltage, at grid/CH U");
    private static List<ItemOfGroupedTemplate> ELEC_LOW_MAP = ImmutableList.of(
            ItemOfGroupedTemplate
                    .of("AR", "Electricity, low voltage, production AR, at grid (ALCIG)/AR U", "electricity_AR.csv"),
            ItemOfGroupedTemplate.of("BE", "Electricity, low voltage, at grid/BE U"),
            ItemOfGroupedTemplate.of("BR", "Electricity, low voltage, at grid/BR U"),
            ItemOfGroupedTemplate
                    .of("CA", "Electricity, low voltage, production CA, at grid (ALCIG)/CA U", "electricity_CA.csv"),
            ELEC_LOW_CH,
            ItemOfGroupedTemplate
                    .of("CI", "Electricity, low voltage, production CI, at grid (ALCIG)/CI U", "electricity_CI.csv"),
            ItemOfGroupedTemplate
                    .of("CL", "Electricity, low voltage, production CL, at grid (ALCIG)/CL U", "electricity_CL.csv"),
            ItemOfGroupedTemplate.of("CN", "Electricity, low voltage, at grid/CN U"),
            ItemOfGroupedTemplate
                    .of("CO", "Electricity, low voltage, production CO, at grid (ALCIG)/CO U", "electricity_CO.csv"),
            ItemOfGroupedTemplate
                    .of("CR", "Electricity, low voltage, production CR, at grid (ALCIG)/CR U", "electricity_CR.csv"),
            ItemOfGroupedTemplate.of("DE", "Electricity, low voltage, at grid/DE U"),
            ItemOfGroupedTemplate
                    .of("EC", "Electricity, low voltage, production EC, at grid (ALCIG)/EC U", "electricity_EC.csv"),
            ItemOfGroupedTemplate.of("ES", "Electricity, low voltage, at grid/ES U"),
            ItemOfGroupedTemplate.of("FI", "Electricity, low voltage, at grid/FI U"),
            ItemOfGroupedTemplate.of("FR", "Electricity, low voltage, at grid/FR U"),
            ItemOfGroupedTemplate
                    .of("GH", "Electricity, low voltage, production GH, at grid (ALCIG)/GH U", "electricity_GH.csv"),
            ItemOfGroupedTemplate.of("HU", "Electricity, low voltage, at grid/BE U"),
            ItemOfGroupedTemplate
                    .of("ID", "Electricity, low voltage, production ID, at grid (ALCIG)/ID U", "electricity_ID.csv"),
            ItemOfGroupedTemplate
                    .of("IN", "Electricity, low voltage, production IN, at grid (ALCIG)/IN U", "electricity_IN.csv"),
            ItemOfGroupedTemplate.of("IT", "Electricity, low voltage, at grid/IT U"),
            ItemOfGroupedTemplate
                    .of("MX", "Electricity, low voltage, production MX, at grid (ALCIG)/MX U", "electricity_MX.csv"),
            ItemOfGroupedTemplate.of("NL", "Electricity, low voltage, at grid/NL U"),
            ItemOfGroupedTemplate
                    .of("PH", "Electricity, low voltage, production PH, at grid (ALCIG)/PH U", "electricity_PH.csv"),
            ItemOfGroupedTemplate.of("PL", "Electricity, low voltage, at grid/BE U"),
            ItemOfGroupedTemplate
                    .of("RU", "Electricity, low voltage, production RU, at grid (ALCIG)/RU U", "electricity_RU.csv"),
            ItemOfGroupedTemplate
                    .of("TR", "Electricity, low voltage, production TR, at grid (ALCIG)/TR U", "electricity_TR.csv"),
            ItemOfGroupedTemplate.of("US", "Electricity, low voltage, at grid/BE U"),
            ItemOfGroupedTemplate
                    .of("VN", "Electricity, low voltage, production VN, at grid (ALCIG)/VN U", "electricity_VN.csv"));

    private static ItemOfGroupedTemplate SEEDLINGS_CH = ItemOfGroupedTemplate.of("CH",
            "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv"));

    private static List<ItemOfGroupedTemplate> SEEDLINGS_MAP = ImmutableList.of(
            ItemOfGroupedTemplate
                    .of("AR", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("AU", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("BE", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("BR", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("CA", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            SEEDLINGS_CH,
            ItemOfGroupedTemplate
                    .of("CI", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("CL", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("CN", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("CO", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("CR", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("DE", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("EC", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("ES", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("FI", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("FR", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("GH", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("HU", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("ID", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("IL", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("IN", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("IT", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("KE", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("LK", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("MX", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("NL", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("NZ", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("PE", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("PH", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("PL", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("RU", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("TH", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("TR", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"),
            ItemOfGroupedTemplate
                    .of("UA", "Seedling, greenhouse, heated, at nursery (ALCIG)/GLO U",
                            ImmutableList.of("seedlings_heated.csv", "irrigation_drip_NL.csv")),
            ItemOfGroupedTemplate
                    .of("US", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("VN", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/GLO U", "seedlings_GLO.csv"),
            ItemOfGroupedTemplate
                    .of("ZA", "Seedling, greenhouse, non-heated, at nursery (ALCIG)/RER U", "seedlings_RER.csv"));

    static
    {
        ImmutableMap.Builder<String, String> photoBuilder = ImmutableMap.builder();
        photoBuilder.put("AR", "US");
        photoBuilder.put("BR", "US");
        photoBuilder.put("CL", "US");
        photoBuilder.put("CN", "TR");
        photoBuilder.put("CO", "US");
        photoBuilder.put("CR", "US");
        photoBuilder.put("CI", "TR");
        photoBuilder.put("EC", "US");
        photoBuilder.put("GH", "TR");
        photoBuilder.put("IN", "TR");
        photoBuilder.put("ID", "TR");
        photoBuilder.put("IL", "TR");
        photoBuilder.put("KE", "TR");
        photoBuilder.put("MX", "US");
        photoBuilder.put("PE", "US");
        photoBuilder.put("PH", "AU");
        photoBuilder.put("PL", "DE");
        photoBuilder.put("RU", "DE");
        photoBuilder.put("ZA", "AU");
        photoBuilder.put("LK", "TR");
        photoBuilder.put("TH", "TR");
        photoBuilder.put("UA", "DE");
        photoBuilder.put("VN", "TR");
        PHOTOVOLTAIC_REMAP = photoBuilder.build();
    }

    private static final TemplateProductUsage[] materialsFuels = {
            // NOTE: There is one process per crop. No other process is added (rooting trees, oilseed processing, etc)
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_almond",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_apple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_apricot",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new GroupedTemplateProductUsage("p", "seeds_asparagus",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings", "country", SEEDLINGS_CH, SEEDLINGS_MAP),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_banana",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Potato seed IP, at regional storehouse/CH U", "kg", "seeds_carrot",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_cocoa",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_coconut",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_coffee",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_lemonlime",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Grass seed IP, at regional storehouse/CH U", "kg", "seeds_linseed",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Maize seed IP, at regional storehouse/CH U", "kg", "seeds_maizegrain",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_mandarin",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new GroupedTemplateProductUsage("p", "seeds_mint",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings", "country", SEEDLINGS_CH, SEEDLINGS_MAP),
            new TemplateProductUsage("Rye seed IP, at regional storehouse/CH U", "kg", "seeds_oat",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_olive",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new GroupedTemplateProductUsage("p", "seeds_onion",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings", "country", SEEDLINGS_CH, SEEDLINGS_MAP),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_orange",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_palmtree",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_peach",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Potato seed IP, at regional storehouse/CH U", "kg", "seeds_peanut",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_pear",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "seeds_pineapple",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Potato seed IP, at regional storehouse/CH U", "kg", "seeds_potato",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Rape seed IP, at regional storehouse/CH U", "kg", "seeds_rapeseed",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Rice seed, at regional storehouse/US U", "kg", "seeds_rice",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Pea seed IP, at regional storehouse/CH U", "kg", "seeds_soybean",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new GroupedTemplateProductUsage("p", "seeds_strawberry",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings", "country", SEEDLINGS_CH, SEEDLINGS_MAP),
            new TemplateProductUsage("Sugar beet seed IP, at regional storehouse/CH U", "kg",
                    "seeds_sugarbeet", StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_sugarcane",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new TemplateProductUsage("Rape seed IP, at regional storehouse/CH U", "kg", "seeds_sunflower",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Maize seed IP, at regional storehouse/CH U", "kg", "seeds_sweetcorn",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ", "seeds_tea",
                    StandardUncertaintyMetadata.SEEDS, "nb_planted_trees", 0.45 * 42.8),
            new GroupedTemplateProductUsage("p", "seeds_tomato",
                    StandardUncertaintyMetadata.SEEDS, "nb_seedlings", "country", SEEDLINGS_CH, SEEDLINGS_MAP),
            new TemplateProductUsage("Wheat seed IP, at regional storehouse/CH U", "kg", "seeds_wheat",
                    StandardUncertaintyMetadata.SEEDS, "seeds"),

            new GroupedTemplateProductUsage("m3", "surface_irrigation_electricity",
                    StandardUncertaintyMetadata.ELECTRICITY, "irr_surface_electricity", "country", SURFACE_CH,
                    IRR_SURFACE_MAP),
            new TemplateProductUsage("Irrigating, surface, diesel powered (ALCIG)/GLO U", "m3",
                    "surface_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_surface_diesel",
                    ImmutableList.of("irrigation_surface_diesel.csv", "irrigation_diesel_bg.csv")),
            new TemplateProductUsage("Irrigating, surface, gravity (ALCIG)/GLO U", "m3",
                    "surface_irrigation_no_energy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_surface_no_energy", ImmutableList.of("irrigation_surface_gravity.csv")),
            new GroupedTemplateProductUsage("m3", "sprinkler_irrigation_electricity",
                    StandardUncertaintyMetadata.ELECTRICITY, "irr_sprinkler_electricity", "country", SPRINKLER_CH,
                    IRR_SPRINKLER_MAP),
            new TemplateProductUsage("Irrigating, sprinkler, diesel powered (ALCIG)/GLO U", "m3",
                    "sprinkler_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_sprinkler_diesel", ImmutableList.of("irrigation_sprinkler_diesel.csv",
                            "irrigation_diesel_bg.csv")),
            new GroupedTemplateProductUsage("m3", "drip_irrigation_electricity",
                    StandardUncertaintyMetadata.ELECTRICITY, "irr_drip_electricity", "country", DRIP_CH, IRR_DRIP_MAP),
            new TemplateProductUsage("Irrigating, drip, diesel powered (ALCIG)/GLO U", "m3",
                    "drip_irrigation_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "irr_drip_diesel", ImmutableList.of("irrigation_drip_diesel.csv", "irrigation_diesel_bg.csv")),

            new TemplateProductUsage("Tap water, at user/RER U", "ton", "wateruse_non_conventional_sources",
                    StandardUncertaintyMetadata.IRRIGATION_WATER, "wateruse_non_conventional_sources"),

            new TemplateProductUsage("Ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonium_nitrate"),
            new TemplateProductUsage("Urea, as N, at regional storehouse/RER U", "kg", "fert_n_urea",
                    StandardUncertaintyMetadata.FERTILISERS, "fertnmin_urea"),
            new TemplateProductUsage("Urea ammonium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ureaAN", StandardUncertaintyMetadata.FERTILISERS, "fertnmin_urea_an"),
            new TemplateProductUsage("Monoammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_mono_ammonium_phosphate"),
            new TemplateProductUsage("Diammonium phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_di_ammonium_phosphate"),
            new TemplateProductUsage("Ammonium nitrate phosphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertnmin_an_phosphate"),
            new TemplateProductUsage("Calcium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_lime_ammonium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_lime_ammonium_nitrate"),
            new TemplateProductUsage("Ammonium sulphate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_ammonium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonium_sulphate"),
            new TemplateProductUsage("Potassium nitrate, as N, at regional storehouse/RER U", "kg",
                    "fert_n_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_potassium_nitrate"),
            new TemplateProductUsage("Ammonia, liquid, at regional storehouse/RER U", "kg",
                    "fert_n_ammonia_liquid_as_nh3", StandardUncertaintyMetadata.FERTILISERS,
                    "fertnmin_ammonia_liquid"),

            new TemplateProductUsage("Triple superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_triple_superphosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_triple_superphosphate"),
            new TemplateProductUsage("Single superphosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_superphosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_superphosphate"),
            new TemplateProductUsage("Monoammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_mono_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_mono_ammonium_phosphate"),
            new TemplateProductUsage("Diammonium phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_di_ammonium_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_di_ammonium_phosphate"),
            new TemplateProductUsage("Ammonium nitrate phosphate, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_an_phosphate", StandardUncertaintyMetadata.FERTILISERS, "fertpmin_an_phosphate"),
            new TemplateProductUsage("Phosphate rock, as P2O5, beneficiated, dry, at plant/MA U", "kg",
                    "fert_p_hypophosphate_raw_phosphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_hypophosphate_raw_phosphate"),
            new TemplateProductUsage("Thomas meal, as P2O5, at regional storehouse/RER U", "kg",
                    "fert_p_ground_basic_slag", StandardUncertaintyMetadata.FERTILISERS,
                    "fertpmin_ground_basic_slag"),

            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_salt", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_salt_kcl"),
            new TemplateProductUsage("Potassium sulphate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_sulphate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_sulphate_k2so4"),
            new TemplateProductUsage("Potassium nitrate, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_potassium_nitrate", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_potassium_nitrate"),
            new TemplateProductUsage("Potassium chloride, as K2O, at regional storehouse/RER U", "kg",
                    "fert_k_patent_potassium", StandardUncertaintyMetadata.FERTILISERS,
                    "fertkmin_patent_potassium"),

            new TemplateProductUsage("Magnesium sulphate, at plant/RER U", "kg", "magnesium_from_fertilizer_as_mgso4",
                    StandardUncertaintyMetadata.FERTILISERS, "fert_other_total_mg"),
            new TemplateProductUsage("Dolomite, at plant/RER U", "kg", "magnesium_from_fertilizer_as_dolomite",
                    StandardUncertaintyMetadata.FERTILISERS, "fert_other_total_mg"),
            new TemplateProductUsage("Limestone, milled, packed, at plant/CH U", "kg", "fert_ca_limestone_as_limestone",
                    StandardUncertaintyMetadata.FERTILISERS, "fertotherca_limestone"),
            new TemplateProductUsage("Lime, from carbonation, at regional storehouse/CH U", "kg",
                    "fert_ca_carbonation_limestone_as_limestone", StandardUncertaintyMetadata.FERTILISERS,
                    "fertotherca_carbonation_limestone"),
            new TemplateProductUsage("Lime, algae, at regional storehouse/CH U", "kg",
                    "fert_ca_seaweed_limestone_as_seaweed_lime",
                    StandardUncertaintyMetadata.FERTILISERS, "fertotherca_seaweed_limestone"),

            new TemplateProductUsage("Compost, at plant/CH U", "kg", "composttype_compost",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_compost"),
            new TemplateProductUsage("Vinasse, at fermentation plant/CH U", "kg", "composttype_vinasse",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_vinasse"),
            new TemplateProductUsage("Poultry manure, dried, at regional storehouse/CH U", "kg",
                    "composttype_dried_poultry_manure", StandardUncertaintyMetadata.FERTILISERS,
                    "composttype_dried_poultry_manure"),
            new TemplateProductUsage("Stone meal, at regional storehouse/CH U", "kg", "composttype_stone_meal",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_stone_meal"),
            new TemplateProductUsage("Horn meal, at regional storehouse/CH U", "kg", "composttype_horn_meal",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_meal"),
            new TemplateProductUsage("Horn meal, at regional storehouse/CH U", "kg",
                    "composttype_horn_shavings_fine",
                    StandardUncertaintyMetadata.FERTILISERS, "composttype_horn_shavings_fine"),

            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "remains_machinery_diesel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "", 42.8),
            new TemplateProductUsage("Application of plant protection products, by field sprayer/CH U", "ha",
                    "plantprotection_spraying", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_spraying"),
            new TemplateProductUsage("Natural gas, burned in boiler modulating <100kW/RER U", "MJ",
                    "plantprotection_flaming", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_flaming"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "plantprotection_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "plantprotection_other", 42.8),
            new TemplateProductUsage(
                    "Diesel, burned in building machine/GLO U"/*"Soil decompactation, with 4.5m chisel/FR U"*/,
                    "MJ", "soilcultivation_decompactation", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_decompactation", 42.8),
            new TemplateProductUsage("Tillage, cultivating, chiselling/CH U", "ha",
                    "soilcultivation_tillage_chisel", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_chisel"),
            new TemplateProductUsage("Tillage, currying, by weeder/CH U", "ha",
                    "soilcultivation_tillage_spring_tine_weeder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_spring_tine_weeder"),
            new TemplateProductUsage("Tillage, harrowing, by rotary harrow/CH U", "ha",
                    "soilcultivation_tillage_rotary_harrow", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_rotary_harrow"),
            new TemplateProductUsage("Tillage, harrowing, by spring tine harrow/CH U", "ha",
                    "soilcultivation_tillage_sprint_tine_harrow",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_sprint_tine_harrow"),
            new TemplateProductUsage("Tillage, hoeing and earthing-up, potatoes/CH U", "ha",
                    "soilcultivation_tillage_hoeing_earthing_up",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_hoeing_earthing_up"),
            new TemplateProductUsage("Tillage, ploughing/CH U", "ha", "soilcultivation_tillage_plough",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_plough"),
            new TemplateProductUsage("Tillage, rolling/CH U", "ha", "soilcultivation_tillage_roll",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "soilcultivation_tillage_roll"),
            new TemplateProductUsage("Tillage, rotary cultivator/CH U", "ha",
                    "soilcultivation_tillage_rotary_cultivator", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_tillage_rotary_cultivator"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "soilcultivation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "soilcultivation_other", 42.8),
            new TemplateProductUsage("Sowing/CH U", "ha", "sowingplanting_sowing",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_sowing"),
            new TemplateProductUsage("Planting/CH U", "ha", "sowingplanting_planting_seedlings",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_seedlings"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "sowingplanting_planting_trees",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_trees", 42.8),
            new TemplateProductUsage("Potato planting/CH U", "ha", "sowingplanting_planting_potatoes",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "sowingplanting_planting_potatoes"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "sowingplanting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "sowingplanting_other", 42.8),
            new TemplateProductUsage("Fertilising, by broadcaster/CH U", "ha",
                    "fertilisation_fertilizing_broadcaster",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "fertilisation_fertilizing_broadcaster"),
            new TemplateProductUsage("Slurry spreading, by vacuum tanker/CH U", "m3",
                    "fertilisation_liquid_manure_vacuum_tanker", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_liquid_manure_vacuum_tanker"),
            new TemplateProductUsage("Solid manure loading and spreading, by hydraulic loader and spreader/CH U",
                    "ton",
                    "fertilisation_solid_manure", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_solid_manure"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "fertilisation_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "fertilisation_other", 42.8),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "harvesting_chopping_maize",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_chopping_maize"),
            new TemplateProductUsage("Combine harvesting/CH U", "ha", "harvesting_threshing_combine_harvester",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_threshing_combine_harvester"),
            new TemplateProductUsage("Fodder loading, by self-loading trailer/CH U", "m3",
                    "harvesting_picking_up_forage_self_propelled_loader",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_picking_up_forage_self_propelled_loader"),
            new TemplateProductUsage("Harvesting, by complete harvester, beets/CH U", "ha",
                    "harvesting_beets_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_beets_complete_havester"),
            new TemplateProductUsage("Harvesting, by complete harvester, potatoes/CH U", "ha",
                    "harvesting_potatoes_complete_havester", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "harvesting_potatoes_complete_havester"),
            new TemplateProductUsage("Haying, by rotary tedder/CH U", "ha",
                    "harvesting_making_hay_rotary_tedder",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_making_hay_rotary_tedder"),
            new TemplateProductUsage("Loading bales/CH U", "unit", "harvesting_loading_bales",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_loading_bales"),
            new TemplateProductUsage("Mowing, by motor mower/CH U", "ha", "harvesting_mowing_motor_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_mowing_motor_mower"),
            new TemplateProductUsage("Mowing, by rotary mower/CH U", "ha", "harvesting_mowing_rotary_mower",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_mowing_rotary_mower"),
            new TemplateProductUsage("Potato haulm cutting/CH U", "ha", "harvesting_removing_potatoes_haulms",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_removing_potatoes_haulms"),
            new TemplateProductUsage("Swath, by rotary windrower/CH U", "ha",
                    "harvesting_windrowing_rotary_swather",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_windrowing_rotary_swather"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "harvesting_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "harvesting_other",
                    42.8),

            new TemplateProductUsage("Baling/CH U", "unit", "otherworkprocesses_baling",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_baling"),
            new TemplateProductUsage("Chopping, maize/CH U", "ha", "otherworkprocesses_chopping",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_chopping"),
            new TemplateProductUsage("Mulching/CH U", "ha", "otherworkprocesses_mulching",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "otherworkprocesses_mulching"),
            new TemplateProductUsage("Transport, tractor and trailer/CH U", "tkm",
                    "otherworkprocesses_transport_tractor_trailer",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "otherworkprocesses_transport_tractor_trailer"),
            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "otherworkprocesses_other", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "otherworkprocesses_other", 42.8),

            new TemplateProductUsage("Diesel, burned in building machine/GLO U", "MJ",
                    "energy_diesel_excluding_diesel_used_in_tractor",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_diesel_excluding_diesel_used_in_tractor", 42.8),
            new TemplateProductUsage("Lignite briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_lignite_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_lignite_briquette"),
            new TemplateProductUsage("Hard coal briquette, burned in stove 5-15kW/RER U", "MJ",
                    "energy_hard_coal_briquette", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_hard_coal_briquette"),
            new TemplateProductUsage("Light fuel oil, burned in industrial furnace 1MW, non-modulating/RER U",
                    "MJ",
                    "energy_fuel_oil_light", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_light"),
            new TemplateProductUsage("Heavy fuel oil, burned in industrial furnace 1MW, non-modulating/RER U",
                    "MJ",
                    "energy_fuel_oil_heavy", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_fuel_oil_heavy"),
            new TemplateProductUsage("Natural gas, burned in industrial furnace >100kW/RER U", "MJ",
                    "energy_natural_gas", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_natural_gas"),
            new TemplateProductUsage("Pellets, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_pellets_humidity_10_percent", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_wood_pellets_humidity_10_percent"),
            new TemplateProductUsage("Wood chips, from industry, mixed, burned in furnace 50kW/CH U", "MJ",
                    "energy_wood_chips_fresh_humidity_40_percent",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_wood_chips_fresh_humidity_40_percent"),
            new TemplateProductUsage("Logs, mixed, burned in furnace 100kW/CH U", "MJ", "energy_wood_logs",
                    StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK, "energy_wood_logs"),

            new TemplateProductUsage("Tap water, at user/RER U", "kg",
                    "utilities_wateruse_non_conventional_sources",
                    StandardUncertaintyMetadata.UTILITIES_WATER, "utilities_wateruse_non_conventional_sources"),

            new TemplateProductUsage("Polypropylene, granulate, at plant/RER U", "kg", "materials_fleece",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_fleece"),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg",
                    "materials_silage_foil",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_silage_foil"),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg",
                    "materials_covering_sheet",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_covering_sheet"),
            new TemplateProductUsage("Polyethylene, HDPE, granulate, at plant/RER U", "kg", "materials_bird_net",
                    StandardUncertaintyMetadata.OTHER_MATERIALS, "materials_bird_net"),

            new TemplateProductUsage("Plastic tunnel (ALCIG)/FR U", "m2", "greenhouse_plastic_tunnel",
                    StandardUncertaintyMetadata.OTHER_GREENHOUSES, "greenhouse_plastic_tunnel",
                    ImmutableList.of("plastic_tunnel.csv")),
            new TemplateProductUsage("Greenhouse, glass walls and roof, metal tubes (ALCIG)/FR U", "m2",
                    "greenhouse_glass_roof_metal_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_glass_roof_metal_tubes", ImmutableList.of("greenhouse_glass_metal.csv")),
            new TemplateProductUsage("Greenhouse, glass walls and roof, plastic tubes (ALCIG)/FR U", "m2",
                    "greenhouse_glass_roof_plastic_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_glass_roof_plastic_tubes", ImmutableList.of("greenhouse_glass_plastic.csv")),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, metal tubes (ALCIG)/FR U", "m2",
                    "greenhouse_plastic_roof_metal_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_plastic_roof_metal_tubes", ImmutableList.of("greenhouse_plastic_metal.csv")),
            new TemplateProductUsage("Greenhouse, plastic walls and roof, plastic tubes (ALCIG)/FR U", "m2",
                    "greenhouse_plastic_roof_plastic_tubes", StandardUncertaintyMetadata.OTHER_GREENHOUSES,
                    "greenhouse_plastic_roof_plastic_tubes", ImmutableList.of("greenhouse_plastic_plastic.csv")),

            new TemplateProductUsage(
                    "Agricultural biomass, heavy metals uptake, per kg dry matter harvested (ALCIG)/GLO U", "kg",
                    "hm_uptake_formula", StandardUncertaintyMetadata.HM_TO_SOIL, "", ImmutableList.of("hmuptake.csv")),

            new LucTemplateProductUsage("ha", "luc_formula", StandardUncertaintyMetadata.LAND_TRANSFORMATION, ""),

            new TemplateProductUsage("Transport, van <3.5t/CH U", "tkm", "ecoinvent_transport_van_inf_3_5t",
                    StandardUncertaintyMetadata.TRANSPORTS, ""),
            new TemplateProductUsage("Transport, lorry >16t, fleet average/RER U", "tkm",
                    "ecoinvent_transport_lorry_sup_16t", StandardUncertaintyMetadata.TRANSPORTS, ""),
            new TemplateProductUsage("Transport, barge/RER U", "tkm",
                    "ecoinvent_transport_barge", StandardUncertaintyMetadata.TRANSPORTS, ""),
            new TemplateProductUsage("Transport, freight, rail/CH U", "tkm", "ecoinvent_transport_freight_rail",
                    StandardUncertaintyMetadata.TRANSPORTS, ""),

            new TemplateProductUsage("Packaging, per kg dry fertilisers or pesticides (ALCIG)/GLO U", "kg",
                    "ecoinvent_packaging_solid", StandardUncertaintyMetadata.OTHER_MATERIALS, "",
                    ImmutableList.of("packaging_solid.csv")),
            new TemplateProductUsage("Packaging, per kg liquid fertilisers or pesticides (ALCIG)/GLO U", "kg",
                    "ecoinvent_packaging_liquid", StandardUncertaintyMetadata.OTHER_MATERIALS, "",
                    ImmutableList.of("packaging_liquid.csv")),

            new TemplateProductUsage("Lubricating oil, at plant/RER U", "kg", "pest_horticultural_oil",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, "pest_horticultural_oil"),

            new TemplateProductUsage("Pesticide unspecified, at regional storehouse/RER U", "g", "pest_remains",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from pesticides, unspecified (ALCIG)/GLO S", "g", "pest_remains",
                    StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, "",
                    ImmutableList.of("pesticides_undefined_to_soil.csv")),
            new TemplateProductUsage("Herbicides, at regional storehouse/RER U", "g", "remains_herbicides",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from herbicides, unspecified (ALCIG)/GLO S", "g",
                    "remains_herbicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, "",
                    ImmutableList.of("herbicides_undefined_to_soil.csv")),
            new TemplateProductUsage("Fungicides, at regional storehouse/RER U", "g", "remains_fungicides",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from fungicides, unspecified (ALCIG)/GLO S", "g",
                    "remains_fungicides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, "",
                    ImmutableList.of("fungicides_undefined_to_soil.csv")),
            new TemplateProductUsage("Insecticides, at regional storehouse/RER U", "g", "remains_insecticides",
                    StandardUncertaintyMetadata.PESTICIDES_MANUFACTURING, ""),
            new TemplateProductUsage("Emissions from insecticides, unspecified (ALCIG)/GLO S", "g",
                    "remains_insecticides", StandardUncertaintyMetadata.PESTICIDES_EMISSION_TO_SOIL, "",
                    ImmutableList.of("insecticides_undefined_to_soil.csv"))
    };

    private static final TemplateProductUsage[] electricityHeat = {
            new GroupedTemplateProductUsage("kWh",
                    "energy_electricity_low_voltage_at_grid", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_low_voltage_at_grid", "country", ELEC_LOW_CH, ELEC_LOW_MAP),
            new WithLookupTemplateProductUsage("Electricity, production mix photovoltaic, at plant/{country} U", "kWh",
                    "energy_electricity_photovoltaic_produced_locally", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_photovoltaic_produced_locally", buildBiFun(PHOTOVOLTAIC_REMAP)),
            new TemplateProductUsage("Electricity, at wind power plant/RER U", "kWh",
                    "energy_electricity_wind_power_produced_locally", StandardUncertaintyMetadata.ELECTRICITY,
                    "energy_electricity_wind_power_produced_locally"),
            new TemplateProductUsage("Heat, natural gas, at industrial furnace >100kW/RER U", "MJ",
                    "energy_heat_district_heating", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_district_heating"),
            new TemplateProductUsage("Heat, at flat plate collector, multiple dwelling, for hot water/CH U",
                    "MJ",
                    "energy_heat_solar_collector", StandardUncertaintyMetadata.ENERGY_CARRIERS_FUEL_WORK,
                    "energy_heat_solar_collector")
    };

    private static final TemplateProductUsage[] wastes = {
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to sanitary landfill/CH U", "kg",
                    "eol_plastic_landfill", StandardUncertaintyMetadata.WASTE_MANAGEMENT, "eol_landfill"),
            new TemplateProductUsage("Disposal, polyethylene, 0.4% water, to municipal incineration/CH U", "kg",
                    "eol_plastic_incineration", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                    "eol_incineration"),
            new TemplateProductUsage("Treatment, sewage, to wastewater treatment, class 4/CH U", "m3",
                    "eol_waste_water_to_treatment_facility", StandardUncertaintyMetadata.WASTE_MANAGEMENT,
                    "eol_waste_water_to_treatment_facility"),
    };

    private static class GroupedTemplateProductUsage extends TemplateProductUsage
    {
        private final String key;
        private final Map<String, ItemOfGroupedTemplate> items;
        private final ItemOfGroupedTemplate defaultItem;

        public GroupedTemplateProductUsage(String unit, String amountVariable, StandardUncertaintyMetadata uncertainty,
                String commentVariable, String key, ItemOfGroupedTemplate defaultItem,
                List<ItemOfGroupedTemplate> items)
        {
            super("", unit, amountVariable, uncertainty, commentVariable);
            this.key = key;
            this.defaultItem = defaultItem;
            this.items = Maps.uniqueIndex(items, i -> i.key);
        }

        @Override
        public String provideName(Map<String, String> modelOutputs)
        {
            String discriminent = modelOutputs.get(key);
            ItemOfGroupedTemplate item = items.getOrDefault(discriminent, defaultItem);
            return item.name;
        }

        @Override
        public Optional<List<String>> provideRequiredDep(Map<String, String> modelOutputs)
        {
            String discriminent = modelOutputs.get(key);
            ItemOfGroupedTemplate item = items.getOrDefault(discriminent, defaultItem);
            return item.requiredDep;
        }
    }

    public static class ItemOfGroupedTemplate
    {
        public final String key;
        public final String name;
        public final Optional<List<String>> requiredDep;

        public static ItemOfGroupedTemplate of(String key, String name)
        {
            return new ItemOfGroupedTemplate(key, name, Optional.empty());
        }

        public static ItemOfGroupedTemplate of(String key, String name, String requiredDep)
        {
            return of(key, name, ImmutableList.of(requiredDep));
        }

        public static ItemOfGroupedTemplate of(String key, String name, List<String> requiredDep)
        {
            return new ItemOfGroupedTemplate(key, name, Optional.of(requiredDep));
        }

        private ItemOfGroupedTemplate(String key, String name, Optional<List<String>> requiredDep)
        {
            this.key = key;
            this.name = name;
            this.requiredDep = requiredDep;
        }
    }

    private static class WithLookupTemplateProductUsage extends TemplateProductUsage
    {
        private final BiFunction<Map<String, String>, String, String> variableResolver;

        public WithLookupTemplateProductUsage(String name, String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable,
                BiFunction<Map<String, String>, String, String> variableResolver)
        {
            super(name, unit, amountVariable, uncertainty, commentVariable);
            this.variableResolver = variableResolver;
        }

        protected String lookupVariable(Map<String, String> modelOutputs, String variable)
        {
            return variableResolver.apply(modelOutputs, variable);
        }
    }

    private static class LucTemplateProductUsage extends TemplateProductUsage
    {
        public LucTemplateProductUsage(String unit, String amountVariable,
                StandardUncertaintyMetadata uncertainty, String commentVariable)
        {
            super("", unit, amountVariable, uncertainty, commentVariable);
        }

        @Override
        public String provideName(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");

            return "Land use change, " + cropType + " crop (ALCIG)/" + country + " U";
        }

        @Override
        public Optional<List<String>> provideRequiredDep(Map<String, String> modelOutputs)
        {
            String cropType = modelOutputs.get("luc_crop_type");
            String country = modelOutputs.get("country");
            return Optional.of(ImmutableList.of("luc_" + cropType + "_" + country + ".csv", "luc_bg_" + country
                    + ".csv"));
        }
    }

    private static BiFunction<Map<String, String>, String, String> buildBiFun(Map<String, String> lookupMap)
    {
        return (map, var) ->
            {
                String res = map.get(var);
                return lookupMap.getOrDefault(res, res);
            };
    }

}
