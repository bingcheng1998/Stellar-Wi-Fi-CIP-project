package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/21 14:48
 *          类说明
 */
public class FragTypeFilter implements PacketFilter {

    Packet packet;

    public FragTypeFilter(Packet packet) {
        this.packet = packet;
    }

    @Override
    public boolean accept(Packet packet) {
        return packet.getFragType() == this.packet.getFragType();
    }
}
