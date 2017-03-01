package com.gam.nocr.ems.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gam.commons.core.BaseException;
import com.gam.nocr.ems.data.domain.EnrollmentOfficeTO;
import com.gam.nocr.ems.data.domain.LocationTO;
import com.gam.nocr.ems.data.domain.PersonTO;
import com.gam.nocr.ems.data.domain.PreparedMessageTO;

@Stateless(name = "PreparedMessageDAO")
@Local(PreparedMessageDAOLocal.class)
@Remote(PreparedMessageDAORemote.class)
public class PreparedMessageDAOImpl extends EmsBaseDAOImpl<PreparedMessageTO>
		implements PreparedMessageDAOLocal, PreparedMessageDAORemote {

	@Override
	@PersistenceContext(unitName = "EmsOraclePU")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public PreparedMessageTO loadById(Long id) throws BaseException {

		PreparedMessageTO messageTO = find(PreparedMessageTO.class, id);

		List<LocationTO> provinces = new ArrayList<LocationTO>();
		List<EnrollmentOfficeTO> offices = new ArrayList<EnrollmentOfficeTO>();
		List<PersonTO> persons = new ArrayList<PersonTO>();

		// load lazy value
		if (messageTO != null) {

			messageTO.getContent();
			messageTO.getAttachFile();

			if (messageTO.getProvinces().size() > 0) {
				for (LocationTO loc : messageTO.getProvinces()) {
					LocationTO newLoc = new LocationTO();
					newLoc.setName(loc.getName());
					newLoc.setId(loc.getId());
					provinces.add(newLoc);
				}
			}

			if (messageTO.getOffices().size() > 0) {
				for (EnrollmentOfficeTO eof : messageTO.getOffices()) {
					EnrollmentOfficeTO newEof = new EnrollmentOfficeTO();
					newEof.setName(eof.getName());
					newEof.setId(eof.getId());
					offices.add(newEof);
				}
			}

			if (messageTO.getPersons().size() > 0) {
				for (PersonTO per : messageTO.getPersons()) {
					PersonTO newPer = new PersonTO();
					newPer.setFirstName(per.getFirstName());
					newPer.setLastName(per.getLastName());
					newPer.setId(per.getId());
					persons.add(newPer);
				}
			}

		}

		messageTO.setProvinces(provinces);
		messageTO.setOffices(offices);
		messageTO.setPersons(persons);

		return messageTO;
	}

}
