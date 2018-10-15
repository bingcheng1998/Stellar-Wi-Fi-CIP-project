package udp.core;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2016/9/20 13:35
 *          类说明
 */
public class NotConnectedException extends NbaException {
    public NotConnectedException() {
    }

    public NotConnectedException(String detailMessage) {
        super(detailMessage);
    }

    public NotConnectedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotConnectedException(Throwable throwable) {
        super(throwable);
    }
}
