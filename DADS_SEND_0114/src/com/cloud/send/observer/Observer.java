package com.cloud.send.observer;

import com.cloud.send.observer.subject.Subject;

/**
 * 抽象接口
 * @author Administrator
 *
 */
public interface Observer {
	public abstract void update(Subject subject);
}
