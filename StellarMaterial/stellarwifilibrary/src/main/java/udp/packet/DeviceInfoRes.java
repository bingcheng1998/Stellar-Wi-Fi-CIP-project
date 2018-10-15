package udp.packet;

import com.sansi.stellar.object.DeviceFilter;

import udp.core.Packet;
import udp.core.PacketProvider;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/1 13:05
 *          类说明
 */
public class DeviceInfoRes extends Packet {
    ComType comType;
    byte softVer;
    byte hardVer;
    DeviceFilter.LIGHT_TYPES deviceType;

    public ComType getComType() {
        return comType;
    }

    public void setComType(ComType comType) {
        this.comType = comType;
    }

    public byte getSoftVer() {
        return softVer;
    }

    public void setSoftVer(byte softVer) {
        this.softVer = softVer;
    }

    public byte getHardVer() {
        return hardVer;
    }

    public void setHardVer(byte hardVer) {
        this.hardVer = hardVer;
    }

    public DeviceFilter.LIGHT_TYPES getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceFilter.LIGHT_TYPES deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String getChildString() {
        StringBuilder sb=new StringBuilder();
        sb.append("comType=").append(comType).append(",")
                .append("softVer=").append(softVer).append(",")
                .append("hardVer=").append(hardVer).append(",")
                .append("deviceType=").append(deviceType);
        return sb.toString();
    }

    public static class DeviceInfoResProvider implements PacketProvider {

        @Override
        public Packet parsePacket(byte[] data) throws Exception {
            DeviceInfoRes deviceInfoRes=new DeviceInfoRes();
            deviceInfoRes.comType= ComType.toComType(data[0]);
            deviceInfoRes.softVer=data[1];
            deviceInfoRes.hardVer=data[2];
            deviceInfoRes.deviceType = DeviceFilter.lookup((short) (data[3] << 8 | data[4]));
            return deviceInfoRes;
        }
    }
}
