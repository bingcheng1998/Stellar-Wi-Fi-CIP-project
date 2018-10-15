package udp.packet;

import udp.core.Packet;
import udp.core.PacketProvider;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 19:09
 *          类说明
 */
public class DeviceRgbRes extends PacketSegment {
    public DeviceRgbRes() {
    }

    public static class DeviceRgbResProvider implements PacketProvider {

        @Override
        public Packet parsePacket(byte[] data) throws Exception {

            DeviceRgbRes deviceRgbRes = new DeviceRgbRes();
            if(data.length==1) {
                deviceRgbRes.seq = data[0];
            }
            return deviceRgbRes;
        }
    }

    @Override
    protected synchronized void prepareSegment() {

    }
}
