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
import model.vo.MoedaVO;

@Path("/colecionador")
public class ColecionadorController {

	
	@POST //Usado quando utilizamos o Insert no MySQL
	@Path("/cadastrar")
	@Consumes(MediaType.MULTIPART_FORM_DATA) //Consumes é o que estou esperando receber com essa requisição. Nesse caso está esperando Multipart_Form_Data
	@Produces(MediaType.APPLICATION_JSON) //Produces é o que se espera devolver. Nesse caso será devolvido um arquivo JSON
	public MoedaVO casdastrarColecionadorController(@FormDataParam("file") InputStream fileInsputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData,
			@FormDataParam("moedaVO") InputStream moedaInsputStream) throws Exception{
		MoedaBO moedaBO = new MoedaBO();
		return moedaBO.cadastrarMoedaBO(moedaInsputStream, fileInsputStream, fileMetaData);
	}
	 
	@GET //Quando utilizamos o Select no MySQL
	@Path("/listar")
	@Produces(MediaType.MULTIPART_FORM_DATA)// Será enviada a imagem e os dados da moeda, por isso é multipart
	public Response consultarTodasMoedasController(){
		MoedaBO moedaBO = new MoedaBO();
		return moedaBO.consultarTodasMoedasBO();
	}
	 
/*	@GET
	@Path("/pesquisar/{idmoeda}")
	@Consumes(MediaType.APPLICATION_JSON)//Recebe um JSON que é o ID recebendo na URL
	@Produces(MediaType.MULTIPART_FORM_DATA)// Devolve um multipart
	public Response consultarMoedaController(@PathParam("idmoeda") int idMoeda) {
		MoedaBO moedaBO = new MoedaBO();
		return moedaBO.consultarMoedaBO(idMoeda);
	}
*/	
	 
	 
	@PUT //Quando utilizamos o Update no MySQL
	@Path("/atualizar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)//Consume um MultiPart
	@Produces(MediaType.APPLICATION_JSON)//Devolve um JSON     
	public Boolean atualizarMoedaController(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData,
			@FormDataParam("moedaVO") InputStream moedaInputStream) throws Exception{
		MoedaBO moedaBO = new MoedaBO();
		return moedaBO.atualizarMoedaBO(moedaInputStream, fileInputStream, fileMetaData);
	}
	
	@DELETE //Quando utilizamos o Delete no MySQL
	@Path("/excluir")
	@Consumes(MediaType.APPLICATION_JSON)//Consume um JSON que é o Id
	@Produces(MediaType.APPLICATION_JSON)//Devolve um JSON boolean
	public Boolean excluirMoedaController(MoedaVO moedaVO) {
		MoedaBO moedaBO = new MoedaBO();
		return moedaBO.excluirMoedaBO(moedaVO);
	}
	
	@GET
	@Path("/imagemMoeda/{idMoeda}")
	@Produces("image/jpeg")
	public Response consultarImagemMoedaController(@PathParam("idMoeda") int idMoeda) {
		MoedaBO moedaBO = new MoedaBO();
		return moedaBO.consultarImagemMoedaBO(idMoeda);
	}
}
