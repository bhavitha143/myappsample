package com.bh.realtrack.service;

import javax.servlet.http.HttpServletRequest;

import com.bh.realtrack.exception.UnknowAuthenticationException;
import com.bh.realtrack.model.CallerContext;

/**
 * @author Anand Kumar
 */
public interface ICallerContextManager {

    CallerContext getCallerContext(HttpServletRequest request) throws UnknowAuthenticationException;

}
