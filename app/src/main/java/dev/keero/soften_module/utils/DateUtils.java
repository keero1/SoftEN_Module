package dev.keero.soften_module.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    public static String getDate(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis((time / 1000) * 1000L);

        return DateFormat.format("dd MMM. yyyy", calendar).toString();
    }
}
