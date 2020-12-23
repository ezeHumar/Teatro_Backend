package com.jyaa.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.general.Notificacion;

@Service
public class JPAParticipanteDAO extends JPADAO<Participante> implements ParticipanteDAO {

	public JPAParticipanteDAO() {
		// TODO Auto-generated constructor stub
		super(Participante.class);
	}
	
	@Inject
	EntityManager manager;	
	
	@Override
	public Participante getByEmail(String email) {
		try {
			Query q= manager.createQuery("from Participante where email=:email and esAdmin=false and esOperador=false");
			q.setParameter("email", email);
			Participante participante=(Participante) q.getSingleResult();
			return participante;
			
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar el participante");
			return null;
		}
	}
	
	@Override
	public List<Participante> getListFiltered(String nombre, String apellido, String email) {
		StringBuilder query = new StringBuilder("from Participante p");
		
		
		if(nombre!=null || apellido!=null || email!=null) {
			query.append(" where (");
		}
		
		if(nombre!=null) {
			System.out.println(nombre);
			query.append("p.nombre like CONCAT('%',:nombre,'%') ) ");
			if(apellido!=null || email!=null){
				query.append("and (");
			}
		}
		
		if(apellido!=null) {
			System.out.println(apellido);
			query.append("p.apellido like CONCAT('%',:apellido,'%') )");
			if( email!=null){
				query.append("and (");
			}
		}
		
		if(email!=null) {
			System.out.println(apellido);
			query.append("p.email like CONCAT('%',:email,'%') )");
		}
		
		System.out.println(query.toString());
		
		Query q= manager.createQuery(query.toString());
		
		if (nombre!=null) {
			q.setParameter("nombre", nombre);
		}
		if (apellido!=null) {
			q.setParameter("apellido", apellido);
		}
		if (email!=null) {
			q.setParameter("email", email);
		}
		
		
		List<Participante> participantes=(List<Participante>) q.getResultList();			
		return participantes;
	}
	
	@Override
	public boolean delete(long id) {
		try {
			//Pimero se borran las notificaciones del participante
			Query q= manager.createQuery("select p from Participante p inner join p.asistencias a where a.id=:actividadID");
			q.setParameter ("actividadID", id);
			Participante p= this.get(id);
			List<Notificacion> l=new ArrayList<Notificacion>();
			p.setNotificaciones(l);
			this.modify(p);
			//Luego se borra la actividad
			EntityTransaction etx = manager.getTransaction();
			etx.begin();
			manager.remove(this.get(id));
			etx.commit();
			return true;
		}
		catch(Exception e){
			System.out.println("No se pudo borrar la actividad");
			return false;
		}
		
	}



}
