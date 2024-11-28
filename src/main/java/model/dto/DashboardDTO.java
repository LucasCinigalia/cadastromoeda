package model.dto;

import java.util.ArrayList;
import java.util.List;

import model.vo.MoedaVO;

public class DashboardDTO {

	private int totalMoedas;
	private double valorTotalColecao;
	private List<MoedaVO> ultimasMoedas;
	
	public DashboardDTO(int totalMoedas, double valorTotalColecao, ArrayList<MoedaVO> ultimasMoedas) {
		super();
		this.totalMoedas = totalMoedas;
		this.valorTotalColecao = valorTotalColecao;
		this.ultimasMoedas = ultimasMoedas;
	}
	public DashboardDTO() {
		super();
	}
	
	public int getTotalMoedas() {
		return totalMoedas;
	}
	public void setTotalMoedas(int totalMoedas) {
		this.totalMoedas = totalMoedas;
	}
	public double getValorTotalColecao() {
		return valorTotalColecao;
	}
	public void setValorTotalColecao(double valorTotalColecao) {
		this.valorTotalColecao = valorTotalColecao;
	}
	public List<MoedaVO> getUltimasMoedas() {
		return ultimasMoedas;
	}
	public void setUltimasMoedas(List<MoedaVO> ultimasMoedas) {
		this.ultimasMoedas = ultimasMoedas;
	}
	
	
	
	
}