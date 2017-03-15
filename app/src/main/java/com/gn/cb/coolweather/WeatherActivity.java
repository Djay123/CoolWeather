package com.gn.cb.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gn.cb.coolweather.gson.Forecast;
import com.gn.cb.coolweather.gson.Weather;
import com.gn.cb.coolweather.service.AutoUpdateService;
import com.gn.cb.coolweather.util.HttpUnit;
import com.gn.cb.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by CBJ on 2017/3/12.
 */

public class WeatherActivity extends AppCompatActivity {
    //显示天气的总布局
    private ScrollView weatherLayout;
    //显示当前的城市
    private TextView titleCity;
    //显示跟新时间
    private TextView titleUpdateTiem;
    //显示当前温度
    private TextView nowDegree;
    //天气状况
    private TextView nowWeatherInfo;
    //显示预报的布局
    private LinearLayout forecastLayout;
    //显示aqi
    private TextView aqiText;
    //显示pm25
    private TextView pm25Text;
    //舒适度
    private TextView comfortText;
    //洗车指数
    private TextView carWashText;
    //运动指数
    private TextView sportText;
    //显示bing图片布局
    private ImageView backgroundImg;

    //下拉刷新天气
    public SwipeRefreshLayout swipeRefresh;
    public final static String STOREKEY = "weather";
    public final static String STOREBINGPIC = "bing_pic";
    public final static String URL_BINGPIC = "http://guolin.tech/api/bing_pic";
    public DrawerLayout drawerLayout;
    private Button navButton;
    private ImageView nav_Background;
    private Fragment nav_choose_Area;
    private LinearLayout nav_LinearLayou;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        final String weatherId;
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            //得到当前活动的DecorView
            View decorView = getWindow().getDecorView();
            //改变系统UI的显示，这里两个参数的作用是活动的布局会显示在状态栏上
            decorView.setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            );
            //将状态栏的背景设置成透明的
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initView();
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        String weatherString = prefs.getString(STOREKEY,null);
        String bingPic = prefs.getString(STOREBINGPIC,null);
        if(null != bingPic){
            Glide.with(this).load(bingPic).into(backgroundImg);

        }else{
            loadBingPic();
        }
        if(weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);

        }else{
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);

            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });


    }
    public void initView(){
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTiem = (TextView)findViewById(R.id.title_update_time);
        nowDegree = (TextView)findViewById(R.id.now_degree_text);
        nowWeatherInfo = (TextView)findViewById(R.id.now_weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.suggestion_comfort_text);
        carWashText = (TextView)findViewById(R.id.suggestion_wash_text);
        sportText = (TextView)findViewById(R.id.suggestion_sport_text);

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        backgroundImg = (ImageView)findViewById(R.id.background_bing_pic);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navButton = (Button)findViewById(R.id.title_nav_button);
        nav_Background = (ImageView)findViewById(R.id.nav_backgroud_imageview);
        nav_LinearLayou = (LinearLayout)findViewById(R.id.choose_linearlayout);


    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        String weathrUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=6778a2408fd64a14b8fa1b6e0e4b5c0b";

        HttpUnit.sendOkHttpRequest(weathrUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).
                                    edit();
                            editor.putString(STOREKEY,responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }
    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateTieme = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTiem.setText(updateTieme);
        nowDegree.setText(degree);
        nowWeatherInfo.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                    forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.forecast_item_date_text);
            TextView infoText = (TextView)view.findViewById(R.id.forecast_item_info_text);
            TextView maxText = (TextView)view.findViewById(R.id.forecast_item_max_text);
            TextView minText = (TextView)view.findViewById(R.id.forecast_item_min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);

        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);

    }

    /**
     * 加载bing的每日一图
     */
    private void loadBingPic(){
        HttpUnit.sendOkHttpRequest(URL_BINGPIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                byte[] arrayBingPic = bingPic.getBytes();

                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(STOREBINGPIC,bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic)
                                .into(backgroundImg);

                    }
                });
            }
        });
    }
}
