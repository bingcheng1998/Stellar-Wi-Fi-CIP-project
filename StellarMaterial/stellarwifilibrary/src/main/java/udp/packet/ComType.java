package udp.packet;

/**
 * 通讯类型
 */
public enum ComType {
    wifi("wifi", (byte) 0x00), bluetooth4("bluetooth4", (byte) 0x01), zigbeen("zigbeen", (byte) 0x02);

    private final String k;
    private final byte v;

    private ComType(String k, byte v) {
        this.k = k;
        this.v = v;
    }

    public static ComType toComType(byte v) {
        if (v == wifi.v) return wifi;
        if (v == bluetooth4.v) return bluetooth4;
        if (v == zigbeen.v) return zigbeen;
        return null;
    }

    public byte getData() {
        return v;
    }

}

