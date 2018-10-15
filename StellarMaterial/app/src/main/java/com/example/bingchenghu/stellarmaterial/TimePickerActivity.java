package com.example.bingchenghu.stellarmaterial;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

public class TimePickerActivity extends AppCompatActivity {
    private Toast toast;
    private TextView textView;
    int minutes = 0, seconds = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        textView =(TextView) findViewById(R.id.textView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }



    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.view_time_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("请选择定时长度");

        dialogBuilder.setCancelable(true);

        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String string = minutes +":" + seconds;
                textView.setText(string);
            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        LoopView loopView = (LoopView) dialogView.findViewById(R.id.loopView);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(""+i);
        }

        LoopView loopView1 = (LoopView) dialogView.findViewById(R.id.loopView1);
        ArrayList<String> list1 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list1.add("" + i);
        }
        // 滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(TimePickerActivity.this, "item " + index, Toast.LENGTH_SHORT);
                }
                minutes = index;
                toast.setText("item " + index);
                toast.show();
            }
        });

        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (toast == null) {
                    toast = Toast.makeText(TimePickerActivity.this, "item " + index, Toast.LENGTH_SHORT);
                }
                seconds = index;
                toast.setText("item " + index);
                toast.show();
            }
        });

        // 设置原始数据

        loopView.setItems(list);
        loopView1.setItems(list1);

        loopView.setInitPosition(minutes);
        loopView1.setInitPosition(seconds);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
