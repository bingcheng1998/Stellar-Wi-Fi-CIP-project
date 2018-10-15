package com.example.bingchenghu.stellarmaterial;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;
import com.sansi.stellar.object.Light;
import com.sansi.stellarWiFi.api.ApiHelper;
import com.sansi.stellarWiFi.api.Call;
import com.sansi.stellarWiFi.api.Callback;
import com.sansi.stellarWiFi.api.LocalApiService;
import com.sansi.stellarWiFi.util.L;
import com.sansi.stellarWiFi.util.T;
import com.sansi.stellarWiFi.util.WifiUtils;

import java.util.List;

public class LitePalActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    TextView card, aOut, bOut, createDatabase,dataNumber;
    EditText idIn, aIn, bIn, idOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        idIn = (EditText) findViewById(R.id.ID_in);
        aIn = (EditText) findViewById(R.id.A_in);
        bIn = (EditText) findViewById(R.id.B_in);

        Button writeIn = (Button) findViewById(R.id.Write_in);
        Button searchOut = (Button) findViewById(R.id.search_out);
        Button deleteId = (Button) findViewById(R.id.DeleteId);
        Button writeTest = (Button) findViewById(R.id.Write_test);

        idOut = (EditText) findViewById(R.id.ID_out);

        aOut = (TextView) findViewById(R.id.A_out);
        bOut = (TextView) findViewById(R.id.B_out);
        createDatabase = (TextView) findViewById(R.id.createDatabase);

        dataNumber = (TextView) findViewById(R.id.Number);
        card = (TextView) findViewById(R.id.Card);

//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        boolean isRemenber = sharedPreferences.getBoolean("remenbered", false);
//        if (isRemenber){
//            String account = sharedPreferences.getString("account", "");
//            String password = sharedPreferences.getString("password", "");
//            int dataNum = sharedPreferences.getInt("dataNum",0);
//            dataNumber.setText(dataNum);
//        }

        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.d("Before Find");
                List<Category> categories = DataSupport.findAll(Category.class);
                L.d("After Find");
                Toast.makeText(LitePalActivity.this, "现有数据"+categories.size(), Toast.LENGTH_SHORT).show();
                L.d("before setText");
                dataNumber.setText(String.valueOf(categories.size()));
                L.d("After SetText");
            }
        });

        writeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category category1 = new Category();
                //category.setId(2);
                category1.setCategoryId(123);
                category1.setCategoryName("固定写入测试");
                category1.setCategoryCode(10010);
                category1.save();
            }
        });

        //搜索数据
        searchOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idString = idOut.getText().toString();
                int id;
                if(TextUtils.isEmpty(idString)){
                    Toast.makeText(LitePalActivity.this, "ID 号码不能为空，否则无法查找", Toast.LENGTH_SHORT).show();
                }
                else {

                    id = Integer.parseInt(idString);
                    //显示到cardView上面，不管id是否存在
                    List<Category> categories = DataSupport.findAll(Category.class);
                    String newText = "";
                    boolean idExist = false;
                    for(Category category:categories){
                        //L.d("StorageActivity","book num: " + book);
                        newText += ("ID: " + category.getId());
                        newText += ("\tcategoryID: " + category.getCategoryId());
                        newText +=("\tString: " +category.getCategoryName());
                        newText +=("\tint: " + category.getCategoryCode()+"\n");
                        //如果ID存在，进行以下操作
                        if( category.getCategoryId()== id){
                            aOut.setText(category.getCategoryName());
                            bOut.setText(String.valueOf(category.getCategoryCode()));
                            Toast.makeText(LitePalActivity.this, "找到此ID号", Toast.LENGTH_SHORT).show();
                            idExist = true;
                        }
                    }
                    card.setText(newText);
                    if(idExist == false){
                        Toast.makeText(LitePalActivity.this, "此id号未录入", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //保存数据：
        writeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idString = idIn.getText().toString();
                String bInString = bIn.getText().toString();
                String aInString = aIn.getText().toString();
                int id;
                if(TextUtils.isEmpty(idString)){
                    Toast.makeText(LitePalActivity.this, "ID 号码不能为空, 否则无法保存",
                            Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(aInString) && TextUtils.isEmpty(bInString)){
                    Toast.makeText(LitePalActivity.this, "A 和 B 至少填上一个才能更新",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    id = Integer.parseInt(idString);
                    L.d("id = "+ id);
                    List<Category> categories = DataSupport.findAll(Category.class);
                    boolean idExist = false;
                    for(Category category:categories){
                        //如果ID存在，进行以下操作，更新数据
                        L.d("category.getCategoryId() = "+category.getCategoryId());
                        if(category.getCategoryId()== id){
                            if(!TextUtils.isEmpty(aInString)){
                                category.setCategoryName(aInString);
                                category.save();
                            }
                            if(!TextUtils.isEmpty(bInString)){
                                category.setCategoryCode(Integer.parseInt(bInString));
                                category.save();
                            }
                            idExist = true;
                            Toast.makeText(LitePalActivity.this, "更新成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    //当ID不存在，创建新数据
                    if(idExist == false){
                        if(TextUtils.isEmpty(aInString)||TextUtils.isEmpty(bInString)) {
                            Toast.makeText(LitePalActivity.this, "A 和 B 必须全部填上才可创建新的", Toast.LENGTH_SHORT).show();
                        } else {
                            Category category = new Category();
                            //category.setId(id);
                            category.setCategoryId(id);
                            category.setCategoryName(aInString);
                            category.setCategoryCode(Integer.parseInt(bInString));
                            category.save();
                            L.d("setCategoryAll "+id+aInString+Integer.parseInt(bInString));
                            Toast.makeText(LitePalActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //删除数据
        deleteId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idString = idOut.getText().toString();
                int id;
                if(TextUtils.isEmpty(idString)){
                    Toast.makeText(LitePalActivity.this, "ID 号码不能为空, 否则无法删除", Toast.LENGTH_SHORT).show();
                }
                else {
                    id = Integer.parseInt(idString);
                    List<Category> categories = DataSupport.findAll(Category.class);
                    for(Category category:categories){
                        //如果ID存在，进行以下操作，更新数据
                        L.d("category.getCategoryId() = "+category.getCategoryId());
                        if(category.getCategoryId()== id) {
                            DataSupport.deleteAll(Category.class, "categoryId = ?", "" + Integer.parseInt(idString));
                            Toast.makeText(LitePalActivity.this, "删除成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                        }
                    }
                }
            }
        );
    }
}

