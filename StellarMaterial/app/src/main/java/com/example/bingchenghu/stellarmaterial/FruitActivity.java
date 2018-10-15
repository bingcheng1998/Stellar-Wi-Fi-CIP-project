package com.example.bingchenghu.stellarmaterial;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FruitActivity extends AppCompatActivity implements View.OnClickListener
{

    public static final String FRUIT_NAME = "fruit_name";

    public static final String FRUIT_IMAGE_ID = "fruit_image_id";

    private ImageView bulbPicUp,bulbPicMiddle0,bulbPicMiddle1,bulbPicDown;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        bulbPicMiddle0 = (ImageView) findViewById(R.id.bulbPicMiddle0);
        bulbPicMiddle1 = (ImageView) findViewById(R.id.bulbPicMiddle1);
        bulbPicUp = (ImageView) findViewById(R.id.bulbPicUp);
        bulbPicDown = (ImageView) findViewById(R.id.bulbPicDown);

        bulbPicDown.setOnClickListener(this);
        bulbPicMiddle1.setOnClickListener(this);
        bulbPicMiddle0.setOnClickListener(this);
        bulbPicUp.setOnClickListener(this);
        //获取跳转过来的数据
        Intent intent = getIntent();
        String fruitName = intent.getStringExtra(FRUIT_NAME);
        int fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageView ivButton = (ImageView) findViewById(R.id.bulbPicUp);
        Drawable src = ivButton.getDrawable();
        ivButton.setImageDrawable(tintDrawable(src,ColorStateList.valueOf(0xFFFF0000)));


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
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

//    //500个水果名去
//    private String generateFruitContent(String fruitName)
//    {
//        StringBuilder fruitContent = new StringBuilder();
//        for (int i = 0; i < 500; i++)
//        {
//            fruitContent.append(fruitName);
//        }
//        return fruitContent.toString();
//    }
//
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
            Toast.makeText(FruitActivity.this, "V 1.0.0",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bulbPicDown:
                Intent intentDown = new Intent(FruitActivity.this, ColorDiskActivity.class);
                startActivity(intentDown);
                break;
            case R.id.bulbPicUp:
                Intent intentUp = new Intent(FruitActivity.this, ColorDiskActivity.class);
                startActivity(intentUp);
                break;
            default:
                break;
        }
    }
}
