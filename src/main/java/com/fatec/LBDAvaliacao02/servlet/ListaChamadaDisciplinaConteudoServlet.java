package com.fatec.LBDAvaliacao02.servlet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.LBDAvaliacao02.controller.CursoController;
import com.fatec.LBDAvaliacao02.controller.DisciplinaController;
import com.fatec.LBDAvaliacao02.model.Curso;
import com.fatec.LBDAvaliacao02.model.Disciplina;

/**
 * Essa Classe é responsável por fazer o request do .jsp do tipo lista-chamada.
 */
@Controller
public class ListaChamadaDisciplinaConteudoServlet
{
	@RequestMapping(name = "lista-chamada-disciplina-conteudo", value = "/lista-chamada/{codigo}/{codigoDisciplina}", method = RequestMethod.GET)
	public ModelAndView listaChamadaDisciplinaAulaGet(@PathVariable("codigoDisciplina") int codigoDisciplina, ModelMap model)
	{
		Disciplina disciplina = new Disciplina();
		DisciplinaController dControl = new DisciplinaController();
		String erro = "";
		
		try {			
			disciplina = dControl.listarDisciplinaConteudo(codigoDisciplina);
		} catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			
			model.addAttribute("erro", erro);
			model.addAttribute("disciplina", disciplina);
		}
		
		return new ModelAndView("lista-chamada-disciplina-conteudo");
	}

	@RequestMapping(name = "lista-chamada-disciplina-aula", value = "/lista-chamada/{codigo}/{codigoDisciplina}", method = RequestMethod.POST)
	public ModelAndView listaChamadaDisciplinaAulaPost(@PathVariable("codigoDisciplina") int codigoDisciplina, ModelMap model)
	{
		return new ModelAndView("lista-chamada-disciplina-conteudo");
	}
}
