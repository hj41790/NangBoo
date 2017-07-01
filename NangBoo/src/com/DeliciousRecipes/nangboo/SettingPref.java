package com.DeliciousRecipes.nangboo;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingPref {
	/* 싱글턴 패턴으로 구현 */
	
	static SharedPreferences settingPref = null;
	static Editor editor = null;
	
	SettingPref(Context context)
	{
		//맨 처음 settingPref와 editor를 만드는 부분. 한번만 실행되겠지
		if(settingPref == null)
		{
			settingPref = context.getSharedPreferences("settingPref", 0);
			editor = settingPref.edit();			
			
			//notiIsOn
			if (!settingPref.contains("notiIsOn"))
				editor.putBoolean("notiIsOn", true);
			
			//THEME
			if (!settingPref.contains("THEME"))
				editor.putInt("THEME", R.color.yellow);

			//notiTime
			if (!settingPref.contains("notiTime"))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, 18);
				calendar.set(Calendar.MINUTE, 0);
				editor.putLong("notiTime", calendar.getTimeInMillis());
			}

			editor.commit();
		}
		
	}//constructor 끝

	SharedPreferences getPref()
	{
		return settingPref;
	}
	
	Editor getEdit()
	{
		return editor;
	}

}
