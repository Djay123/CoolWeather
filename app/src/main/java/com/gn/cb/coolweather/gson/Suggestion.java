package com.gn.cb.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * "suggestion":{
 *     "comf":{
 *         "txt":"白天天气较热，虽然有雨，...."
 *     }
 *     "cw":{
 *         "txt":"不宜洗车，未来24小时有雨，..."
 *     }
 *     "sport":{
 *         "txt":"有降水，且风力较强，..."
 *     }
 * }
 * Created by CBJ on 2017/3/12.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }
    public class CarWash{
        @SerializedName("txt")
        public String info;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }

}
