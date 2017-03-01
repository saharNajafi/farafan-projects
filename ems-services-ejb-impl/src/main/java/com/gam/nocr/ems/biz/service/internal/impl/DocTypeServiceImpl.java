package com.gam.nocr.ems.biz.service.internal.impl;

import com.gam.commons.core.BaseException;
import com.gam.commons.core.biz.service.BizLoggable;
import com.gam.commons.core.biz.service.Permissions;
import com.gam.commons.core.biz.service.ServiceException;
import com.gam.commons.core.data.dao.factory.DAOFactoryException;
import com.gam.commons.core.data.dao.factory.DAOFactoryProvider;
import com.gam.nocr.ems.biz.service.EMSAbstractService;
import com.gam.nocr.ems.config.BizExceptionCode;
import com.gam.nocr.ems.data.dao.DocTypeDAO;
import com.gam.nocr.ems.data.dao.ServiceDocumentTypeDAO;
import com.gam.nocr.ems.data.domain.DocumentTypeTO;
import com.gam.nocr.ems.data.domain.ServiceDocumentTypeTO;
import com.gam.nocr.ems.data.domain.vol.DocTypeVTO;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

import static com.gam.nocr.ems.config.EMSLogicalNames.*;

/**
 * @author: Haeri (haeri@gamelectronics.com)
 */
@Stateless(name = "DocTypeService")
@Local(DocTypeServiceLocal.class)
@Remote(DocTypeServiceRemote.class)
public class DocTypeServiceImpl extends EMSAbstractService implements DocTypeServiceLocal, DocTypeServiceRemote {//todo: add permissions

    @Resource
    private SessionContext sessionContext;

    // Conversions
    private DocumentTypeTO extractDocType(DocTypeVTO vto) {
        DocumentTypeTO to = new DocumentTypeTO();
        to.setId(vto.getId());
        to.setName(vto.getDocTypeName());
        return to;
    }

    // Internal checks
    private void checkDocTypeVTO(DocTypeVTO vto) throws BaseException {
        if (vto == null)
            throw new ServiceException(BizExceptionCode.DTS_001, BizExceptionCode.DTS_001_MSG);
        if (vto.getDocTypeName() == null || vto.getDocTypeName().trim().equals(""))
            throw new ServiceException(BizExceptionCode.DTS_002, BizExceptionCode.DTS_002_MSG);
        if (vto.getServices() == null || vto.getServices().split(",").length == 0)
            throw new ServiceException(BizExceptionCode.DTS_005, BizExceptionCode.DTS_005_MSG);
    }

    // DAOs
    private DocTypeDAO getDocTypeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_DOC_TYPE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DTS_003, BizExceptionCode.GLB_001_MSG, e, DAO_DOC_TYPE.split(","));
        }
    }

    private ServiceDocumentTypeDAO getServiceDocumentTypeDAO() throws BaseException {
        try {
            return DAOFactoryProvider.getDAOFactory().getDAO(getDaoJNDIName(DAO_SERVICE_DOCTYPE));
        } catch (DAOFactoryException e) {
            throw new ServiceException(BizExceptionCode.DTS_004, BizExceptionCode.GLB_001_MSG, e, DAO_SERVICE_DOCTYPE.split(","));
        }
    }

    @Override
    @Permissions(value = "ems_editDocType || ems_addDocType")
    @BizLoggable(logAction = "INSERT", logEntityName = "DOC_TYPE")
    public Long save(DocTypeVTO vto) throws BaseException {
        try {
            checkDocTypeVTO(vto);
            DocumentTypeTO docType = getDocTypeDAO().create(extractDocType(vto));

            ServiceDocumentTypeDAO serviceDocumentTypeDAO = getServiceDocumentTypeDAO();
            for (String serviceId : vto.getServices().split(",")) {
                serviceDocumentTypeDAO.create(new ServiceDocumentTypeTO(docType, Integer.parseInt(serviceId)));
            }

            return docType.getId();
        } catch (BaseException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    @Override
    @Permissions(value = "ems_editDocType")
    @BizLoggable(logAction = "UPDATE", logEntityName = "DOC_TYPE")
    public Long update(DocTypeVTO vto) throws BaseException {
        checkDocTypeVTO(vto);
        if (vto.getId() == null)
            throw new ServiceException(BizExceptionCode.DTS_007, BizExceptionCode.DTS_007_MSG);
        DocumentTypeTO docType = extractDocType(vto);
        docType = getDocTypeDAO().update(docType);
        ServiceDocumentTypeDAO serviceDocumentTypeDAO = getServiceDocumentTypeDAO();
        List<Long> docTypeIds = new ArrayList<Long>();
        docTypeIds.add(docType.getId());
        serviceDocumentTypeDAO.deleteByDocType(docTypeIds);
        for (String serviceId : vto.getServices().split(",")) {
            serviceDocumentTypeDAO.create(new ServiceDocumentTypeTO(docType, Integer.parseInt(serviceId)));
        }
        return docType.getId();
    }

    @Override
    @Permissions(value = "ems_viewDocType")
    @BizLoggable(logAction = "LOAD", logEntityName = "DOC_TYPE")
    public DocTypeVTO load(Long docTypeId) throws BaseException {
        if (docTypeId == null)
            throw new ServiceException(BizExceptionCode.DTS_006, BizExceptionCode.DTS_006_MSG);
        DocumentTypeTO docType = getDocTypeDAO().find(DocumentTypeTO.class, docTypeId);
        if (docType == null)
            throw new ServiceException(BizExceptionCode.DTS_008, BizExceptionCode.DTS_008_MSG, new Long[]{docTypeId});
        List<ServiceDocumentTypeTO> sdts = getServiceDocumentTypeDAO().findByDocumentTypeId(docTypeId);
        List<Integer> sdtIds = new ArrayList<Integer>();
        for (ServiceDocumentTypeTO sdt : sdts) {
            sdtIds.add(sdt.getServiceId());
        }
        return new DocTypeVTO(docTypeId, docType.getName(), sdtIds);
    }

    @Override
    @Permissions(value = "ems_removeDocType")
    @BizLoggable(logAction = "DELETE", logEntityName = "DOC_TYPE")
    public boolean remove(String docTypeIds) throws BaseException {
        if (docTypeIds == null || docTypeIds.trim().length() == 0)
            throw new ServiceException(BizExceptionCode.DTS_009, BizExceptionCode.DTS_009_MSG);
        String[] ids = docTypeIds.split(",");
        List<Long> idsList = new ArrayList<Long>();
        try {
            for (String id : ids) {
                idsList.add(Long.parseLong(id));
            }
        } catch (NumberFormatException e) {
            throw new ServiceException(BizExceptionCode.DTS_010, BizExceptionCode.DTS_010_MSG);
        }
        // Foreign key in service documentType refers to docType but is set to cascade on delete in DB
        // so there is no need to delete service documentType manually
        return getDocTypeDAO().deleteDocTypes(idsList) == idsList.size();
    }
}
