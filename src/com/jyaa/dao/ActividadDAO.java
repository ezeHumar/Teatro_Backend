package com.jyaa.dao;

import java.sql.SQLException;
import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Actividad;

@Contract
public interface ActividadDAO extends DAO<Actividad>{
	public List<Actividad> getListFiltered(List<String> obras, List<String> espacios, String edicionId, String horaMin, String horaMax, String fechaMin, String fechaMax) throws SQLException;
	public List<Long> getListNoBorrable();

}
