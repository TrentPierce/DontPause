package com.pierceholdings.dontpause;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.preference.*;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by raidzero on 4/13/14 10:16 AM
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // this has to be done before any preferences are populated!
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

        addPreferencesFromResource(R.xml.preferences2);

        // get a list of launchable apps
        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);

        main.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);

        Collections.sort(launchables,
                new ResolveInfo.DisplayNameComparator(pm));

        PreferenceCategory targetCategory = (PreferenceCategory) findPreference("targetCategory");
        targetCategory.setTitle(getString(R.string.settings_header));

        for (ResolveInfo info : launchables) {

            // get app info
            ActivityInfo activity = info.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
            final String pkgName = name.getPackageName();
            CharSequence appName = info.loadLabel(pm);

            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(this);

            // set the data
            checkBoxPreference.setKey(pkgName);
            checkBoxPreference.setTitle(appName);
            checkBoxPreference.setSummary(pkgName);
            checkBoxPreference.setIcon(info.loadIcon(pm));

            // add to category
            targetCategory.addPreference(checkBoxPreference);
        }
    }

    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        CheckBoxPreference cbtp = (CheckBoxPreference) pref;
        final String pkgName = key;
       pkgName.toString();
       prefs.edit();
       pref.shouldCommit();
    }
}
//        // present dialog asking to kill the app
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.kill_app_title);
//        builder.setMessage(R.string.kill_app_detail);
//        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                // loop over running processes so we can find and kill the right one
//                ActivityManager  manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                List<ActivityManager.RunningAppProcessInfo> listOfProcesses = manager.getRunningAppProcesses();
//                for (ActivityManager.RunningAppProcessInfo process : listOfProcesses)
//                {
//                    if (process.processName.contains(pkgName))
//                    {
//                        // Ends the app
//                        manager.restartPackage(process.processName);
//                        break;
//                    }
//                }
//
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }
