package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface DAO<T> {
	public T get(long id);
	public List<T> getList();
	public void set(T t);
	public void modify(T t);
	public boolean delete(long id);
	

}
