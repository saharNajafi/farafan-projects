package com.gam.nocr.ems.data.dao.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.data.dao.DAOException;
import com.gam.nocr.ems.config.DataExceptionCode;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.ws.ProvinceWTO;
import com.gam.nocr.ems.data.enums.LocationType;
import com.gam.nocr.ems.sharedobjects.GeneralCriteria;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:saadat@gamelectronics.com">Alireza Saadat</a>
 */
@Stateless(name = "LocationDAO")
@Local(LocationDAOLocal.class)
@Remote(LocationDAORemote.class)
public class LocationDAOImpl extends EmsBaseDAOImpl<LocationTO> implements LocationDAOLocal, LocationDAORemote {

    private static final String UNIQUE_KEY_PVC_NAME = "AK_PVC_NAME";

    @Override
    @PersistenceContext(unitName = "EmsOraclePU")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long fetchModifiedLocationCount(LocationType locationType) throws BaseException {
        try {
            String query = "SELECT COUNT(LOC.id) " +
                    "FROM LocationTO LOC " +
                    "WHERE LOC.modified = :MODIFIED_FLAG ";


            switch (locationType) {
                case PROVINCE:
                    query += "and LOC.province is null " +
                            "and LOC.county is null " +
                            "and LOC.township is null " +
                            "and LOC.district is null";
                    break;
                case COUNTY:
                    query += "and LOC.province is not null " +
                            "and LOC.county is null " +
                            "and LOC.township is null " +
                            "and LOC.district is null";
                    break;
                case TOWNSHIP:
                    query += "and LOC.province is not null " +
                            "and LOC.county is not null " +
                            "and LOC.township is null " +
                            "and LOC.district is null";
                    break;
                case DISTRICT:
                    query += "and LOC.province is not null " +
                            "and LOC.county is not null " +
                            "and LOC.township is not null " +
                            "and LOC.district is null";
                    break;
                case CITY:
                    query += "and LOC.province is not null " +
                            "and LOC.county is not null " +
                            "and LOC.township is not null " +
                            "and LOC.district is not null";
                    break;
            }
            return em.createQuery(query, Long.class)
                    .setParameter("MODIFIED_FLAG", true)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.LDI_001, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method findModifiedProvinces is used to find the new or the modified instances of type {@link
     * com.gam.nocr.ems.data.domain.LocationTO}
     *
     * @return an instance of type {@link com.gam.nocr.ems.data.domain.LocationTO}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public List<LocationTO> findModifiedLocationsByType(LocationType locationType, Integer from, Integer to) throws BaseException {
        try {
            String query = "SELECT LOC " +
                    "FROM LocationTO LOC " +
                    "WHERE LOC.modified = :MODIFIED_FLAG ";


            switch (locationType) {
                case PROVINCE:
                    query += "and LOC.province is null " +
                            "and LOC.county is null " +
                            "and LOC.township is null " +
                            "and LOC.district is null ";
                    break;
                case COUNTY:
                    query += "and LOC.province is not null " +
                            "and LOC.county is null " +
                            "and LOC.township is null " +
                            "and LOC.district is null ";
                    break;
                case TOWNSHIP:
                    query += "and LOC.province is not null " +
                            "and LOC.county is not null " +
                            "and LOC.township is null " +
                            "and LOC.district is null ";
                    break;
                case DISTRICT:
                    query += "and LOC.province is not null " +
                            "and LOC.county is not null " +
                            "and LOC.township is not null " +
                            "and LOC.district is null ";
                    break;
                case CITY:
                    query += "and LOC.province is not null " +
                            "and LOC.county is not null " +
                            "and LOC.township is not null " +
                            "and LOC.district is not null ";
                    break;
            }
            query += "ORDER BY LOC.id";

            return em.createQuery(query, LocationTO.class)
                    .setParameter("MODIFIED_FLAG", true)
//                    .setFirstResult(from)
                    .setMaxResults(to)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.LDI_002, DataExceptionCode.GLB_005_MSG, e);
        }
    }

    /**
     * The method updateModifiedFields is used to update the field 'modified' of a list of type {@link
     * com.gam.nocr.ems.data.domain.LocationTO}
     *
     * @param idList is a list of type {@link Long}
     * @param flag   is an instance of type {@link Boolean}
     * @throws com.gam.commons.core.BaseException
     *
     */
    @Override
    public void updateModifiedFields(List<Long> idList,
                                     Boolean flag) throws BaseException {
        try {
            em.createQuery("UPDATE LocationTO LOC " +
                    "SET LOC.modified = :MODIFIED_FLAG " +
                    "WHERE LOC.id IN (:ID_LIST)")
                    .setParameter("MODIFIED_FLAG", flag)
                    .setParameter("ID_LIST", idList)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException(DataExceptionCode.LDI_003, DataExceptionCode.GLB_006_MSG, e);
        }
    }
    /**
     * @author ganjyar
     * @param generalCriteria
     * @return
     * @throws BaseException
     */
	@Override
	public List<ProvinceWTO> fetchLocationLivingList(
			GeneralCriteria generalCriteria) throws BaseException {

		try {

			String parentId=null;
			String livingType=null;
			Map<String, Object> parameters = generalCriteria.getParameters();
			if(parameters.get("parentId")!=null)
			{
			 parentId = parameters.get("parentId").toString();
			}
			if(parameters.get("livingType")!=null)
			{
			livingType = parameters.get("livingType").toString();
			}
			StringBuffer queryBuffer = new StringBuffer(
					"select "
							+ " loc1.loc_id,"
							+ " loc1.loc_name,"
							+ " (case when loc1.loc_type = 1 then loc1.loc_township_id "
							+ " when loc1.loc_type = 2 then loc1.loc_district_id "
							+ " when loc1.loc_type = 7 then loc1.loc_township_id "
							+ " when loc1.loc_type = 6 then loc1.loc_county_id "
							+ " when loc1.loc_type = 4 then null"
							+ " when loc1.loc_type = 5 then loc1.loc_province_id "
							+ " else loc1.loc_id end) loc_pid"
							+ " from emst_location loc1"
							+ " where loc1.loc_TYPE not in (3)"
							+ " and loc1.loc_STATE not in ('I')");
			if (parentId == null)
				queryBuffer.append(" and (loc1.loc_type = 4)");
			else {

				if (livingType == null) {
					queryBuffer
							.append(" and (loc1.loc_type = 2 and loc1.loc_district_id = :parentId) or "
									+ "(loc1.loc_type = 6 and loc1.loc_county_id = :parentId) or "
									+ "(loc1.loc_type = 5 and loc1.loc_province_id = :parentId) ");

				} else {
					// 1 -----> shahr
					if (livingType.equals("1")) {

						queryBuffer
								.append(" and loc1.loc_type = 1 and loc1.loc_township_id = :parentId");

					}
					// 0 ------> dehestan
					else if (livingType.equals("0")) {
						queryBuffer
								.append(" and loc1.loc_type = 7 and loc1.loc_township_id = :parentId");

					}

				}
			}

			queryBuffer.append(" order by loc1.loc_name asc");

			Query query = em.createNativeQuery(queryBuffer.toString());
			if (parentId != null)
				query.setParameter("parentId", parentId);
			List resultList = query.getResultList();
			List<ProvinceWTO> result = new ArrayList<ProvinceWTO>();
			if (resultList != null) {
				for (Object record : resultList) {
					Object[] data = (Object[]) record;
					ProvinceWTO obj = new ProvinceWTO();
					obj.setId(((BigDecimal) data[0]).longValue());
					obj.setName((String) data[1]);
					
					
					obj.setParentId( data[2] ==null ?null:  ((BigDecimal) data[2]).longValue());
					result.add(obj);
				}
			}
			return result;
		} catch (Exception e) {
			throw new DAOException(DataExceptionCode.LDI_004,
					DataExceptionCode.GLB_006_MSG, e);
		}
	}
}
