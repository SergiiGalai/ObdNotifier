package com.chebur.obdnotifier;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.util.List;

public class Helper
{
    public static String resourceToString(final Context context, @StringRes final int resId){
        return context.getResources().getText(resId).toString();
    }
}
