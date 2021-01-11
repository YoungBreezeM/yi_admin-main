package com.fw.core.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author yqf
 * @date 2020 下午7:45
 */
public class DateUtil {
    private Long timeStamp = System.currentTimeMillis();

    /**
     * 获取标准时间时间
     * */
    public Date createTime(){
        return  new Date(Long.parseLong(String.valueOf(timeStamp)));
    }

    /**
     * 获取自定义时间
     * */
    public String createTime(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
    }


}
