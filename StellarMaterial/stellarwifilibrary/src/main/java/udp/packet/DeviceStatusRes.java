package udp.packet;

import java.nio.ByteBuffer;

import udp.core.Kits;
import udp.core.Packet;
import udp.core.PacketProvider;
import udp.core.Utils;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 15:17
 *          类说明
 */
public class DeviceStatusRes extends Packet {
    byte bir;
    int rgbw;
    short cctVal;
    byte modeId;
    short modeChangeTime;
    short fault;
    String name;

    public byte getBir() {
        return bir;
    }

    public void setBir(byte bir) {
        this.bir = bir;
    }

    public int getRgbw() {
        return rgbw;
    }

    public void setRgbw(int rgbw) {
        this.rgbw = rgbw;
    }

    public short getCctVal() {
        return cctVal;
    }

    public void setCctVal(short cctVal) {
        this.cctVal = cctVal;
    }

    public byte getModeId() {
        return modeId;
    }

    public void setModeId(byte modeId) {
        this.modeId = modeId;
    }

    public short getModeChangeTime() {
        return modeChangeTime;
    }

    public void setModeChangeTime(short modeChangeTime) {
        this.modeChangeTime = modeChangeTime;
    }

    public short getFault() {
        return fault;
    }

    public void setFault(short fault) {
        this.fault = fault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getChildString() {
        StringBuilder sb=new StringBuilder();
        sb.append("bir=").append(Kits.bytetoHexString(bir)).append(",")
                .append("rgbw=").append(Kits.int2HexString(rgbw)).append(",")
                .append("cctVal=").append(Utils.shortToHexString(cctVal)).append(",")
                .append("modeId=").append(Kits.bytetoHexString(modeId)).append(",")
        .append("modeChangeTime=").append(Utils.shortToHexString(modeChangeTime)).append(",")
        .append("fault=").append(Utils.shortToHexString(fault)).append(",")
        .append("name=").append(name);
        return sb.toString();
    }

    public static class DeviceStatusResProvider implements PacketProvider {

        @Override
        public Packet parsePacket(byte[] data) throws Exception {
            DeviceStatusRes deviceInfoRes=new DeviceStatusRes();
            ByteBuffer buffer=ByteBuffer.wrap(data);
            deviceInfoRes.bir=buffer.get();
            deviceInfoRes.rgbw=buffer.getInt();
            deviceInfoRes.cctVal=buffer.getShort();
            deviceInfoRes.modeId=buffer.get();
            deviceInfoRes.modeChangeTime=buffer.getShort();
            deviceInfoRes.fault=buffer.getShort();
            byte []namebytes=new byte[buffer.remaining()];
            buffer.get(namebytes);
            deviceInfoRes.name=new String(namebytes,"utf-8").trim();
            return deviceInfoRes;
        }
    }


}
