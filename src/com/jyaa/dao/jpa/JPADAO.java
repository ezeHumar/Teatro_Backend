package com.jyaa.dao.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.DAO;

@Service
public class JPADAO<T> implements DAO<T> {
	
	

	public JPADAO(Class<T> entityClass) {
		this.entityClass=entityClass;
	}
	
	private Class<T> entityClass;
	
	@Inject
	EntityManager manager;

	@Override
	public T get(long id) {
		try {		
			System.out.println(entityClass.getClass());
			T t=(T) manager.find(entityClass, id);
			return t;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar" + entityClass.getName().toLowerCase());
			return null;
		}
	}

	@Override
	public List<T> getList() {
		try {
			System.out.println(entityClass.getName());
			Query q= manager.createQuery("from " + entityClass.getName());
			List<T> lista=(List<T>) q.getResultList();
			return lista;
		}
		catch(Exception e){
			System.out.println("No se pudo listar " + entityClass.getName().toLowerCase());
			return null;
		}
	}

	@Override
	public void set(T t) {
		try {
			EntityTransaction etx = manager.getTransaction();
			etx.begin();
			manager.persist(t);
			etx.commit();
		}
		catch(Exception e){
			System.out.println("No se pudo guardar "+ entityClass.getName().toLowerCase());
		}
		
	}

	@Override
	public void modify(T t) {
		try {
			EntityTransaction etx = manager.getTransaction();
			etx.begin();
			manager.merge(t);
			etx.commit();
		}
		catch(Exception e){
			System.out.println("No se pudo modificar "+ entityClass.getName().toLowerCase());
		}
		
	}

	@Override
	public boolean delete(long id) {
		try {
			EntityTransaction etx = manager.getTransaction();
			etx.begin();
			manager.remove(this.get(id));
			etx.commit();
			return true;
			
		}
		catch(Exception e){
			manager.clear();
			System.out.println("No se pudo borrar" + entityClass.getName().toLowerCase());
			return false;
		}
		
	}

}
