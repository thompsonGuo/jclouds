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
package org.jclouds.trmk.vcloud_0_8.compute.strategy;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.domain.Image;
import org.jclouds.compute.strategy.GetImageStrategy;
import org.jclouds.trmk.vcloud_0_8.TerremarkVCloudClient;
import org.jclouds.trmk.vcloud_0_8.domain.VAppTemplate;

import com.google.common.base.Function;

/**
 * @author Adrian Cole
 */
@Singleton
public class TerremarkVCloudGetImageStrategy implements GetImageStrategy {

   protected final TerremarkVCloudClient client;
   protected final Function<VAppTemplate, Image> vAppToImage;

   @Inject
   protected TerremarkVCloudGetImageStrategy(TerremarkVCloudClient client, Function<VAppTemplate, Image> vAppToImage) {
      this.client = checkNotNull(client, "client");
      this.vAppToImage = vAppToImage;
   }

   @Override
   public Image getImage(String in) {
      URI id = URI.create(in);
      VAppTemplate from = client.getVAppTemplate(id);
      if (from == null)
         return null;
      return vAppToImage.apply(from);
   }

}