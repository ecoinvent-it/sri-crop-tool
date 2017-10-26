/*
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2017 Quantis SARL, All Rights Reserved.
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

package com.quantis_intl.lcigenerator.ecospold;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.io.Resources;
import com.quantis_intl.lcigenerator.imports.PropertiesLoader;
import com.quantis_intl.stack.utils.StackProperties;

@Singleton
public class PossibleActivityLinkCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleActivityLinkCache.class);

    private final String path;
    private final GeographyMappingCache geographyMappingCache;
    private final Map<UUID, Map<String, UUID>> possibleActivityLink;

    @Inject
    public PossibleActivityLinkCache(GeographyMappingCache geographyMappingCache,
                                     @Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER) String path)
    {
        this.geographyMappingCache = geographyMappingCache;
        this.path = path;
        this.possibleActivityLink = new HashMap<>();
    }

    private Map<String, UUID> loadActivityMap(UUID activity)
    {
        File f = Paths.get(path, "ActivityIndex.xml").toFile();
        if (!f.exists())
        {
            LOGGER.error("File not found: {}", f.getAbsolutePath());
            throw new IllegalStateException("File not found: " + f.getAbsolutePath());
        }

        Map<UUID, ActivityIndexEntry> m = JAXB.unmarshal(f, ActivityIndex.class).activityIndexEntry
                .stream().filter(a -> a.activityNameId.equals(activity))
                .collect(Collectors.toMap(a -> a.geographyId, Function.identity(),
                                          (a1, a2) -> a1.startDate.compareTo(a2.startDate) > 0 ? a1 : a2));

        Multimap<UUID, UUID> geoIntersections = null;

        try
        {
            geoIntersections =
                    Resources.readLines(getClass().getResource("ecospold_geography_intersections.csv"), Charsets.UTF_8)
                             .stream()
                             .map(s -> s.substring(1, s.length() - 1))
                             .map(s -> s.split("\";\""))
                             .filter(a -> geographyMappingCache.getGeography(a[0]) != null &&
                                     geographyMappingCache.getGeography(a[1]) != null)
                             .collect(Multimaps.toMultimap(a -> geographyMappingCache.getGeography(a[0])
                                                                                     .getGeographyId(),
                                                           a -> geographyMappingCache.getGeography(a[1])
                                                                                     .getGeographyId(),
                                                           () -> ArrayListMultimap.create()));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }

        Properties countries = PropertiesLoader.loadProperties("/countries.properties");

        Map<String, UUID> res = Maps.newHashMapWithExpectedSize(countries.size());
        for (String k : countries.stringPropertyNames())
        {
            List<UUID> geoCandidates = new ArrayList<>();
            UUID geo = null;
            geoCandidates.add(geographyMappingCache.getGeography(k).getGeographyId());
            while (!geoCandidates.isEmpty() && !m.containsKey(geo = geoCandidates.remove(0)))
                geoCandidates.addAll(geoIntersections.get(geo));

            if (!m.containsKey(geo))
            {
                geo = UUID.fromString("34dbbff8-88ce-11de-ad60-0019e336be3a"); //GLO
                if (!m.containsKey(geo))
                    throw new IllegalStateException("No compatible geography for dataset " + activity.toString());
            }

            res.put(k, geo);
        }

        return res;
    }

    public UUID getActivityLink(UUID activity, String geography)
    {
        return possibleActivityLink.computeIfAbsent(activity, this::loadActivityMap).get(geography);
    }

    @XmlRootElement
    private static class ActivityIndex
    {
        public List<ActivityIndexEntry> activityIndexEntry;
    }

    @XmlRootElement
    private static class ActivityIndexEntry
    {
        @XmlAttribute
        public UUID id;

        @XmlAttribute
        public UUID activityNameId;

        @XmlAttribute
        public UUID geographyId;

        @XmlAttribute
        public String startDate;

        @XmlAttribute
        public String endDate;
    }
}
