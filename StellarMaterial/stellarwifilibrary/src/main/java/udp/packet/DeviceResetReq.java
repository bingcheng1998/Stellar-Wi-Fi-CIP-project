package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 23:06
 *          类说明
 */
public class DeviceResetReq extends Packet {
    public final static byte mFrameType = 0x08;

    public DeviceResetReq() {
        super(mFrameType, NEED_RESPONSE_CODE);

    }

}
