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
        return p;
    }

    private static Properties getAdditionalProperties()
    {
        Properties p = new Properties();
        p.setProperty("jersey.rest.package", "com.quantis_intl.lcigenerator.api");
        return p;
    }
}
