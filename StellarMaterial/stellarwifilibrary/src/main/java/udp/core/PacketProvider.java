package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 21:31
 *          类说明
 */
public interface PacketProvider {
    public Packet parsePacket(byte[] data) throws Exception;
}
