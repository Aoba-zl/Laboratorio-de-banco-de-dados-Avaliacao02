package com.fatec.LBDAvaliacao02.servlet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.fatec.LBDAvaliacao02.controller.AlunoController;
import com.fatec.LBDAvaliacao02.controller.CursoController;
import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Curso;
import com.fatec.LBDAvaliacao02.model.Telefone;
import com.fatec.LBDAvaliacao02.model.Vestibular;

@Controller
public class AlunoServlet
{
	
	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.GET)
	public ModelAndView alunoGet(ModelMap model)
	{
		String erro = "";
		
		List<Aluno> alunos = new ArrayList<>();
		List<Curso> cursos = new ArrayList<>();
		try {
			alunos = getAlunos(alunos);
			cursos = getCursos(cursos);
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		}finally {
			model.addAttribute("erro", erro);
			model.addAttribute("alunos",alunos);
			model.addAttribute("cursos",cursos);
		}
		return new ModelAndView("aluno");
	}

	@RequestMapping(name = "aluno", value = "/aluno", method = RequestMethod.POST)
	public ModelAndView alunoPost(@RequestParam Map<String, String> allRequestParam, @RequestParam("telefone") List<Telefone> requestTelefonesParam, ModelMap model)
	{
		String saida ="";
		String erro = "";
		AlunoController alunoController = new AlunoController();
		String cmd = allRequestParam.get("botao");
		//Conteudo aluno
		String ra = allRequestParam.get("ra").trim();
		String cpf = allRequestParam.get("cpf").trim();
		String nome = allRequestParam.get("nome").trim();
		String nomeSocial = allRequestParam.get("nomeSocial").trim();
		String dtNascimento = allRequestParam.get("dtNasc");
		String emailPessoal = allRequestParam.get("emailPessoal").trim();
		String emailCorporativo = allRequestParam.get("emailCorporativo").trim();
		String instituicaoConclusaoSegGrau = allRequestParam.get("instituicaoConclusaoSegGrau").trim();
		String dtConclusao = allRequestParam.get("dtConclusaoSegGrau");
		String cursoId = allRequestParam.get("tabCursos");	
		
		//Conteudo telefone

		List<Telefone> telefones = requestTelefonesParam;

		//Conteudo Vestibular
		String posicao = allRequestParam.get("posicao");
		String pontuacao = allRequestParam.get("pontuacao");

		List<Aluno> alunos = new ArrayList<>();
		Aluno aluno = new Aluno();
		List<Curso> cursos = new ArrayList<>();
		Curso curso = new Curso();
		
		if (cmd.contains("Buscar") || cmd.contains("Alterar") || cmd.contains("Trancar")) {
			aluno.setRa(ra);
		}
		if (cmd.contains("Cadastrar")|| cmd.contains("Alterar")) {
			aluno.setCpf(cpf);
			aluno.setNome(nome);
			aluno.setNomeSocial(nomeSocial);
			if (dtNascimento != "") {
				aluno.setDtNascimento(toLocalDate(dtNascimento));
			}
			aluno.setEmailPessoal(emailPessoal);
			if (emailCorporativo.trim().length() > 2) {
				aluno.setEmailCorporativo(emailCorporativo);
			} else {
				aluno.setEmailCorporativo(null);
			}
			aluno.setInstituicaoConclusaoSegGrau(instituicaoConclusaoSegGrau);
			if (dtConclusao != "") {
				aluno.setDtConclusaoSegGrau(toLocalDate(dtConclusao));	
			}
			Vestibular vestibular = new Vestibular();
			if (pontuacao != "") {
				Float pont = Float.parseFloat(pontuacao);
				vestibular.setPontuacao(pont);
			}

			if (posicao != "") {
				int posi = Integer.parseInt(posicao);
				vestibular.setPosicao(posi);
			}
			aluno.setVestibular(vestibular);

			if (cursoId != null) {
				curso.setCodigo(Integer.parseInt(cursoId));
			} else {
				curso.setCodigo(-1);
			}
			aluno.setTelefone(telefones);
		}
		try {
			if (cmd.contains("Cadastrar")) {
				saida = alunoController.cadastrar(aluno,curso);
				//saida = "Aluno Cadastrado";
				if (saida.contains("Aluno cadastrado!")) {
					aluno = null;
				}
			}
			if (cmd.contains("Alterar")) {
				saida = alunoController.alterar(aluno,curso);
				//saida = "Aluno Alterado";
				if (saida.contains("Aluno atualizado!")) {
					aluno = null;
				}
			}
			if (cmd.contains("Trancar")) {
				saida = alunoController.excluir(aluno);
				//saida = "Aluno Excluido";
				if (saida.contains("Matricula trancada!")) {
					aluno = null;
				}
			}
			if (cmd.contains("Buscar")) {
				aluno = alunoController.buscar(aluno);
				
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		}finally {
			try {
				cursos = getCursos(cursos);
				alunos = getAlunos(alunos);
			} catch (SQLException | ClassNotFoundException e) {
				erro = e.getMessage();
			}
			
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno",aluno);
			model.addAttribute("alunos",alunos);
			model.addAttribute("cursos",cursos);
			if (cmd.contains("Buscar") && aluno.getTelefone().size() != 0) {
				model.addAttribute("telefones",aluno.getTelefone());
			} else {
				model.addAttribute("telefones",telefones);
			}
		}
		return new ModelAndView("aluno");
	}
	

	private LocalDate toLocalDate (String data) {
		LocalDate localdate = LocalDate.parse(data);
		return localdate;
	}
	private List<Curso> getCursos (List<Curso> cursos) throws ClassNotFoundException, SQLException {
		CursoController cursoController = new CursoController();
		cursos = cursoController.listarCompleto();
		return cursos;
		
	}
	private List<Aluno> getAlunos (List<Aluno> alunos) throws ClassNotFoundException, SQLException {
		AlunoController alunoController = new AlunoController();
		return alunoController.listar(alunos);
	}
}
