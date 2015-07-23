package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	ListView				mListView = null;
	BaseAdapter_main 		mAdapter = null;
	ArrayList<Ingredient> 	mData = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mData = new ArrayList<Ingredient>();
		
		for(int i=0; i<50; i++){
			Ingredient ingredient = new Ingredient();
			ingredient.name = "Àç·á" + i;
			ingredient.expirationDate = "15-08-1"+i;
			
			mData.add(ingredient);
		}
		
		mAdapter = new BaseAdapter_main(this, mData);
		
		mListView = (ListView) findViewById(R.id.listview_main);
		mListView.setAdapter(mAdapter);
		
	}
}
