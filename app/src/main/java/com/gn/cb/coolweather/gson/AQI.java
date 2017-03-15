package com.gn.cb.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * "aqi":{
 *     "city":{
 *         "aqi":"44",
 *         "pm25":"13"
 *     }
 * }
 * Created by CBJ on 2017/3/12.
 */

public class AQI {
    public AQICity city;

    public class AQICity{

        public String aqi;
        public String pm25;
    }
}
