package com.dominator.utils.dateutil;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class DateUtil {

    /**
     * 获取两个月之间所有月份的集合
     *
     */
    synchronized public static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<>();


        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        try {
            min.setTime(StringToDateFormat(minDate, "yyyy-MM"));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(StringToDateFormat(maxDate, "yyyy-MM"));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("DateUtil  have some error" + e);
        }

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(DatetoStringFormat(curr.getTime(), "yyyy-MM"));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    /**
     * 日期类型转换为string类型
     *
     * @return
     */
    synchronized public static String DatetoString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String str = sdf.format(date);
        return str;
    }

    /**
     * 根据给定的日期格式转换日期
     *
     * @param date
     * @param format
     * @return
     */

    synchronized public static String DatetoStringFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 根据给定的格式字符串转换成date
     *
     * @param str
     * @return
     */
    synchronized public static Date StringToDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据给定的格式字符串转换成date
     *
     * @param str
     * @return
     */
    synchronized public static Date StringToDateFormat(String str, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(str);
        return date;
    }

    /**
     * 获取传入日期的前n个月
     *
     * @param date
     * @param n
     * @return
     */
    synchronized public static Date minusMonth(Date date, int n) {
        log.info("n:{}", n);
        log.info("date:{}", DatetoStringFormat(date, "yyyy-MM-dd"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - n);
        Date date1 = calendar.getTime();
        log.info("date1:{}", DatetoStringFormat(date1, "yyyy-MM-dd"));
        return date1;
    }


    /**
     * 时间戳准换为String类型
     *
     * @param timeStamp
     * @param format
     * @return
     */
    synchronized public static String timeStampToString(long timeStamp,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String str = sdf.format(new Date(timeStamp));
        log.info("timeStampToString "+str);
        return str;
    }

//    public static void main(String[] args) {
//        Date date1 = new Date();
//        String str = DatetoStringFormat(date1, "yyyy-MM-dd HH:mm");
//        log.info("str:{}", str);
////        String str = "2017-11-24 16:00:31";
//
//    }
    public static String getWeekOfDate(Date date)
        {
            String[] weekOfDays={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
            Calendar calendar=Calendar.getInstance();
            if(date!=null)
            {
                calendar.setTime(date);
            }
            int w=calendar.get(Calendar.DAY_OF_WEEK)-1;
            if(w<0)
            {
                w=0;
            }
            return weekOfDays[w];
        }

    synchronized public static Date getStartTime(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    synchronized public static Date getEndTime(Long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * localDateTime 转 自定义格式string
     *
     * @param localDateTime
     * @param format        例：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String formatLocalDateTimeToString(LocalDateTime localDateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);

        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * string 转 LocalDateTime
     *
     * @param dateStr 例："2017-08-11 01:00:00"
     * @param format  例："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 特定日期的当月第一天
     *
     * @param date
     * @return
     */
    public static LocalDate newThisMonth(Date date) {
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }

    /**
     * 特定日期的当月最后一天
     *
     * @param date
     * @return
     */
    public static LocalDate lastThisMonth(Date date) {
        int lastDay = getActualMaximum(date);
        LocalDate localDate = dateToLocalDate(date);
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), lastDay);
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDate dateToLocalDate(Date date) {

        return dateToLocalDateTime(date).toLocalDate();
    }

    public static LocalTime dateToLocalTime(Date date) {

        return dateToLocalDateTime(date).toLocalTime();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date localToDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static Long localToTimestamp(LocalDateTime localDateTime){
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 根据时间获取当月有多少天数
     *
     * @param date
     * @return
     */
    public static int getActualMaximum(Date date) {

        return dateToLocalDateTime(date).getMonth().length(dateToLocalDate(date).isLeapYear());
    }



}
