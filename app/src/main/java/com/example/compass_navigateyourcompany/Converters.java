package com.example.compass_navigateyourcompany;

import androidx.room.TypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @TypeConverter
    public static Date fromTimestamp(String value) {
        try {
            return value == null ? null : dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : dateFormat.format(date);
    }
}
