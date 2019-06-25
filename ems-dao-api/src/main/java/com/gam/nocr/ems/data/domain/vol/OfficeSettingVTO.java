package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/5/18.
 */
public class OfficeSettingVTO extends ExtEntityTO{

    private Long feiN;
    private Long feiCC;
    private Long feiISOCC;

    public Long getFeiN() {
        return feiN;
    }

    public void setFeiN(Long feiN) {
        this.feiN = feiN;
    }

    public Long getFeiCC() {
        return feiCC;
    }

    public void setFeiCC(Long feiCC) {
        this.feiCC = feiCC;
    }

    public Long getFeiISOCC() {
        return feiISOCC;
    }

    public void setFeiISOCC(Long feiISOCC) {
        this.feiISOCC = feiISOCC;
    }
}
