package edu.ncc.nest.nestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NestDBDataSource {
    private SQLiteDatabase db;
    private NestDBOpenHelper helper;
    public static String TABLE_NAME_NEST_UPCS = "nestUPCs";


    public NestDBDataSource(Context context) throws SQLException  {
        helper = NestDBOpenHelper.getInstance(context);
        this.db = helper.getWritableDatabase();
    }

//  opting to not have these methods...
//    public void open() throws SQLException {
//        // why have an open? since we don't need a close, just get the db as part of the constructor
//    }
//
//    public void close() {
//        // as NestDBOpenHelper is Singleton, do not call itd close() method; close not required anyway
//        // helper.close();
//    }

    /**
     * insertNewUPC method --
     * adds a new record the Nest UPCs table
     * @param upc upc code
     * @param brand brand name
     * @param description description
     * @param productId associated FoodKeeper product id
     */
    void insertNewUPC(String upc, String brand, String description, int productId) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put("upc", upc);
        values.put("brand", brand);
        values.put("description", description);
        values.put("productId", productId);
        db.insert(TABLE_NAME_NEST_UPCS, null, values);
    }

    /**
     * getProductIdFromUPC method -
     * looks up the given UPC code in the Nest UPCs table and returns the associated
     * product ID
     * @param upc  UPC code
     * @return integer productId field value if found, -1 otherwise
     */
    int getProductIdFromUPC(String upc) {
        int result = -1; // indicate not found
        String qry = "SELECT productId FROM nestUPCs WHERE upper(UPC) = upper(?)";
        Cursor c = db.rawQuery(qry, new String[]{upc});
        if(c.moveToFirst()){
            result = c.getInt(0);
        }
        c.close();
        return result;
    }

    /**
     * getNestUPC method --
     * looks up the related UPC, product and category information based
     * on the given upc
     * @param upc upc code
     * @return if found, a filled NestUPC pojo with all
     *          the key related information; null otherwise.
     */
    NestUPC getNestUPC(String upc) {
        NestUPC result = null;
        String qry = "SELECT * FROM view_upc_product_category_joined WHERE upper(UPC) = upper(?)";
        Cursor c = db.rawQuery(qry, new String[]{upc});
        if(c.moveToFirst()){
            result = new NestUPC(
                    c.getString(c.getColumnIndex("UPC")),
                    c.getString(c.getColumnIndex("brand")),
                    c.getString(c.getColumnIndex("description")),
                    c.getInt(c.getColumnIndex("productId")),
                    c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("subtitle")),
                    c.getInt(c.getColumnIndex("categoryId")),
                    c.getString(c.getColumnIndex("cat_desc"))
            );
        }
        c.close();
        return result;
    }

    /**
     * getShelfLivesForProduct method --
     * looks up the shelf life records for the given productId
     * @param productId the FoodKeeper product id to lookup
     * @return an ArrayList<ShelfLife> object, which will have no
     * contents if nothing is found
     */
    List<ShelfLife> getShelfLivesForProduct(int productId) {
        List<ShelfLife> result = new ArrayList<>();
        String qry = "SELECT * FROM view_shelf_lives_and_type_info_joined WHERE productId = ?";
        Cursor c = db.rawQuery(qry, new String[]{String.valueOf(productId)});
        while (c.moveToNext()){
            ShelfLife life = new ShelfLife(
                c.getInt(c.getColumnIndex("typeIndex")),
                c.getInt(c.getColumnIndex("min")),
                c.getInt(c.getColumnIndex("max")),
                c.getString(c.getColumnIndex("metric")),
                c.getString(c.getColumnIndex("tips"))
            );
            life.setCode(c.getString(c.getColumnIndex("typeCode")));
            life.setDesc(c.getString(c.getColumnIndex("description")));
            result.add(life);
        }
        c.close();
        return result;
    }

    // have method that takes "box"/"printed" expiration date and UPC and returns
    // ArrayList of pojos that have all the appropriate shelfLives info and the corresponding
    // calculated expiration dates (and even tips?)

}
