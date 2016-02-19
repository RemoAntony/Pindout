package com.app.pindout.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoryDBAdapter {
	static String KEY_ROWID = "_id";
	public static final String KEY_CATEGORYID = "catid";
	public static final String KEY_CATEGORYNAME = "catname";
	public static final String KEY_CATFACIDS = "catfacid";
	static final String TAG = "DBAdapter";

	public static final String DATABASE_NAME = "Category.db";
	static final String DATABASE_TABLE = "category";
	static final int DATABASE_VERSION = 1;

	static final String DATABASE_CREATE = "create table category (_id integer primary key autoincrement, "
			+ "catid text not null, catname text not null, catfacid text not null);";

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public CategoryDBAdapter(Context ctx) {
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

	public CategoryDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert(String catid, String catname, String catfacids) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CATEGORYID, catid);
		initialValues.put(KEY_CATEGORYNAME, catname);
		initialValues.put(KEY_CATFACIDS, catfacids);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean delete(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor getAll() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_CATEGORYID, KEY_CATEGORYNAME, KEY_CATFACIDS }, null, null,
				null, null, null);
	}

	public boolean deleteAll() {

		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public Cursor get(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_CATEGORYID, KEY_CATEGORYNAME, KEY_CATFACIDS },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean update(long rowId, String catid, String catname,
			String catfacids) {
		ContentValues args = new ContentValues();
		args.put(KEY_CATEGORYID, catid);
		args.put(KEY_CATEGORYNAME, catname);
		args.put(KEY_CATFACIDS, catfacids);

		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetch(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_CATEGORYNAME, KEY_CATEGORYID }, null, null, null, null,
					null);

		} else {
			mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_CATEGORYID, KEY_CATEGORYNAME }, KEY_CATFACIDS
					+ " like '%" + inputText + "%'", null, null, null, null,
					null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
}
