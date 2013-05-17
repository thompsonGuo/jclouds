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
package org.jclouds.abiquo.strategy.admin.internal;

import static com.google.common.collect.Iterables.filter;
import static org.jclouds.abiquo.domain.DomainWrapper.wrap;

import javax.inject.Singleton;

import org.jclouds.abiquo.AbiquoAsyncApi;
import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.domain.enterprise.Role;
import org.jclouds.abiquo.strategy.admin.ListRoles;
import org.jclouds.rest.RestContext;

import com.abiquo.server.core.enterprise.RolesDto;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

/**
 * List enterprises.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Singleton
public class ListRolesImpl implements ListRoles
{
    // This strategy does not have still an Executor instance because the current methods call
    // single api methods

    protected final RestContext<AbiquoApi, AbiquoAsyncApi> context;

    @Inject
    ListRolesImpl(final RestContext<AbiquoApi, AbiquoAsyncApi> context)
    {
        this.context = context;
    }

    @Override
    public Iterable<Role> execute()
    {
        RolesDto result = context.getApi().getAdminApi().listRoles();
        return wrap(context, Role.class, result.getCollection());
    }

    @Override
    public Iterable<Role> execute(final Predicate<Role> selector)
    {
        return filter(execute(), selector);
    }

}
