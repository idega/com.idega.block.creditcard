package com.idega.block.creditcard2.data.dao.impl;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.idega.core.persistence.Param;
import com.idega.core.persistence.impl.GenericDaoImpl;

@Repository(BorgunDAO.BEAN_NAME)
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Transactional(readOnly = false)
public class BorgunDAO extends GenericDaoImpl {

	public static final String BEAN_NAME = "borgunDAO";
	

}
