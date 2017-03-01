package com.gam.nocr.ems.data.enums;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public enum ReportOutputType {
	PDF,
	XLS;

	/**
	 * The method getContentType is used to get the content type of the requested report
	 *
	 * @param type is an instance of type {@link ReportOutputType}
	 * @return an instance of type {@link String}, which represents the content type
	 */
	public static String getContentType(ReportOutputType type) {
		String contentType = null;
		switch (type) {
			case PDF:
				contentType = "application/pdf";
				break;
			case XLS:
				contentType = "application/vnd.ms-excel";
				break;
		}
		return contentType;
	}
}
