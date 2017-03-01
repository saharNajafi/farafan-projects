Ext.define('Ems.store.WorkstationStore', {
    extend: 'Gam.data.store.grid.EditableGrid',
    alias: 'store.workstationstore',

    require: ['Ems.model.WorkStationModel'],

    model: 'Ems.model.WorkStationModel',

    listName: 'workstationList',

    baseUrl: 'action/workstation'



//    ,proxy:{
//
//        type:'ajax'
//        ,url:'data/workstation/Grid.json'
//
//        ,reader:{
//            type:'json'
//            ,root:'records'
//        }
//    }
//    ,autoLoad:true
});