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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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

    private ExcelInputReader inputReader;

    private PyBridgeService pyBridgeService;

    private ScsvFileWriter scsvFileWriter;

    private final String uploadedFilesFolder;

    @Inject
    public Api(ExcelInputReader inputReader, PyBridgeService pyBridgeService, ScsvFileWriter scsvFileWriter,
            @Named("configuration.properties") Properties properties)
    {
        this.inputReader = inputReader;
        this.pyBridgeService = pyBridgeService;
        this.scsvFileWriter = scsvFileWriter;
        this.uploadedFilesFolder = properties.getProperty("server.uploadedFilesFolder");
    }

    @POST
    @Path("computeLci")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public void computeLci(@FormDataParam("uploadFile") InputStream is,
            @FormDataParam("canBeStored") boolean canBeStored,
            @FormDataParam("username") String username,
            @FormDataParam("email") String email,
            @FormDataParam("address") String address,
            @Suspended AsyncResponse response)
            throws URISyntaxException, IOException
    {
        long startTime = System.nanoTime();
        ErrorReporterImpl errorReporter = new ErrorReporterImpl();

        String idResult = UUID.randomUUID().toString();
        LOGGER.info("BETA: user using this feature: name {}, email {}, other info: {}: {}",
                username, email, address);

        BufferedInputStream bfis = new BufferedInputStream(is);
        if (canBeStored)
            saveUploadedFile(bfis, email, idResult);

        ValueGroup extractedInputs = inputReader.getInputDataFromFile(bfis, errorReporter);

        if (!errorReporter.hasErrors())
        {
            Map<String, Object> validatedData = extractedInputs.flattenValues();

            pyBridgeService.callComputeLci(validatedData,
                    result -> onResult(result, extractedInputs, idResult, errorReporter, response, startTime),
                    error -> onError(error, response));

            LOGGER.info("Uploaded file handled with {} warnings", errorReporter.getWarnings().size());
        }
        else
        {
            LOGGER.info(
                    "Uploaded file handled with errors: {}",
                    errorReporter.getErrors().stream().map(Object::toString).collect(Collectors.joining(", ")));
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
        }
        bfis.close();
        is.close();
    }

    private static final String UPLOADED_FILE_EXTENSION = ".xlsx";

    private void saveUploadedFile(BufferedInputStream uploadedInputStream, String email, String idResult)
            throws IOException
    {
        // FIXME: Check if regExp is uncompleted or too strict
        String safeFolderName = email.replaceAll("[\\\\\\*\\.\\\" ~%$&+,/:;=?@<>#%]+", "_");
        String userFolderName = uploadedFilesFolder + safeFolderName;
        new File(userFolderName).mkdir();

        uploadedInputStream.mark(Integer.MAX_VALUE);
        String uploadedFileLocation = userFolderName + "/" + idResult + UPLOADED_FILE_EXTENSION;

        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        int read = 0;
        byte[] bytes = new byte[1024];

        out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(bytes)) != -1)
        {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
        uploadedInputStream.reset();

        LOGGER.info("BETA: file saved for user {}, file id {}", email, idResult);
    }

    private void onResult(Map<String, String> modelsOutput, ValueGroup extractedInputs, String idResult,
            ErrorReporter errorReporter, AsyncResponse response, long startTime)
    {
        SecurityUtils.getSubject().getSession().setAttribute(idResult, modelsOutput);
        SecurityUtils.getSubject().getSession().setAttribute(idResult + "_inputs", extractedInputs);
        LOGGER.info("Computations done and stored in {} in {} ms", idResult,
                (System.nanoTime() - startTime) / 1000000.d);
        response.resume(Response.ok(new ImportResult(errorReporter, idResult)).build());
    }

    // TODO: Define standards in stack for error handling (e500 template, client code, etc)
    private void onError(Throwable error, AsyncResponse response)
    {
        LOGGER.error("Error using pyBridge", error);
        response.resume(error);
    }

    @POST
    @Path("generateScsv")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateScsv(@FormParam("idResult") final String idResult,
            @FormParam("filename") final String importedFilename)
    {
        long startTime = System.nanoTime();
        if (idResult == null || importedFilename == null)
        {
            LOGGER.error("Bad request, idResult {} or filename {} is null", idResult, importedFilename);
            return Response.status(Status.BAD_REQUEST).entity("idResult or filename is missing").build();
        }

        @SuppressWarnings("unchecked")
        Map<String, String> modelsOutput = (Map<String, String>) SecurityUtils.getSubject().getSession()
                .getAttribute(idResult);
        ValueGroup extractedInputs = (ValueGroup) SecurityUtils.getSubject().getSession()
                .getAttribute(idResult + "_inputs");

        Objects.requireNonNull(modelsOutput, "results not found");
        String filename = importedFilename.substring(0, importedFilename.lastIndexOf(".xls"));

        LOGGER.info("Scsv file generated for results {} in {} ms", idResult,
                (System.nanoTime() - startTime) / 1000000.d);

        return Response.ok(
                (StreamingOutput) outputStream ->
                scsvFileWriter.writeModelsOutputToScsvFile(modelsOutput, extractedInputs, outputStream))
                .header("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"").build();
    }
}
