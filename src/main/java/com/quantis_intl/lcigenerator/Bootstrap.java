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

import java.util.List;
import java.util.Properties;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.quantis_intl.lcigenerator.guice.CoreModule;
import com.quantis_intl.stack.QtsStack;
import com.quantis_intl.stack.configuration.DefaultParametrizedModulesBuilder;

public class Bootstrap
{
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

        stack.withAdditionalProperties(getAdditionalProperties());
        stack.withDefaultBuilders();
        stack.withParametrizedModulesBuilder(new DefaultParametrizedModulesBuilder()
        {
            @Override
            protected List<Module> buildAdditionalModules(Properties properties)
            {
                return ImmutableList.of();
            }
        });
        stack.withAdditionalModules(new CoreModule());

        stack.start();
    }

    private static Properties getDefaultProperties()
    {
        Properties p = new Properties();
        p.setProperty("server.port", "7879");
        p.setProperty("server.baseContext", "/");
        p.setProperty("server.webFolder", "src/main/web");
        // FIXME: Should be optional
        p.setProperty("server.appResourcesFolder", "src/main/web");
        p.setProperty("server.logFolder", "");
        p.setProperty("pyBridge.url", "http://localhost:7880/computeLci");
        return p;
    }

    private static Properties getAdditionalProperties()
    {
        Properties p = new Properties();
        p.setProperty("jersey.rest.package", "com.quantis_intl.lcigenerator.api");
        return p;
    }
}
