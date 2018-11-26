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
import com.quantis_intl.commons.ecospold2.ecospold02.TValidProperties;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidProperty;
import com.quantis_intl.stack.utils.StackProperties;

@Singleton
public class PossiblePropertyCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PossiblePropertyCache.class);

    private final Map<UUID, TValidProperty> possibleProperties;

    @Inject
    public PossiblePropertyCache(@Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER) String path)
    {
        LOGGER.info("Initializing Property cache");
        File f = Paths.get(path, "Properties.xml").toFile();
        if (!f.exists())
        {
            LOGGER.error("File not found: {}", f.getAbsolutePath());
            throw new IllegalStateException("File not found: " + f.getAbsolutePath());
        }
        possibleProperties = Maps.uniqueIndex(JAXB.unmarshal(f, TValidProperties.class)
                                                  .getProperty(), TValidProperty::getId);

    }

    public TValidProperty getProperty(UUID id)
    {
        return possibleProperties.get(id);
    }
}
