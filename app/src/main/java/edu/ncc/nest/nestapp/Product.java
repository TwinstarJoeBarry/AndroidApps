package edu.ncc.nest.nestapp;
/*
 *
 * Copyright (C) 2019 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  Product class --
 *  Models a product from the FoodKeeper API
 */
public class Product {
    private int id;
    private int categoryId;
    private String name;
    private String subtitle;
    private String keywords;
    private List<ShelfLife> shelfLives;

    /**
     * Product constructor (direct) --
     * create basic Product object via passed in values
     * @param id product id number
     * @param categoryId category id number
     * @param name name of product
     * @param subtitle extended name
     */
    public Product(int id, int categoryId, String name, String subtitle, String keywords) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.subtitle = subtitle;
        this.keywords = keywords;
        shelfLives = new ArrayList<>();
    }

    /**
     * Product constructor (via JSON) --
     * creates a full Product object by parsing given JSONObject
     * @param apiProduct JSONObject representing one Product as retrieved via the
     *                   FoodKeeper API
     * @throws JSONException thrown on any parsing error
     */
    public Product(JSONObject apiProduct) throws JSONException {
        this.id = apiProduct.getInt("id");
        this.categoryId = apiProduct.getInt("categoryId");
        this.name = apiProduct.getString("name");
        this.subtitle = apiProduct.getString("subtitle");
        this.keywords = apiProduct.getString("keywords");
        shelfLives = new ArrayList<>();
        addShelfLifeIfApplicable(ShelfLife.PL    , apiProduct);
        addShelfLifeIfApplicable(ShelfLife.PAOL  , apiProduct);
        addShelfLifeIfApplicable(ShelfLife.RL    , apiProduct);
        addShelfLifeIfApplicable(ShelfLife.RAOL  , apiProduct);
        addShelfLifeIfApplicable(ShelfLife.RATL  , apiProduct);
        addShelfLifeIfApplicable(ShelfLife.FL    , apiProduct);
        addShelfLifeIfApplicable(ShelfLife.DOP_PL, apiProduct);
        addShelfLifeIfApplicable(ShelfLife.DOP_RL, apiProduct);
        addShelfLifeIfApplicable(ShelfLife.DOP_FL, apiProduct);
    }

    /**
     * addShelfLifeIfApplicable method --
     *
     * @param type constant from ShelfLife class indicating which set of shelf life values to 
     *             look at
     * @param apiProduct JSONObject representing one Product as retrieved via the
     *                   FoodKeeper API
     * @throws JSONException thrown on any parsing error
     */
    private void addShelfLifeIfApplicable(int type, JSONObject apiProduct) throws JSONException {
        // determine which shelf life to look at and parse a new JSONObject for that shelf life type
        JSONObject jsonShelf;
        switch (type) {
            case ShelfLife.PAOL  : jsonShelf = apiProduct.getJSONObject("pantryAfterOpeningLife"); break;
            case ShelfLife.RL    : jsonShelf = apiProduct.getJSONObject("refrigeratorLife"); break;
            case ShelfLife.RAOL  : jsonShelf = apiProduct.getJSONObject("refrigerateAfterOpeningLife"); break;
            case ShelfLife.RATL  : jsonShelf = apiProduct.getJSONObject("refrigerateAfterThawingLife"); break;
            case ShelfLife.FL    : jsonShelf = apiProduct.getJSONObject("freezerLife"); break;
            case ShelfLife.DOP_PL: jsonShelf = apiProduct.getJSONObject("dop_pantryLife"); break;
            case ShelfLife.DOP_RL: jsonShelf = apiProduct.getJSONObject("dop_refrigeratorLife"); break;
            case ShelfLife.DOP_FL: jsonShelf = apiProduct.getJSONObject("dop_freezerLife"); break;
            default:
                jsonShelf = apiProduct.getJSONObject("pantryLife");
                break;
        }
        // if the min field isn't null, this type of storage has info
        if (!jsonShelf.isNull("min"))
            shelfLives.add(
                    new ShelfLife(
                            type,
                            jsonShelf.getInt("min"),
                            jsonShelf.getInt("max"),
                            jsonShelf.getString("metric"),
                            jsonShelf.getString("tips")
                            ));
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<ShelfLife> getShelfLives() {
        return shelfLives;
    }

    public void addShelfLife(ShelfLife shelfLife) {
        shelfLives.add(shelfLife);
    }

    /**
     * toString method --
     * set as needed; possibly for display in a list
     * @return String representing the state of the object
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subtitle='" + subtitle + '\'' +
                '}';
    }

    /**
     * equals method --
     * set as needed; for now just compares id values
     * @param o object to compare this one against
     * @return true if the given object is equal to this one, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return id == that.id;
    }

}
