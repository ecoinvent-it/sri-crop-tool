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
