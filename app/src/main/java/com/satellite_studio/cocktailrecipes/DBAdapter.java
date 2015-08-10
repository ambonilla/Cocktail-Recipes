package com.satellite_studio.cocktailrecipes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by antiadriano on 8/9/15.
 */
public class DBAdapter extends SQLiteOpenHelper{
    static final String KEY_NAME = "name";
    static final String KEY_INGREDIENTS = "ingredients";
    static final String KEY_DESCRIPTION = "description";
    static final String TAG = "DBAdapter";
    static final String destDir = "/data/data/com.satellite_studio.cocktailrecipes/databases/";
    static final String destPath = destDir + "cocktails";

    static final String DATABASE_NAME = "cocktails";
    static final String DATABASE_TABLE = "cocktails";
    static final int DATABASE_VERSION = 1;

    //static final String DATABASE_CREATE = "create table contacts ();

    final Context context;

    SQLiteDatabase db;

    public DBAdapter(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    public void createDatabase() throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){
        }

        else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            checkDB = SQLiteDatabase.openDatabase(destPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }

        if(checkDB != null){
            checkDB.close();
            return true;
        }
        return false;
    }

    private void copyDataBase() throws IOException{
        InputStream inputStr = context.getAssets().open(DATABASE_NAME);
        OutputStream outputStr = new FileOutputStream(destPath);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStr.read(buffer)) > 0){
            outputStr.write(buffer,0,length);
        }

        outputStr.flush();
        outputStr.close();
        inputStr.close();
    }

    public void openDataBase() throws SQLiteException {
        db = SQLiteDatabase.openDatabase(destPath,null, SQLiteDatabase.OPEN_READONLY);
    }

    public synchronized void close(){
        if (db != null) {
            db.close();
        }
        super.close();
    }


    public Cursor getAllCocktails(){
        return db.query(DATABASE_TABLE, new String[]{KEY_NAME, KEY_INGREDIENTS, KEY_DESCRIPTION},null,null,null,null,KEY_NAME,null);
    }

    public Cursor getCocktail(String cocktailName) throws SQLException{
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{KEY_NAME,
                KEY_INGREDIENTS, KEY_DESCRIPTION}, KEY_NAME + "=" + "'"
                        + cocktailName + "'", null,null,null,null,null);

        if (mCursor != null){
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
