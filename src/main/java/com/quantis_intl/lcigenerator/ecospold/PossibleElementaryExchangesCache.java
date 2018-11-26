package com.quantis_intl.lcigenerator.ecospold;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidElementaryExchange;
import com.quantis_intl.commons.ecospold2.ecospold02.TValidElementaryExchanges;
import com.quantis_intl.stack.utils.StackProperties;

@Singleton
public class PossibleElementaryExchangesCache
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleElementaryExchangesCache.class);

    private final Table<UUID, String, TValidElementaryExchange> possibleExchanges;

    @Inject
    public PossibleElementaryExchangesCache(@Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER)
                                                    String path)
    {
        LOGGER.info("Initializing Elementary exchanges cache");
        File f = Paths.get(path, "ElementaryExchanges.xml").toFile();
        if (!f.exists())
        {
            LOGGER.error("File not found: {}", f.getAbsolutePath());
            throw new IllegalStateException("File not found: " + f.getAbsolutePath());
        }
        possibleExchanges = JAXB.unmarshal(f, TValidElementaryExchanges.class)
                                .getElementaryExchange().stream().collect(Tables.toTable
                        ((TValidElementaryExchange e) -> e.getCompartment().getSubcompartmentId(),
                         (TValidElementaryExchange e) -> e.getName().getValue(),
                         Function.identity(), HashBasedTable::create));

    }

    public TValidElementaryExchange getExchange(UUID subcompartmentId, String name)
    {
        return possibleExchanges.get(subcompartmentId, name);
    }
}
