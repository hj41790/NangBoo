package com.DeliciousRecipes.nangboo.copy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.DeliciousRecipes.nangboo.R;

public class NotiActivity extends Activity {

	public static boolean isNotiActivated = true; //알림 설정 기본으로 On
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noti);
		
		/* 액션바(?) back 버튼 리스너 부분 
		 * 왜 안될까!!!!!!!???????????
		 */
		Button back = (Button)findViewById(R.id.back_button_noti);
		back.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				finish();
			}
		});

		/* 알림 설정 onoff 스위치 부분*/
		Switch onoff = (Switch)findViewById(R.id.onoff_switch_noti);
		onoff.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked)
				{
					isNotiActivated = true;
					Toast.makeText(getApplicationContext(), Boolean.toString(isNotiActivated), Toast.LENGTH_SHORT).show();
				}
				else
				{
					isNotiActivated = false;
					Toast.makeText(getApplicationContext(), Boolean.toString(isNotiActivated), Toast.LENGTH_SHORT).show();					
				}
			}
		});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.noti, menu);
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
