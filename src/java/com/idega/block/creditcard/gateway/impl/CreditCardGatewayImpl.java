package com.idega.block.creditcard.gateway.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.idega.block.creditcard.gateway.CreditCardGateway;
import com.idega.block.creditcard.service.FinanceService;
import com.idega.util.expression.ELUtil;

@Component
@Path(CreditCardGateway.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CreditCardGatewayImpl implements CreditCardGateway {

	private FinanceService getFinanceService() {
		return ELUtil.getInstance().getBean(FinanceService.BEAN_NAME);
	}

	@Override
	@POST
	@Path(SUCCESS)
	public void doHandleSuccessWebHook(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@Context ServletContext context
	) {
		getFinanceService().doHandleSuccessWebHook(request, response, context);
	}

	@Override
	@POST
	@Path(FAILURE)
	public void doHandleFailureWebHook(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@Context ServletContext context
	) {
		getFinanceService().doHandleFailureWebHook(request, response, context);
	}

}