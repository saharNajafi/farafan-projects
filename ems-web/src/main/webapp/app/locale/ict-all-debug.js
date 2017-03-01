/*

This file is part of Gam JS 4

Copyright (c) 2011 Sencha Inc

Contact:  http://www.sencha.com/contact

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

 */
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/4/11
 * Time: 11:51 AM
 */
/**
 * @class Ext
 * @singleton
 */
var Gam = Gam || {};

var _oldKhosusiType = '';
var timeOutFlag = false;
(function()
		{
	var global = this;

	if(typeof Gam === 'undefined')
	{
		global.Gam = {};
	}

	Gam.global = global;

	Ext.apply(Gam, {
		isEmptyObject: function(obj)
		{
			if(!(!Ext.isEmpty(obj) && typeof obj == "object"))
			{
				return false;
			}
			for(var p in obj)
			{
				return false;
			}
			return true;
		}
	});

		})();



/*

This file is part of Gam JS 4

Copyright (c) 2011 Sencha Inc

Contact:  http://www.sencha.com/contact

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

 */
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/4/11
 * Time: 12:20 PM
 */
/**
 * Loads Gam.app.Application class and starts it up with given configuration after the page is ready.
 *
 * See Gam.app.Application for details.
 *
 * @param {Object} config
 */
Gam.application = function(config)
{
	config = config || {};

	var className = config.appClass || 'Gam.app.DefaultApplication';
	Ext.require(className);

	Ext.onReady(function()
			{
		Ext.create(className, config);
			});
};




/*

This file is part of Gam JS 4

Copyright (c) 2011 Sencha Inc

Contact:  http://www.sencha.com/contact

GNU General Public License Usage
This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

 */
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/8/11
 * Time: 5:16 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.GlobalConfiguration
 */
