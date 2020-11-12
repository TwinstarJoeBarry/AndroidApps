/**
 *

 Copyright (C) 2020 The LibreFoodPantry Developers.


 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.


 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.


 You should have received a copy of the GNU General Public License
 along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package edu.ncc.nest.nestapp;

/**
 * this POJO holds the related upc, product and category fields
 * needed for lookup and processing of a Nest UPC code
 */
public class NestUPC {
    private String upc, brand, description;
    private int productId;
    private String productName, productSubtitle;
    private int categoryId;
    private String catDesc;

    public NestUPC(String upc, String brand, String description, int productId, String productName, String productSubtitle, int categoryId, String catDesc) {
        this.upc = upc;
        this.brand = brand;
        this.description = description;
        this.productId = productId;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.categoryId = categoryId;
        this.catDesc = catDesc;
    }

    public String getUpc() {
        return upc;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCatDesc() {
        return catDesc;
    }
}
