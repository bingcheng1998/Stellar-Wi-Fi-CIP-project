package com.example.bingchenghu.stellarmaterial;

import org.litepal.crud.DataSupport;

public class Category extends DataSupport {




    private int id;

    private int categoryId;

    private String categoryName;

    private int categoryCode;

//    public void setCategoryId(int categoryId){
//        this.categoryId = categoryId;
//    }
    public int getId() {
    return id;
}

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

//    public int getCategoryId() {
//        return categoryId;
//    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryCode() {
        return categoryCode;
    }
}
