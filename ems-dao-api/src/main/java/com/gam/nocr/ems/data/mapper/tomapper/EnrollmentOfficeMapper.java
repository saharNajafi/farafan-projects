package com.gam.nocr.ems.data.mapper.tomapper;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public class EnrollmentOfficeMapper {

//    public static EnrollmentOfficeTO convert(EnrollmentOfficeVTO enrollmentOfficeVTO) {
//        EnrollmentOfficeTO office = new EnrollmentOfficeTO();
//        office.setName(enrollmentOfficeVTO.getName());
//        office.setAddress(enrollmentOfficeVTO.getAddress());
//        office.setPostalCode(enrollmentOfficeVTO.getZip());
//        office.setPhone(enrollmentOfficeVTO.getPhone());
//        office.setFax(enrollmentOfficeVTO.getFax());
//        office.setCode(enrollmentOfficeVTO.getCode());
//        office.setLocation(new ProvinceTO(enrollmentOfficeVTO.getLocId()));
//        office.setWorkingHoursFrom(enrollmentOfficeVTO.getWorkingHoursStart());
//        office.setWorkingHoursTo(enrollmentOfficeVTO.getWorkingHoursFinish());
//        office.setType(EnrollmentOfficeType.valueOf(enrollmentOfficeVTO.getOfficeType()));
//    }

//	TODO : Correct these methods to use as mappers

//	public static List<EnrollmentOfficeWTO> convert(List<EnrollmentOfficeTO> enrollmentOfficeTOList) {
//		List<EnrollmentOfficeWTO> enrollmentOfficeWTOList = new ArrayList<EnrollmentOfficeWTO>();
//		for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
//			EnrollmentOfficeWTO enrollmentOfficeWTO = new EnrollmentOfficeWTO();
//			enrollmentOfficeWTO.setEnrollmentOfficeAddress(enrollmentOfficeTO.getAddress());
//			enrollmentOfficeWTO.setId(enrollmentOfficeTO.getId());
//			enrollmentOfficeWTO.setEnrollmentOfficeArea(enrollmentOfficeTO.getArea());
//			enrollmentOfficeWTO.setEnrollmentOfficeCode(enrollmentOfficeTO.getCode());
//			enrollmentOfficeWTO.setEnrollmentOfficeFax(enrollmentOfficeTO.getFax());
//			enrollmentOfficeWTO.setEnrollmentOfficeName(enrollmentOfficeTO.getName());
//			enrollmentOfficeWTO.setEnrollmentOfficePhone(enrollmentOfficeTO.getPhone());
//			enrollmentOfficeWTO.setEnrollmentOfficePostalCode(enrollmentOfficeTO.getPostalCode());
//			enrollmentOfficeWTO.setLocationId(enrollmentOfficeTO.getParentId());
//			enrollmentOfficeWTO.setRatingInfoId(enrollmentOfficeTO.getRatingInfo().getId());
//			enrollmentOfficeWTOList.add(enrollmentOfficeWTO);
//		}
//
//		return enrollmentOfficeWTOList;
//	}


//	public static List<UserSiteInfo> convertToUserSitesInfo(List<EnrollmentOfficeTO> enrollmentOfficeTOList) {
//		List<UserSiteInfo> userSiteInfoList = new ArrayList<UserSiteInfo>();
//		for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
//			UserSiteInfo userSiteInfo = new UserSiteInfo();
//			userSiteInfo.setUserSiteID(String.valueOf(enrollmentOfficeTO.getId()));
//			userSiteInfo.setUserSiteCode(enrollmentOfficeTO.getCode());
//			userSiteInfo.setUserSiteName(enrollmentOfficeTO.getName());
//			userSiteInfo.setUserSiteContact(enrollmentOfficeTO.getPhone());
//			userSiteInfo.setUserSitePostalCode(enrollmentOfficeTO.getPostalCode());
//			userSiteInfo.setNocrOfficeID(String.valueOf(enrollmentOfficeTO.getParentDepartment().getId()));
//			userSiteInfo.setNocrOfficeCode(enrollmentOfficeTO.getParentDepartment().getCode());
//			userSiteInfo.setNocrOfficeName(enrollmentOfficeTO.getParentDepartment().getName());
//			userSiteInfo.setNocrOfficeStateName(enrollmentOfficeTO.getParentDepartment().getLocation().getName());
//			userSiteInfo.setNocrOfficePostalCode(enrollmentOfficeTO.getParentDepartment().getPostalCode());
//			if (EnrollmentOfficeTO.Status.ENABLED.equals(enrollmentOfficeTO.getStatus())) {
//				userSiteInfo.setStatus(USER_SITE_STATUS_ENABLED);
//			} else {
//				userSiteInfo.setStatus(USER_SITE_STATUS_DISABLED);
//			}
//			userSiteInfoList.add(userSiteInfo);
//		}
//		return userSiteInfoList;
//	}
//
//	public static UserSiteInfo convertToUserSiteInfo(EnrollmentOfficeTO enrollmentOfficeTO) {
//		UserSiteInfo userSiteInfo = null;
//		if (enrollmentOfficeTO != null) {
//			userSiteInfo = new UserSiteInfo();
//			userSiteInfo.setUserSiteID(String.valueOf(enrollmentOfficeTO.getId()));
//			userSiteInfo.setUserSiteCode(enrollmentOfficeTO.getCode());
//			userSiteInfo.setUserSiteName(enrollmentOfficeTO.getName());
//			userSiteInfo.setUserSiteContact(enrollmentOfficeTO.getPhone());
//			userSiteInfo.setUserSitePostalCode(enrollmentOfficeTO.getPostalCode());
//			userSiteInfo.setNocrOfficeID(String.valueOf(enrollmentOfficeTO.getParentDepartment().getId()));
//			userSiteInfo.setNocrOfficeCode(enrollmentOfficeTO.getParentDepartment().getCode());
//			userSiteInfo.setNocrOfficeName(enrollmentOfficeTO.getParentDepartment().getName());
//			userSiteInfo.setNocrOfficeStateName(enrollmentOfficeTO.getParentDepartment().getLocation().getName());
//			userSiteInfo.setNocrOfficePostalCode(enrollmentOfficeTO.getParentDepartment().getPostalCode());
//			if (EnrollmentOfficeTO.Status.ENABLED.equals(enrollmentOfficeTO.getStatus())) {
//				userSiteInfo.setStatus(USER_SITE_STATUS_ENABLED);
//			} else {
//				userSiteInfo.setStatus(USER_SITE_STATUS_DISABLED);
//			}
//		}
//		return userSiteInfo;
//	}

}