Ext.define('Gam.app.GlobalConfiguration', {
	singleton: true,
	alternateClassName: 'Gam.GlobalConfiguration',

	actionName: 'extJsController/configManager',

	GRID_PAGE_SIZE: 10,

	LOV_GRID_PAGE_SIZE: 25,

	SINGLE_GRID_PAGE_SIZE: 25,

	JSON_ROOT: 'records',

	JSON_READER_TOTAL_PROPERTY: 'total',

	WINDOW_MIN_HEIGHT: 100,

	WINDOW_MIN_WIDTH: 440,

	ACTION_TYPES: {
		ADD: 0,
		EDIT: 1,
		VIEW: 2
	},

	ENTITY_STATES: {
		ADDED: 0,
		CHANGED: 1,
		DELETED: 2
	},

	CONTROLLER_ACTIONS: {
		LOAD_BY_ID: 'loadById',
		LOAD_BY_PARENT_ID: 'loadByParentId',
		SAVE: 'save',
		DELETE: 'delete',
		QUERY: 'query',
		KEY_VALUE_LIST: 'keyValueList',
		LOV: 'lov',
		LOAD_BY_SURROGATE_KEYS: 'loadBySurrogateKeys'
	},

	IS_READONLY_ACTION: {
		0: false,
		1: false,
		2: true
	},

	MESSAGE: {
		MANNERS: {
			NOTIFICATION: 0,
			MESSAGE_BOX: 1,
			CONFIRMATION: 2
		},

		PRIORITIES: {
			LOW: 0,
			HIGH: 1
		}
	},

	viewTypeSeparator: '->',

	dialogBasedGrid: {
		continuousDataEntry: false ,
		clearDataEntry: false
	},

	load: function()
	{

	},

	onLoadSuccess: function()
	{

	},

	onLoadFailure: function()
	{

	},

	save: function()
	{

	},

	onSaveSuccess: function()
	{

	},

	onSaveFailure: function()
	{

	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/15/12
 * Time: 5:05 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.Ajax
 * @extends Gam.util.
 */
Ext.define('Gam.Ajax', function()
		{
	function onBeforeRequest(conn, options, eOpts)
	{
		this.fireEvent('gambeforerequest', conn, options, eOpts);
	}

	function handleResponse(eventName, conn, response, options, eOpts)
	{
		try
		{
			response.responseJSON = Ext.decode(response.responseText);
			this.fireEvent(eventName, conn, response, options, eOpts);
		}
		catch(e)
		{
		}
	}

	function onRequestComplete(conn, response, options, eOpts)
	{
		handleResponse.call(this, 'gamrequestcomplete', conn, response, options, eOpts);
	}

	function onRequestException(conn, response, options, eOpts)
	{
		handleResponse.call(this, 'gamrequestexception', conn, response, options, eOpts);
	}

	return {
		singleton: true,

		mixins: {
			observable: 'Ext.util.Observable'
		},

		constructor: function()
		{
			this.addEvents(
					'gambeforerequest',
					'gamrequestcomplete',
					'gamrequestexception'
			);

			this.mixins.observable.constructor.call(this);

			Ext.Ajax.method = 'POST';
			Ext.Ajax.timeout = 360000;

			Ext.Ajax.on({
				beforerequest: onBeforeRequest,
				requestcomplete: onRequestComplete,
				requestexception: onRequestException,
				scope: this
			});
		}
	};
		}());

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/26/11
 * Time: 8:23 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.model.Model
 * @extends Ext.data.Model
 */
Ext.define('Gam.data.model.Model', {
	extend: 'Ext.data.Model',

	onClassExtended: function(cls, data, hooks)
	{
		var onBeforeClassCreated = hooks.onBeforeCreated;

		hooks.onBeforeCreated = function(cls, data)
		{
			var me = this,
			isLogModel = Ext.getClassName(cls).indexOf('LogModel') != -1;

			if(isLogModel)
			{ Ext.apply(data, Gam.util.Model.defaultLogConfig)}

			onBeforeClassCreated.call(me, cls, data, hooks);
		}
	},

	fields: [ { name: 'clientId', type: 'text' } ],

	clientIdProperty: 'clientId'
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/9/11
 * Time: 10:40 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.model.comboBox.Simple
 * @extends Gam.data.model.Model
 */
Ext.define('Gam.data.model.comboBox.Simple', {
	extend: 'Gam.data.model.Model',

	fields: [ 'name' ]
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/9/11
 * Time: 10:40 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.model.autocomplete.Base
 * @extends Ext.data.Model
 */
Ext.define('Gam.data.model.autocomplete.Base', {
	extend: 'Ext.data.Model',

	idProperty: 'acId',

	fields: [
	         'acName',
	         'acType',
	         'acCat'
	         ]
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 2/8/12
 * Time: 12:59 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.model.StateProvider
 * @extends Gam.data.model.Model
 */
Ext.define('Gam.data.model.StateProvider', {
	extend: 'Gam.data.model.Model',

	fields: [
	         'stateId',
	         'value'
	         ]
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/7/11
 * Time: 1:55 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.Store
 * @extends Ext.data.Store
 */
Ext.define('Gam.data.store.Store', {
	extend: 'Ext.data.Store',
	alias: 'store.gam.store',

	addBlankRecord: function(index)
	{
		index = index || 0;

		var record = this.model.create();

		this.insert(index, record);

		return record;
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/7/11
 * Time: 1:55 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.Remote
 * @extends Gam.data.store.Store
 */
Ext.define('Gam.data.store.Remote', {
	extend: 'Gam.data.store.Store',
	alias: 'store.gam.remote',

	remoteSort: true,

	remoteFilter: true,

	remoteGroup: true,

	constructor: function(config)
	{

		config = config || {};
		var me = this,
		baseUrl = config.baseUrl || me.baseUrl;

		baseUrl = Ext.String.removeFromEnd(baseUrl, '/');

		if(baseUrl == "extJsController/cardrequestlist"){//N.khodayari
			me.proxy = Ext.merge({
				timeout: 120000,
				type: 'gamajax',
				api: {
					create: baseUrl + '/save',						
					read: baseUrl + '/get',
					update: baseUrl + '/save',
					destroy: baseUrl + '/delete'
				},
			}, config.proxy);
		}else{
			me.proxy = Ext.merge({
				type: 'gamajax',
				api: {
					create: baseUrl + '/save',
					read: baseUrl + '/get',
					update: baseUrl + '/save',
					destroy: baseUrl + '/delete'
				}
			}, config.proxy);
		}



		delete config.proxy;

		me.callParent([config]);
	},

	load: function(options)
	{
		options = options || {};
		options.recursiveSerialization = Ext.isDefined(options.recursiveSerialization) ? options.recursiveSerialization : true;

		this.callParent([options]);
	},

});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 6/10/12
 * Time: 9:47 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.Local
 * @extends Gam.data.store.Store
 */
Ext.define('Gam.data.store.Local', {
	extend: 'Gam.data.store.Store',
	alias: 'store.gam.local',

	constructor: function(config)
	{
		var me = this;

		config = config || {};

		if(config.proxy)
		{
			var initProxyConfig = {
					reader: {
						type: 'array'
					},
					writer: {
						type: 'array'
					}
			};
			config.proxy = Ext.merge(initProxyConfig, config.proxy);
		}

		me.callParent([config]);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/26/11
 * Time: 8:27 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.Json
 * @extends Gam.data.store.Store
 */
Ext.define('Gam.data.store.Json', {
	extend: 'Gam.data.store.Remote',
	alias: 'store.gam.json',

	constructor: function(config)
	{
		config = config || {};
		config.proxy = config.proxy || {};

		var initProxyConfig = {
				reader: {
					type: 'json',
					successProperty: 'success'
				},
				writer: {
					type: 'json',
					allowSingle: false
				}
		};
		config.proxy = Ext.merge(initProxyConfig, config.proxy);

		this.callParent([config]);
	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/3/11
 * Time: 8:00 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.Array
 * @extends Gam.data.store.Remote
 */
Ext.define('Gam.data.store.Array', {
	extend: 'Gam.data.store.Remote',
	alias: 'store.gam.array',

	constructor: function(config)
	{
		config = config || {};
		config.proxy = config.proxy || {};

		var initProxyConfig = {
				reader: {
					type: 'array'
				},
				writer: {
					type: 'array'
				}
		};
		config.proxy = Ext.merge(initProxyConfig, config.proxy);

		this.callParent([config]);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 2/8/12
 * Time: 12:00 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.StateProvider
 * @extends Gam.data.store.Json
 */
Ext.define('Gam.data.store.StateProvider', {
	extend: 'Gam.data.store.Json',
	alias: 'store.gam.stateprovider',

	requires: 'Gam.data.model.StateProvider',

	model: 'Gam.data.model.StateProvider',

	baseUrl: 'extJsController/stateProvider',

	constructor: function(config)
	{
		config = config	|| {};

		config.proxy = {
				timeout: 100000,
				reader: {
					root: Gam.GlobalConfiguration.JSON_ROOT
				},
				writer: {
					root: Gam.GlobalConfiguration.JSON_ROOT
				}
		};

		this.callParent(arguments);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/29/11
 * Time: 2:18 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.grid.Grid
 * @extends Gam.data.store.Json
 */
Ext.define('Gam.data.store.grid.Grid', {
	extend: 'Gam.data.store.Json',
	alias: 'store.grid',

	listName: null,

	constructor: function(config)
	{
		var me = this;

		config = config || {};

		config.proxy = config.proxy || {};
		me.model = Ext.ModelManager.getModel(me.model);


		/////////////////////
		if(me.listName == "emsCardRequestList"){//N.Khodayari
			var initProxyConfig = {
					api: {
						read: 'extJsController/generalList/query'
					},
					reader: {
						root: Gam.GlobalConfiguration.JSON_ROOT
					},
					writer: {
						root: Gam.GlobalConfiguration.JSON_ROOT
					},
					timeout: 120000,
					type: 'gamajax'
			};
			config.proxy = Ext.merge(initProxyConfig,config.proxy);

		}else{
			var initProxyConfig = {
					api: {
						read: 'extJsController/generalList/query'
					},
					reader: {
						root: Gam.GlobalConfiguration.JSON_ROOT
					},
					writer: {
						root: Gam.GlobalConfiguration.JSON_ROOT
					}
			};
			config.proxy = Ext.merge(initProxyConfig,config.proxy);
		}

		////////////////////////
		me.applyReadParam(Ext.apply({

			listName: me.listName,
			fields: Ext.encode(me.model.prototype.fields.keys)
		}, config.readParams));
		delete config.readParams;

		me.callParent([config]);
	},
	applyReadParam: function(params)
	{

		var me = this;

		me.readParams = me.readParams || {};
		Ext.applyIf(me.readParams, params);
	},

	load: function(options)
	{

		options = options || {};
		options.params = options.params || {};
		Ext.applyIf(options.params, this.readParams);

		this.callParent([options]);
	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/29/11
 * Time: 2:18 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.grid.EditableGrid
 * @extends Gam.data.store.grid.Grid
 */
Ext.define('Gam.data.store.grid.EditableGrid', {
	extend: 'Gam.data.store.grid.Grid',
	alias: 'store.editablegrid'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/23/11
 * Time: 5:21 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.grid.Log
 * @extends Gam.data.store.grid.Grid
 */
Ext.define('Gam.data.store.grid.Log', {
	extend: 'Gam.data.store.grid.Grid',
	alias: 'store.log'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/9/11
 * Time: 10:08 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.comboBox.Local
 * @extends Gam.data.store.Local
 */
Ext.define('Gam.data.store.comboBox.Local', {
	extend: 'Gam.data.store.Local',
	alias: 'store.gam.localcombobox'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/9/11
 * Time: 10:08 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.comboBox.Remote
 * @extends Gam.data.store.comboBox.Remote
 */
Ext.define('Gam.data.store.comboBox.Remote', {
	extend: 'Gam.data.store.Remote',
	alias: 'store.gam.remotecombobox'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 9/18/11
 * Time: 6:12 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.data.store.Autocomplete
 * @extends Ext.data.TreeStore
 */
Ext.define('Gam.data.store.Autocomplete', {
	extend: 'Ext.data.TreeStore',
	alias: 'store.autocomplete',

	/**
	 * @cfg {Number} pageSize
	 * The number of records considered to form a 'page'. This is used to power the built-in
	 * paging using the nextPage and previousPage functions. Defaults to 25.
	 */
	pageSize: 15,

	/**
	 * The page that the Store has most recently loaded (see {@link #loadPage})
	 * @property currentPage
	 * @type Number
	 */
	currentPage: 1,

	autocompleteName: null,

	/**
	 * Central autocomplete supplier
	 */
	baseUrl: 'extJsController/autocomplete',

	constructor: function()
	{

		var me = this;

		me.proxy = {
				type: 'ajax',
				api: {
					read: me.baseUrl + '/query',
					addToFav: me.baseUrl + '/addToFavorite',
					addToHis: me.baseUrl + '/addToHistory'
				}
		};

		me.readParams = me.readParams || {};
		Ext.applyIf(me.readParams, {
			autocompleteName: me.autocompleteName
		});

		me.callParent(arguments);
	},

	load: function(options)
	{
		options = options || {};
		options.params = options.params || {};
		Ext.applyIf(options.params, this.readParams);

		this.callParent([options]);
	},

	/**
	 * Returns the total number of {@link Ext.data.Model Model} instances that the {@link Ext.data.proxy.Proxy Proxy}
	 * indicates exist. This will usually differ from {@link #getCount} when using paging - getCount returns the
	 * number of records loaded into the Store at the moment, getTotalCount returns the number of records that
	 * could be loaded into the Store if the Store contained all data
	 * @return {Number} The total number of Model instances available via the Proxy
	 */
	getTotalCount: function()
	{
		return this.totalCount;
	},

	/**
	 * Sets the root node for this store.  See also the {@link #root} config option.
	 * @param {Ext.data.Model/Ext.data.NodeInterface/Object} root
	 * @return {Ext.data.NodeInterface} The new root
	 */
	setRootNode: function(root)
	{

		var me = this,
		args = Ext.Array.from(arguments);

		root = root || {};

		delete root.params;

		if(args.length == 1)
		{
			args.push(true);
		}

		Ext.applyIf(root, {
			acId: me.defaultRootId
		});

		return me.callParent(args);
	},

	// PAGING METHODS
	/**
	 * Loads a given 'page' of data by setting the start and limit values appropriately. Internally this just causes a normal
	 * load operation, passing in calculated 'start' and 'limit' params
	 * @param {Number} page The number of the page to load
	 * @param {Object} options See options for {@link #load}
	 */
	loadPage: function(page, options)
	{
		var me = this;
		options = Ext.apply({}, options);

		me.currentPage = page;

		me.read(Ext.applyIf(options, {
			page: page,
			start: (page - 1) * me.pageSize,
			limit: me.pageSize,
			addRecords: !me.clearOnPageLoad
		}));
	},

	/**
	 * Loads the next 'page' in the current data set
	 * @param {Object} options See options for {@link #load}
	 */
	nextPage: function(options)
	{
		this.loadPage(this.currentPage + 1, options);
	},

	/**
	 * Loads the previous 'page' in the current data set
	 * @param {Object} options See options for {@link #load}
	 */
	previousPage: function(options)
	{
		this.loadPage(this.currentPage - 1, options);
	},

	/**
	 * @private
	 * Called internally when a Proxy has completed a load request
	 */
	onProxyLoad: function(operation)
	{

		var me = this,
		resultSet = operation.getResultSet();

		if(resultSet)
		{
			me.totalCount = resultSet.total;
		}

		this.callParent([operation]);
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Sina
 * Date: 11/20/11
 * Time: 2:43 PM
 */
Ext.define('Gam.data.proxy.Ajax', {
	extend: 'Ext.data.proxy.Ajax',
	alias: 'proxy.gamajax',

	//N.khodayari
	listeners:
	{
		exception: function (proxy, response, operation)
		{
			
			if(response.timedout){
				timeOutFlag = false;
				Ext.Msg.alert('Error','مدت زمان درخواست شما منقضی شده است.');
			}
			
			if(response.responseJSON.messageInfo.arguments[0] == "EMS_S_EOS_080"
				|| response.responseJSON.messageInfo.arguments[0] == "EMS_S_EOS_079"){
				timeOutFlag = true;
			}

		}
	},



	/**
	 * Encodes the array of {@link Ext.util.Filter} objects into a string to be sent in the request url. By default,
	 * this simply JSON-encodes the filter data
	 * @param {Ext.util.Filter[]} filters The array of {@link Ext.util.Filter Filter} objects
	 * @return {String} The encoded filters
	 */
	encodeFilters: function(filters)
	{
		var jsonFilter = {},
		length = filters.length,
		i = 0;

		for(; i < length; i++)
		{
			jsonFilter[filters[i].property] = filters[i].value;
		}

		return this.applyEncoding(jsonFilter);
	},

	/**
	 * Encodes the array of {@link Ext.util.Sorter} objects into a string to be sent in the request url. By default,
	 * this simply JSON-encodes the filter data
	 * @param {Ext.util.Sorter[]} sorters The array of {@link Ext.util.Sorter Sorter} objects
	 * @return {String} The encoded sorters
	 */
	encodeSorters: function(sorters)
	{
		var length = sorters.length,
		sortStrs = [],
		sorter, i;

		for(i = 0; i < length; i++)
		{
			sorter = sorters[i];

			sortStrs[i] = sorter.property + ' ' + sorter.direction
		}

		return sortStrs.join(",");
	},

	/**
	 * Performs a batch of {@link Ext.data.Operation Operations}, in the order specified by {@link #batchOrder}. Used
	 * internally by {@link Ext.data.Store}'s {@link Ext.data.Store#sync sync} method. Example usage:
	 *
	 *     myProxy.batch({
	 *         create : [myModel1, myModel2],
	 *         update : [myModel3],
	 *         destroy: [myModel4, myModel5]
	 *     });
	 *
	 * Where the myModel* above are {@link Ext.data.Model Model} instances - in this case 1 and 2 are new instances and
	 * have not been saved before, 3 has been saved previously but needs to be updated, and 4 and 5 have already been
	 * saved but should now be destroyed.
	 *
	 * Note that the previous version of this method took 2 arguments (operations and listeners). While this is still
	 * supported for now, the current signature is now a single `options` argument that can contain both operations and
	 * listeners, in addition to other options. The multi-argument signature will likely be deprecated in a future release.
	 *
	 * @param {Object} options Object containing one or more properties supported by the batch method:
	 *
	 * @param {Object} options.operations Object containing the Model instances to act upon, keyed by action name
	 *
	 * @param {Object} [options.listeners] Event listeners object passed straight through to the Batch -
	 * see {@link Ext.data.Batch} for details
	 *
	 * @param {Ext.data.Batch/Object} [options.batch] A {@link Ext.data.Batch} object (or batch config to apply
	 * to the created batch). If unspecified a default batch will be auto-created.
	 *
	 * @param {Function} [options.callback] The function to be called upon completion of processing the batch.
	 * The callback is called regardless of success or failure and is passed the following parameters:
	 * @param {Ext.data.Batch} options.callback.batch The {@link Ext.data.Batch batch} that was processed,
	 * containing all operations in their current state after processing
	 * @param {Object} options.callback.options The options argument that was originally passed into batch
	 *
	 * @param {Function} [options.success] The function to be called upon successful completion of the batch. The
	 * success function is called only if no exceptions were reported in any operations. If one or more exceptions
	 * occurred then the `failure` function will be called instead. The success function is called
	 * with the following parameters:
	 * @param {Ext.data.Batch} options.success.batch The {@link Ext.data.Batch batch} that was processed,
	 * containing all operations in their current state after processing
	 * @param {Object} options.success.options The options argument that was originally passed into batch
	 *
	 * @param {Function} [options.failure] The function to be called upon unsuccessful completion of the batch. The
	 * failure function is called when one or more operations returns an exception during processing (even if some
	 * operations were also successful). In this case you can check the batch's {@link Ext.data.Batch#exceptions
	 * exceptions} array to see exactly which operations had exceptions. The failure function is called with the
	 * following parameters:
	 * @param {Ext.data.Batch} options.failure.batch The {@link Ext.data.Batch batch} that was processed,
	 * containing all operations in their current state after processing
	 * @param {Object} options.failure.options The options argument that was originally passed into batch
	 *
	 * @param {Object} [options.scope] The scope in which to execute any callbacks (i.e. the `this` object inside
	 * the callback, success and/or failure functions). Defaults to the proxy.
	 *
	 * @return {Ext.data.Batch} The newly created Batch
	 */
	batch: function(options)
	{

		var me = this,
		useBatch = me.batchActions,
		batch,
		records,
		actions, aLen, action, a, r, rLen, record;

		if(options.batch)
		{
			if(Ext.isDefined(options.batch.runOperation))
			{
				batch = Ext.applyIf(options.batch, {
					proxy: me,
					listeners: {}
				});
			}
		}
		else
		{
			options.batch = {
					proxy: me,
					listeners: options.listeners || {}
			};
		}

		if(!batch)
		{
			batch = new Ext.data.Batch(options.batch);
		}

		batch.on('complete', Ext.bind(me.onBatchComplete, me, [options], 0));

		actions = me.batchOrder.split(',');
		aLen = actions.length;

		for(a = 0; a < aLen; a++)
		{
			action = actions[a];
			records = options.operations[action];

			if(records)
			{
				if(useBatch)
				{
					batch.add(new Ext.data.Operation({
						action: action,
						records: records,
						params: options.params
					}));
				}
				else
				{
					rLen = records.length;

					for(r = 0; r < rLen; r++)
					{
						record = records[r];

						batch.add(new Ext.data.Operation({
							action: action,
							records: [record],
							params: options.params
						}));
					}
				}
			}
		}


		batch.start();
		return batch;
	},
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 2/5/12
 * Time: 4:41 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.state.RemoteProvider
 * @extends Gam.state.
 */

/*
 * File: RemoteStorageProvider.js
 * Date: 20-Jul-2011
 * By  : Kevin L. Esteb
 *
 * This module provides a state provider that uses remote storage as
 * the backing store.
 *
 *   RemoteStorageProvider.js is free software: you can redistribute
 *   it and/or modify it under the terms of the GNU General Public License
 *   as published by the Free Software Foundation, either version 3 of the
 *   License, or (at your option) any later version.
 *
 *   RemoteStorageProvider.js is distributed in the hope that it will be
 *   useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with RemoteStorageProvider.js. If not, see
 *   <http://www.gnu.org/licenses/>.
 *
 */
Ext.define('Gam.state.RemoteProvider', {
	extend: 'Ext.state.Provider',
	mixins: {
		observable: 'Ext.util.Observable'
	},

	requires: [
	           'Gam.data.model.StateProvider',
	           'Gam.data.store.StateProvider'
	           ],

	           store: { type: 'gam.stateprovider' },

	           constructor: function(config)
	           {
	        	   var me = this;

	        	   config = config || {};
	        	   me.initialConfig = config;

	        	   Ext.apply(me, config);

	        	   me.store = Ext.StoreManager.lookup(me.store);
	        	   me.store.on({
	        		   load: me.onStateLoad,
	        		   scope: me
	        	   });

	        	   me.mixins.observable.constructor.call(me, config);
	        	   me.addEvents('statechange', 'stateload');
	           },

	           set: function(stateId, value)
	           {
	        	   var pos, row;

	        	   if((pos = this.store.find('stateId', stateId)) > -1)
	        	   {
	        		   row = this.store.getAt(pos);
	        		   row.set('value', this.encodeValue(value));
	        	   }
	        	   else
	        	   {
	        		   this.store.add({
	        			   stateId: stateId,
	        			   value: this.encodeValue(value)
	        		   });
	        	   }

	        	   this.save();
	        	   this.fireEvent('statechange', this, stateId, value);
	           },

	           get: function(stateId, defaultValue)
	           {
	        	   var pos, row, value;

	        	   if((pos = this.store.find('stateId', stateId)) > -1)
	        	   {
	        		   row = this.store.getAt(pos);
	        		   // Edited by greatly honored Mr Fallah
	        		   // begin
	        		   try
	        		   {
	        			   value = Ext.decode(row.get('value'));
	        		   }
	        		   catch(e)
	        		   {
	        			   value = this.decodeValue(row.get('value'));
	        			   if(Ext.isString(value))
	        			   {
	        				   value = Ext.decode(value);
	        			   }
	        		   }
	        		   // end
	        	   }
	        	   else
	        	   {
	        		   value = defaultValue;
	        	   }

	        	   return value || {};
	           },

	           clear: function(stateId)
	           {
	        	   var pos;

	        	   if((pos = this.store.find('stateId', stateId)) > -1)
	        	   {
	        		   this.store.removeAt(pos);
	        		   this.save();
	        		   this.fireEvent('statechange', this, stateId, null);
	        	   }
	           },

	           save: function()
	           {
	        	   this.store.sync();
	           },

	           load: function(moduleName, stateIds)
	           {
	        	   this.store.filter([
	        	                      {
	        	                    	  id: 'moduleName',
	        	                    	  property: 'moduleName',
	        	                    	  value: moduleName
	        	                      },
	        	                      {
	        	                    	  id: 'stateIds',
	        	                    	  property: 'stateIds',
	        	                    	  value: Ext.Array.from(stateIds)
	        	                      }
	        	                      ]);
	           },

	           cleanUp: function()
	           {
	        	   this.store.removeAll(true);
	           },

	           onStateLoad: function(store, records, successful, options)
	           {
	        	   this.fireEvent('stateload', this, store.filters.get(0).value, successful);
	           }
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 2:47 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.security.Security
 */


Ext.define('Gam.security.Security', {
	singleton: true,

	getCurrentUsername: function()
	{

	},

	setDocLocation: function(url, target)
	{
		if(target)
		{
			switch(target)
			{
			case 'top':
				top.document.location = url;
				break;
			case 'parent':
				parent.document.location = url;
				break;
			}
		}
		else
		{
			document.location = url;
		}
	},

	signOut: function()
	{
		Gam.Msg.confirm(
				Gam.Resource.message.confirmation.logout,
				function()
				{
					Ext.Ajax.request({ url: '/Profiling/Login/SignOut' });
				});
	}
}, function()
{
	Ext.onReady(function()
			{

		var handler = function(conn, response, options)
		{
			if(response.responseJSON.location)
			{
				Gam.security.Security.setDocLocation(obj.location, obj.target);
			}
		};

		Gam.Ajax.on({
			gamrequestcomplete: handler,
			gamrequestexception: handler
		});
			});
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 4/19/12
 * Time: 8:32 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.security.Acl
 */
Ext.define('Gam.security.Acl', {

	singleton: true,

	attributeNames: {
		HIDDEN: 'hidden',
		DISABLED: 'disabled'
	},

	hasAccess: function(stateId)
	{

	},

	isChangeable: function(stateId, attributeName)
	{
		return !stateId || Ext.state.Manager.get(stateId)[attributeName] !== true
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/14/12
 * Time: 6:38 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.SimpleNotifier
 * @extends Gam..
 */
Ext.define('Gam.util.SimpleNotifier', function()
		{
	var msgCt;

	function createBox(t, s)
	{
		return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}

	return {
		show: function(title, format)
		{
			if(!msgCt)
			{
				msgCt = Ext.DomHelper.insertFirst(document.body, {id: 'msg-div'}, true);
			}
			var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
			var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
			m.hide();
			m.slideIn('t').ghost("t", { delay: 1000, remove: true});
		}
	};
		}());

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/14/12
 * Time: 6:38 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.SimpleNotifier
 * @extends Gam..
 */
Ext.define('Gam.util.SimpleNotifier', function()
		{
	var msgCt;

	function createBox(t, s)
	{
		return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}

	return {
		show: function(title, format)
		{
			if(!msgCt)
			{
				msgCt = Ext.DomHelper.insertFirst(document.body, {id: 'msg-div'}, true);
			}
			var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
			var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
			m.hide();
			m.slideIn('t').ghost("t", { delay: 1000, remove: true});
		}
	};
		}());

/*
 *	Notification / Toastwindow extension for Ext JS 4.x
 *
 *	Copyright (c) 2011 Eirik Lorentsen (http://www.eirik.net/)
 *
 *	Examples and documentation at: http://www.eirik.net/Ext/ux/window/Notification.html
 *
 *	Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 *	and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 *	Version: 1.3
 *	Last changed date: 2011-09-13
 */

Ext.define('Gam.window.Notification', {
	extend: 'Ext.window.Window',
	alias: 'widget.gam.notification',

	title: Gam.Resource.message.info.notification,

	cls: 'x-notification-window',
	autoDestroy: true,
	autoHeight: true,
	plain: false,
	draggable: false,
	shadow: false,
	focus: Ext.emptyFn,

	// For alignment and to store array of rendered notifications. Defaults to document if not set.
	manager: null,

	useXAxis: false,

	// Options: br, bl, tr, tl
	corner: 'bl',

	// Pixels between each notification
	spacing: 6,

	// Pixels from the managers borders to start the first notification
	paddingX: 30,
	paddingY: 10,

	slideInAnimation: 'easeIn',
	slideDownAnimation: 'bounceOut',
	autoDestroyDelay: 7000,
	slideInDelay: 1500,
	slideDownDelay: 1000,
	fadeDelay: 500,
	stickOnClick: true,
	stickWhileHover: true,

	// Private. Do not override!
	underDestruction: false,
	readyToDestroy: false,
	// Caching position coordinate to avoid windows overlapping when fading in simultaneously
	xPos: 0,
	yPos: 0,

	layout: {
		type: 'hbox',
		padding:'5',
		align:'middle'
	},

	iconWidth: 35,

	iconHeight: 35,

	minWidth: 200,

	statics: {
		/**
		 * @property {String}
		 * The CSS class that provides the LOADING icon image
		 */
		LOADING: Ext.baseCSSPrefix + 'notification-loading',
		/**
		 * @property {String}
		 * The CSS class that provides the SUCCESS icon image
		 */
		SUCCESS: Ext.baseCSSPrefix + 'notification-success',
		/**
		 * @property {String}
		 * The CSS class that provides the FAILURE icon image
		 */
		FAILURE: Ext.baseCSSPrefix + 'notification-failure',
		/**
		 * @property {String}
		 * The CSS class that provides the INFO icon image
		 */
		INFO: Ext.baseCSSPrefix + 'message-box-info',
		/**
		 * @property {String}
		 * The CSS class that provides the WARNING icon image
		 */
		WARNING: Ext.baseCSSPrefix + 'message-box-warning',
		/**
		 * @property {String}
		 * The CSS class that provides the ERROR icon image
		 */
		ERROR: Ext.baseCSSPrefix + 'message-box-error',

		defaultManager: {
			notifications: [],
			el: null
		},

		responseHandler: function()
		{

		}
	},

	initComponent: function()
	{
		var me = this;

		me.items = [
		            me.iconComponent = new Ext.Component({
		            	cls: Ext.baseCSSPrefix + 'notification-icon',
		            	width: me.iconWidth,
		            	height: me.iconHeight
		            }),
		            me.promptContainer = new Ext.container.Container({
		            	flex: 1,
		            	html: me.html
		            })
		            ];
		me.setIcon(me.icon);
		delete me.html;
		delete me.icon;

		me.callParent(arguments);

		switch(me.corner)
		{
		case 'br':
			me.paddingFactorX = 1;
			me.paddingFactorY = -1;
			me.siblingAlignment = "br-br";
			if(me.useXAxis)
			{
				me.managerAlignment = "bl-br";
			}
			else
			{
				me.managerAlignment = "tr-br";
			}
			break;
		case 'bl':
			me.paddingFactorX = -1;
			me.paddingFactorY = -1;
			me.siblingAlignment = "bl-bl";
			if(me.useXAxis)
			{
				me.managerAlignment = "br-bl";
			}
			else
			{
				me.managerAlignment = "tl-bl";
			}
			break;
		case 'tr':
			me.paddingFactorX = 1;
			me.paddingFactorY = 1;
			me.siblingAlignment = "tr-tr";
			if(me.useXAxis)
			{
				me.managerAlignment = "tl-tr";
			}
			else
			{
				me.managerAlignment = "br-tr";
			}
			break;
		case 'tl':
			me.paddingFactorX = -1;
			me.paddingFactorY = 1;
			me.siblingAlignment = "tl-tl";
			if(me.useXAxis)
			{
				me.managerAlignment = "tr-tl";
			}
			else
			{
				me.managerAlignment = "bl-tl";
			}
			break;
		}

		if(typeof me.manager == 'string')
		{
			me.manager = Ext.getCmp(me.manager);
		}

		// If no manager is provided or found, then the static object is used and the el property pointed to the body document.
		if(!me.manager)
		{
			me.manager = me.statics().defaultManager;

			if(!me.manager.el)
			{
				me.manager.el = Ext.getBody();
			}
		}

		if(typeof me.manager.notifications == 'undefined')
		{
			me.manager.notifications = [];
		}
	},

	afterRender: function()
	{
		var me = this;

		me.callParent(arguments);

		if(me.stickOnClick)
		{
			if(me.body)
			{
				me.body.on('click', me.cancelAutoDestroy, me);
			}
		}

		if(me.autoDestroy)
		{
			me.task = new Ext.util.DelayedTask(me.doAutoDestroy, me);
			me.task.delay(me.autoDestroyDelay);
		}

		me.el.hover(
				function()
				{
					me.mouseIsOver = true;
				},
				function()
				{
					me.mouseIsOver = false;
				},
				me
		);

	},

	getXposAlignedToManager: function()
	{
		var me = this;

		var xPos = 0;

		if(me.corner == 'bl' || me.corner == 'tl')
		{
			xPos += me.manager.el.getLeft();
			xPos -= (me.el.getWidth() + me.paddingX);
		}
		else
		{
			xPos += me.manager.el.getRight();
			xPos += me.paddingX;
		}

		return xPos;
	},

	getYposAlignedToManager: function()
	{
		var me = this;

		var yPos = 0;

		if(me.corner == 'br' || me.corner == 'bl')
		{
			yPos += me.manager.el.getBottom();
			yPos -= (me.el.getHeight() + me.paddingY);
		}
		else
		{
			yPos += me.manager.el.getTop();
			yPos += me.paddingY;
		}

		return yPos;
	},

	getXposAlignedToSibling: function(sibling)
	{
		var me = this;

		if(me.useXAxis)
		{
			if(me.corner == 'tr' || me.corner == 'tr')
			{
				// Using sibling's width when adding
				return (sibling.xPos + sibling.el.getWidth() + sibling.spacing);
			}
			else
			{
				// Using own width when subtracting
				return (sibling.xPos - me.el.getWidth() - me.spacing);
			}
		}
		else
		{
			return me.el.getRight();
		}
	},

	getYposAlignedToSibling: function(sibling)
	{
		var me = this;

		if(me.useXAxis)
		{
			return me.el.getTop();
		}
		else
		{
			if(me.corner == 'tr' || me.corner == 'tl')
			{
				// Using sibling's width when adding
				return (sibling.yPos + sibling.el.getHeight() + sibling.spacing);
			}
			else
			{
				// Using own width when subtracting
				return (sibling.yPos - me.el.getHeight() - sibling.spacing);
			}
		}
	},

	beforeShow: function()
	{
		var me = this;

		if(me.manager.notifications.length)
		{
			me.el.alignTo(me.manager.notifications[me.manager.notifications.length - 1].el, me.siblingAlignment, [0, 0]);
			me.xPos = me.getXposAlignedToSibling(me.manager.notifications[me.manager.notifications.length - 1]);
			me.yPos = me.getYposAlignedToSibling(me.manager.notifications[me.manager.notifications.length - 1]);
		}
		else
		{
			me.el.alignTo(me.manager.el, me.managerAlignment, [(me.paddingX * me.paddingFactorX), (me.paddingY * me.paddingFactorY)]);
			me.xPos = me.getXposAlignedToManager();
			me.yPos = me.getYposAlignedToManager();
		}

		Ext.Array.include(me.manager.notifications, me);

		me.el.animate({
			to: {
				x: me.xPos,
				y: me.yPos
			},
			easing: me.slideInAnimation,
			duration: me.slideInDelay,
			dynamic: true
		});

	},

	slideDown: function()
	{
		var me = this,
		index = Ext.Array.indexOf(me.manager.notifications, me);

		// Not animating the element if it already started to destroy itself
		if(!me.underDestruction && me.el)
		{

			if(index)
			{
				me.xPos = me.getXposAlignedToSibling(me.manager.notifications[index - 1]);
				me.yPos = me.getYposAlignedToSibling(me.manager.notifications[index - 1]);
			}
			else
			{
				me.xPos = me.getXposAlignedToManager();
				me.yPos = me.getYposAlignedToManager();
			}

			me.el.animate({
				to: {
					x: me.xPos,
					y: me.yPos
				},
				easing: me.slideDownAnimation,
				duration: me.slideDownDelay,
				dynamic: true
			});
		}
	},

	cancelAutoDestroy: function()
	{
		var me = this;

		me.addClass('x-notification-fixed');
		if(me.autoDestroy)
		{
			me.task.cancel();
			me.autoDestroy = false;
		}
	},

	setMessage: function(msg)
	{
		this.promptContainer.el.update(msg);
	},

	/**
	 * Adds the specified icon to the dialog.  By default, the class 'x-notification-window' is applied for default
	 * styling, and the class passed in is expected to supply the background image url. Pass in empty string ('')
	 * to clear any existing icon. This method must be called before the MessageBox is shown.
	 * The following built-in icon classes are supported, but you can also pass in a custom class name:
	 *
	 *	 Gam.window.Notification.INFO
	 *	 Gam.window.Notification.LOADING
	 *	 Gam.window.Notification.WARNING
	 *	 Gam.window.Notification.ERROR
	 *
	 * @param {String} icon A CSS classname specifying the icon's background image url, or empty string to clear the icon
	 * @return {Gam.window.Notification} this
	 */
	setIcon: function(icon)
	{
		var me = this;

		me.iconComponent.removeCls(me.messageIconCls);
		if(icon)
		{
			me.iconComponent.show();
			me.iconComponent.addCls(Ext.baseCSSPrefix + 'dlg-icon');
			me.iconComponent.addCls(me.messageIconCls = icon);
		}
		else
		{
			me.iconComponent.removeCls(Ext.baseCSSPrefix + 'dlg-icon');
			me.iconComponent.hide();
		}
		return me;
	},

	setTitle: function(title, iconCls)
	{
		this.callParent([title, iconCls || this.iconCls]);
	},

	doAutoDestroy: function()
	{
		var me = this;

		/* Delayed destruction when mouse leaves the component.
		 Doing this before me.mouseIsOver is checked below to avoid a race condition while resetting event handlers */
		me.el.hover(
				function()
				{
				},
				function()
				{
					me.destroy();
				},
				me
		);

		if(!(me.stickWhileHover && me.mouseIsOver))
		{
			// Destroy immediately
			me.destroy();
		}
	},

	listeners: {
		'beforehide': function(me, eOpts)
		{
			if(!me.underDestruction)
			{
				// Force window to animate and destroy, instead of hiding
				me.destroy();
				return false;
			}
		}
	},

	destroy: function()
	{
		var me = this;

		// Avoids starting the last animation on an element already underway with its destruction
		if(!me.underDestruction)
		{

			me.underDestruction = true;

			me.cancelAutoDestroy();
			me.stopAnimation();

			me.el.animate({
				to: {
					opacity: 0
				},
				easing: 'easeIn',
				duration: me.fadeDelay,
				dynamic: true,
				listeners: {
					afteranimate: function()
					{

						var index = Ext.Array.indexOf(me.manager.notifications, me);
						if(index != -1)
						{
							Ext.Array.erase(me.manager.notifications, index, 1);

							// Slide "down" all notifications "above" the destroyed one
							for(; index < me.manager.notifications.length; index++)
							{
								me.manager.notifications[index].slideDown();
							}
						}
						me.readyToDestroy = true;
						me.destroy();
					}
				}
			});
		}

		// After animation is complete the component may be destroyed
		if(me.readyToDestroy)
		{
			this.callParent(arguments);
		}
	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 2/13/12
 * Time: 11:53 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.window.MessageManager
 */
Ext.define('Gam.window.MessageManager', (function()
		{
	var notifications = {};

	return {
		requires: [
		           'Ext.window.MessageBox'
		           ],

		           singleton: true,

		           /**
		            *
		            * @param msg
		            */
		           showInfoMsg: function(msg)
		           {
		        	   var msgConfig = {
		        			   title: Gam.Resource.title.note,
		        			   buttons: Ext.Msg.OK,
		        			   icon: Ext.Msg.INFO
		        	   };

		        	   Ext.Msg.show(!Ext.isObject(msg) ? Ext.apply(msgConfig, { msg: msg }) : Ext.apply(msgConfig, msg));
		           },

		           /**
		            *
		            * @param msg
		            */
		           showWarningMsg: function(msg)
		           {
		        	   var msgConfig = {
		        			   title: Gam.Resource.title.note,
		        			   buttons: Ext.Msg.OK,
		        			   icon: Ext.Msg.WARNING
		        	   };

		        	   Ext.Msg.show(!Ext.isObject(msg) ? Ext.apply(msgConfig, { msg: msg }) : Ext.apply(msgConfig, msg));
		           },

		           /**
		            *
		            * @param msg
		            */
		           showErrorMsg: function(msg)
		           {
		        	   var msgConfig = {
		        			   title: Gam.Resource.title.error,
		        			   buttons: Ext.Msg.OK,
		        			   icon: Ext.Msg.ERROR
		        	   };

		        	   Ext.Msg.show(!Ext.isObject(msg) ? Ext.apply(msgConfig, { msg: msg }) : Ext.apply(msgConfig, msg));
		           },

		           /**
		            *
		            * @param msg
		            */
		           showWaitMsg: function(msg)
		           {
		        	   Ext.Msg.wait(msg || Gam.Resource.message.info.formSubmitWaiting, Gam.Resource.title.wait);
		           },

		           /**
		            *
		            */
		           hideWaitMsg: function()
		           {
		        	   if(Ext.Msg.progressBar.isHidden())
		        	   {
		        		   return;
		        	   }

		        	   Ext.Msg.updateProgress(1);
		        	   Ext.Msg.hide();
		           },

		           /**
		            *
		            * @param callback
		            * @param scope
		            */
		           confirmDelete: function(callback, scope)
		           {
		        	   Ext.Msg.confirm(
		        			   Gam.Resource.title.warning,
		        			   Gam.Resource.message.confirmation.remove,
		        			   function(btn)
		        			   {
		        				   if(btn == 'yes')
		        				   {
		        					   Ext.callback(callback, scope);
		        				   }
		        			   });
		           },

		           /**
		            *
		            * @param moduleName
		            * @return {Boolean}
		            */
		           hasNotification: function(moduleName)
		           {
		        	   return notifications[moduleName] != undefined;
		           },

		           /**
		            *
		            * @param key
		            * @param config
		            * @return {Gam.window.Notification}
		            */
		           notifyLoading: function(key, config)
		           {
		        	   config = config || {};

		        	   var n = new Gam.window.Notification({
		        		   manager: 'moduleContainer',
		        		   cls: 'x-notification-light',
		        		   iconCls: 'x-notification-icon-information',
		        		   icon: Gam.window.Notification.LOADING,
		        		   autoDestroy: false,
		        		   closable: false,
		        		   title: '',
		        		   html: config.moduleName ?
		        				   Ext.String.format(Gam.Resource.message.info.formattedLoading, config.moduleName) :
		        					   Gam.Resource.message.info.loadingText,
		        					   parameters: { moduleName: config.moduleName },
		        					   slideInDelay: 800,
		        					   slideDownDelay: 1500,
		        					   slideInAnimation: 'elasticIn',
		        					   slideDownAnimation: 'elasticIn'
		        	   });
		        	   notifications[key] = n;
		        	   n.show();

		        	   return n;
		           },

		           /**
		            *
		            * @param key
		            * @param message
		            */
		           notifyLoadingFinishedWithError: function(key, message)
		           {
		        	   this.hideNotification(key, Gam.window.Notification.FAILURE, message || Gam.Resource.message.failure.loading);
		           },

		           /**
		            *
		            * @param key
		            * @param message
		            */
		           notifyLoadingFinishedWithoutError: function(key, message)
		           {
		        	   var notification = notifications[key];

		        	   this.hideNotification(key, Gam.window.Notification.SUCCESS, message || (notification.parameters.moduleName ?
		        			   Ext.String.format(Gam.Resource.message.info.formattedLoadingFinished, notification.parameters.moduleName) :
		        				   Gam.Resource.message.info.loadingFinished));
		           },

		           /**
		            *
		            * @param key
		            * @param icon
		            * @param message
		            * @param hideDelay
		            */
		           hideNotification: function(key, icon, message, hideDelay)
		           {
		        	   var notification = notifications[key];

		        	   notification.setIcon(icon);
		        	   notification.setMessage(message);
		        	   delete notifications[key];
		        	   Ext.Function.defer(notification.close, hideDelay || 3000, notification);
		           },

		           /**
		            *
		            * @param message
		            */
		           autoHideNotify: function(message, icon)
		           {
		        	   new Gam.window.Notification({
		        		   corner: 'tl',
		        		   manager: 'moduleContainer',
		        		   cls: 'x-notification-light',
		        		   icon: icon,
		        		   title: '',
		        		   html: message,
		        		   closable: false,
		        		   width: 250,
		        		   autoDestroyDelay: 4000,
		        		   slideDownDelay: 500,
		        		   slideInAnimation: 'bounceOut',
		        		   slideDownAnimation: 'easeIn'
		        	   }).show();
		           },

		           /**
		            *
		            * @param title
		            * @param format
		            */
		           simpleNotify: function(title, format)
		           {
		        	   Ext.util.SimpleNotifier.show(arguments);
		           }
	};
		})(), function()
		{
	var me = this;

	Gam.Msg = me;

	Ext.onReady(function()
			{
		var buildMessage = function(messageInfo, success)
		{
			var message,
			messageKey = messageInfo.message,
			messageKeyParts;

			if(!messageKey)
			{
				return success ? Gam.Resource.message.info.operation : Gam.Resource.message.failure.operation;
			}

			messageKeyParts = messageKey.split('.');
			if(!Ext.isArray(messageKeyParts) || messageKeyParts.length <= 2)
			{
				return messageKey;
			}

			message = window[messageKeyParts[0]];
			if(Ext.isDefined(message))
			{
				messageKeyParts = Ext.Array.erase(messageKeyParts, 0, 1);
			}
			Ext.each(messageKeyParts, function(subKey)
					{
				message = message[subKey];

				return Ext.isDefined(message);
					});

			return message;
		};

		var handler = function(conn, response, options)
		{

			var res = response.responseJSON,
			messageInfo = res.messageInfo,
			message;
			if(!messageInfo)
			{ return; }

			message = buildMessage(messageInfo, res.success);
			if(messageInfo.arguments)
			{
				message = Ext.String.format.apply(this, Ext.Array.merge(message, messageInfo.arguments));
			}

			messageInfo.message = message;
			messageInfo.msgMethodPart = messageInfo.icon;
			messageInfo.icon = messageInfo.icon.toUpperCase();
			if(!messageInfo.autoShow)
			{
				return;
			}

			if(messageInfo.manner == Gam.GlobalConfiguration.MESSAGE.MANNERS.NOTIFICATION)
			{
				me.autoHideNotify(message, Gam.window.Notification[messageInfo.icon]);
			}
			else
			{
				me['show' + messageInfo.msgMethodPart + 'Msg'](message);
			}
		};

		Gam.Ajax.on('gamrequestcomplete', handler);
			});
		});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/2/12
 * Time: 9:50 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Format
 * @extends Gam.util.
 */
//<localeInfo useApply="true" />
/**
 * @class Ext.util.Format

 This class is a centralized place for formatting functions. It includes
 functions to format various different types of data, such as text, dates and numeric values.

 __Localization__
 This class contains several options for localization. These can be set once the library has loaded,
 all calls to the functions from that point will use the locale settings that were specified.
 Options include:
 - thousandSeparator
 - decimalSeparator
 - currenyPrecision
 - currencySign
 - currencyAtEnd
 This class also uses the default date format defined here: {@link Ext.Date#defaultFormat}.

 __Using with renderers__
 There are two helper functions that return a new function that can be used in conjunction with
 grid renderers:

 columns: [{
 dataIndex: 'date',
 renderer: Ext.util.Format.dateRenderer('Y-m-d')
 }, {
 dataIndex: 'time',
 renderer: Ext.util.Format.numberRenderer('0.000')
 }]

 Functions that only take a single argument can also be passed directly:
 columns: [{
 dataIndex: 'cost',
 renderer: Ext.util.Format.usMoney
 }, {
 dataIndex: 'productCode',
 renderer: Ext.util.Format.uppercase
 }]

 __Using with XTemplates__
 XTemplates can also directly use Ext.util.Format functions:

 new Ext.XTemplate([
 'Date: {startDate:date("Y-m-d")}',
 'Cost: {cost:usMoney}'
 ]);

 * @markdown
 * @singleton
 */
(function()
		{
	Ext.ns('Gam.util');

	Gam.util.Format = {};
	var UtilFormat = Gam.util.Format,
	stripTagsRE = /<\/?[^>]+>/gi,
	stripScriptsRe = /(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)/ig,
	nl2brRe = /\r?\n/g,

	// A RegExp to remove from a number format string, all characters except digits and '.'
	formatCleanRe = /[^\d\.]/g,

	// A RegExp to remove from a number format string, all characters except digits and the local decimal separator.
	// Created on first use. The local decimal separator character must be initialized for this to be created.
	I18NFormatCleanRe;

	Ext.apply(UtilFormat, {
		/**
		 * @property {String} thousandSeparator
		 * <p>The character that the {@link #number} function uses as a thousand separator.</p>
		 * <p>This may be overridden in a locale file.</p>
		 */
		//<locale>
		thousandSeparator: ',',
		//</locale>

		/**
		 * @property {String} decimalSeparator
		 * <p>The character that the {@link #number} function uses as a decimal point.</p>
		 * <p>This may be overridden in a locale file.</p>
		 */
		//<locale>
		decimalSeparator: '.',
		//</locale>

		/**
		 * @property {Number} currencyPrecision
		 * <p>The number of decimal places that the {@link #currency} function displays.</p>
		 * <p>This may be overridden in a locale file.</p>
		 */
		//<locale>
		currencyPrecision: 2,
		//</locale>

		/**
		 * @property {String} currencySign
		 * <p>The currency sign that the {@link #currency} function displays.</p>
		 * <p>This may be overridden in a locale file.</p>
		 */
		//<locale>
		currencySign: '$',
		//</locale>

		/**
		 * @property {Boolean} currencyAtEnd
		 * <p>This may be set to <code>true</code> to make the {@link #currency} function
		 * append the currency sign to the formatted value.</p>
		 * <p>This may be overridden in a locale file.</p>
		 */
		//<locale>
		currencyAtEnd: false,
		//</locale>

		/**
		 * Checks a reference and converts it to empty string if it is undefined
		 * @param {Object} value Reference to check
		 * @return {Object} Empty string if converted, otherwise the original value
		 */
		undef: function(value)
		{
			return value !== undefined ? value : "";
		},

		/**
		 * Checks a reference and converts it to the default value if it's empty
		 * @param {Object} value Reference to check
		 * @param {String} defaultValue The value to insert of it's undefined (defaults to "")
		 * @return {String}
		 */
		defaultValue: function(value, defaultValue)
		{
			return value !== undefined && value !== '' ? value : defaultValue;
		},

		/**
		 * Returns a substring from within an original string
		 * @param {String} value The original text
		 * @param {Number} start The start index of the substring
		 * @param {Number} length The length of the substring
		 * @return {String} The substring
		 */
		substr: function(value, start, length)
		{
			return String(value).substr(start, length);
		},

		/**
		 * Converts a string to all lower case letters
		 * @param {String} value The text to convert
		 * @return {String} The converted text
		 */
		lowercase: function(value)
		{
			return String(value).toLowerCase();
		},

		/**
		 * Converts a string to all upper case letters
		 * @param {String} value The text to convert
		 * @return {String} The converted text
		 */
		uppercase: function(value)
		{
			return String(value).toUpperCase();
		},

		/**
		 * Format a number as US currency
		 * @param {Number/String} value The numeric value to format
		 * @return {String} The formatted currency string
		 */
		usMoney: function(v)
		{
			return UtilFormat.currency(v, '$', 2);
		},

		/**
		 * Format a number as a currency
		 * @param {Number/String} value The numeric value to format
		 * @param {String} sign The currency sign to use (defaults to {@link #currencySign})
		 * @param {Number} decimals The number of decimals to use for the currency (defaults to {@link #currencyPrecision})
		 * @param {Boolean} end True if the currency sign should be at the end of the string (defaults to {@link #currencyAtEnd})
		 * @return {String} The formatted currency string
		 */
		currency: function(v, currencySign, decimals, end)
		{
			var negativeSign = '',
			format = ",0",
			i = 0;
			v = v - 0;
			if(v < 0)
			{
				v = -v;
				negativeSign = '-';
			}
			decimals = Ext.isDefined(decimals) ? decimals : UtilFormat.currencyPrecision;
			format += format + (decimals > 0 ? '.' : '');
			for(; i < decimals; i++)
			{
				format += '0';
			}
			v = UtilFormat.number(v, format);
			if((end || UtilFormat.currencyAtEnd) === true)
			{
				return Ext.String.format("{0}{1}{2}", negativeSign, v, currencySign || UtilFormat.currencySign);
			}
			else
			{
				return Ext.String.format("{0}{1}{2}", negativeSign, currencySign || UtilFormat.currencySign, v);
			}
		},

		/**
		 * Formats the passed date using the specified format pattern.
		 * @param {String/Date} value The value to format. If a string is passed, it is converted to a Date by the Javascript
		 * Date object's <a href="http://www.w3schools.com/jsref/jsref_parse.asp">parse()</a> method.
		 * @param {String} format (Optional) Any valid date format string. Defaults to {@link Ext.Date#defaultFormat}.
		 * @return {String} The formatted date string.
		 */
		date: Ext.isIE & !Ext.isIE9 ? function(v, format)
				{
			if(!v)
			{
				return "";
			}
			if(!Ext.isDate(v))
			{
				v = new Date(Date.parse(v.replace(/-/g, '/')));
			}
			return Ext.JalaliDate.dateFormat(v, format || Ext.Date.defaultFormat);
				} : function(v, format)
				{
					if(!v)
					{
						return "";
					}
					if(!Ext.isDate(v))
					{
						v = new Date(Date.parse(v));
					}
					return Ext.JalaliDate.dateFormat(v, format || Ext.Date.defaultFormat);
				},

				/**
				 * Returns a date rendering function that can be reused to apply a date format multiple times efficiently
				 * @param {String} format Any valid date format string. Defaults to {@link Ext.Date#defaultFormat}.
				 * @return {Function} The date formatting function
				 */
				dateRenderer: function(format)
				{
					return function(v)
					{
						return '<div style="text-align: right; direction: ltr;">' + UtilFormat.date(v, format) + '</div>';
					};
				},

				/**
				 * Strips all HTML tags
				 * @param {Object} value The text from which to strip tags
				 * @return {String} The stripped text
				 */
				stripTags: function(v)
				{
					return !v ? v : String(v).replace(stripTagsRE, "");
				},

				/**
				 * Strips all script tags
				 * @param {Object} value The text from which to strip script tags
				 * @return {String} The stripped text
				 */
				stripScripts: function(v)
				{
					return !v ? v : String(v).replace(stripScriptsRe, "");
				},

				/**
				 * Simple format for a file size (xxx bytes, xxx KB, xxx MB)
				 * @param {Number/String} size The numeric value to format
				 * @return {String} The formatted file size
				 */
				fileSize: function(size)
				{
					if(size < 1024)
					{
						return size + " bytes";
					} else if(size < 1048576)
					{
						return (Math.round(((size * 10) / 1024)) / 10) + " KB";
					}
					else
					{
						return (Math.round(((size * 10) / 1048576)) / 10) + " MB";
					}
				},

				/**
				 * It does simple math for use in a template, for example:<pre><code>
				 * var tpl = new Ext.Template('{value} * 10 = {value:math("* 10")}');
				 * </code></pre>
				 * @return {Function} A function that operates on the passed value.
				 * @method
				 */
				math: function()
				{
					var fns = {};

					return function(v, a)
					{
						if(!fns[a])
						{
							fns[a] = Ext.functionFactory('v', 'return v ' + a + ';');
						}
						return fns[a](v);
					};
				}(),

				/**
				 * Rounds the passed number to the required decimal precision.
				 * @param {Number/String} value The numeric value to round.
				 * @param {Number} precision The number of decimal places to which to round the first parameter's value.
				 * @return {Number} The rounded value.
				 */
				round: function(value, precision)
				{
					var result = Number(value);
					if(typeof precision == 'number')
					{
						precision = Math.pow(10, precision);
						result = Math.round(value * precision) / precision;
					}
					return result;
				},

				/**
				 * <p>Formats the passed number according to the passed format string.</p>
				 * <p>The number of digits after the decimal separator character specifies the number of
				 * decimal places in the resulting string. The <u>local-specific</u> decimal character is used in the result.</p>
				 * <p>The <i>presence</i> of a thousand separator character in the format string specifies that
				 * the <u>locale-specific</u> thousand separator (if any) is inserted separating thousand groups.</p>
				 * <p>By default, "," is expected as the thousand separator, and "." is expected as the decimal separator.</p>
				 * <p><b>New to Ext JS 4</b></p>
				 * <p>Locale-specific characters are always used in the formatted output when inserting
				 * thousand and decimal separators.</p>
				 * <p>The format string must specify separator characters according to US/UK conventions ("," as the
				 * thousand separator, and "." as the decimal separator)</p>
				 * <p>To allow specification of format strings according to local conventions for separator characters, add
				 * the string <code>/i</code> to the end of the format string.</p>
				 * <div style="margin-left:40px">examples (123456.789):
				 * <div style="margin-left:10px">
				 * 0 - (123456) show only digits, no precision<br>
				 * 0.00 - (123456.78) show only digits, 2 precision<br>
				 * 0.0000 - (123456.7890) show only digits, 4 precision<br>
				 * 0,000 - (123,456) show comma and digits, no precision<br>
				 * 0,000.00 - (123,456.78) show comma and digits, 2 precision<br>
				 * 0,0.00 - (123,456.78) shortcut method, show comma and digits, 2 precision<br>
				 * To allow specification of the formatting string using UK/US grouping characters (,) and decimal (.) for international numbers, add /i to the end.
				 * For example: 0.000,00/i
				 * </div></div>
				 * @param {Number} v The number to format.
				 * @param {String} format The way you would like to format this text.
				 * @return {String} The formatted number.
				 */
				number: function(v, formatString)
				{
					if(!formatString)
					{
						return v;
					}
					v = Ext.Number.from(v, NaN);
					if(isNaN(v))
					{
						return '';
					}
					var comma = UtilFormat.thousandSeparator,
					dec = UtilFormat.decimalSeparator,
					i18n = false,
					neg = v < 0,
					hasComma,
					psplit;

					v = Math.abs(v);

					// The "/i" suffix allows caller to use a locale-specific formatting string.
					// Clean the format string by removing all but numerals and the decimal separator.
					// Then split the format string into pre and post decimal segments according to *what* the
					// decimal separator is. If they are specifying "/i", they are using the local convention in the format string.
					if(formatString.substr(formatString.length - 2) == '/i')
					{
						if(!I18NFormatCleanRe)
						{
							I18NFormatCleanRe = new RegExp('[^\\d\\' + UtilFormat.decimalSeparator + ']', 'g');
						}
						formatString = formatString.substr(0, formatString.length - 2);
						i18n = true;
						hasComma = formatString.indexOf(comma) != -1;
						psplit = formatString.replace(I18NFormatCleanRe, '').split(dec);
					}
					else
					{
						hasComma = formatString.indexOf(',') != -1;
						psplit = formatString.replace(formatCleanRe, '').split('.');
					}

					if(1 < psplit.length)
					{
						v = Ext.Number.toFixed(v, psplit[1].length);
					} else if(2 < psplit.length)
					{
					}
					else
					{
						v = Ext.Number.toFixed(v, 0);
					}

					var fnum = v.toString();

					psplit = fnum.split('.');

					if(hasComma)
					{
						var cnum = psplit[0],
						parr = [],
						j = cnum.length,
						m = Math.floor(j / 3),
						n = cnum.length % 3 || 3,
						i;

						for(i = 0; i < j; i += n)
						{
							if(i !== 0)
							{
								n = 3;
							}

							parr[parr.length] = cnum.substr(i, n);
							m -= 1;
						}
						fnum = parr.join(comma);
						if(psplit[1])
						{
							fnum += dec + psplit[1];
						}
					}
					else
					{
						if(psplit[1])
						{
							fnum = psplit[0] + dec + psplit[1];
						}
					}

					if(neg)
					{
						/*
						 * Edge case. If we have a very small negative number it will get rounded to 0,
						 * however the initial check at the top will still report as negative. Replace
						 * everything but 1-9 and check if the string is empty to determine a 0 value.
						 */
						neg = fnum.replace(/[^1-9]/g, '') !== '';
					}

					return (neg ? '-' : '') + formatString.replace(/[\d,?\.?]+/, fnum);
				},

				/**
				 * Returns a number rendering function that can be reused to apply a number format multiple times efficiently
				 * @param {String} format Any valid number format string for {@link #number}
				 * @return {Function} The number formatting function
				 */
				numberRenderer: function(format)
				{
					return function(v)
					{
						return UtilFormat.number(v, format);
					};
				},

				/**
				 * Selectively do a plural form of a word based on a numeric value. For example, in a template,
				 * {commentCount:plural("Comment")}  would result in "1 Comment" if commentCount was 1 or would be "x Comments"
				 * if the value is 0 or greater than 1.
				 * @param {Number} value The value to compare against
				 * @param {String} singular The singular form of the word
				 * @param {String} plural (optional) The plural form of the word (defaults to the singular with an "s")
				 */
				plural: function(v, s, p)
				{
					return v + ' ' + (v == 1 ? s : (p ? p : s + 's'));
				},

				/**
				 * Converts newline characters to the HTML tag &lt;br/>
				 * @param {String} The string value to format.
				 * @return {String} The string with embedded &lt;br/> tags in place of newlines.
				 */
				nl2br: function(v)
				{
					return Ext.isEmpty(v) ? '' : v.replace(nl2brRe, '<br/>');
				},

				/**
				 * Alias for {@link Ext.String#capitalize}.
				 * @method
				 * @inheritdoc Ext.String#capitalize
				 */
				capitalize: Ext.String.capitalize,

				/**
				 * Alias for {@link Ext.String#ellipsis}.
				 * @method
				 * @inheritdoc Ext.String#ellipsis
				 */
				ellipsis: Ext.String.ellipsis,

				/**
				 * Alias for {@link Ext.String#format}.
				 * @method
				 * @inheritdoc Ext.String#format
				 */
				format: Ext.String.format,

				/**
				 * Alias for {@link Ext.String#htmlDecode}.
				 * @method
				 * @inheritdoc Ext.String#htmlDecode
				 */
				htmlDecode: Ext.String.htmlDecode,

				/**
				 * Alias for {@link Ext.String#htmlEncode}.
				 * @method
				 * @inheritdoc Ext.String#htmlEncode
				 */
				htmlEncode: Ext.String.htmlEncode,

				/**
				 * Alias for {@link Ext.String#leftPad}.
				 * @method
				 * @inheritdoc Ext.String#leftPad
				 */
				leftPad: Ext.String.leftPad,

				/**
				 * Alias for {@link Ext.String#trim}.
				 * @method
				 * @inheritdoc Ext.String#trim
				 */
				trim: Ext.String.trim,

				/**
				 * Parses a number or string representing margin sizes into an object. Supports CSS-style margin declarations
				 * (e.g. 10, "10", "10 10", "10 10 10" and "10 10 10 10" are all valid options and would return the same result)
				 * @param {Number/String} v The encoded margins
				 * @return {Object} An object with margin sizes for top, right, bottom and left
				 */
				parseBox: function(box)
				{
					if(Ext.isNumber(box))
					{
						box = box.toString();
					}
					var parts = box.split(' '),
					ln = parts.length;

					if(ln == 1)
					{
						parts[1] = parts[2] = parts[3] = parts[0];
					}
					else if(ln == 2)
					{
						parts[2] = parts[0];
						parts[3] = parts[1];
					}
					else if(ln == 3)
					{
						parts[3] = parts[1];
					}

					return {
						top: parseInt(parts[0], 10) || 0,
						right: parseInt(parts[1], 10) || 0,
						bottom: parseInt(parts[2], 10) || 0,
						left: parseInt(parts[3], 10) || 0
					};
				},

				/**
				 * Escapes the passed string for use in a regular expression
				 * @param {String} str
				 * @return {String}
				 */
				escapeRegex: function(s)
				{
					return s.replace(/([\-.*+?\^${}()|\[\]\/\\])/g, "\\$1");
				}
	});
		})();

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/22/11
 * Time: 5:58 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.FileDownload
 * @extends Ext.Component
 */
Ext.define('Gam.util.AttachmentFileDownload', {
	extend: 'Ext.Component',
	alias: 'widget.attachmentfiledownload',

	singleton: true,

	autoEl: {
		tag: 'iframe',
		cls: 'x-hidden',
		src: Ext.SSL_SECURE_URL
	},

	beforeDownload: function()
	{
		var e = this.getEl();
		if(!e)
		{
			this.render(Ext.getBody());
		}
	},

	request: function(config)
	{
		this.beforeDownload();

		var e = this.getEl();

		e.dom.src = config.url + (config.params ? '?' + Ext.urlEncode(config.params) : '');
		e.dom.onload = function()
		{
			if(e.dom.contentDocument.body.childNodes[0].innerHTML.indexOf('HTTP Status 404') != -1)
			{
				Ext.Msg.show({
					title: 'Attachment missing',
					msg: 'The document you are after can not be found on the server.',
					buttons: Ext.Msg.OK,
					icon: Ext.MessageBox.ERROR
				})
			}
		}
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/22/11
 * Time: 5:58 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.FileDownload
 * @extends Ext.Component
 */
Ext.define('Gam.util.PopupWindow', {

	singleton: true,

	open: function(config)
	{
		config = Ext.isString(config) ? { url: config } : config;

		var url = config.url + (config.params ? '?' + Ext.urlEncode(config.params) : ''),
		windowName = !Ext.isIE ? (config.windowName || 'New Window') : '',
				windowFeatures = config.windowFeatures || 'status=no,toolbar=no,location=no,menubar=no,directories=no';

		window.open(url, windowName, windowFeatures);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/10/11
 * Time: 9:41 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Inheritance
 * @extends Gam..
 */
Ext.define('Gam.util.Inheritance', {

	callParentSpecially: function(args, level)
	{
		var method = this.callParentSpecially.caller,
		supr = method.$previous ||
		((method = method.$owner ? method : method.caller) &&
				method.$owner.superclass[method.$name]);

		for(var i = 0; i < (level || 1) - 1; i++)
		{
			supr = supr.$owner.superclass[method.$name];
		}

		return supr.apply(this, args || []);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 1:53 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Store
 */
Ext.define('Gam.util.Store', {

	singleton: true,

	storeExceptionHandler: function(store, mode, action, options, responce)
	{
		if(responce)
		{
			Sajat.UTILS.showErrorMsg((responce.raw && responce.raw.feedback) ?
					responce.raw.feedback : Gam.Resource.message.failure.loadData);
		}
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 2:42 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Dialog
 */
Ext.define('Gam.util.Dialog', {
	singleton: true
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 1:53 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Form
 */
Ext.define('Gam.util.Form', {
	singleton: true,

	requiredTpl: '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>',

	fillDisplayFieldConfig: {
		xtype: 'gamdisplayfield',
		fieldLabel: '&nbsp;',
		value: '&nbsp;',
		labelSeparator: ' '
	},

	findInvalidItem: function(formOrFieldSet, checkReadOnlyFields)
	{
		var invalidItem,
		validatorFunction = checkReadOnlyFields !== false ? function(field)
				{
			if(!field.isValid())
			{
				invalidItem = field;
				return false;
			}
				} : function(field)
				{
					if(field.readOnly)
					{
						return;
					}
					if(!field.isValid())
					{
						invalidItem = field;
						return false;
					}
				};
				formOrFieldSet.getFields().each(validatorFunction);

				return invalidItem;
	},

	isValid: function(formOrFieldSet, checkReadOnlyFields)
	{
		if(formOrFieldSet.isValid(checkReadOnlyFields))
		{
			return true;
		}

		var invalidItem = this.findInvalidItem(formOrFieldSet, checkReadOnlyFields);
		if(invalidItem)
		{
			Gam.util.Form.showErrorTip(invalidItem);
		}
	},

	showErrorTip: function(invalidItem)
	{
		try
		{
			invalidItem.focus(true);
		}
		catch(e)
		{
		}

		/*var tip = Ext.create('Ext.tip.ToolTip', {
		 target: invalidItem.getXType() != 'htmleditor' ? invalidItem.errorIcon : invalidItem.wrap,
		 anchor: 'right',
		 closable: true,
		 autoHide: false,
		 html: invalidItem.getActiveError(),
		 cls: 'x-form-invalid-tip',
		 listeners: {
		 hide: function()
		 {
		 Ext.defer(this.destroy, 1000, this);
		 }
		 }
		 }).show();

		 try
		 {
		 invalidItem.on('valid', function()
		 {
		 tip.hide();
		 }, window, { single: true });
		 }
		 catch(e)
		 {
		 }*/
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/29/11
 * Time: 1:52 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Model
 * @extends Gam.util.
 */
Ext.define('Gam.util.Model', {

	singleton: true,

	defaultLogConfig: {
		fields: [
		         { name: 'logId', type: 'int' },
		         { name: 'createDate', type: 'date' },
		         { name: 'perId', type: 'int' },
		         'perName',
		         'action'
		         ],

		         idProperty: 'logId',

		         idgen: 'sequential'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 2:42 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.util.Dialog
 */
Ext.define('Gam.util.Config', {
	singleton: true,

	decorateConfigArray: function(configArray, applyConfig, applyIfConfig)
	{
		configArray = Ext.Array.from(configArray);

		Ext.Array.forEach(configArray, function(config, i)
				{
			if(Ext.isString(config))
			{
				config = {
						xtype: config
				};
			}
			Ext.apply(config, applyConfig, applyIfConfig);
			configArray[i] = config;
				});

		return configArray;
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/9/11
 * Time: 1:20 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.AbstractApplication
 * @extends Ext.app.Application
 */
Ext.define('Gam.app.AbstractApplication', {
	extend: 'Ext.app.Application',

	dialogViews: [],

	moduleContainer: null,

	stateful: true,

	/**
	 * @private
	 */
	onBeforeLaunch: function()
	{
		var me = this;

		if(me.enableQuickTips)
		{
			Ext.tip.QuickTipManager.init();
		}

		if(me.autoCreateViewport)
		{
			me.getView('Viewport').create({
				application: me,
				listeners: {
					afterrender: me.onViewportAfterRender,
					scope: me
				}
			});
		}

		if(me.stateful)
		{
			var stateProvider = Ext.create(me.name + '.state.Provider', {
				listeners: {
					stateload: me.onModuleStateLoad,
					scope: me
				}
			});
			Ext.state.Manager.setProvider(stateProvider);
		}

		Ext.require(me.name + '.app.GlobalConfiguration', function()
				{
			Ext.merge(Gam.GlobalConfiguration, Ext.ClassManager.get(me.name + '.app.GlobalConfiguration'))
				});

		me.launch.call(me.scope || me);
		me.launched = true;
		me.fireEvent('launch', me);
	},

	/**
	 *
	 * @param viewport
	 */
	onViewportAfterRender: function(viewport)
	{
		var me = this;

		me.moduleContainer = viewport.down("container#moduleContainer");

		if(Ext.isDefined(me.autoRun) && Ext.isString(me.autoRun) && me.autoRun.length > 0)
		{ me.launchModule(me.autoRun); }
	},

	/**
	 *
	 * @param widget
	 */
	addDialogView: function(widget)
	{
		this.dialogViews.push(widget);
	},

	/**
	 *
	 * @param moduleName
	 */
	launchModule: function(moduleName)
	{
		var controllerClassName = this.getModuleClassName(moduleName + 'Controller', 'controller');

		Gam.Msg.notifyLoading(moduleName);
		Ext.require(controllerClassName, Ext.pass(this.onLoadModule, [moduleName], this), this);
	},

	/**
	 *
	 * @param moduleName
	 */
	onLoadModule: function(moduleName)
	{
		var controllerClass = Ext.ClassManager.get(this.getModuleClassName(moduleName + 'Controller', 'controller'));

		if(this.stateful)
		{
			if(controllerClass.statefulComponents)
			{
				Ext.state.Manager.getProvider().load(moduleName, controllerClass.statefulComponents);
			}
			else
			{
				Ext.state.Manager.getProvider().cleanUp();
				this.initiateController(moduleName);
			}
		}
		else
		{
			this.initiateController(moduleName);
		}
	},

	/**
	 *
	 * @param stateProvider
	 * @param moduleName
	 * @param successful
	 */
	onModuleStateLoad: function(stateProvider, moduleName, successful)
	{
		if(!successful)
		{
			Gam.Msg.notifyLoadingFinishedWithError(moduleName);
			return;
		}

		this.initiateController(moduleName);
	},

	/**
	 *
	 * @param moduleName
	 */
	initiateController: function(moduleName)
	{
		var controller = this.getController(moduleName + 'Controller');

		controller.init(this);
		controller.createMainView();
		Gam.Msg.notifyLoadingFinishedWithoutError(moduleName);
	},

	/**
	 *
	 * @param viewType/config
	 * @param config
	 */
	createView: function()
	{
		var args = Ext.Array.from(arguments),
		widget;

		if(args.length == 1)
		{
			Ext.Array.insert(args, 0, [args[0].xtype])
		}
		widget = Ext.widget.apply(window, args);

		this.addDialogView(widget);

		return widget;
	},

	/**
	 *
	 * @param Ext.Component[]/Ext.Component... component
	 * @return Ext.Component[]/Ext.Component
	 */
	createMainView: function()
	{
		this.destroyAll();
		return this.moduleContainer.add.apply(this.moduleContainer, Ext.Array.from(arguments));
	},

	/**
	 *
	 */
	destroyAll: function()
	{
		this.moduleContainer.removeAll(true);
	}
});


/*

 This file is part of Ext JS 4

 Copyright (c) 2011 Sencha Inc

 Contact:  http://www.sencha.com/contact

 GNU General Public License Usage
 This file may be used under the terms of the GNU General Public License version 3.0 as published by the Free Software Foundation and appearing in the file LICENSE included in the packaging of this file.  Please review the following information to ensure the GNU General Public License version 3.0 requirements will be met: http://www.gnu.org/copyleft/gpl.html.

 If you are unsure which license is appropriate for your use, please contact the sales department at http://www.sencha.com/contact.

 */
/**
 * @class Gam.app.Application
 * @extend Gam.app.AbstractApplication
 */
Ext.define('Gam.app.DefaultApplication', {
	extend: 'Gam.app.AbstractApplication',

	launch: function()
	{
		if(this.autoCreateViewport)
		{
			return;
		}

		Ext.container.Viewport.create({
			layout: {
				type: 'vbox',
				align: 'stretch'
			},
			items: [
			        {
			        	xtype: 'form',
			        	title: 'Config',
			        	layout: 'anchor',
			        	height: 100,
			        	items: [
			        	        {
			        	        	xtype: 'textfield',
			        	        	allowBlank: false,
			        	        	fieldLabel: 'ModuleName',
			        	        	value: this.autoRun || '',
			        	        	anchor: '20%'
			        	        }
			        	        ],
			        	        buttons: [
			        	                  {
			        	                	  text: 'clickme',
			        	                	  handler: function(button)
			        	                	  {
			        	                		  if(button.up('panel').getForm().isValid())
			        	                		  {
			        	                			  this.launchModule(button.up('panel').down('textfield[fieldLabel=ModuleName]').getValue());
			        	                		  }
			        	                	  },
			        	                	  scope: this
			        	                  }
			        	                  ]
			        },
			        {
			        	layout: 'fit',
			        	xtype: 'container',
			        	id: 'moduleContainer',
			        	flex: 1,
			        	listeners: {
			        		render: function(moduleContainer)
			        		{
			        			this.moduleContainer = moduleContainer;
			        		},
			        		scope: this,
			        		options: {
			        			single: true
			        		}
			        	}
			        }
			        ],
			        listeners: {
			        	afterrender: function(viewport)
			        	{
			        		if(Ext.isDefined(this.autoRun) && Ext.isString(this.autoRun) && this.autoRun.length > 0)
			        		{ this.launchModule(this.autoRun);}
			        	},
			        	scope: this
			        }
		});
	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/8/11
 * Time: 5:33 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.Controller
 * @extends Ext.app.Controller
 */
Ext.define('Gam.app.controller.Controller', {
	extend: 'Ext.app.Controller',
	requires: 'Gam.app.GlobalConfiguration',

	/**
	 *
	 */
	ns: null,

	viewTypePrefix: null,

	initViewTypePostfix: null,

	initViewType: null,

	constructor: function(config)
	{
		var me = this;

		me.ns = Ext.String.removeFromEnd(me.ns, '/');
		me.viewTypePrefix = (me.ns.indexOf('/') != -1 ?
				me.ns.substring(me.ns.lastIndexOf('/') + 1, me.ns.length) :
					me.ns).toLowerCase();
		me.initViewType = me.viewTypePrefix + me.initViewTypePostfix;

		me.callParent(arguments);
	},

	getInitViewTypeConfig: function() { /*should be implemented as needed*/ },

	createMainView: function(moduleConfig)
	{
		var me = this,
		className = Ext.ClassManager.getNameByAlias('widget.' + me.initViewType);
		me.application.createMainView(Ext.apply({
			xtype: me.initViewType,
			controller: me
		}, me.getInitViewTypeConfig()));
		me.addRef({ref: className.substring(className.lastIndexOf('.') + 1), selector: me.initViewType});
	},

	init: function()
	{
		this.init = Ext.emptyFn;
	},

	createView: function()
	{
		var me = this,
		args = Ext.Array.from(arguments),
		config = args[0] || {},
		view, className;

		config.controller = me;
		args[0] = config;
		view = me.application.createView.apply(me.application, args);
		className = view.self.getName();
		me.addRef({ref: className.substring(className.lastIndexOf('.') + 1), selector: config.xtype});

		return view;
	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/13/11
 * Time: 6:51 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.FormAutocompletes
 */
Ext.define('Gam.app.controller.FormAutocompletes', {

	/**
	 *
	 */
	initFormAutocompleteController: function()
	{
		var viewTypes = Ext.Array.from(arguments),
		eventConfig = {},
		i;

		for(i = 0; i < viewTypes.length; i++)
		{
			eventConfig[viewTypes + ' form autocomplete'] = {
					autocompleteselect: this.onAutocompleteSelect,
					autocompleteclear: this.onAutocompleteClear
			};
		}

		this.control(eventConfig);
	},

	/**
	 *
	 * @param autocomplete
	 * @param record/records
	 */
	onAutocompleteSelect: function(autocomplete, record)
	{ 
		var capitalizedAutocompleteName = Ext.String.capitalize(autocomplete.getStore().autocompleteName),
		fieldSet = autocomplete.up('fieldset'),
		handlerName = 'on' + capitalizedAutocompleteName + 'Select';

		if(this[handlerName])
		{ this[handlerName](autocomplete, record); }
		else if(fieldSet && fieldSet[handlerName])
		{ fieldSet[handlerName](autocomplete, record); }
	},

	/**
	 *
	 * @param autocomplete
	 */
	onAutocompleteClear: function(autocomplete)
	{
		var autocompleteName = autocomplete.getStore().autocompleteName,
		handler = this['on' + Ext.String.capitalize(autocompleteName) + 'Clear'],
		fieldSet = autocomplete.up('fieldset');

		if(handler)
		{ handler(); }
		else if(fieldSet)
		{ fieldSet['on' + Ext.String.capitalize(autocompleteName) + 'Clear'](); }
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/13/11
 * Time: 6:51 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.GridActionColumns
 */
Ext.define('Gam.app.controller.GridActionColumns', {

	initGridActionColumnController: function()
	{
		var viewTypes = Ext.Array.from(arguments),
		eventConfig = {},
		i;

		for(i = 0; i < viewTypes.length; i++)
		{ eventConfig[viewTypes + ' actioncolumn'] = { actionclick: this.onActionClick };}

		this.control(eventConfig);
	},

	onActionClick: function(view, rowIndex, colIndex, item, e)
	{
		return this['do' + Ext.String.capitalize(item.action)](view, rowIndex, colIndex, item, e);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/8/11
 * Time: 8:28 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.GridBasedCrud
 * @extends Gam.app.controller.Controller
 */
Ext.define('Gam.app.controller.GridBasedCrud', {
	extend: 'Gam.app.controller.Controller',

	mixins: [ 'Gam.app.controller.GridActionColumns' ],

	/**
	 *
	 */
	viewDetailEnabled: true,

	/**
	 *
	 */
	initViewTypePostfix: 'grid',

	/**
	 *
	 */
	init: function()
	{
		var me = this,
		eventConfig = {};

		me.initGridActionColumnController(me.initViewType);

		if(me.viewDetailEnabled)
		{
			eventConfig[me.initViewType] = { itemdblclick: me.doViewDetailDialog };
			eventConfig[me.viewTypePrefix + 'detaildialog button[action=close]'] = { click: me.doClose };
		}

		me.control(eventConfig);

		me.callParent(arguments);
	},

	/**
	 *
	 * @return {Boolean}
	 */
	isOnValidState: function() { return true; },

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doAdd: function(grid, button) { /*empty*/},

	/**
	 *
	 * @param button
	 */
	doPrint: function(button) {/*empty*/},

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doDelete: function(grid, button)
	{
		var me = this,
		sm = grid.getSelectionModel(),
		ids = '',
		delimiter = '';

		if(!sm.hasSelection())
		{
			return;
		}

		Gam.Msg.confirmDelete(function()
				{
			var records = sm.getSelection();
			Ext.Array.forEach(records, function(record)
					{
				ids += delimiter + record.get('id');
				delimiter = ',';
					});

			Gam.Msg.showWaitMsg();
			Ext.Ajax.request({
				url: me.ns + '/' + Gam.GlobalConfiguration.CONTROLLER_ACTIONS.DELETE,
				params: { ids: ids },
				success: me.onDeleteSuccess,
				failure: me.onDeleteSuccess,
				grid: grid,
				scope: me
			});
				});
	},

	onDeleteSuccess: function(response, options)
	{
		var obj = Ext.decode(response.responseText);

		Gam.Msg.hideWaitMsg();

		if(obj.success)
		{
			options.grid.down('pagingtoolbar').doRefresh();
		}
	},

	onDeleteFailure: function(response, options) { /*empty*/ },

	/**
	 *
	 * @param view
	 * @param record
	 * @param el
	 * @param index
	 * @param e
	 * @param eOpts
	 */
	doViewDetailDialog: function(view, record, el, index, e, eOpts)
	{
		var me = this,
		dialog = me.createView({ xtype: me.viewTypePrefix + 'detaildialog' });

		if(dialog)
		{
			dialog.setTitle(Gam.Resource.title.viewLog);

			me.showDetailDialog(dialog, record);
		}
	},

	/**
	 *
	 * @param dialog
	 * @param record
	 */
	showDetailDialog: function(dialog, record)
	{
		var masterPanelsOnDialog = Ext.Array.merge(dialog.query('grid[masterOnDialog=true]'),
				dialog.query('form[masterOnDialog=true]'));


		Ext.Array.forEach(masterPanelsOnDialog, function(panel)
				{
			panel.loadByRecord(record);
				});

		dialog.show();
	},

	/**
	 *
	 * @param button
	 */
	doPrintSelectedItems: function(button)
	{
		var me = this,
		grid = me.getGrid(),
		sm = grid.getSelectionModel(),
		ids = '',
		delimiter = '';

		if(sm.hasSelection())
		{
			var records = sm.getSelection();
			Ext.Array.forEach(records, function(record)
					{
				ids += delimiter + record.get('id');
				delimiter = ',';
					});

			Gam.util.PopupWindow.open(me.ns + '/print?ids=' + ids);
		}
	},

	/**
	 *
	 * @param button
	 */
	doPrintAllItems: function(button)
	{
		Gam.util.PopupWindow.open(this.ns + '/print');
	},

	/**
	 *
	 * @param button
	 */
	doClose: function(button)
	{
		button.up('window').close();
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/4/11
 * Time: 7:03 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.FormBasedCrud
 * @extends Gam.app.controller.Controller
 */
Ext.define('Gam.app.controller.FormBasedCrud', {
	extend: 'Gam.app.controller.Controller',
	alias: 'widget.formbasedcrud',

	initViewTypePostfix: 'form',

	initComponent: function()
	{
		var me = this;

		me.callParent();
	},

	resetCorrectEntityStates: function(formPanel)
	{
		this.acceptChanges();

		Ext.Object.each(formPanel.form.baseParams, function(key, value, baseParams)
				{
			if(key.indexOf('.EntityState') != -1)
			{
				delete baseParams[key];
			}
				});
	},

	acceptChanges: function()
	{

	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/16/11
 * Time: 12:32 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.RowEditorBasedGrid
 * @extends Gam.app.controller.GridBasedCrud
 */
Ext.define('Gam.app.controller.RowEditorBasedGrid', {
	extend: 'Gam.app.controller.GridBasedCrud',

	isOnValidState: function(grid)
	{
		return grid.getView().editingPlugin.editing !== true;
	},

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doAdd: function(grid, button)
	{

		if(this.isOnValidState(grid) === false)
		{
			return;
		}

		grid.getStore().addBlankRecord();
		grid.getView().editingPlugin.startEdit(0, 0);
	},

	/**
	 *
	 * @param view
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doEdit: function(view, rowIndex, colIndex, item, e)
	{
		var record = view.store.getAt(rowIndex);

		view.editingPlugin.startEdit(record, view.getHeaderAtIndex(colIndex));
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/16/11
 * Time: 12:26 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.Controller.DialogBasedGrid
 * @extends Gam.app.controller.GridBasedCrud
 */
Ext.define('Gam.app.controller.DialogBasedGrid', {
	extend: 'Gam.app.controller.GridBasedCrud',

	mixins: ['Gam.app.controller.FormAutocompletes'],

	/**
	 *
	 */
	init: function()
	{
		var me = this,
		eventConfig = {};

		me.initFormAutocompleteController(me.viewTypePrefix + 'dialog');

		eventConfig[me.viewTypePrefix + 'dialog button[action=save]'] = { click: me.doSave };
		eventConfig[me.viewTypePrefix + 'dialog button[action=close]'] = { click: me.doClose };
		eventConfig[me.viewTypePrefix + 'dialog button[action=cancel]'] = { click: me.doCancel };
		eventConfig[me.viewTypePrefix + 'extradialog button[action=save]'] = { click: me.doSave };
		eventConfig[me.viewTypePrefix + 'extradialog button[action=cancel]'] = { click: me.doCancel };

		me.control(eventConfig);

		me.callParent(arguments);
	},

	/**
	 *
	 * @param view
	 * @param records
	 */
	loadFormViaStore: function(view, records)
	{
		var formPanel = view.down('form');
		formPanel.loadRecords(records);
	},

	/**
	 *
	 * @param view
	 * @param record
	 * @param baseUrl
	 * @param params
	 */
	loadFormViaServer: function(view, record, baseUrl, params)
	{
		var me = this,
		formPanel = view.down('form');

		params = params || {};
		Ext.apply(params, {
			readOnly: view.isReadOnly()
		});

		formPanel.load({
			url: (baseUrl || me.ns) + '/' + Gam.GlobalConfiguration.CONTROLLER_ACTIONS.LOAD_BY_ID,
			params: params,
			success: me.onLoadSuccess,
			failure: me.onLoadFailure,
			waitMsg: Gam.Resource.message.info.formLoading,
			scope: me
		});
	},

	/**
	 *
	 * @param form
	 * @param action
	 */
	onLoadSuccess: function(form, action)
	{
		var formPanel = form.owner,
		dialog = formPanel.up('entitydialog');
		formPanel.items.each(function(item)
				{
			if(!(item instanceof Gam.form.FieldSet))
			{
				return;
			}
			if(dialog.isReadOnly())
			{
				item.onReadOnlyFormLoadSuccess(form, action);
			}
			else
			{
				item.onEditableFormLoadSuccess(form, action);
			}
				});
	},

	/**
	 *
	 * @param form
	 * @param action
	 */
	onLoadFailure: function(form, action)
	{
		switch(action.failureType)
		{
		case 'connect':
			Gam.Msg.showErrorMsg(Gam.Resource.message.failure.connection);
			break;
		default:
			Gam.Msg.showErrorMsg(action.result.feedback || Gam.Resource.message.failure.loadData);
		break;
		}
	},

	/**
	 *
	 * @param gridView
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doEdit: function(gridView, rowIndex, colIndex, item, e) { /*empty*/ },

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doRegisterExtraInfo: function(grid, button) { /*empty*/ },

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doSave: function(grid, button) { /*empty*/ },

	/**
	 *
	 * @param gridView
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doView: function(gridView, rowIndex, colIndex, item, e) { /*empty*/ },

	/**
	 *
	 * @param button
	 */
	beforeSave: function(button)
	{
		var dialog = button.up('entitydialog'),
		formPanel = dialog.down('form');

		if(!Gam.util.Form.isValid(formPanel.form))
		{
			return false;
		}

		if(formPanel.beforeSave() === false)
		{
			return false;
		}

		if(!formPanel.isDirty())
		{
			Gam.Msg.showInfoMsg(Gam.Resource.message.failure.notEditedForm);
			return false;
		}

		formPanel.setCorrectEntityStates();
	},

	/**
	 *
	 * @param {Gam.form.Panel} formPanel
	 * @param {Boolean} [continuousDataEntry]
	 */
	syncStore: function(formPanel, continuousDataEntry, clearDataEntry )
	{
		var me = this;

		formPanel.form.updateRecords();
		Gam.Msg.showWaitMsg();
		formPanel.getRecords()[0].stores[0].sync({
			success: me.onSyncSuccess,
			failure: me.onSyncFailure,
			continuousDataEntry: continuousDataEntry || Gam.GlobalConfiguration.dialogBasedGrid.continuousDataEntry,
			// save and repeat Data Entry config
			clearDataEntry : clearDataEntry || Gam.GlobalConfiguration.dialogBasedGrid.clearDataEntry ,
			formPanel: formPanel ,
			scope: me
		});
	},

	/**
	 *
	 * @param batch
	 * @param options
	 */
	onSyncSuccess: function(batch, options)
	{
		Gam.Msg.hideWaitMsg();
		var currentActionUrl = batch.operations[0].request.url;

//		if(currentActionUrl.indexOf('office') != -1){
//		setTimeout(function() {
//		location.reload();
//		}, 1000);
//		}
	},

	/**
	 *
	 * @param batch
	 * @param options
	 */
	onSyncFailure: function(batch, options) { 
		var currentActionUrl = batch.operations[0].request.url;
		
		if(currentActionUrl.indexOf('office') != -1 && timeOutFlag){
			setTimeout(function() {
				location.reload();
			}, 10000);
		}
		timeOutFlag = false;
	/*empty*/ },

	/**
	 *
	 * @param formPanel
	 * @param action
	 * @param baseUrl
	 * @param clientValidation
	 * @param continuousDataEntry
	 */
	submitForm: function(formPanel, action, baseUrl, clientValidation, continuoerrusDataEntry)
	{
		var me = this;

		clientValidation = Ext.isDefined(clientValidation) ? clientValidation : true;
		formPanel.form.submit({
			url: (baseUrl || me.ns) + '/' + action,
			waitMsg: Gam.Resource.message.info.formSubmitWaiting,
			success: me.onSubmitSuccess,
			failure: me.onSubmitFailure,
			clientValidation: clientValidation,
			recursiveSerialization: true,
			continuousDataEntry: continuousDataEntry || Gam.GlobalConfiguration.dialogBasedGrid.continuousDataEntry,
			scope: me
		});
	},

	/**
	 *
	 * @param form
	 * @param action
	 */
	onSubmitSuccess: function(form, action)
	{
		var formPanel = form.owner,
		dialog = formPanel.up('entitydialog');

		if(action.options.continuousDataEntry !== false)
		{
			dialog.close();
		}
		else
		{
			form.reset();
			formPanel.focus();
		}
	},

	/**
	 *
	 * @param form
	 * @param action
	 */
	onSubmitFailure: function(form, action)
	{
	},


	/**
	 *
	 * @param button
	 */
	doCancel: function(button)
	{
		this.doClose(button);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/16/11
 * Time: 1:34 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.LocalDialogBasedGrid
 * @extends Gam.app.controller.DialogBasedGrid
 */
Ext.define('Gam.app.controller.LocalDialogBasedGrid', {
	extend: 'Gam.app.controller.DialogBasedGrid',

	/**
	 *
	 */
	init: function()
	{

		var me = this,
		eventConfig = {};

		eventConfig[me.viewTypePrefix + 'dialog'] = { close: me.onClose };
		eventConfig[me.viewTypePrefix + 'extradialog'] = { close: me.onClose };

		me.control(eventConfig);

		me.callParent(arguments);
	},

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doAdd: function(grid, button)
	{

		if(this.isOnValidState() === false)
		{
			return;
		}

		var me = this,
		dialog = me.createView({
			xtype: button.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.ADD,
			opener: grid
		});

		if(dialog)
		{
			dialog.setTitle(Gam.Resource.title.add);
			me.addNewRecordForDialog(dialog);
			dialog.show();
		}
	},

	addNewRecordForDialog: function(dialog)
	{
		var record = this.getGrid().getStore().addBlankRecord(),
		formPanel = dialog.down('form');

		formPanel.bindRecords(record);
	},

	/**
	 *
	 * @param gridView
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doEdit: function(gridView, rowIndex, colIndex, item, e)
	{
		var me = this,
		gamGrid = gridView.up(),
		record = gridView.store.getAt(rowIndex),
		dialog = me.createView({
			xtype: item.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.EDIT,
			opener: gamGrid
		});

		dialog.setTitle(Gam.Resource.title.edit);
		dialog.show();
		me.loadFormViaStore(dialog, record);
	},

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doRegisterExtraInfo: function(grid, button)
	{
		var me = this,
		sm = grid.getSelectionModel(),
		dialog = me.createView({
			xtype: button.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.EDIT,
			opener: grid
		});

		dialog.show();
		me.loadFormViaStore(dialog, sm.getSelection());
	},

	/**
	 *
	 * @param {Gam.button.Save} button
	 */
	doSave: function(button)
	{

		var me = this,
		dialog = button.up('entitydialog'),
		formPanel = dialog.down('form');

		if(me.beforeSave(button) === false)
		{
			return;
		}

		me.syncStore(formPanel , Gam.GlobalConfiguration.dialogBasedGrid.continuousDataEntry
				, Gam.GlobalConfiguration.dialogBasedGrid.clearDataEntry);
	},

	/**
	 *
	 * @param batch
	 * @param options
	 */
	onSyncSuccess: function( batch, options)
	{

		var me = this,
		formPanel = options.formPanel,
		dialog = formPanel.up('entitydialog');

		me.callParent(arguments);

		if(dialog.isAddAction() && options.continuousDataEntry && (!options.clearDataEntry))
		{
			//save data and repeat
			formPanel.form.reset();
			me.addNewRecordForDialog(dialog);
			formPanel.focus();
		}
		else if (dialog.isAddAction() && options.continuousDataEntry && options.clearDataEntry)
		{
			//save repetitive data and repeat
			me.addNewRecordForDialog(dialog);
			formPanel.focus();
		}
		else
		{
			dialog.close();
		}
	},

	/**
	 *
	 * @param batch
	 * @param options
	 */
	onSyncFailure: function(batch, options)
	{

		var me = this,
		rawData = batch.proxy.reader.rawData,
		messageInfo = rawData.messageInfo;

		me.callParent(arguments);

		if(messageInfo.autoShow)
		{ return; }

		var callbackButtons = messageInfo.callbackButtons || [];
		if(messageInfo.manner == Gam.GlobalConfiguration.MESSAGE.MANNERS.CONFIRMATION)
		{
			Ext.Msg.confirm({
				title: Gam.Resource.title.warning,
				msg: messageInfo.message,
				icon: Ext.Msg[messageInfo.icon],
				buttons: Ext.Msg[messageInfo.confirmationType.toUpperCase()],
				callback: function(button)
				{
					if(Ext.Array.indexOf(callbackButtons, button) != -1)
					{
						Gam.Msg.showWaitMsg();
						options.formPanel.getRecords()[0].stores[0].sync({
							waitMsg: Gam.Resource.message.info.formSubmitWaiting,
							success: me.onSyncSuccess,
							failure: me.onSyncFailure,
							continuousDataEntry: options.continuousDataEntry,
							formPanel: options.formPanel,
							params: {
								messageButton: button,
								token: rawData.token
							},
							scope: options.scope
						});
					}
				}
			});
		}
	},

	/**
	 *
	 * @param gridView
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doView: function(gridView, rowIndex, colIndex, item, e)
	{
		var me = this,
		record = gridView.store.getAt(rowIndex),
		dialog = me.createView({
			xtype: item.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.VIEW
		});

		dialog.setTitle(Gam.Resource.title.view);
		me.loadFormViaStore(dialog, record);
		dialog.show();
	},

	/**
	 *
	 * @param entityDialog
	 * @param eOptions
	 */
	onClose: function(entityDialog, eOptions)
	{

		if(entityDialog.isAddAction())
		{
			var store = entityDialog.opener.getStore();

			store.remove(store.getNewRecords());
		}
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/16/11
 * Time: 1:33 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.app.controller.RemoteDialogBasedGrid
 * @extends Gam.app.controller.DialogBasedGrid
 */
Ext.define('Gam.app.controller.RemoteDialogBasedGrid', {
	extend: 'Gam.app.controller.DialogBasedGrid',

	/**
	 *
	 */
	init: function()
	{
		var me = this,
		eventConfig = {};

		eventConfig[me.viewTypePrefix + 'dialog'] = { close: me.onClose };
		eventConfig[me.viewTypePrefix + 'extradialog'] = { close: me.onClose };

		me.control(eventConfig);

		me.callParent(arguments);
	},

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doAdd: function(grid, button)
	{

		if(this.isOnValidState() === false)
		{
			/*this.showParentIdError();*/
			return;
		}

		var me = this,
		dialog = me.createView({
			xtype: button.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.ADD,
			opener: grid
		});

		if(dialog)
		{
			dialog.setTitle(Gam.Resource.title.add);
			/*dialog.on('savesuccess', this.reloadStore, this);*/
			dialog.setBaseParams();
			me.addNewRecordForDialog(dialog);
			dialog.show();
		}
	},

	/**
	 *
	 * @param gridView
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doEdit: function(gridView, rowIndex, colIndex, item, e)
	{
		var me = this,
		gamGrid = gridView.up(),
		record = gridView.store.getAt(rowIndex),
		dialog = me.createView({
			xtype: item.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.EDIT,
			opener: gamGrid
		});

		dialog.setTitle(Gam.Resource.title.edit);
		dialog.setBaseParams(record);
		dialog.show();
		me.loadFormViaServer(dialog, record);
	},

	/**
	 *
	 * @param grid
	 * @param button
	 */
	doRegisterExtraInfo: function(grid, button)
	{
		var me = this,
		sm = grid.getSelectionModel(),
		dialog = me.createView({
			xtype: button.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.EDIT,
			opener: grid
		});

		dialog.show();
		me.loadFormViaServer(dialog, sm.getSelection());
	},

	/**
	 *
	 * @param button
	 */
	doSave: function(button)
	{

		var me = this,
		formPanel = button.up('window').down('form');

		if(me.beforeSave(button) === false)
		{
			return;
		}
		me.submitForm(formPanel, Gam.GlobalConfiguration.CONTROLLER_ACTIONS.SAVE);
	},

	/**
	 *
	 * @param gridView
	 * @param rowIndex
	 * @param colIndex
	 * @param item
	 * @param e
	 */
	doView: function(gridView, rowIndex, colIndex, item, e)
	{
		var me = this,
		gamGrid = gridView.up(),
		record = gridView.store.getAt(rowIndex),
		dialog = me.createView({
			xtype: item.viewType,
			actionType: Gam.GlobalConfiguration.ACTION_TYPES.VIEW,
			opener: gamGrid
		});

		dialog.setTitle(Gam.Resource.title.view);
		dialog.setBaseParams(record);
		dialog.show();
		me.loadFormViaServer(dialog, record);
	},

	/**
	 *
	 * @param entityDialog
	 * @param eOptions
	 */
	onClose: function(entityDialog, eOptions) {
		"use strict";

		if (entityDialog.isAddAction()) {
			var store = entityDialog.opener.getStore();
			store.remove(store.getNewRecords());
		}
	},

	/**
	 *
	 * @param dialog
	 */
	addNewRecordForDialog: function (dialog) {
		"use strict";

		var record = this.getGrid().getStore().addBlankRecord(),
		formPanel = dialog.down('form');

		formPanel.bindRecords(record);
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:45 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.Button
 * @extends Ext.button.Button
 */
Ext.define('Gam.button.Button', {
	extend: 'Ext.button.Button',
	alias: 'widget.gam.button',

	minWidth: 65,

	addStateEvents: Ext.emptyFn,

	initComponent: function()
	{
		var me = this;

		if(!me.stateId && me.baseStateId && (me.stateful || me.stateManagement !== false))
		{
			me.stateId = me.baseStateId + Ext.String.capitalize(me.action);
		}
		delete me.baseStateId;

		me.callParent();
	},

	show: function()
	{
		if(Gam.security.Acl.isChangeable(this.stateId, Gam.security.Acl.attributeNames.HIDDEN))
		{
			this.callParent(arguments);
		}
	},

	enable: function()
	{
		if(Gam.security.Acl.isChangeable(this.stateId, Gam.security.Acl.attributeNames.DISABLED))
		{
			this.callParent(arguments);
		}
	}

//	applyState: function(state)
//	{
//	this.callParent(arguments);

//	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:52 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.Delete
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Delete', {
	extend: 'Gam.button.Button',
	alias: 'widget.deletebtn',

	text: Gam.Resource.label.remove,

	iconCls: 'delete-btn',

	action: 'delete'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:51 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.Add
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Add', {
	extend: 'Gam.button.Button',
	alias: 'widget.gam.addbtn',

	text: Gam.Resource.label.add,

	iconCls: 'add-btn',

	action: 'add'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:51 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.Print
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Print', {
	extend: 'Gam.button.Button',
	alias: 'widget.gam.printbtn',

	text: Gam.Resource.label.print,

	iconCls: 'print-btn',

	action: 'print'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:51 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.PrintMenu
 * @extends Gam.button.Print
 */
Ext.define('Gam.button.PrintMenu', {
	extend: 'Gam.button.Print',
	alias: 'widget.printmenubtn',

	text: Gam.Resource.label.print,

	iconCls: 'print-btn',

	action: 'print'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:49 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.EditButton
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Save', {
	extend: 'Gam.button.Button',
	alias: 'widget.savebtn',

	text: Gam.Resource.label.save,

	iconCls: 'save-btn',

	action: 'save'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:49 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.Close
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Close', {
	extend: 'Gam.button.Button',
	alias: 'widget.closebtn',

	text: Gam.Resource.label.close,

	iconCls: 'close-btn',

	action: 'close'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:49 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.button.Cancel
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Cancel', {
	extend: 'Gam.button.Button',
	alias: 'widget.cancelbtn',

	text: Gam.Resource.label.cancel,

	iconCls: 'close-btn',

	action: 'cancel'
});


/**
 *
 * @author R. Bakhshpour
 * @class Gam.button.Search
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Search', {
	extend: 'Gam.button.Button',
	alias: 'widget.searchbtn',

	text: Gam.Resource.label.search,

	iconCls: 'search-btn',

	action: 'search'
});

/**
 *
 * @author R. Bakhshpour
 * @class Gam.button.Insert
 * @extends Gam.button.Button
 */
Ext.define('Gam.button.Insert', {
	extend: 'Gam.button.Button',
	alias: 'widget.insertbtn',

	text: Gam.Resource.label.insert,

	iconCls: 'insert-btn',

	action: 'insert'
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/2/11
 * Time: 6:03 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.toolbar.Toolbar
 * @extends Ext..
 */
Ext.define('Gam.toolbar.Toolbar', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.gam.toolbar',
	required: [
	           'Gam.button.Add',
	           'Gam.button.Delete',
	           'Gam.button.Print'
	           ],

	           statics: {
	        	   shortcuts: {
	        		   'add': 'addbtn',
	        		   'delete': 'deletebtn',
	        		   'print': 'printbtn'
	        	   }
	           },

	           defaultType: 'gam.button',

	           // private
	           lookupComponent: function(c)
	           {
	        	   if(typeof c == 'string')
	        	   {
	        		   var T = Gam.toolbar.Toolbar,
	        		   parts = c.split(Gam.GlobalConfiguration.viewTypeSeparator),
	        		   shortcut = T.shortcuts[parts[0]];

	        		   if(typeof shortcut == 'string')
	        		   {
	        			   c = {
	        					   xtype: shortcut
	        			   };
	        		   }
	        		   if(parts.length > 1)
	        		   {
	        			   c.viewType = parts[1];
	        		   }

	        		   this.applyDefaults(c);
	        	   }

	        	   return this.callParent(arguments);
	           }
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/9/11
 * Time: 8:18 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.toolbar.Paging
 * @extends Ext.toolbar.Paging
 */
Ext.define('Gam.toolbar.Paging', {
	extend: 'Ext.toolbar.Paging',
	alias: 'widget.gam.pagingtoolbar',

	initComponent:function(){
		var me = this ;
		me.store.pageSize = Gam.GlobalConfiguration.GRID_PAGE_SIZE;
		//using gridPageSizeCombo the records numebr of the grid may be changed to 10 , 25, 50, 75 and 100.
		var gridPageSizeCombo= Ext.create('Ext.form.ComboBox' ,{
			fieldLabel   :  'تعداد رکوردهای قابل نمایش' ,
			labelWidth   : 150,
			xtype        : 'combo',
			store        : ['10','25','50','75','100'],
			value        : Gam.GlobalConfiguration.GRID_PAGE_SIZE.toString(),
			emptyText    : Gam.Resource.comboEmptyText,
			width        : 250 ,
			listeners    : {
				select : me.onComboSelect
			}
		});
		//adding filter cleaner to remove all grid filters
		if  (me.showFilterCleanerButton === true) {

			me.buttons = [{
				xtype           : 'gam.button',
				minWidth        : 0,
				tooltip         : 'حذف فیلترها',
				overflowText    : 'حذف فیلتر ها',
				iconCls         : Ext.baseCSSPrefix+ 'tbar-clearfiltersbutton' ,
				handler         : me.doCleanFilters,
				scope           : me
			},{
				xtype: 'tbseparator'
			},
			gridPageSizeCombo];
		}else
		{
			me.buttons = [{
				xtype : 'tbseparator'
			},
			gridPageSizeCombo];
		}
		me.callParent(arguments);
	} ,

	doCleanFilters : function(){
		var me = this ;
		me.ownerCt.getPlugin().filterbar.clearFilter();
	},
	onComboSelect : function(comboBox,value) {
		var me =  this ;
		me.ownerCt.store.pageSize = parseInt(value[0].raw[0]);
		me.ownerCt.store.loadPage(1);
		me.ownerCt.doRefresh();
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/12/11
 * Time: 5:06 PM
 */

/**
 * @class Gam.toolbar.AutocompletePaging
 * @extends Ext.toolbar.Toolbar
 * <p>As the amount of records increases, the time required for the browser to render
 * them increases. Paging is used to reduce the amount of data exchanged with the client.
 * Note: if there are more records/rows than can be viewed in the available screen area, vertical
 * scrollbars will be added.</p>
 * <p>Paging is typically handled on the server side (see exception below). The client sends
 * parameters to the server side, which the server needs to interpret and then respond with the
 * appropriate data.</p>
 * <p><b>Ext.toolbar.Paging</b> is a specialized toolbar that is bound to a {@link Ext.data.Store}
 * and provides automatic paging control. This Component {@link Ext.data.Store#load load}s blocks
 * of data into the <tt>{@link #store}</tt> by passing {@link Ext.data.Store#paramNames paramNames} used for
 * paging criteria.</p>
 *
 * {@img Ext.toolbar.Paging/Ext.toolbar.Paging.png Ext.toolbar.Paging component}
 *
 * <p>PagingToolbar is typically used as one of the Grid's toolbars:</p>
 * <pre><code>
 *	var itemsPerPage = 2;   // set the number of items you want per page
 *
 *	var store = Ext.create('Ext.data.Store', {
 *		id:'simpsonsStore',
 *		autoLoad: false,
 *		fields:['name', 'email', 'phone'],
 *		pageSize: itemsPerPage, // items per page
 *		proxy: {
 *			type: 'ajax',
 *			url: 'pagingstore.js',  // url that will load data with respect to start and limit params
 *			reader: {
 *				type: 'json',
 *				root: 'items',
 *				totalProperty: 'total'
 *			}
 *		}
 *	});
 *
 *	// specify segment of data you want to load using params
 *	store.load({
 *		params:{
 *			start:0,
 *			limit: itemsPerPage
 *		}
 *	});
 *
 *	Ext.create('Ext.grid.Panel', {
 *		title: 'Simpsons',
 *		store: store,
 *		columns: [
 *			{header: 'Name',  dataIndex: 'name'},
 *			{header: 'Email', dataIndex: 'email', flex:1},
 *			{header: 'Phone', dataIndex: 'phone'}
 *		],
 *		width: 400,
 *		height: 125,
 *		dockedItems: [{
 *			xtype: 'pagingtoolbar',
 *			store: store,   // same store GridPanel is using
 *			dock: 'bottom',
 *			displayInfo: true
 *		}],
 *		renderTo: Ext.getBody()
 *	});
 * </code></pre>
 *
 * <p>To use paging, pass the paging requirements to the server when the store is first loaded.</p>
 * <pre><code>
 store.load({
 params: {
 // specify params for the first page load if using paging
 start: 0,
 limit: myPageSize,
 // other params
 foo:   'bar'
 }
 });
 * </code></pre>
 *
 * <p>If using {@link Ext.data.Store#autoLoad store's autoLoad} configuration:</p>
 * <pre><code>
 var myStore = new Ext.data.Store({
 {@link Ext.data.Store#autoLoad autoLoad}: {start: 0, limit: 25},
 ...
 });
 * </code></pre>
 *
 * <p>The packet sent back from the server would have this form:</p>
 * <pre><code>
 {
 "success": true,
 "results": 2000,
 "rows": [ // <b>*Note:</b> this must be an Array
 { "id":  1, "name": "Bill", "occupation": "Gardener" },
 { "id":  2, "name":  "Ben", "occupation": "Horticulturalist" },
 ...
 { "id": 25, "name":  "Sue", "occupation": "Botanist" }
 ]
 }
 * </code></pre>
 * <p><u>Paging with Local Data</u></p>
 * <p>Paging can also be accomplished with local data using extensions:</p>
 * <div class="mdetail-params"><ul>
 * <li><a href="http://sencha.com/forum/showthread.php?t=71532">Ext.ux.data.PagingStore</a></li>
 * <li>Paging Memory Proxy (examples/ux/PagingMemoryProxy.js)</li>
 * </ul></div>
 */
Ext.define('Gam.toolbar.AutocompletePaging', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.gam.autocompletepagingtoolbar',
	requires: ['Ext.toolbar.TextItem', 'Ext.form.field.Number'],
	/**
	 * @cfg {Ext.data.Store} store
	 * The {@link Ext.data.Store} the paging toolbar should use as its data source (required).
	 */
	/**
	 * @cfg {Boolean} displayInfo
	 * <tt>true</tt> to display the displayMsg (defaults to <tt>false</tt>)
	 */
	displayInfo: false,
	/**
	 * @cfg {Boolean} prependButtons
	 * <tt>true</tt> to insert any configured <tt>items</tt> <i>before</i> the paging buttons.
	 * Defaults to <tt>false</tt>.
	 */
	prependButtons: false,
	/**
	 * @cfg {String} displayMsg
	 * The paging status message to display (defaults to <tt>'Displaying {0} - {1} of {2}'</tt>).
	 * Note that this string is formatted using the braced numbers <tt>{0}-{2}</tt> as tokens
	 * that are replaced by the values for start, end and total respectively. These tokens should
	 * be preserved when overriding this string if showing those values is desired.
	 */
	displayMsg: 'Displaying {0} - {1} of {2}',
	/**
	 * @cfg {String} emptyMsg
	 * The message to display when no records are found (defaults to 'No data to display')
	 */
	emptyMsg: 'No data to display',

	refreshText: Gam.Resource.label.refreshText,
	/**
	 * @cfg {Number} inputItemWidth
	 * The width in pixels of the input field used to display and change the current page number (defaults to 30).
	 */
	inputItemWidth: 30,

	/**
	 * Gets the standard paging items in the toolbar
	 * @private
	 */
	getPagingItems: function()
	{
		var me = this;

		return [
		        {
		        	itemId: 'first',
		        	tooltip: Gam.Resource.label.firstPageText,
		        	overflowText: Gam.Resource.label.firstPageText,
		        	iconCls: Ext.baseCSSPrefix + 'tbar-page-first',
		        	disabled: true,
		        	handler: me.moveFirst,
		        	scope: me
		        },
		        {
		        	itemId: 'prev',
		        	tooltip: Gam.Resource.label.prevPageText,
		        	overflowText: Gam.Resource.label.prevPageText,
		        	iconCls: Ext.baseCSSPrefix + 'tbar-page-prev',
		        	disabled: true,
		        	handler: me.movePrevious,
		        	scope: me
		        },
		        '-',
		        {
		        	xtype: 'numberfield',
		        	itemId: 'inputItem',
		        	name: 'inputItem',
		        	cls: Ext.baseCSSPrefix + 'tbar-page-number',
		        	allowDecimals: false,
		        	minValue: 1,
		        	hideTrigger: true,
		        	enableKeyEvents: true,
		        	selectOnFocus: true,
		        	submitValue: false,
		        	width: me.inputItemWidth,
		        	margins: '-1 2 3 2',
		        	listeners: {
		        		scope: me,
		        		keydown: me.onPagingKeyDown,
		        		blur: me.onPagingBlur
		        	}
		        },
		        {
		        	xtype: 'tbtext',
		        	itemId: 'afterTextItem',
		        	text: Ext.String.format(Gam.Resource.label.afterPageText, 1)
		        },
		        '-',
		        {
		        	itemId: 'next',
		        	tooltip: Gam.Resource.label.nextPageText,
		        	overflowText: Gam.Resource.label.nextPageText,
		        	iconCls: Ext.baseCSSPrefix + 'tbar-page-next',
		        	disabled: true,
		        	handler: me.moveNext,
		        	scope: me
		        },
		        {
		        	itemId: 'last',
		        	tooltip: Gam.Resource.label.lastPageText,
		        	overflowText: Gam.Resource.label.lastPageText,
		        	iconCls: Ext.baseCSSPrefix + 'tbar-page-last',
		        	disabled: true,
		        	handler: me.moveLast,
		        	scope: me
		        }
		        ];
	},

	initComponent: function()
	{
		var me = this,
		pagingItems = me.getPagingItems(),
		userItems = Ext.Array.from(me.items || me.buttons || []);

		if(userItems.length == 0)
		{
			me.layout = {
					pack: 'center'
			};
		}
		else
		{
			userItems.unshift('->');
		}

		me.items = Ext.Array.merge(pagingItems, userItems);
		delete me.buttons;

		if(me.displayInfo)
		{
			me.items.push('->');
			me.items.push({xtype: 'tbtext', itemId: 'displayItem'});
		}

		me.callParent();

		me.addEvents(
				/**
				 * @event change
				 * Fires after the active page has been changed.
				 * @param {Ext.toolbar.Paging} this
				 * @param {Object} pageData An object that has these properties:<ul>
				 * <li><code>total</code> : Number <div class="sub-desc">The total number of records in the dataset as
				 * returned by the server</div></li>
				 * <li><code>currentPage</code> : Number <div class="sub-desc">The current page number</div></li>
				 * <li><code>pageCount</code> : Number <div class="sub-desc">The total number of pages (calculated from
				 * the total number of records in the dataset as returned by the server and the current {@link #pageSize})</div></li>
				 * <li><code>toRecord</code> : Number <div class="sub-desc">The starting record index for the current page</div></li>
				 * <li><code>fromRecord</code> : Number <div class="sub-desc">The ending record index for the current page</div></li>
				 * </ul>
				 */
				'change',
				/**
				 * @event beforechange
				 * Fires just before the active page is changed.
				 * Return false to prevent the active page from being changed.
				 * @param {Ext.toolbar.Paging} this
				 * @param {Number} page The page number that will be loaded on change
				 */
				'beforechange'
		);
		me.on('afterlayout', me.onLoad, me, {single: true});

		me.bindStore(me.store, true);
	},
	// private
	updateInfo: function()
	{
		var me = this,
		displayItem = me.child('#displayItem'),
		store = me.store,
		pageData = me.getPageData(),
		count, msg;

		if(displayItem)
		{
			count = store.getCount();
			if(count === 0)
			{
				msg = me.emptyMsg;
			}
			else
			{
				msg = Ext.String.format(
						me.displayMsg,
						pageData.fromRecord,
						pageData.toRecord,
						pageData.total
				);
			}
			displayItem.setText(msg);
			me.doComponentLayout();
		}
	},

	// private
	onLoad: function()
	{

		var me = this,
		pageData,
		currPage,
		pageCount,
		afterText;

		if(!me.rendered)
		{
			return;
		}

		pageData = me.getPageData();
		currPage = pageData.currentPage;
		pageCount = pageData.pageCount;
		afterText = Ext.String.format(Gam.Resource.label.afterPageText, isNaN(pageCount) ? 1 : pageCount);

		me.child('#afterTextItem').setText(afterText);
		me.child('#inputItem').setValue(currPage);
		me.child('#first').setDisabled(currPage === 1);
		me.child('#prev').setDisabled(currPage === 1);
		me.child('#next').setDisabled(currPage === pageCount);
		me.child('#last').setDisabled(currPage === pageCount);
		me.updateInfo();
		me.fireEvent('change', me, pageData);
	},

	// private
	getPageData: function()
	{ 
		var store = this.store,
		totalCount = store.getTotalCount();

		return {
			total: totalCount,
			currentPage: store.currentPage,
			pageCount: Math.ceil(totalCount / store.pageSize),
			fromRecord: ((store.currentPage - 1) * store.pageSize) + 1,
			toRecord: Math.min(store.currentPage * store.pageSize, totalCount)

		};
	},

	// private
	onLoadError: function()
	{
		if(!this.rendered)
		{
			return;
		}
		this.child('#refresh').enable();
	},

	// private
	readPageFromInput: function(pageData)
	{
		var v = this.child('#inputItem').getValue(),
		pageNum = parseInt(v, 10);

		if(!v || isNaN(pageNum))
		{
			this.child('#inputItem').setValue(pageData.currentPage);
			return false;
		}
		return pageNum;
	},

	onPagingFocus: function()
	{
		this.child('#inputItem').select();
	},

	//private
	onPagingBlur: function(e)
	{
		var curPage = this.getPageData().currentPage;
		this.child('#inputItem').setValue(curPage);
	},

	// private
	onPagingKeyDown: function(field, e)
	{
		var me = this,
		k = e.getKey(),
		pageData = me.getPageData(),
		increment = e.shiftKey ? 10 : 1,
				pageNum;

		if(k == e.RETURN)
		{
			e.stopEvent();
			pageNum = me.readPageFromInput(pageData);
			if(pageNum !== false)
			{
				pageNum = Math.min(Math.max(1, pageNum), pageData.pageCount);
				if(me.fireEvent('beforechange', me, pageNum) !== false)
				{
					me.store.loadPage(pageNum);
				}
			}
		} else if(k == e.HOME || k == e.END)
		{
			e.stopEvent();
			pageNum = k == e.HOME ? 1 : pageData.pageCount;
			field.setValue(pageNum);
		} else if(k == e.UP || k == e.PAGEUP || k == e.DOWN || k == e.PAGEDOWN)
		{
			e.stopEvent();
			pageNum = me.readPageFromInput(pageData);
			if(pageNum)
			{
				if(k == e.DOWN || k == e.PAGEDOWN)
				{
					increment *= -1;
				}
				pageNum += increment;
				if(pageNum >= 1 && pageNum <= pageData.pages)
				{
					field.setValue(pageNum);
				}
			}
		}
	},

	// private
	beforeLoad: function()
	{
		if(this.rendered && this.refresh)
		{
			this.refresh.disable();
		}
	},

	// private
	doLoad: function(start)
	{
		if(this.fireEvent('beforechange', this, o) !== false)
		{
			this.store.load();
		}
	},

	/**
	 * Move to the first page, has the same effect as clicking the 'first' button.
	 */
	moveFirst: function()
	{
		if(this.fireEvent('beforechange', this, 1) !== false)
		{
			this.store.loadPage(1);
		}
	},

	/**
	 * Move to the previous page, has the same effect as clicking the 'previous' button.
	 */
	movePrevious: function()
	{
		var me = this,
		prev = me.store.currentPage - 1;

		if(prev > 0)
		{
			if(me.fireEvent('beforechange', me, prev) !== false)
			{
				me.store.previousPage();
			}
		}
	},

	/**
	 * Move to the next page, has the same effect as clicking the 'next' button.
	 */
	moveNext: function()
	{
		var me = this,
		total = me.getPageData().pageCount,
		next = me.store.currentPage + 1;

		if(next <= total)
		{
			if(me.fireEvent('beforechange', me, next) !== false)
			{
				me.store.nextPage();
			}
		}
	},

	/**
	 * Move to the last page, has the same effect as clicking the 'last' button.
	 */
	moveLast: function()
	{
		var me = this,
		last = me.getPageData().pageCount;

		if(me.fireEvent('beforechange', me, last) !== false)
		{
			me.store.loadPage(last);
		}
	},

	/**
	 * Binds the paging toolbar to the specified {@link Ext.data.Store}
	 * @param {Store} store The store to bind to this toolbar
	 * @param {Boolean} initial (Optional) true to not remove listeners
	 */
	bindStore: function(store, initial)
	{
		var me = this;

		if(!initial && me.store)
		{
			if(store !== me.store && me.store.autoDestroy)
			{
				me.store.destroy();
			}
			else
			{
				me.store.un('beforeload', me.beforeLoad, me);
				me.store.un('load', me.onLoad, me);
				me.store.un('exception', me.onLoadError, me);
			}
			if(!store)
			{
				me.store = null;
			}
		}
		if(store)
		{
			store = Ext.data.StoreManager.lookup(store);
			store.on({
				scope: me,
				beforeload: me.beforeLoad,
				load: me.onLoad,
				exception: me.onLoadError
			});
		}
		me.store = store;
	},

	/**
	 * Unbinds the paging toolbar from the specified {@link Ext.data.Store} <b>(deprecated)</b>
	 * @param {Ext.data.Store} store The data store to unbind
	 */
	unbind: function(store)
	{
		this.bindStore(null);
	},

	/**
	 * Binds the paging toolbar to the specified {@link Ext.data.Store} <b>(deprecated)</b>
	 * @param {Ext.data.Store} store The data store to bind
	 */
	bind: function(store)
	{
		this.bindStore(store);
	},

	// private
	onDestroy: function()
	{
		this.bindStore(null);
		this.callParent();
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 9/13/11
 * Time: 2:48 PM
 */

/**
 * @class Gam.selection.TreeModel
 * @extends Ext.selection.RowModel
 *
 * Adds custom behavior for left/right keyboard navigation for use with a tree.
 * Depends on the view having an expand and collapse method which accepts a
 * record.
 *
 * @private
 */
Ext.define('Gam.selection.AutocompleteModel', {
	extend: 'Ext.selection.Model',
	alias: 'selection.autocompletemodel',

	/**
	 * @private
	 * Number of pixels to scroll to the left/right when pressing
	 * left/right keys.
	 */
	deltaScroll: 5,

	constructor: function()
	{
		this.addEvents(
				/**
				 * @event beforedeselect
				 * Fired before a record is deselected. If any listener returns false, the
				 * deselection is cancelled.
				 * @param {Ext.selection.RowSelectionModel} this
				 * @param {Ext.data.Model} record The deselected record
				 * @param {Number} index The row index deselected
				 */
				'beforedeselect',

				/**
				 * @event beforeselect
				 * Fired before a record is selected. If any listener returns false, the
				 * selection is cancelled.
				 * @param {Ext.selection.RowSelectionModel} this
				 * @param {Ext.data.Model} record The selected record
				 * @param {Number} index The row index selected
				 */
				'beforeselect',

				/**
				 * @event deselect
				 * Fired after a record is deselected
				 * @param {Ext.selection.RowSelectionModel} this
				 * @param {Ext.data.Model} record The deselected record
				 * @param {Number} index The row index deselected
				 */
				'deselect',

				/**
				 * @event select
				 * Fired after a record is selected
				 * @param {Ext.selection.RowSelectionModel} this
				 * @param {Ext.data.Model} record The selected record
				 * @param {Number} index The row index selected
				 */
				'select'
		);
		this.callParent(arguments);
	},

	bindComponent: function(view)
	{
		var me = this;

		me.view = view;
		me.bindStore(view.getStore(), true);

		view.on({
			itemmousedown: me.onRowMouseDown,
			scope: me
		});
	},

	// Returns the number of rows currently visible on the screen or
	// false if there were no rows. This assumes that all rows are
	// of the same height and the first view is accurate.
	getRowsVisible: function()
	{
		var rowsVisible = false,
		me = this,
		row = me.view.getNode(0),
		rowHeight, gridViewHeight;

		if(row)
		{
			rowHeight = Ext.fly(row).getHeight();
			gridViewHeight = me.view.el.getHeight();
			rowsVisible = Math.floor(gridViewHeight / rowHeight);
		}

		return rowsVisible;
	},


	highlightLastHighlighted: function()
	{
		this.highlightAt(this.view.highlightedItem || 1);
	},

	highlight: function(record)
	{
		var me = this,
		rowIndex = me.view.store.indexOf(record);

		me.highlightAt(rowIndex);
	},

	/**
	 * Highlights the item at the given index.
	 * @param {Number} index
	 */
	highlightAt: function(index)
	{
		var view = this.view,
		item = view.all.item(index);
		if(item)
		{
			item = item.dom;
			view.highlightItem(item);
			view.scrollRowIntoView(index);
		}
	},

	/**
	 * Triggers selection of the currently highlighted item according to the behavior of
	 * the configured SelectionModel.
	 */
	selectHighlighted: function(e)
	{
		var me = this,
		view = me.view,
		highlighted = view.highlightedItem;
		if(highlighted)
		{
			var record = view.getRecord(highlighted);
			if(record.raw.unselectable === true)
			{ return; }

			me.selectWithEvent(record, e);
		}
	},

	onEnter: function(e)
	{
		this.selectHighlighted(e);
	},

	// go to last visible record in grid.
	onKeyEnd: function(e, t)
	{
		if(!e.altKey)
		{
			return true;
		}

		var me = this,
		last = me.store.getCount() - 1;

		if(last > -1)
		{
			me.highlightAt(last);
		}
	},

	// go to first visible record in grid.
	onKeyHome: function(e, t)
	{
		if(!e.altKey)
		{
			return true;
		}

		var me = this;

		if(me.store.getCount() > 0)
		{
			me.highlightAt(0);
		}
	},

	// Go one page up from the lastFocused record in the grid.
	onKeyPageUp: function(e, t)
	{
		var me = this,
		view = me.view,
		oldItem = view.highlightedItem,
		oldItemIdx = oldItem ? view.indexOf(oldItem) : -1,
				rowsVisible = me.getRowsVisible(),
				prevIdx;

		if(rowsVisible)
		{
			prevIdx = oldItemIdx - rowsVisible;
			if(prevIdx < 0)
			{
				prevIdx = 0;
			}
			me.highlightAt(prevIdx);
		}
	},

	// Go one page down from the lastFocused record in the grid.
	onKeyPageDown: function(e, t)
	{
		var me = this,
		view = me.view,
		oldItem = view.highlightedItem,
		oldItemIdx = oldItem ? view.indexOf(oldItem) : -1,
				rowsVisible = me.getRowsVisible(),
				nextIdx;

		if(rowsVisible)
		{
			nextIdx = oldItemIdx + rowsVisible;
			if(nextIdx >= me.store.getCount())
			{
				nextIdx = me.store.getCount() - 1;
			}
			me.highlightAt(nextIdx);
		}
	},

	// Navigate one record up. This could be a selection or
	// could be simply focusing a record for discontiguous
	// selection. Provides bounds checking.
	onKeyUp: function(e, t)
	{
		var me = this,
		view = me.view,
		allItems = view.all,
		oldItem = view.highlightedItem,
		oldItemIdx = oldItem ? view.indexOf(oldItem) : -1,
				newItemIdx = oldItemIdx > 0 ? oldItemIdx - 1 : allItems.getCount() - 1; //wraps around
				me.highlightAt(newItemIdx);
	},

	// Navigate one record down. This could be a selection or
	// could be simply focusing a record for discontiguous
	// selection. Provides bounds checking.
	onKeyDown: function(e, t)
	{
		var me = this,
		view = me.view,
		allItems = view.all,
		oldItem = view.highlightedItem,
		oldItemIdx = oldItem ? view.indexOf(oldItem) : -1,
				newItemIdx = oldItemIdx < allItems.getCount() - 1 ? oldItemIdx + 1 : 0; //wraps around
		me.highlightAt(newItemIdx);
	},

	// Select the record with the event included so that
	// we can take into account ctrlKey, shiftKey, etc
	onRowMouseDown: function(view, record, item, index, e)
	{
		if(record.raw.unselectable === true)
		{ return; }

		this.selectWithEvent(record, e);
	},

	// Allow the GridView to update the UI by
	// adding/removing a CSS class from the row.
	onSelectChange: function(record, isSelected, suppressEvent, commitFn)
	{
		var me = this,
		store = me.store,
		rowIdx = store.indexOf(record),
		eventName = isSelected ? 'select' : 'deselect',
				i = 0;

		if((suppressEvent || me.fireEvent('before' + eventName, me, record, rowIdx)) !== false &&
				commitFn() !== false)
		{

			if(isSelected)
			{
				me.view.onRowSelect(rowIdx, suppressEvent);
			}
			else
			{
				me.view.onRowDeselect(rowIdx, suppressEvent);
			}

			if(!suppressEvent)
			{
				me.fireEvent(eventName, me, record, rowIdx);
			}
		}
	},

	// Provide indication of what row was last focused via
	// the gridview.
	onLastFocusChanged: function(oldFocused, newFocused, suppressFocus)
	{
		var me = this,
		store = this.store,
		rowIdx,
		i = 0;

		if(oldFocused)
		{
			rowIdx = store.indexOf(oldFocused);
			if(rowIdx != -1)
			{
				me.view.onRowFocus(rowIdx, false);
			}
		}

		if(newFocused)
		{
			rowIdx = store.indexOf(newFocused);
			if(rowIdx != -1)
			{
				me.view.onRowFocus(rowIdx, true, suppressFocus);
			}
		}
	},

	// typically selection models prune records from the selection
	// model when they are removed, because the TreeView constantly
	// adds/removes records as they are expanded/collapsed
	pruneRemoved: false,

	onKeyLeft: function(e, t)
	{
		if(!e.altKey)
		{
			return true;
		}

		var me = this,
		view = me.view,
		oldItem = view.highlightedItem,
		oldItemIdx = oldItem ? view.indexOf(oldItem) : -1,
				highlighted = me.store.getAt(oldItemIdx);

		if(highlighted)
		{
			// tree node is already expanded, go down instead
			// this handles both the case where we navigate to firstChild and if
			// there are no children to the nextSibling
			if(highlighted.isExpanded())
			{
				me.onKeyDown(e, t);
				// if its not a leaf node, expand it
			} else if(!highlighted.isLeaf())
			{
				me.view.expand(highlighted);
				me.highlightAt(oldItemIdx);
			}
		}
	},

	onKeyRight: function(e, t)
	{
		if(!e.altKey)
		{
			return true;
		}

		var me = this,
		view = me.view,
		oldItem = view.highlightedItem,
		oldItemIdx = oldItem ? view.indexOf(oldItem) : -1,
				highlighted = me.store.getAt(oldItemIdx),
				parentNode;

		if(highlighted)
		{
			parentNode = highlighted.parentNode;
			// if highlighted node is already expanded, collapse it
			if(highlighted.isExpanded())
			{
				this.view.collapse(highlighted);
				me.highlightAt(oldItemIdx);
				// has a parentNode and its not root
				// TODO: this needs to cover the case where the root isVisible
			} else if(parentNode && !parentNode.isRoot())
			{
				var parentNodeIdx = me.store.indexOf(parentNode);
				me.highlightAt(parentNodeIdx);
			}
		}
	},

	onKeyPress: function(e, t)
	{
		var selected, checked;

		if(e.altKey && e.getKey() === e.SPACE)
		{
			e.stopEvent();
			selected = this.getLastSelected();
			if(selected && selected.isLeaf())
			{
				checked = selected.get('checked');
				if(Ext.isBoolean(checked))
				{
					selected.set('checked', !checked);
				}
			}
		}
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 9/14/11
 * Time: 5:43 PM
 */

/**
 * @class Gam.autocomplete.View
 * @extends Ext.tree.View
 */
Ext.define('Gam.autocomplete.View', {
	extend: 'Ext.tree.View',
	alias: 'widget.autocompleteview',

	loadMask: false,

	/**
	 * Scroll a particular row and bring it into view. Will fire the rowfocus event.
	 * @param {Mixed} rowIdx An HTMLElement template node, index of a template node, the
	 * id of a template node or the record associated with the node.
	 */
	scrollRowIntoView: function(rowIdx)
	{
		var me = this,
		row = me.getNode(rowIdx),
		el = me.el,
		adjustment = 0,
		panel = me.ownerCt,
		rowRegion,
		elRegion,
		record;

		if(row && el)
		{
			elRegion = el.getRegion();
			rowRegion = Ext.fly(row).getRegion();
			// row is above
			if(rowRegion.top < elRegion.top)
			{
				adjustment = rowRegion.top - elRegion.top;
				// row is below
			} else if(rowRegion.bottom > elRegion.bottom)
			{
				adjustment = rowRegion.bottom - elRegion.bottom;
			}
			record = me.getRecord(row);
			rowIdx = me.store.indexOf(record);

			if(adjustment)
			{
				// scroll the grid itself, so that all gridview's update.
				panel.scrollByDeltaY(adjustment);
			}
		}
	},

	onItemClick: function(record, item, index, e)
	{
		e.stopEvent();
		if(e.getTarget(this.expanderSelector, item))
		{
			this.toggle(record);
			return false;
		}

		return this.callParent(arguments);
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/10/11
 * Time: 6:45 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.autocomplete.Column
 * @extends Ext.grid.column.Column
 *
 * Provides indentation and folder structure markup for a Autocomplete taking into account
 * depth and position within the tree hierarchy.
 *
 * @private
 */
Ext.define('Gam.autocomplete.Column', {
	extend: 'Ext.grid.column.Column',
	alias: 'widget.gam.autocompletecolumn',

	initComponent: function()
	{
		var origRenderer = this.renderer || this.defaultRenderer,
		origScope = this.scope || window;

		this.renderer = function(value, metaData, record, rowIdx, colIdx, store, view)
		{
			var buf = [],
			format = Ext.String.format,
			depth = record.getDepth(),
			treePrefix = Ext.baseCSSPrefix + 'tree-',
			elbowPrefix = treePrefix + 'elbow-',
			expanderCls = treePrefix + 'expander',
			imgText = '<img src="{1}" class="{0}" />',
			checkboxText = '<input type="button" role="checkbox" class="{0}" {1} />',
			formattedValue = origRenderer.apply(origScope, arguments),
			href = record.get('href'),
			target = record.get('hrefTarget'),
			cls = record.get('cls'),
			category = record.get('acCat');

			while(record)
			{
				if(!record.isRoot() || (record.isRoot() && view.rootVisible))
				{
					if(record.getDepth() === depth)
					{
						var iconCls = treePrefix + 'icon ' + treePrefix + 'icon';
						if(record.get('icon'))
						{ iconCls += '-inline ' }
						else if(category == 'H')
						{ iconCls += '-his ' }
						else if(category == 'F')
						{ iconCls += '-fav ' }
						else if(category == 'S')
						{ iconCls += '-srch ' }
						iconCls += (record.get('iconCls') || '');
						buf.unshift(format(imgText,
								iconCls,
								record.get('icon') || Ext.BLANK_IMAGE_URL
						));
						if(record.get('checked') !== null)
						{
							buf.unshift(format(
									checkboxText,
									(treePrefix + 'checkbox') + (record.get('checked') ? ' ' + treePrefix + 'checkbox-checked' : ''),
									record.get('checked') ? 'aria-checked="true"' : ''
							));
							if(record.get('checked'))
							{
								metaData.tdCls += (' ' + treePrefix + 'checked');
							}
						}
						if(record.isLast())
						{
							if(record.isExpandable())
							{
								buf.unshift(format(imgText, (elbowPrefix + 'end-plus ' + expanderCls), Ext.BLANK_IMAGE_URL));
							}
							else
							{
								buf.unshift(format(imgText, (elbowPrefix + 'end'), Ext.BLANK_IMAGE_URL));
							}

						}
						else
						{
							if(record.isExpandable())
							{
								buf.unshift(format(imgText, (elbowPrefix + 'plus ' + expanderCls), Ext.BLANK_IMAGE_URL));
							}
							else
							{
								buf.unshift(format(imgText, (treePrefix + 'elbow'), Ext.BLANK_IMAGE_URL));
							}
						}
					}
					else if(record.getDepth() !== 1)
					{
						if(record.isLast() || record.getDepth() === 0)
						{
							buf.unshift(format(imgText, (elbowPrefix + 'empty'), Ext.BLANK_IMAGE_URL));
						}
						else if(record.getDepth() !== 0)
						{
							buf.unshift(format(imgText, (elbowPrefix + 'line'), Ext.BLANK_IMAGE_URL));
						}
					}
				}
				record = record.parentNode;
			}
			if(href)
			{
				buf.push('<a href="', href, '" target="', target, '">', formattedValue, '</a>');
			}
			else
			{
				buf.push(formattedValue);
			}
			if(cls)
			{
				metaData.tdCls += ' ' + cls;
			}
			return buf.join('');
		};

		this.callParent(arguments);
	},

	defaultRenderer: function(value)
	{
		return Gam.Resource.label.autocomplete[value] || value;
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 8/24/11
 * Time: 4:48 PM
 */
/**
 * @class Gam.view.AutocompleteBoundList
 * @extends Ext.tree.Panel
 */
Ext.define('Gam.autocomplete.BoundList', {
	extend: 'Ext.tree.Panel',
	alias: 'widget.gam.autocompleteboundlist',

	requires: [
	           'Gam.autocomplete.View',
	           'Gam.selection.AutocompleteModel',
	           'Gam.autocomplete.Column',
	           'Gam.toolbar.AutocompletePaging'
	           ],

	           selType: 'autocompletemodel',

	           viewType: 'autocompleteview',

	           floating: true,

	           loadMask: true,

	           useArrows: true,

	           /**
	            * @cfg {Number} pageSize
	            * If greater than `0`, a {@link Ext.toolbar.Paging} is displayed at the bottom of the list and store
	            * queries will execute with page {@link Ext.data.Operation#start start} and
	            * {@link Ext.data.Operation#limit limit} parameters. Defaults to `0`.
	            */
	           pageSize: 0,

	           initComponent: function()
	           { 
	        	   var me = this;
	        	   // If the user specifies the headers collection manually then dont inject our own
	        	   if(!me.columns)
	        	   {
	        		   if(me.initialConfig.hideHeaders === undefined)
	        		   {
	        			   me.hideHeaders = true;
	        		   }
	        		   me.columns = [
	        		                 {
	        		                	 xtype: 'gam.autocompletecolumn',
	        		                	 flex: 1,
	        		                	 dataIndex: me.displayField
	        		                 }/*,
				{
					xtype: 'gam.actioncolumn',
					maxVisibleCount: 3,
					width: 50,
					items: [
						{
							getClass: function(value, metaData, record, rowIndex, colIndex, store)
							{
								return record.get('acCat') == 'H' ? 'grid-del-from-his-action-icon' : '';
							}
						},
						{
							getClass: function(value, metaData, record, rowIndex, colIndex, store)
							{
								return record.get('acCat') == 'F' ? 'grid-go-up-action-icon' : '';
							}
						},
						{
							getClass: function(value, metaData, record, rowIndex, colIndex, store)
							{
								return record.get('acCat') == 'F' ? 'grid-go-down-action-icon' : '';
							}
						},
						{
							getClass: function(value, metaData, record, rowIndex, colIndex, store)
							{
								return 'grid-fav-non-fav-action-icon' + (record.get('acCat') == 'H' ? ' ' : '');
							}
						},
						{
							getClass: function(value, metaData, record, rowIndex, colIndex, store)
							{
								return record.get('acCat') == 'F' ? 'grid-fav-action-icon' : '';
							}
						},
						{
							getClass: function(value, metaData, record, rowIndex, colIndex, store)
							{
								return record.get('acCat') == 'S' ? 'grid-non-fav-action-icon' : '';
							}
						}
					]
				}*/
	        		                 ];
	        	   }

	        	   me.addDockItems();

	        	   me.callParent();
	           },

	           addDockItems: function()
	           {
	        	   var me = this;

	        	   me.dockedItems = [];
	        	   /*if(this.selModel.mode == 'SIMPLE')
		{
			me.dockedItems.push(me.createSimpleToolbar());
		}*/
	        	   me.dockedItems.push(me.createPagingToolbar())
	           },

	           createSimpleToolbar: function()
	           {
	        	   return {
	        		   id: 'simpleAutocompleteToolbar',
	        		   xtype: 'toolbar',
	        		   dock: 'bottom',
	        		   items: {
	        			   iconCls: 'ok-btn',
	        			   action: 'ok'
	        		   },
	        		   border: false
	        	   };
	           },

	           createPagingToolbar: function()
	           {
	        	   var config = {
	        			   id: 'pagingAutocompleteToolbar',
	        			   xtype: 'gam.autocompletepagingtoolbar',
	        			   pageSize: this.pageSize,
	        			   dock: 'bottom',
	        			   store: this.store,
	        			   border: false
	        	   };

	        	   if(this.selModel.mode == 'SIMPLE')
	        	   {
	        		   config.items = {
	        				   iconCls: 'ok-btn',
	        				   action: 'ok'
	        		   };
	        	   }
	        	   return config;
	           },

	           onRender: function()
	           {
	        	   var me = this,
	        	   mask = me.loadMask,
	        	   cfg = {
	        			   msg: me.loadingText,
	        			   msgCls: me.loadingCls,
	        			   useMsg: me.loadingUseMsg
	        	   };

	        	   me.callParent(arguments);

	        	   if(mask)
	        	   {
	        		   // either a config object
	        		   if(Ext.isObject(mask))
	        		   {
	        			   cfg = Ext.apply(cfg, mask);
	        		   }
	        		   // Attach the LoadMask to a *Component* so that it can be sensitive to resizing during long loads.
	        		   // If this DataView is floating, then mask this DataView.
	        		   // Otherwise, mask its owning Container (or this, if there *is* no owning Container).
	        		   // LoadMask captures the element upon render.
	        		   me.loadMask = Ext.create('Ext.LoadMask', me, cfg);
	        		   me.loadMask.on({
	        			   scope: me,
	        			   beforeshow: me.onMaskBeforeShow,
	        			   hide: me.onMaskHide
	        		   });

	        		   this.bindLoadMaskStore();
	        	   }
	           },

	           onMaskBeforeShow: function()
	           {
	        	   var me = this,
	        	   loadingHeight = me.loadingHeight;

	        	   if(me._calledOnMaskBeforeShow)
	        	   {
	        		   delete me._calledOnMaskBeforeShow;
	        		   return;
	        	   }

	        	   me._calledOnMaskBeforeShow = true;

	        	   me.getSelectionModel().deselectAll();
	        	   if(loadingHeight && loadingHeight > me.getHeight())
	        	   {
	        		   me.isLoadingHeight = true;
	        		   me.bufferHeight = me.height;
	        		   me.setHeight(loadingHeight);
	        	   }
	           },

	           onMaskHide: function()
	           {
	        	   var me = this;

	        	   if(!me.destroying && me.loadingHeight && me.isLoadingHeight)
	        	   {
	        		   if(me.bufferHeight)
	        		   {
	        			   me.setHeight(me.bufferHeight);
	        		   }
	        		   else
	        		   {
	        			   delete me.height;
	        			   me.updateLayout();
	        		   }
	        		   delete me.isLoadingHeight;
	        	   }
	           },

	           /**
	            * Changes the data store bound to this view and refreshes it.
	            * @param {Store} store The store to bind to this view
	            */
	           bindStore: function(store)
	           {
	        	   var me = this;

	        	   if(me.store)
	        	   {
	        		   if(!store)
	        		   {
	        			   if(me.loadMask)
	        			   {
	        				   me.loadMask.bindStore(null);
	        			   }
	        		   }
	        	   }

	        	   me.callParent([store]);

	        	   if(store)
	        	   {
	        		   this.bindLoadMaskStore();
	        	   }
	           },

	           bindLoadMaskStore: function()
	           {
	        	   var me = this;

	        	   if(me.loadMask)
	        	   {
	        		   me.loadMask.bindStore(me.store);
	        	   }
	           }
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 9/13/11
 * Time: 12:04 PM
 */

/**
 * @class Ext.view.BoundListKeyNav
 * @extends Ext.util.KeyNav
 * A specialized {@link Ext.util.KeyNav} implementation for navigating a {@link Ext.view.BoundList} using
 * the keyboard. The up, down, pageup, pagedown, home, and end keys move the active highlight
 * through the list. The enter key invokes the selection model's select action using the highlighted item.
 */
Ext.define('Gam.view.AutocompleteBoundListKeyNav', {
	extend: 'Ext.util.KeyNav',
	requires: 'Gam.autocomplete.BoundList',

	/**
	 * @cfg {Ext.view.BoundList} boundList
	 * @required
	 * The {@link Ext.view.BoundList} instance for which key navigation will be managed. This is required.
	 */
	constructor: function(el, config)
	{
		var me = this;
		me.boundList = config.boundList;
		var boundListSelModel = this.boundList.getSelectionModel();
		me.callParent([
		               el,
		               Ext.applyIf(config, {
		            	   up: boundListSelModel.onKeyUp,
		            	   down: boundListSelModel.onKeyDown,
		            	   right: boundListSelModel.onKeyRight,
		            	   left: boundListSelModel.onKeyLeft,
		            	   pageDown: boundListSelModel.onKeyPageDown,
		            	   pageUp: boundListSelModel.onKeyPageUp,
		            	   home: boundListSelModel.onKeyHome,
		            	   end: boundListSelModel.onKeyEnd,
		            	   enter: boundListSelModel.onEnter,
		            	   scope: boundListSelModel
		               })
		               ]);

		el.on(Ext.EventManager.getKeyEvent(), boundListSelModel.onKeyPress, boundListSelModel);
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Action
 * @extends Ext.Action
 */
Ext.define('Gam.action.Action', {
	extend: 'Ext.Action',
	alias: 'action.gam.action',

	onClassExtended: function(cls, data, hooks)
	{
		var onBeforeClassCreated = hooks.onBeforeCreated;

		hooks.onBeforeCreated = function(cls, data)
		{
			var me = this;

			if(data.initialConfig)
			{ Ext.applyIf(data.initialConfig, cls.prototype.superclass.initialConfig); }

			onBeforeClassCreated.call(me, cls, data, hooks);
		}
	},

	constructor: function(config)
	{
		var me = this;

		config = config || {};
		this.initialConfig = Ext.merge({}, this.initialConfig);
		me.applyToInitialConfig(config);
		me.itemId = config.itemId = (config.itemId || config.id || Ext.id());
		me.items = [];
	},

	applyToInitialConfig: function(config)
	{
		Ext.apply(this.initialConfig, config);
	},

	setHidden: function(v)
	{
		if(Gam.security.Acl.isChangeable(this.items[0].stateId, Gam.security.Acl.attributeNames.HIDDEN))
		{
			this.callParent([v]);
		}
	},

	setDisabled: function(v)
	{
		if(Gam.security.Acl.isChangeable(this.items[0].stateId, Gam.security.Acl.attributeNames.DISABLED))
		{
			this.callParent([v]);
		}
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Cancel
 * @extends Ext.Action
 */
Ext.define('Gam.action.Cancel', {
	extend: 'Ext.Action',
	alias: 'action.gam.cancel',

	initialConfig: {
		text: Gam.Resource.label.cancel,

		iconCls: 'cancel-btn',

		action: 'cancel'
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Add
 * @extends Ext.Action
 */
Ext.define('Gam.action.Edit', {
	extend: 'Ext.Action',
	alias: 'action.gam.edit',

	initialConfig: {
		text: Gam.Resource.label.edit,

		iconCls: 'edit-btn',

		action: 'edit'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Action
 * @extends Ext.Action
 */
Ext.define('Gam.action.GridAction', {
	extend: 'Gam.action.Action',
	alias: 'action.gam.grid',

	/**
	 * Should be implemented
	 * @param sm
	 * @param selections
	 */
	onSelectionChange: function(sm, selections) { /* Empty */ }
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/29/12
 * Time: 6:27 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.EnabledOnSelectionAction
 * @extends Gam.action.GridAction
 */
Ext.define('Gam.action.EnabledOnSelectionAction', {
	extend: 'Gam.action.GridAction',
	alias: 'widget.gam.enableonselection',

	initialConfig: {
		disabled: true
	},

	onSelectionChange: function(sm, selections)
	{
		this.setDisabled(!(selections.length > 0));
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/29/12
 * Time: 6:27 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.EnabledOnSingleSelectionAction
 * @extends Gam.action.GridAction
 */
Ext.define('Gam.action.EnabledOnSingleSelectionAction', {
	extend: 'Gam.action.GridAction',
	alias: 'widget.gam.enableonselection',

	initialConfig: {
		disabled: true
	},

	onSelectionChange: function(sm, selections)
	{
		this.setDisabled(selections.length !== 1);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Add
 * @extends Gam.action.GridAction
 */
Ext.define('Gam.action.Add', {
	extend: 'Gam.action.GridAction',
	alias: 'action.gam.add',

	initialConfig: {
		text: Gam.Resource.label.add,

		iconCls: 'add-btn',

		action: 'add'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 5/8/12
 * Time: 4:36 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.RegisterExtraInfo
 * @extends Gam.action.EnabledOnSelectionAction
 */
Ext.define('Gam.action.RegisterExtraInfo', {
	extend: 'Gam.action.EnabledOnSelectionAction',
	alias: 'action.gam.registerextrainfo',

	initialConfig: {
		text: Gam.Resource.label.registerExtraInfo,

		iconCls: 'add-btn',

		action: 'registerExtraInfo'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Delete
 * @extends Gam.action.EnabledOnSelectionAction
 */
Ext.define('Gam.action.Delete', {
	extend: 'Gam.action.EnabledOnSelectionAction',
	alias: 'action.gam.delete',

	initialConfig: {
		text: Gam.Resource.label.remove,

		iconCls: 'delete-btn',

		action: 'delete'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.Print
 * @extends Gam.action.EnabledOnSelectionAction
 */
Ext.define('Gam.action.Print', {
	extend: 'Gam.action.EnabledOnSelectionAction',
	alias: 'action.gam.print',

	initialConfig: {
		text: Gam.Resource.label.print,

		iconCls: 'print-btn',

		action: 'print'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.PrintAllItems
 * @extends Gam.action.GridAction
 */
Ext.define('Gam.action.PrintAllItems', {
	extend: 'Gam.action.GridAction',
	alias: 'action.gam.printallitems',

	initialConfig: {
		text: Gam.Resource.label.printAllItems,

		iconCls: 'print-btn',

		action: 'printAllItems'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/21/12
 * Time: 1:04 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.action.PrintSelectedItems
 * @extends Gam.action.EnabledOnSelectionAction
 */
Ext.define('Gam.action.PrintSelectedItems', {
	extend: 'Gam.action.EnabledOnSelectionAction',
	alias: 'action.gam.printselecteditems',

	initialConfig: {
		text: Gam.Resource.label.printSelectedItems,

		iconCls: 'print-btn',

		action: 'printSelectedItems'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 4/11/12
 * Time: 2:39 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.menu.Item
 * @extends Gam.menu.Item
 */
Ext.define('Gam.menu.Item', {
	extend: 'Ext.menu.Item',
	alias: 'widget.gam.menuitem',

	addStateEvents: Ext.emptyFn,

	initComponent: function()
	{
		var me = this;

		if(!me.stateId && me.baseStateId && (me.stateful || me.stateManagement !== false))
		{
			me.stateId = me.baseStateId + Ext.String.capitalize(me.action);
		}
		delete me.baseStateId;

		me.callParent();
	},

	show: function()
	{
		if(Gam.security.Acl.isChangeable(this.stateId, Gam.security.Acl.attributeNames.HIDDEN))
		{
			this.callParent(arguments);
		}
	},

	enable: function()
	{
		if(Gam.security.Acl.isChangeable(this.stateId, Gam.security.Acl.attributeNames.DISABLED))
		{
			this.callParent(arguments);
		}
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 2/2/12
 * Time: 4:41 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.menu.Menu
 * @extends Ext.menu.Menu
 */
Ext.define('Gam.menu.Menu', {
	extend: 'Ext.menu.Menu',
	alias: 'widget.gam.menu',

	lookupItemFromObject: function(cmp)
	{
		var me = this,
		prefix = Ext.baseCSSPrefix,
		cls;

		if(!cmp.isComponent)
		{
			if(!cmp.xtype)
			{
				cmp = Ext.create((Ext.isBoolean(cmp.checked) ? 'Ext.menu.CheckItem' : 'Gam.menu.Item'), cmp);
			}
			else
			{
				cmp = Ext.ComponentManager.create(cmp, cmp.xtype);
			}
		}

		if(cmp.isMenuItem)
		{
			cmp.parentMenu = me;
		}

		if(!cmp.isMenuItem && !cmp.dock)
		{
			cls = [prefix + 'menu-item', prefix + 'menu-item-cmp'];

			if(!me.plain && (cmp.indent === true || cmp.iconCls === 'no-icon'))
			{
				cls.push(prefix + 'menu-item-indent');
			}

			if(cmp.rendered)
			{
				cmp.el.addCls(cls);
			}
			else
			{
				cmp.cls = (cmp.cls ? cmp.cls : '') + ' ' + cls.join(' ');
			}
			cmp.isMenuItem = true;
		}
		return cmp;
	},

	lookupItemFromString: function(cmp)
	{
		return (cmp == 'separator' || cmp == '-') ?
				new Ext.menu.Separator()
		: new Gam.menu.Item({
			canActivate: false,
			hideOnClick: false,
			plain: true,
			text: cmp
		});
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Sina
 * Date: 9/14/11
 * Time: 3:22 PM
 */

/**
 *
 * Filtering adds a row of fields to the bottom of the grid header for filtering the data stored in the grid.
 * The filtering is supposed to be done on the server side.
 * @author Sina Golesorkhi rivised by S.M.Tayyeb
 * @class Gam.grid.plugin.Filtering
 * @extend Ext.AbstractPlugin
 */
Ext.define('Gam.grid.plugin.Filtering', {
	extend: 'Ext.AbstractPlugin',
	alias: 'plugin.filtering',

	mixins: {
		observable: 'Ext.util.Observable'
	},

	// private
	defaultFieldXType: 'textfield',

	// header, form
	filterStyle: '',

	constructor: function(config)
	{
		var me = this;
		me.addEvents(
				/**
				 * @event beforefilter
				 * Fires before filtering is triggered. Return false from event handler to stop the filtering.
				 *
				 * @param {Gam.grid.plugin.FilterHeader} filterbar
				 */
				'beforefilter',

				/**
				 * @event filter
				 * Fires after a filtering. Usage example:
				 *
				 *	 grid.on('filter', function(filterbar, e) {
				 *		 // commit the changes right after filtering finished
				 *		 e.record.commit();
				 *	 });
				 *
				 * @param {Ext.grid.plugin.Editing} filterbar
				 */
				'filter',

				/**
				 * @event validatefilter
				 * Fires after filtering, but before the value is set in the record. Return false from event handler to
				 * cancel the change.
				 *
				 * Usage example showing how to remove the red triangle (dirty record indicator) from some records (not all). By
				 * observing the grid's validatefilter event, it can be cancelled if the filter occurs on a targeted row (for example)
				 * and then setting the field's new value in the Record directly:
				 *
				 *	 grid.on('validatefilter', function(filterbar, e) {
				 *	   var myTargetRow = 6;
				 *
				 *	   if (e.rowIdx == myTargetRow) {
				 *		 e.cancel = true;
				 *		 e.record.data[e.field] = e.value;
				 *	   }
				 *	 });
				 *
				 * @param {Gam.grid.plugin.FilterHeader} filterbar
				 */
				'validatefilter',
				/**
				 * @event clearfilter
				 * Fires when the user started filtering but then cancelled the filter.
				 * @param {Gam.grid.plugin.FilterHeader} filterbar
				 */
				'canceledit'

		);
		this.mixins.observable.constructor.call(this);

		this.callParent(arguments);
	},

	/**
	 *
	 * @param grid  The grid owning this plugin
	 */
	init: function(grid)
	{
		var me = this;

		me.grid = grid;
		me.view = grid.view;
		me.initEvents();
		grid.relayEvents(me, [
		                      /**
		                       * @event beforeedit
		                       * Forwarded event from Ext.grid.plugin.Editing.
		                       * @member Ext.panel.Table
		                       * @inheritdoc Ext.grid.plugin.Editing#beforeedit
		                       */
		                      'beforefilter',
		                      /**
		                       * @event edit
		                       * Forwarded event from Ext.grid.plugin.Editing.
		                       * @member Ext.panel.Table
		                       * @inheritdoc Ext.grid.plugin.Editing#edit
		                       */
		                      'filter',
		                      /**
		                       * @event validateedit
		                       * Forwarded event from Ext.grid.plugin.Editing.
		                       * @member Ext.panel.Table
		                       * @inheritdoc Ext.grid.plugin.Editing#validateedit
		                       */
		                      'validatefilter',
		                      /**
		                       * @event canceledit
		                       * Forwarded event from Ext.grid.plugin.Editing.
		                       * @member Ext.panel.Table
		                       * @inheritdoc Ext.grid.plugin.Editing#canceledit
		                       */
		                      'clearfilter'
		                      ]);

		grid.isFilterable = true;
		grid.filteringPlugin = grid.view.filteringPlugin = me;
	},

	/**
	 * @private
	 * AbstractComponent calls destroy on all its plugins at destroy time.
	 */
	destroy: function()
	{
		var me = this;

		// Clear all listeners from all our events, clear all managed listeners we added to other Observables
		me.clearListeners();

		delete me.grid.view.filteringPlugin;
		delete me.grid.filteringPlugin;
		delete me.grid;
		delete me.view;
	},

	// private
	getFilterStyle: function()
	{
		return this.filterStyle;
	},

	// private
	initEvents: function()
	{
		var me = this;
		me.initEditTriggers();
	},

	//@abstract
	initEditTriggers: Ext.emptyFn,

	onEscKey: function(e)
	{
		this.clearFilter();
	},

	/**
	 * @private
	 * @template
	 * Template method called before editing begins.
	 * @return {Boolean} Return false to cancel the editing process
	 */
	beforeFilter: Ext.emptyFn,

	/**
	 * Reads the input values from all fields of the FilterHeader
	 */
	filter: function()
	{
		var me = this;

		if(me.beforeFilter() === false || me.fireEvent('beforefilter', me) === false)
		{
			return false;
		}

		me.filtering = true;
	},

	/**
	 * Reads the input values from all fields of the FilterHeader
	 */
	clearFilter: function()
	{
		var me = this;

		me.filtering = false;
		me.fireEvent('clearfilter', me);
	},

	// @abstract
	validateFilter: function()
	{
		var me = this,
		context = me.context;

		return me.fireEvent('validatefilter', me, context) !== false && !context.cancel;
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/11/12
 * Time: 11:20 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.FilterLayout
 * @extends Ext.layout.container.HBox
 */
Ext.define('Gam.grid.FilterLayout', {
	extend: 'Ext.layout.container.HBox',
	alias: 'layout.gridfilter',
	type: 'gridfilter',

	reserveOffset: false,

	shrinkToFit: false,

	clearInnerCtOnLayout: true,


	beginLayout: function(ownerContext)
	{
		var me = this,
		owner = me.owner,
		grid = owner.up('[scrollerOwner]'),
		view = grid.view,
		i = 0,
		items = me.getVisibleItems(),
		len = items.length,
		item;

		me.callParent(arguments);


		for(; i < len; i++)
		{
			item = items[i];
			item.el.setStyle({
				height: 'auto'
			});
		}

		if(!me.owner.isHeader && Ext.getScrollbarSize().width && !grid.collapsed && view && view.rendered && (ownerContext.viewTable = view.el.child('table', true)))
		{
			ownerContext.viewContext = ownerContext.context.getCmp(view);
		}
	},

	roundFlex: function(width)
	{
		return Math.floor(width);
	},


	getContainerSize: function(ownerContext)
	{
		var me = this,
		result = me.callParent(arguments),
		viewContext = ownerContext.viewContext,
		viewHeight;


		if(viewContext && !viewContext.heightModel.shrinkWrap)
		{
			viewHeight = viewContext.getProp('height');
			if(isNaN(viewHeight))
			{
				me.done = false;
			} else if(ownerContext.state.tableHeight > viewHeight)
			{
				result.width -= Ext.getScrollbarSize().width;
				ownerContext.state.parallelDone = false;
				viewContext.invalidate();
			}
		}


		return result;
	},

	calculate: function(ownerContext)
	{
		var me = this,
		viewContext = ownerContext.viewContext;


		if(viewContext && !ownerContext.state.tableHeight)
		{
			ownerContext.state.tableHeight = ownerContext.viewTable.offsetHeight;
		}
		me.callParent(arguments);
	},

	completeLayout: function(ownerContext)
	{
		var me = this,
		owner = me.owner,
		state = ownerContext.state,
		needsInvalidate = false,
		calculated = me.sizeModels.calculated,
		childItems, len, i, childContext, item;

		me.callParent(arguments);


		if(!state.flexesCalculated && owner.forceFit && !owner.isHeader)
		{
			childItems = ownerContext.childItems;
			len = childItems.length;

			for(i = 0; i < len; i++)
			{
				childContext = childItems[i];
				item = childContext.target;


				if(item.width)
				{
					item.flex = ownerContext.childItems[i].flex = item.width;
					delete item.width;
					childContext.widthModel = calculated;
					needsInvalidate = true;
				}
			}


			if(needsInvalidate)
			{
				me.cacheFlexes(ownerContext);
				ownerContext.invalidate({
					state: {
						flexesCalculated: true
					}
				});
			}
		}
	},


	publishInnerCtSize: function(ownerContext)
	{
		var me = this,
		plan = ownerContext.state.boxPlan,
		size = plan.targetSize;


		if(!me.owner.isHeader)
		{
			size.width = ownerContext.getProp('contentWidth') + Ext.getScrollbarSize().width;
		}

		return me.callParent(arguments);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/8/12
 * Time: 6:55 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.Filterbar
 * @extends Ext.container.Container
 */
Ext.define('Gam.grid.Filterbar', {
	extend: 'Ext.container.Container',
	requires: [
	           'Gam.grid.FilterLayout',
	           'Ext.util.KeyNav'
	           ],

	           liveSearch: false,

	           searchBtnText: 'جستجو',

	           cancelBtnText: 'انصراف',

	           lastScrollRight: 0,

	           fieldDefaults: {
	        	   msgTarget: 'side'
	           },

	           border: true,

	           dock: 'top',

	           weight: 101,

	           baseCls: Ext.baseCSSPrefix + 'grid-filterbar-ct',

	           initComponent: function()
	           {
	        	   var me = this;

	        	   me.cls = Ext.baseCSSPrefix + 'grid-filterbar';

	        	   me.layout = {
	        			   type: 'gridfilter',
	        			   align: 'middle'
	        	   };

	        	   me.columns = new Ext.util.HashMap();
	        	   me.columns.getKey = function(columnHeader)
	        	   {
	        		   var f;
	        		   if(columnHeader.getFilter)
	        		   {
	        			   f = columnHeader.getFilter();
	        			   if(f)
	        			   {
	        				   return f.id;
	        			   }
	        		   }
	        		   return columnHeader.id;
	        	   };
	        	   me.mon(me.columns, {
	        		   add: me.onFilterFieldAdd,
	        		   remove: me.onFilterFieldRemove,
	        		   replace: me.onFilterFieldReplace,
	        		   scope: me
	        	   });

	        	   me.callParent(arguments);

	        	   if(me.fields)
	        	   {
	        		   me.setFilterField(me.fields);
	        		   delete me.fields;
	        	   }
	        	   me.hasFields = true;
	        	   me.initEvents();
	           },

	           // private
	           initEvents: function()
	           {
	        	   var me = this;
	        	   me.filteringPlugin.view.on('afterrender', me.onGridViewAfterRender, me, {single: true});
	           },

	           onFieldChange: function()
	           {
	        	   var me = this;

	        	   if(!me.liveSearch)
	        	   {
	        		   return;
	        	   }
	           },

	           afterRender: function()
	           {
	        	   var me = this,
	        	   plugin = me.filteringPlugin;

	        	   me.callParent(arguments);

	        	   me.el.swallowEvent([
	        	                       'keypress',
	        	                       'keydown'
	        	                       ]);


	        	   me.keyNav = new Ext.util.KeyNav(me.el, {
	        		   enter: plugin.filter,
	        		   esc: plugin.onEscKey,
	        		   scope: plugin
	        	   });
	           },

	           onGridViewAfterRender: function()
	           {
	        	   var me = this,
	        	   plugin = me.filteringPlugin;

	        	   me.mon(plugin.view.getEl(), 'scroll', me.onCtScroll, me, { buffer: 100 });
	        	   me.mon(plugin.view, 'refresh', me.onCtRefresh, me);
	           },

	           onCtScroll: function(e, target)
	           {
	        	   var me = this,
	        	   scrollRight = Ext.fly(target).getScrollRight();

	        	   if(scrollRight !== me.lastScrollRight)
	        	   {
	        		   me.lastScrollRight = scrollRight;
	        		   //me.reposition();
	        	   }
	           },

	           onCtRefresh: function(){
	        	   //resets headerCt and filterbarCt to right = 0
	        	   this.filteringPlugin.filterbar.el.setScrollRight( -this.lastScrollRight);
	        	   this.filteringPlugin.grid.headerCt.el.setScrollRight( -this.lastScrollRight);
	           },

	           onColumnAdd: function(column)
	           {
	        	   this.setFilterField(column);
	           },

	           onColumnRemove: function(column)
	           {
	        	   this.columns.remove(column);
	           },

	           onColumnResize: function(column, width)
	           {
	        	   column.getFilter().setWidth(width - 2);
	        	   /*if(this.isVisible())
		 {
		 this.reposition();
		 }*/
	           },

	           onColumnHide: function(column)
	           {
	        	   column.getFilter().hide();
	        	   /*if(this.isVisible())
		 {
		 this.reposition();
		 }*/
	           },

	           onColumnShow: function(column)
	           {
	        	   try
	        	   {
	        		   var filterField = column.getFilter();
	        		   filterField.setWidth(column.getWidth() - 2).show();
	        		   /*if(this.isVisible())
			 {
			 this.reposition();
			 }*/
	        	   } catch(e)
	        	   { }
	           },

	           onColumnMove: function(column, fromIdx, toIdx)
	           {
	        	   var filterField = column.getFilter(),
	        	   index = toIdx > fromIdx ? toIdx - 1 : toIdx;
	        	   if(this.items.indexOf(filterField) != index)
	        	   {
	        		   this.move(fromIdx, index);
	        	   }
	           },

	           onFilterFieldAdd: function(map, fieldId, column)
	           {
	        	   var me = this,
	        	   colIdx = me.filteringPlugin.grid.headerCt.getHeaderIndex(column),
	        	   filterField = column.getFilter({ xtype: 'displayfield' });

	        	   me.insert(colIdx, filterField);
	           },

	           onFilterFieldRemove: function(map, fieldId, column)
	           {
	        	   var me = this,
	        	   filterField = column.getFilter(),
	        	   fieldEl = filterField.el;
	        	   me.remove(filterField, false);
	        	   if(fieldEl)
	        	   {
	        		   fieldEl.remove();
	        	   }
	           },

	           onFilterFieldReplace: function(map, fieldId, column, oldColumn)
	           {
	        	   var me = this;
	        	   me.onFieldRemove(map, fieldId, oldColumn);
	           },

	           clearFields: function()
	           {
	        	   var me = this,
	        	   map = me.columns;
	        	   map.each(function(fieldId)
	        			   {
	        		   map.removeAtKey(fieldId);
	        			   });
	           },

	           getFloatingButtons: function()
	           {
	        	   var me = this,
	        	   cssPrefix = Ext.baseCSSPrefix,
	        	   btnsCss = cssPrefix + 'grid-filterbar-buttons',
	        	   plugin = me.filteringPlugin,
	        	   btns;

	        	   if(!me.floatingButtons)
	        	   {
	        		   btns = me.floatingButtons = new Ext.Container({
	        			   renderTpl: [
	        			               '<div class="{baseCls}-ml"></div>',
	        			               '<div class="{baseCls}-mr"></div>',
	        			               '<div class="{baseCls}-bl"></div>',
	        			               '<div class="{baseCls}-br"></div>',
	        			               '<div class="{baseCls}-bc"></div>',
	        			               '{%this.renderContainer(out,values)%}'
	        			               ],
	        			               width: 200,
	        			               renderTo: me.el,
	        			               baseCls: btnsCss,
	        			               layout: {
	        			            	   type: 'hbox',
	        			            	   align: 'middle'
	        			               },
	        			               defaults: {
	        			            	   flex: 1,
	        			            	   margins: '0 1 0 1'
	        			               },
	        			               items: [
	        			                       {
	        			                    	   itemId: 'update',
	        			                    	   xtype: 'button',
	        			                    	   handler: plugin.doSearch,
	        			                    	   scope: plugin,
	        			                    	   text: me.saveBtnText,
	        			                    	   disabled: !me.isValid,
	        			                    	   minWidth: Ext.panel.Panel.prototype.minButtonWidth
	        			                       },
	        			                       {
	        			                    	   xtype: 'button',
	        			                    	   handler: plugin.clearSearch,
	        			                    	   scope: plugin,
	        			                    	   text: me.cancelBtnText,
	        			                    	   minWidth: Ext.panel.Panel.prototype.minButtonWidth
	        			                       }
	        			                       ]
	        		   });

	        		   // Prevent from bubbling click events to the grid view
	        		   me.mon(btns.el, {
	        			   // BrowserBug: Opera 11.01
	        			   //   causes the view to scroll when a button is focused from mousedown
	        			   mousedown: Ext.emptyFn,
	        			   click: Ext.emptyFn,
	        			   stopEvent: true
	        		   });
	        	   }
	        	   return me.floatingButtons;
	           },

	           reposition: function(animateConfig)
	           {
	        	   var me = this,
	        	   btns = me.getFloatingButtons(),
	        	   btnEl = btns.el,
	        	   grid = me.filteringPlugin.grid,
	        	   viewEl = grid.view.el,

	        	   // always get data from ColumnModel as its what drives
	        	   // the GridView's sizing
	        	   mainBodyWidth = grid.headerCt.getFullWidth(),
	        	   scrollerWidth = grid.getWidth(),

	        	   // use the minimum as the columns may not fill up the entire grid
	        	   // width
	        	   width = Math.min(mainBodyWidth, scrollerWidth),
	        	   scrollRight = grid.view.el.getScrollRight(),
	        	   btnWidth = btns.getWidth(),
	        	   right = (width - btnWidth) / 2 + scrollRight,
	        	   invalidateScroller = function()
	        	   {
	        		   btnEl.scrollIntoView(viewEl, false);
	        		   if(animateConfig && animateConfig.callback)
	        		   {
	        			   animateConfig.callback.call(animateConfig.scope || me);
	        		   }
	        	   };

	        	   invalidateScroller();
	        	   btnEl.setRight(right);
	           },

	           getFilter: function(fieldInfo)
	           {
	        	   var me = this;

	        	   if(Ext.isNumber(fieldInfo))
	        	   {
	        		   // Query only form fields. This just future-proofs us in case we add
	        		   // other components to Filterbar later on.  Don't want to mess with
	        		   // indices.
	        		   return me.query('>[isFormField]')[fieldInfo];
	        	   }
	        	   else if(fieldInfo instanceof Ext.grid.column.Column)
	        	   {
	        		   return fieldInfo.getFilter();
	        	   }
	           },

	           removeField: function(filterField)
	           {
	        	   var me = this;

	        	   // Incase we pass a column instead, which is fine
	        	   filterField = me.getFilter(filterField);
	        	   me.mun(filterField, 'validitychange', me.onValidityChange, me);

	        	   // Remove filterField/column from our mapping, which will fire the event to
	        	   // remove the filterField from our container
	        	   me.columns.removeAtKey(filterField.id);
	        	   Ext.destroy(filterField);
	           },

	           setFilterField: function(column)
	           {
	        	   var me = this,
	        	   field;

	        	   if(Ext.isArray(column))
	        	   {
	        		   Ext.Array.forEach(column, me.setFilterField, me);
	        		   return;
	        	   }

	        	   // Get a default display field if necessary
	        	   field = column.getFilter(null, {
	        		   xtype: 'displayfield',
	        		   // Override Field's implementation so that the default display fields will not return values. This is done because
	        		   // the display field will pick up column renderers from the grid.
	        		   getModelData: function()
	        		   {
	        			   return null;
	        		   }
	        	   });
	        	   field.margins = '0 2 0 0';
	        	   if(!field.fieldLabel)
	        	   { field.hideLabel = true; }
	        	   if(!field.is('displayfield'))
	        	   { me.mon(field, 'change', me.onFieldChange, me);}

	        	   // Maintain mapping of fields-to-columns
	        	   // This will fire events that maintain our container items

	        	   me.columns.add(field.id, column);
	        	   if(column.hidden)
	        	   {
	        		   me.onColumnHide(column);
	        	   } else if(me.hasFields)
	        	   {
	        		   // Setting after initial render
	        		   me.onColumnShow(column);
	        	   }
	           },

	           /**
	            * Retrieves the fields in the form as a set of key/value pairs, using their
	            * {@link Ext.form.field.Field#getSubmitData getSubmitData()} method to collect the values.
	            * If multiple fields return values under the same name those values will be combined into an Array.
	            * This is similar to {@link #getFieldValues} except that this method collects only String values for
	            * submission, while getFieldValues collects type-specific data values (e.g. Date objects for date fields.)
	            *
	            * @param {Boolean} [asString=false] If true, will return the key/value collection as a single
	            * URL-encoded param string.
	            * @param {Boolean} [dirtyOnly=false] If true, only fields that are dirty will be included in the result.
	            * @param {Boolean} [includeEmptyText=false]] If true, the configured emptyText of empty fields will be used.
	            * @return {String/Object}
	            */
	           getValues: function(asString, dirtyOnly, includeEmptyText, useDataValues)
	           {
	        	   var values = {};

	        	   this.getFields().each(function(field)
	        			   {
	        		   if(!dirtyOnly || field.isDirty())
	        		   {
	        			   var data = field[useDataValues ? 'getModelData' : 'getSubmitData'](includeEmptyText);
	        			   if(Ext.isObject(data))
	        			   {
	        				   Ext.iterate(data, function(name, val)
	        						   {
	        					   if(includeEmptyText && val === '')
	        					   {
	        						   val = field.emptyText || '';
	        					   }
	        					   if(name in values)
	        					   {
	        						   var bucket = values[name],
	        						   isArray = Ext.isArray;
	        						   if(!isArray(bucket))
	        						   {
	        							   bucket = values[name] = [bucket];
	        						   }
	        						   if(isArray(val))
	        						   {
	        							   values[name] = bucket.concat(val);
	        						   }
	        						   else
	        						   {
	        							   bucket.push(val);
	        						   }
	        					   }
	        					   else
	        					   {
	        						   values[name] = val;
	        					   }
	        						   });
	        			   }
	        		   }
	        			   });

	        	   if(asString)
	        	   {
	        		   values = Ext.Object.toQueryString(values);
	        	   }
	        	   return values;
	           },

	           /**
	            * Return all the {@link Ext.form.field.Field} components in the owner container.
	            * @return {Ext.util.MixedCollection} Collection of the Field objects
	            */
	           getFields: function()
	           {
	        	   var fields = this._fields;
	        	   if(!fields)
	        	   {
	        		   fields = this._fields = new Ext.util.MixedCollection();
	        		   fields.addAll(this.query('[isFormField]'));
	        	   }
	        	   return fields;
	           },

	           /**
	            * Resets all fields in this form.
	            * @return {Gam.grid.Filterbar} this
	            */
	           reset: function()
	           {
	        	   var me = this;
	        	   Ext.suspendLayouts();
	        	   me.getFields().each(function(f)
	        			   {
	        		   f.reset();
	        			   });
	        	   Ext.resumeLayouts(true);
	        	   return me;
	           },

	           /**
	            * Creates the filter query and sends it to the server for remote filtering
	            */
	           filter: function()
	           {
	        	   var me = this,
	        	   store = me.filteringPlugin.grid.store,
	        	   values = me.getValues(),
	        	   filterQuery = [];

	        	   Ext.iterate(values, function(property, value)
	        			   {
	        		   if(value == '')
	        		   { return; }
	        		   filterQuery.push({
	        			   property: property,
	        			   value: value,
	        			   root: 'data'
	        		   });
	        			   });

	        	   store.filters.clear();
	        	   store.filter(filterQuery);
	           },

	           clearFilter: function()
	           {
	        	   var me = this;

	        	   me.reset();
	        	   me.filteringPlugin.grid.store.clearFilter();
	           },

	           beforeFilter: function()
	           {
	        	   return true;
	           },

	           beforeDestroy: function()
	           {
	        	   Ext.destroy(this.floatingButtons);

	        	   this.callParent();
	           }
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/9/12
 * Time: 3:13 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.plugin.HeaderFiltering
 * @extends Gam.grid.plugin.Filtering
 */
Ext.define('Gam.grid.plugin.HeaderFiltering', {
	extend: 'Gam.grid.plugin.Filtering',
	alias: 'plugin.headerfiltering',

	requires: [ 'Gam.grid.Filterbar' ],

	filterStyle: 'header',

	floatingButtons: true,

	init: function(grid)
	{
		var me = this;

		me.callParent(arguments);

		me.mon(grid, 'reconfigure', me.onReconfigure, me);
		me.onReconfigure();

		grid.addDocked(me.getFilterbar());
	},

	/**
	 * Fires after the grid is reconfigured
	 * @private
	 */
	onReconfigure: function()
	{
		this.initFilterFieldAccessors(this.view.getGridColumns());
	},

	/**
	 * @private
	 * AbstractComponent calls destroy on all its plugins at destroy time.
	 */
	destroy: function()
	{
		var me = this,
		grid = me.grid;

		me.removeFilterFieldAccessors(grid.getView().getGridColumns());

		Ext.destroy(me.keyNav);
		Ext.destroy(me.filterbar);
		delete me.keyNav;
		delete me.filterbar;

		me.callParent(arguments);
	},

	// private
	initFilterFieldAccessors: function(column)
	{
		var me = this;

		if(Ext.isArray(column))
		{
			Ext.Array.forEach(column, me.initFilterFieldAccessors, me);
			return;
		}

		// Augment the Header class to have a getFilter and setFilter method
		// Important: Only if the header does not have its own implementation.
		Ext.applyIf(column, {
			getFilter: function(record, defaultFilterField)
			{
				return me.getColumnFilter(this, defaultFilterField);
			},

			setFilter: function(filterField)
			{
				me.setColumnFilter(this, filterField);
			}
		});
	},

	// private
	removeFilterFieldAccessors: function(column)
	{
		var me = this;

		if(Ext.isArray(column))
		{
			Ext.Array.forEach(column, me.removeFilterFieldAccessors, me);
			return;
		}

		delete column.getFilter;
		delete column.setFilter;
	},

	getColumnFilter: function(columnHeader, defaultFilterField)
	{
		var filterField = columnHeader.filterField;

		if(!filterField && columnHeader.filter)
		{
			filterField = columnHeader.filter;
			delete columnHeader.filter;
		}

		if(!filterField && defaultFilterField)
		{
			filterField = defaultFilterField;
		}

		if(filterField)
		{
			if(Ext.isBoolean(filterField))
			{
				filterField = {};
			}
			if(Ext.isString(filterField))
			{
				filterField = { xtype: filterField };
			}
			if(!filterField.isFormField)
			{
				/*if(filterField.xtype !== 'displayfield')
				{
					Ext.applyIf(filterField, {
						plugins: ['clearbutton']
					});
				}*/
				filterField = Ext.ComponentManager.create(filterField, this.defaultFieldXType);
			}
			columnHeader.filterField = filterField;

			Ext.apply(filterField, {
				name: columnHeader.dataIndex
			});

			return filterField;
		}
	},

	setColumnFilter: function(column, filterField)
	{
		if(Ext.isObject(filterField) && !filterField.isFormField)
		{
			filterField = Ext.ComponentManager.create(filterField, this.defaultFieldXType);
		}
		column.filterField = filterField;
	},

	/**
	 * Starts editing the specified record, using the specified Column definition to define which field is being edited.
	 */
	filter: function()
	{
		var me = this,
		filterbar = me.getFilterbar();

		if(me.callParent(arguments) === false)
		{
			return false;
		}

		// Fire off our filterbar
		if(filterbar.beforeFilter() !== false)
		{
			filterbar.filter();
		}
	},

	// private
	clearFilter: function()
	{
		var me = this;

		if(me.filtering)
		{
			me.getFilterbar().clearFilter();
			me.callParent(arguments);
		}
	},

	// private
	getFilterbar: function()
	{
		var me = this;

		if(!me.filterbar)
		{
			me.filterbar = me.initFilterbar();
		}
		return me.filterbar;
	},

	// private
	initFilterbar: function()
	{
		var me = this,
		grid = me.grid,
		headerCt = grid.headerCt,
		cfg = {
				fields: headerCt.getGridColumns(),

				// keep a reference..
				filteringPlugin: me
		};

		Ext.Array.forEach(['saveBtnText', 'cancelBtnText'], function(item)
				{
			if(Ext.isDefined(me[item]))
			{
				cfg[item] = me[item];
			}
				});

		return Ext.create('Gam.grid.Filterbar', cfg);
	},

	// private
	initEditTriggers: function()
	{
		var me = this;

		me.view.on('render', me.addHeaderEvents, me, {single: true});
	},

	addHeaderEvents: function()
	{
		var me = this;
		me.mon(me.grid.headerCt, {
			scope: me,
			add: me.onColumnAdd,
			remove: me.onColumnRemove,
			columnresize: me.onColumnResize,
			columnhide: me.onColumnHide,
			columnshow: me.onColumnShow,
			columnmove: me.onColumnMove
		});

		me.keyNav = Ext.create('Ext.util.KeyNav', me.view.el, {
			esc: me.onEscKey,
			scope: me
		});
	},

	// private
	onColumnAdd: function(ct, column)
	{
		if(column.isHeader)
		{
			var me = this,
			filterbar;

			me.initFilterFieldAccessors(column);

			// Only inform the filterbar about a new column if the filterbar has already been instantiated,
			// so do not use getEditor which instantiates the filterbar if not present.
			filterbar = me.filterbar;
			if(filterbar && filterbar.onColumnAdd)
			{
				filterbar.onColumnAdd(column);
			}
		}
	},

	// private
	onColumnRemove: function(ct, column)
	{
		if(column.isHeader)
		{
			var me = this,
			filterbar = me.getFilterbar();

			if(filterbar && filterbar.onColumnRemove)
			{
				filterbar.onColumnRemove(column);
			}
			me.removeFilterFieldAccessors(column);
		}
	},

	// private
	onColumnResize: function(ct, column, width)
	{
		if(column.isHeader)
		{
			var me = this,
			filterbar = me.getFilterbar();

			if(filterbar && filterbar.onColumnResize)
			{
				filterbar.onColumnResize(column, width);
			}
		}
	},

	// private
	onColumnHide: function(ct, column)
	{
		// no isHeader check here since its already a columnhide event.
		var me = this,
		filterbar = me.getFilterbar();

		if(filterbar && filterbar.onColumnHide)
		{
			filterbar.onColumnHide(column);
		}
	},

	// private
	onColumnShow: function(ct, column)
	{
		// no isHeader check here since its already a columnshow event.
		var me = this,
		filterbar = me.getFilterbar();

		if(filterbar && filterbar.onColumnShow)
		{
			filterbar.onColumnShow(column);
		}
	},

	// private
	onColumnMove: function(ct, column, fromIdx, toIdx)
	{
		// no isHeader check here since its already a columnmove event.
		var me = this,
		filterbar = me.getFilterbar();

		if(filterbar && filterbar.onColumnMove)
		{
			filterbar.onColumnMove(column, fromIdx, toIdx);
		}
	}
});
/**
 * Plugin that enable filters on the grid header.<br>
 * The header filters are integrated with new Ext4 <code>Ext.data.Store</code> filters.<br>
 *
 * @author Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * @version unversioned preview release
 * @updated 2011-10-18 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Support renderHidden config option, isVisible(), and setVisible() methods (added getFilterBar() method to the grid)
 * Fix filter bug that append filters to Store filters MixedCollection
 * Fix layout broken on initial render when columns have width property
 * @updated 2011-10-24 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Rendering code rewrited, filters are now rendered inside de column headers, this solves scrollable grids issues, now scroll, columnMove, and columnHide/Show is handled by the headerCt
 * Support showClearButton config option, render a clear Button for each filter to clear the applied filter (uses Gam.form.field.plugin.ClearButton plugin)
 * Added clearFilters() method.
 * @updated 2011-10-25 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Allow preconfigured filter's types and auto based on store field data types
 * Auto generated stores for combo and list filters (local collect or server in autoStoresRemoteProperty response property)
 * @updated 2011-10-26 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Completelly rewriten to support reconfigure filters on grid's reconfigure
 * Supports clearAll and showHide buttons rendered in an actioncolumn or in new generetad small column
 * @updated 2011-10-27 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Added support to 4.0.7 (columnresize not fired correctly on this build)
 * @updated 2011-11-02 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Filter on ENTER
 * Defaults submitFormat on date filter to 'Y-m-d' and use that in applyFilters for local filtering
 * Added null value support on combo and list filters (autoStoresNullValue and autoStoresNullText)
 * Fixed some combo styles
 * @updated 2011-11-10 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Parse and show initial filters applied to the store (only property -> value filters, filterFn is unsuported)
 * @updated 2011-12-12 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Extends AbstractPlugin and use Observable as a Mixin
 * Yes/No localization on constructor
 * @updated 2012-03-22 by Ing. Leonardo D'Onofrio (leonardo_donofrio at hotmail.com)
 * Fix focusFirstField method
 * Allow to specify listConfig in combo filter
 * Intercept column's setPadding for all columns except actionColumn or extraColumn (fix checkBoxSelectionModel header)
 */

Ext.define('Gam.grid.plugin.FilterBar', {
	extend: 'Ext.AbstractPlugin',
	alias: 'plugin.filterbar',
	uses: [
	       'Ext.window.MessageBox',
	       'Gam.form.field.plugin.ClearButton',
	       'Ext.container.Container',
	       'Ext.util.DelayedTask',
	       'Ext.layout.container.HBox',
	       'Ext.data.ArrayStore',
	       'Ext.button.Button',
	       'Ext.form.field.Text',
	       'Ext.form.field.Number',
	       'Ext.form.field.Date',
	       'Ext.form.field.ComboBox'
	       ],
	       mixins: {
	    	   observable: 'Ext.util.Observable'
	       },

	       headerCtHeight: 22, // nothing to do with it if you use are using the default font, padding, etc in sass
	       rowHeight: 23, // do nothing with this

	       updateBuffer: 800, // buffer time to apply filtering when typing/selecting

	       columnFilteredCls: Ext.baseCSSPrefix + 'column-filtered', // CSS class to apply to the filtered column header

	       renderHidden: false/*true*/, // renders the filters hidden by default, use in combination with showShowHideButton
	       showShowHideButton: true, // add show/hide button in actioncolumn header if found, if not a new small column is created
	       showHideButtonTooltipDo: 'Show filter bar', // button tooltip show
	       showHideButtonTooltipUndo: 'Hide filter bar', // button tooltip hide
	       showHideButtonIconCls: 'filter', // button iconCls

	       showClearButton: true, // use Gam.form.field.plugin.ClearButton to allow user to clear each filter, the same as showShowHideButton
	       showClearAllButton: true, // add clearAll button in actioncolumn header if found, if not a new small column is created
	       clearAllButtonIconCls: 'clear-filters', // css class with the icon of the clear all button
	       clearAllButtonTooltip: 'Clear all filters', // button tooltip

	       autoStoresRemoteProperty: 'autoStores', // if no store is configured for a combo filter then stores are created automatically, if remoteFilter is true then use this property to return arrayStores from the server
	       autoStoresNullValue: '###NULL###', // value send to the server to expecify null filter
	       autoStoresNullText: '[empty]', // NULL Display Text
	       autoUpdateAutoStores: false, // if set to true combo autoStores are updated each time that a filter is applied

	       boolTpl: {
	    	   xtype: 'combo',
	    	   queryMode: 'local',
	    	   forceSelection: true,
	    	   triggerAction: 'all',
	    	   editable: false,
	    	   store: [
	    	           ['1', 'Yes'],
	    	           ['0', 'No']
	    	           ],
	    	           operator: 'eq'
	       },
	       dateTpl: {
	    	   xtype: 'datefield',
	    	   editable: true,
	    	   submitFormat: 'Y-m-d',
	    	   operator: 'eq'
	       },
	       floatTpl: {
	    	   xtype: 'numberfield',
	    	   allowDecimals: true,
	    	   minValue: 0,
	    	   hideTrigger: true,
	    	   keyNavEnabled: false,
	    	   mouseWheelEnabled: false,
	    	   operator: 'eq'
	       },
	       intTpl: {
	    	   xtype: 'numberfield',
	    	   allowDecimals: false,
	    	   minValue: 0,
	    	   operator: 'eq'
	       },
	       stringTpl: {
	    	   xtype: 'textfield',
	    	   operator: 'like'
	       },
	       comboTpl: {
	    	   xtype: 'combo',
	    	   queryMode: 'local',
	    	   forceSelection: true,
	    	   editable: false,
	    	   triggerAction: 'all',
	    	   typeAhead: true,
	    	   operator: 'eq'
	       },
	       listTpl: {
	    	   xtype: 'combo',
	    	   queryMode: 'local',
	    	   forceSelection: true,
	    	   editable: false,
	    	   triggerAction: 'all',
	    	   multiSelect: true,
	    	   operator: 'in'
	       },

	       constructor: function()
	       {
	    	   var me = this;

	    	   me.boolTpl.store[0][1] = Ext.htmlDecode(Ext.MessageBox.msgButtons['yes'].text);
	    	   me.boolTpl.store[1][1] = Ext.htmlDecode(Ext.MessageBox.msgButtons['no'].text);

	    	   me.mixins.observable.constructor.call(me);
	    	   me.callParent(arguments);
	       },

	       // private
	       init: function(grid)
	       {
	    	   var me = this;

	    	   grid.on({
	    		   columnresize: me.resizeContainers,
	    		   columnhide: me.resizeContainers,
	    		   columnshow: me.resizeContainers,
	    		   beforedestroy: me.unsetup,
	    		   reconfigure: me.resetup,
	    		   scope: me
	    	   });
	    	   grid.headerCt.on({
	    		   afterlayout: me.resizeContainers,
	    		   scope: me
	    	   });
	    	   grid.addEvents('filterupdated');

	    	   Ext.apply(grid, {
	    		   filterBar: me,
	    		   getFilterBar: function()
	    		   {
	    			   return this.filterBar;
	    		   }
	    	   });

	    	   me.setup(grid);
	       },

	       // private
	       setup: function(grid)
	       {
	    	   var me = this;

	    	   me.grid = grid;
	    	   me.visible = !me.renderHidden;
	    	   me.autoStores = Ext.create('Ext.util.MixedCollection');
	    	   me.autoStoresLoaded = false;
	    	   me.columns = Ext.create('Ext.util.MixedCollection');
	    	   me.containers = Ext.create('Ext.util.MixedCollection');
	    	   me.fields = Ext.create('Ext.util.MixedCollection');
	    	   me.actionColumn = me.grid.down('actioncolumn') || me.grid.down('actioncolumnpro');
	    	   me.extraColumn = null;
	    	   me.clearAllEl = null;
	    	   me.showHideEl = null;
	    	   me.task = Ext.create('Ext.util.DelayedTask');
	    	   me.filterArray = [];

	    	   me.overrideProxy();
	    	   me.parseFiltersConfig(); 	// sets me.columns and me.autoStores
	    	   me.parseInitialFilters();   // sets me.filterArray with the store previous filters if any (adds operator and type if missing)
	    	   me.renderExtraColumn(); 	// sets me.extraColumn if applicable

	    	   // renders the filter's bar
	    	   if(grid.rendered)
	    	   {
	    		   me.renderFilterBar(grid);
	    	   }
	    	   else
	    	   {
	    		   grid.on('afterrender', me.renderFilterBar, me, { single: true });
	    	   }
	       },

	       // private
	       unsetup: function(grid)
	       {
	    	   var me = this;

	    	   if(me.autoStores.getCount())
	    	   {
	    		   me.grid.store.un('load', me.fillAutoStores, me);
	    	   }

	    	   me.autoStores.each(function(item)
	    			   {
	    		   Ext.destroy(item);
	    			   });
	    	   me.autoStores.clear();
	    	   me.autoStores = null;
	    	   me.columns.each(function(column)
	    			   {
	    		   if(column.rendered)
	    		   {
	    			   if(column.getEl().hasCls(me.columnFilteredCls))
	    			   {
	    				   column.getEl().removeCls(me.columnFilteredCls);
	    			   }
	    		   }
	    			   }, me);
	    	   me.columns.clear();
	    	   me.columns = null;
	    	   me.fields.each(function(item)
	    			   {
	    		   Ext.destroy(item);
	    			   });
	    	   me.fields.clear();
	    	   me.fields = null;
	    	   me.containers.each(function(item)
	    			   {
	    		   Ext.destroy(item);
	    			   });
	    	   me.containers.clear();
	    	   me.containers = null;
	    	   if(me.clearAllEl)
	    	   {
	    		   Ext.destroy(me.clearAllEl);
	    		   me.clearAllEl = null;
	    	   }
	    	   if(me.showHideEl)
	    	   {
	    		   Ext.destroy(me.showHideEl);
	    		   me.showHideEl = null;
	    	   }
	    	   if(me.extraColumn)
	    	   {
	    		   me.grid.headerCt.items.remove(me.extraColumn);
	    		   Ext.destroy(me.extraColumn);
	    		   me.extraColumn = null;
	    	   }
	    	   me.task = null;
	    	   me.filterArray = null;
	       },

	       // private
	       resetup: function(grid)
	       {
	    	   var me = this;

	    	   me.unsetup(grid);
	    	   me.setup(grid);
	       },

	       // private
	       overrideProxy: function()
	       {
	    	   var me = this;

	    	   // override encodeFilters to append type and operator in remote filtering
	    	   Ext.apply(me.grid.store.proxy, {
	    		   encodeFilters: function(filters)
	    		   {
	    			   var min = [],
	    			   length = filters.length,
	    			   i = 0;

	    			   for(; i < length; i++)
	    			   {
	    				   min[i] = {
	    						   property: filters[i].property,
	    						   value: filters[i].value
	    				   };
	    				   if(filters[i].type)
	    				   {
	    					   min[i].type = filters[i].type;
	    				   }
	    				   if(filters[i].operator)
	    				   {
	    					   min[i].operator = filters[i].operator;
	    				   }
	    			   }
	    			   return this.applyEncoding(min);
	    		   }
	    	   });
	       },

	       // private
	       parseFiltersConfig: function()
	       {
	    	   var me = this;
	    	   var columns = this.grid.headerCt.getGridColumns(true);
	    	   me.columns.clear();
	    	   me.autoStores.clear();
	    	   Ext.each(columns, function(column)
	    			   {
	    		   if(column.filter)
	    		   {
	    			   if(column.filter === true || column.filter === 'auto')
	    			   { // automatic types configuration (store based)
	    				   var type = me.grid.store.model.prototype.fields.get(column.dataIndex).type.type;
	    				   if(type == 'auto')
	    				   {
	    					   type = 'string';
	    				   }
	    				   column.filter = type;
	    			   }
	    			   if(Ext.isString(column.filter))
	    			   {
	    				   column.filter = {
	    						   type: column.filter // only set type to then use templates
	    				   };
	    			   }
	    			   if(column.filter.type)
	    			   {
	    				   column.filter = Ext.applyIf(column.filter, me[column.filter.type + 'Tpl']); // also use templates but with user configuration
	    			   }

	    			   if(column.filter.xtype == 'combo' && !column.filter.store)
	    			   {
	    				   column.autoStore = true;
	    				   column.filter.store = Ext.create('Ext.data.ArrayStore', {
	    					   fields: [
	    					            {
	    					            	name: 'text'
	    					            },
	    					            {
	    					            	name: 'id'
	    					            }
	    					            ]
	    				   });
	    				   me.autoStores.add(column.dataIndex, column.filter.store);
	    				   column.filter = Ext.apply(column.filter, {
	    					   displayField: 'text',
	    					   valueField: 'id'
	    				   });
	    			   }

	    			   if(!column.filter.type)
	    			   {
	    				   switch(column.filter.xtype)
	    				   {
	    				   case 'combo':
	    					   column.filter.type = (column.filter.multiSelect ? 'list' : 'combo');
	    					   break;
	    				   case 'datefield':
	    					   column.filter.type = 'date';
	    					   break;
	    				   case 'numberfield':
	    					   column.filter.type = (column.filter.allowDecimals ? 'float' : 'int');
	    					   break;
	    				   default:
	    					   column.filter.type = 'string'
	    				   }
	    			   }

	    			   if(!column.filter.operator)
	    			   {
	    				   column.filter.operator = me[column.filter.type + 'Tpl'].operator;
	    			   }
	    			   me.columns.add(column.dataIndex, column);
	    		   }
	    			   }, me);
	    	   if(me.autoStores.getCount())
	    	   {
	    		   if(me.grid.store.getCount() > 0)
	    		   {
	    			   me.fillAutoStores(me.grid.store);
	    		   }
	    		   if(me.grid.store.remoteFilter)
	    		   {
	    			   var autoStores = [];
	    			   me.autoStores.eachKey(function(key, item)
	    					   {
	    				   autoStores.push(key);
	    					   });
	    			   me.grid.store.proxy.extraParams = me.grid.store.proxy.extraParams || {};
	    			   me.grid.store.proxy.extraParams[me.autoStoresRemoteProperty] = autoStores;
	    		   }
	    		   me.grid.store.on('load', me.fillAutoStores, me);
	    	   }
	       },

	       // private
	       fillAutoStores: function(store)
	       {
	    	   var me = this;

	    	   if(!me.autoUpdateAutoStores && me.autoStoresLoaded)
	    	   {
	    		   return;
	    	   }

	    	   me.autoStores.eachKey(function(key, item)
	    			   {
	    		   var field = me.fields.get(key),
	    		   data,
	    		   records;
	    		   if(field)
	    		   {
	    			   field.suspendEvents();
	    			   var fieldValue = field.getValue();
	    		   }
	    		   if(!store.remoteFilter)
	    		   { // values from local store
	    			   data = store.collect(key, true, false).sort();
	    			   records = [];
	    			   Ext.each(data, function(txt)
	    					   {
	    				   if(Ext.isEmpty(txt))
	    				   {
	    					   Ext.Array.insert(records, 0, [
	    					                                 {
	    					                                	 text: me.autoStoresNullText,
	    					                                	 id: me.autoStoresNullValue
	    					                                 }
	    					                                 ]);
	    				   }
	    				   else
	    				   {
	    					   records.push({
	    						   text: txt,
	    						   id: txt
	    					   });
	    				   }
	    					   });
	    			   item.loadData(records);
	    		   }
	    		   else
	    		   { // values from server
	    			   if(store.proxy.reader.rawData[me.autoStoresRemoteProperty])
	    			   {
	    				   data = store.proxy.reader.rawData[me.autoStoresRemoteProperty];
	    				   if(data[key])
	    				   {
	    					   records = [];
	    					   Ext.each(data[key].sort(), function(txt)
	    							   {
	    						   if(Ext.isEmpty(txt))
	    						   {
	    							   Ext.Array.insert(records, 0, [
	    							                                 {
	    							                                	 text: me.autoStoresNullText,
	    							                                	 id: me.autoStoresNullValue
	    							                                 }
	    							                                 ]);
	    						   }
	    						   else
	    						   {
	    							   records.push({
	    								   text: txt,
	    								   id: txt
	    							   });
	    						   }
	    							   });
	    					   item.loadData(records);
	    				   }
	    			   }
	    		   }
	    		   if(field)
	    		   {
	    			   field.setValue(fieldValue);
	    			   field.resumeEvents();
	    		   }
	    			   }, me);
	    	   me.autoStoresLoaded = true;
	    	   if(me.grid.store.remoteFilter && !me.autoUpdateAutoStores)
	    	   {
	    		   delete me.grid.store.proxy.extraParams[me.autoStoresRemoteProperty];
	    	   }
	       },

	       // private
	       parseInitialFilters: function()
	       {
	    	   var me = this;

	    	   me.filterArray = [];
	    	   me.grid.store.filters.each(function(filter)
	    			   {
	    		   // try to parse initial filters, for now filterFn is unsuported
	    		   if(filter.property && !Ext.isEmpty(filter.value) && me.columns.get(filter.property))
	    		   {
	    			   if(!filter.type)
	    			   {
	    				   filter.type = me.columns.get(filter.property).filter.type;
	    			   }
	    			   if(!filter.operator)
	    			   {
	    				   filter.operator = me.columns.get(filter.property).filter.operator;
	    			   }
	    			   me.filterArray.push(filter);
	    		   }
	    			   }, me);
	       },

	       // private
	       renderExtraColumn: function()
	       {
	    	   var me = this;

	    	   if(me.columns.getCount() && !me.actionColumn && (me.showClearAllButton || me.showShowHideButton))
	    	   {
	    		   var extraColumnCssClass = Ext.baseCSSPrefix + 'filter-bar-extra-column-hack';
	    		   if(!document.getElementById(extraColumnCssClass))
	    		   {
	    			   var style = document.createElement('style');
	    			   var css = 'tr.' + Ext.baseCSSPrefix + 'grid-row td.' + extraColumnCssClass + ' { background-color: #ffffff !important; border-color: #ffffff !important; }';
	    			   style.setAttribute('type', 'text/css');
	    			   style.setAttribute('id', extraColumnCssClass);
	    			   document.body.appendChild(style);
	    			   if(style.styleSheet)
	    			   {       // IE
	    				   style.styleSheet.cssText = css;
	    			   }
	    			   else
	    			   {                    // others
	    				   var cssNode = document.createTextNode(css);
	    				   style.appendChild(cssNode);
	    			   }
	    		   }
	    		   me.extraColumn = Ext.create('Ext.grid.column.Column', {
	    			   draggable: false,
	    			   hideable: false,
	    			   menuDisabled: true,
	    			   sortable: false,
	    			   resizable: false,
	    			   fixed: true,
	    			   width: 28,
	    			   minWidth: 28,
	    			   maxWidth: 28,
	    			   header: '&nbsp;',
	    			   tdCls: extraColumnCssClass
	    		   });
	    		   me.grid.headerCt.add(me.extraColumn);
	    	   }
	       },

	       // private
	       renderFilterBar: function(grid)
	       {
	    	   var me = this;

	    	   me.containers.clear();
	    	   me.fields.clear();
	    	   me.columns.eachKey(function(key, column)
	    			   {
	    		   var listConfig = column.filter.listConfig || {};
	    		   listConfig = Ext.apply(listConfig, {
	    			   style: 'border-top-width: 1px'
	    		   });
	    		   var field = Ext.widget(column.filter.xtype || 'textfield', Ext.apply(column.filter, {
	    			   dataIndex: key,
	    			   flex: 1,
	    			   margin: 0,
	    			   fieldStyle: 'border-right-width: 0px; border-bottom-width: 0px;',
	    			   listConfig: listConfig,
	    			   preventMark: true,
	    			   enableKeyEvents: true,
	    			   listeners: {
	    				   change: me.applyFilters,
	    				   keypress: function(txt, e)
	    				   {
	    					   if(e.getCharCode() == 13)
	    					   {
	    						   me.applyFilters(txt, txt.getValue());
	    						   e.stopEvent();
	    					   }
	    				   },
	    				   scope: me
	    			   },
	    			   plugins: (!me.showClearButton ? [] : [
	    			                                         {
	    			                                        	 ptype: 'clearbutton'
	    			                                         }
	    			                                         ])
	    		   }));
	    		   me.fields.add(column.dataIndex, field);
	    		   var container = Ext.create('Ext.container.Container', {
	    			   dataIndex: key,
	    			   layout: 'hbox',
	    			   bodyStyle: 'background-color: "transparent";',
	    			   width: column.getWidth(),
	    			   items: [field],
	    			   listeners: {
	    				   scope: me,
	    				   element: 'el',
	    				   mousedown: function(e) { e.stopPropagation(); },
	    				   click: function(e) { e.stopPropagation(); },
	    				   dblclick: function(e) { e.stopPropagation(); }
	    			   }
	    		   });
	    		   container.on('afterrender', function(cnt)
	    				   {
	    			   me.rowHeight = cnt.getHeight();
	    			   var delayedFn = function()
	    			   {
	    				   this.resizeContainer(cnt.dataIndex);
	    			   };
	    			   Ext.Function.defer(delayedFn, 1, this);
	    				   }, me, { single: true });
	    		   me.containers.add(column.dataIndex, container);
	    		   container.render(Ext.get(column.id));
	    			   }, me);
	    	   var excludedCols = [];
	    	   if(me.actionColumn)
	    	   {
	    		   excludedCols.push(me.actionColumn.id);
	    	   }
	    	   if(me.extraColumn)
	    	   {
	    		   excludedCols.push(me.extraColumn.id);
	    	   }
	    	   Ext.each(me.grid.headerCt.getGridColumns(true), function(column)
	    			   {
	    		   if(!Ext.Array.contains(excludedCols, column.id))
	    		   {
	    			   column.setPadding = Ext.Function.createInterceptor(column.setPadding, function(h) { return false; });
	    		   }
	    			   });

	    	   me.setVisible(me.visible);

	    	   me.renderButtons();

	    	   me.showInitialFilters();
	       },

	       //private
	       renderButtons: function()
	       {
	    	   var me = this,
	    	   column,
	    	   buttonEl;

	    	   if(me.showShowHideButton && me.columns.getCount())
	    	   {
	    		   column = me.actionColumn || me.extraColumn;
	    		   buttonEl = column.el.first().first();
	    		   me.showHideEl = Ext.get(Ext.core.DomHelper.append(buttonEl, {
	    			   tag: 'div',
	    			   style: 'margin-top: -32px; text-align: center;',
	    			   html: '<span style="cursor: pointer; background-repeat: no-repeat;" class="' + me.showHideButtonIconCls + '" data-qtip="' + (me.renderHidden ? me.showHideButtonTooltipDo : me.showHideButtonTooltipUndo) + '">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'
	    		   }));
	    		   me.showHideEl.on('click', function()
	    				   {
	    			   me.setVisible(!me.isVisible());
	    			   me.showHideEl.set({
	    				   'data-qtip': (!me.isVisible() ? me.showHideButtonTooltipDo : me.showHideButtonTooltipUndo)
	    			   });
	    				   });
	    	   }

	    	   if(me.showClearAllButton && me.columns.getCount())
	    	   {
	    		   column = me.actionColumn || me.extraColumn;
	    		   buttonEl = column.el.first().first();
	    		   me.clearAllEl = Ext.get(Ext.core.DomHelper.append(buttonEl, {
	    			   tag: 'div',
	    			   style: 'margin-top: ' + (me.showShowHideButton ? '-2px' : '-12px') + '; text-align: center;',
	    			   html: '<span style="cursor: pointer; background-repeat: no-repeat;" class="' + me.clearAllButtonIconCls + '" data-qtip="' + me.clearAllButtonTooltip + '">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'
	    		   }));

	    		   me.clearAllEl.hide();
	    		   me.clearAllEl.on('click', function()
	    				   {
	    			   me.clearFilters();
	    				   });
	    	   }
	       },

	       // private
	       showInitialFilters: function()
	       {
	    	   var me = this;

	    	   Ext.each(me.filterArray, function(filter)
	    			   {
	    		   var column = me.columns.get(filter.property);
	    		   var field = me.fields.get(filter.property);
	    		   if(!column.getEl().hasCls(me.columnFilteredCls))
	    		   {
	    			   column.getEl().addCls(me.columnFilteredCls);
	    		   }
	    		   field.suspendEvents();
	    		   field.setValue(filter.value);
	    		   field.resumeEvents();
	    			   });

	    	   if(me.filterArray.length && me.showClearAllButton)
	    	   {
	    		   me.clearAllEl.show({duration: 1000});
	    	   }
	       },

	       // private
	       resizeContainer: function(dataIndex)
	       {
	    	   var me = this;

	    	   var item = me.containers.get(dataIndex);
	    	   if(item && item.rendered)
	    	   {
	    		   var itemWidth = item.getWidth();
	    		   var colWidth = me.columns.get(dataIndex).getWidth();
	    		   if(itemWidth != colWidth)
	    		   {
	    			   item.setWidth(me.columns.get(dataIndex).getWidth());
	    			   item.doLayout();
	    		   }
	    	   }
	       },

	       // private
	       resizeContainers: function(headerCt, col)
	       {
	    	   var me = this;

	    	   if(col.dataIndex)
	    	   {
	    		   me.resizeContainer(col.dataIndex);
	    	   }
	    	   var delayedFn = function()
	    	   {
	    		   me.containers.eachKey(function(key, item)
	    				   {
	    			   me.resizeContainer(key);
	    				   }, me);
	    	   };
	    	   // need to defer and set all widths because 4.0.7 is not firing columnresize correctly
	    	   Ext.Function.defer(delayedFn, 1);
	       },

	       // private
	       applyFilters: function(field, newVal)
	       {
	    	   var me = this;

	    	   if(!field.isValid())
	    	   {
	    		   return;
	    	   }
	    	   var grid = me.grid;
	    	   var column = me.columns.get(field.dataIndex);
	    	   newVal = (grid.store.remoteFilter ? field.getSubmitValue() : newVal);
	    	   me.task.delay(me.updateBuffer, function()
	    			   {
	    		   if(Ext.isArray(newVal) && newVal.length == 0)
	    		   {
	    			   newVal = '';
	    		   }
	    		   var myIndex = -1;
	    		   Ext.each(me.filterArray, function(item2, index, allItems)
	    				   {
	    			   if(item2.property === column.dataIndex)
	    			   {
	    				   myIndex = index;
	    			   }
	    				   });
	    		   if(myIndex != -1)
	    		   {
	    			   me.filterArray.splice(myIndex, 1);
	    		   }
	    		   if(!Ext.isEmpty(newVal))
	    		   {
	    			   if(!grid.store.remoteFilter)
	    			   {
	    				   var filterFn;
	    				   switch(column.filter.operator)
	    				   {
	    				   case 'eq':
	    					   filterFn = function(item)
	    					   {
	    					   if(column.filter.type == 'date')
	    					   {
	    						   return Ext.Date.clearTime(item.get(column.dataIndex), true).getTime() == Ext.Date.clearTime(newVal, true).getTime();
	    					   }
	    					   else
	    					   {
	    						   return (Ext.isEmpty(item.get(column.dataIndex)) ? me.autoStoresNullValue : item.get(column.dataIndex)) == (Ext.isEmpty(newVal) ? me.autoStoresNullValue : newVal);
	    					   }
	    					   };
	    					   break;
	    				   case 'like':
	    					   filterFn = function(item)
	    					   {
	    					   var re = new RegExp(newVal, 'i');
	    					   return re.test(item.get(column.dataIndex));
	    					   };
	    					   break;
	    				   case 'in':
	    					   filterFn = function(item)
	    					   {
	    					   var re = new RegExp('^' + newVal.join('|') + '$', 'i');
	    					   return re.test((Ext.isEmpty(item.get(column.dataIndex)) ? me.autoStoresNullValue : item.get(column.dataIndex)));
	    					   };
	    					   break;
	    				   }
	    				   me.filterArray.push(Ext.create('Ext.util.Filter', {
	    					   property: column.dataIndex,
	    					   filterFn: filterFn,
	    					   me: me
	    				   }));
	    			   }
	    			   else
	    			   {
	    				   me.filterArray.push(Ext.create('Ext.util.Filter', {
	    					   property: column.dataIndex,
	    					   value: newVal,
	    					   type: column.filter.type,
	    					   operator: column.filter.operator
	    				   }));
	    			   }
	    			   if(!column.getEl().hasCls(me.columnFilteredCls))
	    			   {
	    				   column.getEl().addCls(me.columnFilteredCls);
	    			   }
	    		   }
	    		   else
	    		   {
	    			   if(column.getEl().hasCls(me.columnFilteredCls))
	    			   {
	    				   column.getEl().removeCls(me.columnFilteredCls);
	    			   }
	    		   }
	    		   grid.store.currentPage = 1;
	    		   if(me.filterArray.length > 0)
	    		   {
	    			   if(!grid.store.remoteFilter)
	    			   {
	    				   grid.store.clearFilter();
	    			   }
	    			   grid.store.filters.clear();
	    			   grid.store.filter(me.filterArray);
	    			   if(me.clearAllEl)
	    			   {
	    				   me.clearAllEl.show({duration: 1000});
	    			   }
	    		   }
	    		   else
	    		   {
	    			   grid.store.clearFilter();
	    			   if(me.clearAllEl)
	    			   {
	    				   me.clearAllEl.hide({duration: 1000});
	    			   }
	    		   }
	    		   if(!grid.store.remoteFilter && me.autoUpdateAutoStores)
	    		   {
	    			   me.fillAutoStores();
	    		   }
	    		   me.fireEvent('filterupdated', me.filterArray);
	    			   }, me);
	       },

	       //private
	       getFirstField: function()
	       {
	    	   var me = this,
	    	   field = undefined;

	    	   Ext.each(me.grid.headerCt.getGridColumns(true), function(col)
	    			   {
	    		   if(col.filter)
	    		   {
	    			   field = me.fields.get(col.dataIndex);
	    			   return false;
	    		   }
	    			   });

	    	   return field;
	       },

	       //private
	       focusFirstField: function()
	       {
	    	   var me = this;

	    	   var field = me.getFirstField();

	    	   if(field)
	    	   {
	    		   field.focus(false, 200);
	    	   }
	       },

	       clearFilters: function()
	       {
	    	   var me = this;

	    	   if(me.filterArray.length == 0)
	    	   {
	    		   return;
	    	   }
	    	   me.filterArray = [];
	    	   me.fields.eachKey(function(key, field)
	    			   {
	    		   field.suspendEvents();
	    		   field.reset();
	    		   field.resumeEvents();
	    		   var column = me.columns.get(key);
	    		   if(column.getEl().hasCls(Ext.baseCSSPrefix + 'column-filtered'))
	    		   {
	    			   column.getEl().removeCls(Ext.baseCSSPrefix + 'column-filtered');
	    		   }
	    			   }, me);
	    	   me.grid.store.clearFilter();
	    	   if(me.clearAllEl)
	    	   {
	    		   me.clearAllEl.hide({duration: 1000});
	    	   }

	    	   me.fireEvent('filterupdated', me.filterArray);
	       },

	       isVisible: function()
	       {
	    	   var me = this;

	    	   return me.visible;
	       },

	       setVisible: function(visible)
	       {
	    	   var me = this;

	    	   var headerCt = me.grid.headerCt;
	    	   if(!me.visible && visible)
	    	   {
	    		   me.focusFirstField();
	    	   }
	    	   me.visible = visible;

	    	   headerCt.setHeight((visible ? me.headerCtHeight + me.rowHeight : me.headerCtHeight));
	    	   headerCt.doLayout();
	       }
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/3/12
 * Time: 7:10 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.RowEditor
 * @extends Ext.grid.RowEditor
 */
Ext.define('Gam.grid.RowEditor', {
	extend: 'Ext.grid.RowEditor',

	onFieldChange: function()
	{
		var me = this,
		form = me.getForm(),
		valid = form.isValid(),
		dirty = valid ? form.isDirty() : false;
		if(me.errorSummary && me.isVisible())
		{
			me[valid ? 'hideToolTip' : 'showToolTip']();
		}
		if(me.floatingButtons)
		{
			me.floatingButtons.child('#update').setDisabled(!dirty);
		}
		me.isValid = valid;
	},

	reRenderDisplayFields: function(record)
	{
		var me = this;

		// render display fields so they honor the column renderer/template
		Ext.Array.forEach(me.query('>displayfield'), function(field)
				{
			me.renderColumnData(field, record);
				}, me);
	},

	/**
	 * @private
	 */
	createForm: function()
	{
		return new Gam.form.Basic(this, Ext.applyIf({listeners: {}}, this.initialConfig));
	},

	/**
	 * Start editing the specified grid at the specified position.
	 * @param {Ext.data.Model} record The Store data record which backs the row to be edited.
	 * @param {Ext.data.Model} columnHeader The Column object defining the column to be edited.
	 */
	startEdit: function(record, columnHeader)
	{
		var me = this,
		grid = me.editingPlugin.grid,
		store = grid.store,
		fields = me.form.getFields();

		me.context = Ext.apply(me.editingPlugin.context, {
			view: grid.getView(),
			store: store
		});

		me.editingPlugin.grid.getSelectionModel().deselectAll();
		me.editingPlugin.grid.getSelectionModel().setLocked(true);

		// Reload the record data
		me.loadRecord(record);

		if(!me.isVisible())
		{
			me.show();
			me.focusContextCell();
		}
		else
		{
			me.reposition({
				callback: this.focusContextCell
			});
		}

		me.onFieldChange();
		fields.each(function(field)
				{
			if(field.is('textareafield'))
			{
				field.setHeight(this.up().getHeight() - 10);
			}
				});
	},

	cancelEdit: function()
	{
		this.callParent();

		this.editingPlugin.grid.getSelectionModel().setLocked(false);
	},

	completeEdit: function()
	{
		var me = this,
		form = me.getForm();

		if(!form.isValid())
		{
			return;
		}

		form.updateRecord(me.context.record);
		Gam.Msg.showWaitMsg();
		form.getRecord().stores[0].sync({
			waitMsg: Gam.Resource.message.info.formSubmitWaiting,
			success: me.onSyncSuccess,
			scope: me
		});

		return true;
	},

	onSyncSuccess: function(batch, options)
	{
		var me = this;

		Gam.Msg.hideWaitMsg();
		me.editingPlugin.finalizeEdit();
		me.hide();
		me.editingPlugin.grid.getSelectionModel().setLocked(false);
	},

	eachField: function(fn)
	{
		this.getForm().getFields().each(fn, this);
	},

	eachEditableField: function(fn)
	{
		this.getForm().getEditableFields().each(fn, this);
	},

	/**
	 * @private
	 * Returns the focus holder element associated with this Window. By default, this is the Window's element.
	 * @returns {Ext.Element/Ext.Component} the focus holding element or Component.
	 */
	getFocusEl: function()
	{
		return this.getDefaultFocus();
	},

	/**
	 * Gets the configured default focus item.  If a {@link #defaultFocus} is set, it will
	 * receive focus when the Window's <code>focus</code> method is called, otherwise the
	 * Window itself will receive focus.
	 */
	getDefaultFocus: function()
	{
		var me = this,
		result,
		selector;

		if(me.defaultFocus === undefined)
		{
			result = this.getForm().getFirstEditableField();
		}
		// String is ID or CQ selector
		else if(Ext.isString(me.defaultFocus))
		{
			selector = me.defaultFocus;
			if(selector.substr(0, 1) !== '#')
			{
				selector = '#' + selector;
			}
			result = me.down(selector);
		}
		// Otherwise, if it's got a focus method, use it
		else if(me.defaultFocus.focus)
		{
			result = me.defaultFocus;
		}

		return result;
	},

	/**
	 * @private
	 * Called when a Component's focusEl receives focus.
	 * If there is a valid default focus Component to jump to, focus that,
	 * otherwise continue as usual, focus this Component.
	 */
	onFocus: function()
	{
		var me = this,
		focusDescendant;

		// If the FocusManager is enabled, then we must noy jumpt to focus the default focus. We must focus the Window
		if((Ext.FocusManager && Ext.FocusManager.enabled) || ((focusDescendant = me.getDefaultFocus()) === me))
		{
			me.callParent(arguments);
		}
		else
		{
			focusDescendant.focus();
		}
	},

	focusContextCell: function()
	{
		this.focus();
	},

	hideToolTip: Ext.emptyFn,

	showToolTip: Ext.emptyFn
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/24/11
 * Time: 4:05 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.plugin.RowEditing
 * @extends Gam.grid.plugin.RowEditing
 */
Ext.define('Gam.grid.plugin.RowEditing', {
	extend: 'Ext.grid.plugin.RowEditing',
	alias: 'plugin.gamrowediting',

	requires: [ 'Gam.grid.RowEditor' ],

	// private
	initEditTriggers: function()
	{
		var me = this,
		view = me.view,
		moveEditorEvent = me.clicksToMoveEditor === 1 ? 'click' : 'dblclick';

		view.on('render', me.addHeaderEvents, me, {single: true});

		if(me.clicksToMoveEditor !== me.clicksToEdit)
		{
			me.mon(view, 'cell' + moveEditorEvent, me.moveEditorByClick, me);
		}
	},

	cancelEdit: function()
	{
		var me = this,
		store = me.grid.getStore();

		try
		{
			me.context.record.reject();
			store.remove(store.getNewRecords());
		}
		catch(e)
		{ }
		if(me.editing)
		{
			me.callParent(arguments);
			me.context = null;
		}
	},

	// private
	completeEdit: function()
	{
		var me = this;

		if(me.editing && me.validateEdit())
		{
			//me.editing = false;
			me.fireEvent('edit', me, me.context);
		}
		//me.callParent(arguments);

		if(!me.editing)
		{ me.context = null;}
	},

	finalizeEdit: function()
	{
		this.context = null;
		this.editing = false;
	},

	// private
	initEditor: function()
	{
		var me = this,
		grid = me.grid,
		view = me.view,
		headerCt = grid.headerCt,
		cfg = {
				autoCancel: me.autoCancel,
				errorSummary: me.errorSummary,
				fields: headerCt.getGridColumns(),
				hidden: true,

				// keep a reference..
				editingPlugin: me,
				renderTo: view.el
		};

		Ext.Array.forEach(['saveBtnText', 'cancelBtnText', 'errorsText', 'dirtyText'], function(item)
				{
			if(Ext.isDefined(me[item]))
			{
				cfg[item] = me[item];
			}
				});

		return Ext.create('Gam.grid.RowEditor', Ext.apply(cfg, me.editorCfg));
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Sina Golesorkhi
 * Date: 9/10/11
 * Time: 12:07 PM
 */

/**
 * RowMetadata is plugin which can be added to a Grid.
 * This plugin adds a row of metadata (extra information about a record) to each row in the grid.
 * The '+' button will be added to the left/right hand side of the grid which enables us to expand a row to see the metadata
 * assigned to that specific record.
 *
 *
 * This component accepts every component as it’s config to be added to each row.
 *
 *
 * ##Example Usage
 *
 * Ext.define('Gam.grid.SimpleParameterPanel', {
 *		 requires:['Ext.grid.plugin.RowEditing',
 *		 'Gam.grid.plugin.RowMetadata'],
 *		 extend: 'Ext.grid.Panel',
 *		 id: 'simpleParameterPanel',
 *		 scroll: 'horizontal',
 *		 plugins: [
 *		 {
 *			 ptype:'rowmetadata',
 *			 component: 'Ext.grid.Panel',
 *			 componentConfig: {
 *				 scroll:'horizontal',
 *				 title: 'inner grid',
 *				 store: Ext.create('GamCM.store.HistoryStore'),
 *				 columns: [
 *					 { header: 'Id',  dataIndex: 'id', flex: 1 },
 *					 { header: 'Name', dataIndex: 'name', flex: 1 },
 *					 { header: 'Email', dataIndex: 'email', flex: 1 }
 *				 ],
 *				 height:100
 *			 }
 *		 },
 *		 Ext.create('Ext.grid.plugin.RowEditing'),
 *	 ],
 *
 * component:  Is the name of component which should be rendered into each row.
 * componentConfig: Is the initial configuration for the specified component.
 * Hint: If you’re using this plugin alongside with “RowEditing” be aware that you should include it before RowEditing in the list of plugins.
 * That way when a user double clicks an an expanded row it first collapses and then “RowEditing” plugin comes into the play and lets you edit the row.
 *
 *
 * The plugin accepts every kind of components to be added to the bottom of each row in the grid.
 * @author Sina Golesorkhi
 * @class Gam.grid.plugin.RowMetadata
 * @extend Ext.ux.RowExpander
 */
Ext.define('Gam.grid.plugin.RowMetadata', {
	extend: 'Ext.AbstractPlugin',
	alias: 'plugin.rowmetadata',

	requires: [
	           'Ext.grid.feature.RowWrap',
	           'Ext.grid.feature.RowBody'
	           ],

	           mixins: ['Ext.util.Observable'],

	           /**
	            * @cfg {Boolean} expandOnEnter
	            * <tt>true</tt> to toggle selected row(s) between expanded/collapsed when the enter
	            * key is pressed (defaults to <tt>true</tt>).
	            */
	           expandOnEnter: false,

	           /**
	            * @cfg {Boolean} expandOnDblClick
	            * <tt>true</tt> to toggle a row between expanded/collapsed when double clicked
	            * (defaults to <tt>true</tt>).
	            */
	           collapseOnDblClick: true,

	           /**
	            * @cfg {Boolean} selectRowOnExpand
	            * <tt>true</tt> to select a row when clicking on the expander icon
	            * (defaults to <tt>false</tt>).
	            */
	           selectRowOnExpand: false,

	           rowBodyTrSelector: '.x-grid-rowbody-tr',

	           rowBodyHiddenCls: 'x-grid-row-body-hidden',

	           rowCollapsedCls: 'x-grid-row-collapsed',

	           rowBodyDivSelector: '.x-grid-rowbody',

	           /**
	            * @event expandbody
	            * <b<Fired through the grid's View</b>
	            * @param {HtmlElement} rowNode The &lt;tr> element which owns the expanded row.
	            * @param {Ext.data.Model} record The record providing the data.
	            * @param {HtmlElement} expandRow The &lt;tr> element containing the expanded data.
	            */
	           /**
	            * @event collapsebody
	            * <b<Fired through the grid's View.</b>
	            * @param {HtmlElement} rowNode The &lt;tr> element which owns the expanded row.
	            * @param {Ext.data.Model} record The record providing the data.
	            * @param {HtmlElement} expandRow The &lt;tr> element containing the expanded data.
	            */

	           constructor: function()
	           {
	        	   var me = this;

	        	   me.callParent(arguments);

	        	   var grid = me.getCmp();
	        	   me.recordsExpanded = {};
	        	   var features = [
	        	                   {
	        	                	   ftype: 'rowbody',
	        	                	   columnId: this.getHeaderId(),
	        	                	   recordsExpanded: me.recordsExpanded,
	        	                	   rowBodyHiddenCls: me.rowBodyHiddenCls,
	        	                	   rowCollapsedCls: me.rowCollapsedCls,
	        	                	   getAdditionalData: me.getRowBodyFeatureData,
	        	                	   getRowBodyContents: function(data)
	        	                	   {
	        	                		   return '';
	        	                	   }
	        	                   },
	        	                   {
	        	                	   ftype: 'rowwrap'
	        	                   }
	        	                   ];

	        	   if(grid.features)
	        	   {
	        		   grid.features = Ext.Array.merge(features, grid.features);
	        	   }
	        	   else
	        	   {
	        		   grid.features = features;
	        	   }
	           },

	           init: function(grid)
	           {
	        	   var me = this;

	        	   me.view = grid.view;
	        	   me.initKeyNav();
	        	   grid.headerCt.insert(0, me.getHeaderConfig());
	        	   me.mon(me.view, 'celldblclick', me.onDoubleClick, me);
	           },

	           getComponentConfig: function()
	           {
	        	   return {
	        		   xtype: 'label',
	        		   text: 'row meta data'
	        	   };
	           },

	           getHeaderId: function()
	           {
	        	   if(!this.headerId)
	        	   {
	        		   this.headerId = Ext.id();
	        	   }
	        	   return this.headerId;
	           },

	           getRowBodyFeatureData: function(data, idx, record, orig)
	           {
	        	   var me = this,
	        	   o = Ext.grid.feature.RowBody.prototype.getAdditionalData.apply(me, arguments),
	        	   id = me.columnId;

	        	   o.rowBodyColspan = o.rowBodyColspan - 1;
	        	   o.rowBody = me.getRowBodyContents(data);
	        	   o.rowCls = me.recordsExpanded[record.internalId] ? '' : me.rowCollapsedCls;
	        	   o.rowBodyCls = me.recordsExpanded[record.internalId] ? '' : me.rowBodyHiddenCls;
	        	   o[id + '-tdAttr'] = ' valign="top" rowspan="2" ';
	        	   if(orig[id + '-tdAttr'])
	        	   {
	        		   o[id + '-tdAttr'] += orig[id + '-tdAttr'];
	        	   }

	        	   return o;
	           },

	           initKeyNav: function()
	           {
	        	   if(this.expandOnEnter)
	        	   {
	        		   this.keyNav = Ext.create('Ext.KeyNav', this.view.getEl(), {
	        			   'enter': this.onEnter,
	        			   scope: this
	        		   });
	        	   }
	           },

	           onEnter: function(e)
	           {
	        	   var view = this.view,
	        	   ds = view.store,
	        	   sm = view.getSelectionModel(),
	        	   sels = sm.getSelection(),
	        	   ln = sels.length,
	        	   i = 0,
	        	   rowIdx;

	        	   for(; i < ln; i++)
	        	   {
	        		   rowIdx = ds.indexOf(sels[i]);
	        		   this.toggleRow(rowIdx);
	        	   }
	           },

	           /**
	            * Will be triggered when the '+' button on the left/right hand side of the grid is clicked.
	            * @param rowEl  The index of the row/record which should be expanded.
	            */
	           toggleRow: function(rowEl, rowIndex)
	           {
	        	   var me = this,
	        	   rowNode = me.view.getNode(rowEl),
	        	   row = Ext.get(rowNode),
	        	   nextBd = Ext.fly(row).down(me.rowBodyTrSelector),
	        	   div = Ext.fly(row).down(me.rowBodyDivSelector),
	        	   record = me.view.getRecord(rowNode),
	        	   componentConfig;

	        	   //collapsed
	        	   if(row.hasCls(me.rowCollapsedCls) && me.collapseOnDblClick)
	        	   {
	        		   row.removeCls(me.rowCollapsedCls);
	        		   nextBd.removeCls(me.rowBodyHiddenCls);
	        		   me.recordsExpanded[record.internalId] = true;

	        		   componentConfig = me.getComponentConfig(rowIndex);
	        		   Ext.applyIf(componentConfig, {
	        			   id: row.id + 'inner-component',
	        			   width: div.getWidth(),
	        			   containerDiv: div,
	        			   renderTo: div
	        		   });

	        		   if(componentConfig.className)
	        		   { Ext.create(componentConfig.className, componentConfig); }
	        		   else
	        		   { Ext.widget(componentConfig.xtype, componentConfig); }

	        		   me.view.fireEvent('expandbody', rowNode, record, nextBd.dom);
	        	   }
	        	   else //expanded
	        	   {
	        		   me.collapseOnDblClick = true;
	        		   Ext.destroy(row.id + 'inner-component');
	        		   Ext.removeNode(Ext.getDom(row.id + 'inner-component'));
	        		   row.addCls(me.rowCollapsedCls);
	        		   nextBd.addCls(me.rowBodyHiddenCls);
	        		   this.recordsExpanded[record.internalId] = false;
	        		   me.view.fireEvent('collapsebody', rowNode, record, nextBd.dom);
	        	   }
	           },

	           onDoubleClick: function(view, cell, colIdx, record, row, rowIdx, e)
	           {
	        	   this.collapseOnDblClick = false;
	        	   this.toggleRow(rowIdx);
	           },

	           getHeaderConfig: function()
	           {
	        	   this.collapseOnDblClick = true;
	        	   var me = this,
	        	   toggleRow = Ext.Function.bind(me.toggleRow, me),
	        	   selectRowOnExpand = me.selectRowOnExpand;

	        	   return {
	        		   id: this.getHeaderId(),
	        		   width: 23,
	        		   sortable: false,
	        		   fixed: true,
	        		   draggable: false,
	        		   hideable: false,
	        		   menuDisabled: true,
	        		   cls: Ext.baseCSSPrefix + 'grid-header-special',
	        		   renderer: function(value, metadata)
	        		   {
	        			   metadata.tdCls = Ext.baseCSSPrefix + 'grid-cell-special';

	        			   return '<div class="' + Ext.baseCSSPrefix + 'grid-row-expander">&#160;</div>';
	        		   },
	        		   processEvent: function(type, view, cell, recordIndex, cellIndex, e)
	        		   {
	        			   if(type == "mousedown" && e.getTarget('.x-grid-row-expander'))
	        			   {
	        				   var row = e.getTarget('.x-grid-row');
	        				   toggleRow(row, recordIndex);
	        				   return selectRowOnExpand;
	        			   }
	        		   }
	        	   };
	           }
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 1/2/12
 * Time: 9:47 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.column.Date
 * @extends Ext.grid.column.Column
 */
Ext.define('Gam.grid.column.Date', {
	extend: 'Ext.grid.column.Column',
	alias: 'widget.gam.datecolumn',
	requires: [
	           'Ext.Date',
	           'Ext.JalaliDate',
	           'Ext.GregorianDate',
	           'Ext.IslamicDate'
	           ],

	           /**
	            * @cfg {String} format
	            * A formatting string as used by {@link Ext.Date#format} to format a Date for this Column.
	            *
	            * Defaults to the default date from {@link Ext.Date#defaultFormat} which itself my be overridden
	            * in a locale file.
	            */
	           minWidth : 200,
	           resizable : false,
	           initComponent: function()
	           {
	        	   var me = this;

	        	   me.callParent(arguments);
	        	   if(!me.format)
	        	   {
	        		   me.format = Ext.Date.defaultFormat;
	        	   }
	        	   me.renderer = Gam.util.Format.dateRenderer(me.format);
	           }
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 5/16/12
 * Time: 6:41 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.column.LongText
 * @extends Ext.grid.column.Column
 */
Ext.define('Gam.grid.column.LongText', {
	extend: 'Ext.grid.column.Column',
	alias: 'widget.gam.longtextcolumn',

	/**
	 * Generates the HTML to be rendered in the injected checkbox column for each row.
	 * Creates the standard checkbox markup by default; can be overridden to provide custom rendering.
	 * See {@link Ext.grid.column.Column#renderer} for description of allowed parameters.
	 */
	renderer: function(value, metaData, record, rowIndex, colIndex, store, view)
	{
		metaData.tdCls = Ext.baseCSSPrefix + 'grid-cell-long-text';
		return value;
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/13/11
 * Time: 8:44 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.column.Action
 * @extends Ext.grid.column.Action
 */
Ext.define('Gam.grid.column.Action', {
	extend: 'Ext.grid.column.Action',

	alias: 'widget.gam.actioncolumn',

	resizable: false,

	menuDisabled: true,

	hideable: false,

	draggable: false,

	statics: {
		shortcuts: {
			'edit': {
				tooltip: Gam.Resource.tip.edit,
				action: 'edit',
				getClass: function(value, metaData, record, rowIndex, colIndex, store)
				{
					return 'grid-edit-action-icon';
				}
			},
			'view': {
				tooltip: Gam.Resource.tip.view,
				action: 'view',
				getClass: function(value, metaData, record, rowIndex, colIndex, store)
				{
					return 'grid-view-action-icon';
				}
			},
			'viewDetailDialog': {
				tooltip: Gam.Resource.tip.viewLog,
				action: 'viewDetailDialog',
				getClass: function(value, metaData, record, rowIndex, colIndex, store)
				{
					return 'grid-view-log-action-icon';
				}
			}
		}
	},

	constructor: function(config)
	{
		config = config || {};

		var me = this;

		me.baseStateId = config.baseStateId;
		me.lookupActions(config.items);
		delete config.baseStateId;
		config.width = 25 * (config.maxVisibleCount || config.items.length);

		me.callParent([config]);
	},

	editRenderer: function()
	{
		return '&#160;';
	},

	lookupActions: function(items)
	{
		var me = this,
		A = Gam.grid.column.Action,
		stateId,
		state;

		Ext.Array.forEach(items, function(actions, index)
				{
			if(typeof actions == 'string')
			{
				actions = actions.split(Gam.GlobalConfiguration.viewTypeSeparator);

				items[index] = Ext.apply({}, A.shortcuts[actions[0]]);

				if(actions.length > 1)
				{
					items[index].viewType = actions[1];
				}
			}
			if(!items[index].stateId && me.baseStateId &&
					(me.stateful || items[index].stateful || me.stateManagement !== false || items[index].stateManagement !== false))
			{
				items[index].stateId = me.baseStateId + Ext.String.capitalize(items[index].action);
			}

			stateId = items[index].stateId;
			if(!stateId)
			{
				return
			}
			state = Ext.state.Manager.get(stateId);
			if(state && state.hidden)
			{
				delete items[index];
				index--;
			}
			Ext.apply(items[index], state);
				});
		delete me.baseStateId;
	},

	/**
	 * @private
	 * Process and refire events routed from the GridView's processEvent method.
	 * Also fires any configured click handlers. By default, cancels the mousedown event to prevent selection.
	 * Returns the event handler's status to allow canceling of GridView's bubbling process.
	 */
	processEvent: function(type, view, cell, recordIndex, cellIndex, e, record, row)
	{
		var me = this,
		match = e.getTarget().className.match(me.actionIdRe),
		item, fn;

		if(match)
		{
			item = me.items[parseInt(match[1], 10)];
			if(item && type == 'click' && !item.disabled)
			{
				this.fireEvent('action' + type, view, recordIndex, cellIndex, item, e, record, row);
			}
		}

		return me.callParent(arguments);
	},

	/**
	 * Enables this ActionColumn's action at the specified index.
	 * @param {Number/Ext.grid.column.Action} index
	 * @param {Boolean} [silent=false]
	 */
	enableAction: function(index, silent)
	{
		var me = this,
		item;

		if(!index)
		{
			index = 0;
		}
		else if(!Ext.isNumber(index))
		{
			index = Ext.Array.indexOf(me.items, index);
		}

		item = me.items[index];
		if(Gam.security.Acl.isChangeable(item.stateId, Gam.security.Acl.attributeNames.DISABLED))
		{
			me.callParent([index, silent])
		}
	},

	setWidthByCount: function(count)
	{
		this.setWidth(25 * Math.max(config.maxVisibleCount, count));
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/20/11
 * Time: 7:49 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.selection.CheckboxModel
 * @extends Ext.selection.CheckboxModel
 */
Ext.define('Gam.selection.CheckboxModel', {
	extend: 'Ext.selection.CheckboxModel',
	alias: 'selection.gam.checkboxmodel',

	id: 'gamCheckboxSelModel'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/9/11
 * Time: 4:01 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.Panel
 * @extends Ext.grid.Panel
 */
Ext.define('Gam.grid.Panel', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.gam.grid',

	requires: ['Gam.toolbar.Paging'],

	rowNumbererEnabled: true,

	pagingEnabled: true,

	actionColumnType: 'gam.actioncolumn',

	multiSelect: true,

	pageSize: Gam.GlobalConfiguration.GRID_PAGE_SIZE,

	constructor: function(config)
	{
		config = config || {};

		var masterPanel = config.masterPanel || this.masterPanel,
		masterId = masterPanel ? (masterPanel.getId() + '-') : '';

		config.id = masterId + this.getXType();

		this.callParent(arguments);
	},

	initComponent: function()
	{
		var me = this;

		me.columns = Ext.Array.clone(Ext.Array.from(me.columns));

		if(me.rowNumbererEnabled)
		{
			me.columns.unshift(Ext.create('Ext.grid.RowNumberer', { id: me.getId() + '-rowNumberer' , resizable : true , align: 'right'}));
		}

		me.initActions();
		me.initContextMenu();
		me.initTopToolbar();
		me.initActionColumn();
		me.initStore();
		me.initBottomToolbar();

		me.callParent();
	},

	initEvents: function()
	{
		var me = this;

		me.callParent();

		if(!Ext.isDefined(me.masterPanel) && me.storeAutoLoad !== false)
		{
			Ext.defer(me.store.load, 10, me.store, [undefined]);
		}

		me.getSelectionModel().on({
			selectionchange: me.onSelectionChange,
			scope: me
		});
	},

	initActions: function()
	{
		var me = this,
		actions = me.actions,
		arrayActions;

		if(actions)
		{
			if(!Ext.isObject(actions))
			{
				arrayActions = Ext.Array.clone(actions);
				actions = {};
				var actionType;
				Ext.Array.forEach(arrayActions, function(action)
						{
					if(Ext.isString(action))
					{
						actionType = action.indexOf(Gam.GlobalConfiguration.viewTypeSeparator) != -1 ?
								action.substring(0, action.indexOf(Gam.GlobalConfiguration.viewTypeSeparator)) :
									action;
								actions[actionType] = action;
					}
					else if(!action.isAction)
					{
						actions[action.type] = action;
					}
					else
					{
						actionType = action.getXType();
						actions[actionType.substring(actionType.indexOf('.') + 1, actionType.length)] = action;
					}
						});
			}

			Ext.iterate(actions, function(key, value)
					{
				if(Ext.isString(value))
				{
					value = value.split(Gam.GlobalConfiguration.viewTypeSeparator);

					actions[key] = {
							type: value[0]
					};

					if(value.length > 1)
					{
						actions[key].viewType = value[1];
					}
				}

				if(!actions[key].isActin)
				{
					Ext.applyIf(actions[key], {
						handler: Ext.pass(me.controller['do' + Ext.String.capitalize(key.substring(key.indexOf('.') + 1, key.length))], [me]),
						scope: me.controller,
						baseStateId: me.stateId
					});
					actions[key] = Ext.createByAlias('action.' + actions[key].type.toLowerCase(),
							actions[key]
					);
				}
					});

			me.actions = actions;
		}
	},

	initContextMenu: function(subMenu)
	{
		var me = this,
		menu = subMenu || me.contextMenu,
		action;

		if(!menu || menu.isMenu)
		{
			return;
		}

		menu = Ext.Array.clone(menu);
		menu = {
				xtype: 'gam.menu',
				items: menu
		};

		Ext.Array.each(menu.items, function(item, i)
				{
			if(Ext.isString(item))
			{
				action = me.actions[item];
				if(action)
				{
					menu.items[i] = action;
				}
			}
			if(Ext.isObject(item) && item.menu && !item.menu.isMenu)
			{
				item.menu = me.initContextMenu(item.menu);
			}
				});

		if(subMenu)
		{
			return menu;
		}
		me.contextMenu = Ext.widget(menu.xtype, menu);
		me.viewConfig = me.viewConfig || {};
		me.viewConfig = Ext.apply(me.viewConfig, {
			stripeRows: true,
			columnLines: true,
			listeners: {
				itemcontextmenu: function(view, rec, node, index, e)
				{
					e.stopEvent();
					me.contextMenu.showAt(e.getXY());
					return false;
				}
			}
		});
	},

	initTopToolbar: function()
	{
		var me = this,
		action;

		if(!me.tbar || me.tbar.isToolbar)
		{
			return;
		}

		me.tbar = Ext.Array.clone(me.tbar);
		me.tbar = {
				xtype: 'gam.toolbar',
				items: me.tbar
		};

		Ext.Array.each(me.tbar.items, function(item, i)
				{
			if(Ext.isString(item))
			{
				action = me.actions[item];
				if(action)
				{
					me.tbar.items[i] = action;
				}
			}
				});
	},

	initActionColumn: function()
	{
		var me = this;

		if(!me.actionColumnItems)
		{
			return;
		}

		me.actionColumnItems = Ext.Array.from(me.actionColumnItems);
		if(me.actionColumnItems.length > 0)
		{
			me.columns.push({
				id: me.getId() + '-actionColumn',
				xtype: me.actionColumnType,
				items: me.actionColumnItems,
				baseStateId: me.stateId/*,
				 locked: true*/
			});
		}
		delete me.actionColumnType;
		delete me.actionColumnItems;
	},

	initStore: function()
	{
		var me = this;

		if(!me.store.isStore && !me.store.baseUrl && me.controller)
		{
			me.store.baseUrl = me.controller.ns;
		}
		me.store = Ext.data.StoreManager.lookup(me.store);
	},

	initBottomToolbar: function()
	{
		var me = this;

		if(me.pagingEnabled !== false)
		{
			me.bbar = Ext.widget(me.pagingToolbar || 'gam.pagingtoolbar', {
				pageSize: me.pageSize,
				store: me.store,
				displayInfo: true ,
				showFilterCleanerButton : me.plugins!==undefined
			});
		}
		delete me.pagingEnabled;
		delete me.pageSize;
	},

	onSelectionChange: function(sm, selections)
	{
		var me = this;
		Ext.iterate(me.actions, function(key, action)
				{
			if(action.onSelectionChange)
			{
				action.onSelectionChange(sm, selections);
			}
				});
	}
});

/**
 * Created by IntelliJ IDEA.
 * User: Maysam
 * Date: 11/28/11
 * Time: 3:08 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.MasterGridOnDialog
 */
Ext.define('Gam.grid.MasterGridOnDialog', {

	masterOnDialog: true,

	loadByRecord: function(record)
	{
		var me = this,
		baseFilter = Ext.Array.from(me.retrieveBaseFilterFromRecord(record));

		Ext.defer(me.store.filter, 100, me.store, baseFilter);
	},

	retrieveBaseFilterFromRecord: function(record)
	{
		return { property: 'id', value: record.get('id') }
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/23/11
 * Time: 10:58 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.Log
 * @extends Gam.grid.Panel
 */
Ext.define('Gam.grid.Log', {
	extend: 'Gam.grid.Panel',
	alias: 'widget.loggrid',

	mixins: {
		masterGridOnDialog: 'Gam.grid.MasterGridOnDialog'
	},

	statics: {
		specificColumns: [
		                  { header: 'تاریخ ایجاد', dataIndex: 'createDate', width: 130, xtype: 'gam.datecolumn', format: 'Y/m/d - H:m:s', sortable: true },
		                  { header: 'ایجاد کننده', dataIndex: 'perName', sortable: true },
		                  { header: 'نوع تغییر', dataIndex: 'action', sortable: true }
		                  ],

		                  actionRenderer: function(value)
		                  {
		                	  return Gam.Resource.logActions[value.toUpperCase()] || '';
		                  }
	},

	storeAutoLoad: false,

	/**
	 * @cfg {String/Object} logMaster
	 */

	initComponent: function()
	{
		var me = this,
		id = me.getId(),
		logMaster = Ext.ClassManager.getByAlias(
				me.logMaster ? ('widget.' + me.logMaster) : me.self.prototype.alias[0].replace('log', 'grid'));

		me.columns = Ext.Array.merge(logMaster.prototype.columns,
				Gam.grid.Log.specificColumns);
		Ext.Array.forEach(me.columns, function(column)
				{
			if(column.id)
			{
				column.id += '-' + id;
			}
				});

		me.callParent();
	}
}, function()
{
	Gam.grid.Log.specificColumns[2].renderer = Gam.grid.Log.actionRenderer;
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/23/11
 * Time: 10:58 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.Report
 * @extends Gam.grid.Panel
 */
Ext.define('Gam.grid.Report', {
	extend: 'Gam.grid.Panel',
	alias: 'widget.reportgrid',

	storeAutoLoad: false
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/9/11
 * Time: 2:15 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.Crud
 * @extends Gam.grid.Panel
 */
Ext.define('Gam.grid.Crud', {
	extend: 'Gam.grid.Panel',
	alias: 'widget.crudgrid',

	requires: [
	           'Gam.grid.plugin.HeaderFiltering',
	           'Gam.selection.CheckboxModel',
	           'Gam.toolbar.Toolbar'
	           ],

	           selType: 'gam.checkboxmodel',

	           plugins: [
	                     'headerfiltering'
	                     ],

	                     initComponent: function()
	                     {
	                    	 var me = this;

	                    	 me.columns = Ext.Array.clone(Ext.Array.from(me.columns));

	                    	 me.callParent();
	                     },

	                     onHorizontalScroll: function(event, target)
	                     {
	                    	 var owner = this.getScrollerOwner(),
	                    	 items = owner.query('tableview'),
	                    	 center = items[1] || items[0],
	                    	 scrollRight = Ext.fly(target, '_grid_scroller').getScrollRight();

	                    	 center.el.dom.scrollLeft = target.scrollLeft;
	                    	 this.headerCt.el.setScrollRight(scrollRight);
	                    	 this.filteringPlugin.filterbar.el.setScrollRight(scrollRight);
	                     },

	                     afterRender: function(){
	                    	 this.callParent(arguments);
	                    	 this.hideToolbarSeparator();
	                    	 this.hideContextMenuSeparator();
	                     },

	                     hideToolbarSeparator: function() {
	                    	 var dockedItems = this.dockedItems,
	                    	 toolbarIndex = dockedItems.findIndex('xtype', 'gam.toolbar'),
	                    	 toolbar,
	                    	 indices;

	                    	 if (toolbarIndex == -1){
	                    		 return;
	                    	 }
	                    	 toolbar = this.dockedItems.items[toolbarIndex];
	                    	 indices = this.getSeparatorIndices(toolbar, 'tbseparator');

	                    	 if (indices == null){
	                    		 return;
	                    	 }
	                    	 this.checkSeparatorStatus(toolbar, indices);
	                     },

	                     hideContextMenuSeparator: function(){
	                    	 var contextMenu = this.contextMenu,
	                    	 indices;

	                    	 if (contextMenu === undefined) {
	                    		 return;
	                    	 }

	                    	 indices = this.getSeparatorIndices(contextMenu, 'menuseparator');

	                    	 if (indices == null){
	                    		 return;
	                    	 }

	                    	 this.checkSeparatorStatus(contextMenu, indices);

	                     },

	                     getSeparatorIndices: function(container, separatorType){
	                    	 var items = container.items.items,
	                    	 indices= [];

	                    	 for (var i = 0, len = items.length; i < len; i++){

	                    		 if (items[i].xtype === separatorType) {
	                    			 indices.push(i);
	                    		 }
	                    	 }
	                    	 return indices.length != 0 ? indices : null;
	                     },

	                     checkSeparatorStatus: function(container, indices){
	                    	 var containerItems = container.items,
	                    	 leftIndex,
	                    	 rightIndex,
	                    	 lFlag=0,
	                    	 rFlag=0;

	                    	 for (var i = 0; i < indices.length ; i++) {
	                    		 rFlag = 0;
	                    		 lFlag=0;
	                    		 leftIndex= indices[i-1] ? indices[i-1] : 0; //check to see if there is any separator at left
	                    		 rightIndex = indices[i+1] ? indices[i+1] : containerItems.length;

	                    		 //checking rightSide of the separator (considering right to left)
	                    		 for (var k = leftIndex+1 ; k < indices[i]; k++ ){
	                    			 if (containerItems.items[k].hidden == false){
	                    				 rFlag = 1;
	                    				 break;
	                    			 }
	                    		 }

	                    		 //checking leftSide of the separator
	                    		 for (var j = indices[i]+1 ; j < rightIndex; j++ ){
	                    			 if (containerItems.items[j].hidden == false){
	                    				 lFlag = 1;
	                    				 break;
	                    			 }
	                    		 }

	                    		 if (rFlag !=1 || lFlag !=1 ){
	                    			 containerItems.items[indices[i]].hide();
	                    			 indices.splice(i,1);
	                    			 i--;
	                    		 }
	                    	 }
	                     }
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/9/11
 * Time: 2:15 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.grid.RowEditingCrud
 * @extends Gam.grid.Panel
 */
Ext.define('Gam.grid.RowEditingCrud', {
	extend: 'Gam.grid.Crud',
	alias: 'widget.roweditingcrudgrid',

	requires: [
	           'Gam.grid.plugin.RowEditing'
	           ],

	           actionColumns: [
	                           'edit'
	                           ],

	                           constructor: function(config)
	                           {
	                        	   var me = this;

	                        	   me.plugins = Ext.Array.merge(me.plugins, ['gamrowediting']);

	                        	   me.callParent(arguments);
	                           }
});
/**
 * Created by Gam Electronics Co.
 * User: Sina
 * Date: 9/11/11
 * Time: 12:58 PM
 */
Ext.define('Gam.grid.plugin.SearchToolbar', {
	extend: 'Ext.AbstractPlugin',
	alias: 'plugin.searchtoolbar',


	init: function(grid)
	{
		this.grid = grid;
		grid.onRender = Ext.Function.createSequence(grid.onRender, this.onRender, this);

		this.toolbar = Ext.create('Ext.toolbar.Toolbar', {
			width: 500,
			items: [
			        {
			        	xtype: 'combo',
			        	displayField: 'title',
			        	name: 'field1',
			        	emptyText: 'Enter search term',
			        	typeAhead: false,
			        	hideLabel: true,
			        	hideTrigger: true,
			        	width: 200,
			        	anchor: '50%'
			        },
			        {
			        	xtype: 'button',
			        	text: 'Search',
			        	handler: function()
			        	{
			        		//alert('search button is clicked');
			        	}
			        }
			        ]
		});
	},

	onRender: function()
	{
		this.toolbar.render('simpleParameterPanel');
//		this.callParent(arguments);
	}
});

(function()
		{
	/**
	 * @class Gam.form.field.plugin.ClearButton
	 *
	 * Plugin for text components that shows a "clear" button over the text field.
	 * When the button is clicked the text field is set empty.
	 * Icon image and positioning can be controlled using CSS.
	 * Works with Ext.form.field.Text, Ext.form.field.TextArea, Ext.form.field.ComboBox and Ext.form.field.Date.
	 *
	 * Plugin alias is 'clearbutton' (use "plugins: 'clearbutton'" in GridPanel config).
	 *
	 * @author <a href="mailto:stephen.friedrich@fortis-it.de">Stephen Friedrich</a>
	 * @author <a href="mailto:fabian.urban@fortis-it.de">Fabian Urban</a>
	 *
	 * @copyright (c) 2011 Fortis IT Services GmbH
	 * @license Ext.ux.form.field.ClearButton is released under the
	 * <a target="_blank" href="http://www.apache.org/licenses/LICENSE-2.0">Apache License, Version 2.0</a>.
	 *
	 */
	Ext.define('Gam.form.field.plugin.ClearButton', {
		alias: 'plugin.clearbutton',

		/**
		 * @cfg {Boolean} Hide the clear button when the field is empty (default: true).
		 */
		hideClearButtonWhenEmpty: true,

		/**
		 * @cfg {Boolean} Hide the clear button until the mouse is over the field (default: true).
		 */
		hideClearButtonWhenMouseOut: true,

		/**
		 * @cfg {Boolean} When the clear buttons is hidden/shown, this will animate the button to its new state (using opacity) (default: true).
		 */
		animateClearButton: true,

		/**
		 * @cfg {Boolean} Empty the text field when ESC is pressed while the text field is focused.
		 */
		clearOnEscape: true,

		/**
		 * @cfg {String} CSS class used for the button div.
		 * Also used as a prefix for other classes (suffixes: '-mouse-over-input', '-mouse-over-button', '-mouse-down', '-on', '-off')
		 */
		clearButtonCls: 'x-clearbutton',

		/**
		 * The text field (or text area, combo box, date field) that we are attached to
		 */
		textField: null,

		/**
		 * Will be set to true if animateClearButton is true and the browser supports CSS 3 transitions
		 * @private
		 */
		animateWithCss3: false,

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		// Set up and tear down
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////

		constructor: function(cfg)
		{
			Ext.apply(this, cfg);

			this.callParent(arguments);
		},

		/**
		 * Called by plug-in system to initialize the plugin for a specific text field (or text area, combo box, date field).
		 * Most all the setup is delayed until the component is rendered.
		 */
		init: function(textField)
		{
			this.textField = textField;
			if(!textField.rendered)
			{
				textField.on('afterrender', this.handleAfterRender, this);
			}
			else
			{
				// probably an existing input element transformed to extjs field
				this.handleAfterRender();
			}
		},

		/**
		 * After the field has been rendered sets up the plugin (create the Element for the clear button, attach listeners).
		 * @private
		 */
		handleAfterRender: function(textField)
		{
			this.isTextArea = (this.textField.inputEl.dom.type.toLowerCase() == 'textarea');

			this.createClearButtonEl();
			this.addListeners();

			this.repositionClearButton();
			this.updateClearButtonVisibility();

			this.addEscListener();
		},

		/**
		 * Creates the Element and DOM for the clear button
		 */
		createClearButtonEl: function()
		{
			var animateWithClass = this.animateClearButton && this.animateWithCss3;
			this.clearButtonEl = this.textField.bodyEl.createChild({
				tag: 'div',
				cls: this.clearButtonCls
			});
			if(this.animateClearButton)
			{
				this.animateWithCss3 = this.supportsCssTransition(this.clearButtonEl);
			}
			if(this.animateWithCss3)
			{
				this.clearButtonEl.addCls(this.clearButtonCls + '-off');
			}
			else
			{
				this.clearButtonEl.setStyle('visibility', 'hidden');
			}
		},

		/**
		 * Returns true iff the browser supports CSS 3 transitions
		 * @param el an element that is checked for support of the "transition" CSS property (considering any
		 *           vendor prefixes)
		 */
		supportsCssTransition: function(el)
		{
			var styles = ['transitionProperty', 'WebkitTransitionProperty', 'MozTransitionProperty',
			              'OTransitionProperty', 'msTransitionProperty', 'KhtmlTransitionProperty'];

			var style = el.dom.style;
			for(var i = 0, length = styles.length; i < length; ++i)
			{
				if(style[styles[i]] !== 'undefined')
				{
					// Supported property will result in empty string
					return true;
				}
			}
			return false;
		},

		/**
		 * If config option "clearOnEscape" is true, then add a key listener that will clear this field
		 */
		addEscListener: function()
		{
			if(!this.clearOnEscape)
			{
				return;
			}

			// Using a KeyMap did not work: ESC is swallowed by combo box and date field before it reaches our own KeyMap
			this.textField.inputEl.on('keydown',
					function(e)
					{
				if(e.getKey() == Ext.EventObject.ESC)
				{
					if(this.textField.isExpanded)
					{
						// Let combo box or date field first remove the popup
						return;
					}
					// No idea why the defer is necessary, but otherwise the call to setValue('') is ignored
					Ext.Function.defer(this.textField.setValue, 1, this.textField, ['']);
					e.stopEvent();
				}
					},
					this);
		},

		/**
		 * Adds listeners to the field, its input element and the clear button to handle resizing, mouse over/out events, click events etc.
		 */
		addListeners: function()
		{
			// listeners on input element (DOM/El level)
			var textField = this.textField;
			var bodyEl = textField.bodyEl;
			bodyEl.on('mouseover', this.handleMouseOverInputField, this);
			bodyEl.on('mouseout', this.handleMouseOutOfInputField, this);

			// listeners on text field (component level)
			textField.on('destroy', this.handleDestroy, this);
			textField.on('resize', this.repositionClearButton, this);
			textField.on('change', function()
					{
				this.repositionClearButton();
				this.updateClearButtonVisibility();
					}, this);

			// listeners on clear button (DOM/El level)
			var clearButtonEl = this.clearButtonEl;
			clearButtonEl.on('mouseover', this.handleMouseOverClearButton, this);
			clearButtonEl.on('mouseout', this.handleMouseOutOfClearButton, this);
			clearButtonEl.on('mousedown', this.handleMouseDownOnClearButton, this);
			clearButtonEl.on('mouseup', this.handleMouseUpOnClearButton, this);
			clearButtonEl.on('click', this.handleMouseClickOnClearButton, this);
		},

		/**
		 * When the field is destroyed, we also need to destroy the clear button Element to prevent memory leaks.
		 */
		handleDestroy: function()
		{
			this.clearButtonEl.destroy();
		},

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		// Mouse event handlers
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////

		/**
		 * Tada - the real action: If user left clicked on the clear button, then empty the field
		 */
		handleMouseClickOnClearButton: function(event, htmlElement, object)
		{
			if(!this.isLeftButton(event))
			{
				return;
			}
			this.textField.setValue('');
			this.textField.focus();
		},

		handleMouseOverInputField: function(event, htmlElement, object)
		{
			this.clearButtonEl.addCls(this.clearButtonCls + '-mouse-over-input');
			if(event.getRelatedTarget() == this.clearButtonEl.dom)
			{
				// Moused moved to clear button and will generate another mouse event there.
				// Handle it here to avoid duplicate updates (else animation will break)
				this.clearButtonEl.removeCls(this.clearButtonCls + '-mouse-over-button');
				this.clearButtonEl.removeCls(this.clearButtonCls + '-mouse-down');
			}
			this.updateClearButtonVisibility();
		},

		handleMouseOutOfInputField: function(event, htmlElement, object)
		{
			this.clearButtonEl.removeCls(this.clearButtonCls + '-mouse-over-input');
			if(event.getRelatedTarget() == this.clearButtonEl.dom)
			{
				// Moused moved from clear button and will generate another mouse event there.
				// Handle it here to avoid duplicate updates (else animation will break)
				this.clearButtonEl.addCls(this.clearButtonCls + '-mouse-over-button');
			}
			this.updateClearButtonVisibility();
		},

		handleMouseOverClearButton: function(event, htmlElement, object)
		{
			event.stopEvent();
			if(this.textField.bodyEl.contains(event.getRelatedTarget()))
			{
				// has been handled in handleMouseOutOfInputField() to prevent double update
				return;
			}
			this.clearButtonEl.addCls(this.clearButtonCls + '-mouse-over-button');
			this.updateClearButtonVisibility();
		},

		handleMouseOutOfClearButton: function(event, htmlElement, object)
		{
			event.stopEvent();
			if(this.textField.bodyEl.contains(event.getRelatedTarget()))
			{
				// will be handled in handleMouseOverInputField() to prevent double update
				return;
			}
			this.clearButtonEl.removeCls(this.clearButtonCls + '-mouse-over-button');
			this.clearButtonEl.removeCls(this.clearButtonCls + '-mouse-down');
			this.updateClearButtonVisibility();
		},

		handleMouseDownOnClearButton: function(event, htmlElement, object)
		{
			if(!this.isLeftButton(event))
			{
				return;
			}
			this.clearButtonEl.addCls(this.clearButtonCls + '-mouse-down');
		},

		handleMouseUpOnClearButton: function(event, htmlElement, object)
		{
			if(!this.isLeftButton(event))
			{
				return;
			}
			this.clearButtonEl.removeCls(this.clearButtonCls + '-mouse-down');
		},

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		// Utility methods
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////

		/**
		 * Repositions the clear button element based on the textfield.inputEl element
		 * @private
		 */
		repositionClearButton: function()
		{
			var clearButtonEl = this.clearButtonEl;
			if(!clearButtonEl)
			{
				return;
			}
			var left = 0;
			if(this.fieldHasScrollBar())
			{
				left += Ext.getScrollBarWidth();
			}
			if(this.textField.triggerWrap)
			{
				left += this.textField.getTriggerWidth();
			}
			clearButtonEl.alignTo(this.textField.bodyEl, 'tl-tl', [-1 * (left + 3), 5]);
		},

		/**
		 * Calculates the position of the clear button based on the textfield.inputEl element
		 * @private
		 */
		calculateClearButtonPosition: function(textField)
		{
			var positions = textField.inputEl.getBox(true, true);
			var top = positions.y;
			var left = positions.x;
			if(this.fieldHasScrollBar())
			{
				left += Ext.getScrollBarWidth();
			}
			if(this.textField.triggerWrap)
			{
				left += this.textField.getTriggerWidth();
			}
			return {
				left: left,
				top: top
			};
		},

		/**
		 * Checks if the field we are attached to currently has a scrollbar
		 */
		fieldHasScrollBar: function()
		{
			if(!this.isTextArea)
			{
				return false;
			}

			var inputEl = this.textField.inputEl;
			var overflowY = inputEl.getStyle('overflow-y');
			if(overflowY == 'hidden' || overflowY == 'visible')
			{
				return false;
			}
			if(overflowY == 'scroll')
			{
				return true;
			}
			//noinspection RedundantIfStatementJS
			if(inputEl.dom.scrollHeight <= inputEl.dom.clientHeight)
			{
				return false;
			}
			return true;
		},


		/**
		 * Small wrapper around clearButtonEl.isVisible() to handle setVisible animation that may still be in progress.
		 */
		isButtonCurrentlyVisible: function()
		{
			if(this.animateClearButton && this.animateWithCss3)
			{
				return this.clearButtonEl.hasCls(this.clearButtonCls + '-on');
			}

			// This should not be necessary (see Element.setVisible/isVisible), but else there is confusion about visibility
			// when moving the mouse out and _quickly_ over then input again.
			var cachedVisible = Ext.core.Element.data(this.clearButtonEl.dom, 'isVisible');
			if(typeof(cachedVisible) == 'boolean')
			{
				return cachedVisible;
			}
			return this.clearButtonEl.isVisible();
		},

		/**
		 * Checks config options and current mouse status to determine if the clear button should be visible.
		 */
		shouldButtonBeVisible: function()
		{
			if(this.hideClearButtonWhenEmpty && Ext.isEmpty(this.textField.getValue()))
			{
				return false;
			}

			var clearButtonEl = this.clearButtonEl;
			//noinspection RedundantIfStatementJS
			if(this.hideClearButtonWhenMouseOut
					&& !clearButtonEl.hasCls(this.clearButtonCls + '-mouse-over-button')
					&& !clearButtonEl.hasCls(this.clearButtonCls + '-mouse-over-input'))
			{
				return false;
			}

			return true;
		},

		/**
		 * Called after any event that may influence the clear button visibility.
		 */
		updateClearButtonVisibility: function()
		{
			var oldVisible = this.isButtonCurrentlyVisible();
			var newVisible = this.shouldButtonBeVisible();

			var clearButtonEl = this.clearButtonEl;
			if(oldVisible != newVisible)
			{
				if(this.animateClearButton && this.animateWithCss3)
				{
					this.clearButtonEl.removeCls(this.clearButtonCls + (oldVisible ? '-on' : '-off'));
					clearButtonEl.addCls(this.clearButtonCls + (newVisible ? '-on' : '-off'));
				}
				else
				{
					clearButtonEl.stopAnimation();
					clearButtonEl.setVisible(newVisible, this.animateClearButton);
				}

				// Set background-color of clearButton to same as field's background-color (for those browsers/cases
				// where the padding-left (see below) does not work)
				clearButtonEl.setStyle('background-color', this.textField.inputEl.getStyle('background-color'));

				// Adjust padding-left of the input tag to make room for the button
				// IE (up to v9) just ignores this and Gecko handles padding incorrectly with  textarea scrollbars
//				if(!(this.isTextArea && Ext.isGecko) && !Ext.isIE)
//				{
				// See https://bugzilla.mozilla.org/show_bug.cgi?id=157846
//				var deltaPaddingLeft = clearButtonEl.getWidth() - this.clearButtonEl.getMargin('r');
//				var currentPaddingLeft = this.textField.inputEl.getPadding('l');
//				var factor = (newVisible ? +1 : -1);
//				this.textField.inputEl.dom.style.paddingLeft = (currentPaddingLeft + factor * deltaPaddingLeft) + 'px';
//				}
			}
		},

		isLeftButton: function(event)
		{
			return event.button === 0;
		}
	});
		})();

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/6/11
 * Time: 2:22 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.FieldSet
 * @extends Ext.form.FieldSet
 */
Ext.onReady(function()
		{

	Ext.apply(Ext.form.field.VTypes, {
		daterange: function(val, field)
		{
			var date = field.parseDate(val);

			if(!date)
			{
				return false;
			}
			if(field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime())))
			{
				var start = field.up('form').down('#' + field.startDateField);
				start.setMaxValue(date);
				start.validate();
				this.dateRangeMax = date;
			}
			else if(field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime())))
			{
				var end = field.up('form').down('#' + field.endDateField);
				end.setMinValue(date);
				end.validate();
				this.dateRangeMin = date;
			}
			/*
			 * Always return true since we're only using this vtype to set the
			 * min/max allowed values (these are tested for after the vtype test)
			 */
			return true;
		},

		daterangeText: 'تاریخ شروع باید کمتر از تاریخ پایان باشد',

		password: function(val, field)
		{
			if(field.initialPassField)
			{
				var pwd = field.up('form').down('#' + field.initialPassField);
				return (val == pwd.getValue());
			}
			return true;
		},

		passwordText: 'کلمات عبور همخوانی ندارند'
	});
		});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 8/23/11
 * Time: 2:07 PM
 */

Ext.define('Gam.form.field.Autocomplete', {
	extend: 'Ext.form.field.ComboBox',
	requires: [
	           'Ext.util.DelayedTask',
	           'Ext.EventObject',
	           'Ext.data.StoreManager',
	           'Gam.autocomplete.BoundList',
	           'Gam.view.AutocompleteBoundListKeyNav'
	           ],
	           alias: 'widget.autocomplete',

	           /**
	            *
	            */
	           pageSize: 15,

	           /**
	            *
	            */
	           displayField: 'acName',

	           /**
	            *
	            */
	           defaultListConfig: {
	        	   emptyText: '',
	        	   loadingText: Gam.Resource.message.info.loadingText,
	        	   loadingHeight: 70,
	        	   minWidth: 300,
	        	   maxHeight: 500,
	        	   shadow: 'sides'
	           },

	           /**
	            *
	            */
	           initComponent: function()
	           {
	        	   var me = this,
	        	   isDefined = Ext.isDefined;

	        	   if(me.hasHiddenName())
	        	   {
	        		   me.valueField = 'acId';
	        	   }

	        	   if(me.multiSelect)
	        	   {
	        		   me.defaultListConfig.minWidth = 350;
	        	   }

	        	   this.addEvents(
	        			   'autocompletclear',
	        			   /**
	        			    * @event autocompleteselect
	        			    * Fires after the picker has collapsed and there be some selected items
	        			    * @param {Gam.form.field.Autocomplete} autocomplete This autocomplete box
	        			    * @param {Array} records The selected records
	        			    */
	        			   'autocompleteselect'
	        	   );

	        	   if(!isDefined(me.queryDelay))
	        	   {
	        		   me.queryDelay = 500;
	        	   }
	        	   if(!isDefined(me.minChars))
	        	   {
	        		   me.minChars = 0;
	        	   }

	        	   me.callParent(arguments);
	           },

	           /**
	            *
	            * @return {String}
	            */
	           getHiddenName: function()
	           {
	        	   return this.hiddenName;
	           },

	           /**
	            *
	            * @return {Boolean}
	            */
	           hasHiddenName: function()
	           {
	        	   return this.hiddenName != '';
	           },

	           /**
	            *
	            */
	           beforeBlur: function()
	           {
	        	   this.doQueryTask.cancel();
	        	   this.assertValue();
	           },

	           // private
	           assertValue: function()
	           {
	        	   var me = this,
	        	   value = me.getRawValue(),
	        	   rec;

	        	   if(me.hasHiddenName())
	        	   {
	        		   {
	        			   me.setValue(value.length > 0 ? me.lastSelection : {});
	        			   if(value.length == 0)
	        			   {
	        				   me.fireEvent('autocompleteclear', me);
	        			   }
	        		   }
	        	   }
	        	   me.collapse();
	           },

	           /**
	            *
	            */
	           onLoad: function()
	           { 
	        	   var me = this;

	        	   --me.ignoreSelection;
	        	   // If performing a remote query upon the raw value...
	        	   if(me.rawQuery)
	        	   {
	        		   me.rawQuery = false;
	        		   me.syncSelection();
	        		   if(me.picker && !me.picker.getSelectionModel().hasSelection())
	        		   {
	        			   me.doAutoSelect();
	        		   }
	        	   }
	        	   // If store initial load or triggerAction: 'all' trigger click.
	        	   else
	        	   {
	        		   // There's no value.
	        		   // Highlight the first item in the list if autoSelect: true
	        		   if(me.store.getCount())
	        		   {
	        			   me.doAutoSelect();
	        		   }
	        	   }
	           },

	           /**
	            * @private
	            * Execute the query with the raw contents within the textfield.
	            */
	           doRawQuery: function()
	           {
	        	   var rawValue = this.getRawValue();
	        	   this.doQuery(rawValue, rawValue.length == 0, true);
	           },

	           /**
	            * Executes a query to filter the dropdown list. Fires the {@link #beforequery} event prior to performing the query
	            * allowing the query action to be canceled if needed.
	            *
	            * @param {String} queryString The SQL query to execute
	            * @param {Boolean} forceAll `true` to force the query to execute even if there are currently fewer characters in
	            * the field than the minimum specified by the `{@link #minChars}` config option. It also clears any filter
	            * previously saved in the current store (defaults to `false`)
	            * @param {Boolean} rawQuery Pass as true if the raw typed value is being used as the query string. This causes the
	            * resulting store load to leave the raw value undisturbed.
	            * @return {Boolean} true if the query was permitted to run, false if it was cancelled by a {@link #beforequery}
	            * handler.
	            */
	           doQuery: function(queryString, forceAll, rawQuery)
	           {
	        	   queryString = queryString || '';

	        	   // store in object and pass by reference in 'beforequery'
	        	   // so that client code can modify values.
	        	   var me = this,
	        	   qe = {
	        			   query: queryString,
	        			   forceAll: forceAll,
	        			   autocomplete: me,
	        			   cancel: false
	        	   },
	        	   store = me.store;

	        	   if(me.fireEvent('beforequery', qe) === false || qe.cancel)
	        	   {
	        		   return false;
	        	   }

	        	   // get back out possibly modified values
	        	   queryString = qe.query;
	        	   forceAll = qe.forceAll;

	        	   // query permitted to run
	        	   if(forceAll || (queryString.length >= me.minChars))
	        	   {
	        		   // expand before starting query so LoadMask can position itself correctly
	        		   me.expand();

	        		   // make sure they aren't querying the same thing
	        		   me.lastQuery = queryString;

	        		   // Set flag for onLoad handling to know how the Store was loaded
	        		   me.rawQuery = rawQuery;

	        		   /*var simpleToolbar = me.picker.down('toolbar#simpleAutocompleteToolbar');
			if(simpleToolbar)
			{ simpleToolbar.setVisible(queryString.length == 0 && me.multiSelect);}
			me.picker.down('toolbar#pagingAutocompleteToolbar').setVisible(queryString.length != 0);*/

	        		   me.ignoreSelection++;
	        		   me.picker.getSelectionModel().deselectAll();
	        		   me.ignoreSelection--;
	        		   // In queryMode: 'remote', we assume Store filters are added by the developer as remote filters,
	        		   // and these are automatically passed as params with every load call, so we do *not* call clearFilter.
	        		   if(me.pageSize)
	        		   {
	        			   // if we're paging, we've changed the query so start at page 1.
	        			   me.loadPage(1);
	        		   }
	        		   else
	        		   {
	        			   store.load({
	        				   params: { query: rawQuery }
	        			   });
	        		   }
	        	   }
	        	   return true;
	           },

	           /**
	            * @private
	            * If the autoSelect config is true, and the picker is open, highlights the first item.
	            */
	           doAutoSelect: function()
	           {
	        	   var me = this,
	        	   picker = me.picker;

	        	   if(picker && me.autoSelect && me.picker.view.store.getCount() > 0)
	        	   {
	        		   // Highlight the last selected item and scroll it into view
	        		   picker.getSelectionModel().highlightLastHighlighted();
	        	   }
	        	   else
	        	   {
	        		   me.collapse();
	        	   }
	           },

	           /**
	            *
	            */
	           onTriggerClick: function()
	           {
	        	   var me = this;
	        	   if(!me.readOnly && !me.disabled)
	        	   {
	        		   if(me.isExpanded)
	        		   {
	        			   me.collapse();
	        		   }
	        		   else
	        		   {
	        			   me.onFocus({});
	        			   me.doRawQuery();
	        		   }
	        		   me.inputEl.focus();
	        	   }
	           },

	           /**
	            *
	            * @return {*}
	            */
	           createPicker: function()
	           {
	        	   var me = this,
	        	   picker,
	        	   menuCls = Ext.baseCSSPrefix + 'menu',
	        	   opts = Ext.apply({
	        		   pickerField: me,
	        		   selModel: {
	        			   mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'
	        		   },
	        		   floating: true,
	        		   hidden: true,
	        		   ownerCt: me.ownerCt,
	        		   cls: me.el.up('.' + menuCls) ? menuCls : '',
	        				   store: me.store,
	        				   displayField: me.displayField,
	        				   focusOnToFront: false,
	        				   pageSize: me.pageSize,
	        				   tpl: me.tpl,
	        				   rootVisible: me.rootVisible || false
	        	   }, me.listConfig, me.defaultListConfig);

	        	   picker = me.picker = new Gam.autocomplete.BoundList(opts);
	        	   if(me.pageSize)
	        	   {
	        		   me.mon(picker.down('toolbar'), 'beforechange', me.onPageChange, me);
	        	   }

	        	   me.mon(picker, {
	        		   refresh: me.onListRefresh,
	        		   scope: me
	        	   });

	        	   me.mon(picker.getSelectionModel(), {
	        		   'beforeselect': me.onBeforeSelect,
	        		   'beforedeselect': me.onBeforeDeselect,
	        		   'selectionchange': me.onListSelectionChange,
	        		   scope: me
	        	   });

	        	   Ext.Array.forEach(picker.query('toolbar button[action=ok]'), function(button)
	        			   {
	        		   me.mon(button, 'click', me.onOkClick, me);
	        			   });

	        	   return picker;
	           },

	           /**
	            *
	            */
	           alignPicker: function()
	           {
	        	   var me = this,
	        	   picker = me.picker,
	        	   heightAbove = me.getPosition()[1] - Ext.getBody().getScroll().top,
	        	   heightBelow = Ext.Element.getViewHeight() - heightAbove - me.getHeight(),
	        	   space = Math.max(heightAbove, heightBelow);

	        	   if(me.isExpanded)
	        	   {
	        		   picker = me.getPicker();
	        		   // Auto the height (it will be constrained by min and max width) unless there are no records to display.
	        		   picker.setSize(me.bodyEl.getWidth(), 250);
	        		   if(picker.isFloating())
	        		   {
	        			   me.doAlign();
	        		   }
	        	   }

	        	   if(picker.getHeight() > (space - 5))
	        	   {
	        		   picker.setHeight(space - 5); // have some leeway so we aren't flush against
	        		   me.doAlign();
	        	   }
	           },

	           /**
	            *
	            * @param list
	            * @param selectedRecords
	            */
	           onListSelectionChange: function(list, selectedRecords)
	           {
	        	   var me = this,
	        	   isMulti = me.multiSelect,
	        	   hasRecords = selectedRecords.length > 0;
	        	   // Only react to selection if it is not called from setValue, and if our list is
	        	   // expanded (ignores changes to the selection model triggered elsewhere)
	        	   if(!me.ignoreSelection && me.isExpanded)
	        	   {
	        		   if(!isMulti)
	        		   {
	        			   Ext.defer(me.collapse, 1, me);
	        		   }
	        		   if(hasRecords)
	        		   {
	        			   me.fireEvent('select', me, selectedRecords);
	        		   }
	        		   me.inputEl.focus();
	        	   }
	           },

	           /**
	            *
	            */
	           onOkClick: function()
	           {
	        	   this.collapse();
	           },

	           /**
	            * @private
	            * Enables the key nav for the BoundList when it is expanded.
	            */
	           onExpand: function()
	           {
	        	   var me = this,
	        	   keyNav = me.listKeyNav,
	        	   selectOnTab = me.selectOnTab,
	        	   picker = me.getPicker();

	        	   // Handle BoundList navigation from the input field. Insert a tab listener specially to enable selectOnTab.
	        	   if(keyNav)
	        	   {
	        		   keyNav.enable();
	        	   }
	        	   else
	        	   {
	        		   keyNav = me.listKeyNav = new Gam.view.AutocompleteBoundListKeyNav(this.inputEl, {
	        			   boundList: picker,
	        			   forceKeyDown: true,
	        			   tab: function(e)
	        			   {
	        				   if(selectOnTab)
	        				   {
	        					   this.selectHighlighted(e);
	        					   me.triggerBlur();
	        				   }
	        				   // Tab key event is allowed to propagate to field
	        				   return true;
	        			   }
	        		   });
	        	   }

	        	   // While list is expanded, stop tab monitoring from Ext.form.field.Trigger so it doesn't short-circuit selectOnTab
	        	   if(selectOnTab)
	        	   {
	        		   me.ignoreMonitorTab = true;
	        	   }

	        	   Ext.defer(keyNav.enable, 1, keyNav); //wait a bit so it doesn't react to the down arrow opening the picker
	        	   me.inputEl.focus();
	           },

	           /**
	            * TODO: place comfortable doc here
	            */
	           onCollapse: function()
	           {
	        	   var me = this,
	        	   sm = me.picker.getSelectionModel();

	        	   me.callParent();

	        	   if(sm.hasSelection())
	        	   {
	        		   var selection = sm.getSelection();
	        		   if(me.setValue !== Ext.emptyFn)
	        		   { me.setValue(selection);}
	        		   me.fireEvent('autocompleteselect', me, me.multiSelect ? selection : selection[0]);
	        		   if(me.setValue === Ext.emptyFn)
	        		   { me.setRawValue(''); }
	        	   }
	           },

	           /**
	            * Sets the specified value(s) into the field. For each value, if a record is found in the {@link #store} that
	            * matches based on the {@link #valueField}, then that record's {@link #displayField} will be displayed in the
	            * field. If no match is found, and the {@link #valueNotFoundText} config option is defined, then that will be
	            * displayed as the default field text. Otherwise a blank value will be shown, although the value will still be set.
	            * @param {String/String[]} value The value(s) to be set. Can be either a single String or {@link Ext.data.Model},
	            * or an Array of Strings or Models.
	            * @return {Ext.form.field.Field} this
	            */
	           setValue: function(value)
	           {
	        	   var me = this,
	        	   inputEl = me.inputEl,
	        	   i, len, record,
	        	   matchedRecords = [],
	        	   displayTplData = [],
	        	   processedValue = [];

	        	   // This method processes multi-values, so ensure value is an array.
	        	   value = Ext.Array.from(value);

	        	   // Loop through values
	        	   for(i = 0, len = value.length; i < len; i++)
	        	   {
	        		   record = value[i];
	        		   if(record && record.isModel)
	        		   {
	        			   matchedRecords.push(record);
	        			   displayTplData.push(record.data);
	        			   processedValue.push(record.get(me.valueField));
	        		   }
	        		   else if(me.hasHiddenName())
	        		   {
	        			   matchedRecords.push(record);
	        			   displayTplData.push({ acName: value[i][me.name] || '' });
	        			   processedValue.push(value[i][me.hiddenName] || '' );
	        		   }
	        		   else
	        		   {
	        			   displayTplData.push({ acName: value[i] || '' });
	        			   processedValue.push(value[i] || '' );
	        		   }
	        	   }

	        	   // Set the value of this field. If we are multiselecting, then that is an array.
	        	   me.setHiddenValue(processedValue);
	        	   me.value = me.multiSelect ? processedValue : processedValue[0];
	        	   if(!Ext.isDefined(me.value))
	        	   {
	        		   me.value = null;
	        	   }
	        	   me.displayTplData = displayTplData; //store for getDisplayValue method
	        	   me.lastSelection = matchedRecords;

	        	   if(inputEl && me.emptyText && !Ext.isEmpty(value))
	        	   {
	        		   inputEl.removeCls(me.emptyCls);
	        	   }

	        	   // Calculate raw value from the collection of Model data
	        	   me.setRawValue(me.getDisplayValue());
	        	   me.checkChange();

	        	   me.applyEmptyText();

	        	   return me;
	           },

	           getModelData: function()
	           {
	        	   var me = this,
	        	   data = null;
	        	   if(!me.disabled)
	        	   {
	        		   data = {};
	        		   if(me.hasHiddenName())
	        		   {
	        			   data[me.getName()] = me.displayTplData[0] ? me.displayTplData[0].acName : null;
	        			   data[me.getHiddenName()] = me.getValue();
	        		   }
	        		   else
	        		   { data[me.getName()] = me.getValue(); }
	        	   }
	        	   return data;
	           }
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/3/11
 * Time: 6:11 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.field.Display
 * @extends Ext.form.field.Display
 */
Ext.define('Gam.form.field.Display', {
	extend: 'Ext.form.field.Display',
	alias: 'widget.gamdisplayfield',

	height: 22,

	style: {
		height: '22px'
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 12/3/11
 * Time: 6:11 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.field.DateDisplay
 * @extends Gam.form.field.Display
 */
Ext.define('Gam.form.field.DateDisplay', {
	extend: 'Gam.form.field.Display',
	alias: 'widget.datedisplayfield',

	initComponent: function()
	{
		var me = this;

		me.callParent(arguments);

		if(!me.format)
		{
			me.format = Ext.Date.defaultFormat;
		}
		me.renderer = Gam.util.Format.dateRenderer(me.format);
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 6/13/12
 * Time: 7:43 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.field.Grid
 * @extends Gam.grid.Panel
 */
Ext.define('Gam.form.field.Grid', {
	extend: 'Gam.grid.Panel',
	mixins: {
		labelable: 'Ext.form.Labelable',
		field: 'Ext.form.field.Field'
	},
	alias: 'widget.gam.gridfield',

	initComponent: function()
	{
		var me = this;

		// Init mixins
		me.initLabelable();
		me.initField();

		me.callParent();
	},

	getValue: function()
	{

	},

	setValue: function(value) {
		var me = this;
		me.value = value;
		me.checkChange();
		return me;
	},

	isDirty : function() {
		var me = this;
		return !me.disabled && me.store.isDirty();
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 2:56 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.field.ComboBox
 * @extends Ext.form.field.ComboBox
 */
Ext.define('Gam.form.field.comboBox.ComboBox', {
	extend: 'Ext.form.field.ComboBox',
	alias: 'widget.gamcombobox',

	typeAhead: true,

	forceSelection: true,

	queryMode: 'local',

	triggerAction: 'all',

	valueField: 'id',

	displayField: 'name',

	emptyText: Gam.Resource.comboEmptyText,

	initComponent: function()
	{
		var me = this;

		me.submitValue = Ext.isDefined(me.name) || Ext.isDefined(me.hiddenName);

		me.callParent();
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 3:09 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.field.comboBox.Local
 * @extends Gam.form.field.comboBox.ComboBox
 */
Ext.define('Gam.form.field.comboBox.Local', {
	extend: 'Gam.form.field.comboBox.ComboBox',


	// improving ComboBox behaviour : Combobox can be cleared using BackSpace or Delete Keys both
	onKeyUp: function(e, t) {
		var me = this,
		key = e.getKey();

		if (!me.readOnly && !me.disabled && me.editable) {
			me.lastKey = key;
			var keyCombination = !e.isSpecialKey() || key == e.BACKSPACE || key == e.DELETE ,
			fieldIsEmpty= me.getRawValue()=='';
			if ( keyCombination ) {
				if(!fieldIsEmpty){
					me.doQueryTask.delay(me.queryDelay);
				}
				else {
					me.clearValue();
					me.collapse() ;
				}
			}
		}
		if (me.enableKeyEvents) {
			me.callParent(arguments);
		}
	}

});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 3:09 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.field.comboBox.Remote
 * @extends Gam.form.field.comboBox.ComboBox
 */
Ext.define('Gam.form.field.comboBox.Remote', {
	extend: 'Gam.form.field.comboBox.ComboBox'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/6/11
 * Time: 2:22 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.FieldSet
 * @extends Ext.form.FieldSet
 */
Ext.define('Gam.form.FieldSet', {
	extend: 'Ext.form.FieldSet',
	alias: 'widget.gam.fieldset',

	dirty: false,

	padding: 5,

	margin: '5 5 0 5',

	initComponent: function()
	{ 
		var me = this,
		readOnly = me.isReadOnly();

		me.items = me['get' + (readOnly ? 'ReadOnly' : 'Editable') + 'Fields']();
		me.autoHeight = !Ext.isDefined(me.height);
		me.originalTitle = me.title;
		me.defaultType = (me.specified === true ?
				me.defaultType :
					(readOnly ? 'gamdisplayfield' : 'textfield'));

		me.callParent();
	},

	getFields: function()
	{
		var me = this,
		fields = new Ext.util.MixedCollection();

		fields.addAll(me.query('[isFormField]'));

		return fields;
	},

	isReadOnly: function()
	{
		return Ext.isDefined(this.readOnly) ? this.readOnly :
			Gam.GlobalConfiguration.IS_READONLY_ACTION[this.actionType];
	},

	isAddAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD;
	},

	isEditAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.EDIT;
	},

	isViewAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.VIEW;
	},

	/**
	 * Returns true if client-side validation on the form is successful. Any invalid fields will be
	 * marked as invalid. If you only want to determine overall form validity without marking anything,
	 * use {@link #hasInvalidField} instead.
	 * @return Boolean
	 */
	isValid: function()
	{
		var me = this,
		invalid;
		Ext.suspendLayouts();
		invalid = me.getFields().filterBy(function(field)
				{
			return !field.validate();
				});
		Ext.resumeLayouts(true);
		return invalid.length < 1;
	},

	/**
	 * @param form
	 */
	beforeSave: function(form) { /*empty*/ },

	beforeDelete: function() { /*empty*/ },

	getReadOnlyFields: function() { /*empty*/ },

	getEditableFields: function() { /*empty*/ },

	retrieveBaseParams: function(formPanel, record) { /*empty*/ },

	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormLoadSuccess: function(form, action) { /*empty*/ },

	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormLoadFailure: function(form, action) { /*empty*/ },

	/**
	 * @param form
	 * @param action
	 */
	onEditableFormLoadSuccess: function(form, action) { /*empty*/ },

	/**
	 * @param form
	 * @param action
	 */
	onEditableFormLoadFailure: function(form, action) { /*empty*/ },

	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormSaveSuccess: function(form, action) { /*empty*/ },

	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormSaveFailure: function(form, action) { /*empty*/ }
});





/**
 *
 * @author N.Khodayari
 * @class ICT.form.FieldSet
 * @extends widget.gam.fieldset
 */
Ext.define('ICT.form.FieldSet', {
	extend: 'Ext.form.FieldSet',
	alias: 'widget.ictfieldset',
	dirty: false,
	padding: 5,
	margin: '5 5 0 5',
	initComponent: function()
	{

		var me = this,
		readOnly = me.isReadOnly();
		me.items = me['get' + (readOnly ? 'ReadOnly' : 'Editable') + 'Fields']();
		me.autoHeight = !Ext.isDefined(me.height);
		me.originalTitle = me.title;
		me.defaultType = (me.specified === true ?
				me.defaultType :
					(readOnly ? 'gamdisplayfield' : 'textfield'));
		me.callParent();
	},
	//*****************     wrapper   ************************//
	setData : function(record,container){
		var recordData = record.data;
		var allProperties = Object.keys(recordData);
		var items = container.items.keys;
		for(var iProperties = 0; iProperties < allProperties.length ; iProperties++){
			if(items.indexOf(allProperties[iProperties]) != -1){
				container.items.items[items.indexOf(allProperties[iProperties])].setValue(recordData[allProperties[iProperties]].toString());
			}
		}
	},
	//****************************************************************//
	//getItems :function(){ /*empty: just implement it*/},
	getFields: function()
	{
		var me = this,
		fields = new Ext.util.MixedCollection();
		fields.addAll(me.query('[isFormField]'));
		return fields;
	},
	isReadOnly: function()
	{
		return Ext.isDefined(this.readOnly) ? this.readOnly :
			Gam.GlobalConfiguration.IS_READONLY_ACTION[this.actionType];
	},
	isAddAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD;
	},
	isEditAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.EDIT;
	},
	isViewAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.VIEW;
	},
	/**
	 * Returns true if client-side validation on the form is successful. Any invalid fields will be
	 * marked as invalid. If you only want to determine overall form validity without marking anything,
	 * use {@link #hasInvalidField} instead.
	 * @return Boolean
	 */
	isValid: function()
	{
		var me = this,
		invalid;
		Ext.suspendLayouts();
		invalid = me.getFields().filterBy(function(field)
				{
			return !field.validate();
				});
		Ext.resumeLayouts(true);
		return invalid.length < 1;
	},
	/**
	 * @param form
	 */
	beforeSave: function(form) { /*empty*/ },
	beforeDelete: function() { /*empty*/ },
	getReadOnlyFields: function() { /*empty*/ },
	getEditableFields: function() { /*empty*/ },
	retrieveBaseParams: function(formPanel, record) { /*empty*/ },
	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormLoadSuccess: function(form, action) { /*empty*/ },
	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormLoadFailure: function(form, action) { /*empty*/ },
	/**
	 * @param form
	 * @param action
	 */
	onEditableFormLoadSuccess: function(form, action) { /*empty*/ },
	/**
	 * @param form
	 * @param action
	 */
	onEditableFormLoadFailure: function(form, action) { /*empty*/ },
	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormSaveSuccess: function(form, action) { /*empty*/ },
	/**
	 * @param form
	 * @param action
	 */
	onReadOnlyFormSaveFailure: function(form, action) { /*empty*/ }
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/28/11
 * Time: 3:08 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.MasterFormOnDialog
 */
Ext.define('Gam.form.MasterFormOnDialog', {

	masterOnDialog: true,

	loadByRecord: function(record)
	{
		var me = this,
		baseFilter = me.retrieveBaseFilterFromRecord(record);

		//Todo: implement loading here
	},

	retrieveBaseFilterFromRecord: function(record)
	{
		return { id: record.get('id') }
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 5/4/12
 * Time: 3:53 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.Basic
 * @extends Ext.form.Basic
 */
Ext.define('Gam.form.Basic', {
	extend: 'Ext.form.Basic',

	/**
	 * Return all the {@link Ext.form.field.Field} editable components in the owner container.
	 * @return {Ext.util.MixedCollection} Collection of the editable Field objects
	 */
	getEditableFields: function()
	{
		var editableFields = [],
		fields = this.getFields();

		fields.each(function(field)
				{
			if(field.isFocusable())
			{
				editableFields.push(field);
			}
				});

		return editableFields;
	},

	/**
	 * Return the first editable {@link Ext.form.field.Field} component in the owner container.
	 * @return {Ext.form.field.Field} editable field
	 */
	getFirstEditableField: function()
	{
		var firstEditableField,
		fields = this.getFields();

		fields.each(function(field)
				{
			if(field.isFocusable())
			{
				firstEditableField = field;
				return false;
			}
				});

		return firstEditableField;
	},

	/**
	 * Set values for fields in this form in bulk.
	 *
	 * @param {Object/Object[]} values Either an array in the form:
	 *
	 *     [{id:'clientName', value:'Fred. Olsen Lines'},
	 *      {id:'portOfLoading', value:'FXT'},
	 *      {id:'portOfDischarge', value:'OSL'} ]
	 *
	 * or an object hash of the form:
	 *
	 *     {
	 *         clientName: 'Fred. Olsen Lines',
	 *         portOfLoading: 'FXT',
	 *         portOfDischarge: 'OSL'
	 *     }
	 *
	 * @return {Ext.form.Basic} this
	 */
	setValues: function(values, parentObject)
	{
		parentObject = parentObject || '';
		var me = this;

		function setVal(fieldId, val)
		{
			var field = me.findField(parentObject + fieldId);

			if(field)
			{
				if(field.hasHiddenName && field.hasHiddenName())
				{
					var objVal = {};

					objVal[fieldId] = val;
					objVal[field.hiddenName] = values[field.hiddenName];
					val = objVal;
				}
				field.setValue(val);
				if(me.trackResetOnLoad)
				{
					field.resetOriginalValue();
				}
			}
			else if(Ext.isObject(val))
			{
				var _parentObject = parentObject + fieldId + '.';
				me.setValues(val, _parentObject);
			}
		}
		// object hash
		Ext.iterate(values, setVal);

		return this;
	},

	/**
	 * Persists the values in this form into the passed {@link Ext.data.Model} objects in a beginEdit/endEdit block.
	 * If the records is not specified, it will attempt to update (if it exists) the records provided to loadRecords.
	 * @param {Ext.data.Model} [records] The records to edit
	 * @return {Ext.form.Basic} this
	 */
	updateRecords: function(records)
	{
		records = records || this._records;

		var values = Ext.apply(this.getValues(false, true, false, true), this.baseParams);

		Ext.each(records, function(record)
				{
			record.beginEdit();
			record.set(values);
			record.endEdit();
				});

		return this;
	},

	loadRecords: function(records)
	{
		var me = this;

		me._records = Ext.Array.from(records);
		me.owner.retrieveBaseParams(me._records[0]);

		me.loadRecord(me._records[0]);
	},

	bindRecords: function(records)
	{
		var me = this;

		me._records = Ext.Array.from(records);
		me.owner.retrieveBaseParams(me._records[0]);
	},

	/**
	 * Returns the last Ext.data.Model instances that was loaded via {@link #loadRecords}
	 * @return {Ext.data.Model} The record
	 */
	getRecords: function()
	{
		return this._records;
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 10/9/11
 * Time: 3:56 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.form.Panel
 * @extends Ext.form.Panel
 */
Ext.define('Gam.form.Panel', {
	extend: 'Ext.form.Panel',
	alias: 'widget.gam.form',

	requires: ['Gam.form.FieldSet'],

	timeout: 180,

	deferredRender: false,

	fieldDefaults: {
		msgTarget: 'side',
		margin: '5 5 0 10',
		allowBlank: false
	},

	constructor: function(config)
	{
		config = config || {};

		config.trackResetOnLoad = Ext.isBoolean(config.trackResetOnLoad) ? config.trackResetOnLoad : true;

		this.callParent([config]);
	},

	initComponent: function()
	{
		var me = this;

		if(!me.items)
		{
			me.items = Gam.util.Config.decorateConfigArray(me.buildItems(), undefined, { controller: me.controller });
			me.buttons = me.buildButtons();
		}

		me.callParent();
	},

	/**
	 *
	 */
	buildItems: function() {/*empty*/},

	/**
	 *
	 */
	buildButtons: function() { /*empty*/ },

	beforeRender: function()
	{
		this.eachField(function(field)
				{
			field.clearInvalid();
			if(field.allowBlank === false && field.submitValue !== false)
			{
				field.afterLabelTextTpl = Gam.util.Form.requiredTpl;
			}
				});

		this.callParent(arguments);
	},

	/**
	 * @private
	 */
	createForm: function()
	{
		return new Gam.form.Basic(this, Ext.applyIf({listeners: {}}, this.initialConfig));
	},

	eachField: function(fn)
	{
		this.getForm().getFields().each(fn, this);
	},

	eachEditableField: function(fn)
	{
		this.getForm().getEditableFields().each(fn, this);
	},

	setCorrectEntityStates: function()
	{
		var me = this,
		fieldValues = me.form.getFieldValues(true),
		isAddAction = me.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD,
		baseParams = {},
		entities = [];

		Ext.iterate(fieldValues, function(key)
				{
			var indexOfDot = key.indexOf('.'),
			entityName;

			if(indexOfDot != -1)
			{
				return;
			}

			entityName = key.substring(0, indexOfDot);
			if(Ext.Array.indexOf(entities, entityName) != -1)
			{
				return;
			}
			entities.push(entityName);

			baseParams[entityName + '.EntityState'] =
				Gam.GlobalConfiguration.ENTITY_STATES[isAddAction && !(isAddAction && baseParams[entityName + 'Id']) ? 'ADDED' : 'CHANGED'];
				}, this);

		if(Gam.isEmptyObject(baseParams))
		{
			return;
		}

		me.applyBaseParams(baseParams);
	},

	/**
	 *
	 * @param record
	 */
	retrieveBaseParams: function(record)
	{
		this.items.each(function(fieldSet)
				{
			if(fieldSet.is('fieldset'))
			{
				fieldSet.retrieveBaseParams(this, record);
			}
				}, this);
	},

	/**
	 *
	 * @param params
	 */
	applyBaseParams: function(params)
	{
		var me = this;

		me.form.baseParams = me.form.baseParams || {};
		Ext.apply(me.form.baseParams, params);
	},

	/**
	 *
	 */
	beforeSave: function()
	{
		var me = this,
		continueSaving = true;

		me.items.each(function(fieldSet)
				{
			if(Ext.isDefined(fieldSet.beforeSave) && fieldSet.beforeSave() === false)
			{
				return continueSaving = false;
			}
				});

		return continueSaving;
	},

	/**
	 *
	 */
	isDirty: function()
	{
		return this.form.isDirty();
	},

	loadRecords: function(records)
	{
		return this.getForm().loadRecords(records);
	},

	bindRecords: function(records)
	{
		return this.getForm().bindRecords(records);
	},

	/**
	 * Returns the last Ext.data.Model instances that was loaded via {@link #loadRecords}
	 * @return {Ext.data.Model} The record
	 */
	getRecords: function()
	{
		return this.getForm().getRecords();
	},

	isReadOnly: function()
	{
		return Ext.isDefined(this.readOnly) ? this.readOnly :
			Gam.GlobalConfiguration.IS_READONLY_ACTION[this.actionType];
	},

	isAddAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD;
	},

	isEditAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.EDIT;
	},

	isViewAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.VIEW;
	},

	/**
	 * @private
	 * Returns the focus holder element associated with this Window. By default, this is the Window's element.
	 * @returns {Ext.Element/Ext.Component} the focus holding element or Component.
	 */
	getFocusEl: function()
	{
		return this.getDefaultFocus();
	},

	/**
	 * Gets the configured default focus item.  If a {@link #defaultFocus} is set, it will
	 * receive focus when the Window's <code>focus</code> method is called, otherwise the
	 * Window itself will receive focus.
	 */
	getDefaultFocus: function()
	{
		var me = this,
		result,
		selector;

		if(me.isReadOnly())
		{
			return me.el;
		}

		if(me.defaultFocus === undefined)
		{
			result = this.getForm().getFirstEditableField();
		}
		// String is ID or CQ selector
		else if(Ext.isString(me.defaultFocus))
		{
			selector = me.defaultFocus;
			if(selector.substr(0, 1) !== '#')
			{
				selector = '#' + selector;
			}
			result = me.down(selector);
		}
		// Otherwise, if it's got a focus method, use it
		else if(me.defaultFocus.focus)
		{
			result = me.defaultFocus;
		}

		return result;
	},

	/**
	 * @private
	 * Called when a Component's focusEl receives focus.
	 * If there is a valid default focus Component to jump to, focus that,
	 * otherwise continue as usual, focus this Component.
	 */
	onFocus: function()
	{
		var me = this,
		focusDescendant;

		// If the FocusManager is enabled, then we must noy jumpt to focus the default focus. We must focus the Window
		if((Ext.FocusManager && Ext.FocusManager.enabled) || ((focusDescendant = me.getDefaultFocus()) === me))
		{
			me.callParent(arguments);
		}
		else
		{
			focusDescendant.focus();
		}
	}
});


/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 10:28 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.window.Window
 * @extends Ext.window.Window
 */
Ext.define('Gam.window.Window', {
	extend: 'Ext.window.Window',
	alias: 'widget.gamwindow',

	height: 600,

	width: 900,

	minHeight: Gam.GlobalConfiguration.WINDOW_MIN_HEIGHT,

	minWidth: Gam.GlobalConfiguration.WINDOW_MIN_WIDTH,

	initComponent: function()
	{
		var me = this;

		me.height = Math.min(me.height, Ext.Element.getViewHeight() - 30);
		me.width = Math.min(me.width, Ext.Element.getViewWidth() - 30);

		me.callParent();
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 11:25 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.window.dialog.Dialog
 * @extends Gam.window.Window
 */
Ext.define('Gam.window.dialog.Dialog', {
	extend: 'Gam.window.Window',
	alias: 'widget.dialog',

	layout: 'fit',

	modal: true,

	title: 'تعیین نشده',

	initComponent: function()
	{
		var me = this;

		me.items = Gam.util.Config.decorateConfigArray(me.buildItems(), undefined, { controller: me.controller });
		me.buttons = me.buildButtons();

		me.callParent();
	},

	/**
	 *
	 */
	buildItems: function() {/*empty*/},

	/**
	 *
	 */
	buildButtons: function()
	{
		return this.buildCloseButton();
	},

	buildCloseButton: function()
	{
		return [
		        {
		        	xtype: 'closebtn'
		        }
		        ];
	}
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/12/11
 * Time: 11:40 AM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.window.dialog.Entity
 * @extends Gam.window.dialog.Dialog
 */
Ext.define('Gam.window.dialog.Entity', {
	extend: 'Gam.window.dialog.Dialog',
	alias: 'widget.entitydialog',

	requires: 'Ext.form.Panel',

	/**
	 *
	 */
	saveButton: true,
	cancelButton: true,// add by khodayari for hide cancel button on upload file form
	/**
	 *
	 */
	minWidth: Gam.GlobalConfiguration.DIALOG_MIN_WIDTH,

	/**
	 *
	 */
	initComponent: function()
	{
		var me = this;

		if(!me.width || me.width < Gam.GlobalConfiguration.DIALOG_MIN_WIDTH)
		{
			me.width = Gam.GlobalConfiguration.DIALOG_MIN_WIDTH;
		}

		me.callParent();
		me.defaultFocus = me.items.items[0].id;
	},

	/**
	 *
	 * @return {*}
	 */
	isReadOnly: function()
	{
		return Ext.isDefined(this.readOnly) ? this.readOnly :
			Gam.GlobalConfiguration.IS_READONLY_ACTION[this.actionType];
	},

	/**
	 *
	 * @return {Boolean}
	 */
	isAddAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD;
	},

	/**
	 *
	 * @return {Boolean}
	 */
	isEditAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.EDIT;
	},

	/**
	 *
	 * @return {Boolean}
	 */
	isViewAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.VIEW;
	},

	/**
	 *
	 * @return {*}
	 */
	buildItems: function()
	{
		return this.getFormConfig();
	},

	/**
	 *
	 * @return {Object}
	 */
	getFormConfig: function()
	{
		var me = this;

		return {
			xtype: 'gam.form',
			actionType: me.actionType,
			trackResetOnLoad: me.trackResetOnLoad,
			items: Gam.util.Config.decorateConfigArray(me.buildFormItems(), { actionType: me.actionType })
		};
	},

	/**
	 *
	 */
	buildFormItems: function() { /*empty: just implement it*/ },

	/**
	 *
	 * @return {Array}
	 */
	buildButtons: function()
	{
		var me = this,
		buttons = [];

		buttons.push('->');

		if(me.saveButton && !me.isReadOnly())
		{
			buttons.push(me.buildSaveButton());
		}
		if(me.cancelButton)// add by khodayari for hide cancel button on upload file form
			buttons = Ext.Array.merge(buttons, me.callParent(arguments));

		return buttons;
	},

	/**
	 *
	 * @return {Object}
	 */
	buildSaveButton: function()
	{
		return {
			xtype: 'savebtn'
		};
	},

	/**
	 *
	 */
	buildCloseButton: function()
	{
		return this.isViewAction() ? this.callParent(arguments) : { xtype: 'cancelbtn' };
	}
});





/**
 *
 * @author N.Khodayari
 * @class ICT.TabPanel
 * @extends Ext.TabPanel
 */
Ext.define('ICT.TabPanel', {
	extend: 'Ext.TabPanel',
	alias: 'widget.icttabpanel',
	requires: 'Ext.TabPanel',
	/**
	 *
	 */
	saveButton: true,
	/**
	 *
	 */
	minWidth: Gam.GlobalConfiguration.DIALOG_MIN_WIDTH,
	/**
	 *
	 */
	initComponent: function()
	{

		var me = this;
		if(!me.width || me.width < Gam.GlobalConfiguration.DIALOG_MIN_WIDTH)
		{
			me.width = Gam.GlobalConfiguration.DIALOG_MIN_WIDTH;
		}
		if(!me.items)
		{
			me.items = Gam.util.Config.decorateConfigArray(me.getItems(), undefined, { actionType: me.actionType });
		}
		me.callParent();
		me.defaultFocus = me.items.items[0].id;
	},
	/**
	 *
	 * @return {*}
	 */
	isReadOnly: function()
	{
		return Ext.isDefined(this.readOnly) ? this.readOnly :
			Gam.GlobalConfiguration.IS_READONLY_ACTION[this.actionType];
	},
	/**
	 *
	 * @return {Boolean}
	 */
	isAddAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD;
	},
	/**
	 *
	 * @return {Boolean}
	 */
	isEditAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.EDIT;
	},
	/**
	 *
	 * @return {Boolean}
	 */
	isViewAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.VIEW;
	},
	/**
	 *
	 */
	getItems: function() { /*empty: just implement it*/ },
	/**
	 *
	 * @return {Array}
	 */
	buildButtons: function()
	{
		var me = this,
		buttons = [];
		buttons.push('->');
		if(me.saveButton && !me.isReadOnly())
		{
			buttons.push(me.buildSaveButton());
		}
		buttons = Ext.Array.merge(buttons, me.callParent(arguments));
		return buttons;
	},
	/**
	 *
	 * @return {Object}
	 */
	buildSaveButton: function()
	{
		return {
			xtype: 'savebtn'
		};
	},
	/**
	 *
	 */
	buildCloseButton: function()
	{
		return this.isViewAction() ? this.callParent(arguments) : { xtype: 'cancelbtn' };
	}
});



/**
 *
 * @author N.Khodayari
 * @class ICT.Panel
 * @extends Ext.Panel
 */
Ext.define('ICT.Panel', {
	extend: 'Ext.Panel',
	alias: 'widget.ictpanel',
	requires: 'Ext.Panel',
	/**
	 *
	 */
	saveButton: true,
	/**
	 *
	 */
	minWidth: Gam.GlobalConfiguration.DIALOG_MIN_WIDTH,
	/**
	 *
	 */
	initComponent: function()
	{ 
		var me = this;
		if(!me.width || me.width < Gam.GlobalConfiguration.DIALOG_MIN_WIDTH)
		{
			me.width = Gam.GlobalConfiguration.DIALOG_MIN_WIDTH;
		}
		if(!me.items)
		{
			me.items = Gam.util.Config.decorateConfigArray(me.getItems(), undefined, { actionType: me.actionType });
		}
		me.callParent();
		me.defaultFocus = me.items.items[0].id;
	},
	/**
	 *
	 * @return {*}
	 */
	isReadOnly: function()
	{
		return Ext.isDefined(this.readOnly) ? this.readOnly :
			Gam.GlobalConfiguration.IS_READONLY_ACTION[this.actionType];
	},
	/**
	 *
	 * @return {Boolean}
	 */
	isAddAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.ADD;
	},
	/**
	 *
	 * @return {Boolean}
	 */
	isEditAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.EDIT;
	},
	/**
	 *
	 * @return {Boolean}
	 */
	isViewAction: function()
	{
		return this.actionType == Gam.GlobalConfiguration.ACTION_TYPES.VIEW;
	},
	/**
	 *
	 */
	getItems: function() { /*empty: just implement it*/ },
	/**
	 *
	 * @return {Array}
	 */
	buildButtons: function()
	{
		var me = this,
		buttons = [];
		buttons.push('->');
		if(me.saveButton && !me.isReadOnly())
		{
			buttons.push(me.buildSaveButton());
		}
		buttons = Ext.Array.merge(buttons, me.callParent(arguments));
		return buttons;
	},
	/**
	 *
	 * @return {Object}
	 */
	buildSaveButton: function()
	{
		return {
			xtype: 'savebtn'
		};
	},
	/**
	 *
	 */
	buildCloseButton: function()
	{
		return this.isViewAction() ? this.callParent(arguments) : { xtype: 'cancelbtn' };
	}
});

/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/14/11
 * Time: 7:22 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.window.dialog.LocalEntity
 * @extends Gam.window.dialog.Entity
 */
Ext.define('Gam.window.dialog.LocalEntity', {
	extend: 'Gam.window.dialog.Entity',
	alias: 'widget.localentitydialog'
});
/**
 * Created by Gam Electronics Co.
 * User: Maysam
 * Date: 11/14/11
 * Time: 7:22 PM
 */

/**
 *
 * @author S.M Tayyeb
 * @class Gam.window.dialog.RemoteEntity
 * @extends Gam.window.dialog.Entity
 */
Ext.define('Gam.window.dialog.RemoteEntity', {
	extend: 'Gam.window.dialog.Entity',
	alias: 'widget.remoteentitydialog',

	/**
	 *
	 * @param record
	 */
	setBaseParams: function(record) {/*empty*/}
});