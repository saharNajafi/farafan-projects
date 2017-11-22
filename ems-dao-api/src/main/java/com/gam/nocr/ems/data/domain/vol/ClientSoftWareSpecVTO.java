package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/15/17.
 */
public class ClientSoftWareSpecVTO extends ExtEntityTO {
    private String osVersion;
    private Boolean isDotNetwork45Installed;

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Boolean getDotNetwork45Installed() {
        return isDotNetwork45Installed;
    }

    public void setDotNetwork45Installed(Boolean dotNetwork45Installed) {
        isDotNetwork45Installed = dotNetwork45Installed;
    }
}
