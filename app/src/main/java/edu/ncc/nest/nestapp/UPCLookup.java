package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class UPCLookup extends AppCompatActivity {
    private HashMap<String, String> fdcid_to_upc_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_p_c_lookup);

        this.fdcid_to_upc_map = new HashMap<String, String>();
        try {
            InputStream lookupTableInputStream = this.getResources().openRawResource(R.raw.fdcid_to_upc);
            InputStreamReader isr = new InputStreamReader(lookupTableInputStream);
            BufferedReader lookupTableReader = new BufferedReader(isr);

            String line = lookupTableReader.readLine();
            while(line != null) {
                String[] splitLine = line.split(",");
                this.fdcid_to_upc_map.put(splitLine[1], splitLine[0]);
                line = lookupTableReader.readLine();
            }

            lookupTableReader.close();
            isr.close();
            lookupTableInputStream.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        Log.d("INFO", "There are " + Integer.toString(this.fdcid_to_upc_map.size()) + " objects in the HashMap.");
    }
}
