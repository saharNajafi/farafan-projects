Ext.define('Ems.model.UserModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.userForm.firstName
        , EmsObjectName.userForm.lastName
        , EmsObjectName.userForm.nationalCode
        , EmsObjectName.userForm.certificateId
        , EmsObjectName.userForm.certificateSerial
        , EmsObjectName.userForm.fatherName
        , EmsObjectName.userForm.userName
        , EmsObjectName.userForm.password
        , EmsObjectName.userForm.repeatPassword
        , {name: EmsObjectName.userForm.lastLoginDate, type: 'date'}
        , {name: EmsObjectName.userForm.userActive }
        , EmsObjectName.userForm.userAccess
        , EmsObjectName.userForm.userRole
        , EmsObjectName.userForm.departmentName
        , EmsObjectName.userForm.departmentId
        , EmsObjectName.userForm.email
        , EmsObjectName.userForm.provinceName
        , EmsObjectName.userForm.requestStatus

    ]
});