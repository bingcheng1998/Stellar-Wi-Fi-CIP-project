package com.example.bingchenghu.stellarmaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class PatternActivity extends AppCompatActivity
{

    public static final String FRUIT_NAME = "fruit_name";

    public static final String FRUIT_IMAGE_ID = "fruit_image_id";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        //获取跳转过来的数据
        Intent intent = getIntent();
        String fruitName = intent.getStringExtra(FRUIT_NAME);
        int fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        TextView fruitContentText = (TextView) findViewById(R.id.fruit_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(fruitName);
        Glide.with(this).load(fruitImageId).into(fruitImageView);
//        String fruitContent = generateFruitContent(fruitName);
//        fruitContentText.setText(fruitContent);

    }

    //500个水果名去
//    private String generateFruitContent(String fruitName)
//    {
//        StringBuilder fruitContent = new StringBuilder();
//        for (int i = 0; i < 500; i++)
//        {
//            fruitContent.append(fruitName);
//        }
//        return fruitContent.toString();
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }



}
