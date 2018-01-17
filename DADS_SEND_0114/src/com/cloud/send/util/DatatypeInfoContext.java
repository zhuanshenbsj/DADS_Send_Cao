package com.cloud.send.util;

import java.util.HashMap;
import java.util.List;

public class DatatypeInfoContext {
	private static HashMap<String, HashMap<String, String>> data;
	
	static{
		data=new HashMap<String, HashMap<String,String>>();
		String sql="select * from datatype";
		List<HashMap<String, String>> data2 = C3P0Util.getData(sql);
		if(data2!=null&&data2.size()>0){
			for (HashMap<String, String> hashMap : data2) {
				data.put(hashMap.get("dataTypeName"), hashMap);
			}
		}
	}
	
	public static String getProperty(String dataType,String param){
		HashMap<String, String> hashMap = data.get(dataType);
		if(hashMap!=null){
			return hashMap.get(param);
		}
		return null;
	}
}
