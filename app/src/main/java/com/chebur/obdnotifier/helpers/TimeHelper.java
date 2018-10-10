package com.chebur.obdnotifier.helpers;

import java.util.Date;

public class TimeHelper {
    public static Date toDate(long millis){
        return new Date(millis);
    }
    public static long now(){ return System.currentTimeMillis(); }
}
