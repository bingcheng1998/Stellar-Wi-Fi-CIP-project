package udp.core;

import android.content.Context;
import android.net.wifi.WifiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 10:15
 *          类说明
 */
public class PacketReader {
    private static final Logger logger = LoggerFactory.getLogger(PacketReader.class);
    private Thread readThread;
    private UDPConnection connection;
    private DatagramSocket reader;
    volatile boolean done;
    private DatagramPacket dpReceivPacket;
    private PacketDecoder packetDecoder=new PacketDecoder();
    public final static boolean DEBUG= Connection.DEBUG;

    protected  PacketReader(UDPConnection connection){
        ConnectionConfiguration confi = connection.getConfiguration();
        if (confi != null) {
            Context context = confi.getContext();
            if(context!=null && confi.isEnableMulticastLock()) {
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                mLock = manager.createMulticastLock("PacketReaderMulticastLock");
            }
        }
        this.connection=connection;
        init();
    }

    private WifiManager.MulticastLock mLock;
    private synchronized void acquireLock() {
        if (mLock != null && !mLock.isHeld()) {
            mLock.acquire();
        }
    }

    private synchronized void releaseLock() {
        if (mLock != null && mLock.isHeld()) {
            try {
                mLock.release();
            } catch (Throwable th) {
                // ignoring this exception, probably wakeLock was already released
            }
        }
    }
    protected void init() {
        this.reader=connection.readDatagramSocket;
        this.done=false;
        readThread=new Thread(){
            @Override
            public void run() {
                super.run();
                parsePackets(this);
            }
        };
        readThread.setName("UDP Packet Reader");
        readThread.setDaemon(true);
    }
    private void parsePackets(Thread thread) {
        if (dpReceivPacket == null) {
            byte[] buffer = new byte[connection.getBufferSize()];
            dpReceivPacket = new DatagramPacket(buffer, 0, buffer.length);
        }
        try {
            acquireLock();
            while (!done && thread == readThread) {
                reader.receive(dpReceivPacket);
                Packet packet = null;
                try {
                    byte[] datas = Arrays.copyOfRange(dpReceivPacket.getData(), dpReceivPacket.getOffset(),
                            dpReceivPacket.getOffset() + dpReceivPacket.getLength());
                    packet =packetDecoder.decode(datas,dpReceivPacket.getAddress().getAddress(),dpReceivPacket.getPort());
                    if (packet != null) {
                        if(DEBUG)
                            logger.info("<=="+ PacketParserUtils.packetToString(packet));
                        connection.processPacket(packet);
                    }else{
                        logger.warn("packet is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dpReceivPacket.setLength(connection.getBufferSize());
            }
        } catch (Exception e) {
//            L.d("UDP读异常:done="+done+" connection.isSocketClosed="+connection.isSocketClosed());
            e.printStackTrace();
            // The exception can be ignored if the the connection is 'done'
            // or if the it was caused because the socket got closed
            if (!(done || connection.isSocketClosed())) {
                synchronized (this) {
                    this.notify();
                }
                // Close the connection and notify connection listeners of the
                // error.
                connection.notifyConnectionError(e);
            }
        }finally {
            releaseLock();
        }
    }

    public void shutdown(){
        done=true;
    }

    synchronized public void startup(){
        readThread.start();
//        try {
//            wait(connection.getPacketReplyTimeout());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
