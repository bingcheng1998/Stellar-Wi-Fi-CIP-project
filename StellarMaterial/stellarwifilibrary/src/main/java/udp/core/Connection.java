package udp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 13:10
 *          类说明
 */
abstract  public class Connection {
    private static final Logger logger = LoggerFactory.getLogger(Connection.class);
    public static final boolean DEBUG = false;
    private long packetReplyTimeout= Configuration.getDefaultPacketReplyTimeout();
    private final static AtomicInteger connectionCounter=new AtomicInteger(0);
    private final static Set<ConnectionCreationListener> connectionEstablishedListeners=new CopyOnWriteArraySet<>();
    protected final Collection<PacketCollector> collectors = new ConcurrentLinkedQueue<PacketCollector>();
    protected final Collection<MultiPacketCollector> multiCollectors = new ConcurrentLinkedQueue<MultiPacketCollector>();
    protected final Map<PacketListener, ListenerWrapper> recvListeners =
            new ConcurrentHashMap<PacketListener, ListenerWrapper>();
    protected final ConnectionConfiguration config;
    protected final int connectionCounterValue = connectionCounter.getAndIncrement();
    /**
     * ExecutorService used to invoke the PacketListeners on newly arrived and parsed stanzas. It is
     * important that we use a <b>single threaded ExecutorService</b> in order to guarantee that the
     * PacketListeners are invoked in the same order the stanzas arrived.
     */
    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new HeheExecutorThreadFactory(connectionCounterValue));
    /**
     * SmackExecutorThreadFactory is a *static* inner class of XMPPConnection. Note that we must not
     * use anonymous classes in order to prevent threads from leaking.
     */
    private static final class HeheExecutorThreadFactory implements ThreadFactory {
        private final int connectionCounterValue;
        private int count = 0;

        private HeheExecutorThreadFactory(int connectionCounterValue) {
            this.connectionCounterValue = connectionCounterValue;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "Hehe Executor Service " + count++ + " ("
                    + connectionCounterValue + ")");
            thread.setDaemon(true);
            return thread;
        }
    }
    protected Connection(ConnectionConfiguration configuration) {
        config = configuration;
    }

    /**
     * Returns the configuration used to connect to the server.
     *
     * @return the configuration used to connect to the server.
     */
    protected ConnectionConfiguration getConfiguration() {
        return config;
    }

    public synchronized void connect() throws IOException, NbaException.AlreadyConnectedException {
        if (isConnected()) throw new NbaException.AlreadyConnectedException();
        connectInternal();
    }
    protected abstract void connectInternal() throws IOException;
    /**
     * A collection of ConnectionListeners which listen for connection closing
     * and reconnection events.
     */
    protected final Collection<ConnectionListener> connectionListeners =
            new CopyOnWriteArrayList<ConnectionListener>();
    /**
     * List of PacketListeners that will be notified when a new packet was sent.
     */
    protected final Map<PacketListener, ListenerWrapper> sendListeners =
            new ConcurrentHashMap<PacketListener, ListenerWrapper>();
    public abstract boolean isConnected();


    public long getPacketReplyTimeout() {
        return packetReplyTimeout;
    }

    public void setPacketReplyTimeout(long packetReplyTimeout) {
        this.packetReplyTimeout = packetReplyTimeout;
    }

    protected void removePacketCollector(PacketCollector collector) {
        collectors.remove(collector);
    }
    protected void removeMultiPacketCollector(MultiPacketCollector collector) {
        multiCollectors.remove(collector);
    }
    /**
     * Get the collection of all packet collectors for this connection.
     *
     * @return a collection of packet collectors for this connection.
     */
    protected Collection<PacketCollector> getPacketCollectors() {
        return collectors;
    }

    public Collection<MultiPacketCollector> getMultiPacketCollectors() {
        return multiCollectors;
    }

    public void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
        recvListeners.put(packetListener, wrapper);
    }

    /**
     * Removes a packet listener for received packets from this connection.
     *
     * @param packetListener the packet listener to remove.
     */
    public void removePacketListener(PacketListener packetListener) {
        recvListeners.remove(packetListener);
    }


    /**
     * Get a map of all packet listeners for received packets of this connection.
     *
     * @return a map of all packet listeners for received packets.
     */
    protected Map<PacketListener, ListenerWrapper> getPacketListeners() {
        return recvListeners;
    }
    public void addPacketSendingListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
        sendListeners.put(packetListener, wrapper);
    }

    /**
     * Removes a packet listener for sending packets from this connection.
     *
     * @param packetListener the packet listener to remove.
     */
    public void removePacketSendingListener(PacketListener packetListener) {
        sendListeners.remove(packetListener);
    }


    /**
     * Adds a connection listener to this connection that will be notified when
     * the connection closes or fails.
     *
     * @param connectionListener a connection listener.
     */
    public void addConnectionListener(ConnectionListener connectionListener) {
        if (connectionListener == null) {
            return;
        }
        if (!connectionListeners.contains(connectionListener)) {
            connectionListeners.add(connectionListener);
        }
    }

    protected Collection<ConnectionListener> getConnectionListeners() {
        return connectionListeners;
    }
    public void removeConnectionListener(ConnectionListener connectionListener) {
        connectionListeners.remove(connectionListener);
    }

    protected void callConnectionClosedOnErrorListener(Exception e) {
        logger.warn("Connection closed with error", e);
        for (ConnectionListener listener : getConnectionListeners()) {
            try {
                listener.connectionClosedOnError(e);
            }
            catch (Exception e2) {
                // Catch and print any exception so we can recover
                // from a faulty listener
                logger.warn("Error in listener while closing connection",e2);
            }
        }
    }

    /**
     * Get a map of all packet listeners for sending packets of this connection.
     *
     * @return a map of all packet listeners for sent packets.
     */
    protected Map<PacketListener, ListenerWrapper> getPacketSendingListeners() {
        return sendListeners;
    }
    /**
     * Sends the specified packet to the server.
     *
     * @param packet the packet to send.
     * @throws NotConnectedException
     */
    public void sendPacket(Packet packet) throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException();
        }
        if (packet == null) {
            throw new NullPointerException("Packet is null.");
        }

        sendPacketInternal(packet);
        // Process packet writer listeners. Note that we're using the sending thread so it's
        // expected that listeners are fast.
        firePacketSendingListeners(packet);
    }
    protected abstract void sendPacketInternal(Packet packet) throws NotConnectedException;
    /**
     * Shuts the current connection down.
     */
    protected abstract void shutdown();
    /**
     * Process all packet listeners for sending packets.
     *
     * @param packet the packet to process.
     */
    private void firePacketSendingListeners(Packet packet) {
        // Notify the listeners of the new sent packet
        for (ListenerWrapper listenerWrapper : sendListeners.values()) {
            try {
                listenerWrapper.notifyListener(packet);
            }
            catch (NotConnectedException e) {
              e.printStackTrace();
                break;
            }
        }
    }
    protected void processPacket(Packet packet) {
        if (packet == null) {
            return;
        }

        // Loop through all collectors and notify the appropriate ones.
        for (PacketCollector collector: getPacketCollectors()) {
            collector.processPacket(packet);
        }

        for(MultiPacketCollector multiPacketCollector:getMultiPacketCollectors()){
            multiPacketCollector.processPacket(packet);
        }

        // Deliver the incoming packet to listeners.
        executorService.submit(new ListenerNotification(packet));
    }
    /**
     * Adds a new listener that will be notified when new Connections are created. Note
     * that newly created connections will not be actually connected to the server.
     *
     * @param connectionCreationListener a listener interested on new connections.
     */
    public static void addConnectionCreationListener(
            ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.add(connectionCreationListener);
    }

    /**
     * Removes a listener that was interested in connection creation events.
     *
     * @param connectionCreationListener a listener interested on new connections.
     */
    public static void removeConnectionCreationListener(
            ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.remove(connectionCreationListener);
    }
    public synchronized void disconnect() throws NotConnectedException {
        if (!isConnected()) {
            return;
        }
        shutdown();
    };

    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
        PacketCollector collector = new PacketCollector(this, packetFilter);
        // Add the collector to the list of active collectors.
        collectors.add(collector);
        return collector;
    }

    public MultiPacketCollector createMultiPacketCollector(MultiPacketFilter packetFilter) {
        MultiPacketCollector collector = new MultiPacketCollector(this, packetFilter);
        // Add the collector to the list of active collectors.
        multiCollectors.add(collector);
        return collector;
    }

    public MultiPacketCollector createMultiPacketCollectorAndSend(List<Packet> packets) throws NotConnectedException, ConnectionNotReadyException {
        MultiPacketFilter packetFilter = new MultiFragTypeAndMacFilter(packets);
        // Create the packet collector before sending the packet
        MultiPacketCollector packetCollector = createMultiPacketCollector(packetFilter);
        // Now we can send the packet as the collector has been created
        for (Packet packet : packets) {
            sendPacket(packet);
        }
        return packetCollector;
    }
    public PacketCollector createPacketCollectorAndSend(Packet packet) throws NotConnectedException {
        PacketFilter packetFilter = new FragTypeAndMacFilter(packet);
        // Create the packet collector before sending the packet
        PacketCollector packetCollector = createPacketCollector(packetFilter);
        // Now we can send the packet as the collector has been created
        sendPacket(packet);
        return packetCollector;
    }

    public PacketCollector createPacketCollectorAndSend(Packet packet, PacketFilter packetFilter) throws NotConnectedException {
        // Create the packet collector before sending the packet
        PacketCollector packetCollector = createPacketCollector(packetFilter);
        // Now we can send the packet as the collector has been created
        sendPacket(packet);
        return packetCollector;
    }

    protected ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return executorService.schedule(command, delay, unit);
    }

    /**
     * Get the connection counter of this XMPPConnection instance. Those can be used as ID to
     * identify the connection, but beware that the ID may not be unique if you create more then
     * <tt>2*Integer.MAX_VALUE</tt> instances as the counter could wrap.
     *
     * @return the connection counter of this XMPPConnection
     */
    public int getConnectionCounter() {
        return connectionCounterValue;
    }
    @Override
    protected void finalize() throws Throwable {
        try {
            // It's usually not a good idea to rely on finalize. But this is the easiest way to
            // avoid the " Listener Processor" leaking. The thread(s) of the executor have a
            // reference to their ExecutorService which prevents the ExecutorService from being
            // gc'ed. It is possible that the Connection instance is gc'ed while the
            // listenerExecutor ExecutorService call not be gc'ed until it got shut down.
            executorService.shutdownNow();
        }
        finally {
            super.finalize();
        }
    }
    /**
     * A runnable to notify all listeners of a packet.
     */
    private class ListenerNotification implements Runnable {

        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public void run() {
            for (ListenerWrapper listenerWrapper : recvListeners.values()) {
                try {
                    listenerWrapper.notifyListener(packet);
                } catch(NotConnectedException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        }
    }
    protected static class ListenerWrapper {

        private PacketListener packetListener;
        private PacketFilter packetFilter;

        /**
         * Create a class which associates a packet filter with a listener.
         *
         * @param packetListener the packet listener.
         * @param packetFilter the associated filter or null if it listen for all packets.
         */
        public ListenerWrapper(PacketListener packetListener, PacketFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        /**
         * Notify and process the packet listener if the filter matches the packet.
         *
         * @param packet the packet which was sent or received.
         * @throws NotConnectedException
         */
        public void notifyListener(Packet packet) throws NotConnectedException {
            if (packetFilter == null || packetFilter.accept(packet)) {
                packetListener.processPacket(packet);
            }
        }
    }

}
