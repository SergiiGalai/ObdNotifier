package com.chebur.obdnotifier;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ApplicationLauncher
{
    final Activity context;

    public ApplicationLauncher(Activity activity) {
        this.context = activity;
    }

    public boolean isAppRunning(String packageName) {
        final List<String> processes = getRunningAppProcesses();
        if (processes != null)
        {
            for (final String processName : processes) {
                if (processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getRunningAppProcesses() {
        UsageStatsManager usm = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar=Calendar.getInstance();
        long toTime=calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR,-1);
        long fromTime=calendar.getTimeInMillis();
        final List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,fromTime,toTime);

        List<String> names = new ArrayList<>();

        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                names.add(usageStats.getPackageName());
            }
        }
        return names;
    }


    public void delayedStart(@StringRes final int applicationNameResourseId, @IntegerRes final int delayMillisResId){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String packageName = Helper.resourceToString(context, applicationNameResourseId);
                if (!tryStartApp(context, packageName)){
                    openOnPlayMarket(context, packageName);
                }
                context.finish();
            }
        }, context.getResources().getInteger(delayMillisResId));
    }

    private static boolean tryStartApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return intent != null;
    }

    private static void openOnPlayMarket(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}