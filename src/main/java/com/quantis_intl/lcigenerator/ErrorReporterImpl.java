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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

// FIXME: Use codes for errors
public class ErrorReporterImpl implements ErrorReporter
{
    private final Map<String, String> additionalContext;
    private final Collection<ErrorReporterResult> warnings = new ArrayList<>();
    private final Collection<ErrorReporterResult> errors = new ArrayList<>();

    public ErrorReporterImpl()
    {
        additionalContext = ImmutableMap.of();
    }

    public ErrorReporterImpl(Map<String, String> additionalContext)
    {
        this.additionalContext = ImmutableMap.copyOf(additionalContext);
    }

    @Override
    public void warning(Map<String, String> context, String message)
    {
        warnings.add(new ErrorReporterResult("warning", ImmutableMap.<String, String> builder()
                .putAll(additionalContext).putAll(context).build(), message));
    }

    @Override
    public void error(Map<String, String> context, String message)
    {
        errors.add(new ErrorReporterResult("error", ImmutableMap.<String, String> builder().putAll(additionalContext)
                .putAll(context).build(), message));
    }

    @Override
    public ErrorReporter withAdditionalContext(Map<String, String> context)
    {
        return new ErrorReporterImpl(ImmutableMap.<String, String> builder().putAll(additionalContext).putAll(context)
                .build());
    }

    public Collection<ErrorReporterResult> getWarnings()
    {
        return warnings;
    }

    public Collection<ErrorReporterResult> getErrors()
    {
        return errors;
    }

    @Override
    public boolean hasErrors()
    {
        return !errors.isEmpty();
    }

    public static class ErrorReporterResult
    {
        public final String type;

        public final Map<String, String> context;

        public final String message;

        private ErrorReporterResult(String type, Map<String, String> context, String message)
        {
            this.type = type;
            this.context = context;
            this.message = message;
        }

        @Override
        public String toString()
        {
            return message;
        }
    }
}
