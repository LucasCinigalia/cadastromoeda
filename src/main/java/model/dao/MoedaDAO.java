package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.vo.MoedaVO;
import model.vo.UsuarioVO;
import model.dto.DashboardDTO;


public class MoedaDAO {

	public boolean verificarCadastroMoedaBancoDAO(MoedaVO moedaVO){
		Connection conn = Banco.getConnection(); //Método criado na classe Banco - linhas 19 à 31. Pegando coexão.
		Statement stmt = Banco.getStatement(conn); //Statement executa o MySQL.
		ResultSet resultado = null;
		
		boolean retorno = false; // Estamos partindo do presuposto de que a moeda ainda não está cadastrada. Se ela estiver, a linha 29 vai mudar para true.
		String query = "SELECT idMoeda FROM moeda WHERE nome = '" + moedaVO.getNome() + "' AND pais = '" + moedaVO.getPais() + "' AND ano = " + moedaVO.getAno(); // Precisa sempre colocar as aspas simples nas Strings para ficar no padrão para o MySQL.
		// Foi utilizado nome, pais e ano, pensando que ainda não temos o ID para buscar, pois ele é único. Uma moeda com o mesmo nome, país e ano será uma moeda repetida.
		
		try{
			resultado = stmt.executeQuery(query);
			if(resultado.next()){
				retorno = true;
			}
		} catch(SQLException erro){
			System.out.println("Erro ao executar a query do método verificarCadastroMoedaBancoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally{
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);                           
		}
		return retorno;
	}
	
	public MoedaVO cadastrarMoedaDAO(MoedaVO moedaVO) {
		String query =  "INSERT INTO moeda (nome, pais, ano, valor, detalhes, dataCadastro, imagem) VALUES (?, ?, ?, ?, ?, ?, ?)"; // ? São mascaras, para depois informar o valor utilizando o numero de cada ?.
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query); // Para pegar um Prepared Statement preciso já ter informado qual a query que quero executar, por isso a query veio antes.
		ResultSet resultado = null;
		
