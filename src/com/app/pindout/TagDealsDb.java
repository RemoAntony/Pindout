package com.app.pindout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vms20591 on 6/1/16.
 */
public class TagDealsDb {

        static String KEY_ROWID = "_id";
        public static final String KEY_DEAL_ID = "row_id";
        public static final String KEY_DEALNAME = "dealname";
        public static final String KEYEND = "end_date";
        public static final String KEY_MAINPRICE = "main_price";
        public static final String KEY_DEALPRICE = "deal_price";
        public static final String KEY_DEALURL = "deal_url";
        public static final String KEY_DEALSTATUS = "deal_status";
        public static final String KEY_DEALADMINAPPROVAL = "deal_adminapproval";
        public static final String KEY_DEALDESC = "deal_desc";
        public static final String KEY_IMAGEURL = "img_url";
        public static final String KEYTOT = "total";
        public static final String KEYCURTOT = "cur_total";
        public static final String KEYBUS = "business_name";
        public static final String KEYCATNAME = "catid";
        static final String TAG = "DBAdapter";
/*
String dealname, String deal_desc, String deal_url,
                       String main_price, String deal_price, String end_date,
                       String deal_status, String deal_adminapproval, String bus_name,
                       String img_url,String tot,String cur_tot
 */
        public static final String DATABASE_NAME = "Tagdeals.db";
        static final String DATABASE_TABLE = "TaggedDeals";
        static final int DATABASE_VERSION = 1;

        static final String DATABASE_CREATE = "create table TaggedDeals (_id integer primary key autoincrement, "
                + "dealname text not null," + "deal_desc text not null," + "main_price text not null," + "deal_price text not null," +
                "end_date text not null," + "deal_url text not null," + "deal_status text not null," + "deal_adminapproval text not null,business_name text not null," +
                "total text not null," + "cur_total text not null,img_url text not null,dealid String,dealbuttonstatus String);";

        final Context context;


        DatabaseHelper DBHelper;
        SQLiteDatabase db;

        public TagDealsDb(Context ctx) {
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

    public TagDealsDb open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insert(String dealname, String deal_desc, String deal_url,
                       String main_price, String deal_price, String end_date,
                       String deal_status, String deal_adminapproval, String bus_name,
                       String img_url,String tot,String cur_tot,String dealid,String buttonstatus) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DEALNAME, dealname);
        initialValues.put(KEYEND,end_date);
        initialValues.put(KEY_MAINPRICE, main_price);
        initialValues.put(KEY_DEALPRICE, deal_price);
        initialValues.put(KEY_DEALURL, deal_url);
        initialValues.put(KEY_DEALSTATUS, deal_status);
        initialValues.put(KEY_DEALADMINAPPROVAL, deal_adminapproval);
        initialValues.put(KEY_DEALDESC, deal_desc);
        initialValues.put(KEY_IMAGEURL, img_url);
        initialValues.put(KEYTOT, tot);
        initialValues.put(KEYCURTOT,cur_tot);
        initialValues.put(KEYBUS,bus_name);
        initialValues.put("dealid",dealid);
        initialValues.put("dealbuttonstatus",buttonstatus);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean delete(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAll() {
        return db.query(DATABASE_TABLE, new String[] {KEY_DEALNAME,KEY_DEALDESC, KEY_MAINPRICE, KEY_DEALPRICE,KEYEND,
                KEY_DEALURL, KEY_DEALSTATUS, KEY_DEALADMINAPPROVAL,KEYBUS,KEYCURTOT,KEYTOT,
                KEY_IMAGEURL,"dealid","dealbuttonstatus" }, null, null, null, null, null);

    }

    public boolean deleteAll() {

        return db.delete(DATABASE_TABLE, null, null) > 0;
    }


}
