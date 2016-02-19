package com.app.pindout;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vms20591 on 5/1/16.
 */
public class Businessdeatilsdb {
   public static final String KEY_ROWID = "rid";
    public static final String KEYBUS = "business_name";
    public static final String KEYgroup="group_id";
    public static final String KEYemail="email";


    static final String TAG = "DBAdapter";
    /*
    String dealname, String deal_desc, String deal_url,
                           String main_price, String deal_price, String end_date,
                           String deal_status, String deal_adminapproval, String bus_name,
                           String img_url,String tot,String cur_tot
     */
    public static final String DATABASE_NAME = "Bus.db";
    static final String DATABASE_TABLE = "Busdetails";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE = "create table Busdetails(_id integer primary key autoincrement, "
            + "rid text not null,business_name text not null," + "group_id text not null,"  + "email text not null);";

    final Context context;


    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public Businessdeatilsdb(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS signup");
            onCreate(db);
        }
    }

    public Businessdeatilsdb open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insert(String busname,String rid,String group_id, String email) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEYBUS,busname);
        initialValues.put(KEYgroup,group_id);
        initialValues.put(KEYemail,email);
        initialValues.put(KEY_ROWID,rid);



        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean delete(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAll() {
        return db.query(DATABASE_TABLE, new String[] {KEYBUS,KEY_ROWID,KEYgroup,KEYemail}, null, null, null, null, null);

    }

    public boolean deleteAll() {

        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

}