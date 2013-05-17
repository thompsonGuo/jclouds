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
package org.jclouds.vcloud.director.v1_5.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.vcloud.director.v1_5.predicates.ReferencePredicates.nameEquals;

import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.vcloud.director.v1_5.domain.Reference;
import org.jclouds.vcloud.director.v1_5.domain.Catalog;
import org.jclouds.vcloud.director.v1_5.endpoints.Org;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

/**
 * 
 * @author danikov
 */
@Singleton
public class OrgNameCatalogNameItemNameToEndpoint implements Function<Object, URI> {
   private final Supplier<Map<String, Map<String, Catalog>>> orgCatalogMap;
   private final Supplier<Reference> defaultOrg;
   private final Supplier<Reference> defaultCatalog;

   @Inject
   public OrgNameCatalogNameItemNameToEndpoint(
         Supplier<Map<String, Map<String, Catalog>>> orgCatalogMap,
         @Org Supplier<Reference> defaultOrg, 
         @org.jclouds.vcloud.director.v1_5.endpoints.Catalog Supplier<Reference> defaultCatalog) {
      this.orgCatalogMap = orgCatalogMap;
      this.defaultOrg = defaultOrg;
      this.defaultCatalog = defaultCatalog;
   }

   @SuppressWarnings("unchecked")
   public URI apply(Object from) {
      Iterable<Object> orgCatalog = (Iterable<Object>) checkNotNull(from, "args");
      Object org = Iterables.get(orgCatalog, 0);
      Object catalog = Iterables.get(orgCatalog, 1);
      Object catalogItem = Iterables.get(orgCatalog, 2);
      if (org == null)
         org = defaultOrg.get().getName();
      if (catalog == null)
         catalog = defaultCatalog.get().getName();
      try {
         Map<String, Catalog> catalogs = checkNotNull(orgCatalogMap.get().get(org));
         return Iterables.find(catalogs.get(catalog).getCatalogItems(), nameEquals((String)catalogItem)).getHref();
      } catch (NullPointerException e) {
         throw new NoSuchElementException(org + "/" + catalog + "/" + catalogItem + " not found in "
               + orgCatalogMap.get());
      }
   }

}
