package com.hoan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {
	public static final String KEY_ROWID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_DIEM = "diem";

	private static final String DATABASE_NAME = "Database";
	private static final String DATABASE_TABLE = "TableDiem";
	private static final int DATABASE_VERSION = 1;

	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
			+ " TEXT NOT NULL, " + KEY_DIEM + " INTEGER);";

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	//------------------------------------------------------------------------------
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}
	//------------------------------------------------------------------------------
	public Database(Context c) {
		ourContext = c;
		ourHelper = new DbHelper(ourContext);
	}
	//------------------------------------------------------------------------------
	public Database open() {
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	//------------------------------------------------------------------------------
	public void close() {
		ourHelper.close();
	}
	//------------------------------------------------------------------------------
	public long isertRow(String name, int diem) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_DIEM, diem);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	//------------------------------------------------------------------------------
	public Cursor getAllRows() {
		return ourDatabase.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_NAME, KEY_DIEM }, null, null, null, null, KEY_DIEM
				+ " DESC");
	}
	//------------------------------------------------------------------------------
	public long deleteAllRows() {
		return ourDatabase.delete(DATABASE_TABLE, null, null);
	}
	//------------------------------------------------------------------------------
	//Update Ä‘iá»ƒm ngÆ°á»�i chÆ¡i
	public long updatePlayer(String id, String name, int diem) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_DIEM, diem);
		return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=" + id,
				null);
	}
	
	//-------------------------------------------------------------------------------
	//Kiá»ƒm tra xem vá»›i sá»‘ Ä‘iá»ƒm Ä‘Æ°á»£c truyá»�n vÃ o cÃ³ Ä‘Æ°á»£c phÃ©p lÆ°u vÃ o csdl hay khÃ´ng.
	public boolean kt_luu(int diem_player){
		try {			
			this.open();
			Cursor c = this.getAllRows();
			if(c.moveToPosition(9)) {
				int diem = c.getInt(c.getColumnIndex(Database.KEY_DIEM));
				if(diem < diem_player){ 
					c.close();
					this.close();	
					return true;//Ä�Æ°á»£c phÃ©p lÆ°u
					}
			}
			c.close();
			this.close();			
		} catch(SQLiteException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
