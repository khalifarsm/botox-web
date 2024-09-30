package com.pandora.api.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date afterDays(int days){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date tenDaysAgo = calendar.getTime();
        return tenDaysAgo;
    }

    public static Date afterSeconds(int seconds){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.SECOND, seconds);
        Date tenDaysAgo = calendar.getTime();
        return tenDaysAgo;
    }
}
