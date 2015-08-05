package com.DeliciousRecipes.nangboo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IngredientDBManager {
	
	static final String DB_INGREDIENT 		= "ingredient.db";
	static final String TABLE_INGREDIENT 	= "ingredient";
	static final int 	DB_VERSION 			= 1;
	
	Context mContext = null;
	
	private static	IngredientDBManager mDBmanager 	= null;
	private		   	SQLiteDatabase	   	mDatabase 	= null;
	
	/* 싱글톤 패턴을 이용하여 하나의 객체만을 생성 */
	public static IngredientDBManager getInstance(Context context){
		
		if(mDBmanager==null) mDBmanager = new IngredientDBManager(context);
		
		return mDBmanager;
	}
	
	private IngredientDBManager(Context context){
		mContext = context;
		
		mDatabase = context.openOrCreateDatabase(DB_INGREDIENT, Context.MODE_PRIVATE, null);
		
		mDatabase.execSQL(	"CREATE TABLE IF NOT EXISTS " + TABLE_INGREDIENT +
							"("+"_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
							"name				TEXT, 	" +
							"expirationDate		TEXT, 	" +
							"memo				TEXT, 	" +
							"isChoosed			INTEGER); " );
	}
	
	/* 레코드 추가 기능 */
	public long insert(ContentValues addRowValue){
		return mDatabase.insert(TABLE_INGREDIENT, null, addRowValue);
	}
	
	/* 추가한 전체 레코드 반환 */
	public Cursor query(String[] string, 
						String 	selection, 
						String[] selectionArgs, 
						String groupBy, 
						String having, 
						String orderBy){
		
		return mDatabase.query(	TABLE_INGREDIENT, string, selection, 
								selectionArgs, groupBy, having, orderBy);
	}
	
	public Cursor getAll(){
		String[] columns = new String[]{"_id", "name", "expirationDate", "memo", "isChoosed"};
		return mDatabase.query(TABLE_INGREDIENT, columns, null, null, null, null, null);
	}

	public boolean getIsChoosed(int position){
		
		Cursor a = mDatabase.query( TABLE_INGREDIENT, new String[]{"isChoosed"}, 
									null, null, null, null, null);
		
		a.moveToPosition(position);
		
		if(a.getInt(0)==1) return true;
		else return false;
	}
	
	/* 특정레코드 수정 */
	public int update(ContentValues updateRowValue, String whereClause, String[] whereArgs){
		return mDatabase.update(TABLE_INGREDIENT, updateRowValue, whereClause, whereArgs);
	}
	
	/* 특정 레코드 삭제 */
	public int delete(String whereClause, String[] whereArgs){
		return mDatabase.delete(TABLE_INGREDIENT, whereClause, whereArgs);
	}
	
	public int getCount(){
		return mDatabase.query(TABLE_INGREDIENT, null, null, null, null, null, null).getCount();
	}
}
