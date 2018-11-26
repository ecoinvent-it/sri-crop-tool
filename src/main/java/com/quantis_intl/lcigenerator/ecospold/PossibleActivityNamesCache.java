package com.quantis_intl.lcigenerator.ecospold;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidActivityName;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidActivityNames;
import com.quantis_intl.stack.utils.StackProperties;

@Singleton
public class PossibleActivityNamesCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleActivityNamesCache.class);

    private final Map<String, TValidActivityName> possibleActivityNames;

    @Inject
    public PossibleActivityNamesCache(@Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER) String path)
    {
        LOGGER.info("Initializing activity name cache");
        File f = Paths.get(path, "ActivityNames.xml").toFile();
        if (!f.exists())
        {
            LOGGER.error("File not found: {}", f.getAbsolutePath());
            throw new IllegalStateException("File not found: " + f.getAbsolutePath());
        }

        possibleActivityNames = Maps.uniqueIndex(JAXB.unmarshal(f, TValidActivityNames.class)
                                                     .getActivityName(), ve -> ve.getName().getValue());

    }

    public UUID getActivityUUID(String name)
    {
        TValidActivityName n = possibleActivityNames.get(name);
        if (n == null)
            return null;
        return n.getId();
    }
}
