package com.DeliciousRecipes.nangboo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IngredientDBManager extends SQLiteOpenHelper{

	String[] ORDER_BY = new String[]{"name asc", "expirationDate asc", "_id asc"};
	
	static final String DB_INGREDIENT 		= "ingredient.db";
	static final String TABLE_INGREDIENT 	= "ingredient";
	static final int 	DB_VERSION 			= 1;
	
	private static	IngredientDBManager mDBmanager 	= null;
	
	Context mContext = null;
	SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
	
	/* 싱글톤 패턴을 이용하여 하나의 객체만을 생성 */
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
	
	/* 레코드 추가 기능 */
	public long insert(ContentValues addRowValue){
		return getWritableDatabase().insert(TABLE_INGREDIENT, null, addRowValue);
	}
	
	/* 추가한 전체 레코드 반환 */
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

	public boolean getIsChoosed(int position, int type){
		
		Cursor a = getReadableDatabase().query( TABLE_INGREDIENT, new String[]{"isChoosed"}, 
									null, null, null, null, ORDER_BY[type]);
		
		a.moveToPosition(position);
		
		if(a.getInt(0)==1) return true;
		else return false;
	}
	
	public boolean compareExpirationDate(int position, int type){
		
		Cursor a = getReadableDatabase().query( TABLE_INGREDIENT, new String[]{"expirationDate"}, 
									null, null, null, null, ORDER_BY[type]);
		
		a.moveToPosition(position);
		
		if(a.getString(0).compareTo(format.format(new Date()))<0) return true;
		else return false;
	}
	
	
	/* 특정레코드 수정 */
	public int update(ContentValues updateRowValue, String whereClause, String[] whereArgs){
		return getWritableDatabase().update(TABLE_INGREDIENT, updateRowValue, whereClause, whereArgs);
	}
	
	/* 특정 레코드 삭제 */
	public int delete(String whereClause, String[] whereArgs){
		return getWritableDatabase().delete(TABLE_INGREDIENT, whereClause, whereArgs);
	}
	
	public int getCount(){
		
		return getReadableDatabase().query(TABLE_INGREDIENT, null, null, null, null, null, null).getCount();
	}
	
	public ArrayList<String> getApproachingItemArray(){
		ArrayList<String> arr = new ArrayList<String>();

		Calendar cal = Calendar.getInstance();
		String day1 = format.format(cal.getTime());
		
		cal.add(cal.DATE, 2);
		String day2 = format.format(cal.getTime());
		
		Cursor mCursor = getAll(ORDER_BY[2]);
		
		while(mCursor.moveToNext()){
			String tmp = mCursor.getString(2);
			if(tmp.compareTo(day1)>=0 && tmp.compareTo(day2)<=0)
				arr.add(mCursor.getString(1));
		}
		
		mCursor.close();
		
		return arr;
	}
	
}
