package com.gn.cb.coolweather;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {


    private Choose_AreaFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new Choose_AreaFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.choose_arerlayout,fragment);
        transaction.commit();

        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null) != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }



    }

    @Override
    public void onBackPressed() {
        if(fragment.currentLevel == Choose_AreaFragment.LEVEL_PROVINCE){
            super.onBackPressed();
        }else if(fragment.currentLevel == Choose_AreaFragment.LEVEL_CITY){
            fragment.queryProvinces();
        }else if(fragment.currentLevel == Choose_AreaFragment.LEVEL_COUNTY){
            fragment.queryCities();
        }
    }
}
