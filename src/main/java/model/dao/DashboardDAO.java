package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.vo.MoedaVO;

public class DashboardDAO {
	
	public int contarMoedasUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
				
		String query = "SELECT COUNT(*) FROM moeda WHERE idUsuario = ?";
        int totalMoedas = 0;

        try {
            pstmt.setInt(1, idUsuario);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                totalMoedas = resultado.getInt(1);
            }
        } catch (SQLException erro) {
        	System.out.println("Erro ao executar a query do método contarMoedaUsuarioDAO!");
			System.out.println("Erro: " + erro.getMessage());
        }

        return totalMoedas;
    }

    public double calcularValorTotalColecaoDAO(int idUsuario) {
    	Connection conn = Banco.getConnection();
    	PreparedStatement pstmt = null;
    	
    	String query = "SELECT SUM(valor) FROM moeda WHERE idUsuario = ?";
        double valorTotal = 0.0;

        try {
            pstmt.setInt(1, idUsuario);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                valorTotal = resultado.getDouble(1);
            }
        } catch (SQLException erro) {
        	System.out.println("Erro ao executar a query do método calcularValorTotalColecaoDAO!");
			System.out.println("Erro: " + erro.getMessage());
        }

        return valorTotal;
    }

    public List<MoedaVO> buscarUltimasMoedasDAO(int idUsuario) {
    	Connection conn = Banco.getConnection();
    	PreparedStatement pstmt = null;
    	
        String query = "SELECT * FROM moeda WHERE idUsuario = ? ORDER BY dataCadastro DESC LIMIT 3";
        List<MoedaVO> ultimasMoedas = new ArrayList<>();

        try {
            pstmt.setInt(1, idUsuario);
            ResultSet resultado = pstmt.executeQuery();

            while (resultado.next()) {
                MoedaVO moedaVO = new MoedaVO();
                moedaVO.setIdMoeda(resultado.getInt("idMoeda"));
                moedaVO.setNome(resultado.getString("nome"));
                moedaVO.setValor(resultado.getDouble("valor"));
                moedaVO.setDataCadastro(resultado.getDate("dataCadastro").toLocalDate());
                ultimasMoedas.add(moedaVO);
            }
        } catch (SQLException erro) {
        	System.out.println("Erro ao executar a query do método buscarUltimasMoedasDAO!");
			System.out.println("Erro: " + erro.getMessage());
        }

        return ultimasMoedas;
    }
}

