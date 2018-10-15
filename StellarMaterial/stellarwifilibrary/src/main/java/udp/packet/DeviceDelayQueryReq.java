package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 22:14
 *          类说明
 */
public class DeviceDelayQueryReq extends Packet {
    public final static byte mFrameType=0x0D;
    public DeviceDelayQueryReq(){
        super(mFrameType,NEED_RESPONSE_CODE);
    }

}
