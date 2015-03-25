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
package com.quantis_intl.lcigenerator.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.ErrorReporterImpl;
import com.quantis_intl.lcigenerator.PyBridgeService;
import com.quantis_intl.lcigenerator.ScsvFileWriter;
import com.quantis_intl.lcigenerator.imports.ExcelInputReader;
import com.quantis_intl.lcigenerator.imports.RawInputLine;
import com.quantis_intl.lcigenerator.imports.RawInputToPyCompatibleConvertor;

@Path("pub/")
public class Api
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);

    @Inject
    private ExcelInputReader inputReader;

    @Inject
    private PyBridgeService pyBridgeService;

    @Inject
    private ScsvFileWriter scsvFileWriter;

    @GET
    @Path("computeLci")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    // TODO: Return a file to download
    public void computeQuestionnaire(@Suspended AsyncResponse response) throws URISyntaxException, IOException
    {
        // FIXME: Tmp
        InputStream is = Api.class
                .getResourceAsStream("/LCI-Database_Data-collection_Crop_2015-03-20.xlsx");

        ErrorReporter errorReporter = new ErrorReporterImpl();
        Map<String, RawInputLine> extractedInputs = inputReader.getInputDataFromFile(is, errorReporter);

        // FIXME: Define what to do if errors are found
        if (!errorReporter.hasErrors())
        {
            RawInputToPyCompatibleConvertor inputConvertor = new RawInputToPyCompatibleConvertor(errorReporter);
            Map<String, Object> validatedData = inputConvertor.getValidatedData(extractedInputs, errorReporter);
            // FIXME: Define what to do if errors are found
            if (!errorReporter.hasErrors())
                pyBridgeService.callComputeLci(validatedData, result -> onResult(result, response),
                        error -> onError(error, response));
        }
    }

    private void onResult(Map<String, String> modelsOutput, AsyncResponse response)
    {
        response.resume(Response.ok(
                (StreamingOutput) outputStream ->
                scsvFileWriter.writeModelsOutputToScsvFile(modelsOutput, outputStream)).build());
    }

    private void onError(Throwable error, AsyncResponse response)
    {
        LOGGER.warn("Error using pyBridge", error);
        response.resume(error);
    }

}
