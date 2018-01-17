package com.cloud.send.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cloud.send.observer.subject.Subject;
import com.cloud.send.thread.CommonThread;
import com.cloud.send.thread.ThreadStateFlag;
import com.cloud.send.util.DatatypeInfoContext;
import com.cloud.send.util.PropertiesReader;

public abstract class AbstractObserver implements Observer{
	
	/**
	 * 这是公共的更新方法
	 * @param subject 
	 * @param threadMap 
	 * @param sendPath 
	 * @param appType 
	 * @param sendFlag 
	   * @Title : commonUpdate 
	   * @功能描述: TODO
	   * @设定文件： 
	   * @返回类型：void 
	   * @author：cqh
	   * @throws ：
	 */
	public void commonUpdate(Subject subject, String sendFlag, String appType, String sendPath, HashMap<String, CommonThread> threadMap){
		//根据dataType类型 查询其表
		String dataType = subject.getDataType();
		String tableName = DatatypeInfoContext.getProperty(dataType, "tableName");
		
		//判断配置的发送方式
		String sendWayList = PropertiesReader.getProp("sendWayList");
		if(StringUtils.isBlank(sendWayList)){
			return ;
		}
		String[] sendWayLists = sendWayList.split(",");
		
		
		
		for (String sendWay : sendWayLists) {
			
			//判断发送方式有无开启
			if(!"on".equals(PropertiesReader.getProp(sendWay+"_toggle"))){
				//日志记录
				continue;
			}
			
			//线程key，若key重复，则说明线程重复
			//APP类型一样,sendWay一样，则不会起重复的线程
			String threadKey = appType+"_"+tableName+"_"+sendWay;
			
			if(threadMap.containsKey(threadKey)){
				CommonThread commonThread = threadMap.get(threadKey);
				//若线程存活，则不需要开启新的线程
				if(commonThread.isAlive()){
					continue;
				}
				
			}
			
			//若不存在该线程key（则说明该类线程从来没有开启），或者 以前的线程已经死了
			CommonThread commonThread= new CommonThread(sendWay,tableName,sendFlag,appType,sendPath,ThreadStateFlag.getNewInstance(threadKey));
			commonThread.setName(threadKey);
			//开启线程，进行数据发送
			commonThread.start();
			threadMap.put(threadKey, commonThread);
			
		}
	}
}
