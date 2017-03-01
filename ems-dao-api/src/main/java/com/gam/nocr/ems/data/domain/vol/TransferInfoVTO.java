package com.gam.nocr.ems.data.domain.vol;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

/**
 * The class TransferInfoVTO is used to encapsulate attributes which are needed in connection to IMS sub system
 */
public class TransferInfoVTO {

    private String requestId;
    private byte[] data;
    /**
     * By default it is assigned by zero
     */
    private int index;

    /**
     * It shows errors
     */
    private String errMessage;

    public TransferInfoVTO() {
    }

    public TransferInfoVTO(
            String requestId,
            int index,
            byte[] data) {
        this.requestId = requestId;
        this.index = index;
        this.data = data;
    }

    public TransferInfoVTO(
            String requestId,
            int index,
            String errMessage,
            byte[] data) {
        this.requestId = requestId;
        this.index = index;
        this.errMessage = errMessage;
        this.data = data;
    }
    
    public TransferInfoVTO(
            String errMessage,
            byte[] data) {
        this.errMessage = errMessage;
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public byte[] getData() {
        return data == null ? data : data.clone();
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}
