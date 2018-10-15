package com.example.bingchenghu.stellarmaterial;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sansi.stellar.object.Light;
import com.sansi.stellarWiFi.api.ApiHelper;
import com.sansi.stellarWiFi.api.Call;
import com.sansi.stellarWiFi.api.Callback;
import com.sansi.stellarWiFi.api.LocalApiService;
import com.sansi.stellarWiFi.util.L;
import com.sansi.stellarWiFi.util.T;
import com.sansi.stellarWiFi.util.WifiUtils;

import java.util.ArrayList;
import java.util.List;

import udp.EventData;
import udp.core.Connection;
import udp.core.ConnectionListener;
import udp.core.Packet;
import udp.packet.RgbEx;


public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {
    //    Button button;
    SeekBar seekBar;
    boolean disp = false;
    SeekBar red_seekbar, blue_seekbar, green_seekbar, color_seekbar, white_seekbar;
    Button sendBtn, wifiBtn ,selectColor;
    private EditText ssidText, pswText;
    private int briProgress, cctProgress, rProgress, gProgress, bProgress, wProgress;
    private LocalApiService localApiService;
    private List<Light> lights = new ArrayList<>();
    private Light light;
    private RgbEx rgbEx = new RgbEx();
    private int r, g, b, w;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox remenberPsw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);
