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

import java.util.Date;

public class UserPwd
{
    private int userId;

    private String base64salt;

    private String password;

    private boolean forceChangePassword;

    private int failedAttemps;

    private Date lockedSince;

    private String registrationCode;

    private String validationCode;

    private Date codeGeneration;

    public UserPwd()
    {
    }

    public UserPwd(int userId, String base64salt, String password, boolean forceChangePassword, int failedAttemps,
            Date lockedSince, String registrationCode, String validationCode, Date codeGeneration)
    {
        super();
        this.userId = userId;
        this.base64salt = base64salt;
        this.password = password;
        this.forceChangePassword = forceChangePassword;
        this.failedAttemps = failedAttemps;
        this.lockedSince = lockedSince;
        this.registrationCode = registrationCode;
        this.validationCode = validationCode;
        this.codeGeneration = codeGeneration;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getBase64salt()
    {
        return base64salt;
    }

    public void setBase64salt(String base64salt)
    {
        this.base64salt = base64salt;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean getForceChangePassword()
    {
        return forceChangePassword;
    }

    public void setForceChangePassword(boolean forceChangePassword)
    {
        this.forceChangePassword = forceChangePassword;
    }

    public int getFailedAttemps()
    {
        return failedAttemps;
    }

    public void setFailedAttemps(int failedAttemps)
    {
        this.failedAttemps = failedAttemps;
    }

    public Date getLockedSince()
    {
        return lockedSince;
    }

    public void setLockedSince(Date lockedSince)
    {
        this.lockedSince = lockedSince;
    }

    public String getRegistrationCode()
    {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode)
    {
        this.registrationCode = registrationCode;
    }

    public String getValidationCode()
    {
        return validationCode;
    }

    public void setValidationCode(String validationCode)
    {
        this.validationCode = validationCode;
    }

    public Date getCodeGeneration()
    {
        return codeGeneration;
    }

    public void setCodeGeneration(Date codeGeneration)
    {
        this.codeGeneration = codeGeneration;
    }

}
