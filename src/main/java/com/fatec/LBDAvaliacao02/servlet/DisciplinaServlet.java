package com.fatec.LBDAvaliacao02.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.LBDAvaliacao02.controller.DisciplinaController;
import com.fatec.LBDAvaliacao02.model.Disciplina;
import com.fatec.LBDAvaliacao02.model.Matricula;
import com.fatec.LBDAvaliacao02.model.MatriculaDisciplina;

/**
 * Essa Classe é responsável por fazer o allRequestParam do .jsp do tipo disciplina.
 */
@Controller
public class DisciplinaServlet
{
	@RequestMapping(name = "disciplina", value = "/disciplina", method = RequestMethod.GET)
	public ModelAndView disciplinaGet(HttpServletRequest request, ModelMap model)
	{
		HttpSession session = request.getSession();
		session.invalidate(); // Invalida/limpa a sessão do usuário.
		return new ModelAndView("disciplina");
	}
	
	@RequestMapping(name = "disciplina", value = "/disciplina", method = RequestMethod.POST)
	public ModelAndView disciplinaPost(@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model)
	{
		//entrada
		HttpSession session = request.getSession();
		String cmd = allRequestParam.get("botao"); // Obtém o valor do botão.
		String ra = allRequestParam.get("ra"); // Obtém o valor do ra.
		String[] cod = request.getParameterValues("checkboxDisciplina"); // Obtém a array de código das checkbox selecionadas.
		String matricula = ""; // String matricula iniciada para a obtenção do valor depois.
		List<Disciplina> disciplinas = new ArrayList<>();
		
		
		//saida
		String saida = "";
		String erro = "";
		DisciplinaController dControl = new DisciplinaController();
		
		try {
			if(dControl.validar(ra)) // Verifica se o campo ra não está em branco.
			{
				saida = "Ra em branco!";
				return new ModelAndView("disciplina");
			}
			
			if(cmd.contains("Buscar")) // Condição para verificar se terá busca do usuário.
			{
				disciplinas = dControl.buscarAlunoDisciplina(ra);
				MatriculaDisciplina md = new MatriculaDisciplina();
				md = disciplinas.get(0).getUmMatriculaDisciplina();
				matricula = String.valueOf(md.getMatricula().getId());
				
				session.setAttribute("disciplinas", disciplinas); // Guarda a lista de disciplina na sessão.
				session.setAttribute("matricula", matricula); // Guarda a String da matricula na sessão.
			}
			if(cmd.contains("escolherDisciplina")) // Condição para verificar se terá escolha de disciplina do usuário.
			{
				if(!(cod == null))
				{
					matricula = (String) session.getAttribute("matricula"); // Recebe a String de matricula da sessão.
					disciplinas = (List<Disciplina>) session.getAttribute("disciplinas"); // Recebe a lista de disciplina da sessão.
					
					if(dControl.verificaHorario(disciplinas, cod)) // Verifica se o horário tem conflito com outro horário.
					{
						saida = "Há algum conflito de horário!";
						return new ModelAndView("disciplina");
					}
					
					
					List<MatriculaDisciplina> mdList = new ArrayList<MatriculaDisciplina>();
					for(String c : cod) // ForEach do código para criar MatriculaDisciplina com seus devidos valores.
					{
						Matricula mMd = new Matricula();
						Disciplina dMd = new Disciplina();
						MatriculaDisciplina md = new MatriculaDisciplina();
						dMd.setCodigo(Integer.parseInt(c));
						md.setDisciplina(dMd);
						
						mMd.setId(Integer.parseInt(matricula));
						md.setMatricula(mMd);
						md.setStatus("Em andamento.");
						
						mdList.add(md);
					}
					
					saida = dControl.escolheDisciplina(mdList); // Manda a lista de MatriculaDisciplina e retorna uma resposta para a saida.
					
					disciplinas = dControl.buscarAlunoDisciplina(ra); // Obtém a nova lista de disciplinas atualizada.
				}
				else
				{
					saida = "Selecione as caixa das disciplinas que queira fazer!";
				}
			}
			
		} catch (SQLException | ClassNotFoundException e)
		{
			erro = e.getMessage();
		} finally {
			
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("ra", ra);
			model.addAttribute("disciplinas", disciplinas);
		}
		return new ModelAndView("disciplina");
	}

}
