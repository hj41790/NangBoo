package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
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
	
	// 재료 추가
	public void add(int index, Ingredient addData){
		mData.add(index, addData);
		notifyDataSetChanged();
	}
	
	// 선택사항 삭제
	public int delete(){
		
		int i=0, count=0;
		while(i<mData.size()){
			if(mData.get(i).isChoosed) {
				mData.remove(i);
				count++;
				i--;
			}
			
			i++;
		}
		notifyDataSetChanged();
		
		return count;
	}
	
	public void modify(int position, String[] info){
		mData.get(position).name = info[0];
		mData.get(position).expirationDate = info[1];
		mData.get(position).memo = info[2];

		notifyDataSetChanged();
	}
	
	// 재료선택 버튼 껐을 때 선택 모두 초기화
	public void clear(){
		for(int i=0; i<mData.size(); i++){
			Ingredient tmp = mData.get(i);
			tmp.isChoosed = false;
		}
		notifyDataSetChanged();
	}	
	
	// 아이템이 선택 되었을 때 값 변경 및 뷰 새로그리기
	public void itemChoosed(int position){
		mData.get(position).isChoosed = !mData.get(position).isChoosed;
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
		
		if(mData.get(position).isChoosed) itemLayout.setBackgroundColor(Color.rgb(255,140,90));
		else itemLayout.setBackgroundColor(Color.rgb(255, 255, 0));
		
		viewHolder.name.setText(mData.get(position).name);
		viewHolder.expiration.setText(mData.get(position).expirationDate);
		
		return itemLayout;
	}

}
