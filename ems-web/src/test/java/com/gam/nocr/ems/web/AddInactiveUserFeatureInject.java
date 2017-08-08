package com.gam.nocr.ems.web;

import com.gam.nocr.ems.biz.service.external.client.gaas.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
import cucumber.runtime.arquillian.api.Features;
import gampooya.tools.util.Base64;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.security.MessageDigest;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by sahar on 7/2/17.
 */

@RunWith(CukeSpace.class)
@Features({ "src/test/resources/feature/inactive_user.feature" })
public class AddInactiveUserFeatureInject {
    GAASWebService gaasWebService;
    GAASWebServiceInterface gaasWebServiceInterface;
    ObjectFactory gaasWebServiceEntitiesObjectFactory;
    CASWebService casWebService;
    CASWebServiceInterface casWebServiceInterface;
    ObjectFactory casWebServiceEntitiesObjectFactory;
    User user;
    Integer userId;
    String hashCode;
    String ticket;
    private static Logger log = Logger.getLogger(AddInactiveUserFeatureInject.class.getName());
    @Deployment
    public static Archive<?> createDeployment() {

        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(User.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
//        clearData();
        insertData();
        startTransaction();
    }

//    private void clearData() throws Exception {
//        utx.begin();
//        em.joinTransaction();
//        System.out.println("Dumping old records...");
//        em.createQuery("delete from USER").executeUpdate();
//        utx.commit();
//    }

    private void insertData() throws Exception {
        log.info("Inserting records...");
        utx.begin();
        em.joinTransaction();
        log.info("Inserting records...");
        gaasWebService = new GAASWebService();
        gaasWebServiceInterface = gaasWebService.getGAASWebServiceImpl();
        gaasWebServiceEntitiesObjectFactory = new ObjectFactory();

        //create user
        user = new User();
        user.setEnabled(true);
        user.setUsername(("SaharUser" + new Random().nextInt()));

        log.info(user.getUsername());
        user.setPassword(MD5(user.getUsername() + "123456", "UTF-8"));

        user.setPersonId(1172);

        user.setUserDescription("new user");

        //create access
        Access access = new Access();
        access.setId(51524);
        user.getAccess().add(access);

        //create LoginIP
        LoginIP loginIP = new LoginIP();
        loginIP.setId(21);
        user.getLoginIP().add(loginIP);

        //create Role
        Role role = new Role();
        role.setId(68);
        user.getRole().add(role);

        //create schedule
        Schedule schedule = new Schedule();
        schedule.setId(1);
        user.setSchedule(schedule);

        em.persist(user);
        utx.commit();
        // reset the persistence context (cache)
        em.clear();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

//    @After
//    public void tearDown() throws Exception {
//        //remove user from GAAS
//        try {
//            gaasWebServiceInterface.deleteUser(user.getId());
//        } catch (GAASWebServiceFaultException e) {
//            log.info("code: " + e.getFaultInfo().getCode() + "\nmessage: ");
//            e.printStackTrace();
//        }
//        gaasWebService = null;
//        gaasWebServiceInterface = null;
//        gaasWebServiceEntitiesObjectFactory = null;
//        casWebService = null;
//        casWebServiceInterface = null;
//        casWebServiceEntitiesObjectFactory = null;
//    }

    @Given("^یک کاربر با شناسه ی ۱۲ در سامانه ی ۳s تعریف شده است$")
    public void یک_کاربر_با_شناسه_ی_۱۲_در_سامانه_ی_۳s_تعریف_شده_است() throws Throwable {
//         userId = gaasWebServiceInterface.addUser(user);
        log.info("userId" + user.getId());
    }

    @Given("^کاربر با شناسه ی ۱۲ غیرفعال است$")
    public void کاربر_با_شناسه_ی_۱۲_غیرفعال_است() throws Throwable {
        gaasWebServiceInterface.disableUser(user.getId());
        log.info("userId "+ userId);
    }

    @When("^کاربر در سامانه ی CCOS اقدام به login می نماید$")
    public void کاربر_در_سامانه_ی_CCOS_اقدام_به_login_می_نماید() throws Throwable {
        casWebService = new CASWebService();
        casWebServiceInterface = casWebService.getCASWebServiceImpl();
        casWebServiceEntitiesObjectFactory = new ObjectFactory();
        hashCode = MD5(user.getUsername() + "123456", "UTF-8");
    }

    @Then("^سیستم پیغام خطا نمایش می دهد$")
    public void سیستم_پیغام_خطا_نمایش_می_دهد() throws Throwable {
        Assert.fail(casWebServiceInterface.login(user.getUsername(), hashCode));
    }

    public static String MD5(String inputString, String encoding) {
        String md5Hash = null;
        byte[] md5HashBytes = null;
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");


            md5HashBytes = md.digest(inputString.getBytes("UTF-8"));
            if (encoding != null)
                md5Hash = Base64.encode(new String(md5HashBytes, encoding), "UTF-8");
            else
                md5Hash = Base64.encode(new String(md5HashBytes));

            //            md5Hash = convertToHex(md5HashBytes);
            // log.info(convertToHex(sha1hash).equals(byteArrayToHexString(sha1hash)) ? "Equal" : "Not Equal");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Hash;
    }


}
