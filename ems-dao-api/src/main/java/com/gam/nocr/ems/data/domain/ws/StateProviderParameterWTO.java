package com.gam.nocr.ems.data.domain.ws;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class StateProviderParameterWTO {
    private String moduleName;
    private StateProviderWTO[] stateProviderWTOs;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public StateProviderWTO[] getStateProviderWTOs() {
        return stateProviderWTOs == null ? stateProviderWTOs : stateProviderWTOs.clone();
    }

    public void setStateProviderWTOs(StateProviderWTO[] stateProviderWTOs) {
        this.stateProviderWTOs = stateProviderWTOs;
    }
}
