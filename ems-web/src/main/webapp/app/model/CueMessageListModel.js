/**
 * Created with IntelliJ IDEA.
 * User: Mousavi
 * Date: 7/25/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
Ext.define('Ems.model.CueMessageListModel', {
    extend: 'Gam.data.model.Model',
    fields: [
          EmsObjectName.cueMessageList.messageDestination
         ,EmsObjectName.cueMessageList.messageDestinationID
//       , EmsObjectName.cueMessageList.citizenNId
    ]
});
