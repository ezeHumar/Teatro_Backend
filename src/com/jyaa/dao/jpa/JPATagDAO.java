package com.jyaa.dao.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.TagDAO;
import com.jyaa.modelo.Tag;

@Service
public class JPATagDAO extends JPADAO<Tag> implements TagDAO{

	public JPATagDAO() {
		// TODO Auto-generated constructor stub
		super(Tag.class);
	}
	
	@Inject
	EntityManager manager;

	@Override
	public List<Long> getListNoBorrable() {
		try {
		
			Set<Long> set = new HashSet<Long>();
			
			//Busco los id de los tags que pertenecen a otra entidad
			List<Long> borrablesActividades=(List<Long>) manager.createQuery("select distinct t.id from Actividad a inner join a.tags t").getResultList();
			List<Long> borrablesObras=(List<Long>) manager.createQuery("select distinct t.id from Obra o inner join o.tags t").getResultList();
			
			//unifico las listas
	        set.addAll(borrablesActividades);
	        set.addAll(borrablesObras);

	        return new ArrayList<Long>(set);
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar los no borrables");
			return null;
		}
	}



}
