package com.jyaa.jersey;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.jyaa.utils.MyApplicationBinder;

@ApplicationPath("resources")
public class MyApplication extends ResourceConfig {

	public MyApplication() {
		register(new MyApplicationBinder());
		register(RolesAllowedDynamicFeature.class);
		register(MultiPartFeature.class);
		register(FormDataContentDisposition.class);
		packages(true, "com.jyaa.resources");
		packages(true, "com.jyaa.security");
	}

}
