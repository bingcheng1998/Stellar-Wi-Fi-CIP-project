package com.example.bingchenghu.stellarmaterial;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

/**
 * Created by bingchenghu on 18/3/21.
 */

public class StellarActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    private List<Light> lights=new ArrayList<>();
    LocalApiService apiService;
    TextView tvRefresh, tvEnter;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e("Start Stellar");
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        apiService = ApiHelper.getInstace().localApiService();
        listView=findViewById(R.id.list);
        tvRefresh=findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(this);
        tvEnter = findViewById(R.id.tv_enter);
        tvEnter.setOnClickListener(this);
        //匿名内部类
        adapter = new DeviceAdapter(lights,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Light light = lights.get(position);
                Intent  intent=new Intent(StellarActivity.this,DeviceActivity.class);
                intent.putExtra("light",light);
                intent = intent.putExtra("dir", 'b');
                startActivity(intent);
            }
        });
        loadData();
    }

    private void loadData() {
        Call<List<Light>> scanDevices = apiService.scanDevices(WifiUtils.getBroadcastAddress(getApplicationContext()).getAddress(),
                1000);
        scanDevices.enqueue(new Callback<List<Light>>() {
            @Override
            public void onResponse(List<Light> ls) {
                L.e("lights:"+ls);
                T.showShort(getApplicationContext(),"size:"+ls.size());
                lights.clear();
                if(ls!=null) {

                    lights.addAll(ls);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) t.printStackTrace();
                L.e ("t:"+t);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tv_refresh:
                loadData();
                break;
            case R.id.tv_enter:
                Intent  intent=new Intent(StellarActivity.this,DeviceActivity.class);
                intent = intent.putExtra("dir", 'a');
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            Toast.makeText(StellarActivity.this, "V 1.0.0",
                    Toast.LENGTH_SHORT).show();
//            Intent intent_version_introduction = new Intent(IntroActivity.this, IntroActivity.class);
//            startActivity(intent_version_introduction);

            return true;
        }
        else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
