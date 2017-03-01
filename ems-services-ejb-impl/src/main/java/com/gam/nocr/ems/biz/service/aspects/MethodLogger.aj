package com.gam.nocr.ems.biz.service.aspects;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.gam.commons.core.BaseLog;
import com.gam.commons.core.data.domain.EntityTO;



/**
 * @author Sina Golesorkhi
 * 
 */

public aspect MethodLogger {

	static final Logger logger = BaseLog.getLogger(MethodLogger.class);

	pointcut methodExecution() : 
		execution(* *(..)) && (within(com.gam.nocr.ems.biz.service.internal.impl.*Impl)||within(com.gam.nocr.ems.biz.service.external.impl.*Impl));

	before() : methodExecution(){
		Object[] arguments = thisJoinPoint.getArgs();
		String signature = "Before method execution: "
				+ thisJoinPoint.getSignature().toString();
		String str = "";
		for (int i = 0; i < arguments.length; i++) {
			if (arguments[i] != null) {
				str += "," + arguments[i] ;
			} else {
				str += ",null";
			}
		}
		if (str.startsWith(",")) {
			str = str.substring(1);
		}
		logger.info(signature + ", Method arguments: " + str);
	}

	after() returning(Object returnValue): methodExecution(){
		logger.info("After method execution: " + thisJoinPoint.getSignature());
        if (returnValue ==null) {
            logger.info("The return value is null");
        } else if (returnValue instanceof EntityTO) {
			EntityTO entityTO = (EntityTO) returnValue;
			logger.info("The return value is: " + entityTO);
		} else if (returnValue instanceof List) {
			
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) returnValue;
			logger.info("The return value is of type 'List' with size: "
					+ list.size());

/**
			 * Comment out the following lines if the contents of the list are
			 * needed to be logged
			 **/

			// for (int i = 0; i < list.size(); i++) {
			// SCPLog.getDefault().info("List[" + i + "]: " + list.get(i));
			// }
		} else if (returnValue instanceof Map) {
			
			@SuppressWarnings("unchecked")
			Map<Object, Object> map = (Map<Object, Object>) returnValue;
			logger.info("The return value is of type 'Map' with size: "
					+ map.size());


/**
			 * Comment out the following lines if the contents of the map are
			 * needed to be logged
			 **//*

			// Iterator<Object> iterator = map.keySet().iterator();
			// while (iterator.hasNext()) {
			// Object key = iterator.next();
			// Object value = map.get(key);
			// SCPLog.getDefault().info("Key: " + key);
			// SCPLog.getDefault().info("Value: " + value);
			// }   */
		} else if (returnValue instanceof Object[]) {
			Object[] objects = (Object[]) returnValue;
			logger.info("The return value is of type 'Object[]' with size: "
					+ objects.length);

/**
			 * Comment out the following lines if the contents of the object
			 * array are needed to be logged
			 **//*

			// for (int i = 0; i < objects.length; i++) {
			// SCPLog.getDefault().info("Object[" + i + "]: " + objects[i]);
			// }   */
		} else {
			logger.info("The return value is: " + returnValue);
		}
	}
}

