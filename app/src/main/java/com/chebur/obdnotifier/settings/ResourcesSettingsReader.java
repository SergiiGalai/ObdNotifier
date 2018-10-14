package com.chebur.obdnotifier.settings;

import android.content.Context;

import com.chebur.obdnotifier.R;
import com.chebur.obdnotifier.helpers.Helper;

public class ResourcesSettingsReader implements ISettingsReader
{
    private final Context context;

    public ResourcesSettingsReader(Context context){
        this.context = context;
    }

    @Override
    public int getApplicationStartDelayMillis(){
        return Helper.resourceToInt(context, R.integer.run_app_delay_millis);
    }

    @Override
    public String getPackageNameToStart() {
        return Helper.resourceToString(context, R.string.appid_to_run);
    }

    @Override
    public String getTextToSpeak() {
        return Helper.resourceToString(context, R.string.notification_title);
    }

    @Override
    public int getSilentNotificationMinutes() {
        return Helper.resourceToInt(context, R.integer.donot_notify_delay_minutes);
    }
}
