package udp.packet;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 16:21
 *          类说明
 */
public interface Segmentable {

    public  boolean hasNext();
    /**
     * 要获取下一个包必须先进行发送操作
     * */
    public Packet next();
    public int segmentCount();

    public PacketSegment newSegment();

}
