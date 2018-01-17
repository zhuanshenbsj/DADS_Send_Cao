package com.cloud.send.observer.subject;

import java.util.ArrayList;
import java.util.List;

import com.cloud.send.observer.Observer;

/**
 * 被观察的东西（主题）
 * @author Administrator
 *
 */
public abstract class Subject {
	private String dataType;
	//观察者的集合
	private List<Observer>observers=new ArrayList<Observer>();
	/**
	 * 添加观察者
	   * @Title : addObserver 
	   * @功能描述: TODO
	   * @设定文件： 
	   * @返回类型：void 
	   * @author：cqh
	   * @throws ：
	 */
	public void addObserver(Observer observer){
		observers.add(observer);
	}
	//移除观察者
	public void removeObserver(Observer observer){
		observers.remove(observer);
	}
	
	//通知观察者
	public void notifyObservers(){
		if(observers!=null){
			for (Observer observer : observers) {
				//通知观察者，调用他们的更新方法
				observer.update(this);
			}
		}
	}
	public List<Observer> getObservers() {
		return observers;
	}
	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
