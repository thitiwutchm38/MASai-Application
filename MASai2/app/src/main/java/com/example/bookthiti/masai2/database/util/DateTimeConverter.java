package com.example.bookthiti.masai2.database.util;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeConverter {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                TimeZone timeZone = TimeZone.getTimeZone("VST");
                dateFormat.setTimeZone(timeZone);
                return dateFormat.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String dateToTimestamp(Date value) {
        TimeZone timeZone = TimeZone.getTimeZone("VST");
        dateFormat.setTimeZone(timeZone);
        return value == null ? null : dateFormat.format(value);
    }
}
