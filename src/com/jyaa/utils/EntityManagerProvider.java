package com.jyaa.utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

@Service
public class EntityManagerProvider implements Factory<EntityManager> {
	private final EntityManagerFactory emf;

	@Inject
	public EntityManagerProvider(EntityManagerFactory emf) {
	    this.emf = emf;
	}
	@Override
	public EntityManager provide() {
	    EntityManager em = emf.createEntityManager();
	    return em;
	}
	@Override
	public void dispose(EntityManager entityManager) {
		System.out.println("Dispose if");
	    if (entityManager.isOpen()) {
	    	entityManager.close();
	    }
	}


}