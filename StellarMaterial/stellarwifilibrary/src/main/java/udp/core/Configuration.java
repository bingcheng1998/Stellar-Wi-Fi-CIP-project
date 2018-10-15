package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 13:24
 *          类说明
 */
public class Configuration {
    private static int packetCollectorSize=500;
    private static int defaultPacketReplyTimeout = 400;

    public static int getPacketCollectorSize() {
        return packetCollectorSize;
    }

    public static void setPacketCollectorSize(int packetCollectorSize) {
        Configuration.packetCollectorSize = packetCollectorSize;
    }
    public static int getDefaultPacketReplyTimeout() {
        // The timeout value must be greater than 0 otherwise we will answer the default obj
        if (defaultPacketReplyTimeout <= 0) {
            defaultPacketReplyTimeout = 400;
        }
        return defaultPacketReplyTimeout;
    }

}
