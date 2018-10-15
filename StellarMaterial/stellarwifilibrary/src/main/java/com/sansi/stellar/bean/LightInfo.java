package com.sansi.stellar.bean;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.sansi.stellar.object.DeviceFilter;
import com.sansi.stellar.object.Light;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/6 17:04
 *          类说明
 */
public class LightInfo implements Parcelable,Cloneable {

    protected String mac_   = "0000000000000000";
    protected String name_  = "";
    protected DeviceFilter.LIGHT_TYPES type_ = DeviceFilter.LIGHT_TYPES.UNKNOWN;
    protected String group_ = "";
    protected String ipAddr = "";
    protected int soft_ver_ = 0;         //8. 当前软件版本号 ：1, 0-255
    protected int hd_ver_   = 0;         //9. 当前硬件版本号 ：1, 0-255

    private int rgbw = 0;                //2. 颜⾊ ： 4 , 四位分别对应RGBW。
    private int cct  = 6500;             //3. ⾊温 ： 2
    private int brightness = 50;         //4. 亮度 ： 1
    private int scene = 10;              //5. 场景号 ：1
    private int rate = 0;                //6. 速度 ： 2

    private byte failure = 0x00;         //1. 故障 ： 1 , 0xff 表示不不⽀支持故障检测， 0x00 表示⽆故障，否则有故障。
    private boolean accessible = false;  //是否可达，即本地是否可控
    private boolean allowAddRemote=true; //是否添加远程
    private boolean fromRemote=false;    //是否来自远程
    private boolean online=false;        //在线状态，即远程是否可控(7. 在线状态：1, 0x01 在线， 0x00 不在线)

    private long timeDelay;

    //后来添加的
    private int latestFirmwareVer=0;     //10. 最新固件版本号 ：2, 没有升级信息的话为0x0000
    private int updateTimeout=0;         //11. 升级超时时间 ：2, 0-65536s
    private int updateProgress=0;        //12. 升级进度 ：1，1-100
    private int curFirmwareVer=0;        //13. 当前固件版本号 ：2，
    private int isUpdate =0;             //14. 是否在更新 ：1，1表示在更新，0表示不在更新
    private int noneZeroBri =1;          //15. 非零亮度 : 1


    public LightInfo() {
    }

    public String getMac_() {
        return mac_;
    }

