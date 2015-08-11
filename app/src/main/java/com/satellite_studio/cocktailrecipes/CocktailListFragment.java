package com.satellite_studio.cocktailrecipes;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.satellite_studio.cocktailrecipes.cocktails.CocktailItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Cocktails. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link CocktailDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class CocktailListFragment extends ListFragment {

    private List<CocktailItem> mItems;
    DBAdapter db;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String name);
    }



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CocktailListFragment() {
    }

    public void GetCocktails(){
        db.openDataBase();
        Cursor c = db.getAllCocktails();

        if(c.moveToFirst()){
            do{
                mItems.add(new CocktailItem(c.getString(0)));
            }
            while(c.moveToNext());
        }

        db.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems = new ArrayList<CocktailItem>();
        Context currCtx = getActivity();
        db = new DBAdapter(currCtx);
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

        CocktailAdapter cocktailAdapter = new CocktailAdapter(currCtx, mItems);

        setListAdapter(cocktailAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        //mCallbacks = cocktailCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        //super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        //mCallbacks.onItemSelected(mItems.get(position).id);
        mCallbacks.onItemSelected(mItems.get(position).name);
        //PresidentViewItem pres = mItems.get(position);
        view.setSelected(true);
        listView.setItemChecked(position,true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
