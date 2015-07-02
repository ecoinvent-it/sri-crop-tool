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
package com.quantis_intl.lcigenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import com.quantis_intl.commons.scsv.ScsvLineSerializer;
import com.quantis_intl.commons.scsv.ScsvLinesWriter;
import com.quantis_intl.commons.scsv.metadata.ScsvMetadataWriter;
import com.quantis_intl.commons.scsv.processes.ScsvProcessWriter;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.scsv.GeneratedMetadata;
import com.quantis_intl.lcigenerator.scsv.GeneratedProcess;

public class ScsvFileWriter
{
    private final ScsvLineSerializer serializer;

    private final ScsvMetadataWriter metadataWriter;

    public ScsvFileWriter()
    {
        this.serializer = new ScsvLineSerializer(';');
        this.metadataWriter = new ScsvMetadataWriter();
    }

    public void writeModelsOutputToScsvFile(Map<String, String> modelsOutput, ValueGroup extractedInputs,
            OutputStream os)
    {
        try
        {
            writeModelsOutputToScsvFile(modelsOutput, extractedInputs,
                    new BufferedWriter(new OutputStreamWriter(os, Charset.forName("windows-1252"))));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    private void writeModelsOutputToScsvFile(Map<String, String> modelsOutput, ValueGroup extractedInputs, Writer writer)
            throws IOException
    {
        ScsvLinesWriter linesWriter = new ScsvLinesWriter(serializer, writer);
        GeneratedMetadata generatedMetadata = new GeneratedMetadata();
        metadataWriter.write(generatedMetadata, linesWriter);
        linesWriter.writeNewLine();
        ScsvProcessWriter processWriter = new ScsvProcessWriter(linesWriter, generatedMetadata.getDateFormatter());
        processWriter.write(new GeneratedProcess(modelsOutput, extractedInputs));

        writer.flush();
    }
}
