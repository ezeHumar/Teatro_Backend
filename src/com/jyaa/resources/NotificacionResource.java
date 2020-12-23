package com.jyaa.resources;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jyaa.dao.ActividadDAO;
import com.jyaa.dao.NotificacionDAO;
import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.dao.UsuarioDAO;
import com.jyaa.dao.VisitanteDAO;
import com.jyaa.modelo.Actividad;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.Visitante;
import com.jyaa.modelo.general.Notificacion;
import com.jyaa.security.Secured;

@Path("/notificaciones")
public class NotificacionResource {

	public NotificacionResource() {
		// TODO Auto-generated constructor stub
	}
	
	@Inject
	ParticipanteDAO pdao;
	
	@Inject
	VisitanteDAO vdao;
	
	@Inject 
	ActividadDAO adao;
	
	@Inject
	UsuarioDAO udao;
	
	@Inject
	NotificacionDAO ndao;
	
	private String msj;
	
	@PUT
	@Secured
	@RolesAllowed({"Operador"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response enviarNotificaion(@PathParam("id") long id, String descripcion) {

		List<Notificacion> lNotificaciones;
		List<Participante> participantes=(List<Participante>) pdao.getList();
		List<Visitante> visitantes=(List<Visitante>) vdao.getList();

		Notificacion nAux=new Notificacion();
		Actividad aAux=adao.get(id);
		nAux.setTitulo(aAux.getObra().getNombre() + " de la fecha " + aAux.getFecha() + " y hora " + aAux.getHora()); //El titulo de la notificacion indica la obra, fecha y hora
		nAux.setDescripcion(descripcion);  //La descripcion se recibe como json
		nAux.setLeido(false);
		ndao.set(nAux);

		for(Participante p : participantes) {
			p.getNotificaciones().size();
			lNotificaciones=p.getNotificaciones();

			if (lNotificaciones==null) {
				lNotificaciones=new ArrayList<>();
			}
			lNotificaciones.add(nAux);
			p.setNotificaciones(lNotificaciones);
			pdao.modify(p);
		}

		for(Visitante v : visitantes) {
			v.getNotificaciones().size();
			lNotificaciones=v.getNotificaciones();

			if (lNotificaciones==null) {
				lNotificaciones=new ArrayList<>();
			}
			lNotificaciones.add(nAux);
			v.setNotificaciones(lNotificaciones);
			vdao.modify(v);
		}
		return Response.ok().build();

	}
}
