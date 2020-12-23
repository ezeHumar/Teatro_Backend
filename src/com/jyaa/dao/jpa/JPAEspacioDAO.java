package com.jyaa.dao.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.EspacioDAO;
import com.jyaa.modelo.Espacio;

@Service
public class JPAEspacioDAO extends JPADAO<Espacio> implements EspacioDAO{

	public JPAEspacioDAO() {
		// TODO Auto-generated constructor stub
		super(Espacio.class);
	}
	
	@Inject
	EntityManager manager;

	@Override
	public List<Espacio> getListFiltered(String nombre, String capacidadMin, String capacidadMax, String ciudad, String condicion) {
		StringBuilder query = new StringBuilder("select e from Espacio e");
		
		if (nombre!=null || capacidadMin!=null || capacidadMax!=null || ciudad!=null || condicion!=null) {
			query.append(" where ");
		}
		
		if (capacidadMax!=null || capacidadMin!=null) {
			
			if (capacidadMax!=null && capacidadMin!=null){
				query.append("(capacidad>=:capacidadMin and capacidad<=:capacidadMax)");
			}else if(capacidadMax!=null){
				query.append("(capacidad<=:capacidadMax)");
			}else if(capacidadMin!=null) {
				query.append("(capacidad>=:capacidadMin)");
			}
			
			if (nombre!=null || ciudad!=null || condicion!=null) {
				query.append(" and ");
			}
		}
		
		if (nombre!=null) {
			query.append("(nombre like CONCAT('%',:nombre,'%'))");
			if (ciudad!=null || condicion!=null) {
				query.append(" and ");
			}
		}
		
		if (ciudad!=null) {
			query.append("(ubicacion.Ciudad like CONCAT('%',:ciudad,'%'))");
			if (condicion!=null) {
				query.append(" and ");
			}
		}
		
		if (condicion!=null) {
			if(condicion.equals("abierto")) {
				query.append("(abierto=true)");
			}else if(condicion.equals("cerrado")){
				query.append("(abierto=false)");
			}else {
				query.append("(abierto=false or abierto=true)");
			}
		}
		System.out.println(query.toString());
		Query q= manager.createQuery(query.toString());
		
		if (nombre!=null) {
			q.setParameter("nombre", nombre);
		}
		if (ciudad!=null) {
			q.setParameter("ciudad", ciudad);
		}
		if (capacidadMax!=null) {
			q.setParameter("capacidadMax", Integer.parseInt(capacidadMax));
		}
		if (capacidadMin!=null) {
			q.setParameter("capacidadMin", Integer.parseInt(capacidadMin));
		}
		
		List<Espacio> espacios=(List<Espacio>) q.getResultList();
		
		return espacios;
	}

	@Override
	public List<Long> getListNoBorrable() {
		try {
			//Busco los id de los tags que pertenecen a otra entidad
			List<Long> noBorrables=(List<Long>) manager.createQuery("select distinct e.id from Actividad a inner join a.espacio e").getResultList();
	        return noBorrables;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar los no borrables");
			return null;
		}
	}
}
