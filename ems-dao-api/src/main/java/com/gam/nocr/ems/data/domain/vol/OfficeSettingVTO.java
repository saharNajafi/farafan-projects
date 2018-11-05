package com.gam.nocr.ems.data.domain.vol;


import com.gam.commons.core.data.domain.ExtEntityTO;


/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 10/15/18.
 */
public class OfficeSettingVTO extends ExtEntityTO {

    private Long id;
    private Long feiCC;
    private Long feiN;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeiCC() {
        return feiCC;
    }

    public void setFeiCC(Long feiCC) {
        this.feiCC = feiCC;
    }

    public Long getFeiN() {
        return feiN;
    }

    public void setFeiN(Long feiN) {
        this.feiN = feiN;
    }
}
