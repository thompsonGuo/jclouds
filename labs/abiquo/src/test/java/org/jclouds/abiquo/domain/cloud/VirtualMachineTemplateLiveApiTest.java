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
package org.jclouds.abiquo.domain.cloud;

import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.config.Category;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.internal.BaseAbiquoApiLiveApiTest;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link VirtualMachineTemplate} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "api", testName = "VirtualMachineTemplateLiveApiTest")
public class VirtualMachineTemplateLiveApiTest extends BaseAbiquoApiLiveApiTest
{

    public void testGetParent()
    {
        Datacenter datacenter = env.virtualMachine.getTemplate().getDatacenter();
        assertNotNull(datacenter);
        assertEquals(datacenter.getId(), env.datacenter.getId());
    }

    public void testGetCategory()
    {
        Category category = env.virtualMachine.getTemplate().getCategory();
        assertNotNull(category);
    }

    public void testRequestConversionToSameFormat()
    {
        try
        {
            env.virtualMachine.getTemplate().requestConversion(
                env.virtualMachine.getTemplate().getDiskFormatType());
            fail("Should not be able to create create a conversion to the base format");
        }
        catch (AbiquoException ex)
        {
            assertHasError(ex, Status.CONFLICT, "CONVERSION-3");
        }
    }

}