    public void setMac_(String mac_) {
        this.mac_ = mac_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public int getSoft_ver_() {
        return soft_ver_;
    }

    public void setSoft_ver_(int soft_ver_) {
        this.soft_ver_ = soft_ver_;
    }

    public int getHd_ver_() {
        return hd_ver_;
    }

    public void setHd_ver_(int hd_ver_) {
        this.hd_ver_ = hd_ver_;
    }

    public DeviceFilter.LIGHT_TYPES getType_() {
        return type_;
    }

    public void setType_(DeviceFilter.LIGHT_TYPES type_) {
        this.type_ = type_;
    }

    public String getGroup_() {
        return group_;
    }

    public void setGroup_(String group_) {
        this.group_ = group_;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getRgbw() {
        return rgbw;
    }

    public int toColor(int alpha) {
        int argb = Color.argb(alpha, (rgbw >> 24) & 0xFF, (rgbw >> 16) & 0xFF, (rgbw >> 8) & 0xFF);
        return argb;
    }

    public int getW() {
        return rgbw & 0xFF;
    }

    public void setRgbw(int rgbw) {
        this.rgbw = rgbw;
    }

    public int getCct() {
        return cct;
    }

    public void setCct(int cct) {
        this.cct = cct;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public boolean isOn() {
        return this.brightness >= 1;
    }

    public int getScene() {
        return scene;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public byte getFailure() {
        return failure;
    }

    public void setFailure(byte failure) {
        this.failure = failure;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    public long getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(long timeDelay) {
        this.timeDelay = timeDelay;
    }
    public boolean haveDelay(){
        return  !(timeDelay==0xFFFFFFFF || timeDelay==0);
    }

    public boolean isFromRemote() {
        return fromRemote;
    }

    public void setFromRemote(boolean fromRemote) {
        this.fromRemote = fromRemote;
    }
    /**
     * 本地是否可控
     * */
    public boolean isLocalCotrolable(){
        return this.getIpAddr() != null && this.getIpAddr().length() > 0;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }


    /**
     * 后来添加的
     * */
    public int getLatestFirmwareVer() {
        return latestFirmwareVer;
    }

    public void setLatestFirmwareVer(int latestFirmwareVer) {
        this.latestFirmwareVer = latestFirmwareVer;
    }

    public int getUpdateTimeout() {
        return updateTimeout;
    }

    public void setUpdateTimeout(int updateTimeout) {
        this.updateTimeout = updateTimeout;
    }

    public int getUpdateProgress() {
        return updateProgress;
    }

    public void setUpdateProgress(int updateProgress) {
        this.updateProgress = updateProgress;
    }

    public int getCurFirmwareVer() {
        return curFirmwareVer;
    }

    public void setCurFirmwareVer(int curFirmwareVer) {
        this.curFirmwareVer = curFirmwareVer;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getNoneZeroBri() {
        return noneZeroBri;
    }

    public void setNoneZeroBri(int noneZeroBri) {
        this.noneZeroBri = noneZeroBri;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LightInfo lightInfo = (LightInfo) o;

        if (mac_ != null ? !mac_.equals(lightInfo.mac_) : lightInfo.mac_ != null) return false;
        return type_ == lightInfo.type_;

    }

    @Override
    public int hashCode() {
        int result = mac_ != null ? mac_.hashCode() : 0;
        result = 31 * result + (type_ != null ? type_.hashCode() : 0);
        return result;
    }

    protected LightInfo(Parcel in) {
        mac_ = in.readString();
        name_ = in.readString();
        soft_ver_ = in.readInt();
        hd_ver_ = in.readInt();
        type_ = (DeviceFilter.LIGHT_TYPES) in.readValue(DeviceFilter.LIGHT_TYPES.class.getClassLoader());
        group_ = in.readString();
        ipAddr = in.readString();
        rgbw = in.readInt();
        cct = in.readInt();
        brightness = in.readInt();
        scene = in.readInt();
        rate = in.readInt();

        failure = in.readByte();
        accessible = (in.readByte() == (byte) 0x00) ;
        allowAddRemote = (in.readByte() == (byte) 0x00) ;
        timeDelay=in.readLong();
        fromRemote=(in.readByte()==(byte)0x00);
        online = (in.readByte() == (byte)0x00);

        //后来添加的
        latestFirmwareVer=in.readInt();
        updateTimeout=in.readInt();
        updateProgress=in.readInt();
        curFirmwareVer=in.readInt();
        isUpdate=in.readInt();
        noneZeroBri=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mac_);
        dest.writeString(name_);
        dest.writeInt(soft_ver_);
        dest.writeInt(hd_ver_);
        dest.writeValue(type_);
        dest.writeString(group_);
        dest.writeString(ipAddr);
        dest.writeInt(rgbw);
        dest.writeInt(cct);
        dest.writeInt(brightness);
        dest.writeInt(scene);
        dest.writeInt(rate);
        dest.writeByte(failure);

        if (accessible) {
            dest.writeByte((byte) 0x00);
        } else {
            dest.writeByte((byte) 0x01);
        }
        if(allowAddRemote){
            dest.writeByte((byte)0x00);
        }else{
            dest.writeByte((byte)0x01);
        }
        dest.writeLong(timeDelay);
        if(fromRemote){
            dest.writeByte((byte) 0x00);
        }else{
            dest.writeByte((byte)0x01);
        }
        if(online){
            dest.writeByte((byte)0x00);
        }else{
            dest.writeByte((byte)0x01);
        }

        //后来添加的
        dest.writeInt(latestFirmwareVer);
        dest.writeInt(updateTimeout);
        dest.writeInt(updateProgress);
        dest.writeInt(curFirmwareVer);
        dest.writeInt(isUpdate);
        dest.writeInt(noneZeroBri);
    }

    @SuppressWarnings("unused")
    public static final Creator<LightInfo> CREATOR = new Creator<LightInfo>() {
        @Override
        public LightInfo createFromParcel(Parcel in) {
            return new LightInfo(in);
        }

        @Override
        public LightInfo[] newArray(int size) {
            return new LightInfo[size];
        }
    };


    @Override
    public String toString() {
        return "LightInfo{" +
                "mac_='" + mac_ + '\'' +
                ", name_='" + name_ + '\'' +
                ", type_=" + type_ +
                ", group_='" + group_ + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", soft_ver_=" + soft_ver_ +
                ", hd_ver_=" + hd_ver_ +
                ", rgbw=" + rgbw +
                ", cct=" + cct +
                ", brightness=" + brightness +
                ", scene=" + scene +
                ", rate=" + rate +
                ", failure=" + failure +
                ", accessible=" + accessible +
                ", allowAddRemote=" + allowAddRemote +
                ", fromRemote=" + fromRemote +
                ", online=" + online +
                ", timeDelay=" + timeDelay +
                ", latestFirmwareVer=" + latestFirmwareVer +
                ", updateTimeout=" + updateTimeout +
                ", updateProgress=" + updateProgress +
                ", curFirmwareVer=" + curFirmwareVer +
                ", isUpdate=" + isUpdate +
                ", noneZeroBri=" + noneZeroBri +
                '}';
    }

    @Override
    public LightInfo clone() throws CloneNotSupportedException {
        return (LightInfo) super.clone();
    }

    public Light toLight() {
        Light light = new Light();
        light.setMac(this.getMac_());
        light.setName(this.getName_());
        light.setType(this.getType_());
        light.setGroup(this.getGroup_());
        light.setHdVer(this.getHd_ver_());
        light.setIpAddr(this.getIpAddr());
        light.setSoftVer(this.getSoft_ver_());
        light.setAllowAddRemote(this.isAllowAddRemote());
        light.setOnline(this.isOnline());
        return light;
    }

    public boolean isAllowAddRemote() {
        return allowAddRemote;
    }

    public void setAllowAddRemote(boolean allowAddRemote) {
        this.allowAddRemote = allowAddRemote;
    }
}
