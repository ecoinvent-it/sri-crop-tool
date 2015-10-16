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
package com.quantis_intl.lcigenerator.model;

import java.time.LocalDateTime;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quantis_intl.lcigenerator.ErrorReporterImpl.ErrorReporterResult;
import com.quantis_intl.lcigenerator.imports.ValueGroup;
import com.quantis_intl.stack.utils.Qid;

public class Generation
{
    private Qid id;
    private Qid userId;
    private Qid licenseId;
    private boolean canUseForTesting;
    private int lastTryNumber;
    private LocalDateTime lastTryDate;
    private String appVersion;
    private String crop;
    private String country;
    private String filename;
    private Collection<ErrorReporterResult> warnings;

    // TODO: Not good to use delivery annotation in business obj
    @JsonIgnore
    private ValueGroup extractedInputs;

    public Generation()
    {}

    public Qid getId()
    {
        return id;
    }

    public void setId(Qid id)
    {
        this.id = id;
    }

    public Qid getUserId()
    {
        return userId;
    }

    public void setUserId(Qid userId)
    {
        this.userId = userId;
    }

    public Qid getLicenseId()
    {
        return licenseId;
    }

    public void setLicenseId(Qid licenseId)
    {
        this.licenseId = licenseId;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public boolean getCanUseForTesting()
    {
        return canUseForTesting;
    }

    public void setCanUseForTesting(boolean canUseForTesting)
    {
        this.canUseForTesting = canUseForTesting;
    }

    public String getCrop()
    {
        return crop;
    }

    public void setCrop(String crop)
    {
        this.crop = crop;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public int getLastTryNumber()
    {
        return lastTryNumber;
    }

    public void setLastTryNumber(int lastTryNumber)
    {
        this.lastTryNumber = lastTryNumber;
    }

    public LocalDateTime getLastTryDate()
    {
        return lastTryDate;
    }

    public void setLastTryDate(LocalDateTime lastTryDate)
    {
        this.lastTryDate = lastTryDate;
    }

    public String getAppVersion()
    {
        return appVersion;
    }

    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public Collection<ErrorReporterResult> getWarnings()
    {
        return warnings;
    }

    public void setWarnings(Collection<ErrorReporterResult> warnings)
    {
        this.warnings = warnings;
    }

    public ValueGroup getExtractedInputs()
    {
        return extractedInputs;
    }

    public void setExtractedInputs(ValueGroup extractedInputs)
    {
        this.extractedInputs = extractedInputs;
    }
}
