package com.example.bingchenghu.stellarmaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private Fruit[] fruits = {
//            new Fruit("Apple",R.drawable.apple),
//            new Fruit("Banana",R.drawable.banana),
//            new Fruit("Orange",R.drawable.orange),
//            new Fruit("Watermelon",R.drawable.watermelon),
//            new Fruit("Pear",R.drawable.pear),
//            new Fruit("Grape",R.drawable.grape),
//            new Fruit("Pineapple",R.drawable.pineapple),
//            new Fruit("Strawberry",R.drawable.strawberry),
//            new Fruit("Cherry",R.drawable.cherry),
//            new Fruit("Mango",R.drawable.mango)};

private Fruit[] fruits = {
        new Fruit("bag",R.drawable.bag),
        new Fruit("clothes",R.drawable.clothes),
        new Fruit("jewelry",R.drawable.jewelry1),
        new Fruit("jewelry",R.drawable.jewelry2),
        new Fruit("knife",R.drawable.knife),
        new Fruit("man",R.drawable.people_man),
        new Fruit("womwn",R.drawable.people_womwn),
        new Fruit("shoes",R.drawable.shoes1),
        new Fruit("face",R.drawable.taylor_swift),
        new Fruit("toy",R.drawable.toy4)};

    private List<Fruit> fruitList = new ArrayList<>();

    private List<Bulb> bulbList = new ArrayList<>();

    private FruitAdapter adapter;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Toast.makeText(MainActivity.this, "Start next activity",
                        Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, StellarActivity.class);
                startActivity(intent1);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        initFruits();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(this,fruitList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshFruits();
            }
        });



    }


    private void refreshFruits()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(400);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        initFruits();;
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


    public void initFruits()
    {
        fruitList.clear();
//        for (int i = 0; i < 50; i++)
//        {
//            Random random = new Random();
//            int index = random.nextInt(fruits.length);
//            fruitList.add(fruits[index]);
//        }
        for (int i = 0; i < 10; i++)
        {
            fruitList.add(fruits[i]);
        }
        for (int i = 0; i < 10; i++)
        {
            fruitList.add(fruits[i]);
        }
        for (int i = 0; i < 10; i++)
        {
            fruitList.add(fruits[i]);
        }
    }

    public void initBulbs(){
        bulbList.clear();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            Toast.makeText(MainActivity.this, "V 1.0.0",
                    Toast.LENGTH_SHORT).show();
            Intent intent_version_introduction = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent_version_introduction);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_link) {
            // Handle the camera action
//            Toast.makeText(MainActivity.this, "You clicked nav_link",
//                    Toast.LENGTH_SHORT).show();
            Intent intent_wifi_connect = new Intent(MainActivity.this, WifiConnectActivity.class);
            startActivity(intent_wifi_connect);
        } else if (id == R.id.nav_book) {
            Intent intent_wifi_connect = new Intent(MainActivity.this, BookActivity.class);
            startActivity(intent_wifi_connect);
        } else if (id == R.id.nav_test) {
//            Toast.makeText(MainActivity.this, "You clicked nav_test",
//                    Toast.LENGTH_SHORT).show();
            Intent intent_wifi_connect = new Intent(MainActivity.this, OrderTestActivity.class);
            startActivity(intent_wifi_connect);
        } else if (id == R.id.nav_manage) {
//            Toast.makeText(MainActivity.this, "Start next activity",
//                    Toast.LENGTH_SHORT).show();
            Intent  intent=new Intent(MainActivity.this,StellarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent  intent=new Intent(MainActivity.this,ColorPickerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent  intent=new Intent(MainActivity.this,TimePickerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_colorpicker) {
            Intent  intent=new Intent(MainActivity.this,ColorPickerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_timepicker) {
            Intent  intent=new Intent(MainActivity.this,TimePickerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_storage) {
            Intent  intent=new Intent(MainActivity.this,StorageActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_litepal) {
            Intent intent = new Intent(MainActivity.this, LitePalActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
