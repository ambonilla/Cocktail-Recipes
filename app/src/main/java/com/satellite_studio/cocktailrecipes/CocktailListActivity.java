package com.satellite_studio.cocktailrecipes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * An activity representing a list of Cocktails. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CocktailDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link CocktailListFragment} and the item details
 * (if present) is a {@link CocktailDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link CocktailListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class CocktailListActivity extends FragmentActivity
        implements CocktailListFragment.Callbacks {

    DBAdapter db;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public void GetCocktails(){
        db.openDataBase();
        Cursor c = db.getAllCocktails();

        if(c.moveToFirst()){
            do{
                Log.e("COCKTAILS!", c.getString(0));
            }
            while(c.moveToNext());
        }

        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_list);

        db = new DBAdapter(this);
        try {
            db.createDatabase();
            db.openDataBase();
        }
        catch(IOException e){
            Log.e("MAIN Create DB","Can't create DB");
            e.printStackTrace();
        }
        catch (SQLiteException e){
            Log.e("MAIN Open DB","Can't open DB");
            e.printStackTrace();
        }

        GetCocktails();

        if (findViewById(R.id.cocktail_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((CocktailListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.cocktail_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }



    /**
     * Callback method from {@link CocktailListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(CocktailDetailFragment.ARG_ITEM_ID, id);
            CocktailDetailFragment fragment = new CocktailDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cocktail_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, CocktailDetailActivity.class);
            detailIntent.putExtra(CocktailDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
