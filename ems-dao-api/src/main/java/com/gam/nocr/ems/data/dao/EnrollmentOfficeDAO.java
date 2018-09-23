package com.gam.nocr.ems.data.dao;

import java.util.List;
import java.util.Map;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.enums.EOFDeliveryState;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
public interface EnrollmentOfficeDAO extends EmsBaseDAO<EnrollmentOfficeTO> {

    /**
     * The method findNewActiveEnrollmentOffices is used to find new enrollment offices in spite of the value of
     * 'lastSyncDate'
     *
     * @return a list of type {@EnrollmentOfficeTO}
     * @throws BaseException
     */
    List<EnrollmentOfficeTO> findNewActiveEnrollmentOffices() throws BaseException;

    /**
     * The method findActiveModifiedEnrollmentOffices is used to find the active modified enrollment offices in spite of
     * the value of 'lastSyncDate' and 'lastModifiedDate'
     *
     * @return a list of type {@EnrollmentOfficeTO}
     * @throws BaseException
     */
    List<EnrollmentOfficeTO> findActiveModifiedEnrollmentOffices() throws BaseException;

    /**
     * The method findInActiveModifiedEnrollmentOffices is used to find the inactive modified enrollment offices in spite
     * of the value of 'lastSyncDate' and 'lastModifiedDate'
     *
     * @return a list of type {@EnrollmentOfficeTO}
     * @throws BaseException
     */
    List<EnrollmentOfficeTO> findInActiveModifiedEnrollmentOffices() throws BaseException;

    EnrollmentOfficeTO getSuperiorOffice(Long enrollmentOfficeId) throws BaseException;

    List<Long> findSubOffice(Long enrollmentOfficeId) throws BaseException;

    EnrollmentOfficeTO fetchOfficeByIdAndManagerId(Long enrollmentOfficeId, Long managerId) throws BaseException;

    //Commented By Adldoost
    //EnrollmentOfficeTO fetchOfficeByIdAndTokenState(Long enrollmentOfficeId, List<TokenState> tokenStateList) throws BaseException;

    List<Long> fetchOtherNocrOfficeCountWithSameParentById(Long enrollmentOfficeId) throws BaseException;

    EnrollmentOfficeTO loadLazyChildren(EnrollmentOfficeTO enrollmentOffice) throws BaseException;    
    
    List<Long> getEnrollmentOfficeListIds() throws BaseException;

    
    int updateEOFEnableFlag(Long eofId,EOFDeliveryState state) throws BaseException;

	List<Long> getEnrollmentOfficeListIdsByEOFType(EnrollmentOfficeType eofType) throws BaseException;

	List<Long> getEnrollmentOfficeListIdsByProvince(Long provinceId) throws BaseException;

	List<Long> getEnrollmentOfficeListIdsByProvinceAndType(Long provinceId,EnrollmentOfficeType eofType) throws BaseException;
	
	//Madanipour
	List<OfficeSettingTO> fetchOfficeSetting(Long enrollmentOfficeId)throws BaseException;

	List<EMSAutocompleteTO> fetchOfficesAutoComplete(UserProfileTO userProfileTO,String searchString, int from, int to, String orderBy,
			Map additionalParams) throws BaseException;

	Integer countOfficesAutoComplete(UserProfileTO userProfileTO,
			String searchString, int from, int to, String orderBy,
			Map additionalParams) throws BaseException;

    EnrollmentOfficeTO findEnrollmentOfficeById(Long eofId) throws BaseException;

    Boolean hasOfficeQueryByAccessibility(
            String climbingStairsAbility, String pupilIsVisible, Long enrollmentOfficeId) throws BaseException;

    Boolean hasOfficeQueryByInstruments(
            String abilityToGo, String hasTwoFingersScanable, Long enrollmentOfficeId) throws BaseException;

    List<EnrollmentOfficeTO> getEnrollmentOfficeList() throws DAOException;
}
