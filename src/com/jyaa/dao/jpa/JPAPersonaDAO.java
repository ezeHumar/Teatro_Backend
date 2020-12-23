package com.jyaa.dao.jpa;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.PersonaDAO;
import com.jyaa.modelo.Persona;

@Service
public class JPAPersonaDAO extends JPADAO<Persona> implements PersonaDAO {

	public JPAPersonaDAO() {
		// TODO Auto-generated constructor stub
		super(Persona.class);
	}
}
