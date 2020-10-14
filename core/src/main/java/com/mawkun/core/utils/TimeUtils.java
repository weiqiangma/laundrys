package com.mawkun.core.utils;

import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: ZhangSuchao
 * @Date: 2019/12/5 17:17
 */
public class TimeUtils {
    /**
     * 获取当天的开始时间
     * @return
     */
    public static Date getBeginDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取date的当天开始时间
     * @return
     */
    public static Date getBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当天的结束时间
     * @return
     */
    public static Date getEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取date的当天结束时间
     * @return
     */
    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取本周的开始时间
     * @return
     */
    public static Date getWeekStart() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        calendar.add(Calendar.DATE, -week);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本周的结束时间
     * @return
     */
    public static Date getWeekEnd() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, 8 - week);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取本月的开始时间
     * @return
     */
    public static Date getMonthStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取本月的结束时间
     * @return
     */
    public static Date getMonthEnd() {
        int year = DateUtils.getYear();
        int month = DateUtils.getMonth();
        return DateUtils.getLastTimeOfMonth(year,month);
    }

    /**
     * 本年开始时间
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTime();
    }

    /**
     * 本年结束时间
     * @return
     */
    public static Date getCurrentYearEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentYearStartTime());
        cal.add(Calendar.YEAR, 1);
        return cal.getTime();
    }

    /**
     * 向date中加addMinutesValue分钟
     * @param date
     * @param addMinutesValue 添加的分钟
     * @return
     */
    public static Date addMinutes(Date date, int addMinutesValue) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addMinutesValue);
        return calendar.getTime();
    }

    /**
     * 获取传入日期所在月的第一天
     * @param date
     * @return
     */
    public static Date getFirstDayDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取传入日期所在月的最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal.getTime();
    }

    /**
     * 获取 小时:分钟 23:12
     * @param inoutTime
     * @return
     */
    public static String getHourMinute(Date inoutTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inoutTime);
        String resultHour = "";
        String resultMinut = "";
        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
        Integer minute = calendar.get(Calendar.MINUTE);
        resultHour = (hour < 10) ? "0" + hour.toString() : hour.toString();
        resultMinut = (minute < 10) ? "0" + minute.toString() : minute.toString();
        return resultHour + ":" + resultMinut;
    }

    /**
     * 将分钟换算成小时
     * @param minuteCount
     * @return
     */
    public static String getHourMinuteByMinute(int minuteCount) {
        int hour = minuteCount / 60;
        int minute = minuteCount % 60;
        if (hour == 0) {
            return minute == 0 ? "" : minuteCount + "分钟";
        } else {
            return minute == 0 ? (hour + "小时") : (hour + "小时" + minute + "分钟");
        }
    }

    /**
     * 将描述换算成小时分钟
     * @param seconds
     * @return
     */
    public static String getHourMinuteBySeconds(int seconds) {
        int hour = seconds / 3600;
        int minute = (seconds % 3600) / 60;
        if(hour == 0) {
            return minute == 0 ? "" : minute + "分钟";
        } else {
            return minute == 0 ? (hour + "小时") : (hour + "小时" + minute + "分钟");
        }
    }

    /**
     * 计算时间戳和一个int的时间相差的秒数 返回 date-intTime
     * @param date 一个时间点
     * @param intTime 93000 表示早上9点30分0秒
     * @return
     */
    public static int calTimeDiffSecond(Date date,int intTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND,0);
        long dateLong = calendar.getTimeInMillis();
        int hour = intTime/10000;
        int minute = intTime/100%100;
        int second = intTime%100;
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        long diff = dateLong-calendar.getTimeInMillis();
        //System.out.println(String.format("%s:%s:%s",hour,minute,second));
        return (int)(diff/1000);
    }

    /**
     * 时间格式为 07:35
     * 计算两个时间相差小时数(工时)
     * @param inTime
     * @param outTime
     * @return
     */
    public static String getHoursByShorTime(String inTime, String outTime) {
        String result = "";
        if(inTime != null && outTime != null) {
            //进校
            String inSchoolHour = inTime.substring(0, inTime.indexOf(":"));
            String inSchoolMinute = inTime.substring(inSchoolHour.length() + 1, inTime.length());
            int inTotalMinutes = NumberUtils.str2Int(inSchoolHour) * 60 + NumberUtils.str2Int(inSchoolMinute);
            //出校
            String outSchoolHour = outTime.substring(0, outTime.indexOf(":"));
            String outSchoolMinute = outTime.substring(outSchoolHour.length() + 1, outTime.length());
            int outTotalMinutes = NumberUtils.str2Int(outSchoolHour) * 60 + NumberUtils.str2Int(outSchoolMinute);

            int minutesDiff = (outTotalMinutes - inTotalMinutes);

            if(minutesDiff > 0) {
                DecimalFormat df = new DecimalFormat("0.00");//格式化小数
                result = df.format((float) minutesDiff / 60);//返回的是String类型
            }
        } else {
            result = "进出校时间有误!";
        }
        return result;
    }


    public static String dateParseToTime(Integer time) {
        Integer hour = time / 10000;
        Integer minute = (time % 10000) /100;
        String mite = minute.toString();
        if(minute == 0) mite = "00";
        return  hour + ":" + mite;
    }

    public static Date convertWeiXinTime(String wxTime) throws ParseException {
        String year = wxTime.substring(0,4);
        String month = wxTime.substring(4, 6);
        String day = wxTime.substring(6, 8);
        String hour = wxTime.substring(8, 10);
        String minute = wxTime.substring(10, 12);
        String second = wxTime.substring(12, 14);
        String time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        Date resultTime = DateUtils.parse("yyyy-MM-dd HH:mm:ss", time);
        return resultTime;
    }

    public static String convertWeiXinTimeStr(String wxTime) throws ParseException {
        String year = wxTime.substring(0,4);
        String month = wxTime.substring(4, 6);
        String day = wxTime.substring(6, 8);
        String hour = wxTime.substring(8, 10);
        String minute = wxTime.substring(10, 12);
        String second = wxTime.substring(12, 14);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }

    public static void main(String[] args) throws ParseException {
        String sTime ="20200926125506";
        String year = sTime.substring(0,4);
        String month = sTime.substring(4, 6);
        String day = sTime.substring(6, 8);
        String hour = sTime.substring(8, 10);
        String minute = sTime.substring(10, 12);
        String second = sTime.substring(12, 14);
        StringBuilder builder = new StringBuilder();
        builder.append(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        String time = builder.toString();
        Date lTime = DateUtils.parse("yyyy-MM-dd HH:mm:ss", time);
        System.out.println(lTime);
    }

}
