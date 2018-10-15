package com.sansi.stellarWiFi.util;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/14 14:59
 *          类说明
 */
public class TimeUtils {
    final static long SECOND = 1;
    final static long MINUTE = 60 * SECOND;
    final static long HOUR = 60 * MINUTE;
    final static long DATE = 24 * HOUR;
    final static long MONTH = 30 * DATE;
    final static  long YEAR = 12 * MONTH;

    public static String friendlyTime(final long delay) {
        final long year = delay / YEAR;
        final long month = (delay - year * YEAR) / MONTH;
        final long day = (delay - year * YEAR - month * MONTH) / DATE;
        final long hour = (delay - year * YEAR - month * MONTH - day * DATE) / HOUR;
        final long minute = (delay - year * YEAR - month * MONTH - day * DATE - hour * HOUR) / MINUTE;
        final long second = (delay - year * YEAR - month * MONTH - day * DATE - hour * HOUR - minute * MINUTE) / SECOND;
        StringBuilder sb = new StringBuilder();
        if (year > 0) {
            sb.append(year).append("年");
        }
        if (month > 0) {
            sb.append(month).append("月");
        }
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("时");
        }
        if (minute > 0) {
            sb.append(minute).append("分");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        return sb.toString();
    }
}
