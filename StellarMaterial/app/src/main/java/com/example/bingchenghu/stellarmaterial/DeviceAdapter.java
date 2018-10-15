package com.example.bingchenghu.stellarmaterial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sansi.stellar.bean.LightInfo;
import com.sansi.stellar.object.DeviceFilter.LIGHT_TYPES;

import com.sansi.stellar.object.Light;

import java.util.List;

/**
 * Created by bingchenghu on 18/3/21.
 */

public class DeviceAdapter extends BaseAdapter {
    private List<Light> lights;
    private Context context;
    public DeviceAdapter(List<Light> datas, Context context){
        this.lights=datas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return lights.size();
    }

    @Override
    public Light getItem(int position) {
        return lights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView contentView = (TextView) LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        contentView.setText(getItem(position).getIpAddr());
        return contentView;
    }
}
