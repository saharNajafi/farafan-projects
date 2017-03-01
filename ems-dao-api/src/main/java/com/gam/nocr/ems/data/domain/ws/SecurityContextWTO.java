package com.gam.nocr.ems.data.domain.ws;

import com.gam.nocr.ems.util.EmsUtil;
import flexjson.JSON;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public class SecurityContextWTO {
    private String username;
    private String ticket;
    private String workstationID;
    private String ccosVersion;    

	public SecurityContextWTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JSON(include = false)
    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getWorkstationID() {
        return workstationID;
    }

    public void setWorkstationID(String workstationID) {
        this.workstationID = workstationID;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }

	public String getCcosVersion() {
		return ccosVersion;
	}

	public void setCcosVersion(String ccosVersion) {
		this.ccosVersion = ccosVersion;
	}
}
