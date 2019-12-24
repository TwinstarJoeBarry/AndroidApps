package edu.ncc.nest.nestapp;
/**
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
public class InventoryEntry {
    private long id;
    private String itemId;
    private String itemName;
    private String pantryLife;
    private String barcodeNum;

    public InventoryEntry(long id, String itemId, String itemName, String pantryLife, String upc)
    {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.pantryLife = pantryLife;
        this.barcodeNum = upc;
    }

    public InventoryEntry( String itemName, String itemId, String pantryLife)
    {
        this.itemId = itemId;
        this.itemName = itemName;
        this.pantryLife = pantryLife;
    }


    public long getId() {
        return id;
    }

    public String getPantryLife(){ return pantryLife; }
    public String getItemName(){ return itemName; }
    public String getBarcodeNum(){ return barcodeNum; }
    public String getItemId(){ return itemId; }

    public boolean equals(Object otherDept)
    {
        return this.id == ((InventoryEntry)otherDept).id;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return "row: " + id + " itemId: " + itemId + " - Name: " + itemName + " - dopLife: " + pantryLife +"- UPC: "+ barcodeNum;
    }
}
