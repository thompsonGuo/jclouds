/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.abiquo.binders.cloud;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.xml.XMLParser;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.LinksDto;
import com.abiquo.server.core.infrastructure.network.AbstractIpDto;

/**
 * Bind the link reference to an {@link AbstractIpDto} object into the payload.
 * 
 * @author Ignasi Barrera
 */
@Singleton
public class BindIpRefToPayload extends BindToXMLPayload
{
    @Inject
    public BindIpRefToPayload(final XMLParser xmlParser)
    {
        super(xmlParser);
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(final R request, final Object input)
    {
        checkArgument(checkNotNull(input, "input") instanceof AbstractIpDto,
            "this binder is only valid for AbstractIpDto objects");

        AbstractIpDto ip = (AbstractIpDto) input;
        RESTLink selfLink =
            checkNotNull(ip.searchLink("self"), "AbstractIpDto must have an self link");

        LinksDto refs = new LinksDto();
        refs.addLink(new RESTLink(selfLink.getTitle(), selfLink.getHref()));

        return super.bindToRequest(request, refs);
    }
}
