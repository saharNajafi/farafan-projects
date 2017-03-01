package com.gam.nocr.ems.data.dao.impl;

import com.gam.nocr.ems.data.dao.CardRequestDAO;
import com.gam.nocr.ems.data.domain.*;
import com.gam.commons.core.util.JPAUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertNotNull;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class CardRequestDAOImplTest {

//	private CardRequestDAO cardRequestDAO;
//
//	@Before
//	public void setUp() throws Exception {
//		cardRequestDAO = new CardRequestDAOImpl();
//		JPAUtil.setEntityManagerFactory("EmsHsqldbPU");
//		cardRequestDAO.setEm(JPAUtil.getEntityManager());
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		JPAUtil.closeEntityManager();
//	}
//
//	@Test
//	public void testCreate() throws Exception {
//		/*CitizenInfoTO ctzInfo = new CitizenInfoTO();
//
//		SpouseTO spouse = new SpouseTO(ctzInfo, "سارا", "اصغری");
//		ChildTO child = new ChildTO(ctzInfo, "نسيم");
//		ChildTO child2 = new ChildTO(ctzInfo, "نازنين");
//
//		ctzInfo.getSpouses().add(spouse);
//		ctzInfo.getChildren().add(child);
//		ctzInfo.getChildren().add(child2);
//
//		CitizenTO citizen = new CitizenTO(ctzInfo, "علی", "کاظميان", "8678678678");
//
//		CardRequestTO cardRequest = new CardRequestTO();
//		cardRequest.setCitizen(citizen);
//		cardRequest.setEnrolledDate(new Date());
//
//		assertNotNull("Persistence of CardRequest failed!", cardRequestDAO.create(cardRequest));*/
//	}
}
