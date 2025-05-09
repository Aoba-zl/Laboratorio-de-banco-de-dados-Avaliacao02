package com.fatec.LBDAvaliacao02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ModelMap;
import com.fatec.LBDAvaliacao02.model.Telefone;
import com.fatec.LBDAvaliacao02.servlet.AlunoServlet;

@SpringBootTest
public class TestTrancarMatriculaAluno {
	@Autowired
	private AlunoServlet AlunoServlet;
	private ModelMap modelMap = new ModelMap();
	private Map<String, String> parametros = new HashMap<>();
	private List<Telefone> telefones;
	@BeforeEach
	private void criarAluno() {
		modelMap = new ModelMap();
        parametros.put("botao", "Trancar");
        parametros.put("ra", "");
        parametros.put("cpf", "");
        parametros.put("nome", "");
        parametros.put("nomeSocial", "");
        parametros.put("dtNasc", "");
        parametros.put("emailPessoal", "");
        parametros.put("emailCorporativo", "");
        parametros.put("instituicaoConclusaoSegGrau", "");
        parametros.put("dtConclusaoSegGrau", "");
        parametros.put("tabCursos", "");
        parametros.put("posicao", "");
        parametros.put("pontuacao", "");

        // Simula os telefones
        telefones = Arrays.asList(
                new Telefone(""),
                new Telefone(""),
                new Telefone("")
        );
	}
	
	@Test
	void REQ03CT01() throws ClassNotFoundException, SQLException {
		parametros.put("ra","200014268");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Esse RA não existe!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ03CT02() throws ClassNotFoundException, SQLException {
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Esse RA não existe!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ03CT03() throws ClassNotFoundException, SQLException {
		parametros.put("ra","202511773");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Matricula trancada!",(String) modelMap.get("saida"));
	}
}
