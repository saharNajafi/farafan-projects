//package com.gam.nocr.ems.web;
//
//import com.gam.nocr.ems.biz.service.external.client.gaas.*;
//import cucumber.api.PendingException;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;
//import cucumber.runtime.arquillian.CukeSpace;
//import cucumber.runtime.arquillian.api.Features;
//import gampooya.tools.util.Base64;
//import org.jboss.arquillian.container.test.json.api.Deployment;
//import org.jboss.arquillian.core.api.annotation.Inject;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.EmptyAsset;
//import org.jboss.shrinkwrap.api.spec.JavaArchive;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//
//import java.security.MessageDigest;
//import java.util.Random;
//import java.util.logging.Logger;
//
///**
// * Created by sahar on 7/2/17.
// */
//
//@RunWith(CukeSpace.class)
//@Features({ "src/test.json/resources/feature/inactive_user.feature" })
//public class InactiveUserFeature {
//    GAASWebService gaasWebService;
//    GAASWebServiceInterface gaasWebServiceInterface;
//    ObjectFactory gaasWebServiceEntitiesObjectFactory;
//    User user;
//    CASWebService casWebService;
//    CASWebServiceInterface casWebServiceInterface;
////    ObjectFactory casWebServiceEntitiesObjectFactory;
//    Integer userId;
//    String hashCode;
//    private static Logger log = Logger.getLogger(InactiveUserFeature.class.getName());
//
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        gaasWebService = new GAASWebService();
//        gaasWebServiceInterface = gaasWebService.getGAASWebServiceImpl();
//        gaasWebServiceEntitiesObjectFactory = new ObjectFactory();
//        casWebService = new CASWebService();
//        casWebServiceInterface = casWebService.getCASWebServiceImpl();
//        casWebServiceEntitiesObjectFactory = new ObjectFactory();
//        userId = 123;
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        gaasWebService = null;
//        gaasWebServiceInterface = null;
//        gaasWebServiceEntitiesObjectFactory=null;
//        casWebService = null;
//        casWebServiceInterface = null;
//        casWebServiceEntitiesObjectFactory = null;
//    }
//
//    @Given("^یک کاربر با شناسه ی ۱۲۳ در سامانه ی ۳s تعریف شده است$")
//    public void یک_کاربر_با_شناسه_ی_۱۲۳_در_سامانه_ی_۳s_تعریف_شده_است() throws Throwable {
//         user = gaasWebServiceInterface.getUser(userId);
//    }
//
//    @Given("^کاربر با شناسه ی ۱۲۳ غیرفعال است$")
//    public void کاربر_با_شناسه_ی_۱۲۳_غیرفعال_است() throws Throwable {
//       Boolean enable = user.isEnabled();
//        log.info("USeeer "+ enable);
//    }
//
//    @When("^کاربر در سامانه ی CCOS اقدام به login می نماید$")
//    public void کاربر_در_سامانه_ی_CCOS_اقدام_به_login_می_نماید() throws Throwable {
//        hashCode = MD5(user.getUsername() + "23", "UTF-8");
//    }
//
//    @Then("^سیستم پیغام خطا نمایش می دهد$")
//    public void سیستم_پیغام_خطا_نمایش_می_دهد() throws Throwable {
//        try{
//       String ticket = casWebServiceInterface.login(user.getUsername(), hashCode);
//        log.info("*ticket: " + ticket);
//    } catch (CASWebServiceFaultException e) {
//        log.info("code: " + e.getFaultInfo().getCode() + "\nmessage: ");
//        e.printStackTrace();
//        Assert.fail();
//    }
//    Assert.assertTrue(true);
//    }
//
//    public static String MD5(String inputString, String encoding) {
//        String md5Hash = null;
//        byte[] md5HashBytes = null;
//        try {
//
//            MessageDigest md = MessageDigest.getInstance("MD5");
//
//
//            md5HashBytes = md.digest(inputString.getBytes("UTF-8"));
//            if (encoding != null)
//                md5Hash = Base64.encode(new String(md5HashBytes, encoding), "UTF-8");
//            else
//                md5Hash = Base64.encode(new String(md5HashBytes));
//
//            //            md5Hash = convertToHex(md5HashBytes);
//            // System.out.println(convertToHex(sha1hash).equals(byteArrayToHexString(sha1hash)) ? "Equal" : "Not Equal");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return md5Hash;
//    }
//
//
//}
