package com.example.bingchenghu.stellarmaterial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sansi.stellarWiFi.util.L;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class StorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        Button createDatabase = (Button) findViewById(R.id.creat_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.getDatabase();
                Toast.makeText(StorageActivity.this, "LitePal.getDatabase()", Toast.LENGTH_SHORT).show();
            }
        });
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setBookName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(12.23);
                book.setPress("Vinci Press");
                book.save();
            }
        });
        Button addData1 = (Button) findViewById(R.id.add_data1);
        addData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setBookName("The Lost Symbol");
                book.setAuthor("Dan Brown");
                book.setPages(510);
                book.setPrice(10.99);
                book.setPress("Vinci Press");
                book.save();
            }
        });
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
//                book.setBookName("The Lost Symbol");
//                book.setAuthor("Dan Brown");
//                book.setPages(510);
//                book.setPrice(10.99);
//                book.setPress("Vinci Press");
//                book.save();
                book.setPrice(14.95);
                book.setPress("Anchor");
                book.updateAll("bookName = ? and author = ?", "The Lost Symbol", "Dan Brown");
            }
        });
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(Book.class, "price < ?", "13");
            }
        });
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Book> books = DataSupport.findAll(Book.class);;
                for(Book book:books){
                    //L.d("StorageActivity","book num: " + book);
                    L.d("StorageActivity","book name: " + book.getBookName());
                    L.d("StorageActivity","book author: " + book.getAuthor());
                    L.d("StorageActivity","book page: " + book.getPages());
                    L.d("StorageActivity","book price: "+book.getPrice());
                    L.d("StorageActivity","book press: " + book.getPress());
                }
            }
        });
    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.creat_database:
//                LitePal.getDatabase();
//                Toast.makeText(StorageActivity.this, "creat_database_new", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.add_data:
//                Book book = new Book();
//                book.setBookName("The Da Vinci Code");
//                book.setAuthor("Dan Brown");
//                book.setPages(454);
//                book.setPrice(12.23);
//                book.setPress("Vinci Press");
//                book.save();
//                break;
//            case R.id.update_data:
//                Book book = new Book();
//                book.setBookName("The Lost Symbol");
//                book.setAuthor("Dan Brown");
//                book.setPages(510);
//                book.setPrice(12.23);
//                book.setPress("Vinci Press");
//                book.save();
//                break;
//            default:
//                break;
//        }
//    }
}
