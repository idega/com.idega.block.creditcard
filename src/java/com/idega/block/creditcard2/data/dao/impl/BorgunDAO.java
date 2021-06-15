package com.idega.block.creditcard2.data.dao.impl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(BorgunDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class BorgunDAO extends GenericDaoImpl {

	public static final String BEAN_NAME = "borgunDAO";


}
