
Ext.define('Ems.model.TokenRequestListModel', {
	extend: 'Gam.data.model.Model',
	fields: [
	         EmsObjectName.tokenRequest.id,
	         EmsObjectName.tokenRequest.tokenType ,
	         EmsObjectName.tokenRequest.tokenState ,
			 EmsObjectName.tokenRequest.tokenReason,
	         {name: EmsObjectName.tokenRequest.requestDate , type: 'date'},
	         {name: EmsObjectName.tokenRequest.issuanceDate , type: 'date'},
	         {name: EmsObjectName.tokenRequest.deliverDate , type: 'date'},
	         EmsObjectName.tokenRequest.adminName,
	         EmsObjectName.tokenRequest.departmentName


	         ]
});
