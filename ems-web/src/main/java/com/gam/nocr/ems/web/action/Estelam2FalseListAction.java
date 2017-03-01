package com.gam.nocr.ems.web.action;

import java.util.List;

import com.gam.commons.core.web.struts2.extJsController.ListControllerImpl;
import com.gam.nocr.ems.data.domain.vol.EstelamFalseVTO;

/**
 * Main action class to handle all requests from card requests list
 * 
 * @author <a href="mailto:moghaddam@gamelectronics.com">Ehsan Zaery
 *         Moghaddam</a>
 */
public class Estelam2FalseListAction extends
		ListControllerImpl<EstelamFalseVTO> {

	private String estelam2falseId;

	public String getEstelam2falseId() {
		return estelam2falseId;
	}

	public void setEstelam2falseId(String estelam2falseId) {
		this.estelam2falseId = estelam2falseId;
	}

	@Override
	public void setRecords(List<EstelamFalseVTO> records) {
		this.records = records;
	}

}
