package com.app.pindout.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FacilityDBAdapter {
	static String KEY_ROWID = "_id";
	public static final String KEY_FACILITYNAME = "facname";
	public static final String KEY_FACILITYIDS = "facid";
	static final String TAG = "DBAdapter";

	public static final String DATABASE_NAME = "Facility.db";
	static final String DATABASE_TABLE = "facility";
	static final int DATABASE_VERSION = 1;

	static final String DATABASE_CREATE = "create table facility (_id integer primary key autoincrement, "
			+ "facname text not null, facid text not null);";

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public FacilityDBAdapter(Context ctx) {
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

	public FacilityDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert(String facilityname, String facilityid) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FACILITYNAME, facilityname);
		initialValues.put(KEY_FACILITYIDS, facilityid);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean delete(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor getAll() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_FACILITYNAME, KEY_FACILITYIDS }, null, null, null, null,
				null);
	}

	public boolean deleteAll() {

		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public Cursor get(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_FACILITYNAME, KEY_FACILITYIDS }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean update(long rowId, String facname, String facid) {
		ContentValues args = new ContentValues();
		args.put(KEY_FACILITYNAME, facname);
		args.put(KEY_FACILITYIDS, facid);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetch(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_FACILITYNAME }, null, null, null, null, null);

		} else {
			mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_FACILITYNAME }, KEY_FACILITYIDS + " like '%"
					+ inputText + "%'", null, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
}