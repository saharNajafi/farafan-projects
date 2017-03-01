/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.Estelam2FalseListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
        EmsObjectName.estelam2FalseList.citizenFirstName
        , EmsObjectName.estelam2FalseList.citizenSurname
        , EmsObjectName.estelam2FalseList.citizenNId
        , EmsObjectName.estelam2FalseList.birthDateSolar
        , EmsObjectName.estelam2FalseList.cardRequestState
        , EmsObjectName.estelam2FalseList.trackingId
        , EmsObjectName.estelam2FalseList.reservationDate
        , EmsObjectName.estelam2FalseList.officeName
    ]
});
