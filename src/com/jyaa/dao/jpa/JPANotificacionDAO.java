package com.jyaa.dao.jpa;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.NotificacionDAO;
import com.jyaa.modelo.general.Notificacion;

@Service
public class JPANotificacionDAO extends JPADAO<Notificacion> implements NotificacionDAO {

	public JPANotificacionDAO() {
		// TODO Auto-generated constructor stub
		super(Notificacion.class);
	}

}
