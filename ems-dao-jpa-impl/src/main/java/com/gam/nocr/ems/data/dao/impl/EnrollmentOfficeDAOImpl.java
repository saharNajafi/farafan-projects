package com.gam.nocr.ems.data.dao.impl;

import static com.gam.nocr.ems.config.EMSLogicalNames.DAO_PERSON;
import static com.gam.nocr.ems.config.EMSLogicalNames.getDaoJNDIName;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.commons.core.data.domain.UserProfileTO;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.config.EMSLogicalNames;
import com.gam.nocr.ems.data.dao.PersonDAO;
import com.gam.nocr.ems.data.domain.EMSAutocompleteTO;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.OfficeSettingTO;
import com.gam.nocr.ems.data.domain.vol.EnrollmentOfficeVTO;
import com.gam.nocr.ems.data.domain.ws.CitizenWTO;
import com.gam.nocr.ems.data.enums.EOFDeliveryState;
import com.gam.nocr.ems.data.enums.EnrollmentOfficeType;
import com.gam.nocr.ems.util.EmsUtil;

/**
 * <p> TODO -- Explain this class </p>
 *
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "EnrollmentOfficeDAO")
@Local(EnrollmentOfficeDAOLocal.class)
@Remote(EnrollmentOfficeDAORemote.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EnrollmentOfficeDAOImpl extends EmsBaseDAOImpl<EnrollmentOfficeTO> implements EnrollmentOfficeDAOLocal, EnrollmentOfficeDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public EnrollmentOfficeTO find(Class type, Object id) throws BaseException {
        try {
            return super.find(type, id);
        } catch (BaseException e) {
            throw new DAOException(DataExceptionCode.ENI_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findNewActiveEnrollmentOffices is used to find new enrollment offices in spite of the value of
     * 'lastSyncDate'
     *
     * @return a list of type {@EnrollmentOfficeTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<EnrollmentOfficeTO> findNewActiveEnrollmentOffices() throws BaseException {
        List<EnrollmentOfficeTO> enrollmentOfficeTOList;
        try {
//EditedByAdldoost
            enrollmentOfficeTOList = em.createQuery("SELECT EOF FROM EnrollmentOfficeTO EOF " +
                    "WHERE (EOF.lastSyncDate IS NULL ) " 
                    + "and ( eof.id in (select ntk1.enrollmentOffice.id from NetworkTokenTO NTK1 where ntk1.state = 'DELIVERED') "
					+ "or eof.id not in (select ntk2.enrollmentOffice.id from NetworkTokenTO NTK2) "
					+ "or eof.type = 'NOCR') "
//                    "AND ((EOF.type = 'OFFICE' and EOF.id IN " +
//                    "(SELECT NTK.enrollmentOffice.id FROM NetworkTokenTO NTK WHERE NTK.enrollmentOffice.id = EOF.id " +
//                    "and NTK.state = :DELIVERED_TOKEN_STATE)) " +
//                    "or (EOF.type = 'NOCR')) " + 
                    + "ORDER BY EOF.id", EnrollmentOfficeTO.class)
//                    .setParameter("DELIVERED_TOKEN_STATE", TokenState.DELIVERED)
                    .getResultList();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_002, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(enrollmentOfficeTOList)) {
            for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
                enrollmentOfficeTO.getParentDepartment().getName();
                if (enrollmentOfficeTO.getSuperiorOffice() != null){
                	enrollmentOfficeTO.getSuperiorOffice().getName();
                }
                if (enrollmentOfficeTO.getParentDepartment().getLocation().getCounty() != null)
                    enrollmentOfficeTO.getParentDepartment().getLocation().getCounty().getName();
                if (enrollmentOfficeTO.getParentDepartment().getLocation().getDistrict() != null)
                    enrollmentOfficeTO.getParentDepartment().getLocation().getDistrict().getName();
            }
        }

        return enrollmentOfficeTOList;
    }

    /**
     * The method findActiveModifiedEnrollmentOffices is used to find the modified enrollment offices in spite of the
     * value of 'lastSyncDate' and 'lastModifiedDate'
     *
     * @return a list of type {@EnrollmentOfficeTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<EnrollmentOfficeTO> findActiveModifiedEnrollmentOffices() throws BaseException {
        List<EnrollmentOfficeTO> enrollmentOfficeTOList;
		try {
			// // Edited By Adldoost
			// enrollmentOfficeTOList =
			// em.createQuery("SELECT EOF FROM EnrollmentOfficeTO EOF " +
			// "WHERE " +
			// "((EOF.lastSyncDate IS NOT NULL AND EOF.lastSyncDate < EOF.lastModifiedDate) OR "
			// +
			// "(EOF.lastSyncDate < EOF.parentDepartment.lastModifiedDate))" +//
			// AND " +
			// // "((EOF.type = 'OFFICE' and EOF.id IN " +
			// //
			// "(SELECT NTK.enrollmentOffice.id FROM NetworkTokenTO NTK WHERE NTK.enrollmentOffice.id = EOF.id "
			// +
			// // "and NTK.state = :DELIVERED_TOKEN_STATE)) " +
			// // "or (EOF.type = 'NOCR')) " +
			// "ORDER BY EOF.id", EnrollmentOfficeTO.class)
			// // .setParameter("DELIVERED_TOKEN_STATE", TokenState.DELIVERED)
			// .getResultList();
			// em.flush();
			enrollmentOfficeTOList = em
					.createQuery(
							"SELECT EOF FROM EnrollmentOfficeTO EOF "
									+ "where ( eof.id in (select ntk1.enrollmentOffice.id from NetworkTokenTO NTK1 where ntk1.state = 'DELIVERED') "
									+ "or eof.id not in (select ntk2.enrollmentOffice.id from NetworkTokenTO NTK2) "
									+ "or eof.type = 'NOCR') "
									+ "and "
									+ "((EOF.lastSyncDate IS NOT NULL AND EOF.lastSyncDate < EOF.lastModifiedDate) OR "
									+ "(EOF.lastSyncDate < EOF.parentDepartment.lastModifiedDate)) ",
							EnrollmentOfficeTO.class).getResultList();
			em.flush();

		} catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_004, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(enrollmentOfficeTOList)) {
            for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
                enrollmentOfficeTO.getParentDepartment().getName();
                if (enrollmentOfficeTO.getSuperiorOffice() != null){
                	enrollmentOfficeTO.getSuperiorOffice().getName();
                }
                if (enrollmentOfficeTO.getParentDepartment().getLocation().getCounty() != null)
                    enrollmentOfficeTO.getParentDepartment().getLocation().getCounty().getName();
                if (enrollmentOfficeTO.getParentDepartment().getLocation().getDistrict() != null)
                    enrollmentOfficeTO.getParentDepartment().getLocation().getDistrict().getName();
            }
        }

        return enrollmentOfficeTOList;
    }

    /**
     * The method findInActiveModifiedEnrollmentOffices is used to find the inactive modified enrollment offices in spite
     * of the value of 'lastSyncDate' and 'lastModifiedDate'
     *
     * @return a list of type {@EnrollmentOfficeTO}
     * @throws com.gam.commons.core.BaseException
     */
    @Override
    public List<EnrollmentOfficeTO> findInActiveModifiedEnrollmentOffices() throws BaseException {
        List<EnrollmentOfficeTO> enrollmentOfficeTOList;
		try {
			// //Edited By Adldoost
			// enrollmentOfficeTOList =
			// em.createQuery("SELECT EOF FROM EnrollmentOfficeTO EOF " +
			// "WHERE " +
			// "((EOF.lastSyncDate IS NOT NULL AND EOF.lastSyncDate < EOF.lastModifiedDate) OR "
			// +
			// "(EOF.lastSyncDate < EOF.parentDepartment.lastModifiedDate))" +
			// // AND " +
			// // "(EOF.type = 'OFFICE' and EOF.id NOT IN " +
			// //
			// "(SELECT NTK.enrollmentOffice.id FROM NetworkTokenTO NTK WHERE NTK.enrollmentOffice.id = EOF.id "
			// +
			// // "and NTK.state = :DELIVERED_TOKEN_STATE)) " +
			// "ORDER BY EOF.id", EnrollmentOfficeTO.class)
			// // .setParameter("DELIVERED_TOKEN_STATE", TokenState.DELIVERED)
			// .getResultList();
			// em.flush();
			enrollmentOfficeTOList = em
					.createQuery(
							"SELECT EOF1 FROM EnrollmentOfficeTO EOF1 where (EOF1.id NOT IN "
									+ "(SELECT EOF.id FROM EnrollmentOfficeTO EOF "
									+ "where ( eof.id in (select ntk1.enrollmentOffice.id from NetworkTokenTO NTK1 where ntk1.state = 'DELIVERED' ) "
									+ "or eof.id not in (select ntk2.enrollmentOffice.id from NetworkTokenTO NTK2) "
									+ "or eof.type = 'NOCR'))) "
									+ "and "
									+ "((EOF1.lastSyncDate IS NOT NULL AND EOF1.lastSyncDate < EOF1.lastModifiedDate) OR "
									+ "(EOF1.lastSyncDate < EOF1.parentDepartment.lastModifiedDate))) ",
							EnrollmentOfficeTO.class).getResultList();
			em.flush();
		} catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_003, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(enrollmentOfficeTOList)) {
            for (EnrollmentOfficeTO enrollmentOfficeTO : enrollmentOfficeTOList) {
                enrollmentOfficeTO.getParentDepartment().getName();
                if (enrollmentOfficeTO.getSuperiorOffice() != null){
                	enrollmentOfficeTO.getSuperiorOffice().getName();
                }
                if (enrollmentOfficeTO.getParentDepartment().getLocation().getCounty() != null)
                    enrollmentOfficeTO.getParentDepartment().getLocation().getCounty().getName();
                if (enrollmentOfficeTO.getParentDepartment().getLocation().getDistrict() != null)
                    enrollmentOfficeTO.getParentDepartment().getLocation().getDistrict().getName();
            }
        }

        return enrollmentOfficeTOList;
    }

    @Override
    public EnrollmentOfficeTO getSuperiorOffice(Long enrollmentOfficeId) throws BaseException {
        List<EnrollmentOfficeTO> enrollmentOfficeTOList;
        try {
            enrollmentOfficeTOList = em.createQuery("select eofSup from EnrollmentOfficeTO eofSup " +
                    "where eofSup.type = :enrollmentOfficeType " +
                    "and eofSup.id in (select eof.superiorOffice.id from EnrollmentOfficeTO eof " +
                    "where eof.id = :enrollmentOfficeId)", EnrollmentOfficeTO.class)
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("enrollmentOfficeType", EnrollmentOfficeType.NOCR)
                    .getResultList();
            em.flush();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_005, DataExceptionCode.GLB_005_MSG, e);
        }

        return enrollmentOfficeTOList.get(0);
    }

    @Override
    public List<Long> findSubOffice(Long enrollmentOfficeId) throws BaseException {
        try {
            return em.createQuery("select eof.id from EnrollmentOfficeTO eof " +
                    "where eof.type = :enrollmentOfficeType " +
                    "and eof.superiorOffice.id = :enrollmentOfficeId", Long.class)
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("enrollmentOfficeType", EnrollmentOfficeType.OFFICE)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_006, DataExceptionCode.GLB_005_MSG, e);
        }
    }

