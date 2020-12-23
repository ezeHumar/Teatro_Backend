package com.jyaa.utils;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;


public class MyApplicationBinder extends AbstractBinder {
		@Override
	    protected void configure() {
			bind( JustInTimeServiceResolver.class ).to( JustInTimeInjectionResolver.class );
			bindFactory(EntityManagerFactoryProvider.class)
			   .to(EntityManagerFactory.class)
			   .in(Singleton.class);
			bindFactory(EntityManagerProvider.class)
			   .proxy(true)
			   .proxyForSameScope(false)
			   .to(EntityManager.class)
			   .in(RequestScoped.class);
	    }	
}