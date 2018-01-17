package com.cloud.send.strategy;

/**
 * mq发送策略
 * @author Administrator
 *
 */
public class MQsendStrategy  implements Strategy{

	@Override
	public boolean sendData(String appType, String url, String params) {
		return false;
	}

}
