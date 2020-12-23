package com.jyaa.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.JustInTimeInjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;
import org.reflections.Reflections;

@Service
public class JustInTimeServiceResolver implements JustInTimeInjectionResolver {

	@Inject
	private ServiceLocator serviceLocator;

	private String packageScan = "com.jyaa";
	
	@Override
	public boolean justInTimeResolution(Injectee injectee) {
		final Type requiredType = injectee.getRequiredType();

		if (injectee.getRequiredQualifiers().isEmpty() && requiredType instanceof Class) {
			final Class<?> requiredClass = (Class<?>) requiredType;

			if (requiredClass.getName().startsWith(packageScan)) {
				final List<ActiveDescriptor<?>> descriptors = new ArrayList<>();
				
				if(requiredClass.isAnnotationPresent(Contract.class)) {
					
					Set<Class<?>> classes =  getReflections().getSubTypesOf(((Class) requiredClass));
                    for(Class subClass : classes) {
                        descriptors.addAll(ServiceLocatorUtilities.addClasses(serviceLocator, subClass));
                    }
                } else {
                	descriptors.addAll(ServiceLocatorUtilities.addClasses( serviceLocator, requiredClass ));
                }
				
				

				if (!descriptors.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	 public Reflections getReflections() {
	        return new Reflections(packageScan);
	       
	    }
}
