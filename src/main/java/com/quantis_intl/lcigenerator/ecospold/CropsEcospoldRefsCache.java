package com.quantis_intl.lcigenerator.ecospold;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;

@Singleton
public class CropsEcospoldRefsCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CropsEcospoldRefsCache.class);

    private final Map<String, String> cropActivityNames;
    private final Map<String, String> cropExchangeNames;

    @Inject
    public CropsEcospoldRefsCache()
    {
        this.cropActivityNames = Maps.fromProperties(
                PropertiesLoader.loadProperties("/ecospold_activity_name_mapping.properties"));
        this.cropExchangeNames = Maps.fromProperties(
                PropertiesLoader.loadProperties("/ecospold_main_output_mapping.properties"));
    }

    public String activityNameOfCrop(String crop)
    {
        return cropActivityNames.get(crop);
    }

    public String exchangeNameOfCrop(String crop)
    {
        return cropExchangeNames.get(crop);
    }
}
