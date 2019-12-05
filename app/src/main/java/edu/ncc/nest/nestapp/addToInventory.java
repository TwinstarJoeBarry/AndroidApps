package edu.ncc.nest.nestapp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class addToInventory extends AppCompatActivity {

    ListView listView;
    String mcategory[] = {"Baby Food", "Baked Goods", "Beverages", "Condiments, Sauces & Canned Goods"};
    String msubExamples[] = {"Cereal, Dinners, Formula, etc.", "Baking, Refrigerated Dough, etc", "Sodas, Juices, Wine, Coffee, etc.", "BBQ, Ketchup, Broth, Cans, etc."};
    int images[] = {R.drawable.baby_food, R.drawable.baked_goods, R.drawable.beverages, R.drawable.condiments_sauce_and_can_goods};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_inventory);

        listView = findViewById(R.id.listview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Adaptor newAdaptor = new Adaptor(this, mcategory, msubExamples, images);
        listView.setAdapter(newAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    Toast.makeText(addToInventory.this, "Baby Food Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 1)
                {
                    Toast.makeText(addToInventory.this, "Baked Goods Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 2)
                {
                    Toast.makeText(addToInventory.this, "Beverages Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 3)
                {
                    Toast.makeText(addToInventory.this, "Condiments Test", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class Adaptor extends ArrayAdapter<String>
    {
        Context context;
        String rCategory[];
        String rSubExamples[];
        int rImg[];

        Adaptor (Context c, String category[], String subExamples[], int imgs[])
        {
            super(c,R.layout.rows, R.id.mainText, category);
            this.context = c;
            this.rCategory = category;
            this.rSubExamples = subExamples;
            this.rImg = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rows, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView theCategory = row.findViewById(R.id.mainText);
            TextView theSubExample = row.findViewById(R.id.subText);

            images.setImageResource(rImg[position]);
            theCategory.setText(rCategory[position]);
            theSubExample.setText(rSubExamples[position]);


            return row;
        }
    }




    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method - goes to the home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }
}
