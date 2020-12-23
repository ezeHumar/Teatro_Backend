package com.jyaa.resources;

import java.sql.SQLException;
import java.time.LocalDate;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.ActividadDAO;
import com.jyaa.dao.ObraDAO;
import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.modelo.Actividad;
import com.jyaa.modelo.Obra;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.general.Valoracion;
import com.jyaa.security.Secured;

@Path("/actividades")
public class ActividadResource {

	public ActividadResource() {
		// TODO Auto-generated constructor stub
	}
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Context
	SecurityContext securityContext;
	
	@Inject
	ActividadDAO adao;
	
	@Inject
	ObraDAO odao;
	
	@Inject
	ParticipanteDAO pdao;
	
	private String msj;
	
	@GET
//	@Secured
//	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Actividad> getList(@QueryParam("idObras") List<String> idObras, @QueryParam("idEspacios") List<String> idEspacios, @QueryParam("idEdicion") String idEdicion, @QueryParam("horaMin") String horaMin, @QueryParam("horaMax") String horaMax, @QueryParam("fechaMin") String fechaMin, @QueryParam("fechaMax") String fechaMax) throws SQLException{
		
		return adao.getListFiltered(idObras, idEspacios, idEdicion, horaMin, horaMax, fechaMin, fechaMax);
//		return adao.getList();
		
	}
	
	@GET
//	@Secured
//	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Actividad a= adao.get(id);
		if(a != null) {
			return Response
					.ok()
					.entity(a)
					.build();					
		}
		else {
			msj="No se encontro la actividad";
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
	public Response set(Actividad a) {
		if(a.getEspacio()==null || a.getFecha()==null || a.getHora()==null || a.getObra()==null || a.getTags().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(a.getFecha().isBefore(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha no puede ser una fecha pasada").build();
		}
		odao.get(a.getObra().getId());
		adao.set(a);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Actividad a) {
		if(a.getEspacio()==null || a.getFecha()==null || a.getHora()==null || a.getObra()==null || a.getTags().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(a.getFecha().isBefore(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha no puede ser una fecha pasada").build();
		}
		Actividad aAux=adao.get(a.getId());
		if(aAux != null) {
			adao.modify(a);
			return Response.ok().entity(a).build();
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
		Actividad aAux = adao.get(id);
		if(aAux != null){
			adao.delete(aAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro la actividad";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Participante"})
	@Path("obras/{id}/meInteresa")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response interesObra(@PathParam("id") long id) {
		
		Participante pAux=(Participante) pdao.getByEmail(securityContext.getUserPrincipal().getName());//Se obtiene el participante por el email del token
		
		Obra oAux=(Obra) odao.get(id);
		
		pAux.getInteresesObras().size();
		List<Obra> lObras=pAux.getInteresesObras();
		if (lObras==null) {
			lObras=new ArrayList<>();
		}
		lObras.add(oAux);
		pAux.setInteresesObras(lObras);
		
		if(pAux != null) {
			pdao.modify(pAux);
			return Response.ok().entity(pAux).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Participante"})
	@Path("obras/{id}/valorar/{valor}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response valorarObra(@PathParam("id") long id, @PathParam("valor") int valor, String comentario) {
		
		if(valor<0 || valor>10) {
			return Response.status(Response.Status.BAD_REQUEST).entity("La calificacion debe ser de 0 a 10").build();
		}
		
		Participante pAux=(Participante) pdao.getByEmail(securityContext.getUserPrincipal().getName());
		
		Obra oAux=(Obra) odao.get(id);
		
		List<Obra> asistenciasObras = new ArrayList<Obra>();//Obtengo las obras a las que asistio el usuario para ver si puede valorarlas
		for(Actividad asistenciaAct : pAux.getAsistencias()) {
			asistenciasObras.add(asistenciaAct.getObra());
		}
		
		if(asistenciasObras.contains(oAux)) {//Veo si asistio a la obra que quiere valorar
			Valoracion vAux=new Valoracion();
			vAux.setObra(oAux);
			vAux.setPuntuacion(valor);
			vAux.setComentario(comentario);
	
			pAux.getValoraciones().size(); //Esto solo lo pongo para que recupere la lista de la base de datos
			List<Valoracion> lValoraciones=pAux.getValoraciones();
			if (lValoraciones==null) {
				lValoraciones=new ArrayList<>();
			}
			
			//Obtengo las obras valoradas para ver si ya la valoro
			List<Obra> valoracionesObras = new ArrayList<Obra>();
			
			for(Valoracion valoracion : lValoraciones) {
				valoracionesObras.add(valoracion.getObra());
			}
			
			if(valoracionesObras.contains(oAux)) { //Si ya valoro la obra se reemplaza la valoracion
				lValoraciones.set(valoracionesObras.indexOf(oAux),vAux);
			}else { //Si no valoro la obra se agrega la valoracion
				lValoraciones.add(vAux);
			}
			
			pAux.setValoraciones(lValoraciones);
			pdao.modify(pAux);
			System.out.println("Hasta aca llegue");
			
			//Actualizo la valoracion promedio de la obra
			List<Valoracion> valoraciones = odao.getValoraciones(oAux.getId());
			float promedio;
			int sumaValoraciones=0;
			int cantidad=0;
			for (Valoracion valoracion : valoraciones) {
				sumaValoraciones=sumaValoraciones + valoracion.getPuntuacion();
				cantidad++;
	        }
			promedio = sumaValoraciones/cantidad;
			
			oAux.setValoracion(promedio);
		}
		
		if(pAux != null) {
			if(!asistenciasObras.contains(oAux)) {
				return Response.status(Response.Status.NOT_FOUND).entity("El usuario no asistio a esta obra").build();
			}else {
				
				odao.modify(oAux);
				return Response.ok().entity(pAux).build();
			}
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Participante"})
	@Path("obras/{id}/entradasEntregadas/{n}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response entradasEntregadasObra(@PathParam("id") long id, @PathParam("n") int n) {
		Obra oAux=(Obra) odao.get(id);

		oAux.setEntradasEntregadas(oAux.getEntradasEntregadas()+n);
		
		if(oAux != null) {
			odao.modify(oAux);
			return Response.ok().entity(oAux).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Participante"})
	@Path("obras/{idActividad}/validar")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validarObra(@PathParam("idActividad") long idActividad, Integer codigo) {
		
		if(codigo==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Se debe ingresar el codigo").build();
		}
		System.out.println(codigo);
		//Validar codigo
		Participante pAux=(Participante) pdao.getByEmail(securityContext.getUserPrincipal().getName());
		
		Actividad aAux=(Actividad) adao.get(idActividad);
		
		if(codigo.intValue()!=aAux.getCodigo()) {
			return Response.status(Response.Status.NOT_FOUND).entity("Codigo invalido").build();
		}
		
		pAux.getAsistencias().size();
		List<Actividad> lAsistencias=pAux.getAsistencias();
		if (lAsistencias==null) {
			lAsistencias=new ArrayList<>();
		}
		
		lAsistencias.add(aAux);
		pAux.setAsistencias(lAsistencias);
		
		if(pAux != null) {
			pdao.modify(pAux);
			return Response.ok().entity(pAux).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("/noborrables")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Long> getActividadesNoBorrables(){
		return adao.getListNoBorrable();
	}		

}
