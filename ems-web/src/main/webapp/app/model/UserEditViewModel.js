/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 9/1/12
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.UserEditViewModel', {
    extend: 'Ext.data.Model',
    fields: [
        EmsObjectName.userForm.r_firstName
        , EmsObjectName.userForm.r_lastName
        , EmsObjectName.userForm.r_nationalCode
        , EmsObjectName.userForm.r_certificateId
        , EmsObjectName.userForm.r_certificateSerial
        , EmsObjectName.userForm.r_fatherName
        , EmsObjectName.userForm.r_userName
        //,EmsObjectName.userForm.r_password
        //,EmsObjectName.userForm.r_repeatPassword
        //,{name:EmsObjectName.userForm.r_lastLoginDate , type: 'date'}
        //,{name: EmsObjectName.userForm.r_userActive }
        //,EmsObjectName.userForm.r_userAccess
        //,EmsObjectName.userForm.r_userRole
        , EmsObjectName.userForm.r_departmentName
        //,EmsObjectName.userForm.r_departmentId
        , EmsObjectName.userForm.r_email

        //,EmsObjectName.userForm.r_requestStatus

    ]
});
