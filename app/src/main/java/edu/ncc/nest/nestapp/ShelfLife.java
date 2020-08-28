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

import java.util.Objects;

/**
 * ShelfLife class --
 * models a ShelfLife from the FoodKeeper API
 */
public class ShelfLife {
    // constants for the different types of shelf life
    // todo make enum?
    public static final int PL     = 0;  // pantryLife
    public static final int PAOL   = 1;  // pantryAfterOpeningLife
    public static final int RL     = 2;  // refrigeratorLife
    public static final int RAOL   = 3;  // refrigerateAfterOpeningLife
    public static final int RATL   = 4;  // refrigerateAfterThawingLife
    public static final int FL     = 5;  // freezerLife
    public static final int DOP_PL = 6;  // dop_pantryLife
    public static final int DOP_RL = 7;  // dop_refrigeratorLife
    public static final int DOP_FL = 8;  // dop_freezerLife
    // todo dop versions needed? or is it either or? investigate

    private int type;       // Storage Type (see constants defined above)
    private String desc;    // descriptive text [comes from shelfLifeTypes table in NestDB]
    private String code;    // code corresponding to type [comes from shelfLifeTypes table in NestDB]
    private int min;        // Minimum time for shelf life
    private int max;        // Maximum time for shelf life
    private String metric;  // Metric for shelf life
    private String tips;    // Storage tips


    /**
     * ShelfLife constructor --
     * create ShelfLife object via passed in values
     * @param type constant from this class indicating which set of shelf life this represents
     * @param min minimum time for shelf life
     * @param max maximum time for shelf life
     * @param metric unit of shelf life (days, weeks, etc.)
     * @param tips storage tips
     */
    public ShelfLife(int type, int min, int max, String metric, String tips) {
        this.type = type;
        this.min = min;
        this.max = max;
        this.metric = metric;
        this.tips = tips;
    }

    // getters and setters
    public int getType() {
        return type;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * toString method --
     * set as needed; can be used to create displayable shelf life information
     * @return String representing the state of the object
     */
    @Override
    public String toString() {
        // todo build informative string ala https://www.foodsafety.gov/keep-food-safe/foodkeeper-app
        return "ShelfLife{" +
                "type=" + type +
                ", min=" + min +
                ", max=" + max +
                ", metric='" + metric + '\'' +
                ", tips='" + tips + '\'' +
                '}';
    }

    /**
     * equals method --
     * set as needed, for now compares all values
     * @param o object to compare this one against
     * @return true if the given object is equal to this one, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShelfLife shelfLife = (ShelfLife) o;
        return type == shelfLife.type &&
                min == shelfLife.min &&
                max == shelfLife.max &&
                Objects.equals(metric, shelfLife.metric) &&
                Objects.equals(tips, shelfLife.tips);
    }

}
