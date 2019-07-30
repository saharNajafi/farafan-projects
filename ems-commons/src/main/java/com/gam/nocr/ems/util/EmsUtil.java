package com.gam.nocr.ems.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.Stub;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.BaseLog;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.commons.profile.ProfileManager;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.ProfileHelper;
import com.gam.nocr.ems.config.ProfileKeyName;
import com.sun.xml.ws.client.BindingProviderProperties;

import flexjson.JSONSerializer;

/**
 * Collection of utility classes used throughout the project
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EmsUtil {

    private static final Logger logger = BaseLog.getLogger(EmsUtil.class);

    /**
     * Default web service timeout configurations in millisecond
     */
    private static final String DEFAULT_WEBSERVICE_TIMEOUT = "30000";

    private static DataSource dataSource;

    /**
     * Regular expression to validate birth certificate serial
     */
    public static String birthCertSerialConstraint = "[1-9][0-9][0-9][0-9][0-9][0-9]";

    /**
     * Regular expression to validation Persian characters
     */
    public static String persianAlphaConstraint = "[\u0621-\u063A\u0641-\u064A\u0674\u067E\u0686\u0698\u06A9\u06AF\u06CC\u0020\uFEFF]+";

    /**
     * Regular expression to validate latin characters
     */
    public static String latinAlphaConstraint = "[a-zA-Z ]+";

    /**
     * Regular expression to validate numbers
     */
    public static String numberConstraint = "[0-9]+";

    /**
     * Regular expression to validate national id
     */
    public static String nationalIdConstraint = "^[0-9]{10}$";

    /**
     * Regular expression to validate cell phone numbers
     */
    public static String cellNoConstraint = "^09\\d{9}$";

    /**
     * Regular expression to validate box identifier
     */
    public static String CMS_BOX_ID_CONSTRAINT = "^[0-9]{32}$";

    /**
     * Given an array of objects and returns true if it's not null and has some elements in it, and false otherwise
     * @param objects    An array of object to check its contents
     * @return  true if given array is not null and has some elements in it, and false otherwise
     */
    public static boolean checkArraySize(Object[] objects) {
        return objects != null && objects.length != 0;
    }

    /**
     * Given an {@link java.util.List} of objects and returns true if it's not null and has some elements in it, and
     * false otherwise
     *
     * @param list    A list of object to check its contents
     * @return  true if given list is not null and has some elements in it, and false otherwise
     */
    public static boolean checkListSize(List list) {
        return list != null && list.size() != 0;
    }

    /**
     * Given an {@link java.util.Map} and returns true if it's not null and has some elements in it, and false otherwise
     *
     * @param map    A map object to check its contents
     * @return  true if given map is not null and has some elements in it, and false otherwise
     */
    public static boolean checkMapSize(Map map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Given a {@link java.lang.String} and returns true if it's not null and its length is non-zero and false otherwise
     *
     * @param param   A String object to check its contents
     * @return  true if given String is not null and has some characters in it, and false otherwise
     */
    public static boolean checkString(String param) {
        return param != null && param.trim().length() != 0;
    }

    /**
     * Given a list of objects and converts it to a String by calling {@link Object#toString()} method of each element
     *
     * @param list    List of objects to be converted to String
     * @return  String representation of given list comprised of String representation of each item in it
     */
    public static String toStringList(List list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        String result = "";
        for (Object o : list) {
            result += "," + o.toString();
        }
        result = "[" + result.substring(1) + "]";// removes first comma

        return result;
    }

    /**
     * Calculates MD5 digest of given String input
     *
     * @param input A String object to calculate its MD5 digest
     * @return  MD5 digest of given String
     */
    public static String MD5Digest(String input) {
        byte[] originalMessage;
        byte[] digestMessage;

        try {
            originalMessage = input.getBytes("UTF-8");
            MessageDigest digestEngine = MessageDigest.getInstance("MD5");

            digestMessage = digestEngine.digest(originalMessage);

            return new String(digestMessage, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            return null;
        } catch (Exception e) {
            logger.error(BizExceptionCode.GLB_ERR_MSG, e);
            return null;
        }
    }

    /**
     * Generates a random 10 digit tracking number using given seed String
     *
     * @param seed  The seed String to be used for generating a random tracking number
     * @return a random tracking number
     */
    public static String generateTrackingId(String seed) {
        Long hashResult = PJWHash(seed + new Date().getTime());

        Random rn = new Random(System.nanoTime());
        Long numberOfStates = 10L;
        Long i = Math.abs(rn.nextInt() % numberOfStates);
        String stringHashResult = String.valueOf(hashResult);
        stringHashResult = StringUtils.leftPad(stringHashResult, 9, "0");

        return stringHashResult + i;
    }

    /**
     * Given a String parameter and returns a hash number base on PJW algorithm
     *
     * @param str   String to generate a hash value
     * @return  Hash value of given parameter
     */
    public static Long PJWHash(String str) {
        long BitsInUnsignedInt = (long) (4 * 8);
        long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
        long OneEighth = (long) (BitsInUnsignedInt / 8);
        long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        long hash = 0;
        long test = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << OneEighth) + str.charAt(i);

            if ((test = hash & HighBits) != 0) {
                hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
            }
        }

        return hash;
    }

    /**
     * Validates the length of given string with the maximum valid value specified as another parameter. It's mainly
     * used in validation of input fields
     *
     * @param param The String to validate its length
     * @param maxLength Maximum valid length of input String
     * @return  True if the length of given String is less than or equals to 'maxLengt' parameter, false otherwise
     */
    public static boolean checkMaxFieldLength(Object param, int maxLength) {
        return param.toString().trim().length() <= maxLength;
    }

    /**
     * Given a {@link java.util.Date} value, adds number of days to it (specified as 'value' parameter) and returns a
     * new {@link java.util.Date}
     *
     * @param date     The date object to add some days to it
     * @param value     Number of days to be added to the date
     * @return  Newly calculated date
     */
    public static Date differDay(Date date, Integer value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }
    
    
    //Anbari
    public static Date differYear(Date date, Integer value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, value);
        return cal.getTime();
    }


    /**
     * Given a {@link java.util.Date} value, adds number of hours to it (specified as 'value' parameter) and returns a
     * new {@link java.util.Date}
     *
     * @param date     The date object to add some hours to it
     * @param value     Number of hours to be added to the date
     * @return  Newly calculated date
     */
    public static Date differHour(Date date, Integer value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, value);
        return cal.getTime();
    }

    /**
     * Checks a String object against given regular expression
     * @param param    The String value to be validated by regular expression
     * @param regex    Regular expression pattern to validate the String
     * @return  true if the String matches given regular expression and false in other cases
     */
    public static boolean checkRegex(String param, String regex) {
        return param.matches(regex);
    }

    /**
     * Given a date value (in String) and append the time value "00:00" to it to represent the immediate start of a day.
     * If given String is null or has no contents at all, returns "1970/01/01 00:00"
     *
     * @param fromDate  String representation of a date value (e.g. 1972/03/01)
     * @return  Given date value with the "00:00" string appended to it (e.g. 1972/03/01 00:00)
     */
    public static String completeFromDate(String fromDate) {
        if (fromDate == null || fromDate.trim().length() == 0)
            return "1970/01/01 00:00";
        else
            return fromDate + " 00:00";
    }

    /**
     * Given a date value (in String) and append the time value "23:59" to it to represent the last minute of a day.
     * If given String is null or has no contents at all, returns "2200/01/01 23:59"
     *
     * @param toDate  String representation of a date value (e.g. 1972/03/01)
     * @return  Given date value with the "23:59" string appended to it (e.g. 1972/03/01 23:59)
     */    public static String completeToDate(String toDate) {
        if (toDate == null || toDate.trim().length() == 0)
            return "2200/01/01 23:59";
        else
            return toDate + " 23:59";
    }

    /**
     * Validate both parameters against null or empty string and returns false if any of them are null or empty
     *
     * @param fromDate  The start date value to validate
     * @param toDate    The end date value to validate
     * @return  false if any of parameters are null or empty and true otherwise
     */
    public static boolean checkFromAndToDate(String fromDate, String toDate) {
        return ((fromDate != null && fromDate.trim().length() != 0) || (toDate != null && toDate.trim().length() != 0));
    }

    /**
     * The method convertByteArrayToXMLString is used to convert an instance of type byte[] to XML string data
     *
     * @param data a primitive value of type {@link byte[]}
     * @return an instance of type {@link String}
     * @throws BaseException
     */
    public static String convertByteArrayToXMLString(byte[] data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        InputStream inputStream = new ByteArrayInputStream(data);
        try {
            Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream);
            return convertDocumentToString(doc);
        } catch (Exception e) {
            throw new BaseException("Error was occurred in converting a data of type 'byte[]' to XML String.", e);
        }
    }

    /**
     * The method convertDocumentToString is used to convert an instance of type {@link Document} to an instance of type
     * {@link String}
     *
     * @param doc an instance of type {@link Document}
     * @return an instance of type {@link String}
     * @throws BaseException
     */
    public static String convertDocumentToString(Document doc) throws Exception {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (Exception e) {
            throw new BaseException("Error was occurred in converting a data of type 'Document' to String.", e);
        }
    }

    /**
     * The method convertByteArrayToString is used to convert a byte array data to an instance of type {@link String}
     *
     * @param data an array of type {@link byte}
     * @return an instance of type {@link String}
     * @throws BaseException
     */
    public static String convertByteArrayToString(byte[] data) throws BaseException {
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("Error was occurred in converting a data of type 'byte[]' to String.", e);
        }
    }

    /**
     * The method createRequestIdForMessage is used to create a requestId for the external requests.
     *
     * @param requestId     is an instance of type {@link String} which represents a unique request id. This parameter can
     *                      be fetch by using sequence
     * @param requestLength is an integer variable which represents the whole length of the required requestId
     * @param prefix        is an instance of type {@link String} which represents the prefix of the requestId
     * @param leftPadString is an instance of type {@link String} which represents the left padding of the requestId
     * @return an instance of type {@link String} which represents the desired requestId
     * @throws BaseException
     */
    public static String createRequestIdForMessage(String requestId,
                                                   int requestLength,
                                                   String prefix,
                                                   String leftPadString) throws BaseException {

        int leftPadSize = requestLength - prefix.length();
        return prefix + StringUtils.leftPad(requestId, leftPadSize, leftPadString);

    }

    /**
     * Given a profile key and tries to look it up using {@link com.gam.commons.profile.ProfileManager}. If no value
     * exists for given key returns the default value specified as second parameter
     *
     * @param profileKey    The profile key to lookup in system configurations
     * @param profileDefaultValue   The default value to return in cases that it doesn't found anything
     * @return  The value corresponding to given profile key or the default value if nothing found
     */
    public static String getProfileValue(String profileKey, String profileDefaultValue) {
        try {
            ProfileManager pm = ProfileHelper.getProfileManager();
            String profileValue = (String) pm.getProfile(profileKey, true, null, null);

            if (!checkString(profileValue))
                return profileDefaultValue;

            return profileValue;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return profileDefaultValue;
        }
    }

    /**
     * Create Random Payment Order ID
     * @return Payment OrderID
     */
    public static Long getRandomPaymentOrderId() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    /**
     * Returns the current date that its time related fields are filled with zero (e.g. 2014/12/2T00:00:00:000)
     *
     * @return  current date that its time related fields are filled with zero (e.g. 2014/12/2T00:00:00:000)
     * @throws BaseException
     */
    public static Date getToday() throws BaseException {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * The method convertStringToJSONFormat is used to convert an instance of type {@link Object}, to an instance of type
     * {@link String} to build a JSON string
     *
     * @param objectName is an instance of type {@link String}, which represents the name of object in JSON String
     * @param map        is an instance of type{@link Map<Object, Object>}, which involves the fields that are the
     *                   attributes in returned JSON String
     * @return an instance of type {@link String}, which represents the generated JSON String
     */
    public static String convertStringToJSONFormat(String objectName,
                                                   Map<Object, Object> map) {
        new JSONObject(map);
        String returnValue = "{" + objectName + ":{";
        Object[] keys = map.keySet().toArray();
        for (Object key : keys) {
            returnValue = returnValue.concat(key.toString() + ":" + map.get(key) + ", ");
        }
        returnValue = returnValue.concat("}}");

        return returnValue;
    }

    /**
     * The method toJSON is used to convert an instance of type {@link Object}, to an instance of {@link String} to
     * build a JSON String
     *
     * @param object is an instance of type {@link Object}
     * @return an instance of type {@link String}, which represents the generated JSON String
     */
    public static String toJSON(Object object) {
        JSONSerializer jsonSerializer = new JSONSerializer();
        jsonSerializer.exclude("clientId", "entityState");
        return jsonSerializer.serialize(object);
    }

    /**
     * Given a {@link java.util.Date} value and returns a new result that its time related fields are filled with zero
     * (e.g. 2014/12/2T00:00:00:000)
     *
     * @param date  the date value to fill its time related fields with zero
     * @return  a new date that its time related fields are filled with zero (e.g. 2014/12/2T00:00:00:000)
     * @throws BaseException
     */
    public static Date getDateAtMidnight(Date date) throws BaseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Given a JNDI name of a data source, look it up in JNDI tree and returns a new {@link java.sql.Connection} created
     * using given data source
     *
     * @param jndiDataSourceName    The JNDI name of a data source to lookup
     * @return  A new connection created from given data source
     * @throws BaseException
     */
    public static Connection getConnectionByDataSource(String jndiDataSourceName) throws BaseException {
        Connection connection;
        Context context;

        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup(jndiDataSourceName);
            connection = dataSource.getConnection();
        } catch (Exception e) {
            throw new ServiceException("Exception was happened in looking up data source by ' " + jndiDataSourceName + " ' as JNDI name.", e);
        }
        return connection;
    }

    /**
     * Converts a JSON string to a {@link java.util.Map} by copying all properties as a keys of the map and their
     * corresponding values as the value
     *
     * @param jsonString    JSON String to be converted to a {@link java.util.Map}
     * @return  A map representation of given JSON string
     * @throws BaseException
     */
    public static Map convertJsonToMap(String jsonString) throws BaseException {
        try {
            Map result = new HashMap<Object, Object>();
            JSONArray parameters = new JSONArray(jsonString);
            for (int i = 0; i < parameters.length(); i++) {
                JSONObject param = parameters.getJSONObject(i);
                result.put(param.getString("name"), param.getString("value"));
            }

            return result;
        } catch (Exception e) {
            throw new ServiceException("Exception was happened in converting JSON String to Map", e);
        }
    }

    /**
     * Converts given {@link java.util.Map} to a {@link org.json.JSONObject}
     *
     * @param map   A map to be converted to a JSON
     * @return  {@link org.json.JSONObject} representation of given {@link java.util.Map}
     * @throws BaseException
     */
    public static JSONObject toJSONObject(Map<Object, Object> map) throws BaseException {
        try {
            return new JSONObject(map);
        } catch (Exception e) {
            throw new ServiceException("Exception was happened in converting Map to JSONObject", e);
        }
    }

    /**
     * Appends the list of {@link org.json.JSONObject} to given {@link org.json.JSONArray}
     *
     * @param jsonArray The array to append JSON objects to it
     * @param jsonObjects   List of {@link org.json.JSONObject} to be appended to given JSONArray
     * @return  The JSONArray that all JSONObjects in given list are appended to it
     * @throws BaseException
     */
    public static JSONArray attachToJSONArray(JSONArray jsonArray, List<JSONObject> jsonObjects) throws BaseException {
        try {
            if (jsonArray == null) {
                throw new ServiceException("EmsUtil Exception : ", "The parameter of type 'JSONArray' is null.");
            }
            if (!checkListSize(jsonObjects)) {
                throw new ServiceException("EmsUtil Exception : ", "The parameter of type 'JSONObject' is null.");
            }
            for (JSONObject jsonObject : jsonObjects) {
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("EmsUtil Exception : ", "Unhandled exception was happened in attaching a list of type 'JSONObject' to an instance of type 'JSONArray'.");

        }
    }

    /**
     * Given a JavaScript source as a String and tries to run it in a context filled with given parameters. It's used
     * for validating report requests come from CCOS in order to run the validation function specified in Jasper file
     * as a JavaScript
     *
     * @param source    The JavaScript source to evaluate
     * @param params    Collection of parameters that needs to be used to fill the context of the script
     * @return  A hash map representing the results of executing given JavaScript
     * @throws BaseException
     */
    public static HashMap<String, String> executeJS(String source, Object params) throws BaseException {
        org.mozilla.javascript.Context context = org.mozilla.javascript.Context.enter();

        source = "(" + source + ")(" + params + ");";
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            Scriptable scope = context.initStandardObjects();
            Object evaluateString = context.evaluateString(scope, source, "script", 1, null);
            NativeObject nativeObject = (NativeObject) evaluateString;

            for (Object key : nativeObject.keySet())
                result.put(key.toString(), nativeObject.get(key).toString());

        } catch (Exception e) {
            logger.error("error", e);
        } finally {
            org.mozilla.javascript.Context.exit();
        }

        return result;
    }

    /**
     * This method is responsible for setting timeout properties while a JAX-RPC webservice call is needed
     *
     * @param webservicePort is an object which represents the appropriate port for calling webservice methods
     * @throws com.gam.commons.core.BaseException
     */
    public static void setJAXRPCWebserviceProperties(Object webservicePort, String wsdl) throws BaseException {
        try {
            String endPoint = wsdl.split("\\?")[0];
            Integer webserviceTimeout = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_WEBSERVICE_TIMEOUT, DEFAULT_WEBSERVICE_TIMEOUT));
            Stub localStub = ((Stub) webservicePort);
            localStub._setProperty("weblogic.wsee.transport.read.timeout", webserviceTimeout);
            localStub._setProperty("weblogic.wsee.transport.connection.timeout", webserviceTimeout);
