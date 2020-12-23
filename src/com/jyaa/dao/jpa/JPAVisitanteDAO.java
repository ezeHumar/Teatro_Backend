package com.jyaa.dao.jpa;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.VisitanteDAO;
import com.jyaa.modelo.Visitante;


@Service
public class JPAVisitanteDAO extends JPADAO<Visitante> implements VisitanteDAO{

	public JPAVisitanteDAO() {
		// TODO Auto-generated constructor stub
		super(Visitante.class);
	}
}
