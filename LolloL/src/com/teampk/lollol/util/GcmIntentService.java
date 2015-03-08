package com.teampk.lollol.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.teampk.lollol.R;
import com.teampk.lollol.LolloLActivity;

public class GcmIntentService extends IntentService{

    public static final int NOTIFICATION_ID = 1;
    
    private NotificationManager mNotificationManager;
    public NotificationCompat.Builder builder;
	
	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		
		if(!extras.isEmpty()){
			
			if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
				
				sendNotification(extras);
				
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)){
				
				sendNotification(extras);
				
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
				
				Log.i("GCM Test", "Complete Work @" + SystemClock.elapsedRealtime());
				
				sendNotification(extras);
				Log.i("GCM Test", "Received: " + extras.toString());
				
			} 
			
		}
		
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(Bundle extras) {
		
		String title = extras.getString("title");
		String message = extras.getString("message");
		String tickerText = extras.getString("tickerText");
		
		mNotificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, LolloLActivity.class), 0);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setContentTitle(title)
		.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
		.setContentText(message)
		.setSmallIcon(R.drawable.ic_lollol_notification)
		.setTicker(tickerText);
		
		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		
	}
}
