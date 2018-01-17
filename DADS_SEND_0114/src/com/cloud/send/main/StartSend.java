package com.cloud.send.main;

import java.util.HashMap;
import java.util.List;

import com.cloud.send.observer.subjectfactory.SubjectFactory;
import com.cloud.send.util.C3P0Util;

public class StartSend {
	
	public static void main(String[] args) {
		
		String sql="select *  from sports where sendFlag=0";
		
		List<HashMap<String, String>> data = C3P0Util.getData(sql);
		if(data!=null&&data.size()>0){
			for (HashMap<String, String> hashMap : data) {
				//获取主题，通知观察者进行更新操作
				SubjectFactory.getSubject(hashMap.get("dataType"),hashMap.get("appType")).notifyObservers();
			}
		}
	}
}
