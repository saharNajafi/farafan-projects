Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', 'app/ux');

Gam.application({
    name: 'Ems',
    appFolder: 'app',
    autoCreateViewport: true,
    controllers: ['NavigationHeaderController']
    //  launch: function() { }
});

