package com.gam.nocr.ems.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUtil {

	public static Map<String, FileObject> map = new HashMap<String, FileObject>();

	public static void putFileToMap(String key, FileObject file) {
		map.put(key, file);
	}

	public static String getNewUUID() {
		return UUID.randomUUID().toString();
	}

	public static FileObject getFileFromMap(String key) {
		if (map.containsKey(key))
			return map.get(key);
		return null;
	}

	public static void removeFileFromMap(String key) {
		map.remove(key);
	}
}
