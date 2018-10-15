package com.example.bingchenghu.stellarmaterial;

import org.litepal.crud.DataSupport;

public class Bulb extends DataSupport {
    private int position;
    private int red;
    private int green;
    private int blue;
    private int brightness;
    private int temperature;

    public void setPosition(int position){
        this.position = position;
    }

    public void setRed(int red){
        this.red = red;
    }

    public void setGreen(int green){
        this.green = green;
    }

    public void setBlue(int blue){
        this.blue = blue;
    }

    public void setBrightness(int brightness){
        this.brightness = brightness;
    }

    public void setTemperature(int temperature){
        this.temperature = temperature;
    }

    public int getPosition(){
        return position;
    }

    public int getRed(){
        return red;
    }

    public int getGreen(){
        return green;
    }

    public int getBlue(){
        return blue;
    }

    public int getBrightness(){
        return brightness;
    }

    public int getTemperature() {
        return temperature;
    }
}
