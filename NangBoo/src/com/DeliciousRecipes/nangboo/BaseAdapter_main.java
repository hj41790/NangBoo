package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaseAdapter_main extends BaseAdapter{
	
	Context 				mContext 		= null;
	ArrayList<Ingredient> 	mData 			= null;
	LayoutInflater 			mLayoutInflater = null;
	
	public BaseAdapter_main(Context context, ArrayList<Ingredient> data){
		mContext = context;
		mData = data;
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	public void add(int index, Ingredient addData){
		mData.add(index, addData);
		notifyDataSetChanged();
	}
	
	public void delete(int index){
		mData.remove(index);
		notifyDataSetChanged();
	}
	
	public void clear(){
		mData.clear();
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View itemLayout = convertView;
		ViewHolder viewHolder = null;
		
		if(itemLayout==null){
			itemLayout = mLayoutInflater.inflate(R.layout.ingredient_main, null);
			
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView)itemLayout.findViewById(R.id.name_ingredient);
			viewHolder.expiration = (TextView)itemLayout.findViewById(R.id.expiration_ingredient);
			
			itemLayout.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)itemLayout.getTag();
		}
		
		viewHolder.name.setText(mData.get(position).name);
		viewHolder.expiration.setText(mData.get(position).expirationDate);
		
		return itemLayout;
	}

}
