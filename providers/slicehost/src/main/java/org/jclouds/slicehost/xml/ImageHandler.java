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
package org.jclouds.slicehost.xml;

import javax.annotation.Resource;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.logging.Logger;
import org.jclouds.slicehost.domain.Image;
import org.xml.sax.SAXException;

/**
 * @author Adrian Cole
 */
public class ImageHandler extends ParseSax.HandlerWithResult<Image> {
   private StringBuilder currentText = new StringBuilder();

   private int id;
   private String name;

   private Image image;
   @Resource
   protected Logger logger = Logger.NULL;

   public Image getResult() {
      return image;
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (qName.equalsIgnoreCase("id")) {
         id = Integer.parseInt(currentText.toString().trim());
      } else if (qName.equalsIgnoreCase("name")) {
         this.name = currentText.toString().trim();
      } else if (qName.equalsIgnoreCase("image")) {
         this.image = new Image(id, name);
         this.id = -1;
         this.name = null;
      }
      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}
