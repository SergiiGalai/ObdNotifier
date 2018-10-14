package com.chebur.obdnotifier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chebur.obdnotifier.R;
import com.chebur.obdnotifier.helpers.Helper;

public class SharedPreferenceSettingsReader implements ISettingsReader
{
    private final SharedPreferences preferences;
    private final ResourcesSettingsReader resourcesReader;

    public SharedPreferenceSettingsReader(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resourcesReader = new ResourcesSettingsReader(context);
    }

    @Override
    public int getApplicationStartDelayMillis(){
        return preferences.getInt("app_start_delay", resourcesReader.getApplicationStartDelayMillis());
    }

    @Override
    public String getPackageNameToStart() {
        return preferences.getString("start_app_id", resourcesReader.getPackageNameToStart());
    }

    @Override
    public String getTextToSpeak() {
        return preferences.getString("text_to_speak", resourcesReader.getTextToSpeak());
    }

    @Override
    public int getSilentNotificationMinutes() {
        return preferences.getInt("silent_notification_minutes", resourcesReader.getSilentNotificationMinutes());
    }
}

