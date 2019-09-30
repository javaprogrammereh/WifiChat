package com.example.wifichat;

/*support telgram id =@javaprogrammer_eh
 * 05/07/1398
 * creted by elmira hossein zadeh*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;


public class Database {

    private static DatabaseHelper databaseHelper;
    private static final String chat_table = "chat";

    public Database() {
    }

    public static SQLiteDatabase getInstance(Context context) {
        if (databaseHelper == null) {
            try {
                databaseHelper = new DatabaseHelper(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return databaseHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getInstance2(Context context) {
        if (databaseHelper == null) {
            try {
                databaseHelper = new DatabaseHelper(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return databaseHelper.getReadableDatabase();
    }
    public static void addLoginData(String text, Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("text", text);
        // Inserting Row
        getInstance(context).insert(chat_table, null, contentValues);
        getInstance(context).close(); // Closing database connection
    }
    public static String[] getDatachat(Context context) {
        String selectQuery = "SELECT  * FROM " + chat_table ;
        Cursor cursor = getInstance2(context).rawQuery(selectQuery, null, null);
        ArrayList<String> spinnerContent = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String word = cursor.getString(cursor.getColumnIndexOrThrow("text"));
                Log.i("getData chat", cursor.getString(cursor.getColumnIndex("text")));
                spinnerContent.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        String[] allSpinner = new String[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);
        return allSpinner;
    }
}