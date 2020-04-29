package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class UPCLookup extends AppCompatActivity {
    private SQLiteDatabase fdcid_to_upc_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_p_c_lookup);

        this.fdcid_to_upc_database = SQLiteDatabase.openDatabase(this.getResources().getResourceEntryName(R.raw.fdcid_to_upc), null, SQLiteDatabase.OPEN_READONLY);
    }

    public int getFDCID(long upc) {
        return 0; //TODO
    }
}
