
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.util.Date;

import gampooya.tools.date.DateUtil;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.fill.JRFillVariable;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;

/**
 * The class 'Scriptlet' is associated to the reports, which are produced by using jasper report.
 * As a result of a bug in jasper report, there is no way to put this class in an appropriate package.
 */

public class Scriptlet extends JRDefaultScriptlet {
    private static final Logger logger = BaseLog.getLogger(Scriptlet.class);
    static Unicode un = null;

    public void beforeReportInit()
            throws JRScriptletException {
        logger.info("call beforeReportInit");
    }

    public void afterReportInit()
            throws JRScriptletException {
        logger.info("call afterReportInit");
    }

    public void beforePageInit()
            throws JRScriptletException {
        logger.info("call   beforePageInit : PAGE_NUMBER = " + getVariableValue("PAGE_NUMBER"));
    }

    public void afterPageInit()
            throws JRScriptletException {
        logger.info("call   afterPageInit  : PAGE_NUMBER = " + getVariableValue("PAGE_NUMBER"));
    }

    public void beforeColumnInit()
            throws JRScriptletException {
        logger.info("call     beforeColumnInit");
    }

    public void afterColumnInit()
            throws JRScriptletException {
        logger.info("call     afterColumnInit");
    }

    public void beforeGroupInit(String groupName)
            throws JRScriptletException {
        if (groupName.equals("CityGroup"))
            logger.info("call       beforeGroupInit : City = " + getFieldValue("City"));
    }

    public void afterGroupInit(String groupName)
            throws JRScriptletException {
        if (groupName.equals("CityGroup")) {
            logger.info("call       afterGroupInit  : City = " + getFieldValue("City"));

            String allCities = (String) getVariableValue("AllCities");
            String city = (String) getFieldValue("City");
            StringBuffer sbuffer = new StringBuffer();

            if (allCities != null) {
                sbuffer.append(allCities);
                sbuffer.append(", ");
            }

            sbuffer.append(city);
            setVariableValue("AllCities", sbuffer.toString());
        }
    }

    public void beforeDetailEval()
            throws JRScriptletException {
        logger.info("        detail");
    }

    public void afterDetailEval()
            throws JRScriptletException {
    }

    public static String toPersianNumeric(String text)
            throws JRScriptletException {
        un = new Unicode();

        return text == null ? "" : un.toUnicode(text);
    }

    public static String toPersianNumericLRE(String text) throws JRScriptletException {
        un = new Unicode();

        return "‭" + un.toUnicode(text);
    }

    public static String toPersianNumericRLE(String text)
            throws JRScriptletException {
        un = new Unicode();

        return "‮" + un.toUnicode(text);
    }

    public static String toPersianDate(Date date) {
        return date == null ? "" : DateUtil.getDateTime(date).substring(0, 11);
    }

    public static String toPersianDate(String strDate) {
        try {
            return strDate == null || strDate.isEmpty() ? "" : toPersianDate(DateUtil.convert(strDate, DateUtil.GREGORIAN));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    public static String toStandardTime(long duration) {
        return DateUtil.getTimeFromSecond(duration).toString();
    }

    public static String showClobField(Clob field) {
        BufferedReader bufferedData = null;
        try {
            Reader valueAsClobStream = field.getCharacterStream();
            bufferedData = new BufferedReader(valueAsClobStream);

            StringBuffer allValue = new StringBuffer();
            String line;
            while ((line = bufferedData.readLine()) != null) {
                allValue.append(line);
            }

            return allValue.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (bufferedData != null)
                    bufferedData.close();
            } catch (IOException e) {
                logger.info(e.getMessage(), e);
            }
        }
        return "exception";
    }

    public String getSerialNo()
            throws JRScriptletException {
        return "kkkk";
    }

    public static JRFillVariable increment(JRFillVariable var) {
        Integer value = (Integer) var.getValue();
        var.setValue(value + 1);

        return var;
    }
}