package com.DeliciousRecipes.nangboo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchingActivity extends Activity{
	
	Button back, search;
	EditText searchingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_searching);
	    
	    Intent intent = getIntent();
		Bundle inputData = intent.getBundleExtra("SEARCHING_INGREDIENT");
		
		String initialText = inputData.getString("INGREDIENT");

		back = (Button) findViewById(R.id.back_button_searching);
		search = (Button) findViewById(R.id.searching_button);
	    searchingBar = (EditText) findViewById(R.id.searching_bar);
	    
	    searchingBar.setText(initialText);
	    
	    
	    back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {				
				finish();
			}
			
		});
	    
	    search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
			}
			
		});
	    
	    
	}

}
