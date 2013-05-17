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
package org.jclouds.abiquo.predicates.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import org.jclouds.abiquo.domain.config.SystemProperty;

import com.google.common.base.Predicate;

/**
 * Container for {@link SystemProperty} filters.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class SystemPropertyPredicates
{
    public static Predicate<SystemProperty> name(final String... names)
    {
        checkNotNull(names, "names must be defined");

        return new Predicate<SystemProperty>()
        {
            @Override
            public boolean apply(final SystemProperty property)
            {
                return Arrays.asList(names).contains(property.getName());
            }
        };
    }

}
