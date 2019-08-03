package com.luo.dubbo.exception;

public class RpcInvokeException extends RuntimeException{
    /** xx */
    private static final long serialVersionUID = 707686865517868779L;

    public RpcInvokeException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }
}
