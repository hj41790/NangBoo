package com.DeliciousRecipes.nangboo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class NotiManager {

	/* AlarmManager를 싱글턴 패턴으로 구현 */

	private static	NotiManager mNotimanager 	= null;
	static AlarmManager am = null;
	private long PERIOD = 1000 * 60 * 60 * 12; //방송 주기
	private Context context;
	private boolean isSet;
	
	private IngredientDBManager DBManager= null;
	
	/* SharedPreferences */
	SettingPref pref;
	SharedPreferences settingPref;

	
	public static NotiManager getInstance(Context context) {

		if (am == null) mNotimanager = new NotiManager(context);
		
		return mNotimanager;
	}

	private NotiManager(Context context) {
		DBManager = IngredientDBManager.getInstance(context);

		this.context = context; // Context 받아오기
		if (am == null)
			am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		/* SharedPreferences 설정 */
		if (pref == null || settingPref == null) {
			pref = new SettingPref(context);
			settingPref = pref.getPref();
		}

	}

	void setAlarm()
	{
		isSet = true;
		
		//시각설정 
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		long time = settingPref.getLong("notiTime", calendar.getTimeInMillis());
		
		if(time < Calendar.getInstance().getTimeInMillis()) time += PERIOD;
		
		calendar.setTimeInMillis(time);
		
		SimpleDateFormat form = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		System.out.println("setAlarm() calendar : "+ form.format(calendar.getTime()) );
		System.out.println("calendar.getTimeInMillis() : "+ calendar.getTimeInMillis());
		
		//알람매니저 설정
//		am.setInexactRepeating(type, triggerAtMillis, intervalMillis, operation);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+5000, PERIOD, pendingIntent());
		
		System.out.println("setAlarm()");
	}
	
	void resetAlarm()
	{
		isSet = false;
		
		am.cancel(pendingIntent());
		System.out.println("resetAlarm()");
	}

	/* 방송하는 PendingIntent */
	private PendingIntent pendingIntent()
	{
		ArrayList<String> tmp = IngredientDBManager.getInstance(context).getApproachingItemArray();
		
		// 유통기한 임박 재료가 없으면 null을 반환 => 노티 안울림
		PendingIntent pen = null;
		
		if(DBManager.getCount()!=0 && tmp.size()>0)
		{
			Intent intent = new Intent();
			intent.setAction("com.DeliciousRecipes.nangboo.action.NOTI");
			
//			PendingIntent.getBroadcast(context, requestCode, intent, flags)
			pen = PendingIntent.getBroadcast(context, 0, intent, 0);
		}
		return pen;
	}

	public boolean getIsSet() {
		return isSet;
	}

}