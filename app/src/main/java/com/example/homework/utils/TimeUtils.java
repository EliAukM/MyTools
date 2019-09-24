package com.example.homework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    private static Date parse;


    //静态方法获取系统时间 并转为时间戳方法
    public static long getNowTimer() {
        SimpleDateFormat alldate = new SimpleDateFormat("yyyy-MM-dd HH:mm");//获取日期时间
        //直接获取时间
        Date curDate = new Date(System.currentTimeMillis());
        //转为字符串
        String format = alldate.format(curDate);

        try {
            parse = alldate.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //以秒为单位
        return parse.getTime() / 1000;
//        Log.e("TAG", "getTimers: "+format );

    }

    //参数为相隔秒数     最后转为“x天--x时--xx分--xx秒”字符串格式
    public static String getRemainTime(long diff) {
        String timers;
        //（60*60*24） == 一天的秒数     (60 * 60) == 一小时的秒数   60 == 一秒


        //相隔秒数  /  （秒*分*时） = 天
        long days = diff / (60 * 60 * 24);    //获取天数  s


        //    （总秒数 -  天 的秒数* （秒*分*时））  / 秒* 分  = 小时
        long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);  //获取小时


        // （总秒数 -  天 的秒数 - 时的秒数 ）/ 秒 = 分钟
        long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / (60); //获取分钟


        //总秒数  - 天的秒数 - 时的秒数 - 分钟的秒数  = 秒
        long ss = ((diff - days * (60 * 60 * 24) - hours * (60 * 60)) - minutes * (60)); //获取秒


        if (days > 5) {
            timers = "很久以前";
        } else {
            timers = days + "天" + hours + "时" + minutes + "分" + ss + "秒";
        }

        return timers;
    }
}
