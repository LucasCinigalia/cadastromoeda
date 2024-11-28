package model.bo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import model.dao.MoedaDAO;
import model.vo.MoedaVO;
import model.vo.UsuarioVO;

public class MoedaBO {
	

	private byte[] converterByteParaArray(InputStream inputStream) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int read = 0;
		byte[] dados = new byte[1024];
		while((read = inputStream.read(dados, 0, dados.length)) != -1) {
			buffer.write(dados, 0, read);
		}
		buffer.flush();
		return buffer.toByteArray();
	}
	
	public MoedaVO cadastrarMoedaBO(InputStream moedaInputStream, InputStream fileInputStream, FormDataContentDisposition fileMetaData) {//Leitura de arquivos são feitas sempre utilizando esse mesmo método, igual receita de bolo
		MoedaDAO moedaDAO = new MoedaDAO();
		MoedaVO moedaVO = null;
		
		try {
			byte[] arquivo = this.converterByteParaArray(fileInputStream);//Lê o conteúdo do arquivo
			
			String moedaJSON = new String(this.converterByteParaArray(moedaInputStream), StandardCharsets.UTF_8);//Lê conteudo do JSON
			
			//Converter o JSON em um objeto Java
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			moedaVO = objectMapper.readValue(moedaJSON, MoedaVO.class);
			//Linha 32 até 41 foi para adaptar oq recebeu do front end para o Java
			
			if(moedaDAO.verificarCadastroMoedaBancoDAO(moedaVO)) {
				System.out.println("Moeda já cadastrada no banco de dados!");
			} else {
				moedaVO = moedaDAO.cadastrarMoedaDAO(moedaVO);
			}
		} catch(FileNotFoundException erro) {
			System.out.println(erro);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return moedaVO;
	}
	
	public Response consultarTodasMoedasBO() {
		//UsuarioBO usuarioBO = new UsuarioBO();
		
		MoedaDAO moedaDAO = new MoedaDAO();
		ArrayList<MoedaVO> listaMoedasVO = moedaDAO.consultarTodasMoedasDAO();
		if(listaMoedasVO.isEmpty()) {
			System.out.println("\nLista de Moedas está vazia.");
			return Response.status(Response.Status.NO_CONTENT).entity("Nenhuma moeda encontrada.").build();
		}
		
		//Entregar objeto MultiPart
		MultiPart multiPart = new FormDataMultiPart();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		
		try {
			//Enviando os dados da moeda separada da imagem Multipart
			for(MoedaVO moedaVO : listaMoedasVO) {
				byte[] imagem = moedaVO.getImagem();
				moedaVO.setImagem(null);
				
				//Adiciona o JSON das moedas
				String moedaJson = objectMapper.writeValueAsString(moedaVO);
				multiPart.bodyPart(new StreamDataBodyPart("MoedaVO", new ByteArrayInputStream(moedaJson.getBytes()), moedaVO.getIdMoeda() + "-moeda.json"));
				
				//Adiciona a imagem como um arquivo
				if(imagem != null) {
					multiPart.bodyPart(new StreamDataBodyPart("imagem", new ByteArrayInputStream(imagem), moedaVO.getIdMoeda() + "-imagem.jpg"));
				}
			}
			return Response.ok(multiPart).build();
		}catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao processar a resposta multipart.").build();
		}
	}
	public Response consultarMoedaBO(int idMoeda) {
		MoedaDAO moedaDAO = new MoedaDAO();
		MoedaVO moedaVO = moedaDAO.consultarMoedaDAO(idMoeda);
		if(moedaVO == null) {
			System.out.println("\nObjeto Moeda está vazio!");
			return Response.status(Response.Status.NO_CONTENT).entity("Moeda não encontrada.").build();

		}
		
		MultiPart multiPart = new FormDataMultiPart();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		
		try {
			byte[] imagem = moedaVO.getImagem();
			moedaVO.setImagem(null);
			
			//Adiciona o JSON das moedas
			String moedaJson = objectMapper.writeValueAsString(moedaVO);
			multiPart.bodyPart(new StreamDataBodyPart("MoedaVO", new ByteArrayInputStream(moedaJson.getBytes()), moedaVO.getIdMoeda() + "-moeda.json"));
			
			//Adiciona a imagem como um arquivo
			if(imagem != null) {
				multiPart.bodyPart(new StreamDataBodyPart("imagem", new ByteArrayInputStream(imagem), moedaVO.getIdMoeda() + "-imagem.jpg"));
			}
			return Response.ok(multiPart).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao processar a resposta multipart.").build();
		}
	}
	
	public Boolean atualizarMoedaBO(InputStream moedaInputStream, InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
		boolean resultado = false;
		MoedaDAO moedaDAO = new MoedaDAO();
		MoedaVO moedaVO = null;
		try {
			//Lê o conteúdo do JSON 
			byte[] arquivo = null;
			if(fileInputStream != null) {
				arquivo = this.converterByteParaArray(fileInputStream);
			}
			
			
			//Lê o conteudo JSON
			String moedaJSON = new String(this.converterByteParaArray(moedaInputStream), StandardCharsets.UTF_8);
			
			//Converte o JSON em um objeto Java
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			moedaVO = objectMapper.readValue(moedaJSON, MoedaVO.class);
			if(arquivo.length > 0) {
				moedaVO.setImagem(arquivo);
			}
			if(moedaDAO.verificarCadastroMoedaPorIDDAO(moedaVO)) {
				resultado = moedaDAO.atualizarMoedaDAO(moedaVO);
			}else {
				System.out.println("Moeda não consta na base de dados!");
			}
		} catch(FileNotFoundException erro) {
			System.out.println(erro);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Boolean excluirMoedaBO(MoedaVO moedaVO) {
		boolean resultado = false;
		MoedaDAO moedaDAO = new MoedaDAO();
		if(moedaDAO.verificarCadastroMoedaPorIDDAO(moedaVO)) {
			resultado = moedaDAO.excluirMoedaDAO(moedaVO);
		}else {
			System.out.println("\nMoeda não existe na base de dados!");
		}
		return resultado;
	}
	
	public Response consultarImagemMoedaBO(int idMoeda) {
		MoedaDAO moedaDAO = new MoedaDAO();
		try {
			byte[] imagem = moedaDAO.consultarImagemMoedaDAO(idMoeda);
			if(imagem != null) {
				return Response.ok(new ByteArrayInputStream(imagem)).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("Imagem não encontrada para o ID especificado").build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao consultar imagem da pessoa").build();
		}
	}
	
	
}
