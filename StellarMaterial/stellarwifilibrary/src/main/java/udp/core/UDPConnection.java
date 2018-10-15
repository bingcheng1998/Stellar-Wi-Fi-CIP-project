package udp.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 10:14
 *          类说明
 */
public class UDPConnection extends Connection {
    private static final Logger logger = LoggerFactory.getLogger(UDPConnection.class);
    DatagramSocket readDatagramSocket;
    DatagramSocket writeDatagramSocket;

    private int bufferSize=128;

    PacketReader packetReader;
    PacketWriter packetWriter;
    // socketClosed is used concurrent
    // by UDPConnection, PacketReader, PacketWriter
    private volatile boolean socketClosed = false;
    private boolean connected = false;

    public UDPConnection() {
        super(new ConnectionConfiguration());
    }

    public UDPConnection(ConnectionConfiguration config) {
        super(config);
    }

    protected DatagramSocket getReadDatagramSocket() {
        return readDatagramSocket;
    }

    public DatagramSocket getWriteDatagramSocket() {
        return writeDatagramSocket;
    }

    /**
     * Sends out a notification that there was an error with the connection
     * and closes the connection. Also prints the stack trace of the given exception
     *
     * @param e the exception that causes the connection close event.
     */
    synchronized void notifyConnectionError(Exception e) {
//        L.d("packetReader="+packetReader+" packetReader.done:"+(packetReader!=null ? packetReader.done:null));
//        L.d("packetWriter="+packetWriter+" packetWriter.done:"+(packetWriter!=null ? packetWriter.done:null));
        // Listeners were already notified of the exception, return right here.
        if ((packetReader == null || packetReader.done) &&
                (packetWriter == null || packetWriter.done)) return;

        // Closes the connection temporary. A reconnection is possible
        shutdown();

        // Notify connection listeners of the error.
        callConnectionClosedOnErrorListener(e);
    }

    @Override
    protected void connectInternal() throws IOException {
        // Establishes the connection, readers and writers
        connectUsingConfiguration(config);
    }

    private void connectUsingConfiguration(ConnectionConfiguration config) throws IOException {
        if(config.isServer()) {
            byte[] dpIpaddress = config.getDpIpaddress();
            int bindPort = config.getDpPort();
            if(bindPort<0){
                throw new IllegalArgumentException("绑定端口号非法");
            }
            if(dpIpaddress==null || dpIpaddress.length!=4)throw new IllegalArgumentException("绑定IP地址非法");
            InetAddress inetAddr = InetAddress.getByAddress(dpIpaddress);
            readDatagramSocket =new DatagramSocket(bindPort, inetAddr);
            writeDatagramSocket = new DatagramSocket();
        }else{
            readDatagramSocket =new DatagramSocket();
        }
        socketClosed = false;
        initConnection();
    }

    private void initConnection(){
        try {
            boolean isFirstInitialization = packetReader == null || packetWriter == null;
            if (isFirstInitialization) {
                packetWriter = new PacketWriter(this);
                packetReader = new PacketReader(this);
            }else {
                packetWriter.init();
                packetReader.init();
            }
            // Start the packet writer. This will open a XMPP stream to the server
            packetWriter.startup();
            // Start the packet reader. The startup() method will block until we
            // get an opening stream packet back from server.
            packetReader.startup();
            connected=true;
        } catch (Exception e) {
            shutdown();
            throw  e;
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


    @Override
    public  boolean isConnected() {
        return connected;
    }

    @Override
    protected void sendPacketInternal(Packet packet) throws NotConnectedException {
        packetWriter.sendPacket(packet);
    }
    public boolean isSocketClosed() {
        return socketClosed;
    }
    @Override
    protected void shutdown() {
        logger.debug("UDPConnection shutdown....");
        if (packetReader != null) {
            packetReader.shutdown();
        }
        if (packetWriter != null) {
            packetWriter.shutdown();
        }

        // Set socketClosed to true. This will cause the PacketReader
        // and PacketWriter to ignore any Exceptions that are thrown
        // because of a read/write from/to a closed stream.
        // It is *important* that this is done before socket.close()!
        socketClosed = true;
        try {
            readDatagramSocket.close();
        } catch (Exception e) {
           logger.warn("shutdown", e);
        }
        if(writeDatagramSocket!=null) {
            try {
                writeDatagramSocket.close();
            } catch (Exception e) {
                logger.warn("WriteDatagramSocket shutdown", e);
            }
        }
        connected=false;
    }
}
