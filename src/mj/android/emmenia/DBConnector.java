package mj.android.emmenia;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBConnector {
	
	// Данные базы данных и таблиц
	private static final String DATABASE_NAME = "emmenia.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "Emmenia";
	
	// Название столбцов
	private static final String COLUMN_ID = "_id";
	private static final String COLUMN_DATE = "Date";
	private static final String COLUMN_TITLE = "Title";
	private static final String COLUMN_ICON = "Icon";
	
	// Номера столбцов
	private static final int NUM_COLUMN_ID = 0;
	private static final int NUM_COLUMN_DATE = 1;
	private static final int NUM_COLUMN_TITLE = 2;
	private static final int NUM_COLUMN_ICON = 3;
	
	private OpenHelper mOpenHelper;

	public DBConnector(Context context) {
		mOpenHelper = new OpenHelper(context);
		Log.w("MY", "DBConnector");
	}
	
	public long insert(OneEntry md) throws Exception {
		SQLiteDatabase mDataBase = mOpenHelper.getWritableDatabase();
		Cursor mCursor = mDataBase.query(TABLE_NAME, new String[] {COLUMN_ID}, COLUMN_DATE + " = ? ", new String[] { String.valueOf(md.getDate()) }, null, null, null);
		if (mCursor.getCount() > 0)
			throw new Exception ("Dublicate entry");
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE, md.getDate());
		cv.put(COLUMN_TITLE, md.getTitle());
		cv.put(COLUMN_ICON, md.getIcon());
		long result = mDataBase.insert(TABLE_NAME, null, cv);
		mDataBase.close();
		
		return result;
	}
	
	public int update(OneEntry md) throws Exception {
		
		SQLiteDatabase mDataBase = mOpenHelper.getWritableDatabase();
		
		Cursor mCursor = mDataBase.query(TABLE_NAME, new String[] {COLUMN_ID}, COLUMN_ID + " <> ? AND " + COLUMN_DATE + " = ? ", new String[] { String.valueOf(md.getID()), String.valueOf(md.getDate()) }, null, null, null);
		if (mCursor.getCount() > 0)
			throw new Exception ("Dublicate entry");
		
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE, md.getDate());
		cv.put(COLUMN_TITLE, md.getTitle());
		cv.put(COLUMN_ICON, md.getIcon());
		int result = mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(md.getID()) });
		mDataBase.close();
		return result;
	}
	
	public void deleteAll() {
		SQLiteDatabase mDataBase = mOpenHelper.getWritableDatabase();
		mDataBase.delete(TABLE_NAME, null, null);
		mDataBase.close();
	}
	
	public void delete(long id) {
		SQLiteDatabase mDataBase = mOpenHelper.getWritableDatabase();
		mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
		mDataBase.close();
	}
	
	public OneEntry select(long id) {
		SQLiteDatabase mDataBase = mOpenHelper.getReadableDatabase();
		Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, COLUMN_DATE);
		
		mCursor.moveToFirst();
		long date = mCursor.getLong(NUM_COLUMN_DATE);
		String title = mCursor.getString(NUM_COLUMN_TITLE);
		int icon = mCursor.getInt(NUM_COLUMN_ICON);
		mDataBase.close();
		return new OneEntry(id, date, title, icon);
	}
	
	public ArrayList<OneEntry> selectAll() {
		SQLiteDatabase mDataBase = mOpenHelper.getReadableDatabase();
		Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, COLUMN_DATE);
		ArrayList<OneEntry> arr = new ArrayList<OneEntry>();
		mCursor.moveToFirst();
		long prevDate = -1;
		if (!mCursor.isAfterLast()) {
			do {
				long id = mCursor.getLong(NUM_COLUMN_ID);
				long date = mCursor.getLong(NUM_COLUMN_DATE);
				String title = mCursor.getString(NUM_COLUMN_TITLE);
				int icon = mCursor.getInt(NUM_COLUMN_ICON);
				OneEntry md = new OneEntry(id, date, title, icon);
				int days = -1;
				if (prevDate > 0)
					days = (int)((date - prevDate) / 1000 / 60 / 60 / 24);
				md.setDays(days);
				prevDate = date;				
				arr.add(md);
			} while (mCursor.moveToNext());
		}
		Collections.reverse(arr);
		mDataBase.close();
		return arr;
	}
	
	public int selectAvg() {
		SQLiteDatabase mDataBase = mOpenHelper.getReadableDatabase();
		Cursor mCursor = mDataBase.query(TABLE_NAME, new String[]{COLUMN_DATE}, null, null, null, null, COLUMN_DATE);
		// если дат еще нет, или только одна, то период высчитать не можем
		if (mCursor.getCount() < 2)
			return -1;
		mCursor.moveToFirst();
		long prevDate = -1;
		int sum = 0;
		if (!mCursor.isAfterLast()) {
			do {
				long date = mCursor.getLong(0);
				int days = 0;
				if (prevDate > 0)
					days = (int)((date - prevDate) / 1000 / 60 / 60 / 24);
				sum += days;
				prevDate = date;
			} while (mCursor.moveToNext());
		}
		int avg = sum / (mCursor.getCount() - 1);
		mDataBase.close();
		return avg;
	}
	
	public long selectMaxDate() {
		SQLiteDatabase mDataBase = mOpenHelper.getReadableDatabase();
		String query = "SELECT MAX(" + COLUMN_DATE  + ") FROM " + TABLE_NAME;
	    Cursor mCursor = mDataBase.rawQuery(query, null);
	    mCursor.moveToFirst();
	    long res = mCursor.getLong(0);
		mDataBase.close();
		return res;
	}
	
	private class OpenHelper extends SQLiteOpenHelper {
				
		OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			String query = "CREATE TABLE " + TABLE_NAME + " (" + 
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
					COLUMN_DATE + " LONG, " + 
					COLUMN_TITLE + " TEXT, " + 
					COLUMN_ICON + " INTEGER); ";
			db.execSQL(query);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}