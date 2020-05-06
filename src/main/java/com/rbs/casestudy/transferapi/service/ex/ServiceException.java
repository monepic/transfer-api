package com.rbs.casestudy.transferapi.service.ex;

/**
 * Indicates an problem in the service layer
 * @author ed
 *
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public ServiceException(String msg) { super(msg); }
}
