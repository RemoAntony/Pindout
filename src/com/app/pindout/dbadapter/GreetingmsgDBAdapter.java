package com.app.pindout.dbadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GreetingmsgDBAdapter {
	static String KEY_ROWID = "_id";
	public static final String KEY_ROW_ID = "row_id";
	public static final String KEY_MESSAGE = "msg";
	public static final String KEY_MSGDATE = "msg_date";
	static final String TAG = "DBAdapter";

	public static final String DATABASE_NAME = "Greetingmsg.db";
	static final String DATABASE_TABLE = "greetingmsg";
	static final int DATABASE_VERSION = 1;

	static final String DATABASE_CREATE = "create table greetingmsg (_id integer primary key autoincrement, "
			+ "row_id text not null, msg text not null, msg_date text not null);";

	final Context context;

	DatabaseHelper DBHelper;
	SQLiteDatabase db;

	public GreetingmsgDBAdapter(Context ctx) {
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

	public GreetingmsgDBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert(String rowid, String msg, String msgdate) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROW_ID, rowid);
		initialValues.put(KEY_MESSAGE, msg);
		initialValues.put(KEY_MSGDATE, msgdate);

		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean delete(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor getAll() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ROW_ID,
				KEY_MESSAGE, KEY_MSGDATE }, null, null, null, null, null);
	}

	public boolean deleteAll() {

		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public Cursor get(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_ROW_ID, KEY_MESSAGE, KEY_MSGDATE }, KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean update(long rowId, String row_id, String msg, String msgdate) {
		ContentValues args = new ContentValues();
		args.put(KEY_ROW_ID, row_id);
		args.put(KEY_MESSAGE, msg);
		args.put(KEY_MSGDATE, msgdate);

		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetch(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null || inputText.length() == 0) {
			mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_MESSAGE, KEY_ROW_ID }, null, null, null, null, null);

		} else {
			mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
					KEY_ROW_ID, KEY_MESSAGE }, KEY_MSGDATE + " like '%"
					+ inputText + "%'", null, null, null, null, null);
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
}
