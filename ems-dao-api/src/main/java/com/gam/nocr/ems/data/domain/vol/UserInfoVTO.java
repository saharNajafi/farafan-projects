package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.EntityTO;
import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

import java.util.List;

/**
 * @author Soheil Toodeh Fallah (fallah@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class UserInfoVTO extends EntityTO {

    private int userId;
    private String username;
    private String password;
    private Long personId;
    private boolean enabled;
    private SchedulingVTO scheduling;
    private List<PermissionVTO> permissions;
    private List<RoleVTO> roles;
    private List<ValidIPVTO> validIps;

    public UserInfoVTO() {
    }

    public UserInfoVTO(PersonVTO personVTO) {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JSON(include = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @JSON(include = false)
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SchedulingVTO getScheduling() {
        return scheduling;
    }

    public void setScheduling(SchedulingVTO scheduling) {
        this.scheduling = scheduling;
    }

    public List<PermissionVTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionVTO> permissions) {
        this.permissions = permissions;
    }

    public List<RoleVTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVTO> roles) {
        this.roles = roles;
    }

    public List<ValidIPVTO> getValidIps() {
        return validIps;
    }

    public void setValidIps(List<ValidIPVTO> validIps) {
        this.validIps = validIps;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
