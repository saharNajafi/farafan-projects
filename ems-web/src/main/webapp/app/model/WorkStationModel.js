Ext.define("Ems.model.WorkStationModel", {
    extend: 'Gam.data.model.Model',
    fields: [
        {type: 'string', name: EmsObjectName.workstation.officeName},
        {type: 'string', name: EmsObjectName.workstation.worksationID},
        {type: 'string', name: EmsObjectName.workstation.checkUnCheck},
        {type: 'string', name: EmsObjectName.workstation.activationCode},
        {type: 'string', name: EmsObjectName.workstation.officeId},
        {type: 'string', name: EmsObjectName.workstation.officeCode},
        {type: 'string', name: EmsObjectName.workstation.provinceName}
    ]
});