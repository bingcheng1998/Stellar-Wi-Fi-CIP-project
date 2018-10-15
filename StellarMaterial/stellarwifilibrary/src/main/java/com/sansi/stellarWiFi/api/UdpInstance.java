package com.sansi.stellarWiFi.api;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.sansi.stellar.StellarApplication;
import com.sansi.stellar.object.Light;
import com.sansi.stellarWiFi.util.L;
import com.sansi.stellarWiFi.util.WifiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import udp.core.Configuration;
import udp.core.ConnectionConfiguration;
import udp.core.FragTypeFilter;
import udp.core.Kits;
import udp.core.MultiPacketCollector;
import udp.core.NbaException;
import udp.core.NoResponseException;
import udp.core.NotConnectedException;
import udp.core.Packet;
import udp.core.PacketCollector;
import udp.core.PacketFilter;
import udp.core.PacketListener;
import udp.core.ProtocolError;
import udp.core.ProtocolErrorException;
import udp.core.UDPConnection;
import udp.core.bean.DeviceDelayInfo;
import udp.core.bean.DeviceStatusInfo;
import udp.packet.DeviceBrightnessReq;
import udp.packet.DeviceBrightnessV2Req;
import udp.packet.DeviceCctReq;
import udp.packet.DeviceDelayQueryReq;
import udp.packet.DeviceDelayQueryRes;
import udp.packet.DeviceDelayReq;
import udp.packet.DeviceInfoReq;
import udp.packet.DeviceInfoRes;
import udp.packet.DeviceResetReq;
import udp.packet.DeviceRgbReq;
import udp.packet.DeviceRgbSimpleReq;
import udp.packet.DeviceSceneReq;
import udp.packet.DeviceSetNameReq;
import udp.packet.DeviceSetSsidReq;
import udp.packet.DeviceStatusReq;
import udp.packet.DeviceStatusRes;
import udp.packet.PacketSegment;
import udp.packet.RgbEx;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/21 14:03
 *          类说明
 */
public class UdpInstance implements LocalApiService {
    public static final int SERVER_PORT = 13434;
    private static UdpInstance udpInstance;
    private UDPConnection udpConnection;
    private ConcurrentHashMap<String, LocalDeviceInfo> mc2AdressMap = new ConcurrentHashMap<>();

    private static class LocalDeviceInfo {
        /*String mac;
        String address;*/
        Light light;
        private long createTime;
        private long liveTime = 10_000L;
        {
            createTime = System.currentTimeMillis();
        }

        public boolean isValidate() {
            return (System.currentTimeMillis() - createTime) <= liveTime;
        }
        public void relive(){
            createTime=System.currentTimeMillis();
        }

    }

