package com.chebur.obdnotifier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceRunApplicationRepository implements IRunApplicationRepository
{
    private final SharedPreferences preferences;

    public SharedPreferenceRunApplicationRepository(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public long getLastTimeNotified(){
        return preferences.getLong("last_time_notified", 0);
    }

    @Override
    public void saveLastTimeNotified(long value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("last_time_notified", value);
        editor.apply();
    }
}
