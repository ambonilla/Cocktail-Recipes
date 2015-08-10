package com.satellite_studio.cocktailrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.satellite_studio.cocktailrecipes.cocktails.CocktailItem;

import java.util.List;

/**
 * Created by antiadriano on 8/9/15.
 */

public class CocktailAdapter extends ArrayAdapter<CocktailItem> {

    private LayoutInflater mLayoutInflater;

    public CocktailAdapter(Context context, List<CocktailItem> objects){
        super(context,0, objects);
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currView = convertView;
        ViewHolder viewHolder;
        if (currView == null){
            LayoutInflater inflater = (LayoutInflater)
        }
    }

    private static class ViewHolder{
        TextView cocktailName;
    }
}
