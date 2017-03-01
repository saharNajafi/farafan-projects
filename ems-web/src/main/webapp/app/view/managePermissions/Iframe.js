/**
 * Date     : 12/8/13
 * Author   : R. Bakhshpour (ramin.bp@gmail.com)
 */
Ext.define('Ems.view.managePermissions.Iframe', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.managepermissionsiframe',
    title: 'مدیریت دسترسی ها',
//    src: 'http://ems.gamelectronics.com:7001/gaas/ManageAccess.do',
    src: '/gaas/ManageAccess.do',
    layout: 'fit',
    initComponent: function () {
        this.items = [
            {
                html: '<iframe id="iframe-' + this.id + '"' +
                    'style="overflow:auto;width:100%;height:100%;"' +
                    ' frameborder="0" ' +
                    'src="' + this.src + '"></iframe>'
            }
        ];
        this.callParent(arguments);
    }
});
