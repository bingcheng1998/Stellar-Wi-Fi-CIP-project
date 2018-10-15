package com.example.bingchenghu.stellarmaterial;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class WifiConnectActivity extends AppCompatActivity implements View.OnClickListener{
    private LocalApiService localApiService;
    private List<Light> lights=new ArrayList<>();
    private Light light;
    boolean disp = false;
    Button sendBtn, wifiBtn;
    private EditText ssidText, pswText;
    private ImageView imageView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox remenberPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);
//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        imageView = (ImageView) findViewById(R.id.image_view);

        sendBtn = (Button) findViewById(R.id.sendBtn);
        wifiBtn = (Button) findViewById(R.id.wifiBtn);
        ssidText = (EditText) findViewById(R.id.ssidText);
        pswText = (EditText) findViewById(R.id.pswText);
        localApiService = ApiHelper.getInstace().localApiService();

        sendBtn.setOnClickListener(this);
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

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


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
            Toast.makeText(WifiConnectActivity.this, "V 1.0.0",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendBroadcast() {
        localApiService.scanDevices(WifiUtils.getBroadcastAddress(this).getAddress(), 2000).enqueue(new Callback<List<Light>>() {
            @Override
            public void onResponse(List<Light> lights) {
                WifiConnectActivity.this.lights.clear();
                if (!lights.isEmpty()) {
                    WifiConnectActivity.this.lights.addAll(lights);
                }
                L.e("搜索到设备个数：" + lights.size());
//                Toast.makeText(WifiConnectActivity.this, "搜索到设备" + lights.size() + "个",
//                        Toast.LENGTH_SHORT).show();
                if(lights.size()==1){
                    imageView.setImageResource(R.drawable.ic_bulb_on);
                    Toast.makeText(WifiConnectActivity.this, "小灯泡已经在线了哦",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    imageView.setImageResource(R.drawable.ic_bulb_off);
                    Toast.makeText(WifiConnectActivity.this, "小灯泡没有被连接哦",
                            Toast.LENGTH_SHORT).show();
                }

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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBtn:
                L.e("onClick.....sendBtn");
                sendBroadcast();
                break;
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
                    Toast.makeText(this, "ssid 或 password 不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    List<Light> lights = WifiConnectActivity.this.lights;
                    L.e("添加wifi设备个数:" + lights.size());
                    for (Light light : lights) {
                        localApiService.setSsid(light.getMac(), light.getIpAddr(), ssid, psw).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Integer code) {
                                L.e("添加wifi结果码:" + code);
                                Toast.makeText(WifiConnectActivity.this, "稍后请手动将WiFi切换到 " + ssid, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }
}
