package com.example.bingchenghu.stellarmaterial;

import org.litepal.crud.DataSupport;

public class BulbGroup extends DataSupport{
    private Bulb bulb1;
    private Bulb bulb2;
    private Bulb bulb3;
    private Bulb bulb4;

    public void setBulb1(Bulb bulb1) {
        this.bulb1 = bulb1;
    }

    public void setBulb2(Bulb bulb2) {
        this.bulb2 = bulb2;
    }

    public void setBulb3(Bulb bulb3) {
        this.bulb3 = bulb3;
    }

    public void setBulb4(Bulb bulb4) {
        this.bulb4 = bulb4;
    }

    public Bulb getBulb1() {
        return bulb1;
    }

    public Bulb getBulb2() {
        return bulb2;
    }

    public Bulb getBulb3() {
        return bulb3;
    }

    public Bulb getBulb4() {
        return bulb4;
    }

}
