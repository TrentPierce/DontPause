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

/**
 * Developed by Trent Pierce for Pierce Holdings LLC
 *
 *Copyright 2014 Pierce Holdings LLC
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */

public class MyWidgetProvider2 extends AppWidgetProvider {

    public static String TOGGLE_DPS = "ToggleDPService";
    private static boolean serviceRunning = false;
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
        final int N = appWidgetIds.length;
        new Intent(context, MyService.class);

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget2);
            
            //Set default widget view
            remoteViews.setViewVisibility(R.id.buttontoggle, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.wText, View.VISIBLE);
            remoteViews.setImageViewResource(R.id.buttontoggle, R.drawable.wdont_pause);
            
            
            Intent newIntent = new Intent(context, MyWidgetProvider2.class);
            newIntent.setAction(TOGGLE_DPS);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.buttontoggle, pendingIntent);
            
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            Log.i(TOGGLE_DPS, "updated");
            
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	//Widget clicked
        if(intent.getAction().equals(TOGGLE_DPS)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget2);

            // Create a fresh intent 
            Intent serviceIntent = new Intent(context, MyService.class);

            //Was the service running?
            if(serviceRunning) {
            	//Service was running, so stop it.
                context.stopService(serviceIntent);
                //Show off button
                remoteViews.setViewVisibility(R.id.buttontoggle, View.VISIBLE);
                remoteViews.setImageViewResource(R.id.buttontoggle, R.drawable.wdont_pause);
                //Show toast to tell that service was turned off
                Toast.makeText(context, "Don't Pause Has Stopped", Toast.LENGTH_SHORT).show();
            } else {
            	//Service was stopped, so start it.
                context.startService(serviceIntent);
                //Set widget view to on
                remoteViews.setViewVisibility(R.id.buttontoggle, View.VISIBLE);
                remoteViews.setImageViewResource(R.id.buttontoggle, R.drawable.wdont_pause_selected);
                //Show toast to tell user that the service was started
                Toast.makeText(context, "Don't Pause Has Started", Toast.LENGTH_SHORT).show();
            }
            serviceRunning=!serviceRunning;
            ComponentName componentName = new ComponentName(context, MyWidgetProvider2.class);
            AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        }
        super.onReceive(context, intent);
    }
}