package com.liaopeixin.lib_utils;

import android.annotation.SuppressLint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

/**
 * 日期时间工具类
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2015/8/5
 */
public class DateUtils extends android.text.format.DateUtils {
    public static final int Second = 0;
    public static final int Minute = 1;
    public static final int Hour = 2;
    public static final int Day = 3;

    @IntDef(value = {Second, Minute, Hour, Day})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DifferenceMode {
    }

    public static long calculateDifferentSecond(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Second);
    }

    public static long calculateDifferentMinute(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Minute);
    }

    public static long calculateDifferentHour(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Hour);
    }

    public static long calculateDifferentDay(Date startDate, Date endDate) {
        return calculateDifference(startDate, endDate, Day);
    }

    public static long calculateDifferentSecond(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Second);
    }

    public static long calculateDifferentMinute(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Minute);
    }

    public static long calculateDifferentHour(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Hour);
    }

    public static long calculateDifferentDay(long startTimeMillis, long endTimeMillis) {
        return calculateDifference(startTimeMillis, endTimeMillis, Day);
    }

    /**
     * 计算两个时间戳之间相差的时间戳数
     */
    public static long calculateDifference(long startTimeMillis, long endTimeMillis, @DifferenceMode int mode) {
        return calculateDifference(new Date(startTimeMillis), new Date(endTimeMillis), mode);
    }

    /**
     * 计算两个日期之间相差的时间戳数
     */
    public static long calculateDifference(Date startDate, Date endDate, @DifferenceMode int mode) {
        long[] different = calculateDifference(startDate, endDate);
        if (mode == Minute) {
            return different[2];
        } else if (mode == Hour) {
            return different[1];
        } else if (mode == Day) {
            return different[0];
        } else {
            return different[3];
        }
    }

    private static long[] calculateDifference(Date startDate, Date endDate) {
        return calculateDifference(endDate.getTime() - startDate.getTime());
    }

    private static long[] calculateDifference(long differentMilliSeconds) {
        long secondsInMilli = 1000;//1s==1000ms
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = differentMilliSeconds / daysInMilli;
        differentMilliSeconds = differentMilliSeconds % daysInMilli;
        long elapsedHours = differentMilliSeconds / hoursInMilli;
        differentMilliSeconds = differentMilliSeconds % hoursInMilli;
        long elapsedMinutes = differentMilliSeconds / minutesInMilli;
        differentMilliSeconds = differentMilliSeconds % minutesInMilli;
        long elapsedSeconds = differentMilliSeconds / secondsInMilli;
        return new long[]{elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds};
    }

    /**
     * 计算每月的天数
     */
    public static int calculateDaysInMonth(int month) {
        return calculateDaysInMonth(0, month);
    }

    /**
     * 根据年份及月份计算每月的天数
     */
    public static int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            if (year <= 0) {
                return 29;
            }
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    /**
     * 月日时分秒，0-9前补0
     */
    @NonNull
    public static String fillZero(int number) {
        return number < 10 ? "0" + number : "" + number;
    }

    /**
     * 截取掉前缀0以便转换为整数
     *
     * @see #fillZero(int)
     */
    public static int trimZero(@NonNull String text) {
        if (text.startsWith("0")) {
            text = text.substring(1);
        }
        return Integer.parseInt(text);
    }

    /**
     * 功能：判断日期是否和当前date对象在同一天。
     * 参见：http://www.cnblogs.com/myzhijie/p/3330970.html
     *
     * @param date 比较的日期
     * @return boolean 如果在返回true，否则返回false。
     */
    public static boolean isSameDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        Calendar nowCalendar = Calendar.getInstance();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(date);
        return (nowCalendar.get(Calendar.ERA) == newCalendar.get(Calendar.ERA) &&
                nowCalendar.get(Calendar.YEAR) == newCalendar.get(Calendar.YEAR) &&
                nowCalendar.get(Calendar.DAY_OF_YEAR) == newCalendar.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr    时间字符串
     * @param dataFormat 当前时间字符串的格式。
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr, String dataFormat) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
            Date date = dateFormat.parse(dateStr);
            return new Date(date.getTime());
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr 时间字符串
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Long parseLong(String dateStr) {
        try {
            Date date = parseDate(dateStr);
            return date.getTime() / 1000;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换成日期<br/>
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss字符串
     * @return Date 日期 ,转换异常时返回null。
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }


    public static Date parseDate1(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    public static Date parseDateNoSS(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm");
    }

    /**
     * 将指定的日期转换为一定格式的字符串
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    public static String getTimeToString(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getYear(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public static String getMon(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(date);
    }

    public static String getDay(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String gethoure(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH");
        return format.format(date);
    }

    public static String getmm(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("mm");
        return format.format(date);
    }

    public static String gethhmm(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH : mm");
        return format.format(date);
    }

    public static String getyyyyMMddHHmm(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


    /**
     * 将当前日期转换为一定格式的字符串
     */
    public static String formatDate(String format) {
        return formatDate(Calendar.getInstance(Locale.CHINA).getTime(), format);
    }

    /**
     * 获得当前时间的时间戳
     *
     * @return
     */
    public static String getCurrentTimeTamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToStringhh(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd HH:mm:ss
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateTohhmm(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");//yyyy-MM-dd HH:mm:ss
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateTommddhhmm(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");//yyyy-MM-dd HH:mm:ss
        return sf.format(d);
    }

    /**
     * 随机生成min到max之间的任意数
     *
     * @param min
     * @param max
     * @return
     */
    public static int nextInt(final int min, final int max) {
        Random rand = new Random();
        int tmp = Math.abs(rand.nextInt());
        return tmp % (max - min + 1) + min;
    }

    /**
     * 判断时间是几天几分几秒
     */
    public static String formateHHMMSS(long time) {
        time = time / 1000;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = time / (24 * 60 * 60);
        hour = (time / (60 * 60) - day * 24);
        min = ((time / (60)) - day * 24 * 60 - hour * 60);
        sec = (time - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuffer stringBuffer = new StringBuffer("");
        stringBuffer.append(hour < 10 ? "0" + hour : hour);
        stringBuffer.append("小时");
        stringBuffer.append(min < 10 ? "0" + min : min);
        stringBuffer.append("分钟");
//        stringBuffer.append(sec < 10 ? "0" + sec : sec);
        return stringBuffer.toString();
    }

    public static boolean isThirtyMinute(long time) {
        time = time / 1000;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = time / (24 * 60 * 60);
        hour = (time / (60 * 60) - day * 24);
        min = ((time / (60)) - day * 24 * 60 - hour * 60);
        if (day > 0) {
            return true;
        }

        if (hour > 0) {
            return true;
        }
        if (min > 30) {
            return true;
        }

        return false;
    }

    /**
     * 计算2个时间戳的间距
     */
    //long time1, long time2
    public static String getDistanceTime() {
        long time1 = System.currentTimeMillis() / 1000;
        Date time3 = parseDateNoSS("2018-3-6 9:00");
        long time2 = time3.getTime() / 1000;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        String flag;
        if (time1 < time2) {
            diff = time2 - time1;
            flag = "前";
        } else {
            diff = time1 - time2;
            flag = "后";
        }
        day = diff / (24 * 60 * 60);
        hour = (diff / (60 * 60) - day * 24);
        min = ((diff / (60)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) {
            //这里不是同一天
            if (day > 1) {
                return getDateToString(time1);
            } else {
                return flag + "天";
            }
        }
        if (hour != 0) {
            return hour + "小时" + flag;
        }
        if (min != 0) {
            return min + "分钟" + flag;
        }
        return "刚刚";
    }

    /**
     * 判断今天，昨天，前天
     */
    public static String getDistanceTime(String s) {
        //2018-03-11 14:59
        Date time3 = parseDateNoSS(s);
        long timeStamp = time3.getTime();

        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if (timeStamp >= todayStartMillis) {
            if ((timeStamp < todayStartMillis + oneDayMillis)) {
                return "今天 " + getDateTohhmm(timeStamp);
            } else if (timeStamp < todayStartMillis + (oneDayMillis * 2)) {
                return "明天 " + getDateTohhmm(timeStamp);
            }
        }

        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (timeStamp >= yesterdayStartMilis && timeStamp < todayStartMillis) {
            return "昨天  " + getDateTohhmm(timeStamp);
        }
//        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
//        if(timeStamp >= yesterdayBeforeStartMilis) {
//            return "前天";
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(timeStamp));
    }

    /**
     * 把2017-2-5 12:32:12 --> 2017-2-5 12:32
     */
    public static String change(String time) {
        Date d = parseDateNoSS(time);
        long l = d.getTime();
        return getDateTommddhhmm(l);
    }

    /**
     * //2019-05-09 08:30:00
     */
    public static String changeNo(String time) {
        Date d = parseDate(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /**
     * 判断2个日期的前后
     */
    public static boolean beforOrAfter(Date date1, Date date2) {
        if (date1.getTime() <= date2.getTime()) {
            return true;
        }
        return false;
    }


    //获取昨天的日期
    public static String getDateOfYesterday() {
        Calendar c = Calendar.getInstance();
        long t = c.getTimeInMillis();
        long l = t - 24 * 3600 * 1000;
        Date d = new Date(l);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sf.format(d);
        return s;
    }


    public static Map<String, String> judgingTime(String s) {
        //2018-03-11 14:59
        Date time3 = parseDateNoSS(s);
        long timeStamp = time3.getTime();
        Map<String, String> map = new HashMap<String, String>();

        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
//        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (timeStamp >= todayStartMillis) {
            if ((timeStamp < todayStartMillis + oneDayMillis)) {
                map.put("st", "请于");
                map.put("time", "今日 " + getDateTohhmm(timeStamp));
            } else {
                map.put("st", "预计");
                map.put("time", getDateTommddhhmm(timeStamp));
            }
        } else {
            map.put("st", "");
            map.put("time", getDateTommddhhmm(timeStamp));
        }
        return map;
    }
}
