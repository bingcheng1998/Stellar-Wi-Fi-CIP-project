package com.sansi.stellar.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sansi.stellar.StellarApplication;
import com.sansi.stellar.bean.LightInfo;
import com.sansi.stellarWiFi.util.L;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/6 17:00
 *          类说明
 */
public class LocalLightHelper {
    private SharedPreferences sp;
    static LocalLightHelper instance;
    private static final  String lightskey ="lightskey";
    Gson gson=new Gson();
    private LocalLightHelper(){
        sp= StellarApplication.getApp().getSharedPreferences("locallights", Context.MODE_PRIVATE);
    }

    public static LocalLightHelper getInstance() {
        if(instance==null){
            instance=new LocalLightHelper();
        }
        return instance;
    }

    private static String getLightskey(){
        return lightskey ;
    }

    public  void setLights(List<LightInfo> lightInfos){
        L.d("saveLights:"+lightInfos);
        sp.edit().putString(getLightskey(),gson.toJson(lightInfos)).apply();
        //toJson这个方法是序列化确定类型的字符串生成json
    }

    public  List<LightInfo> getLights() {
        String json = sp.getString(getLightskey(), null);
        ArrayList<LightInfo> lights = gson.fromJson(json, new TypeToken<ArrayList<LightInfo>>() {
        }.getType());
        return lights;
    }

    public void addLight(LightInfo lightInfo){
        L.d("addLight:"+lightInfo);
        List<LightInfo> lights = getLights();
        if (lights == null) lights = new ArrayList<>();
        lights.add(lightInfo);
        setLights(lights);
    }

    public boolean updateLight(LightInfo lightInfo){
        L.d("updateLight:"+lightInfo);
        boolean updated=false;
        List<LightInfo> lights = getLights();
        if (lights == null || lights.isEmpty())return updated;
        else{
            for(LightInfo light:lights){
                if(light.getMac_().equalsIgnoreCase(lightInfo.getMac_())){
                    light.setGroup_(lightInfo.getGroup_());
                    light.setFailure(lightInfo.getFailure());
                    light.setHd_ver_(lightInfo.getHd_ver_());
                    light.setIpAddr(lightInfo.getIpAddr());
                    light.setBrightness(lightInfo.getBrightness());
                    light.setCct(lightInfo.getCct());
                    light.setName_(lightInfo.getName_());
                    light.setRate(lightInfo.getRate());
                    light.setRgbw(lightInfo.getRgbw());
                    light.setScene(lightInfo.getScene());
                    light.setSoft_ver_(lightInfo.getSoft_ver_());
                    light.setType_(lightInfo.getType_());
                    light.setAllowAddRemote(lightInfo.isAllowAddRemote());
                    updated=true;
                    break;
                }
            }
            if(updated) setLights(lights);
        }
        return updated;
    }

    public boolean updateLight(String mac ,String name,String groupName,boolean allowAdd2Remote){
        L.d("updateLight mac:"+mac+" name:"+name+" groupName:"+groupName);
        boolean updated=false;
        List<LightInfo> lights = getLights();
        if (lights == null || lights.isEmpty())return updated;
        else{
            for(LightInfo light:lights){
                if(light.getMac_().equalsIgnoreCase(mac)){
                    light.setName_(name);
                    light.setGroup_(groupName);
                    light.setAllowAddRemote(allowAdd2Remote);
                    updated=true;
                    break;
                }
            }
            if(updated) setLights(lights);
        }
        return updated;
    }

    public boolean updateLight(List<LightInfo> _lightInfos){
        L.d("updateLight "+_lightInfos);
        boolean updated=false;
        List<LightInfo> lights = getLights();
        if (lights == null || lights.isEmpty())return updated;
        else{
            for(LightInfo lightInfo:_lightInfos) {
                for (LightInfo light : lights) {
                    if (light.getMac_().equalsIgnoreCase(lightInfo.getMac_())) {
                        light.setGroup_(lightInfo.getGroup_());
                        light.setFailure(lightInfo.getFailure());
                        light.setHd_ver_(lightInfo.getHd_ver_());
                        light.setIpAddr(lightInfo.getIpAddr());
                        light.setBrightness(lightInfo.getBrightness());
                        light.setCct(lightInfo.getCct());
                        light.setName_(lightInfo.getName_());
                        light.setRate(lightInfo.getRate());
                        light.setRgbw(lightInfo.getRgbw());
                        light.setScene(lightInfo.getScene());
                        light.setSoft_ver_(lightInfo.getSoft_ver_());
                        light.setType_(lightInfo.getType_());
                        light.setAllowAddRemote(lightInfo.isAllowAddRemote());
                        updated = true;
                        break;
                    }
                }
            }
            if(updated) setLights(lights);
        }
        return updated;
    }

    public boolean deleteLight(String mac){
        L.d("deleteLight mac="+mac);
        boolean deleted=false;
        List<LightInfo> lights = getLights();
        if (lights == null || lights.isEmpty())return deleted;
        else{
            Iterator<LightInfo> iterator = lights.iterator();
            while (iterator.hasNext()){
                LightInfo light=iterator.next();
                if(light.getMac_().equalsIgnoreCase(mac)){
                    iterator.remove();
                    deleted=true;
                    break;
                }
            }
            if(deleted) setLights(lights);
        }
        return deleted;
    }

    public LightInfo getLight(String mac) {
        List<LightInfo> lights = getLights();
        if (lights == null || lights.isEmpty()) return null;
        for (LightInfo light : lights) {
            if (light.getMac_().equalsIgnoreCase(mac)) {
                return light;
            }
        }
        return null;
    }


}
