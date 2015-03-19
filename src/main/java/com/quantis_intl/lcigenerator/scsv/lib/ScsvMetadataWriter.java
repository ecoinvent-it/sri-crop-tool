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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.CsvFormatVersion;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.CsvSeparator;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.DecimalSeparator;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.ExportType;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.Selection;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.SelectionType;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.SimaproVersion;

public class ScsvMetadataWriter
{
    private final ScsvLineSerializer serializer;

    public ScsvMetadataWriter(ScsvLineSerializer serializer)
    {
        this.serializer = serializer;
    }

    public void write(ScsvMetadata metadata, Writer writer)
    {
        PerMetadataWriter metadataWriter = new PerMetadataWriter(writer);
        metadataWriter.writeIfDefined("", metadata.getSimaproVersion(),
                SimaproVersion.UNREAD, SimaproVersion.UNREAD_AFTER_8_0_4);
        metadataWriter.writeIfDefined("", metadata.getExportType(), ExportType.UNREAD);
        metadataWriter.writeDateAndTimeIfPresent(metadata.getDateAndTime(), metadata.getDateFormatter());
        metadataWriter.writeIfDefined("Project: ", metadata.getProject());
        metadataWriter.writeIfDefined("CSV Format version: ", metadata.getCsvFormatVersion(), CsvFormatVersion.UNREAD);
        metadataWriter.writeIfDefined("CSV separator: ", metadata.getCsvSeparator(), CsvSeparator.UNREAD);
        metadataWriter.writeIfDefined("Decimal separator: ", metadata.getDecimalSeparator(), DecimalSeparator.UNREAD);
        // TODO: Write date separator
        metadataWriter.writeLine("Short date format: ", metadata.getShortDateFormat());
        metadataWriter.writeIfBooleanDefined("Skip empty fields: ", metadata.getSkipEmptyFields());
        metadataWriter.writeIfBooleanDefined("Convert expressions to constants: ",
                metadata.getConvertExpressionsToConstants());
        metadataWriter.writeSelectionIfDefined(metadata.getSelection());
        metadataWriter.writeIfBooleanDefined("Related objects (system descriptions, substances, units, etc.): ",
                metadata.getRelatedObjects());
        metadataWriter.writeIfBooleanDefined("Include sub product stages and processes: ",
                metadata.getIncludeSubProduct());
        metadataWriter.writeIfDefined("", metadata.getOpenedAsset());
        metadataWriter.writeLibraries(metadata.getLibraries());
    }

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH);

    private class PerMetadataWriter
    {
        private final Writer writer;

        public PerMetadataWriter(Writer writer)
        {
            this.writer = writer;
        }

        public void writeIfBooleanDefined(String prefix, Optional<Boolean> optionalValue)
        {
            writeIfDefined(prefix, ScsvLineSerializer::booleanToString, optionalValue);
        }

        public <T> void writeIfDefined(String prefix, Optional<T> optionalValue)
        {
            writeIfDefined(prefix, T::toString, optionalValue);
        }

        public <T> void writeIfDefined(String prefix, Function<T, String> toStringFunction, Optional<T> optionalValue)
        {
            if (optionalValue.isPresent())
                writeLine(prefix, toStringFunction.apply(optionalValue.get()));
        }

        // TODO: The generics don't work as expected, writeIfDefined("", 1,"") seems to compile...
        public <T> void writeIfDefined(String prefix, T value, T undefinedValue)
        {
            writeIfDefined(prefix, T::toString, value, undefinedValue);
        }

        public <T> void writeIfDefined(String prefix, Function<T, String> toStringFunction, T value, T undefinedValue)
        {
            Objects.requireNonNull(value);
            if (!value.equals(undefinedValue))
                writeLine(prefix, toStringFunction.apply(value));
        }

        public <T> void writeIfDefined(String prefix, T value, T undefinedValue, T secondUndefinedValue)
        {
            if (!Objects.equals(value, secondUndefinedValue))
                writeIfDefined(prefix, value, undefinedValue);
        }

        public void writeLine(String prefix, String value)
        {
            try
            {
                writer.write(serializer.serializeMetadata(prefix + value));
                writer.write("\r\n");
            }
            catch (IOException e)
            {
                throw new UncheckedIOException(e);
            }
        }

        public void writeDateAndTimeIfPresent(Optional<LocalDateTime> date, DateTimeFormatter dateFormatter)
        {
            if (date.isPresent())
            {
                writeLine("Date: ", dateFormatter.format(date.get()));
                writeLine("Time: ", TIME_FORMATTER.format(date.get()));
            }
        }

        public void writeSelectionIfDefined(Selection selection)
        {
            if (!Selection.ABSENT_SELECTION.equals(selection) && selection.type != SelectionType.UNREAD)
                writeLine("Selection: ", selection.toString());
        }

        public void writeLibraries(List<String> libraries)
        {
            libraries.stream().forEach(s -> writeLine("Library '", s + "'"));
        }
    }
}
