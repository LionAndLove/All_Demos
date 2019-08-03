package com.luo.dubbo.exception;

public class DubboConfigFileNotFound extends RuntimeException {

    /** xx */
    private static final long serialVersionUID = 707686865517868779L;

    public DubboConfigFileNotFound(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }
}
