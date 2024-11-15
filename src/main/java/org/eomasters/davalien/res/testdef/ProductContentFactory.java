/*-
 * ========================LICENSE_START=================================
 * EOMasters GPT Test Environment - This projects provides a test environment for operators you have developed.
 * -> https://www.eomasters.org/
 * ======================================================================
 * Copyright (C) 2023 - 2024 Marco Peters
 * ======================================================================
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * -> http://www.gnu.org/licenses/gpl-3.0.html
 * =========================LICENSE_END==================================
 */

package org.eomasters.davalien.res.testdef;

import java.io.IOException;
import org.eomasters.davalien.DavalienException;
import org.esa.snap.core.datamodel.Product;

/**
 * A factory for creating {@link ProductContent} based on a {@link Product}. It selects elements of a product which will
 * be tested and add include them in the returned {@link ProductContent}.
 */
public class ProductContentFactory {

  /**
   * Create a {@link ProductContent} based on a {@link Product}.
   *
   * @param product the product
   * @return the {@link ProductContent}
   * @throws DavalienException if something goes wrong
   */
  public static ProductContent create(Product product) throws DavalienException {
    return create(product, new DefaultContentDefinition(product));
  }

  private static ProductContent create(Product product, ContentDefinition cd) throws DavalienException {
    ProductContent pc = new ProductContent();
    pc.setName(product.getName());
    pc.setDescription(product.getDescription());
    pc.setProductType(product.getProductType());
    pc.setSceneSize(product.getSceneRasterSize());
    pc.setStartTime(product.getStartTime());
    pc.setEndTime(product.getEndTime());
    pc.setGeoLocations(cd.getGeoLocationsForNode(product));
    try {
      pc.setRasters(cd.getRasters());
      pc.setVectorData(cd.getVectors());
    } catch (IOException e) {
      throw new DavalienException(e);
    }
    pc.setMetadata(cd.getMetadata());
    pc.setSampleCoding(cd.getCodings());
    return pc;
  }

}
