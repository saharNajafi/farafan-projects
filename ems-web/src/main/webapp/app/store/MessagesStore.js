Ext.define('Ems.store.MessagesStore', {
	id:'MessageStore',
    extend: 'Gam.data.store.grid.Grid',
    alias: 'store.messagesstore',

    require: ['Ems.model.MessagesModel'],

    model: 'Ems.model.MessagesModel',

    listName: 'messageList',
    baseUrl: 'extJsController/messages'
});