package com.gam.nocr.ems.data.domain.vol;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.util.EmsUtil;

import java.util.List;

/**
 * @author Haeri (haeri@gamelectronics.com)
 */
public class DocTypeVTO extends ExtEntityTO {

    private String docTypeName;
    //private List<Integer> serviceIds;
    private String services;

    public DocTypeVTO() {
    }

    public DocTypeVTO(Long id, String docTypeName, List<Integer> serviceIds) {
        this.setId(id);
        this.docTypeName = docTypeName;
        //this.serviceIds = serviceIds;
    }

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

	/*public List<Integer> getServiceIds() {
        return serviceIds;
	}

	public void setServiceIds(List<Integer> serviceIds) {
		this.serviceIds = serviceIds;
	}*/

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return EmsUtil.toJSON(this);
    }
}
