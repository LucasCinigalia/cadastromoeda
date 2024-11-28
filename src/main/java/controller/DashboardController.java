package controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.bo.DashboardBO;
import model.dto.DashboardDTO;

@Path("/dashboard")
public class DashboardController {

	@GET
	@Path("/usuario/{idUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public DashboardDTO gerarDashboardPorUsuario(@PathParam("idUsuario") int idUsuario) {
		DashboardBO dashboardBO = new DashboardBO();
		return dashboardBO.gerarDashboard(idUsuario);
	}
}
