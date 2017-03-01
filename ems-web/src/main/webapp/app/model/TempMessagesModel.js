Ext.define("Ems.model.TempMessagesModel", {
	extend : 'Ext.data.Model',
	fields : [ EmsObjectName.messages.title,
			EmsObjectName.messages.content,
			EmsObjectName.messages.msgStatus,
			EmsObjectName.messages.msgPriority, EmsObjectName.messages.office,
			EmsObjectName.messages.province, EmsObjectName.messages.nocroffic,
			EmsObjectName.messages.department, EmsObjectName.messages.person,

			EmsObjectName.messages.department, EmsObjectName.messages.person, {
				name : EmsObjectName.messages.msgDate,
				type : 'date'
			},

			EmsObjectName.messages.provinceName,
			EmsObjectName.messages.provinceId,
			EmsObjectName.messages.departmentId,
			EmsObjectName.messages.departmentName,
			EmsObjectName.messages.officeId, EmsObjectName.messages.officeName,
			EmsObjectName.messages.nocrofficId,
			EmsObjectName.messages.nocrofficName,
			EmsObjectName.messages.personId, EmsObjectName.messages.personName,
			
			EmsObjectName.messages.isAll ,
			EmsObjectName.messages.isManager,
			EmsObjectName.messages.isOffice,
			EmsObjectName.messages.isNocr,
//			EmsObjectName.messages.preparedState,
			
			EmsObjectName.messages.provinces,
			EmsObjectName.messages.offices,
			EmsObjectName.messages.persons

	// {type: 'string', name: EmsObjectName.workstation.worksationID},
	// {type: 'string', name: EmsObjectName.workstation.checkUnCheck},
	// {type: 'string', name: EmsObjectName.workstation.activationCode},
	// {type: 'string', name: EmsObjectName.workstation.officeId}

	]
});