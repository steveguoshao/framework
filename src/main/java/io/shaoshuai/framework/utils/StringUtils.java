/**
 * 
 */
package io.shaoshuai.framework.utils;

import java.util.Collection;

/**
 * @author 帅
 *
 */
public class StringUtils {
	
	public static boolean isEmpty(String str) {
		if(null == str || "".equals(str)) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static boolean isEmpty(Collection<?> collection) {
		if(null == collection || collection.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	public static String toUpperCaseForFirstChar(String str) {
		return str.substring(0, 1).toUpperCase().concat(str.substring(1));
	}
	
	public static String toLowerCaseForFirstChar(String str) {
		return str.substring(0, 1).toLowerCase().concat(str.substring(1));
	}
	
	public static String getFolder(int hashcode) {
		System.out.println("hashcode:" + hashcode);
		StringBuilder builder = new StringBuilder(64);
		for(int bit = (32 - 4); bit > -1; bit -= 4) {
			builder.append("/").append(hashcode >>> bit & 0xf);
		}
		return builder.toString();
	}
}
