package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.EntityTO;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * @author: Soheil Toodeh Fallah (fallah@gamelectronics.com)
 */
public class PermissionVTO extends EntityTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
