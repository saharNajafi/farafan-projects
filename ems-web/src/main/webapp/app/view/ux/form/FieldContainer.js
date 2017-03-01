/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 6/9/12
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.view.ux.form.FieldContainer', {
    extend: 'Ext.container.Container',
    mixins: {
        labelable: 'Ext.form.Labelable',
        fieldAncestor: 'Ext.form.FieldAncestor'
    },
    requires: 'Ext.layout.component.field.FieldContainer',

    alias: 'widget.fieldcontainer',

    componentLayout: 'fieldcontainer',


    combineLabels: false,


    labelConnector: ', ',


    combineErrors: false,

    maskOnDisable: false,

    fieldSubTpl: '{%this.renderContainer(out,values)%}',

    initComponent: function () {
        var me = this;


        me.initLabelable();
        me.initFieldAncestor();

        me.callParent();
    },


    onLabelableAdded: function (labelable) {
        var me = this;
        me.mixins.fieldAncestor.onLabelableAdded.call(this, labelable);
        me.updateLabel();
    },


    onLabelableRemoved: function (labelable) {
        var me = this;
        me.mixins.fieldAncestor.onLabelableRemoved.call(this, labelable);
        me.updateLabel();
    },

    initRenderTpl: function () {
        var me = this;
        if (!me.hasOwnProperty('renderTpl')) {
            me.renderTpl = me.getTpl('labelableRenderTpl');
        }
        return me.callParent();
    },

    initRenderData: function () {
        return Ext.applyIf(this.callParent(), this.getLabelableRenderData());
    },


    getFieldLabel: function () {
        var label = this.fieldLabel || '';
        if (!label && this.combineLabels) {
            label = Ext.Array.map(this.query('[isFieldLabelable]'),function (field) {
                return field.getFieldLabel();
            }).join(this.labelConnector);
        }
        return label;
    },

    getSubTplData: function () {
        var ret = this.initRenderData();

        Ext.apply(ret, this.subTplData);
        return ret;
    },

    getSubTplMarkup: function () {
        var me = this,
            tpl = me.getTpl('fieldSubTpl'),
            html;

        if (!tpl.renderContent) {
            me.setupRenderTpl(tpl);
        }

        html = tpl.apply(me.getSubTplData());
        return html;
    },


    updateLabel: function () {
        var me = this,
            label = me.labelEl;
        if (label) {
            label.update(me.getFieldLabel());
        }
    },


    onFieldErrorChange: function (field, activeError) {
        if (this.combineErrors) {
            var me = this,
                oldError = me.getActiveError(),
                invalidFields = Ext.Array.filter(me.query('[isFormField]'), function (field) {
                    return field.hasActiveError();
                }),
                newErrors = me.getCombinedErrors(invalidFields);

            if (newErrors) {
                me.setActiveErrors(newErrors);
            } else {
                me.unsetActiveError();
            }

            if (oldError !== me.getActiveError()) {
                me.doComponentLayout();
            }
        }
    },


    getCombinedErrors: function (invalidFields) {
        var forEach = Ext.Array.forEach,
            errors = [];
        forEach(invalidFields, function (field) {
            forEach(field.getActiveErrors(), function (error) {
                var label = field.getFieldLabel();
                errors.push((label ? label + ': ' : '') + error);
            });
        });
        return errors;
    },

    getTargetEl: function () {
        return this.bodyEl || this.callParent();
    }
});
