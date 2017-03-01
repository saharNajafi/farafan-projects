package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.BooleanType;
import com.gam.nocr.ems.data.enums.ReportScope;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
@Entity
@Table(name = "EMST_REPORT")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_REPORT", allocationSize = 1)
public class ReportTO extends ExtEntityTO {

	private String name;
	private String comment;
	private BooleanType activateFlag;
	private Date createDate;
    private ReportScope scope;
	private List<ReportFileTO> reportFiles = new ArrayList<ReportFileTO>(0);
	private String permission;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "RPT_ID")
	public Long getId() {
		return super.getId();
	}

	@Column(name = "RPT_NAME", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "RPT_COMMENT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

    @Enumerated(EnumType.STRING)
	@Column(name = "RPT_ACTIVATION_FLAG", length = 1, nullable = false)
	public BooleanType getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(BooleanType activateFlag) {
		this.activateFlag = activateFlag;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RPT_CREATE_DATE", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

    @Enumerated(EnumType.STRING)
    @Column(name = "RPT_SCOPE")
    public ReportScope getScope() {
        return scope;
    }

    public void setScope(ReportScope scope) {
        this.scope = scope;
    }

    @OneToMany(mappedBy = "reportTO", cascade = {CascadeType.PERSIST})
	public List<ReportFileTO> getReportFiles() {
		return reportFiles;
	}

	public void setReportFiles(List<ReportFileTO> reportFiles) {
		this.reportFiles = reportFiles;
	}

	@Column(name = "RPT_PERMISSION", nullable = false)
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
}
