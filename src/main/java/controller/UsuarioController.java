package controller;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.bo.MoedaBO;
import model.bo.UsuarioBO;
import model.vo.MoedaVO;
import model.vo.UsuarioVO;


@Path("/usuario")
public class UsuarioController {

	
	@POST //Usado quando utilizamos o Insert no MySQL
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON) //Consumes é o que estou esperando receber com essa requisição.
	@Produces(MediaType.APPLICATION_JSON) //Produces é o que se espera devolver. Nesse caso será devolvido um arquivo JSON
	public UsuarioVO casdastrarUsuarioController(UsuarioVO usuarioVO) throws Exception{
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.cadastrarUsuarioBO(usuarioVO);
	}
	 
	 
	@PUT //Quando utilizamos o Update no MySQL
	@Path("/atualizar")
	@Consumes(MediaType.APPLICATION_JSON)//Consume um Json
	@Produces(MediaType.APPLICATION_JSON)//Devolve um JSON     
	public Boolean atualizarUsuarioController(UsuarioVO usuarioVO) throws Exception{
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.atualizarUsuarioBO(usuarioVO);
	}
	
	@PUT 
	@Path("/excluir")
	@Consumes(MediaType.APPLICATION_JSON)//Consume um JSON que é o Id
	@Produces(MediaType.APPLICATION_JSON)//Devolve um JSON boolean
	public Boolean excluirUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.excluirUsuarioBO(usuarioVO);
	}
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logarUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.logarUsuarioBO(usuarioVO.getLogin(), usuarioVO.getSenha());
	}
}