//        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBar);
        red_seekbar = findViewById(R.id.red_seekbar);
        blue_seekbar = findViewById(R.id.blue_seekbar);
        green_seekbar = findViewById(R.id.green_seekbar);
        color_seekbar = findViewById(R.id.colorseekBar);
        white_seekbar = findViewById(R.id.white_seekbar);
        sendBtn = (Button) findViewById(R.id.sendBtn);
        //sendBtn2 = (Button) findViewById(R.id.sendBtn2);
        //sendBtn.setOnClickListener((View.OnClickListener) this);
        wifiBtn = (Button) findViewById(R.id.wifiBtn);
        //wifiBtn.setOnClickListener(this);
        selectColor = (Button) findViewById(R.id.selectColor);
        ssidText = (EditText) findViewById(R.id.ssidText);
        pswText = (EditText) findViewById(R.id.pswText);
        localApiService = ApiHelper.getInstace().localApiService();

        sendBtn.setOnClickListener(this);
        selectColor.setOnClickListener(this);
        wifiBtn.setOnClickListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        remenberPsw = (CheckBox) findViewById(R.id.remenber_psw);
        boolean isRemenber = sharedPreferences.getBoolean("remenbered", false);
        if (isRemenber){
            String account = sharedPreferences.getString("account", "");
            String password = sharedPreferences.getString("password", "");
            remenberPsw.setChecked(true);
            ssidText.setText(account);
            pswText.setText(password);
        }

        if(getIntent().getExtras().getChar("dir")!='a'){
            light = (Light) getIntent().getExtras().getSerializable("light");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.e("onClick.....");
//
//                Call<List<Light>> scanDevices = localApiService.scanDevices(WifiUtils.getBroadcastAddress(getApplicationContext()).getAddress(),
//                        2000);
//                scanDevices.enqueue(new Callback<List<Light>>() {
//                    @Override
//                    public void onResponse(List<Light> ls) {
//                        L.e("lights:"+ls);
//                        Toast.makeText(DeviceActivity.this, "搜索到设备"+ls+"个",
//                                Toast.LENGTH_SHORT).show();
//                        T.showShort(getApplicationContext(),"size:"+ls.size());
//                        if(ls!=null) {
//                           lights.clear();
//                            lights.addAll(ls);
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        if (t != null) t.printStackTrace();
//                        L.e ("t:"+t);
//                    }
//                });
//            }
//        });

//        sendBtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                L.i("onClick: "+"000000");
//                sendBroadcast();
//            }
//        });
//
//        wifiBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String ssid=ssidText.getText().toString();
//                String psw=pswText.getText().toString();
//                if(TextUtils.isEmpty(ssid) || TextUtils.isEmpty(psw)){
//                    Toast.makeText(DeviceActivity.this,"ssid或password不能为空",Toast.LENGTH_SHORT).show();
//                }else {
//                    List<Light> lights = DeviceActivity.this.lights;
//                    L.e("添加wifi设备个数:"+lights.size());
//                    for(Light light:lights) {
//                        localApiService.setSsid(light.getMac(),light.getIpAddr(),ssid,psw).enqueue(new Callback<Integer>() {
//                            @Override
//                            public void onResponse(Integer code) {
//                                L.e("添加wifi结果码:"+code);
//                                Toast.makeText(DeviceActivity.this,"稍候请手动将WiFi切换到"+ssid,Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t) {
//                                t.printStackTrace();
//                            }
//                        });
//                    }
//                }
//            }
//        });




//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                for(int i=0;i<lights.size();i++) {
////                    Light light=lights.get(i);
//                    localApiService.setBrightness(light.getMac(),light.getIpAddr(),progress)
//                            .enqueue(new Callback<Integer>() {
//                        @Override
//                        public void onResponse(Integer code) {
//                            L.e("retCode:"+code);
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//
//                        }
//                    });
////                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                briProgress = progress;
                List<Light> lights = DeviceActivity.this.lights;
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

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//        red_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                for(Light light:lights){
//                    RgbEx rgbEx=new RgbEx();
//                    rgbEx.time= (short) 0xFFFF;
//                    rgbEx.mode=0x01;
//                    int[] content=new int[1];
//                    int r= (int) (progress/100.0 * 255);
//                    int g = 0;
//                    int b=0;
//                    int w=0;
//                    content[0]=((r&0xFF)<<24)|((g&0xFF)<<16)|((b&0xFF)<<8)|(w&0xFF);
//                    rgbEx.content=content;
//                    localApiService.setRgbw(light.getMac(),light.getIpAddr(),rgbEx).enqueue(new Callback<Integer>() {
//                        @Override
//                        public void onResponse(Integer integer) {
//                            L.e("rgb retCode:"+integer);
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//
//                        }
//                    });
////                }
//            }
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        red_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rProgress = progress;
                r = (int) (rProgress / 100.0 * 255);
                setRgb();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blue_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bProgress = progress;
                b = (int) (bProgress / 100.0 * 255);
                setRgb();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        green_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gProgress=progress;
                g= (int) (gProgress/100.0*255);
                setRgb();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        color_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cctProgress=progress;
                List<Light> lights = DeviceActivity.this.lights;
                int cctVal= (int) (2700+cctProgress/100.0*(6500-2700));
                for(Light light:lights) {
                    localApiService.setCct(light.getMac(),light.getIpAddr(),cctVal,0).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Integer retCode) {
                            L.e("cct调光成功.");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

                L.e("调灯个数:"+lights.size());
                L.e("cctVal="+cctVal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        white_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wProgress=progress;
                w= (int) (wProgress/100.0*255);
                setRgb();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
            Toast.makeText(DeviceActivity.this, "V 1.0.0",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRgb() {
        rgbEx.time = (short) 0xFFFF;
        rgbEx.mode = 0x01;
        int[] content = new int[1];
        content[0] = ((r & 0xFF) << 24) | ((g & 0xFF) << 16) | ((b & 0xFF) << 8) | (w & 0xFF);
        rgbEx.content = content;
        List<Light> lights = DeviceActivity.this.lights;
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

    public void sendBroadcast() {
        localApiService.scanDevices(WifiUtils.getBroadcastAddress(this).getAddress(), 2000).enqueue(new Callback<List<Light>>() {
            @Override
            public void onResponse(List<Light> lights) {
                DeviceActivity.this.lights.clear();
                if (!lights.isEmpty()) {
                    DeviceActivity.this.lights.addAll(lights);
                }
                L.e("搜索到设备个数：" + lights.size());
                Toast.makeText(DeviceActivity.this, "搜索到设备" + lights.size() + "个",
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
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                disp = true;
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                L.e("onClick.....sendBtn");

                Call<List<Light>> scanDevices = localApiService.scanDevices(WifiUtils.getBroadcastAddress(getApplicationContext()).getAddress(),
                        2000);
                scanDevices.enqueue(new Callback<List<Light>>() {
                    @Override
                    public void onResponse(List<Light> ls) {
                        L.e("lights:" + ls);
                        Toast.makeText(DeviceActivity.this, "搜索到设备" + ls.size() + "个",
                                Toast.LENGTH_SHORT).show();
                        //T.showShort(getApplicationContext(), "size:" + ls.size());
                        if (ls != null) {
                            lights.clear();
                            lights.addAll(ls);
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t != null) t.printStackTrace();
                        L.e("t:" + t);
                    }
                });
                break;
//            case R.id.sendBtn2:
//                L.i("onClick: " + "sendBtn2");
//                sendBroadcast();
//                break;
            case R.id.wifiBtn:
                final String ssid = ssidText.getText().toString();
                String psw = pswText.getText().toString();
                editor = sharedPreferences.edit();
                if(remenberPsw.isChecked()){
                    editor.putBoolean("remenbered", true);
                    editor.putString("account", ssid);
                    editor.putString("password", psw);
                } else {
                    editor.clear();
                }
                editor.apply();
                if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(psw)) {
                    Toast.makeText(this, "ssid或password不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    List<Light> lights = DeviceActivity.this.lights;
                    L.e("添加wifi设备个数:" + lights.size());
                    for (Light light : lights) {
                        localApiService.setSsid(light.getMac(), light.getIpAddr(), ssid, psw).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Integer code) {
                                L.e("添加wifi结果码:" + code);
                                Toast.makeText(DeviceActivity.this, "稍候请手动将WiFi切换到" + ssid, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
                break;
            case R.id.selectColor:
                int[] content = new int[1];
                content[0] = ((w & 0xFF)<< 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
                selectColor.setText(Integer.toHexString(content[0]).toUpperCase());
                selectColor.setBackgroundColor(content[0]);
                break;
            default:
                break;
        }
    }
}
