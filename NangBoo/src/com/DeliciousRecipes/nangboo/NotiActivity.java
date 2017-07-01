package com.DeliciousRecipes.nangboo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;

public class NotiActivity extends Activity implements OnCheckedChangeListener, OnClickListener{

	/* 구성 : 안내문구, 알림 onoff 스위치, 알림 시각 설정 DatePicker (설정된 시간 알려줌) */
	
	NotiManager sm;
	
	/* SharedPreferences */
	SettingPref pref;
	SharedPreferences settingPref;
	Editor editor;
	
	Button time_button;
	Calendar calendar;
	Calendar defaultC;
	
    SimpleDateFormat format;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noti);
		
		/* SharedPreferences 설정 */
		pref = new SettingPref(getApplicationContext());
		settingPref = pref.getPref();
		editor = pref.getEdit();
		
		/* 알람매니저 설정 */
		sm = NotiManager.getInstance(getApplicationContext());
		
		/* 테마 설정 */
		LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_noti);
		layout.setBackgroundResource(settingPref.getInt("THEME", R.color.yellow));		
		time_button = (Button)findViewById(R.id.time_button_noti);
		time_button.setBackgroundResource(settingPref.getInt("THEME", R.color.yellow));
		
		calendar = Calendar.getInstance();
		format = new SimpleDateFormat("a h시 m분");

		
		/* 액션바 BACK버튼 리스너 */
		Button back = (Button)findViewById(R.id.back_button_noti);
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		
		/* onoff 스위치 */
		Switch onoff = (Switch)findViewById(R.id.onoff_switch_noti);		
		onoff.setChecked(settingPref.getBoolean("notiIsOn", true)); //스위치 값을 저장된 값으로 설정
		onoff.setOnCheckedChangeListener(this);
		
		/* 알림시각 button */
		time_button.setOnClickListener(this);
		
		defaultC = Calendar.getInstance();
		defaultC.set(Calendar.HOUR_OF_DAY, 18);
		defaultC.set(Calendar.MINUTE, 0);

		calendar.setTimeInMillis(settingPref.getLong("notiTime", defaultC.getTimeInMillis()));
		
        String button_text = format.format(calendar.getTime());
        time_button.setText(button_text);
        

		
	}//onCreate끝

		
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked)
		{
			editor.putBoolean("notiIsOn", true);
			editor.commit();
			sm.setAlarm();
		}
		else
		{
			editor.putBoolean("notiIsOn", false);
			editor.commit();
			sm.resetAlarm();
		}
	}
	


	@Override
	public void onClick(View v) {

		calendar.setTimeInMillis(settingPref.getLong("notiTime", defaultC.getTimeInMillis()));
		
		TimePickerDialog dialog = new TimePickerDialog(v.getContext(), new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				System.out.println("hourOfDay:minute = " + hourOfDay +":"+ minute);
			
				Calendar setTime = Calendar.getInstance();
				
				SimpleDateFormat form = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
				System.out.println("onClick() setTime : "+ form.format(setTime.getTime()) );
				System.out.println("setTime.getTimeInMillis() : "+ setTime.getTimeInMillis());
				
				setTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				setTime.set(Calendar.MINUTE, minute);
				
				System.out.println("onClick() setTime : "+ form.format(setTime.getTime()) );
				System.out.println("setTime.getTimeInMillis() : "+ setTime.getTimeInMillis());
				
				editor.putLong("notiTime", setTime.getTimeInMillis());
				editor.commit();
				
				sm.setAlarm();
	            
	            String button_text = format.format(setTime.getTime());
	            time_button.setText(button_text);
			}
		}
		, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
		dialog.show();

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
