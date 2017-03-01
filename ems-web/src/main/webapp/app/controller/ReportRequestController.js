/**
 * The Controller of report requests module
 * User: E.Z.Moghaddam
 * Date: 7/3/12
 * Time: 2:10 PM
 * Main controller of report requests module
 */
Ext.define('Ems.controller.ReportRequestController', {
    extend: 'Gam.app.controller.LocalDialogBasedGrid',

    ns: 'extJsController/reportrequest',

    views: [
        'reportRequest.Grid',
        'reportRequest.ReportOutputRadioGroup',
        'office.NewEdit.cmbSuperRegisterOffice',
        'office.PersonDepartmentsCombo',
        'office.ProvincesCombo'
    ],

    initViewType: 'reportrequestgrid',

    reportRequestWindow: null,

    constructor: function (config) {
        this.callParent(arguments);
    },

    init: function () {
        this.control({
            '[action=btnCancelReportRequest]': {
                click: function (btn) {
                    if (this.reportRequestWindow) {
                        this.reportRequestWindow.close();
                        this.reportRequestWindow = null;
                    }
                }
            },
            '[action=btnSaveReportRequest]': {
                click: function (btn) {
                    this.saveReportRequest();
                }
            }
        });

        this.callParent(arguments);
    },

    /**
     * Handler of run report action
     *
     * @param grid      Reference to the report request grid
     * @param rowIndex  Index of the report which has been selected to run
     */
    doRunReport: function (grid, rowIndex) {
        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: 'extJsController/managereports/fetchReportMetaData',
            jsonData: {
                reportId: grid.getStore().getAt(rowIndex).get("id")
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        obj.metaData = Ext.decode(obj.metaData);
//                        obj.metaData.push({
//                            description: null,
//                            name: 'VALIDATOR',
//                            value: "function(params){var result = {success: true}; if(params.nocrFromDate > params.nocrToDate) result.success = false; result.message = Ems.ErrorCode.client.dateFromGreaterThanTo; return result;}",
//                            properties: {
//                                ui:"NONE",
//                                default:"",
//                                label:"",
//                                type:"FUNCTION"
//                            }
//                        });
//                        console.info(Ext.encode(obj.metaData));
                        this.displayReportParameterWindow(grid.getStore().getAt(rowIndex), obj.metaData);
                    }
                    else {
                        Gam.Msg.hideWaitMsg();
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    Gam.Msg.hideWaitMsg();
                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    /**
     * Save report request base on given parameters
     */
    saveReportRequest: function () {
        var reportRecord = this.reportRequestWindow.reportRecord;
        var paramValues = this.reportRequestWindow.down('form').getValues();

        //  Validating form date
        if (!this.reportRequestWindow.down('form').getForm().isValid())
            return;

        //  Adding raw value of autocomplete fields to the list of parameters
        this.reportRequestWindow.down('form').getForm().getFields().each(function (item, index) {
            if (item.getName() && item.getName().indexOf("nocr") == 0) {
                //  This is a report parameter field
                if (item.getXTypes().indexOf("autocomplete") >= 0)
                //  It's an autocomplete field
                    if (item.hasHiddenName && (item.hasHiddenName() == true))
                    //  It has a hidden name property set, so send its raw value
                        paramValues[item.getHiddenName()] = item.getModelData()[item.getName()];
            }
        });

        //  Checking scheduled date of report. If any value selected, validating specified value
        if (Ext.getCmp('jobScheduleFieldset').getState() && !Ext.getCmp('jobScheduleFieldset').getState().collapsed) {
            //  User has selected scheduled report, so he/she has to specify scheduled date
            if (Ext.getCmp('jobScheduleDate').getValue() == null) {
                Tools.errorMessageClient("لطفا تاریخ تولید گزارش را انتخاب فرمایید. انتخاب تاریخی در گذشته و یا انتخاب روز جاری امکان پذیر نمی باشد");
                return;
            }
        }

        //  Checking report's own validation method (if any exists)
        for (var i = 0; i < this.reportRequestWindow.metadata.length; i++) {
            if (this.reportRequestWindow.metadata[i].name == 'nocrVALIDATOR') {
                if ((this.reportRequestWindow.metadata[i].properties != 'undefined') &&
                    (this.reportRequestWindow.metadata[i].properties['default'] != 'undefined') &&
                    (this.reportRequestWindow.metadata[i].properties['default'] != null)) {
                    var validatorFunction = this.reportRequestWindow.metadata[i].properties['default'];
                    try {

//                        The function 'validator' helps to validate the parameters of type date

//                        var validator = function(params){
//                            var result = {success: true};
//
//                            var dateParts = params.nocrToDate.split("/");
//                            var toDate = new Date(dateParts[0], dateParts[1], dateParts[2]);
//                            if(params.nocrFromDate > params.nocrToDate) {
//                                result.success = false;
//                                result.message = Ems.ErrorCode.reportrequest.dateFromGreaterThanTo;
//                            } else if(toDate > new Date()) {
//                                result.success = false;
//                                result.message = Ems.ErrorCode.reportrequest.dateToGreaterThanCurrent;
//                            }
//                            return result;
//                        };

                        var validator = eval("[" + validatorFunction + "][0]");
                        var validationResult = validator.call(validator, paramValues);
                        if ((validationResult == null) || (validationResult == 'undefined') ||
                            (validationResult.success == 'undefined')) {
                            Tools.errorMessageClient(Ems.ErrorCode.reportrequest.reportValidatorError);
                            if (console) {
                                console.error('Validation function of report returns an invalid result. It is : ' + validationResult);
                            }
                            return;
                        } else {
                            if (validationResult.success === false) {
                                if (validationResult.message == 'undefined')
                                    Tools.errorMessageClient(Ems.ErrorCode.reportrequest.reportValidatorError);
                                else
                                    Tools.errorMessageClient(Ems.ErrorCode.reportrequest[validationResult.message]);

                                return;
                            }
                        }
                    } catch (e) {
                        Tools.errorMessageClient(Ems.ErrorCode.reportrequest.reportValidatorError);
                        if (console) {
                            console.error(e.message);
                        }
                        return;
                    }
                }
            }
        }

        var reportRequest = {};
        reportRequest.requestOutputType = paramValues.reportOutputType;
        reportRequest.reportId = reportRecord.get("id");
        reportRequest.jobScheduleDate = Ext.getCmp('jobScheduleDate').getValue();

        //  Removing report output type from the list of official report parameters and send it as a separate parameter
        var parameters = paramValues;
        delete parameters.reportOutputType;

        //  Ext.encode converts all unicode characters to their corresponding UTF-8 encoded format. So we have to use
        //  custom Tools based implementation of Dr. Mousavi in order to bypass this problem temporarily :(
//        reportRequest.parameters = Tools.toJsonRecord(parameters);
//        reportRequest.parameters = Ext.encode(parameters);
        reportRequest.parameters = Ext.encode(this.makeParametersArray(parameters));

        //  Do nothing if the form items are not valid
        if (!this.reportRequestWindow.down('form').getForm().isValid())
            return;

        Gam.Msg.showWaitMsg();
        Ext.Ajax.request({
            url: this.ns + '/save',
            jsonData: {
                records: [reportRequest]
            },
            success: Ext.bind(function (response) {
                Gam.Msg.hideWaitMsg();
                try {
                    var obj = Ext.decode(response.responseText);
                    if (obj.success) {
                        if (this.reportRequestWindow) {
                            this.reportRequestWindow.close();
                            this.reportRequestWindow = null;
                        }
                    }
                    else {
                        Tools.errorMessageServer(obj.messageInfo)
                    }
                } catch (e) {
                    alert(e.message);
                }
            }, this),
            failure: function (resop) {
                Gam.Msg.hideWaitMsg();
                Tools.errorFailure();
            }
        });
    },

    /**
     * Displays a report parameter window to retrieve parameters from the user in order to save the report request
     *
     * @param reportRecord      The record object representing the selected report on the grid
     * @param reportMetadata    Metadata of the report. It would be used to generate a dynamic view for report parameter
     */
    displayReportParameterWindow: function (reportRecord, reportMetadata) {
        this.reportRequestWindow = Ext.create('Ems.view.reportRequest.ReportParameterWindow', {
            controller: this,
            reportRecord: reportRecord,
            metadata: reportMetadata
        });
        this.reportRequestWindow.show();
        Tools.MaskUnMask(this.reportRequestWindow);
    },


    /**
     * Given an object encapsulating pairs of names and values and converts them to an array in order to be send to server
     *
     * @param parameters    An object encapsulating pairs of parameter names and values
     */
    makeParametersArray: function (parameters) {
        var resultArray = [];
        for (var p in parameters) {
            resultArray.push({name: p, value: parameters[p]});
        }
        return resultArray;
    }
});
