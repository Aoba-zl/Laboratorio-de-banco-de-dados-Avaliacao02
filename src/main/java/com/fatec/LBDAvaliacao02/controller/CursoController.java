package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.fatec.LBDAvaliacao02.model.Curso;
import com.fatec.LBDAvaliacao02.persistence.CursoDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

/**
 * Essa Classe é responsável por ser o controlador dos dados da entidade Curso.
 */
@Controller
public class CursoController 
{
	/**
	 * Método responsável por listar a tabela de curso.
	 * @return Uma lista de objetos do tipo Curso
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public List<Curso> listar() throws SQLException, ClassNotFoundException 
	{
		GenericDao gDao = new GenericDao();
		CursoDao cDao = new CursoDao(gDao);
		List<Curso> cursos = cDao.listar();
		return cursos;
	}
	
	/**
	 * Método responsável por listar a tabela de curso.
	 * @return
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public List<Curso> listarCompleto() throws SQLException, ClassNotFoundException 
	{
		GenericDao gDao = new GenericDao();
		CursoDao cDao = new CursoDao(gDao);
		List<Curso> cursos = cDao.listar();
		return cursos;
	}
}
