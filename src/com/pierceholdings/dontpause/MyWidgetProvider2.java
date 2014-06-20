package com.pierceholdings.dontpause;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidgetProvider2 extends AppWidgetProvider {

    public static String TOGGLE_WINET = "ToggleWiNetService";
    private static boolean serviceRunning = false;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
        final int N = appWidgetIds.length;
        new Intent(context, MyService.class);

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget2);
            

            remoteViews.setViewVisibility(R.id.buttontoggle, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.wText, View.VISIBLE);
            remoteViews.setImageViewResource(R.id.buttontoggle, R.drawable.wdont_pause);
            
            

            Intent newIntent = new Intent(context, MyWidgetProvider2.class);
            newIntent.setAction(TOGGLE_WINET);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.buttontoggle, pendingIntent);
            
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            Log.i(TOGGLE_WINET, "updated");
            
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	
        if(intent.getAction().equals(TOGGLE_WINET)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget2);

            // Create a fresh intent 
            Intent serviceIntent = new Intent(context, MyService.class);

            if(serviceRunning) {
                context.stopService(serviceIntent);
                remoteViews.setViewVisibility(R.id.buttontoggle, View.VISIBLE);
                remoteViews.setImageViewResource(R.id.buttontoggle, R.drawable.wdont_pause);
                Toast.makeText(context, "Don't Pause Has Stopped", Toast.LENGTH_SHORT).show();
            } else {
                context.startService(serviceIntent);
                remoteViews.setViewVisibility(R.id.buttontoggle, View.VISIBLE);
                remoteViews.setImageViewResource(R.id.buttontoggle, R.drawable.wdont_pause_selected);
                Toast.makeText(context, "Don't Pause Has Started", Toast.LENGTH_SHORT).show();
            }
            serviceRunning=!serviceRunning;
            ComponentName componentName = new ComponentName(context, MyWidgetProvider2.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        }
        super.onReceive(context, intent);
    }
}