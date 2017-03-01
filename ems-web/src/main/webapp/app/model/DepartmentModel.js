/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/3/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.DepartmentModel', {
    extend: 'Gam.data.model.Model',
    fields: [

        //EmsObjectName.Department.parentDepartment,
        EmsObjectName.Department.code,
        EmsObjectName.Department.name,
        EmsObjectName.Department.address,
        EmsObjectName.Department.postalCode,
        EmsObjectName.Department.dn,
        EmsObjectName.Department.parentDN,
        EmsObjectName.Department.parentDNId,
        EmsObjectName.Department.dispatchSendType,
        EmsObjectName.Department.dispatchSendTypeId,
        EmsObjectName.Department.location,
        EmsObjectName.Department.locationId,
        EmsObjectName.Department.provinceName


        /*{name:'parentName', type:'string'},
         {name:'parentId', type:'int'},
         {name:'code', type:'string'},
         {name:'name', type:'string'},
         {name:'address', type:'string'},
         {name:'postalCode', type:'int'},
         {name:'dn', type:'string'},
         {name:'parentDN', type:'string'},
         {name:'parentDNId' ,type:'int' },
         {name:'dispatchSendType' , type:'string'},
         {name:'dispatchSendTypeId' ,type:'int' },
         {name:'locName' ,type:'string'},
         {name:'locId', type:'int'}*/
    ]
});
