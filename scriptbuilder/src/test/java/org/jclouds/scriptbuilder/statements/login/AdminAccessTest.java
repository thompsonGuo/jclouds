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
package org.jclouds.scriptbuilder.statements.login;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import org.jclouds.scriptbuilder.domain.OsFamily;
import org.testng.annotations.Test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit", singleThreaded = true, testName = "AdminAccessTest")
public class AdminAccessTest {

   public void testStandardUNIX() throws IOException {
      TestConfiguration.INSTANCE.reset();
      try {
         assertEquals(AdminAccess.standard().init(TestConfiguration.INSTANCE).render(OsFamily.UNIX),
               CharStreams.toString(Resources.newReaderSupplier(Resources.getResource("test_adminaccess_standard.sh"),
                     Charsets.UTF_8)));
      } finally {
         TestConfiguration.INSTANCE.reset();
      }
   }

   public void testWithParamsUNIX() throws IOException {
      TestConfiguration.INSTANCE.reset();
      try {
         assertEquals(
               AdminAccess.builder().adminPassword("bar").adminPrivateKey("fooPrivateKey")
                     .adminPublicKey("fooPublicKey").adminUsername("foo")
                     .adminHome("/over/ridden/foo").build()
                     .init(TestConfiguration.INSTANCE).render(OsFamily.UNIX), 
                     CharStreams.toString(Resources.newReaderSupplier(
                     Resources.getResource("test_adminaccess_params.sh"), Charsets.UTF_8)));

      } finally {
         TestConfiguration.INSTANCE.reset();
      }
   }

   public void testWithParamsAndFullNameUNIX() throws IOException {
      TestConfiguration.INSTANCE.reset();
      try {
         assertEquals(
               AdminAccess.builder().adminPassword("bar").adminPrivateKey("fooPrivateKey")
                     .adminPublicKey("fooPublicKey").adminUsername("foo").adminFullName("JClouds Foo")
                     .adminHome("/over/ridden/foo").build()
                     .init(TestConfiguration.INSTANCE).render(OsFamily.UNIX),
               CharStreams.toString(Resources.newReaderSupplier(
                     Resources.getResource("test_adminaccess_params_and_fullname.sh"), Charsets.UTF_8)));

      } finally {
         TestConfiguration.INSTANCE.reset();
      }
   }


   public void testOnlyInstallUserUNIX() throws IOException {
      TestConfiguration.INSTANCE.reset();
      try {
         assertEquals(
               AdminAccess.builder().grantSudoToAdminUser(false).authorizeAdminPublicKey(true)
                     .installAdminPrivateKey(true).lockSsh(false).resetLoginPassword(false).build()
                     .init(TestConfiguration.INSTANCE).render(OsFamily.UNIX), CharStreams.toString(Resources
                     .newReaderSupplier(Resources.getResource("test_adminaccess_plainuser.sh"), Charsets.UTF_8)));
      } finally {
         TestConfiguration.INSTANCE.reset();
      }
   }

   @Test(expectedExceptions = UnsupportedOperationException.class)
   public void testCreateWheelWindowsNotSupported() {
      AdminAccess.standard().init(TestConfiguration.INSTANCE).render(OsFamily.WINDOWS);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   // for issue 682
   public void testRootNotAllowed() throws IOException {
      TestConfiguration.INSTANCE.reset();
      try {
         AdminAccess.builder().adminUsername("root").build().init(TestConfiguration.INSTANCE).render(OsFamily.UNIX);
      } finally {
         TestConfiguration.INSTANCE.reset();
      }
   }

   @Test(expectedExceptions = NullPointerException.class)
   public void testFamilyRequiredAllowed() throws IOException {
      AdminAccess.standard().render(null);
   }

   public void testWhenUninitializedLazyInitWithDefaultConfiguration() throws IOException {
      AdminAccess access = AdminAccess.standard();
      // before rendered, holder is empty
      assertEquals(access.config.getAdminUsername(), null);
      assertEquals(access.config.getAdminPassword(), null);
      assertEquals(access.config.getAdminPublicKey(), null);
      assertEquals(access.config.getAdminPrivateKey(), null);
      assertEquals(access.config.getLoginPassword(), null);
      access.render(OsFamily.UNIX);
      // DefaultConfiguration
      try {
         assertEquals(access.config.getAdminUsername(), System.getProperty("user.name"));
         assertNotNull(access.config.getAdminPassword());
         assertNotNull(access.config.getAdminPublicKey());
         assertNotNull(access.config.getAdminPrivateKey());
         assertNotNull(access.config.getLoginPassword());
      } catch (AssertionError e) {
         throw e;
      } catch (Throwable e) {
         // we are catching throwables here, in case the test runner doesn't
         // have ssh keys setup
      }

   }

}
