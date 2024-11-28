package model.bo;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.core.Response;
import model.dao.UsuarioDAO;
import model.vo.UsuarioVO;

public class UsuarioBO {

	
	public UsuarioVO cadastrarUsuarioBO(UsuarioVO usuarioVO) {//Leitura de arquivos são feitas sempre utilizando esse mesmo método, igual receita de bolo
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		
		if(usuarioDAO.verificarCadastroUsuarioBancoDAO(usuarioVO)) {
			System.out.println("Usuario já cadastrada no banco de dados!");
		} else {
			usuarioVO = usuarioDAO.cadastrarUsuarioDAO(usuarioVO);
		}
		return usuarioVO;
	}
	
			
	public Boolean atualizarUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
			
			if(usuarioDAO.verificarCadastroUsuarioPorIDDAO(usuarioVO)) {
				resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
			}else {
				System.out.println("Usuario não consta na base de dados!");
			}
		return resultado;
	}
	
	public Boolean excluirUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if(usuarioDAO.verificarCadastroUsuarioPorIDDAO(usuarioVO)) {
			resultado = usuarioDAO.excluirUsuarioLogicoDAO(usuarioVO);
		}else {
			System.out.println("\nUsuario não existe na base de dados!");
		}
		return resultado;
	}
	
	public Response logarUsuarioBO(String login, String senha) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		UsuarioVO usuarioVO = usuarioDAO.logarUsuarioDAO(login, senha);
		
		if(usuarioVO != null) {
			return Response.ok(usuarioVO).build();
		} else {
			System.out.println("Login ou senha inválidos.");
			return Response.status(Response.Status.UNAUTHORIZED).entity("Login ou senha inválidos").build();
		}
		
	}
}
