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

import java.io.IOException;
import java.util.Properties;

import javax.inject.Named;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.quantis_intl.lcigenerator.LoginServiceImpl;
import com.quantis_intl.lcigenerator.guice.CoreModule;
import com.quantis_intl.lcigenerator.model.User;
import com.quantis_intl.stack.mybatis.QtsMyBatisModule;

public class UserCreator
{
    static LoginServiceImpl service;

    static final Properties properties = getDefaultProperties();

    public static void main(String[] args) throws IOException
    {
        // if (args.length == 1)
        // {
        Injector injector = Guice.createInjector(new CoreModule(/*properties*/),
                new QtsMyBatisModule(properties, ImmutableList.of("com.quantis_intl.lcigenerator.mappers")),
                buildPropertiesModule(properties));
        service = injector.getInstance(LoginServiceImpl.class);
        // File file = new File(args[0]);
        String usernames = "test";// Files.toString(file, Charset.forName("windows-1252"));
        for (String username : usernames.split("\n"))
        {
            String pwd = generateUser(username);
            System.out.println("\"" + username + "\";\"" + pwd + "\"");
        }
        // }
        // else
        // System.out.println("Args: fileForUsername");
    }

    private static String generateUser(String username)
    {
        User user = new User(0, username);
        return service.createUser(user);
    }

    private static Properties getDefaultProperties()
    {
        Properties p = new Properties();
        p.setProperty("server.port", "7879");
        p.setProperty("server.baseContext", "/");
        p.setProperty("server.webFolder", "src/main/dart/build/web");
        p.setProperty("server.logFolder", "");
        p.setProperty("sql.schema", "lcigenerator_test");
        p.setProperty("sql.username", "root");
        p.setProperty("sql.password", "root");

        p.setProperty("mybatis.mappers.package", "com.quantis_intl.lcigenerator.mappers");
        return p;
    }

    static private Module buildPropertiesModule(final Properties properties)
    {
        return new AbstractModule()
        {
            @Override
            protected void configure()
            {
                Names.bindProperties(binder(), properties);
            }

            @Provides
            @Named("configuration.properties")
            Properties getConfigurationProperties()
            {
                return properties;
            }
        };
    }
}
