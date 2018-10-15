package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/2/28 9:33
 *          类说明
 */
public class ConnectionNotReadyException extends NbaException {
    public ConnectionNotReadyException() {
    }

    public ConnectionNotReadyException(String detailMessage) {
        super(detailMessage);
    }

    public ConnectionNotReadyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ConnectionNotReadyException(Throwable throwable) {
        super(throwable);
    }
}
