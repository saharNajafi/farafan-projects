/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/10/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.controller.NavigationHeaderController', {
    extend: 'Gam.app.controller.FormBasedCrud',
    store: '',
    name: 'Ems',
    ns: 'extJsController/navigationHeader',

    id: 'idNavigationHeader',

    statefulComponents: [
        //'wBaseInfoMenu' ,
        'wBaseInfoMenuUserManagement',
        'wBaseInfoMenuUserManagementUser',
        'wBaseInfoMenuUserManagementUserRequest',
        'wBaseInfoMenuDepartment',
        'wBaseInfoMenuOffice',
        'wBaseInfoMenuWorkstation',
        'wBaseInfoMenuRating',
        'wBaseInfoMenuDocType',
        'wBaseInfoMenuUser',
        'wBaseInfoMenuUserRequest',

        //'wOperationMenu',
        'wOperationMenuDispatch',
        'wOperationMenuBlackList',

        //'wUserInfoMenu',
        'wUserInfoMenuChangePassword',
        'wUserInfoMenuMasterExit',


        //'wReportMenu',
        'wReportMenuBizLogManager',
        'wReportMenuBizLogManagerBizLog',
        'wReportMenuBizLogManagerCardRequestList',
        'wReportMenuBizLogManagerJobList',
        'wReportMenuBizLogManagerSystemProfileList',
        'wReportMenuBizLogManagerTokenRequest',

        'wReportMenuReportManager',
        'wReportMenuReportManagerReport',
        'wReportMenuReportManagerReportView',
        'wHelpFileListGrid',
        'wTokenRequestListGrid'
        , 'wHelpFileListGrid'

    ],


    views: [
        'viewport.BasicInfo',
        'viewport.UserInfo'
    ],
    refs: [
        {
            ref: 'changePasswordForm',
            selector: 'changepasswordform'
        },
        {
            ref: 'viewportuserinfo',
            selector: 'viewportuserinfo'
        }
    ],
    requires: [
        'Ems.view.viewport.MenuPanel',
        'Ems.view.viewport.UserInfo'
    ],

    stores: [/*'ModuleStore'*/],
    models: [/*'ModuleModel'*/],

    constructor: function (config) {

        this.callParent(arguments);
        this.shortCutMasterPage();
        this.getUserInfo();
        this.init();
        config = config || {};
        var masterPanel = config.masterPanel || this.masterPanel,
            masterId = masterPanel ? (masterPanel.getId() + '-') : '';

        Ext.create('Ems.state.Provider').onLoadModuleNavigation('NavigationHeader', this.statefulComponents);

    },


    onModuleStateLoad: function (stateProvider, moduleName, successful) {
        if (!successful) {
            Gam.Msg.notifyLoadingFinishedWithError(moduleName);
            return;
        }

        this.initiateController(moduleName);
    },

    init: function () {

        //var me=this;
        Tools.NavigationContorller.me = this;
        /* me.stateful=true;
         if(me.stateful)
         {
         var stateProvider = Ext.create('Ems.state.Provider', {
         listeners: {
         stateload: me.onModuleStateLoad,
         scope: me
         }
         });
         Ext.state.Manager.setProvider(stateProvider);
         }*/


        this.control({

//           'userinfo':{
//               eventDefineUserAndDepartment:  me.getUserAndDepartment
//           },
            //basicInfo
            '[action=User]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=UserRequest]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=Department]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=Office]': {
                click: function (btn) {
                    this.getAccessChangeOfficeSetting();
                    this.onBtnClicked(btn);
                }
            }, '[action=Workstation]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=Rating]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=DocType]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=Dispatch]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=BlackList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=BizLog]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=Report]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=ReportRequest]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=ReportView]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=CardRequestList]': {
                click: function (btn) {
                    this.getAccessForCardRequestList();
                    // this.getAccessToReceiveBatchId();
                    this.onBtnClicked(btn);
                }
            }, '[action=CmsErrorEvaluateList]': {
                click: function (btn) {
                    this.getAccessProduction();
                    this.onBtnClicked(btn);
                }
            }, '[action=Estelam2FalseList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=HolidayList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=JobList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=SystemProfileList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=ManageReports]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=ReportResult]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            },
            '[action=ManageRoles]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            },

            '[action=ManagePermissions]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }
            , '[action=LostCard]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }, '[action=LostBatch]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            },

            '[action=TokenRequestList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            }
            //userInfo
            , '[action=changePassword]': {
                click: function (btn) {
                    if (Tools.defineUserAndDepartment.changPassWord === 0) {
                        var win = Ext.create('Ems.view.viewport.changePassWord.Window');
                        Tools.MaskUnMask(win);
                        win.show();
                        Tools.defineUserAndDepartment.changPassWord++;
                    }
                }
            },
            '[action=HelpFileList]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                }
            },
            '#btnChangePassWord': {
                click: function (btn) {
                    if (Tools.defineUserAndDepartment.register === 0) {


                        var oldPassWord = Ext.getCmp(EmsObjectName.defineUserAndDepartment.oldPassword).getValue(),
                            newPassWord = Ext.getCmp(EmsObjectName.defineUserAndDepartment.newPassword).getValue(),
                            confirmNewPassWord = Ext.getCmp(EmsObjectName.defineUserAndDepartment.confirmNewPassword).getValue();

                        var win = btn.up('toolbar').up('window'),
                            frm = win.down('form').getForm(),
                            frmValues = frm.getValues();

                        if (!frm.isValid()) {
                            return false;
                        }

                        if (Tools.NavigationContorller.me.PermissionPassWord(newPassWord, confirmNewPassWord)) {
                            Tools.NavigationContorller.me.setChangePassWord(frmValues, win);
                        }

                        Tools.defineUserAndDepartment.register++;
                    }
                }
            },
            '[action=masterExit]': {
                click: function (btn) {
                    if (Tools.defineUserAndDepartment.logout === 0) {
                        Tools.deleteAllCookies();
                        this.castLogout(btn);
                        Tools.defineUserAndDepartment.logout++;
                    }
                }
            },
            '[action=Messages]': {
                click: function (btn) {
                    this.onBtnClicked(btn);
                    //window.alert("sss");
                }
            },
        });
        this.callParent(arguments);
    },

    getAccessProduction: function (view) {
        Ext.Ajax.request({
            url: 'extJsController/cmserrorevaluatelist/doAccessProduction',
            jsonData: {},
            success: function (response) {
                var accessProduction = (Ext.decode(response.responseText)).accessProduction;
                EmsObjectName.cmsErrorEvaluateList.errorDeleteImageAccess = accessProduction.errorDeleteImageAccess;
                EmsObjectName.cmsErrorEvaluateList.errorRepealedAccess = accessProduction.errorRepealedAccess;
                EmsObjectName.cmsErrorEvaluateList.errorRetryAccess = accessProduction.errorRetryAccess;
            },
            failure: function (response) {
                Tools.errorFailure();
            }

        });
    },
    getAccessChangeOfficeSetting: function (view) {
        Ext.Ajax.request({
            url: 'extJsController/office/doAccessViewAndChangeOfficeSetting',
            jsonData: {},
            success: function (response) {

                //debugger;
                var accessViewAndChangeOfficeSetting = (Ext.decode(response.responseText)).accessViewAndChangeOfficeSetting;
                EmsObjectName.officeNewEdit.accessViewAndChangeOfficeSetting = accessViewAndChangeOfficeSetting;

            },
            failure: function (response) {
                Tools.errorFailure();
            }

        });
    },
    getAccessForCardRequestList: function (view) {

        Ext.Ajax.request({
            url: 'extJsController/cardrequestlist/checkCardRequestListAccesses',
            jsonData: {},
            success: function (response) {

                var hasAccessToChangePriority = JSON.parse(response.responseText).hasAccessToChangePriority;
                EmsObjectName.cardRequestedActionMap.hasAccessToChangePriority = hasAccessToChangePriority;

                var hasPrintRegistrationReceipt = JSON.parse(response.responseText).hasPrintRegistrationReceipt;
                EmsObjectName.cardRequestedActionMap.hasPrintRegistrationReceipt = hasPrintRegistrationReceipt;

                var hasAccessToReceiveBatchId = JSON.parse(response.responseText).hasAccessToReceiveBatchId;
                EmsObjectName.cardRequestedActionMap.hasAccessToReceiveBatchId = hasAccessToReceiveBatchId;
            },
            failure: function (response) {
                Tools.errorFailure();
            }

        });
    },
    getUserInfo: function (view) {
        Ext.Ajax.request({
            url: 'extJsController/currentUser/fetchUserInfo',
            jsonData: {},
            success: function (response) {
                var res = Ext.decode(response.responseText);
                Ext.getCmp('defineUserAndDepartment').setText(res.userInfo);
            },
            failure: function (response) {
                Tools.errorFailure();
            }

        });
    },


    onBtnClicked: function (btn) {
        Ext.Ajax.request({
            url: 'extJsController/currentUser/fetchJobVariable',
            method: 'POST',
            success: function (response, request) {
                if (response.responseText.indexOf('<html xmlns="http://www.w3.org/1999/xhtml">') > 0) {
                    Tools.deleteAllCookies();
                    Ext.Ajax.request({
                        url: 'extJsController/sessionClearForLogout/logout',
                        success: function (response) {
                            //window.location.href=document.location.origin+document.location.pathname;
                            window.location.href = document.location.pathname;
                        },
                        failure: function () {
                            Tools.errorFailure();
                        }
                    });
                }
            }
        });
        if (!(btn.action === this.selectionchange)) {
            this.application.launchModule(btn.action);
            this.selectionchange = btn.action;
        }
    },

    PermissionPassWord: function (newPass, RepeatNewPass) {
        if (Tools.trim(newPass + "") === Tools.trim(RepeatNewPass + "")) {
            return true;
        } else {
            Tools.errorMessageClient("گذرواژه جدید و تکرار آن برابر نیست");
            return false;
        }
    },
    setChangePassWord: function (frmValues, win) {
        Ext.Ajax.request({
            url: 'extJsController/currentUser/changePassword',
            jsonData: {
                records: [frmValues]
            },
            success: function (response) {
                var resp = Ext.decode(response.responseText);
                if (resp.success) {
                    Tools.successMessage('تغییر رمز با موفقیت انجام شد');
                    win.close();
                }
            },
            failure: function (response) {
                Tools.errorFailure();
            }

        });
    },
    castLogout: function (btn) {

        Ext.create('Ems.view.viewport.ExitWindow.Window').showAt({x: -80, y: 0});
        this.clearSession();

    },
    clearSession: function () {
        var me = this;
        Ext.Ajax.request({
            url: 'extJsController/sessionClearForLogout/logout',
            success: function (response) {
                //window.location.href=document.location.origin+document.location.pathname;
                window.location.href = document.location.pathname;
            },
            failure: function () {
                Tools.errorFailure();
            }
        });
    },

    shortCutMasterPage: function () {
        var me = this;

        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'U',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("User");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });

        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'U',
            ctrl: true,
            alt: true,
            handler: function () {
                me.BtnClicked("UserRequest");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'T',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("Workstation");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'I',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("Rating");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'J',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("JobList");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'O',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("Department");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'S',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("Office");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'T',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("DocType");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'D',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("Dispatch");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'B',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("BlackList");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'E',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("BizLog");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'R',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("CardRequestList");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });


        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'C',
            ctrl: true,
            shift: true,
            handler: function () {
                me.BtnClicked("SystemProfileList");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });


        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'R',
            ctrl: true,
            alt: true,
            handler: function () {
                me.BtnClicked("ReportRequest");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });

        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'P',
            ctrl: true,
            alt: true,
            handler: function () {
                me.BtnClicked("ReportResult");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });

        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'I',
            ctrl: true,
            alt: true,
            handler: function () {
                me.BtnClicked("ManageReports");
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });

        /*        new Ext.util.KeyMap(Ext.getBody(), {
         key: 'V',
         ctrl: true,
         shift:true,
         handler: function() {
         me.BtnClicked("ReportView");
         },
         scope: this,
         defaultEventAction: "stopEvent"
         });*/
        new Ext.util.KeyMap(Ext.getBody(), {
            key: 'Q',
            ctrl: true,
            shift: true,
            handler: function () {
                me.castLogout(null);
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });

    },

    BtnClicked: function (action) {
        if (!(action === this.selectionchange)) {
            this.application.launchModule(action);
            this.selectionchange = action;
        }
    },

    testClicked: function (action) {
        var me = Tools.NavigationContorller.me;
        if (action != "") {
            me.application.launchModule(action);
            me.selectionchange = action;
        }
    }

});

