package com.gam.nocr.ems.data.domain.vol;

import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class PersonInfoVTO {

    private PersonTO personTO;
    private UserInfoVTO userInfoVTO;

    public PersonInfoVTO() {
    }

    public PersonInfoVTO(PersonTO personTO, UserInfoVTO userInfoVTO) {
        this.personTO = personTO;
        this.userInfoVTO = userInfoVTO;
    }

    public PersonTO getPersonTO() {
        return personTO;
    }

    public void setPersonTO(PersonTO personTO) {
        this.personTO = personTO;
    }

    public UserInfoVTO getUserInfoVTO() {
        return userInfoVTO;
    }

    public void setUserInfoVTO(UserInfoVTO userInfoVTO) {
        this.userInfoVTO = userInfoVTO;
    }

    @Override
    public String toString() {
//		return "PersonInfoVTO: {" +
//				"personTO = " + getPersonTO() +
//				", userInfoVTO = " + getUserInfoVTO() +
//				"}";
        return EmsUtil.toJSON(this);
    }
}
