package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 13:22
 *          类说明
 */
public class ProtocolErrorException extends NbaException {
    private final ProtocolError error;

    public ProtocolErrorException(ProtocolError error) {
        this.error = error;
    }

    public ProtocolErrorException(String detailMessage, ProtocolError error) {
        super(detailMessage);
        this.error = error;
    }

    public ProtocolErrorException(String detailMessage, Throwable throwable, ProtocolError error) {
        super(detailMessage, throwable);
        this.error = error;
    }

    public ProtocolErrorException(Throwable throwable, ProtocolError error) {
        super(throwable);
        this.error = error;
    }

    public ProtocolError getError() {
        return error;
    }
}
