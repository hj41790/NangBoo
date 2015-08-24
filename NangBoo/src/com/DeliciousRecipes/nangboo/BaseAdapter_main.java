package com.DeliciousRecipes.nangboo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaseAdapter_main extends BaseAdapter {
	
	public static final int SORT_NAME = 0;
	public static final int SORT_DATE = 1;
	public static final int SORT_REGISTER = 2;
	
	String[] ORDER_BY = new String[]{"name asc", "expirationDate asc", "_id asc"};
	
	Context mContext = null;
	LayoutInflater mLayoutInflater = null;
	IngredientDBManager mDBmanager = null;
	
	int sort_type;
	
	public BaseAdapter_main(Context context) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mDBmanager = IngredientDBManager.getInstance(mContext);	
		
		sort_type = SORT_REGISTER;
	}

	// 재료 추가
	public void add(Ingredient addData) {
		
		ContentValues addRowValue = new ContentValues();
		
		addRowValue.put("name", addData.name);
		addRowValue.put("expirationDate", addData.expirationDate);
		addRowValue.put("memo", addData.memo);
		addRowValue.put("isChoosed", 0);
		
		mDBmanager.insert(addRowValue);
		notifyDataSetChanged();
	}

	// 선택사항 삭제
	public int delete() {
		
		Cursor mCursor = mDBmanager.query(  new String[]{"isChoosed"},
											null, null, null, null, ORDER_BY[sort_type]);
		
		int count=0;
		
		while(mCursor.moveToNext()){
			if(mCursor.getInt(0)==1) count++;
		}
		
		mCursor.close();
		
		mDBmanager.delete("isChoosed=1", null);
		
		notifyDataSetChanged();

		return count;
	}

	public void modify(int position, Ingredient tmp) {
		
		ContentValues updateRowValue = new ContentValues();
		updateRowValue.put("name", tmp.name);
		updateRowValue.put("expirationDate", tmp.expirationDate);
		updateRowValue.put("memo", tmp.memo);
		
		mDBmanager.update(updateRowValue, "_id="+getID(position), null);
		

		notifyDataSetChanged();
	}

	// 재료선택 버튼 껐을 때 선택 모두 초기화
	public void clear() {
		
		ContentValues updateRowValue = new ContentValues();
		updateRowValue.put("isChoosed", 0);
		
		mDBmanager.update(updateRowValue, "isChoosed=1", null);
		
		notifyDataSetChanged();
	}

	// 아이템이 선택 되었을 때 값 변경 및 뷰 새로그리기
	public void itemChoosed(int position) {
		
		ContentValues updateRowValue = new ContentValues();
		
		if(mDBmanager.getIsChoosed(position, sort_type))
			updateRowValue.put("isChoosed", 0);
		else 
			updateRowValue.put("isChoosed", 1);
		
		mDBmanager.update(updateRowValue, "_id="+getID(position), null);
		
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDBmanager.getCount();
	}

	@Override
	public Object getItem(int position) {
		
		String[] columns = new String[]{"name", "expirationDate", "memo"};
		Cursor mCursor = mDBmanager.query(columns, "_id="+getID(position), null, null, null, ORDER_BY[sort_type]);
		mCursor.moveToFirst();

		Ingredient ingredient = new Ingredient();
		ingredient.name = mCursor.getString(0);
		ingredient.expirationDate = mCursor.getString(1);
		ingredient.memo = mCursor.getString(2);
		
		mCursor.close();
		
		return ingredient;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemLayout = convertView;
		ViewHolder viewHolder = null;
		
		if (itemLayout == null) {
			itemLayout = mLayoutInflater
					.inflate(R.layout.ingredient_main, null);

			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) itemLayout.findViewById(R.id.name_ingredient);
			viewHolder.expiration = (TextView) itemLayout.findViewById(R.id.expiration_ingredient);

			itemLayout.setTag(viewHolder);
		} 
		else {
			viewHolder = (ViewHolder) itemLayout.getTag();
		}
		
		if(mDBmanager.getIsChoosed(position, sort_type))
			itemLayout.setBackgroundColor(Color.rgb(255, 140, 90));
		else
			itemLayout.setBackgroundColor(Color.rgb(255, 255, 0));
		

		Cursor mCursor = mDBmanager.getAll(ORDER_BY[sort_type]);
		mCursor.moveToPosition(position);
		
		Ingredient tmp = new Ingredient();
		tmp.expirationDate = mCursor.getString(2);
		tmp.stringToDate();
		
		viewHolder.name.setText(mCursor.getString(1));
		viewHolder.expiration.setText(Ingredient.dateFormat.format(tmp.date));
		
		mCursor.close();

		return itemLayout;
	}
	
	private int getID(int position){
		
		Cursor a = mDBmanager.query(new String[]{"_id"}, null, null, null, null, ORDER_BY[sort_type]);
		a.moveToPosition(position);
		
		return a.getInt(0);
	}
	
	public void sort(int type){
		sort_type = type;
		notifyDataSetChanged();
	}
}
