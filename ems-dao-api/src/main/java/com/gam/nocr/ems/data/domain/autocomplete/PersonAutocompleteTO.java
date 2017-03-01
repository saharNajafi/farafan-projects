package com.gam.nocr.ems.data.domain.autocomplete;

import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class PersonAutocompleteTO extends EMSAutocompleteTO {
    private String phone;
    private String mobile;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
