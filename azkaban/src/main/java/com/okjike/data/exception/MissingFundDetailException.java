package com.okjike.data.exception;

public class MissingFundDetailException extends Exception {

    public MissingFundDetailException(String msg) {
        super(msg);
    }

    public MissingFundDetailException(String msg, Throwable t) {
        super(msg, t);
    }
}
