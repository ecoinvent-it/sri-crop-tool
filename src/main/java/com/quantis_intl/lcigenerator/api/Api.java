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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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
import com.quantis_intl.lcigenerator.PyBridgeService;
import com.quantis_intl.lcigenerator.ScsvFileWriter;
import com.quantis_intl.lcigenerator.dao.GenerationDao;
import com.quantis_intl.lcigenerator.imports.ExcelInputReader;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.lcigenerator.license.LicenseService;
import com.quantis_intl.lcigenerator.license.LicenseService.NoActiveLicense;
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

    private final GenerationDao generationDao;
    private final LicenseService licenseService;

    private final java.nio.file.Path uploadedFilesFolder;

    // FIXME: To fill
    private final String appVersion = "";

    @Inject
    public Api(ExcelInputReader inputReader, PyBridgeService pyBridgeService, ScsvFileWriter scsvFileWriter,
            GenerationDao generationDao, LicenseService licenseService,
            @Named("server.uploadedFilesFolder") String uploadedFilesFolder)
    {
        this.inputReader = inputReader;
        this.pyBridgeService = pyBridgeService;
        this.scsvFileWriter = scsvFileWriter;
        this.generationDao = generationDao;
        this.licenseService = licenseService;
        this.uploadedFilesFolder = Paths.get(uploadedFilesFolder);
    }

    @GET
    @Path("userGenerations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Generation> getUserGenerations()
    {
        List<Generation> generations = generationDao.getAllUserGenerations(getUserId());
        LOGGER.info("Get user generations");
        return generations;
    }

    @POST
    @Path("computeLci")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public void computeLci(@MultipartForm ComputeLciForm form, @Suspended AsyncResponse response)
            throws URISyntaxException, IOException
    {
        long startTime = System.nanoTime();
        Generation generation = createCurrentGeneration(form);
        final ErrorReporterImpl errorReporter = new ErrorReporterImpl();
        try
        {
            generation = checkGenerationFilenameAndDate(generation);
            checkAndSetLicenseIfMissing(generation);
        }
        catch (GenerationNotOwned e)
        {
            LOGGER.error("SECURITY ISSUE: Found generation is not owned by user: generation id {}", generation.getId());
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }
        catch (NoActiveLicense e)
        {
            LOGGER.error("Depleted license has been used for user {}", getUserId());
            errorReporter.error("Your license is depleted. Please contact your reseller to get a new license.");
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
            return;
        }

        final String fileExtension = form.filename.substring(form.filename.lastIndexOf('.'));
        if (!UPLOADED_FILE_EXTENSIONS.contains(fileExtension))
        {
            errorReporter.error("Sorry, we cannot read this file. Please use the Excel formats (.xls or .xlsx)");
            LOGGER.info("Uploaded file handled with errors: {}",
                    errorReporter.getErrors().stream().map(Object::toString).collect(Collectors.joining(", ")));
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
            return;
        }
        byte[] uploadedFile = ByteStreams.toByteArray(ByteStreams.limit(form.is, UPLOADED_FILE_MAX_SIZE));
        form.is.close();
        if (uploadedFile.length >= UPLOADED_FILE_MAX_SIZE)
        {
            errorReporter.error("Sorry, we cannot read this file. Please use a smaller file (max 10Mo)");
            LOGGER.error("File too big: name {}, size in octet: {}", form.filename, uploadedFile.length);
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
            return;
        }
        try
        {
            generation = parse(generation, uploadedFile);
            checkAndSetLicenseIfMissing(generation);

            SecurityUtils.getSubject().getSession().setAttribute(generation.getId().toString(), generation);
            generationDao.createOrUpdateGeneration(generation);
            licenseService.checkLicenseDepletion(generation.getLicenseId());

            response.resume(Response.ok(generation).build());
            LOGGER.info("Parsing and storing done in {} ms", (System.nanoTime() - startTime) / 1000000.d);
            return;
        }
        catch (ParsingErrorsFound e)
        {
            LOGGER.info(
                    "Uploaded file handled with errors: {}",
                    e.errorReporter.getErrors().stream().map(Object::toString).collect(Collectors.joining(", ")));
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.errorReporter).build());
            return;
        }
        catch (NoActiveLicense e)
        {
            LOGGER.error("Depleted license has been used for user {}", getUserId());
            errorReporter.error("Your license is depleted. Please contact your reseller to get a new license.");
            response.resume(Response.status(Response.Status.BAD_REQUEST).entity(errorReporter).build());
            return;
        }
    }

    private Generation createCurrentGeneration(ComputeLciForm form)
    {
        Generation currentGeneration = new Generation();

        if (!form.generationId.isEmpty())
            currentGeneration.setId(Qid.fromRepresentation(form.generationId));
        else
            currentGeneration.setId(Qid.random());

        currentGeneration.setUserId(getUserId());
        currentGeneration.setFilename(form.filename);
        currentGeneration.setAppVersion(appVersion);
        currentGeneration.setCanUseForTesting(form.canBeStored);
        currentGeneration.setLastTryNumber(1);
        currentGeneration.setLastTryDate(LocalDateTime.now(ZoneOffset.UTC));

        return currentGeneration;
    }

    private static final Duration MAX_DURATION_BETWEEN_TWO_TRIES = Duration.ofMinutes(30);

    private Generation checkGenerationFilenameAndDate(Generation currentGeneration)
    {
        Generation persistedGeneration = generationDao.getGenerationFromId(currentGeneration.getId());
        if (persistedGeneration == null)
            return currentGeneration;
        else
        {
            if (!persistedGeneration.getUserId().equals(getUserId()))
                throw new GenerationNotOwned();

            currentGeneration.setId(Qid.random());
            if (!persistedGeneration.getFilename().equals(currentGeneration.getFilename()))
                return currentGeneration;
            if (currentGeneration.getLastTryDate().minus(MAX_DURATION_BETWEEN_TWO_TRIES)
                    .isAfter(persistedGeneration.getLastTryDate()))
                return currentGeneration;
            else
            {
                persistedGeneration.setLastTryDate(currentGeneration.getLastTryDate());
                persistedGeneration.setLastTryNumber(persistedGeneration.getLastTryNumber() + 1);
                return persistedGeneration;
            }
        }
    }

    private void checkAndSetLicenseIfMissing(Generation currentGeneration)
    {
        if (currentGeneration.getLicenseId() != null)
            return;

        currentGeneration.setLicenseId(licenseService.findActiveLicenseOrFail(currentGeneration.getUserId()).getId());
    }

    @POST
    @Path("generateScsv")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void generateScsv(@FormParam("generationId") final String generationId,
            @FormParam("dbOption") final String database,
            @Suspended AsyncResponse response) throws FileNotFoundException, IOException
    {
        long startTime = System.nanoTime();
        if (generationId == null)
        {
            LOGGER.error("Bad request, generationId {} is null", generationId);
            response.resume(Response.status(Status.BAD_REQUEST).entity("generationId is missing").build());
            return;
        }

        try
        {
            Generation generation = findExistingGeneration(generationId);
            Map<String, Object> validatedData = generation.getExtractedInputs().flattenValues();
            pyBridgeService.callComputeLci(validatedData,
                    result -> onResult(result, generation, OutputTarget.valueOf(database), response, startTime),
                    error -> onError(error, response));
        }
        catch (GenerationNotFound e)
        {
            response.resume(Response.status(Status.BAD_REQUEST).entity("Unknown generation").build());
        }
    }

    private Generation findExistingGeneration(String generationId) throws FileNotFoundException, IOException
    {
        Generation generation = (Generation) SecurityUtils.getSubject().getSession().getAttribute(generationId);
        if (generation == null)
        {
            generation = generationDao.getGenerationFromId(Qid.fromRepresentation(generationId));
            if (generation == null)
            {
                LOGGER.error("Unknown generation: {}", generationId);
                throw new GenerationNotFound();
            }

            Qid userId = getUserId();
            if (!generation.getUserId().equals(userId))
            {
                LOGGER.error("SECURITY ISSUE: Provided generationId {} is not owned by the logged user {}!",
                        generationId, userId);
                throw new GenerationNotFound();
            }
            java.nio.file.Path inputFilePath = uploadedFilesFolder
                    .resolve(Paths.get(userId.toString(), getFileNameFromGeneration(generation)));
            try (FileInputStream is = new FileInputStream(inputFilePath.toFile()))
            {
                generation.setExtractedInputs(inputReader.getInputDataFromFile(is, ErrorReporter.NO_OP));
                SecurityUtils.getSubject().getSession().setAttribute(generation.getId().toString(), generation);
            }
        }
        return generation;
    }

    private void onResult(Map<String, String> modelsOutput, Generation generation,
            OutputTarget outputTarget, AsyncResponse response, long startTime)
    {
        SecurityUtils.getSubject().getSession().setAttribute(generation.getId(), modelsOutput);
        String filename = generation.getFilename().substring(0, generation.getFilename().lastIndexOf(".xls"));
        LOGGER.info("Scsv file {} generated for generation {} in {} ms", outputTarget, generation.getId(),
                (System.nanoTime() - startTime) / 1000000.d);
        response.resume(
                Response.ok((StreamingOutput) outputStream -> scsvFileWriter.writeModelsOutputToScsvFile(modelsOutput,
                        generation.getExtractedInputs(), outputTarget, outputStream))
                        .header("Content-Disposition", "attachment; filename=\"" + filename + ".csv\"").build());
    }

    // TODO: Define standards in stack for error handling (e500 template, client code, etc)
    private void onError(Throwable error, AsyncResponse response)
    {
        LOGGER.error("Error using pyBridge", error);
        response.resume(error);
    }

    private Qid getUserId()
    {
        return (Qid) SecurityUtils.getSubject().getPrincipal();
    }

    public static class GenerationNotOwned extends RuntimeException
    {
        private static final long serialVersionUID = 8658020993889992990L;

        public GenerationNotOwned()
        {}
    }

    public static class ComputeLciForm
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

    // ----- PARSING

    private Generation parse(Generation currentGeneration, byte[] uploadedFile) throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(uploadedFile);
        final ErrorReporterImpl errorReporter = new ErrorReporterImpl();
        ValueGroup extractedInputs = inputReader.getInputDataFromFile(bais, errorReporter);

        if (errorReporter.hasErrors())
        {
            final String fileExtension = currentGeneration.getFilename()
                    .substring(currentGeneration.getFilename().lastIndexOf('.'));
            saveOrphanFile(uploadedFile, currentGeneration.getFilename(), fileExtension,
                    currentGeneration.getUserId(), Qid.random());
            throw new ParsingErrorsFound(errorReporter);
        }

        final Map<String, Object> validatedData = extractedInputs.flattenValues();
        final String crop = (String) validatedData.get("crop");
        final String country = (String) validatedData.get("country");

        if (currentGeneration.getCrop() == null && currentGeneration.getCountry() == null)
        {
            currentGeneration.setCrop(crop);
            currentGeneration.setCountry(country);
        }
        else if (!crop.equals(currentGeneration.getCrop()) || !country.equals(currentGeneration.getCountry()))
        {
            currentGeneration = createNewGenerationUsingSomeInfo(currentGeneration);
            currentGeneration.setCrop(crop);
            currentGeneration.setCountry(country);
        }

        currentGeneration.setWarnings(errorReporter.getWarnings());
        currentGeneration.setExtractedInputs(extractedInputs);
        saveFileForGeneration(uploadedFile, currentGeneration);

        return currentGeneration;
    }

    private Generation createNewGenerationUsingSomeInfo(Generation currentGeneration)
    {
        Generation newGeneration = new Generation();
        newGeneration.setId(Qid.random());
        newGeneration.setUserId(currentGeneration.getUserId());
        newGeneration.setCanUseForTesting(currentGeneration.getCanUseForTesting());
        newGeneration.setLastTryNumber(1);
        newGeneration.setLastTryDate(currentGeneration.getLastTryDate());
        newGeneration.setAppVersion(currentGeneration.getAppVersion());
        newGeneration.setFilename(currentGeneration.getFilename());

        return newGeneration;
    }

    private void saveFileForGeneration(byte[] fileContent, Generation generation) throws IOException
    {
        java.nio.file.Path filePath = uploadedFilesFolder
                .resolve(Paths.get(generation.getUserId().toString(), getFileNameFromGeneration(generation)));

        saveFile(fileContent, filePath);

        LOGGER.info("File saved generation id: {}, original name: {}", generation.getId(), generation.getFilename());
    }

    private void saveOrphanFile(byte[] fileContent, String originalFileName, String fileExtension,
            Qid userId, Qid newFileName) throws IOException
    {
        String fileName = newFileName.toString() + fileExtension;
        java.nio.file.Path filePath = uploadedFilesFolder.resolve(Paths.get(userId.toString(), fileName));

        saveFile(fileContent, filePath);

        LOGGER.info("File saved file id: {}, original name: {}", userId, newFileName, originalFileName);
    }

    private String getFileNameFromGeneration(Generation generation)
    {
        return generation.getId() + "-" + generation.getLastTryNumber()
                + generation.getFilename().substring(generation.getFilename().lastIndexOf('.'));
    }

    private void saveFile(byte[] fileContent, java.nio.file.Path filePath) throws IOException
    {
        File file = filePath.toFile();
        Files.createParentDirs(file);
        Files.write(fileContent, file);
    }

    public static class ParsingErrorsFound extends RuntimeException
    {
        private static final long serialVersionUID = 6576190302342716882L;

        public final ErrorReporterImpl errorReporter;

        public ParsingErrorsFound(ErrorReporterImpl errorReporter)
        {
            this.errorReporter = errorReporter;
        }
    }

    public static class GenerationNotFound extends RuntimeException
    {
        private static final long serialVersionUID = -7696411072607309004L;
    }
}