//Commented By Adldoost
//    @Override
//    public EnrollmentOfficeTO fetchOfficeByIdAndTokenState(Long enrollmentOfficeId, List<TokenState> tokenStateList) throws BaseException {
//        List<EnrollmentOfficeTO> enrollmentOfficeTOs;
//
//        try {
//            enrollmentOfficeTOs = em.createQuery("select eof from " +
//                    "EnrollmentOfficeTO eof, NetworkTokenTO net " +
//                    "where eof.id = net.enrollmentOffice.id " +
//                    "and eof.id = :enrollmentOfficeId " +
//                    "and net.state in (:tokenStateList) " +
//                    "order by net.id desc ", EnrollmentOfficeTO.class)
//                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
//                    .setParameter("tokenStateList", tokenStateList)
//                    .getResultList();
//        } catch (Exception e) {
//            throw new DAOException(DataExceptionCode.ENI_007, DataExceptionCode.GLB_005_MSG, e);
//        }
//
//        if (EmsUtil.checkListSize(enrollmentOfficeTOs))
//            return enrollmentOfficeTOs.get(0);
//        else
//            return null;
//    }

    @Override
    public EnrollmentOfficeTO fetchOfficeByIdAndManagerId(Long enrollmentOfficeId, Long managerId) throws BaseException {
        List<EnrollmentOfficeTO> enrollmentOfficeTOs;

        try {
            enrollmentOfficeTOs = em.createQuery("select eof from EnrollmentOfficeTO eof, PersonTO per " +
                    "where eof.manager.id = per.id " +
                    "and eof.id = :enrollmentOfficeId " +
                    "and eof.manager.id = :managerId", EnrollmentOfficeTO.class)
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("managerId", managerId)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_008, DataExceptionCode.GLB_005_MSG, e);
        }

        if (EmsUtil.checkListSize(enrollmentOfficeTOs))
            return enrollmentOfficeTOs.get(0);
        else
            return null;
    }

    @Override
    public List<Long> fetchOtherNocrOfficeCountWithSameParentById(Long enrollmentOfficeId) throws BaseException {
        try {
            return em.createQuery("select count(eof.id) from EnrollmentOfficeTO eof " +
                    "where eof.parentDepartment.id in " +
                    "(select t1.parentDepartment.id from EnrollmentOfficeTO t1 where t1.id = :enrollmentOfficeId) " +
                    "and eof.id not in (:enrollmentOfficeId) " +
                    "and eof.type = :type", Long.class)
                    .setParameter("enrollmentOfficeId", enrollmentOfficeId)
                    .setParameter("type", EnrollmentOfficeType.NOCR)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.ENI_008, DataExceptionCode.GLB_005_MSG, e);
        }
    }
    
	@Override
	public EnrollmentOfficeTO loadLazyChildren(EnrollmentOfficeTO enrollmentOffice)
			throws BaseException {
		try {
			EnrollmentOfficeTO dbEnrollmentOffice = find(EnrollmentOfficeTO.class, enrollmentOffice.getId());
			Hibernate.initialize(dbEnrollmentOffice.getSuperiorOffice());
			if (dbEnrollmentOffice.getSuperiorOffice() != null){
				dbEnrollmentOffice.getSuperiorOffice().getId();
				dbEnrollmentOffice.getSuperiorOffice().getCode();
				dbEnrollmentOffice.getSuperiorOffice().getName();
				dbEnrollmentOffice.getSuperiorOffice().getAddress();
				dbEnrollmentOffice.getSuperiorOffice().getPostalCode();
				dbEnrollmentOffice.getSuperiorOffice().getPhone();
				dbEnrollmentOffice.getSuperiorOffice().getFax();				
			}
			Hibernate.initialize(dbEnrollmentOffice.getParentDepartment().getLocation().getProvince());
			if(dbEnrollmentOffice.getParentDepartment().getLocation().getProvince() != null)
				dbEnrollmentOffice.getParentDepartment().getLocation().getProvince().getName();				
			return dbEnrollmentOffice;
		} catch (HibernateException e) {
			throw new DAOException(DataExceptionCode.ENI_009, DataExceptionCode.GLB_005_MSG, e);
		}
	}

	// Anbari
		@Override
		public List<Long> getEnrollmentOfficeListIds()
				throws BaseException {
			try {
				return em.createQuery(
						"select eo.id from EnrollmentOfficeTO eo "
								+ "order by eo.id asc ", Long.class).getResultList();
			} catch (Exception e) {
				throw new DAOException(DataExceptionCode.ENI_010, DataExceptionCode.GLB_005_MSG, e);
			}
		}

	@Override
	public int updateEOFEnableFlag(Long eofId, EOFDeliveryState state)
			throws BaseException {
		try {
			int affectedRecords = em
					.createQuery(
							"UPDATE EnrollmentOfficeTO eof "
									+ "SET eof.isDeliverEnable = :enrollmentOfficeId "
									+ "WHERE eof.id = :eofId ")
					.setParameter("enrollmentOfficeId", eofId)
					.setParameter("isDeliverEnable", EOFDeliveryState.toLong(state))
					.executeUpdate();
			em.flush();
			return affectedRecords;
		} catch (Exception e) {
			throw new DataException(DataExceptionCode.ENI_009,
					DataExceptionCode.GLB_006_MSG, e);
		}
	}

	//Anbari
	@Override
	public List<Long> getEnrollmentOfficeListIdsByEOFType(
			EnrollmentOfficeType eofType) throws BaseException {
		  try {
	            return em.createQuery("select eof.id from EnrollmentOfficeTO eof " +
	                    "where eof.type = :enrollmentOfficeType ", Long.class)
	                    .setParameter("enrollmentOfficeType", eofType)
	                    .getResultList();
	        } catch (Exception e) {
	            throw new DAOException(DataExceptionCode.ENI_011, DataExceptionCode.GLB_005_MSG, e);
	        }
	}

	//Anbari
	@Override
	public List<Long> getEnrollmentOfficeListIdsByProvince(Long provinceId)
			throws BaseException {
		 try {
	            return em.createQuery("select eof.id from EnrollmentOfficeTO eof,DepartmentTO dep,LocationTO loc " +
	                    "where eof.id = dep.id and dep.location.id = loc.id and loc.province.id = :provinceId ", Long.class)
	                    .setParameter("provinceId", provinceId)
	                    .getResultList();
	        } catch (Exception e) {
	            throw new DAOException(DataExceptionCode.ENI_012, DataExceptionCode.GLB_005_MSG, e);
	        }
	}

	//Anbari
	@Override
	public List<Long> getEnrollmentOfficeListIdsByProvinceAndType(
			Long provinceId, EnrollmentOfficeType eofType) throws BaseException {
		 try {
	            return em.createQuery("select eof.id from EnrollmentOfficeTO eof,DepartmentTO dep,LocationTO loc " +
	                    "where eof.id = dep.id and dep.location.id = loc.id and loc.province.id = :provinceId and eof.type =:eofType ", Long.class)
	                    .setParameter("provinceId", provinceId)
	                    .setParameter("eofType", eofType)
	                    .getResultList();
	        } catch (Exception e) {
	            throw new DAOException(DataExceptionCode.ENI_013, DataExceptionCode.GLB_005_MSG, e);
	        }
	}

	@Override
	public List<EMSAutocompleteTO> fetchOfficesAutoComplete(UserProfileTO userProfileTO,String searchString, int from, int to,
			String orderBy, Map additionalParams) throws BaseException {
		Long personID = null;
		try {
			List<EMSAutocompleteTO> result=new ArrayList<EMSAutocompleteTO>();
			String type=null;
			personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
			if(additionalParams!=null && additionalParams.get("type")!=null)
				type= additionalParams.get("type").toString();
			StringBuffer queryBuffer = new StringBuffer(
					"SELECT eo.EOF_ID,dep.DEP_NAME FROM EMST_ENROLLMENT_OFFICE eo,emst_department dep  where eo.EOF_ID in" +
							" (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start " +
							"with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id="+personID +
							" union select e.eof_id from emst_enrollment_office e connect by " +
							"prior e.eof_id=e.eof_superior_office start with" +
							" e.eof_id in (select p.per_dep_id from emst_person p where p.per_id="+personID
							+" ))) and dep.DEP_ID=eo.EOF_ID and dep.DEP_NAME like '%"+searchString+"%'");
			
			if(type!=null && type.equals("delivery"))
				queryBuffer.append(" and (eo.EOF_TYPE = 'NOCR' or (eo.EOF_TYPE = 'OFFICE' and eo.EOF_DELIVER_TYPE = '1'))");
			
			
			
			List resultList= em
					.createNativeQuery(queryBuffer.toString()).setFirstResult(from).setMaxResults(to-from).getResultList();
			
			if (resultList != null) {
				for (Object record : resultList) {
					Object[] data = (Object[]) record;
					EMSAutocompleteTO obj = new EMSAutocompleteTO();
					obj.setId(((BigDecimal) data[0]).longValue());
					obj.setAcName((String) data[1]);
					result.add(obj);
				}
			}
			return result;
			
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.ENI_013,
					DataExceptionCode.GLB_005_MSG, e);
		}
	}

	@Override
	public Integer countOfficesAutoComplete(UserProfileTO userProfileTO,
			String searchString, int from, int to, String orderBy,
			Map additionalParams) throws BaseException {
		
		Long personID = null;
		try {
			String type=null;
			if(additionalParams!=null && additionalParams.get("type")!=null)
				type= additionalParams.get("type").toString();
			personID = getPersonDAO().findPersonIdByUsername(userProfileTO.getUserName());
			StringBuffer queryBuffer = new StringBuffer(
					"SELECT count(*) FROM EMST_ENROLLMENT_OFFICE eo,emst_department dep where eo.EOF_ID in" +
							" (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start " +
							"with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id="+personID +
							" union select e.eof_id from emst_enrollment_office e connect by " +
							"prior e.eof_id=e.eof_superior_office start with" +
							" e.eof_id in (select p.per_dep_id from emst_person p where p.per_id="+personID
							+" ))) and dep.DEP_ID=eo.EOF_ID and dep.DEP_NAME like '%"+searchString+"%'");

			if(type!=null && type.equals("delivery"))
				queryBuffer.append(" and (eo.EOF_TYPE = 'NOCR' or (eo.EOF_TYPE = 'OFFICE' and eo.EOF_DELIVER_TYPE = '1'))");
			
			Number number = (Number)em
					.createNativeQuery(queryBuffer.toString()).getSingleResult();
			if (number != null) {
				return number.intValue();
			}
			return null;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.ENI_013,
					DataExceptionCode.GLB_005_MSG, e);
		}
	}
	
	/**
	 * @author Madanipour	 
	 */
	@Override
	public List<OfficeSettingTO> fetchOfficeSetting(Long enrollmentOfficeId)
			throws BaseException {

		try {
			return em
					.createQuery(
							"select ost from OfficeSettingTO ost "
									+ "where est.enrollmentOffice := enrollmentOfficeId",
							OfficeSettingTO.class)
					.setParameter("enrollmentOfficeId", enrollmentOfficeId)
					.getResultList();
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.ENI_008,
					DataExceptionCode.GLB_005_MSG, e);
		}

	}
	
	//Anbari
	private PersonDAO getPersonDAO() throws BaseException {
		try {
			return DAOFactoryProvider.getDAOFactory().getDAO(
					getDaoJNDIName(DAO_PERSON));
		} catch (DAOFactoryException e) {
			throw new ServiceException(BizExceptionCode.PSI_001,
					BizExceptionCode.GLB_001_MSG, e,
					new String[] { EMSLogicalNames.DAO_PERSON });
		}
	}

	@Override
	public EnrollmentOfficeTO findEnrollmentOfficeById(Long eofId)
			throws BaseException {
		List<EnrollmentOfficeTO> enrollmentOfficeList;
		try {
			enrollmentOfficeList = em.createNamedQuery("EnrollmentOfficeTO.findEnrollmentOfficeById")
					.setParameter("eofId", eofId)
					.getResultList();
		}catch (Exception e) {
				throw new DAOException(DataExceptionCode.ENI_013,
						DataExceptionCode.GLB_005_MSG, e);
			}
		return enrollmentOfficeList != null ? enrollmentOfficeList.get(0) : null;

	}
	
}