package udp.packet;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import udp.core.Packet;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/2 23:20
 *          类说明
 */
public class DeviceSetSsidReq extends Packet{
    public final static byte mFrameType = 0x07;
    static public final String key = "pssVmKbeDgYGWLsy";
    String ssid;
    String pass;

    public DeviceSetSsidReq(){
        super(mFrameType,NEED_RESPONSE_CODE);
    }

    public DeviceSetSsidReq(String ssid, String pass) {
        super(mFrameType,NEED_RESPONSE_CODE);
        this.ssid = ssid;
        this.pass = pass;

    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public  static byte[]str2bytes(String input){
        try {
            return input.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getData() {
        byte[] passBytes=new byte[0];
        if (pass != null || pass.length() > 0) {
            passBytes = RC4.encry_RC4_byte(pass, key);
        }
        byte[] ssidBytes = str2bytes(ssid);
        ByteBuffer buffer = ByteBuffer.allocate(2 + passBytes.length + ssidBytes.length);
        buffer.put((byte) ssidBytes.length).put(ssidBytes)
                .put((byte) passBytes.length).put(passBytes);
        return (byte[]) buffer.flip().array();
    }

    @Override
    public String getChildString() {
        StringBuilder sb=new StringBuilder();
        sb.append("ssid='").append(ssid)
                .append("',pass='").append(pass).append("'");
        return sb.toString();
    }
}
