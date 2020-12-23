package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Obra;
import com.jyaa.modelo.general.Valoracion;


@Contract
public interface ObraDAO extends DAO<Obra>{
	public List<Obra> getListFiltered(List<String> idArtistas, List<String> idTags, String nombre);
	public List<Valoracion> getValoraciones(long id);
	public List<Long> getListNoBorrable();
}
