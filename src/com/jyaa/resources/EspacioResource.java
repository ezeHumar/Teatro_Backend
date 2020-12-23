package com.jyaa.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.EspacioDAO;
import com.jyaa.modelo.Espacio;
import com.jyaa.security.Secured;

@Path("/espacios")
public class EspacioResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Inject
	EspacioDAO edao;
	
	private String msj;

	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEspacios(@QueryParam("nombre") String nombre,@QueryParam("capacidadMin") String capacidadMin,@QueryParam("capacidadMax") String capacidadMax,@QueryParam("ciudad") String ciudad,@QueryParam("condicion") String condicion){
		if((capacidadMin!=null && capacidadMax!=null) && (Integer.parseInt(capacidadMin) > Integer.parseInt(capacidadMax))) {
			return  Response
					.status(Response.Status.BAD_REQUEST)
					.entity("La capacidad minima no puede ser mayor a la capacidad maxima")
					.build();					
		}
		List<Espacio> e= edao.getListFiltered(nombre, capacidadMin, capacidadMax, ciudad, condicion);
		return Response
				.status(Response.Status.OK)
				.entity(e)
				.build();
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Espacio e= edao.get(id);
		if(e != null) {
			return Response
					.ok()
					.entity(e)
					.build();					
		}
		else {
			msj="No se encontro el espacio";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}
	
	@POST
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response set(Espacio e) {
		if(e.getNombre()==null || e.getDescripcion()==null || e.getUbicacion()==null || e.getCapacidad()==-1) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		edao.set(e);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Espacio e) {
		if(e.getNombre()==null || e.getDescripcion()==null || e.getUbicacion()==null || e.getCapacidad()==-1) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		Espacio uAux=edao.get(e.getId());
		if(uAux != null) {
			edao.modify(e);
			return Response.ok().entity(e).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@DELETE
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id) {
		Espacio uAux = edao.get(id);
		if(uAux != null){
			edao.delete(uAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro el espacio";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("/noborrables")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Long> getTagsNoBorrables(){
		System.out.println("Entree");
		return edao.getListNoBorrable();
	}
}
