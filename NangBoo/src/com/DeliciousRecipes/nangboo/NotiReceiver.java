package com.DeliciousRecipes.nangboo;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotiReceiver extends BroadcastReceiver{

	/* IngredientDBManager에서 유통기한 임박한 재료 목록을 받아온다 
	 * 알람매니저가 방송을 보내면 받고 노티 띄움!*/
	
	@Override
	public void onReceive(Context context, Intent intent) {

		System.out.println("DeepSleepReceiver onReceive");

		// 유통기한 임박 재료 목록 받아서 String에 저장
		ArrayList<String> tmp = IngredientDBManager.getInstance(context).getApproachingItemArray();
		String list;
		
		int count = tmp.size();
		int i=0;
		
		if(count==0) list = "";
		else{
			list = tmp.get(0);
			for(i = 1; i < tmp.size(); i++){
				list = list + ", " + tmp.get(i);
				if(i==2) {
					i++; 
					break;
				}
			}
		}
		
		if(count>i) list += " 외 "+(count-i)+"개의 식재료 유통기한이 임박했습니다!";
		else list += "의 유통기한이 임박했습니다!";
		
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
    
        Intent notificationIntent = new Intent(context, MainActivity.class);  
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
    
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)  
                .setContentTitle("어서 요리해주세요!")  
                .setContentText(list)  
                .setTicker("!!! 냉장고 주의보 발령 !!!")  
                .setSmallIcon(R.drawable.noti_icon)   
                .setContentIntent(contentIntent)  
                .setAutoCancel(true)  
                .setWhen(System.currentTimeMillis())  
                .setDefaults( Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)  
                .setNumber(count);  
    
        Notification  n = builder.build();  
        nm.notify((int)(Math.random()*100), n);  
        
        System.out.println("onReceive() 끝!");		
	}

	
}
