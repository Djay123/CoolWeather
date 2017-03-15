package com.gn.cb.coolweather;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CBJ on 2017/3/12.
 */

public class TestFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.testfragment,null);
        return view;
    }
}
