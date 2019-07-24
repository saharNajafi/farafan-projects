package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.BatchTO;
import com.gam.nocr.ems.data.domain.vol.BatchDispatchInfoVTO;
import com.gam.nocr.ems.data.enums.CardState;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;
import com.gam.nocr.ems.util.EmsUtil;
import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.DataException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import javax.persistence.Query;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:haeri@gamelectronics.com.com">Nooshin Haeri</a>
 */
@Stateless(name = "BatchDAO")
@Local(BatchDAOLocal.class)
public class BatchDAOImpl extends EmsBaseDAOImpl<BatchTO> implements BatchDAOLocal, BatchDAORemote {

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<BatchTO> findAll() throws BaseException {
        try {
            return em.createQuery("select bat from BatchTO bat", BatchTO.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.BAI_001, DataExceptionCode.BAI_001_MSG, e);
        }
    }
    /**
	 * this method count batches which are missed
	 * @author ganjyar
	*/
	@Override
	public Integer countBatchLostTemp(GeneralCriteria criteria)
			throws BaseException {

		try {
			StringBuffer stringBuffer = new StringBuffer(
					"select distinct"
							+ " count(*) "
							+ " from EMST_BATCH bt,EMST_DISPATCH_INFO df "
							+ " where df.DPI_LOST_DATE is not null and"
							+ " bt.BAT_STATE = :state and df.DPI_CONTAINER_ID = bt.BAT_ID");

			if (criteria.getParameters() != null) {
				Set<String> keySet = criteria.getParameters().keySet();
				if (keySet != null) {
					if (keySet.contains("checkSecurity")) {
						stringBuffer
								.append(" and df.dpi_receiver_dep_id in" +
										" (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start " +
										"with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id="+criteria.getParameters().get("perid") +
										" union select e.eof_id from emst_enrollment_office e where eof_is_deleted = 0 connect by " +
										"prior e.eof_id=e.eof_superior_office start with" +
										" e.eof_id in (select p.per_dep_id from emst_person p where p.per_id="+criteria.getParameters().get("perid")+" ))) ");
					}
					for (String key : keySet) {
						if ("cmsID".equals(key)) {
							stringBuffer.append(" and bt.bat_cms_id  like '"
									+ criteria.getParameters().get(key) + "'");
						}
						if ("fromLostDate".equals(key)) {
							stringBuffer
									.append(" and df.DPI_LOST_DATE > to_date('"
											+ criteria.getParameters().get(key)
											+ "', 'YYYY/MM/DD HH24:MI')");
						}
						if ("toLostDate".equals(key)) {
							stringBuffer
									.append(" and df.DPI_LOST_DATE < to_date('"
											+ criteria.getParameters().get(key)
											+ "', 'YYYY/MM/DD HH24:MI')");
						}
						if ("waitingToConfirmed".equals(key)) {
							stringBuffer
									.append(" and (bt.BAT_LOSTCONFIRM ="+criteria.getParameters().get(key)
											+" or bt.BAT_LOSTCONFIRM is null)");
						}
						if ("confirmed".equals(key)) {
							stringBuffer
									.append(" and bt.BAT_LOSTCONFIRM ="+criteria.getParameters().get(key));
						}
					}

				}
			}

			Query query = em.createNativeQuery(stringBuffer.toString());
			query.setParameter("state", CardState.SHIPPED.toString());
			Number number = (Number) query.getSingleResult();
			if (number != null) {
				return number.intValue();
			}

		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CAI_014,
					DataExceptionCode.GLB_006_MSG, e);
		}
		return null;
	}

	/**
	 * this method fetches batches which are missed
	 * 
	 * @author ganjyar
	 */
	@Override
	public List<BatchDispatchInfoVTO> fetchBatchLostTempList(
			GeneralCriteria criteria) throws BaseException {

		try {
			StringBuffer stringBuffer = new StringBuffer(
					"select distinct"
							+ " bt.bat_id,"
							+ " bt.bat_cms_id,"
							+ " df.dpi_lost_date,"
							+ " bt.bat_lostconfirm "
							+ " from emst_batch bt,emst_dispatch_info df "
							+ " where df.dpi_lost_date is not null and"
							+ " bt.bat_state = :state and df.dpi_container_id = bt.bat_id");

			if (criteria.getParameters() != null) {
				Set<String> keySet = criteria.getParameters().keySet();
				if (keySet != null) {
					if (keySet.contains("checkSecurity")) {
						stringBuffer
								.append(" and df.dpi_receiver_dep_id in" +
										" (select dp.dep_id from emst_department dp connect by prior dp.dep_id=dp.dep_parent_dep_id start " +
										"with dp.dep_id in (select pr.per_dep_id from emst_person pr where pr.per_id="+criteria.getParameters().get("perid") +
										" union select e.eof_id from emst_enrollment_office e where eof_is_deleted = 0 connect by " +
										"prior e.eof_id=e.eof_superior_office start with" +
										" e.eof_id in (select p.per_dep_id from emst_person p where p.per_id="+criteria.getParameters().get("perid")+" ))) ");
					}
					for (String key : keySet) {
						if ("cmsID".equals(key)) {
							stringBuffer.append(" and bt.bat_cms_id  like '"
									+ criteria.getParameters().get(key) + "'");
						}
						if ("fromLostDate".equals(key)) {
							stringBuffer
									.append(" and df.dpi_lost_date > to_date('"
											+ criteria.getParameters().get(key)
											+ "', 'YYYY/MM/DD HH24:MI')");
						}
						if ("toLostDate".equals(key)) {
							stringBuffer
									.append(" and df.dpi_lost_date < to_date('"
											+ criteria.getParameters().get(key)
											+ "', 'YYYY/MM/DD HH24:MI')");
						}
						if ("waitingToConfirmed".equals(key)) {
							stringBuffer
									.append(" and (bt.BAT_LOSTCONFIRM ="+criteria.getParameters().get(key)
											+" or bt.BAT_LOSTCONFIRM is null)");
						}
						if ("confirmed".equals(key)) {
							stringBuffer
									.append(" and bt.BAT_LOSTCONFIRM ="+criteria.getParameters().get(key));
						}
					}

				}
			}

			if (EmsUtil.checkString(criteria.getOrderBy())) {
				String orderBy = criteria.getOrderBy();
				String sortKey = "bt.bat_id";
				String dir = "asc";
				String[] split = orderBy.split(" ");
				if (split.length >= 2) {
					sortKey = split[0].trim();
					dir = split[1].trim();
				}

				if ("cmsID".equals(sortKey)) {
					sortKey = "bt.bat_cms_id";
				} else if ("batchId".equals(sortKey)) {
					sortKey = "df.dpi_lost_date";
				} else if ("isConfirm".equals(sortKey)) {
					sortKey = "crd.crd_lostconfirm";
				} else if ("batchLostDate".equals(sortKey)) {
					sortKey = "df.dpi_lost_date";
				}
				stringBuffer.append(" order by ").append(sortKey).append(" ")
						.append(dir);
			} else {
				stringBuffer.append(" order by bt.bat_id asc ");
			}

			Query query = em.createNativeQuery(stringBuffer.toString());
			query.setParameter("state", CardState.SHIPPED.toString());

			query.setMaxResults(criteria.getPageSize()).setFirstResult(
					criteria.getPageNo() * criteria.getPageSize());

			List resultList = query.getResultList();
			List<BatchDispatchInfoVTO> result = new ArrayList<BatchDispatchInfoVTO>();
			if (resultList != null) {
				for (Object record : resultList) {
					Object[] data = (Object[]) record;
					BatchDispatchInfoVTO obj = new BatchDispatchInfoVTO();
					obj.setId(((BigDecimal) data[0]).longValue());
					obj.setCmsID(((String) data[1]));
					obj.setBatchLostDate((Timestamp) data[2]);
					if (data[3] == null)
						obj.setIsConfirm("0");
					else

						obj.setIsConfirm(((BigDecimal) data[3]).toString());
					result.add(obj);
				}
			}
			return result;

		} catch (Exception e) {
			throw new DataException(DataExceptionCode.CAI_015,
					DataExceptionCode.GLB_006_MSG, e);
		}
	}
}
