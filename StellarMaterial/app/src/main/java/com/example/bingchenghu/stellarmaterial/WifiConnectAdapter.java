package com.example.bingchenghu.stellarmaterial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sansi.stellar.object.Light;
import com.sansi.stellar.bean.LightInfo;
import com.sansi.stellar.object.DeviceFilter.LIGHT_TYPES;


import java.util.List;



/**
 * Created by bingchenghu on 2018/3/23.
 */

public class WifiConnectAdapter {
    private List<Light> lights;
    private Context context;
    public WifiConnectAdapter(List<Light> datas, Context context){
        this.lights=datas;
        this.context=context;
    }

}
