package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Tag;

@Contract
public interface TagDAO extends DAO<Tag>{
	public List<Long> getListNoBorrable();
}
