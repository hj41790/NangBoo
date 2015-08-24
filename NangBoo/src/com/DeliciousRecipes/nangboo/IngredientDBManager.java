package com.DeliciousRecipes.nangboo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IngredientDBManager extends SQLiteOpenHelper{
	
	static final String DB_INGREDIENT 		= "ingredient.db";
	static final String TABLE_INGREDIENT 	= "ingredient";
	static final int 	DB_VERSION 			= 1;
	
	Context mContext = null;
	
	private static	IngredientDBManager mDBmanager 	= null;
	
	/* �̱��� ������ �̿��Ͽ� �ϳ��� ��ü���� ���� */
	public static IngredientDBManager getInstance(Context context){
		
		if(mDBmanager==null) mDBmanager = new IngredientDBManager(context, DB_INGREDIENT, null, DB_VERSION);
		
		
		return mDBmanager;
	}
	
	private IngredientDBManager(Context context, String dbName, CursorFactory factory, int version){
		
		super(context, dbName, factory, version);
		
		mContext = context;
		
	}
	
	public void onCreate(SQLiteDatabase db){
		
		db.execSQL(	"CREATE TABLE IF NOT EXISTS " + TABLE_INGREDIENT +
							"("+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
							"name				TEXT, 	" +
							"expirationDate		TEXT, 	" +
							"memo				TEXT, 	" +
							"isChoosed			INTEGER); " );
	}
	
	public void onOpen(SQLiteDatabase db){
		super.onOpen(db);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
		if(oldVersion<newVersion){

			String[] columns = new String[]{"_id", "name", "expirationDate", "memo", "isChoosed"};
			Cursor cursor = db.query(TABLE_INGREDIENT, columns, null, null, null, null, null);
			cursor.moveToFirst();
			
			int count = cursor.getCount();
			ContentValues[] values = new ContentValues[count];

			for(int i=0; i<count; i++){
				values[i] = new ContentValues();
				values[i].put("_id", cursor.getInt(0));
				values[i].put("name", cursor.getString(1));
				values[i].put("expirationDate", cursor.getString(2));
				values[i].put("memo", cursor.getString(3));
				values[i].put("isChoosed", cursor.getInt(4));
				cursor.moveToNext();
			}
			
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_INGREDIENT);
			onCreate(db);
			
			Log.d("DeliciousRecipes", "start insertAll...");
			
			db.beginTransaction();
			
			for(ContentValues contentValues : values){
				db.insert(TABLE_INGREDIENT, null, contentValues);
			}
			
			db.setTransactionSuccessful();
			db.endTransaction();
			
			Log.i("DeliciousRecipes", "end insertAll...");
			
		}
		
	}
	
	/* ���ڵ� �߰� ��� */
	public long insert(ContentValues addRowValue){
		return getWritableDatabase().insert(TABLE_INGREDIENT, null, addRowValue);
	}
	
	/* �߰��� ��ü ���ڵ� ��ȯ */
	public Cursor query(String[] string, 
						String 	selection, 
						String[] selectionArgs, 
						String groupBy, 
						String having, 
						String orderBy){
		
		return getReadableDatabase().query(	TABLE_INGREDIENT, string, selection, 
								selectionArgs, groupBy, having, orderBy);
	}
	
	public Cursor getAll(String sort){
		String[] columns = new String[]{"_id", "name", "expirationDate", "memo", "isChoosed"};
		return getReadableDatabase().query(TABLE_INGREDIENT, columns, null, null, null, null, sort);
	}

	public boolean getIsChoosed(int position){
		
		Cursor a = getReadableDatabase().query( TABLE_INGREDIENT, new String[]{"isChoosed"}, 
									null, null, null, null, null);
		
		a.moveToPosition(position);
		
		if(a.getInt(0)==1) return true;
		else return false;
	}
	
	/* Ư�����ڵ� ���� */
	public int update(ContentValues updateRowValue, String whereClause, String[] whereArgs){
		return getWritableDatabase().update(TABLE_INGREDIENT, updateRowValue, whereClause, whereArgs);
	}
	
	/* Ư�� ���ڵ� ���� */
	public int delete(String whereClause, String[] whereArgs){
		return getWritableDatabase().delete(TABLE_INGREDIENT, whereClause, whereArgs);
	}
	
	public int getCount(){
		
		return getReadableDatabase().query(TABLE_INGREDIENT, null, null, null, null, null, null).getCount();
	}
}
