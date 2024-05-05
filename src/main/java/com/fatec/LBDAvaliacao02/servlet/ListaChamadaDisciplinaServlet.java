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
public class ListaChamadaDisciplinaServlet
{
	@RequestMapping(name = "lista-chamada-disciplina", value = "/lista-chamada/{codigo}", method = RequestMethod.GET)
	public ModelAndView listaChamadaDisciplinaGet(@PathVariable("codigo") int codigo, ModelMap model)

	{
		List<Disciplina> disciplinas = new ArrayList<>();
		DisciplinaController dControl = new DisciplinaController();
		String erro = "";
		
		System.out.println(codigo);
		
		try {			
			disciplinas = dControl.listarDisciplinaCurso(codigo);
		} catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", disciplinas);
		}
		
		return new ModelAndView("lista-chamada-disciplina");
	}


	@RequestMapping(name = "lista-chamada-disciplina", value = "/lista-chamada/{codigo}", method = RequestMethod.POST)
	public ModelAndView listaChamadaDisciplinaPost(@PathVariable("codigo") long codigo, ModelMap model)
	{
		return new ModelAndView("lista-chamada-disciplina");
	}
}
