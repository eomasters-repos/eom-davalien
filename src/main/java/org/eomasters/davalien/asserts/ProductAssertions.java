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

package org.eomasters.davalien.asserts;

import org.esa.snap.core.datamodel.MetadataElement;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductNodeGroup;
import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.datamodel.VectorDataNode;

/**
 * Assertions for a {@link Product} and its elements.
 */
public class ProductAssertions {

  /**
   * Creates a new instance of the {@link ProductAssert} class.
   *
   * @param actual the {@link Product} to verify
   * @return the created {@link ProductAssert}
   */
  public static ProductAssert assertThat(Product actual) {
    return new ProductAssert(actual);
  }

  /**
   * Creates a new instance of the {@link RasterAssert} class.
   *
   * @param actual the {@link RasterDataNode} to verify
   * @return the created {@link RasterAssert}
   */
  public static RasterAssert assertThat(RasterDataNode actual) {
    return new RasterAssert(actual);
  }

  /**
   * Creates a new instance of the {@link MetadataAssert} class.
   *
   * @param actual the {@link MetadataElement} to verify
   * @return the created {@link MetadataAssert}
   */
  public static MetadataAssert assertThat(MetadataElement actual) {
    return new MetadataAssert(actual);
  }

  /**
   * Creates a new instance of the {@link VectorDataNodeAssert} class.
   *
   * @param actual the {@link VectorDataNode} to verify
   * @return the created {@link VectorDataNodeAssert}
   */
  public static VectorDataNodeAssert assertThat(ProductNodeGroup<VectorDataNode> actual) {
    return new VectorDataNodeAssert(actual);
  }

}
