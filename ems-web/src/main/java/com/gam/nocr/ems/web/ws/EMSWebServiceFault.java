package com.gam.nocr.ems.web.ws;

import com.gam.nocr.ems.util.CcosBundle;

import javax.xml.bind.annotation.XmlType;
import java.text.MessageFormat;

/**
 * For exceptions that are generating in web service body (not those that cough by web service) this type of exception
 * will be used. For example, if a web service parameter is invalid, the given web service will throw an instance of
 * {@link com.gam.nocr.ems.web.ws.InternalException} created base on an instance of this class
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@XmlType(namespace = "http://ws.web.ems.nocr.gam.com/fault")
public class EMSWebServiceFault extends InternalInterfaceException {

    private static final String CCOS_ERROR_GENERAL = "CCOS_ERROR_GENERAL";
    public static final int MODE_LOG = 1;
    public static final int MODE_SHOW = 2;

    public EMSWebServiceFault() {
        super();
    }

    public EMSWebServiceFault(String code) {
        super(code);
        setMode(MODE_SHOW);
        setData(CcosBundle.getMessage(code));
        if (getData() == null || getData().trim().length() == 0) {
            setData(CcosBundle.getMessage(CCOS_ERROR_GENERAL));
        }
    }

    public EMSWebServiceFault(String code, Object[] params) {
        super(code);
        setMode(MODE_SHOW);
        setData(MessageFormat.format(CcosBundle.getMessage(code), params));
        if (getData() == null || getData().trim().length() == 0) {
            setData(CcosBundle.getMessage(CCOS_ERROR_GENERAL));
        }
    }

    public EMSWebServiceFault(String code, int mode) {
        super(code, mode);
        setData(CcosBundle.getMessage(code));
        if (getData() == null || getData().trim().length() == 0) {
            setData(CcosBundle.getMessage(CCOS_ERROR_GENERAL));
        }
    }

    public EMSWebServiceFault(String code, int mode, String data) {
        super(code, mode, data);
        if (getData() == null || getData().trim().length() == 0) {
            setData(CcosBundle.getMessage(CCOS_ERROR_GENERAL));
        }
    }
}
