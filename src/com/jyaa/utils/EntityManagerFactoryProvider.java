package com.jyaa.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

@Service
public class EntityManagerFactoryProvider implements Factory<EntityManagerFactory> {

	private final EntityManagerFactory emf;

	public EntityManagerFactoryProvider() {
		emf = Persistence.createEntityManagerFactory("bdTeatro");
	}
	@Override
	public EntityManagerFactory provide() {
		return emf;
	}
	@Override
	public void dispose(EntityManagerFactory instance) {
		instance.close();   
	}

} 
