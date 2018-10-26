package com.chebur.obdnotifier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.ListPreference;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationListPreference extends ListPreference {
    PackageManager pm;

    public ApplicationListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        pm = context.getPackageManager();
        List<ResolveInfo> applications = getInstalledApplications(context);
        CharSequence[] entryTitles = new CharSequence[applications.size()];
        CharSequence[] entryValues = new CharSequence[applications.size()];
        int i = 0;
        for (ResolveInfo app : applications) {
            entryValues[i] = app.activityInfo.packageName;
            entryTitles[i] = app.loadLabel(pm);
            if (entryTitles[i] == null) entryTitles[i] = "unknown";
            i++;
        }
        setEntries(entryTitles);
        setEntryValues(entryValues);
    }

    public ApplicationListPreference(Context context) {
        this(context, null);
    }

    List<ResolveInfo> getInstalledApplications(final Context context){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(mainIntent,0);
        List<ResolveInfo> packages = new ArrayList<>();

        for(ResolveInfo resolveInfo : resolveInfoList){
            if(!isSystemPackage(resolveInfo))
                packages.add(resolveInfo);
        }

        Collections.sort(packages, new ResolveInfo.DisplayNameComparator(pm));
        return packages;
    }

    public boolean isSystemPackage(ResolveInfo resolveInfo){
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
