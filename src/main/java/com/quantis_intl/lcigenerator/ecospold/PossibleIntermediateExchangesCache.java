package com.quantis_intl.lcigenerator.ecospold;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidIntermediateExchange;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidIntermediateExchanges;
import com.quantis_intl.stack.utils.StackProperties;

@Singleton
public class PossibleIntermediateExchangesCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleIntermediateExchangesCache.class);

    private final Map<String, TValidIntermediateExchange> possibleExchanges;

    @Inject
    public PossibleIntermediateExchangesCache(@Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER) String path)
    {
        LOGGER.info("Initializing Intermediate exchanges cache");
        File f = Paths.get(path, "IntermediateExchanges.xml").toFile();
        if (!f.exists())
        {
            LOGGER.error("File not found: {}", f.getAbsolutePath());
            throw new IllegalStateException("File not found: " + f.getAbsolutePath());
        }

        possibleExchanges = Maps.uniqueIndex(JAXB.unmarshal(f, TValidIntermediateExchanges.class)
                                                 .getIntermediateExchange(), ve -> ve.getName().getValue());

    }

    public TValidIntermediateExchange getExchange(String name)
    {
        return possibleExchanges.get(name);
    }
}
