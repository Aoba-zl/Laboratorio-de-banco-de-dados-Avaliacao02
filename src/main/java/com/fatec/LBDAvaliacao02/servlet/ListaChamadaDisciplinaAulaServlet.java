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
import com.fatec.LBDAvaliacao02.model.Curso;

/**
 * Essa Classe é responsável por fazer o request do .jsp do tipo lista-chamada.
 */
@Controller
public class ListaChamadaDisciplinaAulaServlet
{
	@RequestMapping(name = "lista-chamada-disciplina-aula", value = "/lista-chamada/{codigo}/{codigoDisciplina}", method = RequestMethod.GET)
	public ModelAndView listaChamadaDisciplinaAulaGet(@PathVariable("codigoDisciplina") int codigoDisciplina, ModelMap model)
	{
		List<Curso> cursos = new ArrayList<>();
		CursoController cControl = new CursoController();
		String erro = "";
		
		try {			
			cursos = cControl.listar();
		} catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			
			model.addAttribute("erro", erro);
			model.addAttribute("cursos", cursos);
		}
		
		return new ModelAndView("lista-chamada-disciplina-aula");
	}

	@RequestMapping(name = "lista-chamada-disciplina-aula", value = "/lista-chamada/{codigo}/{codigoDisciplina}", method = RequestMethod.POST)
	public ModelAndView listaChamadaDisciplinaAulaPost(@PathVariable("codigoDisciplina") int codigoDisciplina, ModelMap model)
	{
		return new ModelAndView("lista-chamada-disciplina-aula");
	}
}
