package com.quantis_intl.lcigenerator.ecospold;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.quantis_intl.commons.ecospold2.ecospold02.TGeography;

@Singleton
public class GeographyMappingCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GeographyMappingCache.class);

    //FIXME: Validate
    private static final Map<String, TGeography> GEOGRAPHY_MAPPING = new HashMap<>(1024);

    @Inject
    public GeographyMappingCache()
    {
        try
        {
            for (String l : Resources
                    .readLines(GeographyMappingCache.class.getResource("ecospold_geo_mapping.txt"), Charsets.UTF_8))
            {
                String[] split = l.split("=");
                GEOGRAPHY_MAPPING.put(split[1], TGeography.ofEn(split[0].substring(0, 36), split[0].substring(37)));
            }
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    public TGeography getGeography(String country)
    {
        return GEOGRAPHY_MAPPING.get(country);
    }
}
