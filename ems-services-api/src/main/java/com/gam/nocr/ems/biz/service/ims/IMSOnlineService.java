package com.gam.nocr.ems.biz.service.ims;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO;

import java.util.HashMap;

/**
 * @author Saeed Jalilian (jalilian@gamelectronics.com)
 */
public interface IMSOnlineService {

    /**
     * The method getOnlineEnquiry is used to get the online enquiry from the sub system 'IMS'
     *
     * @param personEnquiryVTOs is an array of type {@link com.gam.nocr.ems.data.domain.vol.PersonEnquiryVTO} which carries the necessary attributes for the process of online enquiry
     * @return a hashmap of {@link java.util.HashMap <String, Boolean>} which carries nationalId and the result of the online enquiry(true or false)
     */
    HashMap<String, Boolean> getOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;

    /**
     * The method fetchDataByOnlineEnquiry is used to fetch the citizen info from the IMS sub system
     *
     * @param personEnquiryVTOs is an array of type {@link PersonEnquiryVTO} which carries the necessary attributes for fetching data from IMS
     * @return a hashmap of {@link java.util.HashMap <String, PersonEnquiryVTO>} which carries nationalId and an instance
     *         of type {@link PersonEnquiryVTO}, which was valued By means of the IMS database
     */
    HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiry(PersonEnquiryVTO[] personEnquiryVTOs) throws BaseException;
    
   // HashMap<String, PersonEnquiryVTO> fetchDataByOnlineEnquiryByEstelam2(PersonEnquiryVTO[] personEnquiryVTOs,String citizenNID) throws BaseException;

    /**
     * The method fetchCitizenInfoByOnlineEnquiry is used to fetch the citizen information, which is signed as valid data on half of IMS sub system
     * @param citizenTO is an instance of type {@link CitizenTO}
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.CitizenTO}
     * @throws BaseException
     */
    CitizenTO fetchCitizenInfoByOnlineEnquiry(CitizenTO citizenTO) throws BaseException;
    
    //Anbari: New Online Estelam(call getEstelam3sc within this method)
    PersonEnquiryVTO fetchDataByOnlineEnquiryByEstelam3(PersonEnquiryVTO personEnquiryVTOInput,String citizenNID) throws BaseException;


}
