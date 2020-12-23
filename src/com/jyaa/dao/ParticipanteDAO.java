package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Participante;


@Contract
public interface ParticipanteDAO extends DAO<Participante>{

	public Participante getByEmail(String email);
	public List<Participante> getListFiltered(String nombre, String apellido, String email);
	public List<Participante> getList();
}
