package com.DeliciousRecipes.nangboo.copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseAdapter_setting extends BaseAdapter{

	final String[] list = new String[]{"�˸�", "�׸�", "���� ũ��", "���� �� ������ ����"};
	
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
		// ������ ���� �� ������..
		return null;
	}

	
}