    private UdpInstance(){
        try {
            checkUdpConnectionStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getMc2AdressMap() {
        HashMap<String,String>ma=new HashMap<>();
        Iterator<Map.Entry<String, LocalDeviceInfo>> iterator = this.mc2AdressMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LocalDeviceInfo> next = iterator.next();
            LocalDeviceInfo localDeviceInfo = next.getValue();
            if (localDeviceInfo.isValidate()) {
                ma.put(next.getKey(), localDeviceInfo.light.getIpAddr());
            } else {
                iterator.remove();
            }
        }
        return ma;
    }

    public synchronized static UdpInstance getInstance(){
        if(udpInstance ==null){
            udpInstance =new UdpInstance();
        }
        return udpInstance;
    }


    public ConnectionConfiguration getDefaultConfiguration() {
        ConnectionConfiguration conf = new ConnectionConfiguration();
        conf.setDpPort(SERVER_PORT);
        conf.setContext(StellarApplication.getApp());
        conf.setEnableMulticastLock(false);
        return conf;
    }

    public UDPConnection checkUdpConnectionStatus() throws IOException {
        if(udpConnection==null){
            udpConnection=new UDPConnection(getDefaultConfiguration());
            try {
                udpConnection.connect();
            } catch (NbaException.AlreadyConnectedException e) {
                e.printStackTrace();
            }
        }else{
            if(!udpConnection.isConnected()){
//                L.d("UDPConnection not connected.");
                try {
                    udpConnection.connect();
                } catch (NbaException.AlreadyConnectedException e) {
                    e.printStackTrace();
                }
            }else{
//                L.d("UDPConnection connected.");
            }
        }
        return udpConnection;
    }

    public void sendMultipPacket(Packet packet, PacketListener packetListener) throws IOException, NotConnectedException {
        sendMultipPacket(packet,packetListener,3,2,500);
    }

    public void sendMultipPacket(Packet packet, PacketListener packetListener, PacketFilter packetFilter) throws IOException, NotConnectedException {
        sendMultipPacket(packet,packetListener,packetFilter,3,2,500);
    }
    /**
     * 默认根据帧类型过滤接收
     * */
    public PacketCollector createPacketCollectorAndSend(Packet packet) throws NotConnectedException, IOException {
        UDPConnection udpConnection = checkUdpConnectionStatus();
        return udpConnection.createPacketCollectorAndSend(packet);
    }

    public PacketCollector createPacketCollectorAndSend(Packet packet, PacketFilter packetFilter) throws NotConnectedException, IOException {
        UDPConnection udpConnection = checkUdpConnectionStatus();
        // Create the packet collector before sending the packet
        PacketCollector packetCollector = udpConnection.createPacketCollector(packetFilter);
        // Now we can send the packet as the collector has been created
        sendPacket(packet);
        return packetCollector;
    }

    public Packet sendPacket(Packet packet, PacketFilter packetFilter) throws NotConnectedException, IOException {
        return sendPacket(packet, packetFilter, Configuration.getDefaultPacketReplyTimeout(), 3);
    }


    public Packet sendPacket(Packet packet, PacketFilter packetFilter, long timeout, int tryCont) throws NotConnectedException, IOException {
        Packet result = null;
        do {
            PacketCollector collector = createPacketCollectorAndSend(packet, packetFilter);
            result = collector.nextResult(timeout);
            collector.cancel();
            tryCont--;
        } while (result == null && tryCont > 0);
        return result;
    }

    public void sendPacket(Packet packet) throws IOException, NotConnectedException {
        checkUdpConnectionStatus().sendPacket(packet);
    }


    public  void shutdown(){
        if(udpConnection!=null){
            try {
                udpConnection.disconnect();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
            udpConnection=null;
        }
    }
    public void sendMultipPacket(Packet packet, PacketListener packetListener,
                                 int tryCount, int pacetCount, long interval) throws IOException, NotConnectedException {
        sendMultipPacket(packet,packetListener,new FragTypeFilter(packet),tryCount,pacetCount,interval);
    }

    public void sendMultipPacket(Packet packet, PacketListener packetListener, PacketFilter packetFilter,
                                 int tryCount, int pacetCount, long interval) throws IOException, NotConnectedException {
        UDPConnection udpConnection = checkUdpConnectionStatus();
        try {
            L.e("udp是否连接:"+udpConnection.isConnected());
            udpConnection.addPacketListener(packetListener, packetFilter);

            for (int i = 0; i < tryCount; i++) {
                for (int k = 0; k < pacetCount; k++) {
                    udpConnection.sendPacket(packet);
                }
                SystemClock.sleep(interval);
            }
        } finally {
            udpConnection.removePacketListener(packetListener);
        }
    }

    public void removePacketListener(PacketListener packetListener){
        try {
            checkUdpConnectionStatus().removePacketListener(packetListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Call<Integer> setBrightness(final String mac, final String hostAdress, final int value) {
        return  new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceBrightnessReq brightnessReq=new DeviceBrightnessReq();
                brightnessReq.setMac(mac);
                brightnessReq.setHostAdress(hostAdress);
                brightnessReq.setValue((byte) value);

                PacketCollector collector = udpConnection.createPacketCollectorAndSend(brightnessReq);
                //Log.i("DataInfoActivity", "execute: "+brightnessReq.toString());
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }
    public Call<Void> asyncSetBrightness(final String mac, final String hostAdress, final int value){
        return  new RealCall<Void>(){
            @Override
            public Void execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceBrightnessReq brightnessReq=new DeviceBrightnessReq();
                brightnessReq.setReply(false);
                brightnessReq.setMac(mac);
                brightnessReq.setHostAdress(hostAdress);
                brightnessReq.setValue((byte) value);
                udpConnection.sendPacket(brightnessReq);
                return super.execute();
            }
        };
    }
    @Override
    public Call<List<LocalResponseCode>> setBrightness(final List<Light>lights, final int value){
        return  new RealCall<List<LocalResponseCode>>(){
            @Override
            public List<LocalResponseCode> execute() throws Exception {
                List<LocalResponseCode>retCodes=new ArrayList<>();
                List<Packet>packets=new ArrayList<>();
                for(Light light:lights) {
                    String mac=light.getMac();
                    String hostAdress=light.getIpAddr();
                    MacFormatUtis.checkMac(mac);
                    DeviceBrightnessReq brightnessReq = new DeviceBrightnessReq();
                    brightnessReq.setMac(mac);
                    brightnessReq.setHostAdress(hostAdress);
                    brightnessReq.setValue((byte) value);
                    packets.add(brightnessReq);
                }
                MultiPacketCollector collector = udpConnection.createMultiPacketCollectorAndSend(packets);
                List<Packet> results = collector.nextResultOrThrow();
                for(Packet result:results) {
                    int retCode = result.getCode() & 0xFF;
                    retCodes.add(LocalResponseCode.toResponseCode(retCode));
                }
                return retCodes;
            }
        };
    }

    @Override
    public Call<Integer> setBrightnessV2(final String mac, final String hostAdress, final int value, final int changetime) {
        return new RealCall<Integer>() {
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceBrightnessV2Req brightnessV2Req = new DeviceBrightnessV2Req();
                brightnessV2Req.setMac(mac);
                brightnessV2Req.setHostAdress(hostAdress);
                brightnessV2Req.setValue((byte) value);
                brightnessV2Req.setChangetime((short) changetime);
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(brightnessV2Req);
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }

    @Override
    public Call<Void> asyncSetBrightnessV2(final String mac, final String ipAdress, final int value, final int changetime){
        return new RealCall<Void>() {
            @Override
            public Void execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceBrightnessV2Req brightnessV2Req = new DeviceBrightnessV2Req();
                brightnessV2Req.setReply(false);
                brightnessV2Req.setMac(mac);
                brightnessV2Req.setHostAdress(ipAdress);
                brightnessV2Req.setValue((byte) value);
                brightnessV2Req.setChangetime((short) changetime);
                udpConnection.sendPacket(brightnessV2Req);
                return super.execute();
            }
        };
    }
    private volatile boolean  scanGuard=false;

    public boolean scanDeviceEnable(){
        return scanGuard;
    }

    public void startScanDevices(final Context context){
        if(scanGuard){
            throw  new IllegalStateException("扫描任务已经再执行");
        }
        L.d("开启后台设备搜索...");
        scanGuard = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    checkUdpConnectionStatus();
                } catch (IOException e) {
                    //ignore
                }
                DeviceInfoReq deviceInfoReq = new DeviceInfoReq();
                deviceInfoReq.setMac("FF_FF_FF_FF_FF_FF_FF_FF".replace("_", ""));
                deviceInfoReq.setIpaddress(WifiUtils.getBroadcastAddress(context).getAddress());
                PacketListener broadcastPacketListener = new PacketListener() {
                    @Override
                    public void processPacket(Packet packet) throws NotConnectedException {
                        DeviceInfoRes res = (DeviceInfoRes) packet;
                        if (res != null) {
                            fireCache(res);
                        }
                    }
                };
                udpConnection.addPacketListener(broadcastPacketListener, new FragTypeFilter(deviceInfoReq));
                while (scanGuard) {
                    try {
                        checkUdpConnectionStatus();
                        udpConnection.sendPacket(deviceInfoReq);
                        Thread.sleep(udpConnection.getPacketReplyTimeout());
                    } catch (Exception ex) {
                        //ignore
                    }
                }

                udpConnection.removePacketListener(broadcastPacketListener);

            }
        }.start();
    }

    public List<Light>getLocalLights(){
        List<Light>lights=new ArrayList<>();
        Iterator<Map.Entry<String, LocalDeviceInfo>> iterator = this.mc2AdressMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LocalDeviceInfo> next = iterator.next();
            LocalDeviceInfo localDeviceInfo = next.getValue();
            if (localDeviceInfo.isValidate()) {
                lights.add(localDeviceInfo.light);
            } else {
                iterator.remove();
            }
        }
        return lights;
    }


