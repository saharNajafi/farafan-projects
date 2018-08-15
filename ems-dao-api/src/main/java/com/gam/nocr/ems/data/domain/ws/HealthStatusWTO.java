package com.gam.nocr.ems.data.domain.ws;


import com.gam.nocr.ems.data.enums.YesNoEnum;

import java.io.Serializable;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 1/1/18.
 */
public class HealthStatusWTO implements Serializable {
    private YesNoEnum pupilIsVisible;
    private YesNoEnum climbingStairsAbility;
    private YesNoEnum abilityToGo;
    private YesNoEnum hasTwoFingerScanable;

    public YesNoEnum getPupilIsVisible() {
        return pupilIsVisible;
    }

    public void setPupilIsVisible(YesNoEnum pupilIsVisible) {
        this.pupilIsVisible = pupilIsVisible;
    }

    public YesNoEnum getClimbingStairsAbility() {
        return climbingStairsAbility;
    }

    public void setClimbingStairsAbility(YesNoEnum climbingStairsAbility) {
        this.climbingStairsAbility = climbingStairsAbility;
    }

    public YesNoEnum getAbilityToGo() {
        return abilityToGo;
    }

    public void setAbilityToGo(YesNoEnum abilityToGo) {
        this.abilityToGo = abilityToGo;
    }

    public YesNoEnum getHasTwoFingerScanable() {
        return hasTwoFingerScanable;
    }

    public void setHasTwoFingerScanable(YesNoEnum hasTwoFingerScanable) {
        this.hasTwoFingerScanable = hasTwoFingerScanable;
    }
}
