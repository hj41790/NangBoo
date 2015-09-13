package com.DeliciousRecipes.nangboo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class NotiActivity extends Activity {

	
	
	NotificationManager nm;
	Notification n;
	NotificationCompat.Builder builder;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noti);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.action_bar_noti);
		layout.setBackgroundResource(MainActivity.settingPref.getInt("THEME", R.color.yellow));		
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
		onoff.setChecked(MainActivity.settingPref.getBoolean("notiIsOn", true)); //스위치 값을 저장된 값으로 설정
		onoff.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					MainActivity.editor.putBoolean("notiIsOn", true);
					MainActivity.editor.commit();
					Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
				}
				else
				{
					MainActivity.editor.putBoolean("notiIsOn", false);
					MainActivity.editor.commit();
					Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();					
				}
			}
			
		});
		
		
		
		//노티띄우기
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		Resources res = getResources();  
	    ///////////////////////////////////getApplicationContext()
	    Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);  
//	    notificationIntent.putExtra("notificationId", 9999); //전달할 값

	    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);  

	    builder = new NotificationCompat.Builder(getApplicationContext())  
	            .setContentTitle("유통기한이 코앞이에요!")  
	            .setContentText("재료이름 재료이름 재료이름 주르륵")  
	            .setTicker("유통기한이 코앞이에요!")  
	            .setSmallIcon(R.drawable.app_icon)  
	            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_icon))  
	            .setContentIntent(contentIntent)  
	            .setAutoCancel(true)
	            .setWhen(System.currentTimeMillis())  
	            .setDefaults( Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)  
	            .setNumber(13);  

    
        /* testing 버튼 */
		Button testing = (Button)findViewById(R.id.testing);
		testing.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(MainActivity.settingPref.getBoolean("notiIsOn", true))
				{

				    n = builder.build();
					nm.notify(1234, n); 
				}
			}
		});
		
		
	}//onCreate끝
	

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
