package com.hadluo.dubbo.transport.serializable;

public class SerializeException extends RuntimeException {

    /** xx */
    private static final long serialVersionUID = 5829296281974487692L;

    public SerializeException(String msg) {
        super(msg);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

}
