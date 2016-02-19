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
public class ReviewDb {

    public static final String KEY_ROWID = "rid";
    public static final String KEYBUS = "business_name";
    public static final String KEYText="review_text";
    public static final String KEYDate="review_date";
    public static final String KEYCDate="date";
    public static final String KEYSlug="slug";
    public static final String KEYcomment="comment_text";
    public static final String KEYuserid="user_id";
    public static final String KEYimage="business_image";


        static final String TAG = "DBAdapter";
    /*
    String dealname, String deal_desc, String deal_url,
                           String main_price, String deal_price, String end_date,
                           String deal_status, String deal_adminapproval, String bus_name,
                           String img_url,String tot,String cur_tot
     */
        public static final String DATABASE_NAME = "Review.db";
        static final String DATABASE_TABLE = "Reviewdet";
        static final int DATABASE_VERSION = 1;

        static final String DATABASE_CREATE = "create table Reviewdet(_id integer primary key autoincrement, "
                + "rid text not null,business_name text not null,slug text not null," + "review_text text not null,"  +
                "review_date text not null,comment_text text not null," +
                "user_id text not null,business_image text not null," +
                "date text not null);";

        final Context context;


        DatabaseHelper DBHelper;
        SQLiteDatabase db;

        public ReviewDb(Context ctx) {
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

    public ReviewDb open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insert(String rid,String busname,String slug, String review_text,String review_date,String comment_text,
                       String user_id,String business_image,String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEYBUS,busname);
        initialValues.put(KEY_ROWID,rid);
        initialValues.put(KEYCDate,date);
        initialValues.put(KEYDate,review_date);
        initialValues.put(KEYText,review_text);
        initialValues.put(KEYcomment,comment_text);
        initialValues.put(KEYSlug,slug);
        initialValues.put(KEYuserid,user_id);
        initialValues.put(KEYimage,business_image);


        return db.insert(DATABASE_TABLE, null, initialValues);
        /*
              + "rid text not null,business_name text not null," + "review_text text not null,"  +
                "review_date text not null,comment_text text not null," +
                "user_id text not null,business_image text not null," +
                "date text not null);";
         */
    }

    public boolean delete(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAll() {
        return db.query(DATABASE_TABLE, new String[] {KEYBUS,KEY_ROWID,KEYCDate,KEYcomment,KEYimage,KEYSlug,KEYText
                ,KEYDate,KEYuserid}, null, null, null, null, null);

    }

    public boolean deleteAll() {

        return db.delete(DATABASE_TABLE, null, null) > 0;
    }


}
