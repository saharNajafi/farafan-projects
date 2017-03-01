package com.gam.nocr.ems.data.dao.impl;

import com.gam.nocr.ems.data.dao.CitizenDAO;
import com.gam.nocr.ems.data.domain.ChildTO;
import com.gam.nocr.ems.data.domain.CitizenInfoTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.SpouseTO;
import com.gam.commons.core.util.JPAUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
public class CitizenDAOImplTest {

	private CitizenDAO citizenDAO;

	@Before
	public void setUp() throws Exception {
		/*citizenDAO = new CitizenDAOImpl();
//		JPAUtil.setEntityManagerFactory("PortalPostgresPU");
		JPAUtil.setEntityManagerFactory("EmsHsqldbPU");
		citizenDAO.setEm(JPAUtil.getEntityManager());*/
	}

	@After
	public void tearDown() throws Exception {
		/*JPAUtil.closeEntityManager();*/
	}

	@Test
	public void testCreate() throws Exception {
		/*CitizenInfoTO ctzInfo = new CitizenInfoTO();
		ctzInfo.setFirstNameEnglish("Hassan");

		SpouseTO spouse = new SpouseTO(ctzInfo, "سارا", "اصغری");
		ChildTO child = new ChildTO(ctzInfo, "نسيم");
		ChildTO child2 = new ChildTO(ctzInfo, "نازنين");

		ctzInfo.getSpouses().add(spouse);
		ctzInfo.getChildren().add(child);
		ctzInfo.getChildren().add(child2);

		CitizenTO citizen = new CitizenTO(ctzInfo, "علی", "کاظميان", "8678678678");

		citizenDAO.create(citizen);
		assertNotNull("Persistence of citizenTO with spouse and child failed!", citizenDAO.create(citizen));*/
	}
}
