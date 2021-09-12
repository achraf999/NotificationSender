package com.smsSender.server.services;

import java.util.List;

import com.smsSender.server.entities.Notification;

public interface INotification<T extends Notification> {

	public T add(T notif);

	public void deleteById(long id);

	public void deleteAll();

	public T update(T notif);

	public List<T> getAll();

	public T getById(long id);

	public List<T> getByStatus(String status);

	public List<T> getByFrequence(String freq);

	public T sendNow(T notif);
	
	public long[] count();

}
