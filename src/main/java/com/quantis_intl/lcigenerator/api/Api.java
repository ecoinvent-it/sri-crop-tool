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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.quantis_intl.lcigenerator.ErrorReporter;
import com.quantis_intl.lcigenerator.ErrorReporterImpl;
import com.quantis_intl.lcigenerator.GenerationService;
import com.quantis_intl.lcigenerator.ImportResult;
import com.quantis_intl.lcigenerator.PyBridgeService;
import com.quantis_intl.lcigenerator.ScsvFileWriter;
import com.quantis_intl.lcigenerator.dao.GenerationDao;
import com.quantis_intl.lcigenerator.imports.ExcelInputReader;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.model.Generation;
import com.quantis_intl.lcigenerator.scsv.OutputTarget;
import com.quantis_intl.stack.utils.Qid;

@Path("/")
public class Api
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Api.class);

    private static final int UPLOADED_FILE_MAX_SIZE = 10 * 1024 * 1024;

    private static final Set<String> UPLOADED_FILE_EXTENSIONS = ImmutableSet.of(".xls", ".xlsx");

    private final ExcelInputReader inputReader;

    private final PyBridgeService pyBridgeService;

    private final ScsvFileWriter scsvFileWriter;

    private final GenerationService generationService;

    private final GenerationDao generationDao;

    private final String uploadedFilesFolder;

    @Inject
    public Api(ExcelInputReader inputReader, PyBridgeService pyBridgeService, ScsvFileWriter scsvFileWriter,
            GenerationService generationService, GenerationDao generationDao,
            @Named("server.uploadedFilesFolder") String uploadedFilesFolder)
    {
        this.inputReader = inputReader;
        this.pyBridgeService = pyBridgeService;
        this.scsvFileWriter = scsvFileWriter;
        this.generationService = generationService;
        this.generationDao = generationDao;
        this.uploadedFilesFolder = uploadedFilesFolder;
    }

    @POST
    @Path("computeLci")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public void computeLci(@MultipartForm ComputeLCiForm form, @Suspended AsyncResponse response)
            throws URISyntaxException, IOException
    {
        final long startTime = System.nanoTime();
        final ErrorReporterImpl errorReporter = new ErrorReporterImpl();
        final Qid presumedGenerationId = form.generationId.isEmpty() ? null : Qid.fromRepresentation(form.generationId);
        final boolean couldBeTheSameGeneration = generationService.couldBeTheSameGeneration(presumedGenerationId,
                form.filename);

        final Qid userId = getUserId();
        // FIXME: Use license Id
        final Qid licenseId = userId;

        final String fileExtension = form.filename.substring(form.filename.lastIndexOf('.'));
        if (!UPLOADED_FILE_EXTENSIONS.contains(fileExtension))
        {
            errorReporter.error("Sorry, we cannot read this file. Please use the Excel formats (.xls or .xlsx)");
            LOGGER.info("User {}: Uploaded file handled with errors: {}", userId,
                    errorReporter.getErrors().stream().map(Object::toString).collect(Collectors.joining(", ")));
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
        }
        byte[] uploadedFile = ByteStreams.toByteArray(ByteStreams.limit(form.is, UPLOADED_FILE_MAX_SIZE));
        if (uploadedFile.length >= UPLOADED_FILE_MAX_SIZE)
        {
            errorReporter.error("Sorry, we cannot read this file. Please use a smaller file (max 10Mo)");
            LOGGER.error("User {}: File too big: name {}, size in octet: {}", userId, form.filename,
                    uploadedFile.length);
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(uploadedFile);

        ValueGroup extractedInputs = inputReader.getInputDataFromFile(bais, errorReporter);

        if (!errorReporter.hasErrors())
        {
            // TODO: store that in the session
            Map<String, Object> validatedData = extractedInputs.flattenValues();

            final String crop = (String) validatedData.get("crop");
            final String country = (String) validatedData.get("country");
            final boolean hasSameCropAndCountry = !couldBeTheSameGeneration ? false
                    : generationService.hasSameCropAndCountry(presumedGenerationId, crop, country);

            // FIXME: Fill appVersion
            final String appVersion = "";
            Generation generation;
            if (couldBeTheSameGeneration && hasSameCropAndCountry)
                generation = generationService.updateGeneration(presumedGenerationId, errorReporter.getWarnings(),
                        appVersion);
            else
                generation = generationService.createNewGeneration(userId, licenseId, appVersion, form.canBeStored,
                        form.filename, crop, country, errorReporter.getWarnings());

            saveFileForGeneration(uploadedFile, fileExtension, generation);

            // TODO: put that in second step (generateScsv)
            pyBridgeService.callComputeLci(validatedData,
                    result -> onResult(result, extractedInputs, generation.getId().toString(), errorReporter, response,
                            startTime),
                    error -> onError(error, response));

            LOGGER.info("User {}: Uploaded file handled with {} warnings", userId, errorReporter.getWarnings().size());
        }
        else
        {
            LOGGER.info(
                    "User {}: Uploaded file handled with errors: {}", userId,
                    errorReporter.getErrors().stream().map(Object::toString).collect(Collectors.joining(", ")));
            saveOrphanFile(uploadedFile, form.filename, fileExtension, userId, Qid.random());
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
        }

        form.is.close();
    }

    private void saveFileForGeneration(byte[] fileContent, String fileExtension, Generation generation)
            throws IOException
    {
        String userFolderName = uploadedFilesFolder + generation.getUserId();

        String uploadedFileLocation = userFolderName + "/" + generation.getId() + "-" + generation.getLastTryNumber()
                + fileExtension;

        saveFile(fileContent, userFolderName, uploadedFileLocation);

        LOGGER.info("File saved generation id: {}, original name: {}", generation.getId(), generation.getFilename());
    }

    private void saveOrphanFile(byte[] fileContent, String originalFileName, String fileExtension,
            Qid userId, Qid newFileName) throws IOException
    {
        String userFolderName = uploadedFilesFolder + userId;
        String uploadedFileLocation = userFolderName + "/" + newFileName + fileExtension;

        saveFile(fileContent, userFolderName, uploadedFileLocation);

        LOGGER.info("File saved file id: {}, original name: {}", userId, newFileName, originalFileName);
    }

    private void saveFile(byte[] fileContent, String folderName, String uploadedFileLocation) throws IOException
    {
        new File(folderName).mkdir();
        Files.write(fileContent, new File(uploadedFileLocation));
    }

    private void onResult(Map<String, String> modelsOutput, ValueGroup extractedInputs, String generationId,
            ErrorReporter errorReporter, AsyncResponse response, long startTime)
    {
        final Qid userId = getUserId();
        SecurityUtils.getSubject().getSession().setAttribute(generationId, modelsOutput);
        SecurityUtils.getSubject().getSession().setAttribute(generationId + "_inputs", extractedInputs);
        LOGGER.info("User {}: Computations done and stored in {} in {} ms", userId, generationId,
                (System.nanoTime() - startTime) / 1000000.d);
        response.resume(Response.ok(new ImportResult(errorReporter, generationId)).build());
    }

    // TODO: Define standards in stack for error handling (e500 template, client code, etc)
    private void onError(Throwable error, AsyncResponse response)
    {
        LOGGER.error("Error using pyBridge", error);
        response.resume(error);
    }

    @POST
    @Path("checkScsvGeneration")
    public Response checkScsvGeneration(@FormParam("generationId") final String generationId,
            @FormParam("filename") final String importedFilename,
            @FormParam("dbOption") final String database)
    {
        final Qid userId = getUserId();
        if (generationId == null || importedFilename == null)
        {
            LOGGER.error("User {}: Bad request, generation {} or filename {} is null", userId, generationId,
                    importedFilename);
            return Response.status(Status.BAD_REQUEST).entity("generation or filename is missing").build();
        }

        @SuppressWarnings("unchecked")
        Map<String, String> modelsOutput = (Map<String, String>) SecurityUtils.getSubject().getSession()
                .getAttribute(generationId);

        Objects.requireNonNull(modelsOutput, "results not found");

        return Response.ok().build();
    }

    @POST
    @Path("generateScsv")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateScsv(@FormParam("generationId") final String generationId,
            @FormParam("filename") final String importedFilename,
            @FormParam("dbOption") final String database)
    {
        final Qid userId = getUserId();
        long startTime = System.nanoTime();
        if (generationId == null || importedFilename == null)
        {
            LOGGER.error("User {}: Bad request, generation {} or filename {} is null", userId, generationId,
                    importedFilename);
            return Response.status(Status.BAD_REQUEST).entity("generationId or filename is missing").build();
        }

        @SuppressWarnings("unchecked")
        Map<String, String> modelsOutput = (Map<String, String>) SecurityUtils.getSubject().getSession()
                .getAttribute(generationId);
        ValueGroup extractedInputs = (ValueGroup) SecurityUtils.getSubject().getSession()
                .getAttribute(generationId + "_inputs");

        Objects.requireNonNull(modelsOutput, "results not found");
        String filename = importedFilename.substring(0, importedFilename.lastIndexOf(".xls"));

        LOGGER.info("User {}: Scsv file generated for generation {} in {} ms", userId, generationId,
                (System.nanoTime() - startTime) / 1000000.d);

        return Response
                .ok(
                        (StreamingOutput) outputStream -> scsvFileWriter.writeModelsOutputToScsvFile(modelsOutput,
                                extractedInputs,
                                OutputTarget.valueOf(database), outputStream))
                .header("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"").build();
    }

    private Qid getUserId()
    {
        return (Qid) SecurityUtils.getSubject().getPrincipal();
    }

    public static class ComputeLCiForm
    {
        @FormParam("uploadFile")
        public InputStream is;
        @FormParam("canBeStored")
        public boolean canBeStored;
        @FormParam("filename")
        public String filename;
        @FormParam("generationId")
        public String generationId;
    }

}
