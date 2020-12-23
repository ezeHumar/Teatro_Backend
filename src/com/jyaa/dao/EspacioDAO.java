package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Espacio;

@Contract
public interface EspacioDAO extends DAO<Espacio>{
	public List<Espacio> getListFiltered(String nombre, String capacidadMin, String capacidadMax, String ciudad, String condicion);
	public List<Long> getListNoBorrable();
}
