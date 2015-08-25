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

public class BaseAdapter_bookmark extends BaseAdapter {
	
	Context mContext = null;
	LayoutInflater mLayoutInflater = null;
	
	BookmarkDBManager mDBmanager = null;

	public BaseAdapter_bookmark(Context context) {
		
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mDBmanager = BookmarkDBManager.getInstance(mContext);
		
		Cursor a = mDBmanager.getAll();

		while (a.moveToNext()) {
			Bookmark tmp = new Bookmark();
			System.out.println(a.getString(1));
			System.out.println(a.getString(2));
		}

		a.close();
		
	}
	
	// 즐겨찾기 추가
	public void add(ContentValues addRowValue) {

		addRowValue.put("isChoosed", 0);

		mDBmanager.insert(addRowValue);
		notifyDataSetChanged();
	}

	// 선택사항 삭제
	public int delete() {

		Cursor mCursor = mDBmanager.query(	new String[] { "isChoosed" }, null,
											null, null, null, null);

		int count = 0;

		while (mCursor.moveToNext()) {
			if (mCursor.getInt(0) == 1)
				count++;
		}

		mCursor.close();

		mDBmanager.delete("isChoosed=1", null);

		notifyDataSetChanged();

		return count;
	}
	
	public void modify(int position, String name) {
		
		ContentValues updateRowValue = new ContentValues();
		updateRowValue.put("name", name);
		
		mDBmanager.update(updateRowValue, "_id="+getItemId(position), null);
		

		notifyDataSetChanged();
	}
	
	public void clear() {
		
		ContentValues updateRowValue = new ContentValues();
		updateRowValue.put("isChoosed", 0);
		
		mDBmanager.update(updateRowValue, "isChoosed=1", null);
		
		notifyDataSetChanged();
	}
	
	// 아이템이 선택 되었을 때 값 변경 및 뷰 새로그리기
	public void itemChoosed(int position) {

		ContentValues updateRowValue = new ContentValues();

		if (mDBmanager.getIsChoosed(position))
			updateRowValue.put("isChoosed", 0);
		else
			updateRowValue.put("isChoosed", 1);

		mDBmanager.update(updateRowValue, "_id=" + getItemId(position), null);

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDBmanager.getCount();
	}

	@Override
	public Object getItem(int position) {
		
		String[] columns = new String[]{"name", "URL"};
		Cursor mCursor = mDBmanager.query(columns, "_id="+getItemId(position), null, null, null, null);
		mCursor.moveToFirst();

		Bookmark tmp = new Bookmark();
		tmp.name = mCursor.getString(0);
		tmp.URL = mCursor.getString(1);
		
		mCursor.close();
		
		return tmp;
	}

	@Override
	public long getItemId(int position) {
		Cursor a = mDBmanager.query(new String[] { "_id" }, null, null, null, null, null);
		a.moveToPosition(position);

		int id = a.getInt(0);
		a.close();

		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemLayout = mLayoutInflater.inflate(R.layout.listview_bookmark, null);

		TextView name = (TextView) itemLayout.findViewById(R.id.name_bookmark_listview);
		
		if(mDBmanager.getIsChoosed(position))
			itemLayout.setBackgroundColor(Color.rgb(255, 140, 90));
		else
			itemLayout.setBackgroundColor(Color.rgb(255, 255, 0));
		

		Cursor mCursor = mDBmanager.getAll();
		mCursor.moveToPosition(position);
		
		name.setText(mCursor.getString(1));
		System.out.println(mCursor.getString(1));
		
		mCursor.close();
		return itemLayout;
	}

}
