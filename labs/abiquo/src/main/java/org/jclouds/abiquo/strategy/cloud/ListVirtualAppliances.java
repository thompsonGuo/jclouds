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
package org.jclouds.abiquo.strategy.cloud;

import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.strategy.ListRootEntities;
import org.jclouds.abiquo.strategy.cloud.internal.ListVirtualAppliancesImpl;

import com.google.inject.ImplementedBy;

/**
 * List virtual appliances in each virtual datacenter.
 * 
 * @author Ignasi Barrera
 */
@ImplementedBy(ListVirtualAppliancesImpl.class)
public interface ListVirtualAppliances extends ListRootEntities<VirtualAppliance>
{

}