		try {
			pstmt.setString(1, moedaVO.getNome()); // Nesses casos, as aspas simples são feitas automaticamente.
			pstmt.setString(2, moedaVO.getPais());
			pstmt.setInt(3, moedaVO.getAno());
			pstmt.setDouble(4, moedaVO.getValor());
			pstmt.setString(5, moedaVO.getDetalhes());
			pstmt.setObject(6, Date.valueOf(LocalDate.now()));
			pstmt.setBytes(7, moedaVO.getImagem());
			pstmt.execute(); //Um INSERT utilizando preparedStatement, só precisamos utilizar o execute.
			resultado = pstmt.getGeneratedKeys(); // Esse método passará o ID
			if(resultado.next()) { //Esse if serve para se receber alguma coisa no resultado, ele vai setar o IdMoeda com o valor;
				moedaVO.setIdMoeda(resultado.getInt(1));
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método CadastrarMoedaDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return moedaVO;
	}
	
	public ArrayList<MoedaVO> consultarTodasMoedasDAO(){
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		
		ResultSet resultado = null;
		ArrayList<MoedaVO> listaMoedas = new ArrayList<>();
		String query = "SELECT idMoeda, nome, pais, ano, valor, detalhes, dataCadastro, imagem FROM moeda";
		
		try {
			resultado = stmt.executeQuery(query);
			while(resultado.next()) { // Laço de repetição indeterminado, só vai parar quando o Next não encontrar mais nenhum resultado, nesse caso, o next é usado para pegar um resultado de cada vez e só para quando não houver mais nenhum cadastro de moeda para mostrar.
				MoedaVO moeda = new MoedaVO();
				moeda.setIdMoeda(Integer.parseInt(resultado.getString(1))); // Poderia ter apenas utilizado um getInt, não precisava do parseInt. É apenas uma demonstração dos Wrappers.
				moeda.setNome(resultado.getString(2));
				moeda.setPais(resultado.getString(3));
				moeda.setAno(Integer.parseInt(resultado.getString(4)));
				moeda.setValor(resultado.getDouble(5));
				moeda.setDetalhes(resultado.getString(6));
				moeda.setDataCadastro(LocalDate.parse(resultado.getString(7), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				moeda.setImagem(resultado.getBytes(8));
				listaMoedas.add(moeda);
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTodasMoedasDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return listaMoedas;
	}
	
	public MoedaVO consultarMoedaDAO(int idMoeda){
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		
		String query = "SELECT idMoeda, nome, pais, ano, valor, detalhes, dataCadastro, imagem FROM moeda WHERE idMoeda = " + idMoeda;
		
		MoedaVO moeda = new MoedaVO();
		try {
			resultado = stmt.executeQuery(query);
			while(resultado.next()) { // Esse While deve executar apenas uma vez, pois a consulta é para apenas um ID.
				moeda.setIdMoeda(Integer.parseInt(resultado.getString(1))); // Poderia ter apenas utilizado um getInt, não precisava do parseInt. É apenas uma demonstração dos Wrappers.
				moeda.setNome(resultado.getString(2));
				moeda.setPais(resultado.getString(3));
				moeda.setAno(Integer.parseInt(resultado.getString(4)));
				moeda.setValor(resultado.getDouble(5));
				moeda.setDetalhes(resultado.getString(6));
				moeda.setDataCadastro(LocalDate.parse(resultado.getString(7), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				moeda.setImagem(resultado.getBytes(8));
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarMoedaDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return moeda;
	}
		
	public boolean verificarCadastroMoedaPorIDDAO(MoedaVO moedaVO) { // Método para verificar se cadastro existe pelo ID
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT idMoeda FROM moeda WHERE idMoeda = " + moedaVO.getIdMoeda();
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				retorno = true;
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarCadastroMoedaPorIDDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public boolean atualizarMoedaDAO(MoedaVO moedaVO) {
		boolean retorno = false;
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		
		String query = "";
		if(moedaVO.getImagem() != null && moedaVO.getImagem().length > 0) { // Se não for nulo e receber algo maior que zero significa que foi enviada uma imagem tambem, então usaremos a seguinte query. 
			query = "UPDATE moeda SET nome = ?, pais = ?, ano = ?, valor = ?, detalhes = ?, dataCadastro = ?, imgaem = ? WHERE idMoeda = ?";
			pstmt = Banco.getPreparedStatement(conn, query);
		} else { // Query sem atualizar a imagem
			query = "UPDATE moeda SET nome = ?, pais = ?, ano = ?, valor = ?, detalhes = ?, dataCadastro = ? WHERE idMoeda = ?";
			pstmt = Banco.getPreparedStatement(conn, query);
		}
		
		try {
			pstmt.setString(1, moedaVO.getNome());
			pstmt.setString(2, moedaVO.getPais());
			pstmt.setInt(3, moedaVO.getAno());
			pstmt.setDouble(4, moedaVO.getValor());
			pstmt.setString(5, moedaVO.getDetalhes());
			pstmt.setObject(6, moedaVO.getDataCadastro());
			if(moedaVO.getImagem() != null && moedaVO.getImagem().length > 0) {
				pstmt.setBytes(7, moedaVO.getImagem());
				pstmt.setInt(8, moedaVO.getIdMoeda());
			} else {
				pstmt.setInt(7, moedaVO.getIdMoeda());
			}
			if(pstmt.executeUpdate() == 1) { //executeUpdate é usado pq foi modificado uma linha da tabela. Preciso fazer == 1 pois será atualizado apenas 1 registro.
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarMoedaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public boolean excluirMoedaDAO(MoedaVO moedaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "DELETE FROM moeda WHERE idMoeda = " + moedaVO.getIdMoeda(); 
		try {
			if(stmt.executeUpdate(query) == 1) { // Está excluindo apenas 1 linha, então 1 seria true e 0 seria false
				retorno = true;
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método excluirMoedaDao!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public byte[] consultarImagemMoedaDAO(int idMoeda) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultado = null;
		byte[] retorno = null;
		String query = "SELECT imagem FROM moeda Where idmoeda = ?";
		
		try {
			pstmt = Banco.getPreparedStatement(conn, query);
			pstmt.setInt(1, idMoeda);
			resultado = pstmt.executeQuery();
			if(resultado.next()) {
				retorno = resultado.getBytes("imagem");
			}
		}catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarImagemMoedaDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
}
	