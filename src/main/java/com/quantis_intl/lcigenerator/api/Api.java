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
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.shiro.SecurityUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.ErrorReporterImpl;
import com.quantis_intl.lcigenerator.ImportResult;
import com.quantis_intl.lcigenerator.PyBridgeService;
import com.quantis_intl.lcigenerator.ScsvFileWriter;
import com.quantis_intl.lcigenerator.imports.ExcelInputReader;
import com.quantis_intl.lcigenerator.imports.ValueGroup;

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

    @POST
    @Path("computeLci")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public void computeQuestionnaire(@FormDataParam("uploadFile") InputStream is,
            @FormDataParam("canBeStored") boolean canBeStored, @Suspended AsyncResponse response)
            throws URISyntaxException, IOException
    {
        ErrorReporter errorReporter = new ErrorReporterImpl();

        // TODO: Store file if it can be stored
        ValueGroup extractedInputs = inputReader.getInputDataFromFile(is, errorReporter);

        if (!errorReporter.hasErrors())
        {
            // RawInputToPyCompatibleConvertor inputConvertor = new RawInputToPyCompatibleConvertor(errorReporter);
            Map<String, Object> validatedData = extractedInputs.flattenValues();

            if (!errorReporter.hasErrors())
                pyBridgeService.callComputeLci(validatedData,
                        result -> onResult(result, errorReporter, response),
                        error -> onError(error, response));
            else
            {
                LOGGER.warn("Bad content in input file");
                response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
            }
        }
        else
        {
            LOGGER.warn("Bad template for input file");
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
        }
    }

    private void onResult(Map<String, String> modelsOutput, ErrorReporter errorReporter,
            AsyncResponse response)
    {
        String idResult = UUID.randomUUID().toString();
        SecurityUtils.getSubject().getSession().setAttribute(idResult, modelsOutput);
        response.resume(Response.ok(new ImportResult(errorReporter, idResult)).build());
    }

    private void onError(Throwable error, AsyncResponse response)
    {
        LOGGER.warn("Error using pyBridge", error);
        response.resume(error);
    }

    @POST
    @Path("generateScsv")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateScsv(@FormParam("idResult") final String idResult,
            @FormParam("filename") final String importedFilename)
    {
        Objects.requireNonNull(idResult, "idResult is null");
        Objects.requireNonNull(importedFilename, "importedFilename is null");

        @SuppressWarnings("unchecked")
        Map<String, String> modelsOutput = (Map<String, String>) SecurityUtils.getSubject().getSession()
                .getAttribute(idResult);

        Objects.requireNonNull(modelsOutput, "results not found");
        String filename = importedFilename.substring(0, importedFilename.lastIndexOf(".xls"));

        return Response.ok(
                (StreamingOutput) outputStream ->
                scsvFileWriter.writeModelsOutputToScsvFile(modelsOutput, outputStream))
                .header("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"").build();
    }
}
