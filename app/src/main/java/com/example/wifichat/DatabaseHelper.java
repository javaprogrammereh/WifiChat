package com.example.wifichat;

/*support telgram id =@javaprogrammer_eh
 * 05/07/1398
 * creted by elmira hossein zadeh*/

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    final String TAG = "Mos";

    private Context context;
    private String DB_PATH;
    private static String DB_NAME = "wifichat.db";
    private static int DB_VER = 1;

    public DatabaseHelper(Context context) throws IOException {
        super(context, DB_NAME, null, DB_VER);
        this.context = context;
        DB_PATH = "/data/data/" + this.context.getPackageName() + "/databases/";
        if (isDatabaseExists()) {
            if (DB_VER > getOldVersion()) {

            }
        } else {
            createDatabase();
            saveNewVersion();
        }
    }

    private void createDatabase() throws IOException {
        this.getReadableDatabase();
        try {
            copyDatabase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
    }

    private boolean isDatabaseExists() {
        try {
            String path = DB_PATH + DB_NAME;
            File file = new File(path);
            return file.exists();
        } catch (SQLiteException ignored) {
            return false;
        }
    }

    private void copyDatabase() throws IOException {
        Log.e("coppy", "coppy");
        InputStream inputStream = context.getAssets().open(DB_NAME);
        String outputFileName = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outputFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private int getOldVersion() {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("DB_VER", 1);
    }

    private void saveNewVersion() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt("DB_VER", DB_VER);
        editor.apply();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
