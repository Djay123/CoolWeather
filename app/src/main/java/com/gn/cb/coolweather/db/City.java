package com.gn.cb.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by CBJ on 2017/3/10.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;//记录该市代号
    private int provinceId;//记录该市所属省的编号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
