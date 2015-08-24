package com.DeliciousRecipes.nangboo.copy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.DeliciousRecipes.nangboo.R;

public class SettingActivity extends Activity {

	final String[] items = new String[]{"알림", "테마", "글자 크기", "버전 및 개발진 정보"};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		/* 버튼 리스터 : BACK 버튼만 */
		Button backButton = (Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		/* 리스트뷰 list */
		ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		
		/* 리스트뷰 리스너 */
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch(position)
				{
				case 0 : //알림
					intent = new Intent(SettingActivity.this, NotiActivity.class);
					startActivity(intent);
					break;
				case 1 : //테마
					Toast.makeText(getApplicationContext(), "테마", Toast.LENGTH_SHORT).show();
					break;
				case 2 : //글자크기
					Toast.makeText(getApplicationContext(), "글자크기", Toast.LENGTH_SHORT).show();
					break;
				case 3 : //버전
					Toast.makeText(getApplicationContext(), "버전", Toast.LENGTH_SHORT).show();
					break;
				}
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.abcd, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
