package kr.co.igns.framework.utils.type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ObjectUtils {
	private static final Logger log = LogManager.getLogger("com.project");

	// 차트데이터를 위한 타입변경
	public Map<String, Object> convertForChart(List<?> list, String[] keyArray) {
		Map<String, Object> map = new HashMap<>();

		for (String key : keyArray) {
			if (key != null) {
				String[] resultArray = ObjectUtils.convertListToArray(list, key);
				map.put(key, resultArray);
			}
		}
		return map;
	}

	public static Map<String, Object> convertObjectToMap(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj));
			} catch (Exception var5) {
				log.error(var5.getMessage());
			}
		}

		return map;
	}

	public static Object convertMapToObject(Map<String, Object> map, Object obj) {
		String keyAttribute = null;
		// String setMethodString = "set";
		String methodString = null;
		for (String s : map.keySet()) {
			keyAttribute = s;
			keyAttribute.substring(0, 1).toUpperCase();
			methodString = keyAttribute.substring(1);
			Method[] methods = obj.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (methodString.equals(method.getName())) {
					try {
						method.invoke(obj, map.get(keyAttribute));
					} catch (Exception var9) {
						log.error(var9.getMessage());
					}
				}
			}
		}

		return obj;
	}

	public static String[] convertListToArray(List<?> list, String key) {
		int count = list.size();
		String keyAttribute = null;
		String[] array = new String[count];
		for (int i = 0; i < count; i++) {
			Map<?, ?> map = convertObjectToMap(list.get(i));
			for (Object o : map.keySet()) {
				keyAttribute = (String) o;
				if (map.get(keyAttribute) != null) {
					if (keyAttribute.equals(key)) {
						array[i] = map.get(keyAttribute).toString();
					}
				}
			}
		}
		return array;
	}

}
