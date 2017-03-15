package com.gn.cb.coolweather.gson;

/**
 * Created by CBJ on 2017/3/12.
 */

import com.google.gson.annotations.SerializedName;

/**
 * basic的格式
 * "basic":{
        "city":"苏州",
        "id":"CN101190401",
        "update":{
            "loc":"2016-08-08 21:58"
            }
        }


 */

public class Basic {

    /**
     * 解释：这里为什么要使用注解
     * 因为JSON字符串中的字段的名字有的并不能很友好的表达出来意思，所以
     * 采用注解的方式进行诠释，如果JSON字符串中的名字能够很好的诠释其意，
     * 则无用使用注解。
     * 凡是出现{}的都是一个类
     * 这里面注解的名字一定要和JSON字符串中的名字保持一致
     */
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
