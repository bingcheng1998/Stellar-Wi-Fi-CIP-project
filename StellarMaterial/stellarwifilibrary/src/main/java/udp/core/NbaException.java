package udp.core;

public class NbaException extends Exception {
    public NbaException() {
    }

    public NbaException(String detailMessage) {
        super(detailMessage);
    }

    public NbaException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NbaException(Throwable throwable) {
        super(throwable);
    }

    public static class AlreadyConnectedException extends NbaException {

        /**
         *
         */
        private static final long serialVersionUID = 5011416918049135231L;

        public AlreadyConnectedException() {
            super("Client is already connected");
        }
    }
}