package com.app.pindout.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BusinessDealsDBAdapter {
	static String KEY_ROWID = "_id";
	public static final String KEY_DEAL_ID = "row_id";
	public static final String KEY_DEALNAME = "dealname";
	public static final String KEYCATNAME = "catid";
	public static final String KEY_MAINPRICE = "main_price";
	public static final String KEY_DEALPRICE = "deal_price";
	public static final String KEY_DEALURL = "deal_url";
	public static final String KEY_DEALSTATUS = "deal_status";
	public static final String KEY_DEALADMINAPPROVAL = "deal_adminapproval";
	public static final String KEY_DEALDESC = "deal_desc";
	public static final String KEY_IMAGEURL = "img_url";
	static final String TAG = "DBAdapter";

	public static final String DATABASE_NAME = "BusinessDeals.db";
	static final String DATABASE_TABLE = "businessdeals";
	static final int DATABASE_VERSION = 1;

	static final String DATABASE_CREATE = "create table businessdeals (_id integer primary key autoincrement, "
			+ "row_id text not null, dealname text not null, catid text not null,main_price text not null,deal_price text not null,deal_url text not null,deal_status text not null,deal_adminapproval text not null,deal_desc text not null,img_url text not null);";

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public BusinessDealsDBAdapter(Context ctx) {
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

	public BusinessDealsDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert(String deal_id, String dealname, String catname,
			String main_price, String deal_price, String deal_url,
			String deal_status, String deal_adminapproval, String deal_desc,
			String img_url) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DEAL_ID, deal_id);
		initialValues.put(KEY_DEALNAME, dealname);
		initialValues.put(KEYCATNAME, catname);
		initialValues.put(KEY_MAINPRICE, main_price);
		initialValues.put(KEY_DEALPRICE, deal_price);
		initialValues.put(KEY_DEALURL, deal_url);
		initialValues.put(KEY_DEALSTATUS, deal_status);
		initialValues.put(KEY_DEALADMINAPPROVAL, deal_adminapproval);
		initialValues.put(KEY_DEALDESC, deal_desc);
		initialValues.put(KEY_IMAGEURL, img_url);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean delete(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor getAll() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_DEAL_ID,
				KEY_DEALNAME, KEYCATNAME, KEY_MAINPRICE, KEY_DEALPRICE,
				KEY_DEALURL, KEY_DEALSTATUS, KEY_DEALADMINAPPROVAL,
				KEY_DEALDESC, KEY_IMAGEURL }, null, null, null, null, null);
	}

	public boolean deleteAll() {

		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public Cursor get(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_DEAL_ID, KEY_DEALNAME, KEYCATNAME,
				KEY_MAINPRICE, KEY_DEALPRICE, KEY_DEALURL, KEY_DEALSTATUS,
				KEY_DEALADMINAPPROVAL, KEY_DEALDESC, KEY_IMAGEURL }, KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean update(long rowId, String deal_id, String dealname,
			String catname, String main_price, String deal_price,
			String deal_url, String deal_status, String deal_adminapproval,
			String deal_desc, String img_url) {
		ContentValues args = new ContentValues();
		args.put(KEY_DEAL_ID, deal_id);
		args.put(KEY_DEALNAME, dealname);
		args.put(KEYCATNAME, catname);
		args.put(KEY_MAINPRICE, main_price);
		args.put(KEY_DEALPRICE, deal_price);
		args.put(KEY_DEALURL, deal_url);
		args.put(KEY_DEALSTATUS, deal_status);
		args.put(KEY_DEALADMINAPPROVAL, deal_adminapproval);
		args.put(KEY_DEALDESC, deal_desc);
		args.put(KEY_IMAGEURL, img_url);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetch(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_DEALNAME, KEY_DEAL_ID }, null, null, null, null, null);

		} else {
			mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_DEAL_ID, KEY_DEALNAME }, KEYCATNAME + " like '%"
					+ inputText + "%'", null, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
