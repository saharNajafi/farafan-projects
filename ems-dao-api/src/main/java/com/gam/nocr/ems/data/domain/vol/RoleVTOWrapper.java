package com.gam.nocr.ems.data.domain.vol;

import java.util.List;

public class RoleVTOWrapper {

	private List<RoleVTO> roles;
	private Integer count;

	public RoleVTOWrapper() {
		// TODO Auto-generated constructor stub
	}

	public RoleVTOWrapper(List<RoleVTO> roles, Integer count) {
		super();
		this.roles = roles;
		this.count = count;
	}

	public List<RoleVTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVTO> roles) {
		this.roles = roles;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
