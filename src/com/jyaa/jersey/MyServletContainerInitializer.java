package com.jyaa.jersey;

import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.glassfish.hk2.utilities.reflection.Logger;
import org.slf4j.LoggerFactory;

public class MyServletContainerInitializer implements ServletContainerInitializer{
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MyServletContainerInitializer.class);
    
	public MyServletContainerInitializer() {
		super();
	}
	
	@Override
    public void onStartup(Set<Class<?>> set, final ServletContext sc) throws ServletException {
        sc.addServlet("jersey-servlet", (Class<? extends Servlet>) MyApplication.class);
    }
}