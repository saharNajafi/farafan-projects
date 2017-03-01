//khodayari
Ext.define('Ems.model.HelpFileListStoreModel', {
    extend: 'Gam.data.model.Model',
    fields: [
          EmsObjectName.helpFileList.helpFile
        , EmsObjectName.helpFileList.title
        , EmsObjectName.helpFileList.desc
        , EmsObjectName.helpFileList.id
        , EmsObjectName.helpFileList.createDateHelp,
        EmsObjectName.helpFileList.creator
        ]
});
