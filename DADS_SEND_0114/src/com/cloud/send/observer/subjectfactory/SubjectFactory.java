package com.cloud.send.observer.subjectfactory;

import org.apache.commons.lang.StringUtils;

import com.cloud.send.observer.CommonObserver;
import com.cloud.send.observer.subject.DataSaveSubject;
import com.cloud.send.observer.subject.Subject;
import com.cloud.send.util.AppDatatypeInfoContext;
import com.cloud.send.util.AppInfoContext;

/**
 * 主题工厂，用于获取主题
 * 
 * @author Administrator
 * 
 */
public class SubjectFactory {
	public static Subject getSubject(String dataType, String appType) {
		Subject dataSaveSubject = new DataSaveSubject();
		if (StringUtils.isNotBlank(dataType) && StringUtils.isNotBlank(appType)) {
			dataSaveSubject.setDataType(dataType);
			String[] appTypes = appType.split(";");
			if (appTypes.length > 0) {
				for (String app : appTypes) {

					// 判断app有没有开启
					if (!"on".equals(AppInfoContext.getPropertyByApp(app,
							"appToggle"))) {
						continue;
					}

					// 判断该app类型是否能接受该数据类型
					if (AppDatatypeInfoContext.isDataTypeOpenForAppType(app,
							dataType) == false) {
						continue;
					}
					// 若app和数据类型都能接受的话，则构造观察者
					CommonObserver commonObserver = new CommonObserver(
							AppInfoContext.getPropertyByApp(app, "sendFlag"),
							app, AppInfoContext.getPropertyByApp(app,
									"sendPath"));

					dataSaveSubject.addObserver(commonObserver);
				}
			}
		}
		return dataSaveSubject;
	}
}
