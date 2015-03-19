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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvLineSerializer;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadata;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.CsvFormatVersion;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.CsvSeparator;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.DecimalSeparator;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.ExportType;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.OpenedAsset;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.OpenedAssetType;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.Selection;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.SelectionType;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataEnums.SimaproVersion;
import com.quantis_intl.lcigenerator.scsv.lib.ScsvMetadataWriter;

public class ScsvMetadataWriterTest
{

    @Test
    public void test() throws IOException
    {
        String expectedString = "{Nestor Mk I}\r\n" +
                "{processes}\r\n" +
                "\"{Project: Unit\"\"test}\"\r\n" +
                "{CSV Format version: 7.0.0}\r\n" +
                "{CSV separator: Semicolon}\r\n" +
                "{Decimal separator: .}\r\n" +
                "{Short date format: d/MM/yyyy}\r\n" +
                "{Skip empty fields: No}\r\n" +
                "{Convert expressions to constants: No}\r\n" +
                "{Selection: All (including libraries) (42)}\r\n" +
                "{Related objects (system descriptions, substances, units, etc.): Yes}\r\n" +
                "{Include sub product stages and processes: Yes}\r\n" +
                "\"{Open project: 'Unit\"\"test'}\"\r\n";

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        OutputStreamWriter ow = new OutputStreamWriter(o, Charsets.UTF_8);
        ScsvMetadataWriter writer = new ScsvMetadataWriter(new ScsvLineSerializer(';'));
        writer.write(new ScsvMetadata()
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
            public Optional<String> getProject()
            {
                return Optional.of("Unit\"test");
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
            public String getShortDateFormat()
            {
                return "d/MM/yyyy";
            }

            @Override
            public Optional<Boolean> getSkipEmptyFields()
            {
                return Optional.of(Boolean.FALSE);
            }

            @Override
            public Optional<Boolean> getConvertExpressionsToConstants()
            {
                return Optional.of(Boolean.FALSE);
            }

            @Override
            public Selection getSelection()
            {
                return new Selection(SelectionType.AllIncludingLibraries, 42);
            }

            @Override
            public Optional<Boolean> getRelatedObjects()
            {
                return Optional.of(Boolean.TRUE);
            }

            @Override
            public Optional<Boolean> getIncludeSubProduct()
            {
                return Optional.of(Boolean.TRUE);
            }

            @Override
            public Optional<OpenedAsset> getOpenedAsset()
            {
                return Optional.of(new OpenedAsset(OpenedAssetType.project, "Unit\"test"));
            }

        }, ow);
        ow.flush();

        assertEquals(expectedString, new String(o.toByteArray(), Charsets.UTF_8));
    }
}
