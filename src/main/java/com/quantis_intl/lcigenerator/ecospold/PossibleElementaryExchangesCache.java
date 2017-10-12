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

import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Function;

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

    public PossibleElementaryExchangesCache(@Named(StackProperties.SERVER_UPLOADED_FILE_FOLDER)
                                                    String path)
    {
        LOGGER.info("Initializing Elementary exchanges cache");
        possibleExchanges = JAXB.unmarshal(Paths.get(path, "ElementaryExchanges.xml").toFile(),
                                           TValidElementaryExchanges.class)
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
