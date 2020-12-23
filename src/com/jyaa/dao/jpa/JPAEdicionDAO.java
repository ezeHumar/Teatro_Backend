package com.jyaa.dao.jpa;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.EdicionDAO;
import com.jyaa.modelo.Edicion;

@Service
public class JPAEdicionDAO extends JPADAO<Edicion> implements EdicionDAO {

	public JPAEdicionDAO() {
		// TODO Auto-generated constructor stub
		super(Edicion.class);
	}

}
