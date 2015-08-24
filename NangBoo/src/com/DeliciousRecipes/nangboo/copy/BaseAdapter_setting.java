package com.DeliciousRecipes.nangboo.copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseAdapter_setting extends BaseAdapter{

	final String[] list = new String[]{"알림", "테마", "글자 크기", "버전 및 개발진 정보"};
	
	Context mContext = null;
	LayoutInflater mLayoutInflater = null;
	
	
	public BaseAdapter_setting(Context c) {
		this.mContext = c;
		this.mLayoutInflater = LayoutInflater.from(mContext);
	}

	
	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 쓸일이 없을 것 같은뎅..
		return null;
	}

	
}
