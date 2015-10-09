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

import java.util.Properties;

import com.quantis_intl.lcigenerator.guice.CoreModule;
import com.quantis_intl.login.LoginFeatureBuilder;
import com.quantis_intl.stack.QtsStack;
import com.quantis_intl.stack.features.MailFeature;
import com.quantis_intl.stack.features.MyBatisFeature;
import com.quantis_intl.stack.utils.StackProperties;

public class Bootstrap
{
    private Bootstrap()
    {}

    public static void main(String[] args) throws Exception
    {
        QtsStack stack;
        if (args.length == 1)
            stack = QtsStack.usingPropertiesFile(args[0]);
        else
        {
            stack = QtsStack.newEmptyStack();
            stack.withAdditionalProperties(getDefaultProperties());
        }

        stack.withFeatures(MailFeature.withGmailSender(),
                MyBatisFeature.withMapperPackages("com.quantis_intl.login.mappers",
                        "com.quantis_intl.lcigenerator.mappers"),
                new LoginFeatureBuilder().build("Jo5xNFdSPUmc4ijk2euM"))
                .withAdditionalModules(new CoreModule());

        stack.start();
    }

    private static Properties getDefaultProperties()
    {
        Properties p = new Properties();
        p.setProperty(StackProperties.SERVER_PORT_PROP, "7879");
        p.setProperty(StackProperties.SERVER_BASE_CONTEXT_PROP, "/");
        p.setProperty(StackProperties.SERVER_WEB_FOLDER_PROP, "src/main/dart/web");
        p.setProperty(StackProperties.SERVER_LOG_FOLDER_PROP, "");
        p.setProperty(StackProperties.ROOT_URL_PROP, "");

        p.setProperty(StackProperties.MAIL_APP_PREFIX_PROP, "ALCIG");
        p.setProperty(StackProperties.MAIL_FROM_PROP, "");
        p.setProperty(StackProperties.MAILER_USERNAME_PROP, "");
        p.setProperty(StackProperties.MAILER_PASSWORD_PROP, "");
        p.setProperty("forms.mail.to", "");

        p.setProperty(StackProperties.SQL_SCHEMA_PROP, "lcigenerator_test");
        p.setProperty(StackProperties.SQL_USERNAME_PROP, "root");
        p.setProperty(StackProperties.SQL_PASSWORD_PROP, "root");

        p.setProperty("server.uploadedFilesFolder", System.getenv("ALCIG_UPLOADED_FILES_FOLDER"));
        p.setProperty("pyBridge.url", "http://localhost:11001/computeLci");
        return p;
    }
}
