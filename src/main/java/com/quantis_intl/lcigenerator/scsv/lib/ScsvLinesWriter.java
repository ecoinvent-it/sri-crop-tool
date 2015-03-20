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
import java.util.stream.Stream;

public class ScsvLinesWriter
{
    private static final String NEW_LINE = "\r\n";

    private final ScsvLineSerializer serializer;

    private final Writer writer;

    public ScsvLinesWriter(ScsvLineSerializer serializer, Writer writer)
    {
        this.serializer = serializer;
        this.writer = writer;
    }

    public void writeNewLine()
    {
        safeWrite(NEW_LINE);
    }

    public void writeKnownLine(KnownLines line)
    {
        safeWrite(line.asString());
        writeNewLine();
    }

    public void writeSingleField(String field)
    {
        safeWrite(serializer.serializeSingleField(field));
        writeNewLine();
    }

    public void writeMultipleFields(String[] fields)
    {
        safeWrite(serializer.serializeFields(fields));
        writeNewLine();
    }

    public void writeMetadata(String prefix, String metadataField)
    {
        writeSingleField("{" + prefix + metadataField + "}");
    }

    public void writeFragment(KnownLines header, String oneField)
    {
        safeWrite(header.asString());
        writeNewLine();
        writeSingleField(oneField);
        writeNewLine();
    }

    public void writeFragment(KnownLines header, String[] oneLineFields)
    {
        safeWrite(header.asString());
        writeNewLine();
        writeMultipleFields(oneLineFields);
        writeNewLine();
    }

    public void writeFragment(KnownLines header, Stream<String[]> linesOfFields)
    {
        safeWrite(header.asString());
        writeNewLine();
        linesOfFields.forEach(this::writeMultipleFields);
        writeNewLine();
    }

    private void safeWrite(String str)
    {
        try
        {
            writer.write(str);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }
}
