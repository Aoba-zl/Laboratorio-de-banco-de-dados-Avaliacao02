package com.fatec.LBDAvaliacao02.servlet;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fatec.LBDAvaliacao02.controller.AulaController;
import com.fatec.LBDAvaliacao02.controller.CursoController;
import com.fatec.LBDAvaliacao02.model.Aula;
import com.fatec.LBDAvaliacao02.model.Curso;
import com.fatec.LBDAvaliacao02.model.Matricula;
import com.fatec.LBDAvaliacao02.model.Presenca;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Essa Classe é responsável por fazer o request do .jsp do tipo lista-chamada.
 */
@Controller
public class ListaChamadaDisciplinaConteudoAulaServlet
{
	@RequestMapping(name = "lista-chamada-disciplina-conteudo-aula", value = "/lista-chamada/{codigo}/{codigoDisciplina}/{codigoDisciplinaConteudo}", method = RequestMethod.GET)
	public ModelAndView listaChamadaDisciplinaAulaGet(@PathVariable("codigoDisciplinaConteudo") int codigoDisciplinaConteudo, ModelMap model)
	{
		Aula aula = new Aula();
		AulaController aControl = new AulaController();
		String erro = "";
		
		try {			
			aula = aControl.listarAlunoAula(codigoDisciplinaConteudo);
		} catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			
			model.addAttribute("erro", erro);
			model.addAttribute("aula", aula);
		}
		
		return new ModelAndView("lista-chamada-disciplina-conteudo-aula");
	}

	@RequestMapping(name = "lista-chamada-disciplina-conteudo-aula", value = "/lista-chamada/{codigo}/{codigoDisciplina}/{codigoDisciplinaConteudo}", method = RequestMethod.POST)
	public ModelAndView listaChamadaDisciplinaAulaPost(@PathVariable("codigoDisciplinaConteudo") int codigoDisciplinaConteudo, @RequestParam Map<String, String> allRequestParam, HttpServletRequest request, ModelMap model)
	{
		AulaController aControl = new AulaController();
		String[] alunoIdPresencaRa = request.getParameterValues("checkboxAulaAluno");
		String cmd = allRequestParam.get("botao");
		
		String erro = "";
		
		
		try {
			if (alunoIdPresencaRa != null)
			{
				Map<String, List<Integer>> mapIdPresenca = new HashMap<>();
				
				for(String idRa : alunoIdPresencaRa)
				{
					String[] idRaSeparado = idRa.split("-");
					
					mapIdPresenca.computeIfAbsent(idRaSeparado[1], k -> new ArrayList<>()).add(Integer.parseInt(idRaSeparado[0]));
				}
				
				List<Entry<String, List<Integer>>> set = mapIdPresenca.entrySet().stream().toList();
				
				if (cmd.contains("confirmarChamada"))
				{
					aControl.selecionarChamada(set, codigoDisciplinaConteudo);
				}
			}
		} catch(SQLException | ClassNotFoundException e)
		{
			erro = e.getMessage();
		}
		
		return new ModelAndView("redirect:/lista-chamada");
	}
}
