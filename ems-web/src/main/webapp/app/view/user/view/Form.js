/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/10/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */

Ext.define('Ems.view.user.view.Form', {
    extend: 'Ext.form.Panel',
    alias: 'widget.userviewform',
    requires: [
        //'Ems.view.user.view.access' ,
        //'Ems.view.user.view.role',
        'Ems.view.user.access.MultiSelect' ,
        'Ems.view.user.role.MultiSelect'
    ],
    //layout:'column',
    id: 'idUserViewForm',
    bodyBorde: false,
    border: false,
    //autoScroll:true,
    initComponent: function () {

        this.items = [
            this.OnPersonalInfoFrmView(),
            this.OnOrganizationFrmView()
        ];
        this.callParent(arguments);
    },

    OnPersonalInfoFrmView: function () {
        return ({
            xtype: 'fieldset',
            title: 'اطلاعات فردی',
            margin: 10,
            items: [
                {
                    xtype: 'displayfield',
                    fieldLabel: 'نام کاربری',
                    id: EmsObjectName.userForm.r_userName
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: 'نام و نام خانوادگی',
                    //combineErrors: true,
                    // msgTarget: 'under',
                    layout: {type: 'hbox'},
                    defaults: {
                        hideLabel: true
                    },
                    items: [
                        {xtype: 'displayfield', id: EmsObjectName.userForm.r_firstName},
                        {xtype: 'component', html: '<div>&nbsp;</div>'},
                        {xtype: 'displayfield', id: EmsObjectName.userForm.r_lastName}
                    ]
                },
                {
                    layout: 'column',
                    border: false,
                    bodyBorder: false,
                    items: [this.OnPersonalInfoFrmView_c1(), this.OnPersonalInfoFrmView_c2()]
                }
            ]
        });
    },
    OnPersonalInfoFrmView_c1: function () {
        return({
            xtype: 'container',
            columnWidth: .5,
            border: false,
            bodyBorder: false,
            defaults: {
                xtype: 'displayfield',
                anchor: '-10'
            },
            items: [
                {
                    fieldLabel: 'نام پدر', id: EmsObjectName.userForm.r_fatherName
                },
                {
                    fieldLabel: 'کد ملی', id: EmsObjectName.userForm.r_nationalCode
                } ,
                {
                    fieldLabel: 'پست الکترونیکی',
                    id: EmsObjectName.userForm.r_email
                }
            ]

        });
    },
    OnPersonalInfoFrmView_c2: function () {
        return({
            xtype: 'container',
            columnWidth: .5,
            border: false,
            bodyBorder: false,
            defaults: {
                xtype: 'displayfield',
                labelWidth: 130,
                anchor: '-10'
            }, items: [
                {
                    fieldLabel: 'شماره سریال شناسنامه', id: EmsObjectName.userForm.r_certificateSerial
                },
                {
                    fieldLabel: 'شماره شناسنامه', id: EmsObjectName.userForm.r_certificateId
                }
            ]
        });
    },
    ////////////////////////////////////////////////

    OnOrganizationFrmView: function () {
        return ({
            xtype: 'fieldset',
            title: 'اطلاعات سازمانی',
            margin: 10,
            anchor: '100%',
            items: [
                {
                    xtype: 'displayfield',
                    fieldLabel: 'جایگاه سازمانی', id: EmsObjectName.userForm.r_departmentName
                },
                {
                    layout: 'column',
                    border: false,
                    bodyBorder: false,
                    items: [this.OnOrganizationFrmView_c1(), this.OnOrganizationFrmView_c2()]
                }
            ]
        });
    },
    OnOrganizationFrmView_c1: function () {
        return({
            xtype: 'container',
            columnWidth: .5,
            border: false,
            bodyBorder: false,
            items: [
                {
                    border: false,
                    bodyBorder: false,
                    layout: {
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'component',
                            html: 'نقش ها :'
                        },
                        {
                            xtype: 'userrolemultiselect',
                            width: 320
                        }
                    ]
                }
            ]

        });
    },
    OnOrganizationFrmView_c2: function () {
        return({
            xtype: 'container',
            columnWidth: .5,
            border: false,
            bodyBorder: false,
            items: [
                {
                    border: false,
                    bodyBorder: false,
                    layout: {
                        type: 'hbox'
                    },
                    items: [
                        {
                            xtype: 'component',
                            html: 'دسترسی ها :'
                        },
                        {
                            xtype: 'useraccessmultiselect',
                            width: 320
                        }
                    ]
                }
            ]

        });
    }
    /*
     , OnOrganizationFrmView:function () {
     return({
     xtype:'fieldset',
     title:'اطلاعات سازمانی',
     layout:'column',
     height:160,
     margin:8,
     padding:4,
     items:[
     {
     xtype:'displayfield',
     fieldLabel:'جایگاه سازمانی', id:EmsObjectName.userForm.r_departmentName
     },
     {
     layout:'column',
     border:false,
     bodyBorder:false,
     items:[
     {
     xtype:'component',
     html:'نقش ها : '

     },
     {
     // fieldLabel:'نقش :',
     xtype:'userrolemultiselect'//'userviewrole', id:EmsObjectName.userForm.userRole, scrollHeight:false, autoHeight:true
     },
     {
     xtype:'component',
     html:'دسترسی ها :'
     },
     {
     // fieldLabel:'دسترسی',
     xtype:'useraccessmultiselect'//'userviewaccess', id:EmsObjectName.userForm.userAccess

     }
     ]
     }
     ]

     });
     }
     */
});


