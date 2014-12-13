package com.com.android.eboerse.database;

/**
 * Databasehandler verbindet anwendung mit SQLite
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "Kurs";

	// Kurs table name
	private static final String TABLE_Kurs = "Kurs";

	//Kurs Table Columns names
	private static final String KEY_Kurs_Name = "Name";
	private static final String KEY_Kurs_Symbol = "Symbol";
	private static final String KEY_Flag_Favorit = "Flag";
	private static final String KEY_OBERE_GRENZE = "ObereGrenze";
	private static final String KEY_UNTERE_GRENZE = "UntereGrenze";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_Kurs_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_Kurs + "("
				+ KEY_Kurs_Name + " TEXT," + KEY_Kurs_Symbol + " TEXT PRIMARY KEY,"
				+ KEY_Flag_Favorit + " TEXT," + KEY_OBERE_GRENZE + " TEXT," + KEY_UNTERE_GRENZE + " TEXT"+ ")";
		db.execSQL(CREATE_Kurs_TABLE);

	}

	public boolean isTableExists()	{
		SQLiteDatabase db = this.getWritableDatabase();
		if (TABLE_Kurs == null || db == null || !db.isOpen())
		{
			return false;
		}
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", TABLE_Kurs});
		if (!cursor.moveToFirst())
		{
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();

		return count > 0;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Kurs);
		onCreate(db);
	}

	// Kurs hinzufuegen
	public void addKurs(Favorit favorite) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_Kurs_Name, favorite.getKurs_Name());
		values.put(KEY_Kurs_Symbol, favorite.getKurs_Symbol());
		values.put(KEY_Flag_Favorit, favorite.getFlag_Favorit());
		values.put(KEY_OBERE_GRENZE, favorite.getObereGrenze());
		values.put(KEY_UNTERE_GRENZE, favorite.getUntereGrenze());


		db.insert(TABLE_Kurs, null, values);
		db.close(); 

	}

	// Get Single Kurs
	public Favorit getFavorit(String Name) {
		SQLiteDatabase db = this.getReadableDatabase();

		Favorit fav = null;

		Cursor cursor = db.query(TABLE_Kurs, new String[] { KEY_Kurs_Name,
				KEY_Kurs_Symbol, KEY_Flag_Favorit, KEY_OBERE_GRENZE, KEY_UNTERE_GRENZE }, KEY_Kurs_Symbol + "=?",
				new String[] { String.valueOf(Name) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		if(cursor.getCount() > 0 && cursor != null){
			fav = new Favorit(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor.getString(4));
		}

		return fav;
	}

	public Favorit getFavoritByName(String name){
		SQLiteDatabase db = this.getReadableDatabase();

		Favorit fav = null;

		Cursor cursor = db.query(TABLE_Kurs, new String[] { KEY_Kurs_Name,
				KEY_Kurs_Symbol, KEY_Flag_Favorit, KEY_OBERE_GRENZE, KEY_UNTERE_GRENZE }, KEY_Kurs_Name + "=?",
				new String[] { String.valueOf(name) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		if(cursor.getCount() > 0 && cursor != null){
			fav = new Favorit(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor.getString(4));
		}

		return fav;
	}

	// Getting All Kurs
	public List<Favorit> getAllKurs() {
		List<Favorit> kursList = new ArrayList<Favorit>();

		String selectQuery = "SELECT  * FROM " + TABLE_Kurs;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Favorit fav = new Favorit();
				fav.setKurs_Name(cursor.getString(0));
				fav.setKurs_Symbol(cursor.getString(1));
				fav.setFlag_Favorit(cursor.getString(2));
				fav.setObereGrenze(cursor.getString(3));
				fav.setUntereGrenze(cursor.getString(4));
				kursList.add(fav);
			} while (cursor.moveToNext());
		}

		return kursList;
	}

	// Deleting single Kurs
	public void deleteKurs(Favorit favorite) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Kurs, KEY_Kurs_Symbol + " = ?",
				new String[] { String.valueOf(favorite.getKurs_Symbol())});
		db.close();
	}

	public int updateFavorit(Favorit favorit) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_OBERE_GRENZE, favorit.getObereGrenze());
		values.put(KEY_UNTERE_GRENZE, favorit.getUntereGrenze());

		// updating row
		return db.update(TABLE_Kurs, values, KEY_Kurs_Symbol + " = ?",
				new String[] { String.valueOf(favorit.getKurs_Symbol()) });
	}

	//	public void drop(){
	//		SQLiteDatabase db = this.getWritableDatabase();
	//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Kurs);
	//	}
}
