//package com.gam.nocr.ems.web;
//
//import com.gam.nocr.ems.biz.service.external.client.gaas.User;
//import com.gam.nocr.ems.data.dao.impl.DepartmentDAOImpl;
//import com.gam.nocr.ems.data.domain.*;
//import com.gam.nocr.ems.data.enums.CardState;
//import com.gam.nocr.ems.data.enums.DepartmentDispatchSendType;
//import cucumber.runtime.arquillian.CukeSpace;
//import org.jboss.arquillian.container.test.json.api.Deployment;
//import org.jboss.arquillian.core.api.annotation.Inject;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.UserTransaction;
//import java.sql.Connection;
//import java.util.logging.Logger;
//
///**
// * Created by sahar on 7/16/17.
// */
//@RunWith(CukeSpace.class)
//public class AddCitizenInfo {
//    @Deployment
//    public static Archive<?> createDeployment() {
//
//        return ShrinkWrap.create(WebArchive.class, "test.json.war")
//                .addPackage(User.class.getPackage())
//                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//
//    @PersistenceContext
//    EntityManager em;
//
//    @EJB
//    DepartmentDAOImpl dep;
//
//
//    private static Logger log = Logger.getLogger(AddInactiveUserFeatureInject.class.getName());
//
//    @Before
//    public void preparePersistenceTest() throws Exception {
////        clearData();
//        insertData();
//    }
//
////    private void clearData() throws Exception {
////        dep.create();
////        em.joinTransaction();
////        System.out.println("Dumping old records...");
////        em.createQuery("delete from Game").executeUpdate();
////        utx.commit();
////    }
//
//    private void insertData() throws Exception {
//        log.info("begiiin");
//        em.joinTransaction();
//        System.out.println("Inserting records...");
//        DepartmentTO departmentTO = new DepartmentTO();
//        departmentTO.setParentDepartment(new DepartmentTO(1L));
//        departmentTO.setCode("003871");
//        departmentTO.setName("تست ثبت");
//        departmentTO.setAddress("تهران - خيابان امام خمينيییییی");
//        departmentTO.setPostalCode("1234567890");
//        departmentTO.setDn("www.g.com");
//        departmentTO.setParentDN("www.g.com");
//        departmentTO.setDispatchSendType(DepartmentDispatchSendType.CARD);
//        departmentTO.setLocation(new LocationTO(1L) );
//        departmentTO.setParentId(1L);
//        departmentTO.setLocId(1L);
//        dep.create(departmentTO);
//
//        em.persist(departmentTO);
//        log.info("idddd" + departmentTO.getId());
//        // clear the persistence context (first-level cache)
////        em.clear();
//    }
//
//}
