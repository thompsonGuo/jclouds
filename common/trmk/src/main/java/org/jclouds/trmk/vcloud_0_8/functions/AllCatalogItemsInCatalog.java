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
package org.jclouds.trmk.vcloud_0_8.functions;

import static com.google.common.collect.Iterables.filter;
import static org.jclouds.concurrent.FutureIterables.transformParallel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.logging.Logger;
import org.jclouds.trmk.vcloud_0_8.TerremarkVCloudAsyncClient;
import org.jclouds.trmk.vcloud_0_8.TerremarkVCloudMediaType;
import org.jclouds.trmk.vcloud_0_8.domain.Catalog;
import org.jclouds.trmk.vcloud_0_8.domain.CatalogItem;
import org.jclouds.trmk.vcloud_0_8.domain.ReferenceType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author Adrian Cole
 */
@Singleton
public class AllCatalogItemsInCatalog implements Function<Catalog, Iterable<? extends CatalogItem>> {
   @Resource
   public Logger logger = Logger.NULL;

   private final TerremarkVCloudAsyncClient aclient;
   private final ExecutorService executor;

   @Inject
   AllCatalogItemsInCatalog(TerremarkVCloudAsyncClient aclient,
            @Named(Constants.PROPERTY_USER_THREADS) ExecutorService executor) {
      this.aclient = aclient;
      this.executor = executor;
   }

   @Override
   public Iterable<? extends CatalogItem> apply(Catalog from) {

      Iterable<? extends CatalogItem> catalogItems = transformParallel(filter(from.values(), new Predicate<ReferenceType>() {

         @Override
         public boolean apply(ReferenceType input) {
            return input.getType().equals(TerremarkVCloudMediaType.CATALOGITEM_XML);
         }

      }), new Function<ReferenceType, Future<? extends CatalogItem>>() {

         @SuppressWarnings("unchecked")
         @Override
         public Future<CatalogItem> apply(ReferenceType from) {
            return (Future<CatalogItem>) aclient.getCatalogItem(from.getHref());
         }

      }, executor, null, logger, "catalogItems in " + from.getHref());
      return catalogItems;
   }

}