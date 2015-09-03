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

import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

public class PyBridgeService
{
    private final WebTarget pyBridgeTarget;

    @Inject
    public PyBridgeService(@Named("pyBridge.url") String pyBridgeUrl)
    {
        this.pyBridgeTarget = ClientBuilder.newClient().register(ObjectMapperProvider.class)
                .register(ResteasyJackson2Provider.class)
                .target(pyBridgeUrl);
    }

    public void callComputeLci(Map<String, Object> request, Consumer<Map<String, String>> onResult,
            Consumer<Throwable> onError)
    {
        pyBridgeTarget.request().async().post(Entity.json(request), new InvocationCallback<Map<String, String>>()
        {
            @Override
            public void completed(Map<String, String> response)
            {
                onResult.accept(response);
            }

            @Override
            public void failed(Throwable throwable)
            {
                onError.accept(throwable);
            }
        });
    }

    @Provider
    public static class ObjectMapperProvider implements ContextResolver<ObjectMapper>
    {
        final ObjectMapper objectMapper;

        public ObjectMapperProvider()
        {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.registerModules(new Jdk8Module(), new JSR310Module());
        }

        @Override
        public ObjectMapper getContext(Class<?> type)
        {
            return objectMapper;
        }
    }
}
