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

    String[] mCategory = {"Baby Food", "Baked Goods", "Beverages", "Condiments, Sauces & Canned Goods", "Dairy Products & Eggs", "Deli & Prepared Foods", "Food Purchased Frozen", "Grains, Beans & Pasta", "Meat", "Poultry", "Produce", "Seafood", "Shelf Stable Food", "Vegetarian Proteins"};

    String[] mSubExamples = {"Cereal, Dinners, Formula, etc.", "Baking, Refrigerated Dough, etc", "Sodas, Juices, Wine, Coffee, etc.", "BBQ, Ketchup, Broth, Cans, etc.", "Butter, Milk, Cheese, etc.", "Prepackaged Meat, Left Overs, Salads, PreCooked Foods, etc.", "Frozen Pizza, Frozen Stuff, Ice cream, etc.", "Dried Vegetables, Grains, Whole Foods, etc.", "Fresh Meat, Smoked, Stuffed, etc.", "Chicken, Birds, Something That Can Flap It's Wings, etc.", "Fruits, Vegetables, etc.", "Fresh Fish, Shellfish, Smoked, etc.", "Chocolate Syrup, oils, canned fruits, etc.", "Miso, Tofu, Soy..., etc."};

    int[] images = {R.drawable.baby_food, R.drawable.baked_goods, R.drawable.beverages, R.drawable.condiments_sauce_and_can_goods, R.drawable.dairy_products, R.drawable.deli_products, R.drawable.frozen_foods, R.drawable.grains_beans_pasta, R.drawable.meats, R.drawable.poultry, R.drawable.product_grocery, R.drawable.fishes, R.drawable.shelf, R.drawable.vegetales};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_inventory);

        listView = findViewById(R.id.listview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Adaptor newAdaptor = new Adaptor(this, mCategory, mSubExamples, images);
        listView.setAdapter(newAdaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
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
                if(position == 4)
                {
                    Toast.makeText(addToInventory.this, "Dairy Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 5)
                {
                    Toast.makeText(addToInventory.this, "Deli Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 6)
                {
                    Toast.makeText(addToInventory.this, "Food Purchased Frozen Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 7)
                {
                    Toast.makeText(addToInventory.this, "Grains Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 8)
                {
                    Toast.makeText(addToInventory.this, "Meat Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 9)
                {
                    Toast.makeText(addToInventory.this, "Poultry Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 10)
                {
                    Toast.makeText(addToInventory.this, "Produce Test", Toast.LENGTH_SHORT).show();
                }
                if(position == 11)
                {
                    Toast.makeText(addToInventory.this, "Sea food test", Toast.LENGTH_SHORT).show();
                }
                if(position == 12)
                {
                    Toast.makeText(addToInventory.this, "shelf food", Toast.LENGTH_SHORT).show();
                }
                if(position == 13)
                {
                    Toast.makeText(addToInventory.this, "vegetales Test", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class Adaptor extends ArrayAdapter<String>
    {
        Context context;
        String[] rCategory;
        String[] rSubExamples;
        int[] rImg;

        Adaptor (Context c, String[] category, String[] subExamples, int[] imgs)
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
