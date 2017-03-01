package com.gam.nocr.ems.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gam.commons.core.data.domain.ExtEntityTO;


@Entity
@Table(name = "EMST_EXCEPTION_CODE")
public class ExceptionCodeTO extends ExtEntityTO{

    private String code;

    @Column(name = "EXC_CODE")
    @Id
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
   
}
