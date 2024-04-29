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
import com.fatec.LBDAvaliacao02.model.MatriculaDisciplina;

/**
 * Essa Classe é responsável por fazer o allRequestParam do .jsp do tipo disciplina.
 */
@Controller
public class HistoricoServlet
{
	@RequestMapping(name = "historico", value = "/historico", method = RequestMethod.GET)
	public ModelAndView historicoGet(HttpServletRequest request, ModelMap model)
	{
		return new ModelAndView("historico");
	}
	
	@RequestMapping(name = "historico", value = "/historico", method = RequestMethod.POST)
	public ModelAndView historicoPost(@RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model)
	{
		//entrada
		String cmd = allRequestParam.get("botao"); // Obtém o valor do botão.
		String ra = allRequestParam.get("ra"); // Obtém o valor do ra.
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
				return new ModelAndView("historico");
			}
			
			if(cmd.contains("Buscar")) // Condição para verificar se terá busca do usuário.
			{
				disciplinas = dControl.buscarAlunoDisciplina(ra);
				MatriculaDisciplina md = new MatriculaDisciplina();
				md = disciplinas.get(0).getUmMatriculaDisciplina();
				matricula = md.getIdMatricula();
				
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
		return new ModelAndView("historico");
	}

}
