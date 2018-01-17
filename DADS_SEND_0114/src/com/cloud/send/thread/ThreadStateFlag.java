package com.cloud.send.thread;

import java.util.HashMap;

/**
 * 单例模式
 * @author Administrator
 *
 */
public class ThreadStateFlag {
	
	private static HashMap<String,ThreadStateFlag> map=new HashMap<String, ThreadStateFlag>();
	private ThreadStateFlag(){
		
	}
	
	public static ThreadStateFlag getNewInstance(String key){
		if(map.containsKey(key)){
			return map.get(key);
		}else{
			map.put(key, new ThreadStateFlag());
			return map.get(key);
		}
	}
}
