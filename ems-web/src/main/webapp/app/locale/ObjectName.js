Ext.define('Ems.locale.objectName', {
	singleton : true,
	alternateClassName : 'EmsObjectName',
	alias : 'localeprovider.Ems',

	//

	actionColumnStatusToken : {
		status : 'tokenStatus'
	},


//	khodayari
	_oldOfficeValues :{
		setKhosusiType :'',
		setSuperiorOffice : '',
		setSuperiorOfficeID : ''

	},
	userForm : {
		id : 'id',
		firstName : 'firstName',
		lastName : 'lastName',
		nationalCode : 'nid',
		certificateId : 'birthCertNum',
		certificateSerial : 'birthCertSeries',
		fatherName : 'fatherName',
		userName : 'userName',
		password : 'password',
		repeatPassword : 'cPassword',
		departmentName : 'departmentName',
		departmentId : 'departmentId',
		lastLoginDate : 'lastLogin',
		userRole : 'roles',
		userAccess : 'permissions',
		email : 'email',
		userActive : 'lStatus',
		provinceName: 'provinceName',

		requestStatus : 'requestStatusString',

		g_firstName : 'g_firstName',
		g_lastName : 'g_lastName',
		g_nationalCode : 'g_nid',
		g_userName : 'g_userName',
		g_departmentName : 'g_departmentName',
		g_requestStatus : 'g_requestStatusString',
		g_lastLoginDate : 'g_lastLogin',

		r_firstName : 'r_firstName',
		r_lastName : 'r_lastName',
		r_userName : 'r_userName',
		r_fatherName : 'r_fatherName',
		r_nationalCode : 'r_nid',
		r_certificateSerial : 'r_birthCertSeries',
		r_certificateId : 'r_birthCertNum',
		r_email : 'r_email',
		r_departmentName : 'r_departmentName'

	},

	//

	capacity: {
		startDate: 'startDate',
		endDate: 'endDate',
		capacity: 'capacity',
		shiftNo: 'shiftNo',
		id: 'id',
		isActive: 'isActive',
        workingHoursFrom: 'workingHoursFrom',
        workingHoursTo: 'workingHoursTo',
		enrollmentOfficeId: 'enrollmentOfficeId'
	},

	suerList : {
		firstName : 'firstName',
		lastName : 'lastName',
		nid : 'nid',
		username : 'userName'
	},

	userRequestToken : {
		status : 'tokenState',
		code : 'tokenType',
		deleteToken : 'deleteToken'
	},

	//

	officeNewEdit : {
		oficName : 'name',
		oficCode : 'code',
		oficMeter : 'area',
		oficFax : 'fax',
		oficTel : 'phone',
		oficTokenExport : 'ssl',
		oficSuperRegisterOffice : 'parentName',
		oficSuperRegisterOfficeId : 'parentId',
		oficPostCode : 'zip',
		oficAddress : 'address',
		oficCapacity: 'capacity',
		// oficRating : 'rate',
		// oficRatingId : 'rateId',
		workingHoursStartId : 'workingHoursStart',
		workingHoursFinishId : 'workingHoursFinish',
		officeType : 'officeType',
		officeDeliver : 'officeDeliver',
		officeTypeNOCR : 'officeTypeNOCR',
		officeTypeOffice : 'officeTypeOffice',
		officeDeliverDiable : 'officeDeliverDiable',
		officeDeliverEnable : 'officeDeliverEnable',
		superiorOfficeId : 'superiorOfficeId',
		superiorOfficeName : 'superiorOfficeName',
		fqdn : 'capacity',
		uploadPhoto : 'uploadPhoto',
		fingerScanSingleMode : 'fingerScanSingleMode',
		disabilityMode : 'disabilityMode',
		reIssueRequest : 'reIssueRequest',
		elderlyMode : 'elderlyMode',
		calenderType : 'calenderType',
		calenderTypeTmp : 'calenderTypeTmp',
		officeID : 'id',
		provinceName: 'provinceName',
		active: 'active',
        postDestinationCode: 'postDestinationCode',
        postNeeded: 'postNeeded',

		khosusiType : 'khosusiType',

		mangName : 'managerName',
		mangId : 'managerId',
		mangTel : 'managerPhone',
		mangMobil : 'managerMobile',

		tokenId : 'tokenId',
		tokenStatus : 'tokenState',

		locationId : 'locId',
		location : 'locName',
		accessViewAndChangeOfficeSetting : 'accessViewAndChangeOfficeSetting',

		g_oficName : 'g_name',
		g_mangName : 'g_managerName',
		g_oficSuperRegisterOffice : 'g_parentName',
		g_oficCode : 'g_code',
		g_oficAddress : 'g_address',
		g_oficTel : 'g_phone',
		g_oficcapacity : 'g_capacity',
		// g_oficRating : 'g_rate',
		g_tokenStatus : 'g_tokenState',
		g_superiorOfficeName : 'g_superiorOfficeName',
		g_fqdn : 'g_fqdn',

		g_workingHoursStartId : 'g_workingHoursStart',
		g_workingHoursFinishId : 'g_workingHoursFinish',
		g_officeType : 'g_officeType',
		g_officeDeliver : 'g_officeDeliver',

		g_khosusiType : 'g_khosusiType',
		officeDepPhoneNumber: 'depPhoneNumber',
		thursdayMorningActive : 'thursdayMorningActive',
		thursdayEveningActive : 'thursdayEveningActive',
		fridayMorningActive : 'fridayMorningActive',
		fridayEveningActive : 'fridayEveningActive',
		singleStageOnly: 'singleStageOnly',
		hasStair: 'hasStair',
		hasElevator: 'hasElevator',
		hasPortabilityEquipment: 'hasPortabilityEquipment'
	},

	//

	comboBox : {
		code : 'code',
		name : 'name'
	},

	AutocompleteManagement : {
		mangMobil : 'managerMobile',
		mangTel : 'managerPhone'
	},

	//

	dispatchGrid : {
		id : 'id',

		batchTotal : 'totalBox',
		receiverCodeBox : 'nextReceiverId',
		cartTotal : 'cartTotal',
		branchCode : 'branchCode',
		typeReceiver : 'receivedType',
		typeSend : 'sendType',

		// Grid
		mySendDateBox : 'mySendDate',
		batchNumber : 'itemCount',
		numberBox : 'cmsId',
		receiverBox : 'nextReceiverName',
		sendDateBox : 'dispatchSentDate',
		receiveDateBox : 'dispatchReceiveDate',
		lostDateBox : 'dispatchLostDate',
		statusBox : 'status',
		action : 'action',

		detail_mySendDateBox : 'detail_mySendDate',
		detail_batchNumber : 'detail_itemCount',
		detail_numberBox : 'detail_cmsId',
		detail_receiverBox : 'detail_nextReceiverName',
		detail_sendDateBox : 'detail_dispatchSentDate',
		detail_receiveDateBox : 'detail_dispatchReceiveDate',
		detail_statusBox : 'detail_status',
		detail_action : 'detail_action'

	},
	lostCard :{
		cardId : 'cardId',
		nationalId:'nationalId',
		lname: 'lname',
		fname: 'fname',
		crn : 'crn',
		cardLostDate : 'cardLostDate',
		citizenFirstName : 'citizenFirstName',
		citizenLastName : 'citizenLastName',
		batchId : 'batchId',
		isConfirm  :'isConfirm'
	},
	lostBatch :{
		batchId : 'batchId',
		cmsID : 'cmsID',
		departmentName : 'departmentName',
		batchLostDate : 'batchLostDate',
		isConfirm  :'isConfirm'
	},
	//

	Rating : {
		clazz : 'clazz',
		size : 'size'
	},
	//
	Department : {
		parentDepartment : 'parentDepartment',
		code : 'code',
		name : 'name',
		address : 'address',
		postalCode : 'postalCode',
		dn : 'dn',
		parentDN : 'parentName',
		parentDNEditor : 'parentNameEditor',
		parentDNId : 'parentId',
		dispatchSendType : 'sendType',
		dispatchSendTypeId : 'sendTypeId',
		location : 'locName',
		locationId : 'locId',
		locationEditor : 'locationEditor',
		provinceName: 'provinceName',
	},
	//

	blackList : {
		firstName : 'firstName',
		lastName : 'surname',
		nid : 'nationalId'
	},

	//

	holidayList : {
		id : 'id',
		holiday : 'holiday',
	},

	userRequestTokenForm : {
		tokenType : "tokenType",
		reasonDeleteToken : 'reasonDeleteToken'
	},

	//

	userRequestTokenDeleteTokenForm : {
		descriptionDeleteToken : 'descriptionDeleteToken',
		reasonDelete : 'reasonDelete',
		replacedEnrollmentOfficeId : 'substitutionOfficeId'

	},

	holidayList: {
		id: 'id',
		holiday: 'holiday',
	},


	userRequestTokenForm: {
		tokenType: "tokenType",
		reasonDeleteToken: 'reasonDeleteToken'
	},


	// hossein 8feature start
	workstation: {
		id: 'id',
		officeName: 'enrollmentOfficeName',
		officeId: 'enrollmentOfficeId',
		officeCode: 'enrollmentOfficeCode',
		worksationID: 'code',
		checkUnCheck: 'lState',
		activationCode: 'activationCode',
		provinceName: 'provinceName'
	},
	// hossein 8feature end

	docType : {
		name : 'name',
		service : 'services'
	},
	//
	bizLog : {
		date : 'date',
		actor : 'actor',
		actionNameStv : 'actionNameStr',
		entityNameStv : 'entityNameStr',
		entityID : 'entityID',
		additionalData : 'additionalData',
		status : 'status'
	},

	manageReports : {
		name : 'name',
		comment : 'comment',
		permission : 'permission',
		activationFlag : 'activationFlag'
	},

	reportRequest : {
		name : 'name',
		comment : 'comment',
		pdf : 'PDF',
		pdfLabel : 'PDF',
		xls : 'XLS',
		xlsLabel : 'XLS'
	},

	reportResult : {
		reportName : 'reportName',
		requestDate : 'requestDate',
		generateDate : 'generateDate',
		jobScheduleDate : 'jobScheduleDate',
		requestState : 'requestState',
		requestOutputType : 'requestOutputType'
	},

	reportResponse : {
		title : 'title',
		count : 'count',
		g_title : 'title',
		g_count : 'count'
	},

	defineUserAndDepartment : {
		oldPassword : 'oldPassword',
		newPassword : 'newPassword',
		confirmNewPassword : 'confirmNewPassword'
	},

	cardRequestList : {
		citizenFirstName : 'citizenFirstName',
		citizenSurname : 'citizenSurname',
		citizenNId : 'citizenNId',
		enrollmentOfficeName : 'enrollmentOfficeName',
		enrolledDate : 'enrolledDate',
		portalEnrolledDate : 'portalEnrolledDate',
		cardRequestState : 'cardRequestState',
		trackingId : 'trackingId',
		cardType : 'cardType',
		cardState : 'cardState',
		requestedAction : 'requestedAction',
		requestOrigin : 'requestOrigin',
		deliveredOfficeName : 'deliveredOfficeName',
		deliveredDate : 'deliveredDate',
		priority : 'priority',
		enrollmentOfficeId:'enrollmentOfficeId',


		//hossein 8 feature start
		reservationDate: 'reservationDate',
		documentFlag: 'documentFlag',
		faceFlag: 'faceFlag',
		fingerFlag: 'fingerFlag',
		//hossein 8 feature end


		/* Card Request Detail dialogue */
		citizenBirthDate: 'citizenBirthDate',
		citizenBirthDateSOL: 'citizenBirthDateSOL',
		citizenBirthDateLUN: 'citizenBirthDateLUN',
		gender: 'gender',
		religion: 'religion',
		birthCertiIssuancePlace: 'birthCertiIssuancePlace',
		postalCode: 'postalCode',
		livingProvinceName: 'livingProvinceName',
		livingCityName: 'livingCityName',
		phone: 'phone',
		mobile: 'mobile',
		address: 'address',
		fatherBirthCertID: 'fatherBirthCertID',
		fatherName: 'fatherName',
		fatherNID: 'fatherNID',
		motherBirthCertID: 'motherBirthCertID',
		motherName: 'motherName',
		motherNID: 'motherNID',

		/* Card Request Detail dialogue */
		citizenBirthDate: 'citizenBirthDate',
		citizenBirthDateSOL: 'citizenBirthDateSOL',
		citizenBirthDateLUN: 'citizenBirthDateLUN',
		gender: 'gender',
		religion: 'religion',
		birthCertiIssuancePlace: 'birthCertiIssuancePlace',
		postalCode: 'postalCode',
		livingProvinceName: 'livingProvinceName',
		livingCityName: 'livingCityName',
		phone: 'phone',
		mobile: 'mobile',
		address: 'address',
		fatherBirthCertID: 'fatherBirthCertID',
		fatherName: 'fatherName',
		fatherNID: 'fatherNID',
		motherBirthCertID: 'motherBirthCertID',
		motherName: 'motherName',
		motherNID: 'motherNID',
		spouseFirstName: 'firstName',
		spouseSureName: 'sureName',
		spouseNID: 'nid',
		spouseMaritalStatus: 'maritalStatusId',
		spouseMarriageDate: 'marriageDate',
		spouseDeathDivorceDate: 'deathDivorceDate',
		childFirstName: 'firstName',
		childNID: 'nid',
		childGender: 'gender',
		childBirthDate: 'birthDate',

		/* Card Request Detail dialogue */
		citizenBirthDate: 'citizenBirthDate',
		citizenBirthDateSOL: 'citizenBirthDateSOL',
		citizenBirthDateLUN: 'citizenBirthDateLUN',
		gender: 'gender',
		religion: 'religion',
		birthCertiIssuancePlace: 'birthCertiIssuancePlace',
		postalCode: 'postalCode',
		livingProvinceName: 'livingProvinceName',
		livingCityName: 'livingCityName',
		phone: 'phone',
		mobile: 'mobile',
		address: 'address',
		fatherBirthCertID: 'fatherBirthCertID',
		fatherName: 'fatherName',
		fatherNID: 'fatherNID',
		motherBirthCertID: 'motherBirthCertID',
		motherName: 'motherName',
		motherNID: 'motherNID',
		spouseFirstName: 'firstName',
		spouseSureName: 'sureName',
		spouseNID: 'nid',
		spouseMaritalStatus: 'maritalStatusId',
		spouseMarriageDate: 'marriageDate',
		spouseDeathDivorceDate: 'deathDivorceDate',
		childFirstName: 'firstName',
		childNID: 'nid',
		childGender: 'gender',
		childBirthDate: 'birthDate',


		/* Grid modal */
		result : 'result',
		date : 'date',
		systemId : 'systemId',
		cmsRequestId : 'cmsRequestId',
		action : 'action',
		actor : 'actor',
		r_firstName : 'citizenFirstName',
		r_lastName : 'citizenSurname',
		r_status : 'cardState',
		r_priority : 'priority',

        /* trackingCodePrint Dialog */
        rsvDate: 'rsvDate',
        birthCertId: 'birthCertId',
        receiptDate: 'receiptDate',
        userFirstName: 'userFirstName',
        userLastName: 'userLastName',
        userSign: 'userSign'
	},
	helpFileList : {

		_title : '_title',
		_desc : '_desc',
		_helpFile : '_helpFile',

		/* Grid modal */
		helpFile : 'helpFile',
		title : 'title',
		desc : 'desc',
		id : 'id',
		creator : 'creator',
		createDateHelp : 'createDateHelp'
	},
	about : {
		id : 'id',
		content : 'content',
		createDateAbout : 'createDateAbout'
	},
	cmsErrorEvaluateList : {

		citizenFirstName : 'citizenFirstName',
		citizenSurname : 'citizenSurname',
		citizenNId : 'citizenNId',

		result : 'result',

		cardRequestId : 'cardRequestId',

		citizenId : 'citizenId',

		cmsRequestId : 'cmsRequestId',


		officeName : 'officeName',	

		backDate : 'backDate',
		//////////////////////////////////////////////////////////////////
		errorDeleteImageAccess : 'errorDeleteImageAccess',
		errorRepealedAccess : 'errorRepealedAccess',
		errorRetryAccess : 'errorRetryAccess'
	},

	estelam2FalseList : {

		citizenFirstName : 'citizenFirstName',
		citizenSurname : 'citizenSurname',
		citizenNId : 'citizenNId',
		birthDateSolar : 'birthDateSolar',
		trackingId : 'trackingId',
		cardRequestState : 'cardRequestState',
		reservationDate : 'reservationDate',
		officeName : 'officeName',

		id: 'id',
		createDate : 'createDate',
		perName : 'perName',
		action : 'action',
	},

	job : {
		name : 'name',
		description : 'description',
		cron : 'cron',
		state : 'state',
		cronState : 'cronState',
		simpleState : 'simpleState',
		previousFireTime : 'previousFireTime',
		nextFireTime : 'nextFireTime'
	},

	systemProfileList : {
		keyName : 'keyName',
		parentKeyName : 'parentKeyName',
		caption : 'caption',
		value : 'value',
		childCount : 'childCount'

	},

	userRequestTokenMap : {
		'A' : 'AUTHENTICATION',
		'S' : 'SIGNATURE',
		'E' : 'ENCRYPTION'
	},

	cardRequestedActionMap : {
		REPEALING : 'REPEALING',
		REPEAL_UNDO : 'REPEAL_UNDO',
		REPEAL_ACCEPTED : 'REPEAL_ACCEPTED',
		REPEAL_COMPLETE : 'REPEAL_COMPLETE',
		hasAccessToChangePriority:'hasAccessToChangePriority',
        hasPrintRegistrationReceipt: 'hasPrintRegistrationReceipt'
	},
	tokenRequest : {
		id:'id',
		tokenType : 'tokenType',
		tokenState : 'tokenState',
		requestDate : 'requestDate',
		issuanceDate : 'issuanceDate',
		deliverDate :'deliverDate',
		departmentName : 'departmentName',
		adminName : 'adminName'
	},

	messages : {
		id : 'id',
		title : 'title',
		content : 'content',
		msgStatus : 'messageType',
		msgPriority : 'msgPriority',
		msgDate : 'msgDate',
		creator : 'creator',

		office : 'office',
		province : 'province',
		nocroffic : 'nocroffic',
		department : 'department',
		person : 'person',

		provinceName : 'provinceName',
		provinceId : 'provinceId',
		departmentId : 'departmentId',
		departmentName : 'departmentName',
		officeId : 'officeId',
		officeName : 'officeName',
		nocrofficId : 'nocrofficId',
		nocrofficName : 'nocrofficName',
		personId : 'personId',
		personName : 'personName',
		officeType :'officeType',
		
		isAll : 'isAll',
		isManager:'isManager',
		isAllOffice:'isAllOffice',
		isOffice:'isOffice',
		isNocr:'isNocr',
		preparedState:'preparedState',
		provinces : 'provinces',
		offices : 'offices',
		persons : 'persons',
		attachFile:'attachFile',
		senderUsername:'senderUsername',
		createDate:'createDate',
		hasFile:'hasFile'

	},
	messages_ID : {
		id : 'id',
		title : 'title_ID',
		content : 'content_ID',
		msgStatus : 'messageType_ID',
		msgPriority : 'msgPriority_ID',
		msgDate : 'msgDate_ID',
		office : 'office_ID',
		province : 'province_ID',
		nocroffic : 'nocroffic_ID',
		department : 'department_ID',
		person : 'person_ID',

		provinceName : 'provinceName_ID',
		provinceId : 'provinceId_ID',
		departmentId : 'departmentId_ID',
		departmentName : 'departmentName_ID',
		officeId : 'officeId_ID',
		officeName : 'officeName_ID',
		nocrofficId : 'nocrofficId_ID',
		nocrofficName : 'nocrofficName_ID',
		personId : 'personId_ID',
		personName : 'personName_ID',
		khosousiOffice : 'khosousiOffice',
		doloatiOffice : 'doloatiOffice',
		kolProvine :'kolProvine' ,
		modir:'modir'

	},
	cueMessageList:{
		messageDestination : 'messageDestination',
		messageDestinationID : 'messageDestinationID'
	},


});