    public void stopScanDevices(){
        scanGuard=false;
        L.d("关闭后台设备搜索...");
    }
    /**
     * @param broadcastAddress  局域网广播地址
     * @param timeout 搜索超时时间
     * */
    @Override
    public Call<List<Light>> scanDevices(final byte[] broadcastAddress, final String mac, final int timeout) {
        return new RealCall<List<Light>>(){
            @Override
            public List<Light> execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                final List<Light>lights=new ArrayList<>();
                DeviceInfoReq deviceInfoReq = new DeviceInfoReq();
                deviceInfoReq.setMac(mac);
                deviceInfoReq.setIpaddress(broadcastAddress);
                long start = System.currentTimeMillis();
                PacketListener broadcastPacketListener = new PacketListener() {
                    @Override
                    public void processPacket(Packet packet) throws NotConnectedException {
                        DeviceInfoRes res = (DeviceInfoRes) packet;
                        if (res != null) {
                            fireCache(res);
                            boolean rept = false;
                            for (Light lt : lights) {
                                if (lt.getMac().equalsIgnoreCase(res.getMac())) {
                                    rept = true;
                                    break;
                                }
                            }
                            if (!rept) {
                                Light l = new Light();
                                l.setIpAddr(res.getHostAdress());
                                l.setMac(res.getMac().toUpperCase());
                                l.setType(res.getDeviceType());
                                l.setName(res.getMac());
                                l.setSoftVer(res.getSoftVer() & 0xFF);
                                l.setHdVer(res.getHardVer() & 0xFF);
                                l.setGroup("");
                                lights.add(l);
                            }
                        }
                    }
                };
                udpConnection.addPacketListener(broadcastPacketListener,new FragTypeFilter(deviceInfoReq));
                do {
                    udpConnection.sendPacket(deviceInfoReq);
                    Thread.sleep(udpConnection.getPacketReplyTimeout()/10);
                } while (System.currentTimeMillis() - start < timeout);
                udpConnection.removePacketListener(broadcastPacketListener);
               /* mc2AdressMap.clear();
                for (Light light : lights) {
                    mc2AdressMap.put(light.getMac().toUpperCase(), light.getIpAddr());
                }*/
                return lights;
            }
        };
    }

    private void fireCache(DeviceInfoRes res) {
        LocalDeviceInfo localDeviceInfo = mc2AdressMap.get(res.getMac().toUpperCase());
        if (localDeviceInfo != null) {
            Light l = localDeviceInfo.light;
            l.setIpAddr(res.getHostAdress());
            l.setMac(res.getMac().toUpperCase());
            l.setType(res.getDeviceType());
            l.setName(res.getMac());
            l.setSoftVer(res.getSoftVer() & 0xFF);
            l.setHdVer(res.getHardVer() & 0xFF);
            l.setGroup("");
            localDeviceInfo.relive();
        } else {
            L.d("发现新的的设备:"+res);
            localDeviceInfo = new LocalDeviceInfo();
            Light l = new Light();
            l.setIpAddr(res.getHostAdress());
            l.setMac(res.getMac().toUpperCase());
            l.setType(res.getDeviceType());
            l.setName(res.getMac());
            l.setSoftVer(res.getSoftVer() & 0xFF);
            l.setHdVer(res.getHardVer() & 0xFF);
            l.setGroup("");
            localDeviceInfo.light=l;
            mc2AdressMap.put(res.getMac().toUpperCase(), localDeviceInfo);
        }
    }

    /**
     * @param broadcastAddress  局域网广播地址
     * @param timeout 搜索超时时间
     * */
    @Override
    public Call<List<Light>> scanDevices(final byte[] broadcastAddress, final int timeout){
        return  scanDevices(broadcastAddress,"FF_FF_FF_FF_FF_FF_FF_FF".replace("_", ""),timeout);
    }
    @Override
    public Call<Integer> setRgbw(final String mac, final String ipAdress, final RgbEx rgbEx) {
        return new RealCall<Integer>() {
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceRgbReq rgbReq = new DeviceRgbReq();
                rgbReq.setMac(mac);
                rgbReq.setHostAdress(ipAdress);
                rgbReq.setRgbEx(rgbEx);
                Packet packet = sendSegmentPacket(rgbReq);
//                if (packet != null && packet.getCode() != Packet.RECV_SUCCESS_CODE) {
//                    throw new NbaException("响应码为:" + Kits.bytetoHexString(packet.getCode()));
//                }
                if(packet!=null){
                    return packet.getCode() & 0xFF;
                }
                return super.execute();
            }
        };
    }

    @Override
    public Call<Void> asyncSetRgbw(final String mac, final String ipAdress,
                                   final int mode, final int time, final int rgbw) {
        return new RealCall<Void>() {
            @Override
            public Void execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceRgbSimpleReq rgbReq = new DeviceRgbSimpleReq();
                rgbReq.setReply(false);
                rgbReq.setMac(mac);
                rgbReq.setHostAdress(ipAdress);
                rgbReq.setMode((byte) (mode & 0xFF));
                rgbReq.setTime((short) (time & 0xFFFF));
                rgbReq.setRgbw(rgbw);
                udpConnection.sendPacket(rgbReq);
                return super.execute();
            }
        };
    }

    private Packet sendSegmentPacket(PacketSegment packetSegment) throws NotConnectedException, ProtocolErrorException, NoResponseException {
        long timeout = (long) (udpConnection.getPacketReplyTimeout() * packetSegment.segmentCount() * 1.3);
        long start = System.currentTimeMillis();
        Packet result;
        do {
            final PacketSegment nexPacketSegment = packetSegment.newSegment();
            PacketCollector collector = udpConnection.createPacketCollectorAndSend(nexPacketSegment, new PacketFilter() {
                @Override
                public boolean accept(Packet packet) {
                    if (packet != null) {
                        return (packet instanceof PacketSegment)
                                && (((PacketSegment) packet).getSeq() == nexPacketSegment.getSeq())
                                && packet.getMac().equalsIgnoreCase(nexPacketSegment.getMac())
                                && packet.getFragType() == nexPacketSegment.getFragType();
                    }
                    return false;
                }
            });
            result = collector.nextResult();
            collector.cancel();
            if (result == null) continue;
            if (result instanceof PacketSegment) {
                byte ackSeq = ((PacketSegment) result).getSeq();
                L.i(" 发送的序号为:" + Kits.bytetoHexString(nexPacketSegment.getSeq()) + ",确认的序号为:" + Kits.bytetoHexString(ackSeq)
                        + ",应答码为:" + Kits.bytetoHexString(result.getCode()));
                if (ackSeq == nexPacketSegment.getSeq()) {
                    if (result.getCode() == Packet.RECV_SUCCESS_CODE) {
                        packetSegment = nexPacketSegment;
                        continue;
                    } else {
                        return result;
                    }
                } else {
                    continue;
                }

            } else {
                throw new ProtocolErrorException("发送包分片确认包也要求分片", new ProtocolError());
            }
        } while (System.currentTimeMillis() - start < timeout && packetSegment.hasNext());
        return result;
    }
