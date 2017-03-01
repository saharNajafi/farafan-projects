Ext.define("Ems.model.MessagesModel", {
	extend : 'Gam.data.model.Model',

	fields : [ EmsObjectName.messages.id, EmsObjectName.messages.title,
			EmsObjectName.messages.content,
			EmsObjectName.messages.msgStatus,
			EmsObjectName.messages.msgPriority, 
			EmsObjectName.messages.office,
			EmsObjectName.messages.province,
			EmsObjectName.messages.nocroffic,
			EmsObjectName.messages.department, 
			EmsObjectName.messages.person, 
			EmsObjectName.messages.senderUsername, 
//			{
//				name : EmsObjectName.messages.msgDate,
//				type : 'date'
//			},
			EmsObjectName.messages.createDate,
			EmsObjectName.messages.provinceName,
			EmsObjectName.messages.provinceId,
			EmsObjectName.messages.departmentId ,
			EmsObjectName.messages.departmentName ,
			EmsObjectName.messages.officeId,
			EmsObjectName.messages.officeName ,
			EmsObjectName.messages.nocrofficId ,
			EmsObjectName.messages.nocrofficName,
			EmsObjectName.messages.personId,
			EmsObjectName.messages.personName,
			
			EmsObjectName.messages.isAll ,
			EmsObjectName.messages.isManager,
			EmsObjectName.messages.isOffice,
			EmsObjectName.messages.isNocr,
			EmsObjectName.messages.preparedState,
			EmsObjectName.messages.attachFile,
			EmsObjectName.messages.hasFile

	]
});