//            localStub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, endPoint);
        } catch (WebServiceException e) {
            throw new BaseException("EmsUtil Exception. An exception has happened in setting webservice properties.", e);
        } catch (Exception e) {
            throw new BaseException("EmsUtil Exception. An exception has happened in setting webservice properties.", e);
        }
    }

    /**
     * This method is responsible for setting timeout properties while a JAX-WS webservice call is needed
     *
     * @param webservicePort is an object which represents the appropriate port for calling webservice methods
     * @throws com.gam.commons.core.BaseException
     */
    public static void setJAXWSWebserviceProperties(Object webservicePort, String wsdl) throws BaseException {
        try {
            String endPoint = wsdl.split("\\?")[0];
            Integer webserviceTimeout = Integer.valueOf(EmsUtil.getProfileValue(ProfileKeyName.KEY_WEBSERVICE_TIMEOUT, DEFAULT_WEBSERVICE_TIMEOUT));
            BindingProvider bindingProvider = (BindingProvider) webservicePort;
            Map<String, Object> context = bindingProvider.getRequestContext();
            context.put(BindingProviderProperties.CONNECT_TIMEOUT, webserviceTimeout);
            context.put(BindingProviderProperties.REQUEST_TIMEOUT, webserviceTimeout);
//            ((javax.xml.ws.BindingProvider)webservicePort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);
        } catch (WebServiceException e) {
            throw new BaseException("EmsUtil Exception. An exception has happened in setting webservice timeout properties.", e);
        } catch (Exception e) {
            throw new BaseException("EmsUtil Exception. An exception has happened in setting webservice timeout properties.", e);
        }
    }

	public static String convertLongNidToString(long nin) {
		String nationalId = "" + nin;
		while (nationalId.length() < 10){
			nationalId = "0" + nationalId ;
		}
		return nationalId;
	}
	
	public static String getUserInfo(UserProfileTO userProfile){
		if (userProfile == null){
			return null;
		}
		return userProfile.getUserName();
	}

    public static String makeFixLengthWithZeroPadding(String input, int length) {
        if (StringUtils.isEmpty(input)) {
            input = "";
        }
        if (input.length() >= length) {
            return input.substring(0, length - 1);
        } else {
            return String.format("%0" + length + "d", Long.valueOf(input));
        }
    }

    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
