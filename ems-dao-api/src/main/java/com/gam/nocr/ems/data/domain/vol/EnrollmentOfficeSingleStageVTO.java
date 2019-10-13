package com.gam.nocr.ems.data.domain.vol;


import com.gam.commons.core.data.domain.ExtEntityTO;

import java.io.Serializable;

public class EnrollmentOfficeSingleStageVTO extends ExtEntityTO implements Serializable {

    private Boolean EOF_IGNORE_ICAO_PERMITTED;
    private Boolean EOF_HAS_STAIR;
    private Boolean EOF_HAS_ELEVATOR;
    private Boolean EOF_HAS_PORTABILITY_EQUIPMENT;
    private Boolean EOF_DEFINE_NMOC_PERMITTED;
    private Boolean EOF_IS_ACTIVE;

    public EnrollmentOfficeSingleStageVTO() {

    }

    public EnrollmentOfficeSingleStageVTO(Boolean EOF_IGNORE_ICAO_PERMITTED,
                                          Boolean EOF_HAS_STAIR,
                                          Boolean EOF_HAS_ELEVATOR,
                                          Boolean EOF_HAS_PORTABILITY_EQUIPMENT,
                                          Boolean EOF_DEFINE_NMOC_PERMITTED,
                                          Boolean EOF_IS_ACTIVE) {
        this.EOF_IGNORE_ICAO_PERMITTED = EOF_IGNORE_ICAO_PERMITTED;
        this.EOF_HAS_STAIR = EOF_HAS_STAIR;
        this.EOF_HAS_ELEVATOR = EOF_HAS_ELEVATOR;
        this.EOF_HAS_PORTABILITY_EQUIPMENT = EOF_HAS_PORTABILITY_EQUIPMENT;
        this.EOF_DEFINE_NMOC_PERMITTED = EOF_DEFINE_NMOC_PERMITTED;
        this.EOF_IS_ACTIVE = EOF_IS_ACTIVE;
    }

    //region getter and setter
    public Boolean getEOF_IGNORE_ICAO_PERMITTED() {
        return EOF_IGNORE_ICAO_PERMITTED;
    }

    public void setEOF_IGNORE_ICAO_PERMITTED(Boolean EOF_IGNORE_ICAO_PERMITTED) {
        this.EOF_IGNORE_ICAO_PERMITTED = EOF_IGNORE_ICAO_PERMITTED;
    }

    public Boolean getEOF_HAS_STAIR() {
        return EOF_HAS_STAIR;
    }

    public void setEOF_HAS_STAIR(Boolean EOF_HAS_STAIR) {
        this.EOF_HAS_STAIR = EOF_HAS_STAIR;
    }

    public Boolean getEOF_HAS_ELEVATOR() {
        return EOF_HAS_ELEVATOR;
    }

    public void setEOF_HAS_ELEVATOR(Boolean EOF_HAS_ELEVATOR) {
        this.EOF_HAS_ELEVATOR = EOF_HAS_ELEVATOR;
    }

    public Boolean getEOF_HAS_PORTABILITY_EQUIPMENT() {
        return EOF_HAS_PORTABILITY_EQUIPMENT;
    }

    public void setEOF_HAS_PORTABILITY_EQUIPMENT(Boolean EOF_HAS_PORTABILITY_EQUIPMENT) {
        this.EOF_HAS_PORTABILITY_EQUIPMENT = EOF_HAS_PORTABILITY_EQUIPMENT;
    }

    public Boolean getEOF_DEFINE_NMOC_PERMITTED() {
        return EOF_DEFINE_NMOC_PERMITTED;
    }

    public void setEOF_DEFINE_NMOC_PERMITTED(Boolean EOF_DEFINE_NMOC_PERMITTED) {
        this.EOF_DEFINE_NMOC_PERMITTED = EOF_DEFINE_NMOC_PERMITTED;
    }

    public Boolean getEOF_IS_ACTIVE() {
        return EOF_IS_ACTIVE;
    }

    public void setEOF_IS_ACTIVE(Boolean EOF_IS_ACTIVE) {
        this.EOF_IS_ACTIVE = EOF_IS_ACTIVE;
    }
    //endregion


}
