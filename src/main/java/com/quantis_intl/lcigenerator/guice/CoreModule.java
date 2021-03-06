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
package com.quantis_intl.lcigenerator.guice;

import com.google.inject.AbstractModule;
import com.quantis_intl.lcigenerator.api.Api;
import com.quantis_intl.lcigenerator.api.LicenseApi;
import com.quantis_intl.lcigenerator.api.PublicAlcigApi;
import com.quantis_intl.lcigenerator.api.PublicApi;
import com.quantis_intl.lcigenerator.dao.GenerationDao;
import com.quantis_intl.lcigenerator.dao.MybatisGenerationDao;
import com.quantis_intl.lcigenerator.license.LicenseDao;
import com.quantis_intl.lcigenerator.license.MybatisLicenseDao;

public class CoreModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(Api.class);
        bind(PublicApi.class);
        bind(PublicAlcigApi.class);
        bind(LicenseApi.class);
        bind(GenerationDao.class).to(MybatisGenerationDao.class);
        bind(LicenseDao.class).to(MybatisLicenseDao.class);
    }
}
