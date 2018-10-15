package udp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 10:04
 *          类说明
 */
public class PacketWriter {
    private static final Logger logger = LoggerFactory.getLogger(PacketWriter.class);
    public static final int QUEUE_SIZE=500;
    private final UDPConnection connection;
    private final ArrayBlockingQueueWithShutdown<Packet> queue=new ArrayBlockingQueueWithShutdown<Packet>(QUEUE_SIZE,true);
    private Thread writerThread;
    private DatagramSocket writer;
    volatile boolean done;
    AtomicBoolean shutdownDone=new AtomicBoolean(false);
    public static final boolean DEBUG= Connection.DEBUG;
    private PacketEncoder packetEncoder=new PacketEncoder();
    protected PacketWriter(UDPConnection connection){
        this.connection=connection;
        init();
    }

    protected void init() {
        if (connection.getConfiguration().isServer()) {
            writer = connection.getWriteDatagramSocket();
        } else {
            writer = connection.getReadDatagramSocket();
        }
        done=false;
        shutdownDone.set(false);
        queue.start();
        writerThread=new Thread(){
            @Override
            public void run() {
                super.run();
                writePackets(this);
            }
        };
        writerThread.setName("UDP packet writer");
        writerThread.setDaemon(true);
    }

    DatagramPacket toDatagramPacket(Packet packet) throws Exception {
        byte[] datas = packetEncoder.encode(packet);
        DatagramPacket dp = new DatagramPacket(datas, 0, datas.length);
        byte[] ipaddress = packet.getIpaddress();
        if (ipaddress != null && ipaddress.length == 4) {
            dp.setAddress(InetAddress.getByAddress(ipaddress));
        }

        if (packet.getPort() > 0) {
            dp.setPort(packet.getPort());
        }
        return dp;
    }

    private void writePackets(Thread thisThread){
        try {
            while (!done && (writerThread == thisThread)) {
                Packet packet = nextPacket();
                if (packet != null) {
                    DatagramPacket dp_pack = null;
                    try {
                        dp_pack = toDatagramPacket(packet);
                    } catch (Exception e) {
                       e.printStackTrace();
                        logger.error("生成数据包异常",e);
                        continue;
                    }
                    if(dp_pack==null){
                        logger.warn("数据包为null");
                        continue;
                    }
                    ConnectionConfiguration configuration = connection.getConfiguration();
                    if(dp_pack.getPort()<0 ){
                        if(configuration.getDpPort()<0){
                            if(DEBUG)
                            logger.error("丢弃"+ PacketParserUtils.packetToString(packet));
                            continue;

                        }
                        dp_pack.setPort(configuration.getDpPort());
                        packet.setPort(configuration.getDpPort());
                    }
                    if (dp_pack.getAddress() == null) {
                        if (configuration.getDpIpaddress() == null
                                || configuration.getDpIpaddress().length != 4) {
                            if(DEBUG)
                                logger.error("丢弃"+ PacketParserUtils.packetToString(packet));
                            continue;
                        }
                        dp_pack.setAddress(InetAddress.getByAddress(configuration.getDpIpaddress()));
                        packet.setIpaddress(configuration.getDpIpaddress());
                    }
                    if(DEBUG)
                    logger.debug("Before==>"+ PacketParserUtils.packetToString(packet));
                    writer.send(dp_pack);
                    if(DEBUG)
                        logger.debug("After==>"+ PacketParserUtils.packetToString(packet));
                }
            }

                while (!queue.isEmpty()) {
                    Packet packet = queue.remove();
                    if(packet==null){
                        logger.warn("发送数据包为null");
                        continue;
                    }
                    try {
                        DatagramPacket datagramPacket = toDatagramPacket(packet);
                        if (datagramPacket == null) continue;
                        writer.send(datagramPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("发送失败", e);
                    }
                }

            queue.clear();
            shutdownDone.set(true);
            synchronized (shutdownDone) {
                shutdownDone.notify();
            }
        } catch (Exception e) {
//            L.d("UDP写异常:done="+done+" connection.isSocketClosed="+connection.isSocketClosed());
            e.printStackTrace();
            // The exception can be ignored if the the connection is 'done'
            // or if the it was caused because the socket got closed
            if (!(done || connection.isSocketClosed())) {
                shutdown();
                connection.notifyConnectionError(e);
            }
        }
    }




    private Packet nextPacket(){
        if(done){
            return  null;
        }
        Packet packet=null;
        try {
            packet=queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return packet;

    }

    public void shutdown(){
        done=true;
        queue.shutdown();
        synchronized (shutdownDone){
            if(!shutdownDone.get()){
                try {
                    shutdownDone.wait(connection.getPacketReplyTimeout());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  void sendPacket(Packet packet){
        if(done){
            throw new IllegalStateException("is done");
        }
        try {
            queue.put(packet);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void startup(){
        writerThread.start();
    }
}
