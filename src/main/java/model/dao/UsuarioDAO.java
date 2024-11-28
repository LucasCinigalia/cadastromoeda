package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import model.vo.MoedaVO;
import model.vo.UsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioDAO {

	public boolean verificarCadastroUsuarioBancoDAO(UsuarioVO usuarioVO){
		Connection conn = Banco.getConnection(); //Método criado na classe Banco - linhas 19 à 31. Pegando coexão.
		Statement stmt = Banco.getStatement(conn); //Statement executa o MySQL.
		ResultSet resultado = null;
		
		boolean retorno = false; // Estamos partindo do presuposto de que o usuario ainda não está cadastrada. Se ela estiver, a linha 29 vai mudar para true.
		String query = "SELECT idusuario FROM usuario WHERE email = '" + usuarioVO.getEmail() + "' "; // Precisa sempre colocar as aspas simples nas Strings para ficar no padrão para o MySQL.
		//Validação feita por meio do email.
		try{
			resultado = stmt.executeQuery(query);
			if(resultado.next()){
				retorno = true;
			}
		} catch(SQLException erro){
			System.out.println("Erro ao executar a query do método verificarCadastroUsuarioBancoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally{
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);                           
		}
		return retorno;
	}
	
	public UsuarioVO cadastrarUsuarioDAO(UsuarioVO usuarioVO) {
		String query =  "INSERT INTO usuario (nome, email, login, senha, dataCadastro) VALUES (?, ?, ?, ?, ?)"; // ? São mascaras, para depois informar o valor utilizando o numero de cada ?.
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query); // Para pegar um Prepared Statement preciso já ter informado qual a query que quero executar, por isso a query veio antes.
		ResultSet resultado = null;
		try {
			pstmt.setString(1, usuarioVO.getNome()); // Nesses casos, as aspas simples são feitas automaticamente.
			pstmt.setString(2, usuarioVO.getEmail());
			pstmt.setString(3, usuarioVO.getLogin());
			pstmt.setString(4, usuarioVO.getSenha());
			pstmt.setDate(5, Date.valueOf(LocalDate.now()));
			pstmt.execute(); //Um INSERT utilizando preparedStatement, só precisamos utilizar o execute.
			resultado = pstmt.getGeneratedKeys(); // Esse método passará o ID
			if(resultado.next()) { //Esse if serve para se receber alguma coisa no resultado, ele vai setar o IdUsuario com o valor;
				usuarioVO.setIdUsuario(resultado.getInt(1));
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método CadastrarUsuarioDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return usuarioVO;
	}
		
	public boolean verificarCadastroUsuarioPorIDDAO(UsuarioVO usuarioVO) { // Método para verificar se cadastro existe pelo ID
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT idUsuario FROM usuario WHERE idUsuario = " + usuarioVO.getIdUsuario();
		try {
			resultado = stmt.executeQuery(query);
			if(resultado.next()) {
				retorno = true;
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarCadastroUsuarioPorIDDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public boolean atualizarUsuarioDAO(UsuarioVO usuarioVO) {
		boolean retorno = false;
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		
		String query = "UPDATE usuario SET nome = ?, email = ?, login = ?, senha = ? WHERE idUsuario = ?";
		pstmt = Banco.getPreparedStatement(conn, query);
		
	//	ERRO AQUI(COMO VOU RECEBER ESSE ID DE USUARIO?)
		try {
			pstmt.setString(1, usuarioVO.getNome());
			pstmt.setString(2, usuarioVO.getEmail());
			pstmt.setString(3, usuarioVO.getLogin());
			pstmt.setString(4, usuarioVO.getSenha());
			pstmt.setInt(5, usuarioVO.getIdUsuario());
			
			if(pstmt.executeUpdate() == 1) { //executeUpdate é usado pq foi modificado uma linha da tabela. Preciso fazer == 1 pois será atualizado apenas 1 registro.
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public boolean confirmarLoginUsuarioBancoDAO(UsuarioVO usuarioVO){
		Connection conn = Banco.getConnection(); //Método criado na classe Banco - linhas 19 à 31. Pegando coexão.
		Statement stmt = Banco.getStatement(conn); //Statement executa o MySQL.
		ResultSet resultado = null;
		
		boolean retorno = false; // Usuario ainda não está cadastrado. Se ela estiver, a linha 133 vai mudar para true.
		String query = "SELECT idusuario FROM usuario WHERE login = '" + usuarioVO.getLogin() + "' AND senha = '" + usuarioVO.getSenha() + "' "; // Precisa sempre colocar as aspas simples nas Strings para ficar no padrão para o MySQL.
		//Validação feita por meio do email.
		try{
			resultado = stmt.executeQuery(query);
			if(resultado.next()){
				retorno = true;
			}
		} catch(SQLException erro){
			System.out.println("Erro ao executar a query do método confirmarLoginUsuarioBancoDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally{
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);                           
		}
		return retorno;
	}
	
	
	//ERRO AQUI(PRECISO RECEBER O ID USUARIO PARA EXCLUIR)
	public boolean excluirUsuarioLogicoDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		boolean retorno = false;
		String query = "UPDATE usuario SET dataExpiracao = ? WHERE idUsuario = ?"; 
		pstmt = Banco.getPreparedStatement(conn, query);
		try {
			pstmt.setDate(1, Date.valueOf(LocalDate.now()));
			pstmt.setInt(2, usuarioVO.getIdUsuario());
			if(pstmt.executeUpdate() == 1) {
				 retorno = true;
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a query do método excluirUsuarioLogicoDao!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
	public UsuarioVO logarUsuarioDAO(String login, String senha) {
		UsuarioVO usuarioVO = null;
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultado = null;

		String query = "SELECT * FROM usuario WHERE login = ? AND senha = ?";

		try {
		    pstmt = conn.prepareStatement(query); // Prepara a query
		    pstmt.setString(1, login);            
		    pstmt.setString(2, senha);            
		    resultado = pstmt.executeQuery();// Executa a consulta

		    if(resultado.next()) {
		    	Date dataExpiracao = resultado.getDate("dataExpiracao");
		    	if(dataExpiracao != null){
		    		System.out.println("Usuario inativo");
		    	} else { // Verifica se existe um resultado
			        usuarioVO = new UsuarioVO();
			        usuarioVO.setIdUsuario(resultado.getInt("idUsuario"));  // Mapeia os campos do ResultSet
			        usuarioVO.setNome(resultado.getString("nome"));
			        usuarioVO.setEmail(resultado.getString("email"));
			        usuarioVO.setLogin(resultado.getString("login"));
			        usuarioVO.setSenha(resultado.getString("senha"));
			        usuarioVO.setDataCadastro(resultado.getDate("dataCadastro").toLocalDate());
		    	
			    
			    } 
		    }
		    
		} catch (SQLException erro) {
		    System.out.println("Erro ao executar a query do método logarUsuarioDAO!");
		    System.out.println("Erro: " + erro.getMessage());
		} finally {
		    Banco.closeResultSet(resultado);
		    Banco.closePreparedStatement(pstmt);
		    Banco.closeConnection(conn);
		}

		return usuarioVO;
	}
	
}
