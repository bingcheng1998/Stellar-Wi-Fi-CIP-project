package com.example.bingchenghu.stellarmaterial;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.sansi.stellar.object.Light;
import com.sansi.stellarWiFi.api.ApiHelper;
import com.sansi.stellarWiFi.api.Call;
import com.sansi.stellarWiFi.api.Callback;
import com.sansi.stellarWiFi.api.LocalApiService;
import com.sansi.stellarWiFi.util.L;
import com.sansi.stellarWiFi.util.WifiUtils;

import java.util.ArrayList;
import java.util.List;

import udp.packet.RgbEx;

public class ColorDiskActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener {
    private SVBar svBar;
    private OpacityBar opacityBar;
    private Button button;
    private ImageView broadcast;
    private TextView text, rgbText;
    boolean disp = false;
    private ColorPicker picker;
    private TextView bri_text;
    private int briProgress, bright;
    private List<Light> lights = new ArrayList<>();
    private Light light;
    private LocalApiService localApiService;
    private RgbEx rgbEx = new RgbEx();
    private int r, g, b, w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_disk);

        picker = (ColorPicker) findViewById(R.id.picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.textView1);
        rgbText = (TextView) findViewById(R.id.textView2);
        localApiService = ApiHelper.getInstace().localApiService();
        broadcast = (ImageView) findViewById(R.id.broadcast);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = picker.getColor();
                String str = Integer.toHexString(i);
                String str1 = "FF";
                String str2 = "FF";
                String str3 = "FF";
                String str4 = "FF";
                if((str.length() ==8)){
                    str1 = str.substring(0,2);
                    str2 = str.substring(2,4);
                    str3 = str.substring(4,6);
                    str4 = str.substring(6,8);
                } else {
                    str1 = Integer.toHexString(bright);
                    str2 = Integer.toHexString(r);
                    str3 = Integer.toHexString(g);
                    str4 = Integer.toHexString(b);
                }
                int red = Integer.parseInt(str2,16);
                int green = Integer.parseInt(str3,16);
                int blue = Integer.parseInt(str4,16);
                int white = Integer.parseInt(str1, 16);
                r = red;
                g = green;
                b = blue;
                w = 0;
                bright = white;
                text.setTextColor(picker.getColor());
                text.setText(Integer.toHexString(i).toUpperCase());
                //b
                rgbText.setText("R" + red +" G" + green + " B"+ blue +" W"+white);
                rgbText.setTextColor(Color.rgb(red, green, blue));
                picker.setOldCenterColor(picker.getColor());
                setRgb();
            }
        });

        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.e("onClick.....sendBtn");
                sendBroadcast();
                L.e("sendBtn success");
            }
        });

        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {

                briProgress = (opacity*100/255)^2/10000;
                L.d("CCC__briProgress "+ briProgress);
                List<Light> lights = ColorDiskActivity.this.lights;
                L.e("调灯个数:" + lights.size());
                for (Light light : lights) {
                    localApiService.setBrightness(light.getMac(), light.getIpAddr(), briProgress).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Integer restCode) {
                            L.e("调光成功.");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        sendBroadcast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.version_introduction) {
            Toast.makeText(ColorDiskActivity.this, "V 1.0.0",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onColorChanged(int color) {
        int i = picker.getColor();
        String str = Integer.toHexString(i);
        String str1 = "FF";
        String str2 = "FF";
        String str3 = "FF";
        String str4 = "FF";
        if((str.length() ==8)){
            str1 = str.substring(0,2);
            str2 = str.substring(2,4);
            str3 = str.substring(4,6);
            str4 = str.substring(6,8);
        } else if(i==0){
            str1 = "00";
            str2 = Integer.toHexString(r);
            str3 = Integer.toHexString(g);
            str4 = Integer.toHexString(b);
        }
        int red = Integer.parseInt(str2,16);
        int green = Integer.parseInt(str3,16);
        int blue = Integer.parseInt(str4,16);
        int white = Integer.parseInt(str1, 16);
        r = red;
        g = green;
        b = blue;
        w = 0;
        bright = white;
        setRgb();
    }

    public void sendBroadcast() {
        localApiService.scanDevices(WifiUtils.getBroadcastAddress(this).getAddress(), 2000).enqueue(new Callback<List<Light>>() {
            @Override
            public void onResponse(List<Light> lights) {
                ColorDiskActivity.this.lights.clear();
                if (!lights.isEmpty()) {
                    ColorDiskActivity.this.lights.addAll(lights);
                }
                L.e("搜索到设备个数：" + lights.size());
                Toast.makeText(ColorDiskActivity.this, "搜索到设备" + lights.size() + "个",
                        Toast.LENGTH_SHORT).show();
                for (Light light : lights) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("type:" + light.getType()).append(" ");
                    sb.append("group:" + light.getGroup()).append(" ");
                    sb.append("ipAdress:" + light.getIpAddr()).append(" ");
                    sb.append("mac:" + light.getMac()).append(" ");
                    sb.append("name:" + light.getName()).append(" ");
                    sb.append("hardVer:" + light.getHdVer()).append(" ");
                    sb.append("softVer:" + light.getSoftVer()).append(" ");
                    L.d("==>" + sb.toString());
                }
                disp = true;
                broadcast.setImageResource(R.drawable.ic_bulb_on);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                disp = true;
                broadcast.setImageResource(R.drawable.ic_bulb_off);
            }
        });
    }

    private void setRgb() {
        rgbEx.time = (short) 0xFFFF;
        rgbEx.mode = 0x01;
        int[] content = new int[1];
        content[0] = ((r & 0xFF) << 24) | ((g & 0xFF) << 16) | ((b & 0xFF) << 8) | (w & 0xFF);
        rgbEx.content = content;
        List<Light> lights = ColorDiskActivity.this.lights;
        L.e("调RGb灯个数:" + lights.size());
        for (Light light : lights) {
            localApiService.asyncSetRgbw(light.getMac(), light.getIpAddr(), 0x01, 0xFFFF, content[0]).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Void aVoid) {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

}