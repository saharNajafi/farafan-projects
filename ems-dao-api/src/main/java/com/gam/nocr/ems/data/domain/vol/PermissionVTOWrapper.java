package com.gam.nocr.ems.data.domain.vol;

import java.util.List;

public class PermissionVTOWrapper {

	private List<PermissionVTO> permissions;
	private Integer count;

	public PermissionVTOWrapper() {
		// TODO Auto-generated constructor stub
	}

	public PermissionVTOWrapper(List<PermissionVTO> permissions, Integer count) {
		super();
		this.permissions = permissions;
		this.count = count;
	}

	public List<PermissionVTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionVTO> permissions) {
		this.permissions = permissions;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
