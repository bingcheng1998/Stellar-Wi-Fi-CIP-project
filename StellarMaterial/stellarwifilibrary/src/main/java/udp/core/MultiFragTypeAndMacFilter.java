package udp.core;

import java.util.List;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/21 14:48
 *          类说明
 */
public class MultiFragTypeAndMacFilter implements MultiPacketFilter {

    List<Packet> packets;

    public MultiFragTypeAndMacFilter(List<Packet> packets) {
        this.packets = packets;
    }

    @Override
    public boolean accept(Packet packet) {
        for (Packet p : packets) {
            if (isAccept(packet, p)) return true;
        }
        return false;
    }

    private boolean isAccept(Packet packet, Packet src) {
        return packet.getFragType() == src.getFragType()
                && packet.getMac().equalsIgnoreCase(src.getMac());
    }
    @Override
    public int packetSize() {
        return packets.size();
    }
}
