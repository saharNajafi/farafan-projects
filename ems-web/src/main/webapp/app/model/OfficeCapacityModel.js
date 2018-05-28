/**
 * Created with IntelliJ IDEA.
 * User: Navid
 * Date: 05/19/2018
 * Time: 2:10 PM
 */
Ext.define('Ems.model.OfficeCapacityModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        { name: 'startDate', type: 'date' },
        { name: 'endDate', type: 'date' },
        { name: 'capacity', type: 'number' },
        { name: 'shiftNo', type: 'number' },
        { name: 'id', type: 'number' },
        { name: 'workingHoursFrom', type: 'number' },
        { name: 'workingHoursTo', type: 'number' }
    ]
});
