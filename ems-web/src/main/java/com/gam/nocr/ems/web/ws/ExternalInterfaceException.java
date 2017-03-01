package com.gam.nocr.ems.web.ws;

import javax.xml.ws.WebFault;

/**
 * Exception class that would be used on web services exposed to external systems (e.g. CMS)
 *
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@WebFault
public class ExternalInterfaceException extends Exception {

    private static final long serialVersionUID = -8067881221319593965L;
    private String errorCode;
    private String reasonMessage;

    public ExternalInterfaceException() {
        super();
        reasonMessage = "unknown";
    }

    public ExternalInterfaceException(String err) {
        super(err);
        reasonMessage = err;
    }

    public ExternalInterfaceException(String code,
                                      String message) {
        super(message);
        this.errorCode = code;
        reasonMessage = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getReasonMessage() {
        return reasonMessage;
    }

    public void setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

    @Override
    public String toString() {
        return "ExternalInterfaceException [errorCode=" + errorCode
                + ", reasonMessage=" + reasonMessage + "]";
    }

}