function testShortKey(value) {

    //var c = Ext.create('Ems.controller.NavigationHeaderController');
    var c2 = Ext.define('ShortKey', {
        mixins: {
            shortKey: 'Ems.controller.NavigationHeaderController'
        }
    });

    var key = Ext.create('ShortKey');

    switch (value) {
        case 'Ctrl+Shift+U':
            key.testClicked('User');
            break;

        case 'Ctrl+Alt+U':
            key.testClicked('UserRequest');
            break;

        case 'Ctrl+Shift+W':
            key.testClicked('Workstation');
            break;

        case 'Ctrl+Shift+I':
            key.testClicked('Rating');
            break;

        case 'Ctrl+Shift+J':
            key.testClicked('JobList');
            break;

        case 'Ctrl+Shift+O':
            key.testClicked('Department');
            break;

        case 'Ctrl+Shift+S':
            key.testClicked('Office');
            break;

        case 'Ctrl+Shift+T':
            key.testClicked('DocType');
            break;

        case 'Ctrl+Shift+D':
            key.testClicked('Dispatch');
            break;

        case 'Ctrl+Shift+B':
            key.testClicked('BlackList');
            break;

        case 'Ctrl+Shift+E':
            key.testClicked('BizLog');
            break;


        case 'Ctrl+Shift+R':
            key.testClicked('CardRequestList');
            break;


        case 'Ctrl+Shift+C':
            key.testClicked('SystemProfileList');
            break;

        default:
            alert(value + 'is incorrect');

    }
}
