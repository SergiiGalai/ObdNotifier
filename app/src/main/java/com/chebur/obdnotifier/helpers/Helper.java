package com.chebur.obdnotifier.helpers;

import android.content.Context;
import android.support.annotation.StringRes;

public class Helper
{
    public static String resourceToString(final Context context, @StringRes final int resId){
        return context.getResources().getText(resId).toString();
    }
}
