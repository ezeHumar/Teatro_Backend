package com.jyaa.resources;

import java.time.LocalDate;
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

import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.dao.UsuarioDAO;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.Usuario;
import com.jyaa.security.Secured;

@Path("/participantes")
public class ParticipanteResource {

	public ParticipanteResource() {
		// TODO Auto-generated constructor stub
	}
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Inject
	ParticipanteDAO pdao;
	
	@Inject
	UsuarioDAO udao;
	
	private String msj;
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Participante> getparticipantesFiltered(@QueryParam("nombre")String nombre,@QueryParam("apellido") String apellido, @QueryParam("email") String email){
		return pdao.getListFiltered(nombre, apellido, email);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Participante p= pdao.get(id);
		if(p != null) {
			return Response
					.ok()
					.entity(p)
					.build();					
		}
		else {
			msj="No se encontro el participante";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response set(Participante p) {
		if(p.getApellido()==null || p.getNombre()==null || p.getEmail()==null || p.getFechaNacimiento()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(p.getFechaNacimiento().isAfter(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimento no puede ser una fecha futura").build();
		}
		if(udao.existe(p)) {
			return Response.status(Response.Status.CONFLICT).entity("Ya existe un usuario con la misma direccion de email").build();
		}else {
			pdao.set(p);
		}
		
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin", "Participante"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Participante p) {
		if(p.getApellido()==null || p.getNombre()==null || p.getEmail()==null || p.getFechaNacimiento()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(p.getFechaNacimiento().isAfter(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimento no puede ser una fecha futura").build();
		}
		if(udao.existe(p) && !p.getEmail().equals(udao.get(p.getId()).getEmail())) {
			return Response.status(Response.Status.CONFLICT).entity("Ya existe un usuario con la misma direccion de email").build();
		}else {
			Participante uAux=pdao.get(p.getId());
			if(uAux != null) {
				pdao.modify(p);
				return Response.ok().entity(p).build();
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
			}
		}
	}
	
	@DELETE
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id) {
		Participante pAux = pdao.get(id);
		if(pAux != null){
			pdao.delete(pAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro el participante";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	

}
