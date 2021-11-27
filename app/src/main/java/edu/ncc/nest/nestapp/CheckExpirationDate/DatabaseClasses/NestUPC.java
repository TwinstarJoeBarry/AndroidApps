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

package edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses;

import java.io.Serializable;

/**
 * This class holds information about a upc that is stored in the database.
 */
public class NestUPC implements Serializable {

    private String upc;
    private int productId;
    private String productName, productSubtitle;
    private int categoryId;
    private String catDesc;

    public NestUPC(String upc, int productId, String productName, String productSubtitle,
                   int categoryId, String catDesc) {
        this.upc = upc;
        this.productId = productId;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.categoryId = categoryId;
        this.catDesc = catDesc;
    }

    public String getUpc() {
        return upc;
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
