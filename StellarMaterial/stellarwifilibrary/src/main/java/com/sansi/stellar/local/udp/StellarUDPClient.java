package com.sansi.stellar.local.udp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sansi.stellar.local.protocol.Data_Protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import coolniu.encrypt.utils.MyLog;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public class StellarUDPClient {
	private final String Tag = StellarUDPClient.class.getSimpleName();
	private final long SCAN_PERIOD;
	private DatagramSocket socket = null;
	private boolean connected = false;
	private int port;
	private boolean scaning;

	/**
	 * keep the listener to receive update events
	 */
	private ArrayList<EventListener> mListeners;	
	
	/**
	 * Monitor the UDP status events.
	 * 
	 * @author Chris Liu
	 * @version 1.0 
	 *
	 */
	public interface EventListener {
		/**
		 * Callback when broadcast data received
		 * @param data
		 * @throws Exception
		 */
		public void onFrameReady(int len, byte[] data, String addr) throws Exception;
		
		/**
		 * 
		 * @param bCancelled
		 */
		public void onScanFinished(boolean bCancelled);
	}
	
	public StellarUDPClient(long scan_period) {
		mListeners 	 = new ArrayList<EventListener>();
		this.scaning = false;
		this.SCAN_PERIOD = scan_period;
	}
	
	/**
	 * register the event listener
	 * @param l: listener instance
	 */
	public void registerUpdateListenner(EventListener l) {
		if (!mListeners.contains(l)) {
			mListeners.add(l);
		}
	}
	
	public void unregisterUpdateListenner(EventListener l) {
		if (mListeners.contains(l)) {
			mListeners.remove(l);
		}
	}
	
	/**
	 * 
	 * @param port
	 * @throws Exception
	 */
	public void connect(int port) throws Exception {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
			connected = true;
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
		Log.i(Tag, "close socket");
		if (socket != null) {
			scaning = false;
			socket.close();
			socket = null;
		}
	}
	
	/**
	 * 
	 * @param host
	 * @param data
	 */
	public void sendData(String host, byte[] data) {
		if (socket == null) {
			return;
		}
		
		byte[] buffer = new byte[data.length];
		DatagramPacket dataPacket = new DatagramPacket(buffer, data.length);
		dataPacket.setData(data);
		dataPacket.setLength(data.length);
		dataPacket.setPort(this.port);
		
		MyLog.i("lei", "udp size: "+data.length);
		try {
			dataPacket.setAddress(InetAddress.getByName(host));
			socket.send(dataPacket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * prepare to read data
	 */
	public void scanDevice() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				scaning = false;
	    		for (EventListener l: mListeners) {
					l.onScanFinished(true);
	    		}
			}
		}, SCAN_PERIOD);
		
		if (!this.scaning) {
			this.scaning = true;
			Thread readThread = new receiveBroadcast();
			readThread.run();
		}
	}
	
	/**
	 * prepare to receive back data
	 */
	public void receiveQueryData(){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				scaning = false;
	    		for (EventListener l: mListeners) {
					l.onScanFinished(true);
	    		}
			}
		}, SCAN_PERIOD);
		
		this.scaning = true;
		Thread readThread = new receiveBroadcast();
		readThread.run();
	}
	
	/**
	 * stop current device scan process
	 */
	public void stopScan(boolean bForce) {
		if (scaning || bForce) {
			scaning = false;
    		for (EventListener l: mListeners) {
				l.onScanFinished(false);
    		}
		}	
	}
	
	/**
	 * Thread to receive broadcast info
	 * @author Chris Liu
	 * @version 1.0 
	 *
	 */
	private class receiveBroadcast extends Thread {
	    public void run() {
		    if(socket == null) {
		    	return;
		    }
	        while(scaning) {
	            byte[] buf = new byte[256];
			    DatagramPacket packet = new DatagramPacket(buf, buf.length);			    
			    
			    try {
			        socket.receive(packet);
			    } catch (IOException e) {
			    	Log.w(Tag, "Socket error: " + e.getMessage());
			    	socket = null;
			    	break;
			    }
			    
			    try {
			        if(packet.getAddress() == null) {
				    	continue;
				    }
			        
				    byte[] data = packet.getData();
				   
				    int len = packet.getLength();
					if (!Data_Protocol.isValidFrame(data, len)) {
				        continue;
				    }
					MyLog.i("lei"," data[11]: "+data[11]+" data[12]: "+data[12]);
					 if (data[11] != 0x00) {
						 //if not success
						 continue;
					 }
					 
				   /* if (data[11] != 0x00 || data[12] != 0x00) {
				        continue;
				    }*/
		    		for (EventListener l: mListeners) {
						l.onFrameReady(len, data, packet.getAddress().getHostAddress());
						 
		    		}
			    } catch (Exception e) {			    	
			        e.printStackTrace();
			    }
			}
	    }
	}

	
	/**
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return this.connected;
	}
	
	Handler mHandler = new Handler() {		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
}
