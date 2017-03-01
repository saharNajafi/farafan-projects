package com.gam.nocr.ems.data.domain.ws;

import com.gam.commons.stateprovider.StateProviderTO;

import java.io.Serializable;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class StateProviderWTO implements Serializable {

    private String stateId;
    private String value;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StateProviderWTO() {
        super();
    }

    public StateProviderWTO(StateProviderTO to) {
        if (to != null) {
            this.stateId = to.getStateId();
            this.value = to.getValue();
        }
    }
}
