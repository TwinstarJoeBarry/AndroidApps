package edu.ncc.nest.nestapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter{

    private Context context;

    public CategoryAdapter(Context ctx)
    {
        context = ctx;
    }




    @Override
    public int getCount()
    {
        return addToInventory.categoryImages.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.category_list, null);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        imageView.setImageResource(addToInventory.categoryImages[position]);

        return convertView;
    }



}
