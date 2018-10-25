package com.chebur.obdnotifier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.ListPreference;
import android.util.AttributeSet;

import java.util.List;

public class ApplicationListPreference extends ListPreference {
    public ApplicationListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> applications = getInstalledApplications(context);
        CharSequence[] entryTitles = new CharSequence[applications.size()];
        CharSequence[] entryValues = new CharSequence[applications.size()];
        int i = 0;
        for (ResolveInfo app : applications) {
            entryValues[i] = app.activityInfo.name;
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
        return context.getPackageManager().queryIntentActivities( mainIntent, 0);
    }
}
