package mj.android.emmenia;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	
	private SQLiteDatabase mDataBase;

	public DBConnector(Context context) {
		OpenHelper mOpenHelper = new OpenHelper(context);
		mDataBase = mOpenHelper.getWritableDatabase();
	}
	
	public long insert(OneEntry md) {
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE, md.getDate());
		cv.put(COLUMN_TITLE, md.getTitle());
		cv.put(COLUMN_ICON, md.getIcon());
		
		return mDataBase.insert(TABLE_NAME, null, cv);
	}
	
	public int update(OneEntry md) {
		ContentValues cv=new ContentValues();
		cv.put(COLUMN_DATE, md.getDate());
		cv.put(COLUMN_TITLE, md.getTitle());
		cv.put(COLUMN_ICON, md.getIcon());
		return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(md.getID()) });
	}
	
	public void deleteAll() {
		mDataBase.delete(TABLE_NAME, null, null);
	}
	
	public void delete(long id) {
		mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
	}
	
	public OneEntry select(long id) {
		Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, COLUMN_DATE);
		
		mCursor.moveToFirst();
		long date = mCursor.getLong(NUM_COLUMN_DATE);
		String title = mCursor.getString(NUM_COLUMN_TITLE);
		int icon = mCursor.getInt(NUM_COLUMN_ICON);
		return new OneEntry(id, date, title, icon);
	}
	
	public ArrayList<OneEntry> selectAll() {
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
		return arr;
	}
	
	public int selectAvg() {
		Cursor mCursor = mDataBase.query(TABLE_NAME, new String[]{COLUMN_DATE}, null, null, null, null, COLUMN_DATE);
		
		mCursor.moveToFirst();
		long prevDate = -1;
		int sum = 0;
		if (!mCursor.isAfterLast()) {
			do {
				long date = mCursor.getLong(NUM_COLUMN_DATE);
				int days = -1;
				if (prevDate > 0)
					days = (int)((date - prevDate) / 1000 / 60 / 60 / 24);
				sum += days;
			} while (mCursor.moveToNext());
		}
		int avg = sum / mCursor.getCount();
		return avg;
	}
	
	public long selectMaxDate() {
		
		String query = "SELECT MAX(" + COLUMN_DATE  + ") FROM " + TABLE_NAME;
	    Cursor mCursor = mDataBase.rawQuery(query, null);
	    
		mCursor.moveToFirst();
		return mCursor.getLong(0);
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