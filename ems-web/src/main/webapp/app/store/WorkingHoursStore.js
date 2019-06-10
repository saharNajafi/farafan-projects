/**
 * Created with IntelliJ IDEA.
 * User: Moghaddam
 * Date: 4/13/13
 * Time: 2:22 PM
 * This store would be used for all combo box components that displays a hour selection (e.g. Enrollment Office Working Hours)
 */
Ext.define('Ems.store.WorkingHoursStore', {
    extend: 'Ext.data.Store',
    alias: 'store.workinghoursstore',
    model: 'Ems.model.WorkingHoursSimpleModel',

    data: function () {
        //  Generating an array of values representing working hours (e.g. 7:00, 7:30, 8:00, . . .)
        var resultArray = [];
        var recordID = 0;
        for (var i = 6

            ; i <= 22; i++) {
            resultArray.push({
                id: recordID++,
                code: i,
                name: i + ":00"
            });
            resultArray.push({
                id: recordID++,
                code: i + 0.5,
                name: i + ":30"
            });
        }

        //  Removing the last half an hour from the list
        Ext.Array.remove(resultArray, resultArray[resultArray.length - 1]);
        return resultArray;
    }()
});

