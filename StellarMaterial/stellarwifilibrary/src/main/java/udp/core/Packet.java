package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 10:14
 *          类说明
 */
public class Packet {
    private int port=-1;
    private byte[] ipaddress;
    private String mac;//8byte
    private ProtocolError error;
    /**
     * 帧类型
     * */
    private  byte fragType;
    /**
     * 发送码、应答码复用
     * */
    private  byte code;
    /**
     * 发送码：0x80为需要应答的帧，0x00表示不需要应答。
     * */
    public static final byte NEED_RESPONSE_CODE =(byte)0x80;
    public static final byte NO_RESPONSE_CODE =(byte)0x00;
    /**
     * 应答码:操作成功0x00,操作失败0x01,参数错误0x02,命令不支持0x03
     * */
    public static final byte RECV_SUCCESS_CODE=(byte)0x00;
    public static final byte RECV_FAIL_CODE=(byte)0x01;
    public static final byte RECV_PARAMS_ERROR_CODE=(byte)0x02;
    public static final byte RECV_COMMAND_NOT_SUPPORT_CODE=(byte)0x03;


    public Packet() {
    }

    public Packet(byte fragType, byte code) {
        this.fragType = fragType;
        this.code = code;
    }

    public Packet(int port, byte[] ipaddress) {
        this.port = port;
        this.ipaddress = ipaddress;
    }

    public Packet(int port, byte[] ipaddress, byte fragType, byte code,String mac) {
        checkMac(mac);
        this.port = port;
        this.ipaddress = ipaddress;
        this.fragType = fragType;
        this.code = code;
        this.mac=mac;

    }

    public Packet(Packet packet) {
        this.port = packet.port;
        this.ipaddress = packet.ipaddress;
        this.mac = packet.mac;
        this.error = packet.error;
        this.fragType = packet.fragType;
        this.code = packet.code;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte[] getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(byte[] ipaddress) {
        this.ipaddress = ipaddress;
    }

    public ProtocolError getError() {
        return error;
    }

    public byte[] getData() {
        return null;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        checkMac(mac);
        this.mac = mac;
    }
    private void checkMac(String mac) {
        if (mac == null || !mac.toLowerCase().matches("[0-f]{16}"))
            throw new IllegalArgumentException("mac 地址非法");
    }

    public String getHostAdress(){
        return PacketParserUtils.bytesToIp(ipaddress);
    }
    public void setHostAdress(String hostAdress){
        this.ipaddress=Kits.ipTobytes(hostAdress);
    }

    public byte getFragType() {
        return fragType;
    }
    /**
     * 发送码,应答码
     * */
    public  byte getCode(){
       return  code;
    }

    protected void setFragType(byte fragType) {
        this.fragType = fragType;
    }

    protected void setCode(byte code) {
        this.code = code;
    }
    public void setReply(boolean reply){
        if(reply){
            setCode(NEED_RESPONSE_CODE);
        }else{
            setCode(NO_RESPONSE_CODE);
        }
    }
    @Override
    public String toString() {
        String ipStr =null;
        if (ipaddress != null) {
            try {
                ipStr = PacketParserUtils.bytesToIp(ipaddress);
            } catch (Exception e) {
                //ignore
            }
        }
        StringBuilder sb=new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" ");
        sb.append("{").append("port=").append(port)
                .append(", ipAddress='").append(ipStr).append('\'');
        sb.append(",mac='").append(mac).append("\'");
        sb.append(", error=").append(error)
                .append(", fragType=").append(Kits.bytetoHexString(fragType))
                .append(", code=").append(Kits.bytetoHexString(code));
        String childString = getChildString();
        if(childString!=null && childString.length()>0){
            sb.append(", ").append(childString);
        }
        sb.append("}");
        return sb.toString();
    }

    public String getChildString(){
            return null;
    }
}
