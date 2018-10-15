package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 15:11
 *          类说明
 */
public class DeviceStatusReq extends Packet {
    public final static byte mFrameType = 0x0A;

    public DeviceStatusReq() {
        setFragType(mFrameType);
        setCode(NEED_RESPONSE_CODE);
    }
}
