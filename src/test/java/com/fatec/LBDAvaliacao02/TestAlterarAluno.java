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
public class TestAlterarAluno {
	@Autowired
	private AlunoServlet AlunoServlet;
	
	private ModelMap modelMap = new ModelMap();
	private Map<String, String> parametros = new HashMap<>();
	private List<Telefone> telefones;
	
	@BeforeEach
	private void criarAluno() {
		modelMap = new ModelMap();
        parametros.put("botao", "Alterar");
        parametros.put("ra", "202511773");
        parametros.put("cpf", "54617362894");
        parametros.put("nome", "João da Silva");
        parametros.put("nomeSocial", "João");
        parametros.put("dtNasc", "2000-01-01");
        parametros.put("emailPessoal", "joao@email.com");
        parametros.put("emailCorporativo", "joao@corp.com");
        parametros.put("instituicaoConclusaoSegGrau", "Escola Estadual X");
        parametros.put("dtConclusaoSegGrau", "2018-12-15");
        parametros.put("tabCursos", "1");
        parametros.put("posicao", "5");
        parametros.put("pontuacao", "750");

        // Simula os telefones
        telefones = Arrays.asList(
                new Telefone("12345678"),
                new Telefone(""),
                new Telefone("")
        );
	}
	@Test
	void REQ01CT01() throws ClassNotFoundException, SQLException {
		parametros.put("cpf", "");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("CPF invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT02() throws ClassNotFoundException, SQLException {
		parametros.put("cpf", "12345678911");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("CPF inválido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT03() throws ClassNotFoundException, SQLException {
		parametros.put("nome", "EstaFraseTemComoIntuitoPossuirMaisDeCemCaracteresComOObjetivoDeTestarORequisitoREQ01CT03OndeNãoSePodeTerMaisDeCemCaracteres");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Nome invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT04() throws ClassNotFoundException, SQLException {
		parametros.put("nome", "");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Nome invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT05() throws ClassNotFoundException, SQLException {
		parametros.put("nomeSocial", "EstaFraseTemComoIntuitoPossuirMaisDeCemCaracteresComOObjetivoDeTestarORequisitoREQ01CT05OndeNãoSePodeTerMaisDeCemCaracteres");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Nome Social invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT06() throws ClassNotFoundException, SQLException {
		parametros.put("emailPessoal","EstaFraseTemComoIntuitoPossuirMaisDeCemCaracteresComOObjetivoDeTestarORequisitoREQ01CT06OndeNãoSePodeTerMaisDeCemCaracteres");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Email Pessoal invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT07() throws ClassNotFoundException, SQLException {
		parametros.put("emailPessoal","");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Email Pessoal invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT08() throws ClassNotFoundException, SQLException {
		parametros.put("emailPessoal","david.fagoni.gmail.com");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Email Pessoal invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT09() throws ClassNotFoundException, SQLException {
		parametros.put("emailPessoal","david.fagoni@gmail");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Email Pessoal invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT10() throws ClassNotFoundException, SQLException {
		
		parametros.put("emailCorporativo","EstaFraseTemComoIntuitoPossuirMaisDeCemCaracteresComOObjetivoDeTestarORequisitoREQ01CT010OndeNãoSePodeTerMaisDeCemCaracteres");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Email Corporativo invalido!",(String) modelMap.get("saida"));
	}

	@Test
	void REQ01CT11() throws ClassNotFoundException, SQLException {
		parametros.put("instituicaoConclusaoSegGrau","");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Nome da Instituição invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT12() throws ClassNotFoundException, SQLException {
		parametros.put("instituicaoConclusaoSegGrau","EstaFraseTemComoIntuitoPossuirMaisDeCemCaracteresComOObjetivoDeTestarORequisitoREQ01CT013OndeNãoSePodeTerMaisDeCemCaracteres");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Nome da Instituição invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT13() throws ClassNotFoundException, SQLException {
		parametros.put("dtConclusaoSegGrau","");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Data de conclusão invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT14() throws ClassNotFoundException, SQLException {
		parametros.put("dtConclusaoSegGrau","3000-02-02");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Data de conclusão invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT15() throws ClassNotFoundException, SQLException {
		parametros.put("dtNasc","2025-02-02");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Tem que ser maior de 15 anos!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT16() throws ClassNotFoundException, SQLException {
		parametros.put("dtNasc","");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Data de nascimento invalido!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT17() throws ClassNotFoundException, SQLException {
		telefones = Arrays.asList(
                new Telefone(""),
                new Telefone(""),
                new Telefone("")
        );
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Ao menos 1 telefone deve ser preenchido",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT18() throws ClassNotFoundException, SQLException {
		telefones = Arrays.asList(
                new Telefone("1234567"),
                new Telefone(""),
                new Telefone("")
        );
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Telefone invalido",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT19() throws ClassNotFoundException, SQLException {
		telefones = Arrays.asList(
                new Telefone("12345678910"),
                new Telefone(""),
                new Telefone("")
        );
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Telefone invalido",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT20() throws ClassNotFoundException, SQLException {
		parametros.put("pontuacao","-1");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Pontuação invalida!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT21() throws ClassNotFoundException, SQLException {
		parametros.put("pontuacao", "1000.55");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Pontuação invalida!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT22() throws ClassNotFoundException, SQLException {
		parametros.put("posicao","-1");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Posição invalida!",(String) modelMap.get("saida"));
	}

	@Test
	void REQ01CT23() throws ClassNotFoundException, SQLException {
		parametros.put("posicao","1234567891");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Posição invalida!",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT25() throws ClassNotFoundException, SQLException {
		parametros.put("tabCursos","-1");
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Curso não selecionado",(String) modelMap.get("saida"));
	}
	@Test
	void REQ01CT26() throws ClassNotFoundException, SQLException {
		parametros.put("cpf","54617362894");
		parametros.put("emailCorporativo","");
        telefones = Arrays.asList(
                new Telefone("12345678"),
                new Telefone("54673272"),
                new Telefone("")
        );
		AlunoServlet.alunoPost(parametros, telefones, modelMap);
		assertEquals("Aluno atualizado!",(String) modelMap.get("saida"));
	}
}
