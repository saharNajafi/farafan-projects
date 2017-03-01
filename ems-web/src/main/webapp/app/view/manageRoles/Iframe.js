/**
 * Date     : 12/8/13
 * Author   : R. Bakhshpour (ramin.bp@gmail.com)
 */
Ext.define('Ems.view.manageRoles.Iframe', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.managerolesiframe',
    title: 'مدیریت نقش ها',
//    src: 'http://ems.gamelectronics.com:7001/gaas/ManageRole.do?method=all',
    src: '/gaas/ManageRole.do?method=all',
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
