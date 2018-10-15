package com.sansi.stellarWiFi.api;

import android.content.Context;

import com.sansi.stellar.object.Light;

import java.util.List;

import udp.core.bean.DeviceDelayInfo;
import udp.core.bean.DeviceStatusInfo;
import udp.packet.RgbEx;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 13:11
 *          类说明
 */
public interface LocalApiService {
    public Call<Integer> setBrightness(String mac, String ipAdress, int value);
    public Call<Void> asyncSetBrightness(String mac, String ipAdress, int value);

    public Call<List<LocalResponseCode>> setBrightness(final List<Light> lights, final int value);
    /**
     * 帧类型 ：固定为 0x0B
     调光系数：范围为0~100，100 表示最亮。
     变化时间：范围为0~65535，以 ms 单位。
     注意：支持此条协议的程序版本，将不能再支持“设置调光系数”这条协议。
     *
     * */
    public Call<Integer> setBrightnessV2(String mac, String ipAdress, int value, int changetime);
    public Call<Void> asyncSetBrightnessV2(final String mac, final String ipAdress, final int value, final int changetime);
    /**
     * @param broadcastAddress  局域网广播地址
     * @param mac 设备mac地址
     * @param timeout 搜索超时时间
     * */
    public Call<List<Light>> scanDevices(final byte[] broadcastAddress, final String mac, final int timeout);
    /**
     * @param broadcastAddress  局域网广播地址
     * @param timeout 搜索超时时间
     * */
    public Call<List<Light>> scanDevices(final byte[] broadcastAddress, final int timeout);

    public Call<Integer> setRgbw(String mac, String ipAdress, RgbEx rgbEx);
    public Call<Void> asyncSetRgbw(final String mac, final String ipAdress,
                                   final int mode, final int time, final int rgbw);
    /**
     *
     *色温值：2700～6500对应 2700K 到 6500K色温，例如 0x0A8C表示色温 2700K。2700K
     对应只亮暖白，6500K 对应只亮冷白，此时依靠调光系数调光。
     变化时间：范围为0~65535，以 ms 单位。
     * */
    public Call<Integer> setCct(final String mac, final String hostAdress, final int cctVal, final int changeTime);

    public Call<Integer> setScene(final String mac, final String hostAdress, final int modeId, final int changeTime);
    /**
     * 软件版本>=3
     * 调光系数：范围为0~100，100 表示最亮。
     变化时间：范围为0~65535，以 ms 单位。
     延迟时间: 范围为 0~0xFFFFFFFF,以 s 单位。0-立即调光，0xFFFFFFFF-取消延迟调光。
     * */
    public Call<Integer> setDelay(final String mac, final String hostAdress, final int bir, final int changeTime, final long delay);

    public Call<DeviceDelayInfo> queryDelay(final String mac, final String hostAdress);
    /**
     *
     * 恢复出厂默认（仅WIFI 设备有用）
     软件版本>=2
     * */
    public Call<Integer> resetDevice(final String mac, final String hostAdress);
    /**
     * 设置SSID及密码（仅WIFI设备有用）
     * */
    public Call<Integer> setSsid(final String mac, final String hostAdress, final String ssid, final String pass);
    @Auth(isCheckStatus = false)
    public String findIpAdress(String mac);
    /**
     * 查询灯具状态
     软件版本>=2
     *
     * */
    public Call<DeviceStatusInfo>queryStatus(final String mac, final String hostAdress);
    public Call<Integer> setDeviceName(final String mac, final String hostAdress, final String name);

    @Auth(isCheckStatus = false)
    public void startScanDevices(final Context context);
    @Auth(isCheckStatus = false)
    public boolean scanDeviceEnable();
    @Auth(isCheckStatus = false)
    public void stopScanDevices();
    @Auth(isCheckStatus = false)
    public List<Light>getLocalLights();
}
