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

import edu.ncc.nest.nestapp.UIFoods.BabyFood;
import edu.ncc.nest.nestapp.UIFoods.BakedGoods;
import edu.ncc.nest.nestapp.UIFoods.Beverages;
import edu.ncc.nest.nestapp.UIFoods.Condiments;
import edu.ncc.nest.nestapp.UIFoods.Dairy;
import edu.ncc.nest.nestapp.UIFoods.Deli;
import edu.ncc.nest.nestapp.UIFoods.FrozenFood;
import edu.ncc.nest.nestapp.UIFoods.Grains;
import edu.ncc.nest.nestapp.UIFoods.Meat;
import edu.ncc.nest.nestapp.UIFoods.Poultry;
import edu.ncc.nest.nestapp.UIFoods.Produce;
import edu.ncc.nest.nestapp.UIFoods.Seafood;
import edu.ncc.nest.nestapp.UIFoods.ShelfFoods;
import edu.ncc.nest.nestapp.UIFoods.VegProteins;


public class addToInventory extends AppCompatActivity {

    ListView listView;
    String[] mCategory = {"Baby Food", "Baked Goods", "Beverages", "Condiments, Sauces & Canned Goods", "Dairy Products & Eggs", "Deli & Prepared Foods", "Food Purchased Frozen", "Grains, Beans & Pasta", "Meat", "Poultry", "Produce", "Seafood", "Shelf Stable Food", "Vegetarian Proteins"};
    String[] mSubExamples = {"Cereal, Dinners, Formula, etc.", "Baking, Refrigerated Dough, etc", "Sodas, Juices, Wine, Coffee, etc.", "BBQ, Ketchup, Broth, Cans, etc.", "Butter, Milk, Cheese, etc.", "Prepackaged Meat, Salads, PreCooked Foods, etc.", "Frozen Pizza, Frozen Stuff, Ice cream, etc.", "Dried Vegetables, Grains, Whole Foods, etc.", "Fresh Meat, Smoked, Stuffed, etc.", "Chicken, Birds, etc.", "Fruits, Vegetables, etc.", "Fresh Fish, Shellfish, Smoked, etc.", "Chocolate Syrup, oils, canned fruits, etc.", "Miso, Tofu, Soy..., etc."};
    int[] images = {R.drawable.baby_food, R.drawable.baked_goods, R.drawable.beverages, R.drawable.condiments_sauce_and_can_goods, R.drawable.dairy, R.drawable.deli_meats, R.drawable.frozen_foods, R.drawable.grains_beans_pasta, R.drawable.meats, R.drawable.poultry, R.drawable.product_grocery, R.drawable.fishes, R.drawable.shelf, R.drawable.vegetales};


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
                    questionaire(mCategory[0]);
                }
                if(position == 1)
                {
                    Toast.makeText(addToInventory.this, "Baked Goods Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[1]);
                }
                if(position == 2)
                {
                    Toast.makeText(addToInventory.this, "Beverages Test", Toast.LENGTH_SHORT).show();
                   questionaire(mCategory[2]);
                }
                if(position == 3)
                {
                    Toast.makeText(addToInventory.this, "Condiments Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[3]);
                }
                if(position == 4)
                {
                    Toast.makeText(addToInventory.this, "Dairy Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[4]);
                }
                if(position == 5)
                {
                    Toast.makeText(addToInventory.this, "Deli Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[5]);
                }
                if(position == 6)
                {
                    Toast.makeText(addToInventory.this, "Food Purchased Frozen Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[6]);
                }
                if(position == 7)
                {
                    Toast.makeText(addToInventory.this, "Grains Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[7]);
                }
                if(position == 8)
                {
                    Toast.makeText(addToInventory.this, "Meat Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[8]);
                }
                if(position == 9)
                {
                    Toast.makeText(addToInventory.this, "Poultry Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[9]);
                }
                if(position == 10)
                {
                    Toast.makeText(addToInventory.this, "Produce Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[10]);
                }
                if(position == 11)
                {
                    Toast.makeText(addToInventory.this, "Sea food test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[11]);
                }
                if(position == 12)
                {
                    Toast.makeText(addToInventory.this, "Shelf food", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[12]);
                }
                if(position == 13)
                {
                    Toast.makeText(addToInventory.this, "Vegetales Test", Toast.LENGTH_SHORT).show();
                    questionaire(mCategory[13]);
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
        if (item.getItemId() == R.id.home_btn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method - goes to the nest home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

    /**
     * questionaire method -- sends out the intent(name of category) to the ATIQuestionaire activity
     * @param category
     */
    public void questionaire(String category){
        Intent intent = new Intent(this,ATIQuestionaire.class);
        String catName = category;
        intent.putExtra("categoryLabel",catName);
        startActivity(intent);
    }

    //--------May or may not be used for this activity-----------
    /**
     * launchBabyFood - starts the BabyFood activity
     */
    public void launchBabyFood() {
        Intent intent = new Intent(this, BabyFood.class);
        startActivity(intent);
    }

    /**
     * launchBakedGoods - starts the BakedGoods activity
     */
    public void launchBakedGoods() {
        Intent intent = new Intent(this, BakedGoods.class);
        startActivity(intent);
    }

    /**
     * launchBakedBeverages - starts the Beverages activity
     */
    public void launchBeverages() {
        Intent intent = new Intent(this, Beverages.class);
        startActivity(intent);
    }

    /**
     * launchCondiments - starts the Condiments activity
     */
    public void launchCondiments() {
        Intent intent = new Intent(this, Condiments.class);
        startActivity(intent);
    }

    /**
     * launchDairy - starts the Diary activity
     */
    public void launchDairy() {
        Intent intent = new Intent(this, Dairy.class);
        startActivity(intent);
    }

    /**
     * launchDeli - starts the Deli activity
     */
    public void launchDeli() {
        Intent intent = new Intent(this, Deli.class);
        startActivity(intent);
    }

    /**
     * launchFrozenFood - starts the FrozenFoods activity
     */
    public void launchFrozenFoods() {
        Intent intent = new Intent(this, FrozenFood.class);
        startActivity(intent);
    }

    /**
     * launchGrains - starts the Grains activity
     */
    public void launchGrains() {
        Intent intent = new Intent(this, Grains.class);
        startActivity(intent);
    }

    /**
     * launchMeta - starts the Meat activity
     */
    public void launchMeat() {
        Intent intent = new Intent(this, Meat.class);
        startActivity(intent);
    }

    /**
     * launchPoultry - starts the Poultry activity
     */
    public void launchPoultry() {
        Intent intent = new Intent(this, Poultry.class);
        startActivity(intent);
    }

    /**
     * launchProduce - starts the Produce activity
     */
    public void launchProduce() {
        Intent intent = new Intent(this, Produce.class);
        startActivity(intent);
    }

    /**
     * launchSeafood - starts the Seafood activity
     */
    public void launchSeafood() {
        Intent intent = new Intent(this, Seafood.class);
        startActivity(intent);
    }

    /**
     * launchShelfFoods - starts the ShelfFoods activity
     */
    public void launchShelfFoods() {
        Intent intent = new Intent(this, ShelfFoods.class);
        startActivity(intent);
    }

    /**
     * launchVegProteins - starts the VegProteins activity
     */
    public void launchVegProteins() {
        Intent intent = new Intent(this, VegProteins.class);
        startActivity(intent);
    }

}
