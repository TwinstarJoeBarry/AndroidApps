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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class InventoryEntry {
    private long id;
    private String itemId;
    private String itemName;
    private String pantryLife;
    private String barcodeNum;

    /**
     * constructs a new InventoryEntry by connecting to the FoodKeeper API and getting data from there.
     *
     * @param foodKeeperID the id number of the food item in the FoodKeeper API
     * @throws IOException if the connection fails
     * @throws JSONException if the FoodKeeper API sends back malformed JSON
     */
    public InventoryEntry(int foodKeeperID) throws IOException, JSONException
    {
        URL foodKeeperURL = null;
        try {
            foodKeeperURL = new URL("https://foodkeeper-api.herokuapp.com/products/" + Integer.toString(foodKeeperID));
        }catch(MalformedURLException ex) {}
        URLConnection foodKeeperConnection = foodKeeperURL.openConnection();
        foodKeeperConnection.connect();

        InputStream foodKeeperStream = foodKeeperConnection.getInputStream();
        StringBuilder streamInputBuilder = new StringBuilder();
        int next = foodKeeperStream.read();
        while(next != -1) {
            streamInputBuilder.append((char) next);
            next = foodKeeperStream.read();
        }
        streamInputBuilder.trimToSize();
        foodKeeperStream.close();


        JSONObject foodKeeperData = new JSONObject(streamInputBuilder.toString());
        JSONObject pantryLifeData = foodKeeperData.getJSONObject("pantryLife");

        this.id = foodKeeperID;
        this.itemId = Integer.toString(foodKeeperID);
        this.itemName = foodKeeperData.getString("name");
        this.pantryLife = pantryLifeData.get("min") == null ? null : Integer.toString(pantryLifeData.getInt("min")) + "-" + Integer.toString(pantryLifeData.getInt("max")) + " " + pantryLifeData.getString("metric");
        this.barcodeNum = null; // TODO: the FoodPantry API doesn't have a way to get the barcode.
    }

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
        return otherDept != null && otherDept instanceof InventoryEntry && this.id == ((InventoryEntry)otherDept).id;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return "row: " + id + " itemId: " + itemId + " - Name: " + itemName + " - dopLife: " + pantryLife +"- UPC: "+ barcodeNum;
    }
}
