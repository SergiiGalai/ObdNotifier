package com.chebur.obdnotifier;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

import com.chebur.obdnotifier.helpers.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

class ApplicationLauncher
{
    private final Activity context;

    ApplicationLauncher(Activity activity) {
        this.context = activity;
    }

    boolean isAppRunning(String packageName) {
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

    private List<String> getRunningAppProcesses() {
        UsageStatsManager usm = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar=Calendar.getInstance();
        long toTime=calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR,-1);
        long fromTime=calendar.getTimeInMillis();
        final List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY,fromTime,toTime);

        List<String> names = new ArrayList<>();

        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                names.add(usageStats.getPackageName());
            }
        }
        return names;
    }

    DelayedStartApplication delayedStart(final String packageName, final int delayMillis){
        DelayedStartApplication result = new DelayedStartApplication();
        result.handler = new Handler();
        result.method = new Runnable() {
            @Override
            public void run() {
                if (!tryStartApp(context, packageName)){
                    openOnPlayMarket(context, packageName);
                }
                context.finish();
            }
        };
        result.handler.postDelayed(result.method, delayMillis);
        return result;
    }

    void cancelStart(DelayedStartApplication delayedMethod){
        delayedMethod.handler.removeCallbacks(delayedMethod.method);
    }

    class DelayedStartApplication{
        Handler handler;
        Runnable method;
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
