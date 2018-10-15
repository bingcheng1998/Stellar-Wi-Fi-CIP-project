
package udp.core;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


public class PacketCollector {

    private PacketFilter packetFilter;
    private ArrayBlockingQueue<Packet> resultQueue;
    private Connection connection;
    private boolean cancelled = false;

    /**
     * Creates a new packet collector. If the packet filter is <tt>null</tt>, then
     * all packets will match this collector.
     *
     * @param connection the connection the collector is tied to.
     * @param packetFilter determines which packets will be returned by this collector.
     */
    protected PacketCollector(Connection connection, PacketFilter packetFilter) {
        this(connection, packetFilter, Configuration.getPacketCollectorSize());
    }

    /**
     * Creates a new packet collector. If the packet filter is <tt>null</tt>, then
     * all packets will match this collector.
     *
     * @param connection the connection the collector is tied to.
     * @param packetFilter determines which packets will be returned by this collector.
     * @param maxSize the maximum number of packets that will be stored in the collector.
     */
    protected PacketCollector(Connection connection, PacketFilter packetFilter, int maxSize) {
        this.connection = connection;
        this.packetFilter = packetFilter;
        this.resultQueue = new ArrayBlockingQueue<Packet>(maxSize);
    }

    /**
     * Explicitly cancels the packet collector so that no more results are
     * queued up. Once a packet collector has been cancelled, it cannot be
     * re-enabled. Instead, a new packet collector must be created.
     */
    public void cancel() {
        // If the packet collector has already been cancelled, do nothing.
        if (!cancelled) {
            cancelled = true;
            connection.removePacketCollector(this);
        }
    }

    /**
     * Returns the packet filter associated with this packet collector. The packet
     * filter is used to determine what packets are queued as results.
     *
     * @return the packet filter.
     */
    public PacketFilter getPacketFilter() {
        return packetFilter;
    }

    /**
     * Polls to see if a packet is currently available and returns it, or
     * immediately returns <tt>null</tt> if no packets are currently in the
     * result queue.
     *
     * @return the next packet result, or <tt>null</tt> if there are no more
     *      results.
     */
    public Packet pollResult() {
    	return resultQueue.poll();
    }

    /**
     * Returns the next available packet. The method call will block (not return) until a packet is
     * available.
     * 
     * @return the next available packet.
     */
    public Packet nextResultBlockForever() {
        try {
            return resultQueue.take();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the next available packet. The method call will block until the connection's default
     * timeout has elapsed.
     * 
     * @return the next availabe packet.
     */
    public Packet nextResult() {
        return nextResult(connection.getPacketReplyTimeout());
    }

    /**
     * Returns the next available packet. The method call will block (not return)
     * until a packet is available or the <tt>timeout</tt> has elapsed. If the
     * timeout elapses without a result, <tt>null</tt> will be returned.
     *
     * @return the next available packet.
     */
    public Packet nextResult(long timeout) {
    	try {
			return resultQueue.poll(timeout, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
    }

    /**
     * Returns the next available packet. The method call will block until a packet is available or
     * the connections reply timeout has elapsed. If the timeout elapses without a result,
     * <tt>null</tt> will be returned. This method does also cancel the PacketCollector.
     * 
     * @return the next available packet.
     * @throws ProtocolErrorException in case an error response.
     * @throws NoResponseException if there was no response from the server.
     */
    public Packet nextResultOrThrow() throws NoResponseException, ProtocolErrorException {
        return nextResultOrThrow(connection.getPacketReplyTimeout());
    }

    /**
     * Returns the next available packet. The method call will block until a packet is available or
     * the <tt>timeout</tt> has elapsed. This method does also cancel the PacketCollector.
     * 
     * @param timeout the amount of time to wait for the next packet (in milleseconds).
     * @return the next available packet.
     * @throws NoResponseException if there was no response from the server.
     * @throws ProtocolErrorException in case an error response.
     */
    public Packet nextResultOrThrow(long timeout) throws NoResponseException, ProtocolErrorException {
        Packet result = nextResult(timeout);
        cancel();
        if (result == null) {
            throw new NoResponseException();
        }

        ProtocolError xmppError = result.getError();
        if (xmppError != null) {
            throw new ProtocolErrorException(xmppError);
        }

        return result;
    }

    /**
     * Processes a packet to see if it meets the criteria for this packet collector.
     * If so, the packet is added to the result queue.
     *
     * @param packet the packet to process.
     */
    protected void processPacket(Packet packet) {
        if (packet == null) {
            return;
        }
        
        if (packetFilter == null || packetFilter.accept(packet)) {
        	while (!resultQueue.offer(packet)) {
        		// Since we know the queue is full, this poll should never actually block.
        		resultQueue.poll();
        	}
        }
    }
}