/**
 *
 *色温值：2700～6500对应 2700K 到 6500K色温，例如 0x0A，0x8C表示色温 2700K。2700K
 对应只亮暖白，6500K 对应只亮冷白，此时依靠调光系数调光。
 变化时间：范围为0~65535，以 ms 单位。
 * */
    @Override
    public Call<Integer> setCct(final String mac, final String hostAdress, final int cctVal, final int changeTime ) {
        return  new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceCctReq brightnessReq=new DeviceCctReq();
                brightnessReq.setMac(mac);
                brightnessReq.setHostAdress(hostAdress);
                brightnessReq.setCctVale((short) (cctVal& 0xFFFF));
                brightnessReq.setChangtime((short) (changeTime&0xFFFF));
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(brightnessReq);
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }
    @Override
    public Call<Integer> setScene(final String mac, final String hostAdress, final int modeId, final int changeTime ) {
        return  new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceSceneReq deviceSceneReq=new DeviceSceneReq();
                deviceSceneReq.setMac(mac);
                deviceSceneReq.setHostAdress(hostAdress);
                deviceSceneReq.setModeId((byte) (modeId&0xFF));
                deviceSceneReq.setChangeTime((short) (changeTime&0xFFFF));
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(deviceSceneReq);
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }

    @Override
    public Call<Integer> setDelay(final String mac, final String hostAdress, final int bir, final int changeTime, final long delay ) {
        return  new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceDelayReq deviceDelayReq=new DeviceDelayReq();
                deviceDelayReq.setMac(mac);
                deviceDelayReq.setHostAdress(hostAdress);
                deviceDelayReq.setBirVal((byte) (bir&0xFF));
                deviceDelayReq.setChangetime((short) (changeTime&0xFFFF));
                deviceDelayReq.setDelay((int) (delay&0xFFFFFFFF));
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(deviceDelayReq);
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }

    @Override
    public Call<DeviceDelayInfo> queryDelay(final String mac, final String hostAdress) {
        return  new RealCall<DeviceDelayInfo>(){
            @Override
            public DeviceDelayInfo execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceDelayQueryReq deviceDelayReq=new DeviceDelayQueryReq();
                deviceDelayReq.setMac(mac);
                deviceDelayReq.setHostAdress(hostAdress);
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(deviceDelayReq);
                DeviceDelayQueryRes result = (DeviceDelayQueryRes) collector.nextResultOrThrow();
                DeviceDelayInfo delayInfo=new DeviceDelayInfo();
                delayInfo.setBirVal(result.getBirVal()&0xFF);
                delayInfo.setChangetime(result.getChangetime()&0xFFFF);
                delayInfo.setDelay(result.getDelay()&0xFFFFFFFF);
                return delayInfo;
            }
        };
    }
