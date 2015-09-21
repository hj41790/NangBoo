package com.DeliciousRecipes.nangboo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	final String[] items = new String[]{"    �˸�", "    �׸�", "    ���� �� ������ ����"};
	
	AlertDialog.Builder builder = null;

	private int selected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		//�׸�����
		LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_setting);
		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
		
		/* ��ư ������ : BACK ��ư�� */
		Button backButton = (Button)findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(0, intent);
				
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
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
				switch(position)
				{
					case 0 : //�˸�
						intent = new Intent(SettingActivity.this, NotiActivity.class);
						startActivity(intent);
						break;
					case 1 : //�׸�
						createThemeDialog();
						break;
					case 2 : //����ũ��
						Toast.makeText(getApplicationContext(), "����ũ��", Toast.LENGTH_SHORT).show();
						break;
					case 3 : //����
						intent = new Intent(SettingActivity.this, InfoActivity.class);
						startActivity(intent);
						break;
				}
			}
			
		});
		
	}
	
	
	private void createThemeDialog(){
		
		//��� ���� ��� �����ϴ� �˾�â
		builder = new AlertDialog.Builder(SettingActivity.this);
		builder.setTitle("�׸�");
		final String items[] = {"��� �Ķ���", "������"};
		
		//�ʱ� ���ð� ����. �⺻�� yellow(��� �Ķ���)
		int checked;
		switch(MainActivity.settingPref.getInt("THEME", R.color.yellow))
		{
			case R.color.green :
				checked = 1;
				break;
			default :
				checked = 0;
		}
		
		builder.setSingleChoiceItems(items, checked,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selected = which;
					}
				})
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
					
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Ȯ�� ��ư ��ġ
						if (selected == 0){ // ��� �Ķ���
							MainActivity.editor.putInt("THEME", R.color.yellow);
							MainActivity.editor.commit();
						}
						else if (selected == 1) // ������
						{
							MainActivity.editor.putInt("THEME", R.color.green);
							MainActivity.editor.commit();
						}

						applyTheme();

						
						
						//�׸� ����
						LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_setting);
						layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ��� ��ư ��ġ
						dialog.dismiss();
					}
				});
		builder.show();
	}//createThemeDialog() ��

	
//	void applyTheme(int res)
//	{
//		LinearLayout layout = (LinearLayout)findViewById(res);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
//	}
	
	//�׸� ���� �޼���
	void applyTheme()
	{
		LinearLayout layout;
		switch(MainActivity.settingPref.getInt("THEME", R.color.yellow))
		{
			case R.color.green :
				layout = (LinearLayout) findViewById(R.id.action_bar_setting);
				layout.setBackgroundColor(Color.rgb(214, 251, 156));
				break;
			default :
				layout = (LinearLayout) findViewById(R.id.action_bar_setting);
				layout.setBackgroundColor(Color.rgb(255,255,0));
		}
		
//		layout = (LinearLayout) findViewById(R.id.action_bar_bookmark);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
//		layout = (LinearLayout) findViewById(R.id.action_bar_modify);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
//		layout = (LinearLayout) findViewById(R.id.action_bar_noti);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
//		layout = (LinearLayout) findViewById(R.id.action_bar_searching);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
//		layout = (LinearLayout) findViewById(R.id.action_bar_setting);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
//		layout = (LinearLayout) findViewById(R.id.action_bar_webview);
//		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));
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
