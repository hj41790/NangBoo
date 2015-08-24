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

	final String[] items = new String[]{"�˸�", "�׸�", "���� ũ��", "���� �� ������ ����"};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		/* ��ư ������ : BACK ��ư�� */
		Button backButton = (Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		/* ����Ʈ�� list */
		ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		
		/* ����Ʈ�� ������ */
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch(position)
				{
				case 0 : //�˸�
					intent = new Intent(SettingActivity.this, NotiActivity.class);
					startActivity(intent);
					break;
				case 1 : //�׸�
					Toast.makeText(getApplicationContext(), "�׸�", Toast.LENGTH_SHORT).show();
					break;
				case 2 : //����ũ��
					Toast.makeText(getApplicationContext(), "����ũ��", Toast.LENGTH_SHORT).show();
					break;
				case 3 : //����
					Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
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
