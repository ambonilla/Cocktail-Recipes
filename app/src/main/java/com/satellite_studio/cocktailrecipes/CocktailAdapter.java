package com.satellite_studio.cocktailrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.satellite_studio.cocktailrecipes.cocktails.CocktailItem;

import java.util.List;

/**
 * Created by antiadriano on 8/9/15.
 */

public class CocktailAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    Context cocktailContext;
    List<CocktailItem> cocktailList;



    public CocktailAdapter(Context context, List<CocktailItem> objects){
        mLayoutInflater = LayoutInflater.from(context);
        this.cocktailContext = context;
        this.cocktailList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currView = convertView;
        ViewHolder viewHolder;
        float oddOrEven = (float) position % 2;
        if (currView == null){
            if(oddOrEven > 0) {
                currView = mLayoutInflater.inflate(R.layout.cocktail_row, parent, false);
            }
            else{
                currView = mLayoutInflater.inflate(R.layout.cocktail_row_even, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.cocktailName = (TextView) currView.findViewById(R.id.cocktail_name);
            currView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) currView.getTag();
        }

        final CocktailItem cocktailItem = getItem(position);

        viewHolder.cocktailName.setText(cocktailItem.name);

        return currView;
    }

    @Override
    public CocktailItem getItem(int position) {
        return cocktailList.get(position);
    }

    @Override
    public int getCount() {
        return cocktailList.size();
    }

    @Override
    public long getItemId(int position) {
        return cocktailList.indexOf(getItem(position));
    }

    private static class ViewHolder{
        TextView cocktailName;
    }
}