/*
 Ext.define('Ems.view.user.view.Form',{
 extend:'Ext.form.Panel'
 ,alias:'widget.userviewform'

 ,requires:[
 //'Ems.view.user.view.access' ,
 //'Ems.view.user.view.role',
 'Ems.view.user.access.MultiSelect' ,
 'Ems.view.user.role.MultiSelect'
 ]
 */
/*,layout:'column' *//*


 ,id:'idUserViewForm'

 ,bodyBorde:false
 ,border:false
 , autoScroll:true
 ,initComponent:function(){

 this.items=[
 this.OnPersonalInfoFrmView(),
 this.OnOrganizationFrmView()
 ] ;
 this.callParent(arguments);
 }
 ,OnPersonalInfoFrmView:function(){
 return ({
 xtype:'fieldset'
 ,title:'اطلاعات فردی'
 , margin: 10
 ,items:[{
 xtype:'displayfield',
 fieldLabel:'نام کاربری'
 ,id: EmsObjectName.userForm.userName
 },{
 xtype: 'fieldcontainer',
 fieldLabel: 'نام و نام خانوادگی',
 //combineErrors: true,
 // msgTarget: 'under',
 layout:{type:'hbox'},
 defaults: {
 hideLabel: true
 },
 items: [
 {xtype: 'displayfield', id: EmsObjectName.userForm.firstName},
 {xtype: 'component', html: '<div>&nbsp;</div>'},
 {xtype: 'displayfield', id: EmsObjectName.userForm.lastName}
 ]
 },{
 layout:'column',
 border:false,
 bodyBorder:false,
 items:[this.OnPersonalInfoFrmView_c1(),this.OnPersonalInfoFrmView_c2()]
 }]
 });
 }
 ,OnPersonalInfoFrmView_c1:function(){
 return({
 xtype:'container',
 columnWidth:.5,
 border:false,
 bodyBorder:false,
 defaults:{
 xtype:'displayfield',
 anchor:'-10'
 },
 items:[{
 fieldLabel:'نام پدر'
 ,id: EmsObjectName.userForm.fatherName
 },{
 fieldLabel:'کد ملی'
 ,id: EmsObjectName.userForm.nationalCode
 }]

 });
 }
 ,OnPersonalInfoFrmView_c2:function(){
 return({
 xtype:'container',
 columnWidth:.5,
 border:false,
 bodyBorder:false,
 defaults:{
 xtype:'displayfield',
 labelWidth:130,
 anchor:'-10'
 }
 ,items:[{
 fieldLabel:'شماره سریال شناسنامه'
 ,id: EmsObjectName.userForm.certificateSerial
 },{
 fieldLabel:'شماره شناسنامه'
 ,id: EmsObjectName.userForm.certificateId
 },{
 fieldLabel:'پست الکترونیکی',
 id:EmsObjectName.userForm.email
 }]
 });
 }
 ////////////////////////////////////////////////

 ,OnOrganizationFrmView:function(){
 return({
 xtype:'fieldset',
 title:'اطلاعات سازمانی',
 layout:'column',
 height:160,
 margin:8,
 padding:4,
 items:[{
 xtype:'displayfield',
 fieldLabel:'جایگاه سازمانی'
 ,id: EmsObjectName.userForm.departmentName
 },{
 layout:'column',
 border:false,
 bodyBorder:false,
 items:[{
 xtype: 'component',
 html:'نقش ها : '

 },{
 // fieldLabel:'نقش :',
 xtype:'userrolemultiselect'//'userviewrole'
 ,id: EmsObjectName.userForm.userRole
 ,scrollHeight:false
 ,autoHeight:true
 },{
 xtype: 'component',
 html:'دسترسی ها :'
 },{
 // fieldLabel:'دسترسی',
 xtype:'useraccessmultiselect'//'userviewaccess'
 ,id: EmsObjectName.userForm.userAccess

 }]
 }]

 });
 */
/*  return ({
 // xtype:'fieldset',
 // title:'اطلاعات سازمانی',
 //layout:'column',
 height:160,
 // margin:8,
 // padding:4,
 items:[{
 layout:'column',
 border:false,
 bodyBorder:false,
 items:[
 this.OnOrganizationFrmNewEdit_c1()
 ,this.OnOrganizationFrmNewEdit_c2()
 ]
 }]
 });*//*

 }
 ,OnOrganizationFrmNewEdit_c1:function(){
 return({
 xtype:'displayfield',
 fieldLabel:'جایگاه سازمانی'
 ,id: EmsObjectName.userForm.departmentName
 },{
 xtype:'container',
 columnWidth:.5,
 border:false,
 bodyBorder:false,
 defaults:{
 anchor:'-1'
 },

 items:[{
 layout:'column',
 border:false,
 bodyBorder:false,
 items:[{
 xtype: 'component',
 html:'نقش ها : '

 },{
 xtype:'userrolemultiselect'//'userviewrole'
 ,id: EmsObjectName.userForm.userRole

 }]
 }]


 });
 }
 ,OnOrganizationFrmNewEdit_c2:function(){
 return({
 xtype:'container',
 columnWidth:.5,
 border:false,
 bodyBorder:false,
 defaults:{

 anchor:'-1'
 }
 ,items:[
 {
 layout:'column',
 border:false,
 bodyBorder:false,
 items:[{
 xtype: 'component',
 html:'دسترسی ها : '

 },{
 xtype:'useraccessmultiselect'//'userviewaccess'
 ,id: EmsObjectName.userForm.userAccess
 //,disabledCls:'x-panel-body x-panel-body-default x-panel-body-default'
 }]
 }
 ]
 });
 }
 });*/
