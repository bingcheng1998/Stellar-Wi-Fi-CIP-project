package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/1 13:00
 *          类说明
 */
public class DeviceInfoReq extends Packet {
    public final static byte mFrameType=0x00;
    public DeviceInfoReq(){
        setFragType(mFrameType);
        setCode(NEED_RESPONSE_CODE);
    }

}
