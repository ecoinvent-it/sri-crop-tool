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
package com.quantis_intl.lcigenerator.scsv.lib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.CsvFormatVersion;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.CsvSeparator;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.DecimalSeparator;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.ExportType;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.OpenedAsset;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.Selection;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.SimaproVersion;

// FIXME: Put this in a separated projet, and merge with DBMT
public interface ScsvMetadata
{
    default SimaproVersion getSimaproVersion()
    {
        return SimaproVersion.UNREAD;
    }

    default ExportType getExportType()
    {
        return ExportType.UNREAD;
    }

    default String getShortDateFormat()
    {
        return "M/d/yyyy";
    }

    default Optional<LocalDateTime> getDateAndTime()
    {
        return Optional.empty();
    }

    default Optional<String> getProject()
    {
        return Optional.empty();
    }

    default CsvFormatVersion getCsvFormatVersion()
    {
        return CsvFormatVersion.UNREAD;
    }

    default CsvSeparator getCsvSeparator()
    {
        return CsvSeparator.UNREAD;
    }

    default DecimalSeparator getDecimalSeparator()
    {
        return DecimalSeparator.UNREAD;
    }

    default Optional<Boolean> getSkipEmptyFields()
    {
        return Optional.empty();
    }

    default Optional<Boolean> getConvertExpressionsToConstants()
    {
        return Optional.empty();
    }

    default Selection getSelection()
    {
        return Selection.ABSENT_SELECTION;
    }

    default Optional<Boolean> getRelatedObjects()
    {
        return Optional.empty();
    }

    default Optional<Boolean> getIncludeSubProduct()
    {
        return Optional.empty();
    }

    default Optional<OpenedAsset> getOpenedAsset()
    {
        return Optional.empty();
    }

    default List<String> getLibraries()
    {
        return Collections.emptyList();
    }

    default DateTimeFormatter getDateFormatter()
    {
        return DateTimeFormatter.ofPattern(getShortDateFormat(), Locale.ENGLISH);
    }
}