/**
 *
 * 恢复出厂默认（仅WIFI 设备有用）
 软件版本>=2
 * */
    @Override
    public Call<Integer> resetDevice(final String mac, final String hostAdress) {
        return  new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceResetReq deviceResetReq = new DeviceResetReq();
                deviceResetReq.setMac(mac);
                deviceResetReq.setHostAdress(hostAdress);
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(deviceResetReq);
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }

    @Override
    public Call<Integer> setSsid(final String mac, final String hostAdress, final String ssid, final String pass) {
        return  new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceSetSsidReq setSsidReq = new DeviceSetSsidReq();
                setSsidReq.setMac(mac);
                setSsidReq.setHostAdress(hostAdress);
                setSsidReq.setSsid(ssid);
                setSsidReq.setPass(pass);
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(setSsidReq);
                Packet result = collector.nextResultOrThrow();
                Log.i("udp", "setSsid: "+mac+hostAdress+ssid+pass);
                return result.getCode() & 0xFF;
            }
        };
    }

    @Override
    public String findIpAdress(String mac) {
        if (mac == null || mac.length() < 1) return null;
        String MAC = mac.toUpperCase();
        if (mc2AdressMap.containsKey(MAC)) {
            LocalDeviceInfo localDeviceInfo = mc2AdressMap.get(MAC);
            if(localDeviceInfo.isValidate()) {
                return localDeviceInfo.light.getIpAddr();
            }else{
                mc2AdressMap.remove(mac);
                return null;
            }
        } else {
            return null;
        }
    }
    @Override
    public Call<DeviceStatusInfo> queryStatus(final String mac, final String hostAdress){
        return new RealCall<DeviceStatusInfo>(){
            @Override
            public DeviceStatusInfo execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceStatusReq statusReq=new DeviceStatusReq();
                statusReq.setMac(mac);
                statusReq.setHostAdress(hostAdress);
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(statusReq);
                DeviceStatusRes statusRes= (DeviceStatusRes) collector.nextResultOrThrow();
                DeviceStatusInfo deviceStatusInfo=new DeviceStatusInfo();
                deviceStatusInfo.setBir(statusRes.getBir() & 0xFF);
                deviceStatusInfo.setCctVal(statusRes.getCctVal() & 0xFFFF);
                deviceStatusInfo.setFault(statusRes.getFault() & 0xFFFF);
                deviceStatusInfo.setModeChangeTime(statusRes.getModeChangeTime() & 0xFFFF );
                deviceStatusInfo.setModeId(statusRes.getModeId() & 0xFF);
                deviceStatusInfo.setName(statusRes.getName());
                deviceStatusInfo.setRgbw(statusRes.getRgbw());
                return deviceStatusInfo;
            }
        };
    }

    @Override
    public Call<Integer> setDeviceName(final String mac, final String hostAdress, final  String name){
        return new RealCall<Integer>(){
            @Override
            public Integer execute() throws Exception {
                MacFormatUtis.checkMac(mac);
                DeviceSetNameReq setNameReq = new DeviceSetNameReq();
                setNameReq.setMac(mac);
                setNameReq.setHostAdress(hostAdress);
                setNameReq.setName(name);
                PacketCollector collector = udpConnection.createPacketCollectorAndSend(setNameReq);
                Packet result = collector.nextResultOrThrow();
                return result.getCode() & 0xFF;
            }
        };
    }
}
