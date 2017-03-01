Ext.define('Ems.store.TempMessagesStore', {
    extend: 'Ext.data.Store',
    alias: 'store.tempmessagesstore',
    id:'TempMessageStore',
    require: ['Ems.model.TempMessagesModel'],

    model: 'Ems.model.TempMessagesModel',

//    listName: 'messagesList',
  //  baseUrl: 'action/sama'
});