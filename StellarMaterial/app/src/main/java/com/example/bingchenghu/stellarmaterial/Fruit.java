package com.example.bingchenghu.stellarmaterial;

/**
 * Created by bingchenghu on 2018/3/22.
 */

public class Fruit {
    private String name;

    private int imageId;

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
