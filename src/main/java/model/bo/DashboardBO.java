package model.bo;

import model.dao.DashboardDAO;
import model.dto.DashboardDTO;

public class DashboardBO {

	public DashboardDTO gerarDashboard(int idUsuario) {
		DashboardDAO dashboardDAO = new DashboardDAO();
		DashboardDTO dashboardDTO =new DashboardDTO();
		dashboardDTO.setTotalMoedas(dashboardDAO.contarMoedasUsuarioDAO(idUsuario));
	    dashboardDTO.setValorTotalColecao(dashboardDAO.calcularValorTotalColecaoDAO(idUsuario));
	    dashboardDTO.setUltimasMoedas(dashboardDAO.buscarUltimasMoedasDAO(idUsuario));
	    return dashboardDTO;	
	}
}
