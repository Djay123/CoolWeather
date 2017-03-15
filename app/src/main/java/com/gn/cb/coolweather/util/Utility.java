package com.gn.cb.coolweather.util;

import android.text.TextUtils;

import com.gn.cb.coolweather.db.City;
import com.gn.cb.coolweather.db.County;
import com.gn.cb.coolweather.db.Province;
import com.gn.cb.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析从服务器返回的数据

 * Created by CBJ on 2017/3/10.
 */

public class Utility {
    public final static String NAME = "name";
    public final static String ID = "id";
    /**
     * http://guolin.tech/api/china
     * 解析和处理服务器返回的省级数据
     *  服务器返回的省级数据格式：
     * [{"id":1,"name":"北京"},
     *  {"id":2,"name":"上海"},
     *  {"id":3,"name":"天津"},
     *  ...
     *  ["id":16,"name":"江苏"]
     *  ...
     * ]
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(provinceObject.getInt(ID));
                    province.setProvinceName(provinceObject.getString(NAME));
                    //只有调用了save方法，该对象才会以数据库的形式保存到数据库中
                    province.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return false;
    }

    /**
     * http://guolin.tech/api/china/16
     * 解析和处理从服务器上返回的市级数据
     * 从服务器返回的市级数据格式
     * [
     *  {"id":113,"name":"南京"},
     *  {"id":114,"name":"无锡"},
     *  ...
     *  {"id":116,"name":"苏州"},
     *  ...
     * ]
     *
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCitys = new JSONArray(response);
                for (int i = 0; i < allCitys.length(); i++) {
                    JSONObject cityObject = allCitys.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString(NAME));
                    city.setCityCode(cityObject.getInt(ID));
                    city.setProvinceId(provinceId);
                    city.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * http://guolin.tech/api/china/16/116
     * 解析和处理从服务器上返回的县级数据
     * 从服务器得到县级数据格式
     * [
     *  {"id":937,"name":"苏州","weather_id":"CN101190401"},
     *  {"id":938,"name":"常熟","weather_id":"CN101190402"},
     *  ...
     *
     * ]
     * 得到县的weather_id之后就可以访问和风天气的后台了：
     * http://guolin.tech/api/weather?cityid=weather_id&key=apiKey
     *
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCountys = new JSONArray(response);
                for (int i = 0; i < allCountys.length(); i++) {
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString(NAME));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();

                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSON数据解析成Weather实体类
     * 返回的数据首先是一个实体类，然后是以"HeWeather"为键值的一个数组
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            //得到Weather类
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
