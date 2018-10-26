package com.chebur.obdnotifier.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceSettingsReader implements ISettingsReader
{
    private final SharedPreferences preferences;
    private final ResourcesSettingsReader resourcesReader;

    public SharedPreferenceSettingsReader(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resourcesReader = new ResourcesSettingsReader(context);
    }

    @Override
    public int getApplicationStartDelaySeconds(){
        String val = preferences.getString("app_start_delay", String.valueOf(resourcesReader.getApplicationStartDelaySeconds()));
        return Integer.parseInt( val );
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
        String val = preferences.getString("silent_notification_minutes", String.valueOf(resourcesReader.getSilentNotificationMinutes()));
        return Integer.parseInt( val );
    }

    @Override
    public boolean isStartAppAllowed(){
        return preferences.getBoolean("is_start_app", resourcesReader.isStartAppAllowed());
    }
}

