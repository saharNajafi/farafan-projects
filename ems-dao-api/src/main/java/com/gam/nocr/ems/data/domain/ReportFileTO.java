package com.gam.nocr.ems.data.domain;

import com.gam.commons.core.data.domain.ExtEntityTO;
import com.gam.nocr.ems.data.enums.ReportFileType;
import com.gam.nocr.ems.data.enums.ReportOutputType;

import javax.persistence.*;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */

@Entity
@Table(name = "EMST_REPORT_FILE")
@SequenceGenerator(name = "seq", sequenceName = "SEQ_EMS_REPORT_FILE", allocationSize = 1)
public class ReportFileTO extends ExtEntityTO {

	private ReportTO reportTO;
	private byte[] data;
	private ReportFileType type;
	private String caption;
    private ReportOutputType outputType;
//	private ReportFileTO parentReportFileTO;
//	private List<ReportFileTO> subReports = new ArrayList<ReportFileTO>(0);

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "RPF_ID")
	public Long getId() {
		return super.getId();
	}

	//	@OneToOne
	@ManyToOne
	@JoinColumn(name = "RPF_REPORT_ID")
	public ReportTO getReportTO() {
		return reportTO;
	}

	public void setReportTO(ReportTO reportTO) {
		this.reportTO = reportTO;
	}

	@Lob
	@Column(name = "RPF_DATA")
	public byte[] getData() {
		return data == null ? data : data.clone();
	}

	public void setData(byte[] data) {
		this.data = data;
	}

    @Enumerated(EnumType.STRING)
	@Column(name = "RPF_TYPE")
	public ReportFileType getType() {
		return type;
	}

	public void setType(ReportFileType type) {
		this.type = type;
	}

	@Column(name = "RPF_CAPTION")
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

    @Enumerated(EnumType.STRING)
    @Column(name = "RPF_OUTPUT_TYPE")
    public ReportOutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(ReportOutputType outputType) {
        this.outputType = outputType;
    }

    //    @ManyToOne
//    @JoinColumn(name = "RPF_PARENT_REPORT_FILE")
//    public ReportFileTO getParentReportFileTO() {
//        return parentReportFileTO;
//    }
//
//    public void setParentReportFileTO(ReportFileTO parentReportFileTO) {
//        this.parentReportFileTO = parentReportFileTO;
//    }

//	@OneToMany(mappedBy = "reportFile", cascade = {CascadeType.PERSIST})
//	@JoinColumn(name = "RPF_PARENT_REPORT_FILE")
//	public List<ReportFileTO> getSubReports() {
//		return subReports;
//	}
//
//	public void setSubReports(List<ReportFileTO> subReports) {
//		this.subReports = subReports;
//	}
}
