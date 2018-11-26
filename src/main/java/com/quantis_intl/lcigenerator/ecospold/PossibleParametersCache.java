package com.quantis_intl.lcigenerator.ecospold;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidParameter;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidParameters;
import com.quantis_intl.stack.utils.StackProperties;

public class PossibleParametersCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleParametersCache.class);

    private final Map<String, TValidParameter> possibleParameters;

    @Inject
    public PossibleParametersCache(@Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER) String path)
    {
        LOGGER.info("Initializing Parameters cache");
        File f = Paths.get(path, "Parameters.xml").toFile();
        if (!f.exists())
        {
            LOGGER.error("File not found: {}", f.getAbsolutePath());
            throw new IllegalStateException("File not found: " + f.getAbsolutePath());
        }

        possibleParameters = Maps.uniqueIndex(JAXB.unmarshal(f, TValidParameters.class).getParameter(),
                                              vp -> vp.getName().getValue());

    }

    public TValidParameter getParameter(String name)
    {
        return possibleParameters.get(name);
    }
}
