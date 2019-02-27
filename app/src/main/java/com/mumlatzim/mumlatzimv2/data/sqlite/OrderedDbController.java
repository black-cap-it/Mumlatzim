package com.mumlatzim.mumlatzimv2.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.mumlatzim.mumlatzimv2.models.bookmark.OrderModel;

import java.util.ArrayList;

public class OrderedDbController {

    private SQLiteDatabase db;

    public OrderedDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public int insertData(int postId, String postImage, String postTitle, String postUrl, String postCategory, String postDate,String postTrack,String  postScreenshot) throws SQLiteException
    {

        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_ORDERED_POST_ID, postId);
        values.put(DbConstants.COLUMN_ORDERED_POST_IMAGE, postImage);
        values.put(DbConstants.COLUMN_ORDERED_POST_TITLE, postTitle);
        values.put(DbConstants.COLUMN_ORDERED_POST_URL, postUrl);
        values.put(DbConstants.COLUMN_ORDERED_POST_CATEGORY, postCategory);
        values.put(DbConstants.COLUMN_ORDERED_POST_DATE, postDate);
        values.put(DbConstants.COLUMN_ORDERED_POST_TRACK_NUMBER,postTrack);
        values.put(DbConstants.COLUMN_ORDERED_POST_SCREENSHOT,postScreenshot);

        // Insert the new row, returning the primary key value of the new row
        return (int) db.insert(
                DbConstants.ORDERED_TABLE_NAME,
                DbConstants.COLUMN_NAME_NULLABLE,
                values);
    }

    public void updateTrack(int postId,String postTrack) throws SQLiteException
    {
        ContentValues values=new ContentValues();
        //values.put(DbConstants.COLUMN_ORDERED_POST_ID,postId);
        values.put(DbConstants.COLUMN_ORDERED_POST_TRACK_NUMBER,postTrack);

         db.update(DbConstants.ORDERED_TABLE_NAME,
                values,DbConstants.COLUMN_POST_ID + "=" +postId ,null);
    }

    public void updateScreenshot(int postId,String postScreenshot) throws SQLiteException
    {
        ContentValues values=new ContentValues();
        values.put(DbConstants.COLUMN_ORDERED_POST_SCREENSHOT,postScreenshot);

         db.update(DbConstants.ORDERED_TABLE_NAME,
                values, DbConstants.COLUMN_POST_ID + "=" +postId ,null);
    }

    public ArrayList<OrderModel> getAllData() throws SQLiteException {
        String[] projection = {
                DbConstants._ID,
                DbConstants.COLUMN_ORDERED_POST_IMAGE,
                DbConstants.COLUMN_ORDERED_POST_ID,
                DbConstants.COLUMN_ORDERED_POST_TITLE,
                DbConstants.COLUMN_ORDERED_POST_URL,
                DbConstants.COLUMN_ORDERED_POST_CATEGORY,
                DbConstants.COLUMN_ORDERED_POST_DATE,
                DbConstants.COLUMN_ORDERED_POST_TRACK_NUMBER,
                DbConstants.COLUMN_ORDERED_POST_SCREENSHOT
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants._ID + " DESC";

        Cursor c = db.query(
                DbConstants.ORDERED_TABLE_NAME,  // The table name to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    private ArrayList<OrderModel> fetchData(Cursor c) {
        ArrayList<OrderModel> favDataArray = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                    int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants._ID));
                    int postId = c.getInt(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_ID));
                    String postImage = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_IMAGE));
                    String postTitle = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_TITLE));
                    String postUrl = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_URL));
                    String postCategory = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_CATEGORY));
                    String postDate = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_DATE));
                    String postTrack = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_TRACK_NUMBER));
                    String postScreenshot = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_ORDERED_POST_SCREENSHOT));

                    // wrap up data list and return
                    favDataArray.add(new OrderModel(itemId, postId, postImage, postTitle, postUrl, postCategory, postDate,postTrack,postScreenshot));
                }
                while (c.moveToNext());
            }
            c.close();
        }
        return favDataArray;
    }

    public void deleteEachFav(int postId) throws SQLiteException
    {
        // Which row to update, based on the ID
        String selection = DbConstants.COLUMN_ORDERED_POST_ID + "=?";
        String[] selectionArgs = {String.valueOf(postId)};

        db.delete(
                DbConstants.ORDERED_TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void deleteAllFav() throws SQLiteException {
        db.delete(
                DbConstants.ORDERED_TABLE_NAME,
                null,
                null);
    }
}
