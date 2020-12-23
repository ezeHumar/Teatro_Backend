package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Artista;

@Contract
public interface ArtistaDAO extends DAO<Artista>{
	public List<Artista> getListFiltered(String nombre,String apellido, List<String> idTags, List<String> idObras);
	public List<Long> getListNoBorrable();
}
