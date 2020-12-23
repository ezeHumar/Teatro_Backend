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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.EdicionDAO;
import com.jyaa.modelo.Edicion;
import com.jyaa.security.Secured;

@Path("/ediciones")
public class EdicionResource {

	public EdicionResource() {
		// TODO Auto-generated constructor stub
	}
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@Inject
	EdicionDAO edao;
	
private String msj;
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Edicion> getEdiciones(){
		return edao.getList();
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Edicion e= edao.get(id);
		if(e != null) {
			return Response
					.ok()
					.entity(e)
					.build();					
		}
		else {
			msj="No se encontro la edicion";
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
	public Response set(Edicion e) {
		if(e.getTitulo()==null || e.getDescripcion()==null || e.getFechaInicio()==null || e.getFechaFin()==null || e.getHoraInicio()==null || e.getHoraFin()==null || e.getActividades().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(e.getFechaFin().isBefore(e.getFechaInicio())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de inicio no puede ser luego de la fecha de finalizacion").build();
		}
		if(e.getFechaFin().equals(e.getFechaInicio()) && e.getHoraFin().isBefore(e.getHoraInicio())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("La hora de inicio no puede ser luego de la hora de finalizacion para un mismo dia").build();
		}
		edao.set(e);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Edicion e) {
		if(e.getTitulo()==null || e.getDescripcion()==null || e.getFechaInicio()==null || e.getFechaFin()==null || e.getHoraInicio()==null || e.getHoraFin()==null || e.getActividades().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(e.getFechaFin().isBefore(e.getFechaInicio())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de inicio no puede ser luego de la fecha de finalizacion").build();
		}
		if(e.getFechaFin().equals(e.getFechaInicio()) && e.getHoraFin().isBefore(e.getHoraInicio())) {
			return Response.status(Response.Status.BAD_REQUEST).entity("La hora de inicio no puede ser luego de la hora de finalizacion para un mismo dia").build();
		}
		Edicion eAux=edao.get(e.getId());
		if(eAux != null) {
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
		Edicion eAux = edao.get(id);
		if(eAux != null){
			edao.delete(eAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro la edicion";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	
}
