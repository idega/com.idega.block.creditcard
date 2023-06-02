package com.idega.block.creditcard.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.block.creditcard.CreditCardConstants;

public interface FinanceService {

	public static final String BEAN_NAME = CreditCardConstants.QUALIFIER_FINANCE_SERVICE;

	public void doHandleSuccessWebHook(HttpServletRequest request, HttpServletResponse response, ServletContext context);

	public void doHandleFailureWebHook(HttpServletRequest request, HttpServletResponse response, ServletContext context);

}