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

	/* IngredientDBManager���� ������� �ӹ��� ��� ����� �޾ƿ´� 
	 * �˶��Ŵ����� ����� ������ �ް� ��Ƽ ���!*/
	
	@Override
	public void onReceive(Context context, Intent intent) {

		System.out.println("DeepSleepReceiver onReceive");

		// ������� �ӹ� ��� ��� �޾Ƽ� String�� ����
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
		
		if(count>i) list += " �� "+(count-i)+"���� ����� ��������� �ӹ��߽��ϴ�!";
		else list += "�� ��������� �ӹ��߽��ϴ�!";
		
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
    
        Intent notificationIntent = new Intent(context, MainActivity.class);  
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
    
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)  
                .setContentTitle("� �丮���ּ���!")  
                .setContentText(list)  
                .setTicker("!!! ����� ���Ǻ� �߷� !!!")  
                .setSmallIcon(R.drawable.noti_icon)   
                .setContentIntent(contentIntent)  
                .setAutoCancel(true)  
                .setWhen(System.currentTimeMillis())  
                .setDefaults( Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)  
                .setNumber(count);  
    
        Notification  n = builder.build();  
        nm.notify((int)(Math.random()*100), n);  
        
        System.out.println("onReceive() ��!");		
	}

	
}
