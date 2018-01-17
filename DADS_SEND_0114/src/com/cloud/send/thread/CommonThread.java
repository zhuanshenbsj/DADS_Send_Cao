package com.cloud.send.thread;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cloud.send.strategy.MQsendStrategy;
import com.cloud.send.strategy.PostSendStrategy;
import com.cloud.send.strategy.Strategy;
import com.cloud.send.util.C3P0Util;
import com.cloud.send.util.PropertiesReader;

public class CommonThread extends Thread{
	private static Logger log = Logger.getLogger(CommonThread.class);
	private String baseQuerySql;
	private String baseUpdateSql;
	private String sendWay;
	private ThreadStateFlag threadStateFlag;
	private String tableName;
	private String sendFlag;
	private String appType;
	private String sendPath;
	public CommonThread(String sendWay, String tableName, String sendFlag,
			String appType, String sendPath,ThreadStateFlag threadStateFlag) {
		super();
		this.sendWay = sendWay;
		this.tableName = tableName;
		this.sendFlag = sendFlag;
		this.appType = appType;
		this.sendPath = sendPath;
		this.threadStateFlag=threadStateFlag;
		this.baseQuerySql=PropertiesReader.getProp("baseQuerySql_"+sendWay);
		this.baseUpdateSql=PropertiesReader.getProp("baseUpdateSql_"+sendWay);
	}
	
	@Override
	public void run() {
		//主流业务
		
//		synchronized (threadStateFlag) {
		//先查询 未发送的数据
		baseQuerySql=String.format(baseQuerySql, tableName,sendFlag,"%"+appType+"%");
		
		//查询一下未发送的数据
		List<HashMap<String, String>> data = C3P0Util.getData(baseQuerySql);
		//发送策略
		Strategy sendStrategy;
		
		if(data!=null&&data.size()>0){
			for (HashMap<String, String> map : data) {
				JSONObject dataJson=new JSONObject();
				
				//将需要发送给emr的参数封装成json对象
				dataJson.put("appType", appType);
				dataJson.put("dataType", map.get("dataType"));
				dataJson.put("collectDate", map.get("receiveTime"));
				dataJson.put("phone", map.get("phone"));
				dataJson.put("dataValue", map.get("dataValue"));
				
				//通过策略模式进行发送
				
				if("PostStrategy".equals(PropertiesReader.getProp(sendWay))){
					sendStrategy=new PostSendStrategy();
				}else{
					sendStrategy=new MQsendStrategy();
				}
				
				if(sendStrategy!=null){
					if(sendStrategy.sendData(appType, sendPath, dataJson.toString())==true){
						
						//更新发送标识
						baseUpdateSql=String.format(baseUpdateSql, tableName,sendFlag,map.get("id"));
						
						boolean updateSuccess = C3P0Util.executeUpdate(baseUpdateSql);
						if(updateSuccess){
							log.info("发送数据成功，但是更新本表发送标识成功");
						}else{
							//做日志
							log.error("发送数据成功，但是更新本表发送标识失败，更新sql语句为"+baseUpdateSql);
						}
					}else{
						try {
							//等会再发
							Thread.sleep(20000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						//发送失败，做日志
						log.error("数据发送失败，发送路径为"+sendPath);
					}
				}
				
			}
		}
		//该线程执行完毕后，释放锁
//		threadStateFlag.notify();
//	}
}
}
