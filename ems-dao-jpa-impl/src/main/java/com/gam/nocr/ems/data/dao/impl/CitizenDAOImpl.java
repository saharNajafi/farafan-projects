package com.gam.nocr.ems.data.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.CardRequestTO;
import com.gam.nocr.ems.data.domain.CitizenTO;
import com.gam.nocr.ems.data.enums.CardRequestState;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "CitizenDAO")
@Local(CitizenDAOLocal.class)
public class CitizenDAOImpl extends EmsBaseDAOImpl<CitizenTO> implements CitizenDAOLocal, CitizenDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public CitizenTO create(CitizenTO citizenTO) throws BaseException {
        try {
            CitizenTO to = super.create(citizenTO);
            em.flush();
            return to;
        } catch (Exception e) {
            String err = e.getMessage();
            if (e.getCause() != null) {
                if (e.getCause().getCause() != null)
                    err = e.getCause().getCause().getMessage();
                else
                    err = e.getCause().getMessage();
            }
            if (err.contains("AK_CTZ_NATIONAL_ID")) {
                throw new DAOException(DataExceptionCode.CTI_002, DataExceptionCode.CTI_002_MSG, e);
            }
            if (err.contains("FK_CZN_CITIZEN_ID")) {
                throw new DAOException(DataExceptionCode.CTI_003, DataExceptionCode.CTI_003_MSG, e, new Long[]{citizenTO.getId()});
            }
            if (err.contains("FK_CZN_INF_BIRTH_PRV_ID")) {
                throw new DAOException(DataExceptionCode.CTI_004, DataExceptionCode.CTI_004_MSG, e, new Long[]{citizenTO.getCitizenInfo().getBirthCertificateIssuancePlaceProvince().getId()});
            }
            if (err.contains("FK_CZN_INF_LVN_CITY_ID")) {
                throw new DAOException(DataExceptionCode.CTI_005, DataExceptionCode.CTI_005_MSG, e, new Long[]{citizenTO.getCitizenInfo().getLivingCity().getId()});
            }
            if (err.contains("FK_CZN_INF_LVN_PRV_ID")) {
                throw new DAOException(DataExceptionCode.CTI_006, DataExceptionCode.CTI_006_MSG, e, new Long[]{citizenTO.getCitizenInfo().getLiving().getId()});
            }
            if (err.contains("FK_CZN_INF_RELGION_ID")) {
                throw new DAOException(DataExceptionCode.CTI_007, DataExceptionCode.CTI_007_MSG, e, new Long[]{citizenTO.getCitizenInfo().getReligion().getId()});
            }
            if (err.contains("FK_SPS_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CTI_008, DataExceptionCode.CTI_008_MSG, e);
            }
            if (err.contains("FK_SPS_MARITAL_STAT_ID")) {
                throw new DAOException(DataExceptionCode.CTI_009, DataExceptionCode.CTI_009_MSG, e);
            }
            if (err.contains("FK_CHILD_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CTI_010, DataExceptionCode.CTI_010_MSG, e);
            }
            if (err.contains("FK_BIM_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CTI_011, DataExceptionCode.CTI_011_MSG, e);
            }
            if (err.contains("FK_DOC_CITZN_INF_ID")) {
                throw new DAOException(DataExceptionCode.CTI_012, DataExceptionCode.CTI_012_MSG, e);
            }
            if (err.contains("FK_DOC_DOC_TYPE_ID")) {
                throw new DAOException(DataExceptionCode.CTI_013, DataExceptionCode.CTI_013_MSG, e);
            }
            throw new DAOException(DataExceptionCode.CTI_001, DataExceptionCode.CTI_001_MSG, e);
        }
    }

    @Override
    public List<CitizenTO> findByNID(String nid) throws BaseException {
        try {
            return em.createQuery("select ctz from CitizenTO ctz where ctz.nationalID = :nid", CitizenTO.class)
                    .setParameter("nid", nid)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CTI_015, DataExceptionCode.CTI_015_MSG, e, new String[]{nid});
        }
    }
    
    
    
    @Override
    public CitizenTO findByNationalID(String nid) throws BaseException {
        try {
            List<CitizenTO> citizens = em.createQuery("select ctz from CitizenTO ctz where ctz.nationalID = :nid", CitizenTO.class)
                    .setParameter("nid", nid)
                    .getResultList();
            if (citizens.size() == 0) {
                return null;
            }
            CitizenTO ctz = citizens.get(0);
			if (ctz.getCitizenInfo().getLivingCity() != null) {
				ctz.getCitizenInfo().getLivingCity().getCounty().getName();
				ctz.getCitizenInfo().getLivingCity().getTownship().getName();
				 if (ctz.getCitizenInfo().getLivingCity().getType()
						.equals("2")) {
					ctz.getCitizenInfo().getLivingCity().getDistrict()
							.getName();
				}

			}
            ctz.getCitizenInfo().getSpouses().size();
            ctz.getCitizenInfo().getChildren().size();
            return ctz;
        } catch (Exception e) {
        	 throw new DAOException(DataExceptionCode.CTI_015, DataExceptionCode.CTI_015_MSG, e, new String[]{nid});
        }
    }

    @Override
    public CitizenTO findRequestId(Long requestId) throws BaseException {
        try {
            List<CitizenTO> citizens = em.createQuery("select ctz " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id", CitizenTO.class)
                    .setParameter("requestId", requestId)
                    .getResultList();
            if (citizens.size() == 0) {
                return null;
            }
            return citizens.get(0);
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CTI_017, DataExceptionCode.CTI_017_MSG, e, new Long[]{requestId});
        }
    }

    @Override
    public CitizenTO findByRequestId(long requestId) throws BaseException {
        try {
            List<CitizenTO> citizens = em.createQuery("select ctz " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id", CitizenTO.class)
                    .setParameter("requestId", requestId)
                    .getResultList();
            if (citizens.size() == 0) {
                return null;
            }
            CitizenTO ctz = citizens.get(0);
			if (ctz.getCitizenInfo().getLivingCity() != null) {
				ctz.getCitizenInfo().getLivingCity().getCounty().getName();
				ctz.getCitizenInfo().getLivingCity().getTownship().getName();
				 if (ctz.getCitizenInfo().getLivingCity().getType()
						.equals("2")) {
					ctz.getCitizenInfo().getLivingCity().getDistrict()
							.getName();
				}

			}
            ctz.getCitizenInfo().getSpouses().size();
            ctz.getCitizenInfo().getChildren().size();
            return ctz;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CTI_017, DataExceptionCode.CTI_017_MSG, e, new Long[]{requestId});
        }
    }

//    @Override
//    public List<CitizenTO> findByAttributes(String firstName, String lastName, String nationalId) throws BaseException {
//        try {
//            return em.createQuery("select ctz " + //todo: later change to ctz.id only
//                    "from CitizenTO ctz " +
//                    "where ctz.firstNamePersian = :firstName " +
//                    "and ctz.surnamePersian = :lastName " +
//                    "and ctz.nationalID = :nationalId", CitizenTO.class)
//                    .setParameter("firstName", firstName)
//                    .setParameter("lastName", lastName)
//                    .setParameter("nationalId", nationalId)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DAOException(DataExceptionCode.CTI_014, DataExceptionCode.GLB_005_MSG, e);
//        }
//    }
    
    // FirstName And LastName do not use any more ( for Sodour mojadad )
    @Override
    public List<CitizenTO> findByAttributes(String firstName, String lastName, String nationalId) throws BaseException {
        try {
            return em.createQuery("select ctz " + //todo: later change to ctz.id only
                    "from CitizenTO ctz " +
                    "where " +
                    "ctz.nationalID = :nationalId", CitizenTO.class)
                    .setParameter("nationalId", nationalId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CTI_014, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public CitizenTO findAllAttributesByRequestId(long requestId) throws BaseException {
        try {
            List<CitizenTO> citizens = em.createQuery("select ctz " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id", CitizenTO.class)
                    .setParameter("requestId", requestId)
                    .getResultList();
            if (citizens.size() == 0) {
                return null;
            }
            CitizenTO ctz = citizens.get(0);
            ctz.getCitizenInfo().getSpouses().size();
            ctz.getCitizenInfo().getChildren().size();
            ctz.getCitizenInfo().getBiometrics().size();
            return ctz;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CTI_019, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    @Override
    public CitizenTO findAllByRequestId(long requestId) throws BaseException {
        try {
            List<CitizenTO> citizens = em.createQuery("select ctz " +
                    "from CitizenTO ctz, CardRequestTO crq " +
                    "where crq.id = :requestId " +
                    "and crq.citizen.id = ctz.id", CitizenTO.class)
                    .setParameter("requestId", requestId)
                    .getResultList();
            if (citizens.size() == 0) {
                return null;
            }
            CitizenTO ctz = citizens.get(0);
            ctz.getCitizenInfo().getSpouses().size();
            ctz.getCitizenInfo().getChildren().size();
            ctz.getCitizenInfo().getBiometrics().size();
            return ctz;
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.CTI_020, DataExceptionCode.GLB_005_MSG, e);
        }
    }
    
    @Override
    public List<CardRequestTO> checkCitizenHasAnyUnDeliveredRequest(CitizenTO citizen) throws BaseException
    {
    	try
    	{
    		List<CardRequestTO> crqList = em.createQuery("SELECT crq FROM CardRequestTO crq WHERE crq.citizen = :ctz and crq.state != :state", CardRequestTO.class).setParameter("ctz", citizen).setParameter("state", CardRequestState.DELIVERED).getResultList();
    		if(crqList == null || crqList.isEmpty())
    		{
    			return null;
    		}
    		else
    			return crqList;
    		
    	}catch(Exception e)
    	{
    		throw new DAOException(DataExceptionCode.CTI_021, DataExceptionCode.GLB_005_MSG, e);
    	}
    }
}
