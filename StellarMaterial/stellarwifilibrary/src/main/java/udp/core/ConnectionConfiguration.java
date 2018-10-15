package udp.core;

import android.content.Context;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 15:58
 *          类说明
 */
public class ConnectionConfiguration {
    private Context context;
    private int dpPort =-1;
    private byte[] dpIpaddress;
    private boolean  enableMulticastLock=false;
    private boolean server=false;

    public int getDpPort() {
        return dpPort;
    }

    public void setDpPort(int dpPort) {
        this.dpPort = dpPort;
    }

    public byte[] getDpIpaddress() {
        return dpIpaddress;
    }

    public void setDpIpaddress(byte[] dpIpaddress) {
        this.dpIpaddress = dpIpaddress;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isEnableMulticastLock() {
        return enableMulticastLock;
    }

    public void setEnableMulticastLock(boolean enableMulticastLock) {
        this.enableMulticastLock = enableMulticastLock;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }
}
