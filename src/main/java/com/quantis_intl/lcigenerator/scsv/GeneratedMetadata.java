/***************************************************************************
 * Quantis Sàrl CONFIDENTIAL
 * Unpublished Copyright (c) 2009-2014 Quantis SARL, All Rights Reserved.
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
package com.quantis_intl.lcigenerator.scsv;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.quantis_intl.commons.scsv.ScsvBoolean;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadata;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataEnums.CsvFormatVersion;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataEnums.CsvSeparator;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataEnums.DecimalSeparator;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataEnums.ExportType;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataEnums.SimaproVersion;

public class GeneratedMetadata implements ScsvMetadata
{
    @Override
    public SimaproVersion getSimaproVersion()
    {
        return SimaproVersion.Nestor_mkI;
    }

    @Override
    public ExportType getExportType()
    {
        return ExportType.processes;
    }

    @Override
    public Optional<LocalDateTime> getDateAndTime()
    {
        return Optional.of(LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    public CsvFormatVersion getCsvFormatVersion()
    {
        return CsvFormatVersion._7_0_0;
    }

    @Override
    public CsvSeparator getCsvSeparator()
    {
        return CsvSeparator.Semicolon;
    }

    @Override
    public DecimalSeparator getDecimalSeparator()
    {
        return DecimalSeparator.Dot;
    }

    @Override
    public Optional<ScsvBoolean> getSkipEmptyFields()
    {
        return Optional.of(ScsvBoolean.No);
    }

    @Override
    public Optional<ScsvBoolean> getConvertExpressionsToConstants()
    {
        return Optional.of(ScsvBoolean.No);
    }

    @Override
    public Optional<ScsvBoolean> getRelatedObjects()
    {
        // TODO: The user should be able the choice with or without
        return Optional.of(ScsvBoolean.No);
    }

    @Override
    public Optional<ScsvBoolean> getIncludeSubProduct()
    {
        // TODO: The user should be able the choice with or without
        return Optional.of(ScsvBoolean.No);
    }

    @Override
    public List<String> getLibraries()
    {
        return ImmutableList.of("Ecoinvent unit processes");
    }
}
