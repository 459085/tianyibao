package com.ctsi.tianyibao.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000L;

    public static Date addSomeDays(Date startDate, int days) {
        return new Date(startDate.getTime() + days * MILLISECONDS_PER_DAY);
    }

    public static Date getSartTimeOfAddSomeDays(Date startDate, int days) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date day = addSomeDays(startDate,days);
        return getStartTimeOfDay(day);
    }


    public static Date getStartTimeOfDay(Date day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
