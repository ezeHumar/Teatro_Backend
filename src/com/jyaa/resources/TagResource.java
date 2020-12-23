package com.jyaa.resources;

import java.util.ArrayList;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.dao.TagDAO;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.Tag;
import com.jyaa.security.Secured;

@Path("/tags")
public class TagResource {

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Context
	SecurityContext securityContext;
	
	@Inject
	TagDAO tdao;
	
	@Inject
	ParticipanteDAO pdao;
	
	private String msj;
	public TagResource() {
		// TODO Auto-generated constructor stub
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin", "Participante"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tag> getTags(){
		return tdao.getList();
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("/noborrables")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Long> getTagsNoBorrables(){
		return tdao.getListNoBorrable();
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin", "Participante"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Tag u= tdao.get(id);
		if(u != null) {
			return Response
					.ok()
					.entity(u)
					.build();					
		}
		else {
			msj="No se encontro el Tag";
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
	public Response set(Tag u) {
		if(u.getTag()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		tdao.set(u);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Tag u) {
		if(u.getTag()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		Tag uAux=tdao.get(u.getId());
		if(uAux != null) {
			tdao.modify(u);
			return Response.ok().entity(u).build();
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
		Tag uAux = tdao.get(id);
		if(uAux != null){
			if(tdao.delete(uAux.getId())) {
				return Response.noContent().entity("Borrado exitosamente").build();
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("No pudo borrarse, posiblemente este referenciado por otra entidad").build();
			}
			
		} else {
			msj="No se encontro el Tag";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	
	
	@PUT
	@Secured
	@RolesAllowed({"Participante"})
	@Path("{id}/meInteresa")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response interesTag(@PathParam("id") long id) {
		Participante pAux=(Participante) pdao.getByEmail(securityContext.getUserPrincipal().getName());
		Tag tAux=(Tag) tdao.get(id);
		if(tAux==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("El tag no existe").build();
		}
		pAux.getInteresesTags().size();
		List<Tag> lTags=pAux.getInteresesTags();
		if (lTags==null) {
			lTags=new ArrayList<>();
		}
		if(lTags.contains(tAux)) {
			return Response.status(Response.Status.CONFLICT).entity("El tag ya se encuentra en la lista de intereses").build();
		}
		lTags.add(tAux);
		pAux.setInteresesTags(lTags);
		
		if(pAux != null) {
			pdao.modify(pAux);
			return Response.ok().entity(pAux).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}

}
