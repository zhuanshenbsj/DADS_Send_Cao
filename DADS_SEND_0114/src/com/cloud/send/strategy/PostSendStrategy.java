package com.cloud.send.strategy;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * http  post请求的发送策略
 * @author Administrator
 *
 */
public class PostSendStrategy implements Strategy{

	@Override
	public boolean sendData(String appType, String url, String params) {
		boolean sendSuccess=false;
		
		HttpClient client=new HttpClient();
		PostMethod post=new PostMethod(url);
		
		//将参数放入post请求体中
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		post.addParameter("data", params);
		
		//设置链接超时时间
		client.setConnectionTimeout(200000);
		//设置相应超时时间
		client.setTimeout(200000);
		
		try {
			int responCode = client.executeMethod(post);
			if(responCode==200){
				sendSuccess=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			post.releaseConnection();
		}
		return sendSuccess;
	}
}
