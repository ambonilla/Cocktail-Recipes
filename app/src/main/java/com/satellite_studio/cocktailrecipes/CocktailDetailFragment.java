package com.satellite_studio.cocktailrecipes;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.satellite_studio.cocktailrecipes.cocktails.CocktailItem;

/**
 * A fragment representing a single Cocktail detail screen.
 * This fragment is either contained in a {@link CocktailListActivity}
 * in two-pane mode (on tablets) or a {@link CocktailDetailActivity}
 * on handsets.
 */
public class CocktailDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_POSITION = "position";

    private String currentPosition = "";
    private String cocktailDescription = "";
    private String cocktailIngredients = "";

    DBAdapter db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cocktail_detail, container, false);

        if(savedInstanceState != null) {
            currentPosition = savedInstanceState.getString(ARG_POSITION);
        }
        return rootView;
    }

    public void getCocktailData(String cocktailName){
        Context currCtx = getActivity();
        db = new DBAdapter(currCtx);
        db.openDataBase();
        Cursor c = db.getCocktail(cocktailName);

        if(c.moveToFirst()){
            do{
                cocktailDescription = c.getString(2);
                cocktailIngredients = c.getString(1);
            }
            while(c.moveToNext());
        }

        db.close();
    }

    public void updatedArticleView(String name){
        View v = getView();
        getCocktailData(name);

        TextView description = (TextView) v.findViewById(R.id.cocktail_detail);
        TextView ingredients = (TextView) v.findViewById(R.id.cocktail_ingredients);

        ingredients.setText(cocktailIngredients);
        description.setText(cocktailDescription);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if(args != null){
            //set the article based on the argument passed in
            updatedArticleView(args.getString(ARG_POSITION));
        }
        //else set the article based on the saved instance state defined during onCreateView
        else if(currentPosition.length() > 0){
            updatedArticleView(currentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_POSITION, currentPosition);
    }
}
