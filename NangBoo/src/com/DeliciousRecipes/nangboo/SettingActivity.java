package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingActivity extends Activity {

	ListView mListView = null;
	BaseAdapter_setting mAdapter = null;

	final String[] list = new String[]{"알림", "테마", "글자 크기", "버전 및 개발진 정보"};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		
//		// 리스트뷰 생성 및 아이템 선택 리스너 설정
//		mAdapter = new BaseAdapter_setting(this);
//		mListView = (ListView) findViewById(R.id.listview_main);
//		mListView.setAdapter(mAdapter);

		//ArrayAdapter 사용해봄....
		
		   mListView = (ListView)findViewById(R.id.listview_setting);
	       
//	        ArrayList<String> dataArr = new ArrayList<String>();
//	        dataArr.add("JAVA");
//	        dataArr.add("JSP");
//	        dataArr.add("EJB");
//	        dataArr.add("ANDROID");

	        ArrayAdapter<String> Adapter = new ArrayAdapter<String>  (this, android.R.layout.simple_list_item_1, list);

	        mListView.setAdapter(Adapter);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
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
