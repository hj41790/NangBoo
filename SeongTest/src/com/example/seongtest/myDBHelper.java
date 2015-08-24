package com.example.seongtest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper{
	public myDBHelper(Context context){
		super(context,"Ingredients",null,1);
	}
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE groupTBL(gName CHAR(20) PRIMARY KEY, gNumber integer);");
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS groupTBL");
		onCreate(db);
	}
}
