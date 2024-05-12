package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Controller;

import com.fatec.LBDAvaliacao02.model.Aula;
import com.fatec.LBDAvaliacao02.persistence.AulaDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

@Controller
public class AulaController 
{
	
	/**
	 * Lista informações de uma aula para um aluno com base no ID do conteúdo.
	 *
	 * @param idConteudo O ID do conteúdo da aula.
	 * @return Um objeto Aula contendo as informações da aula para o aluno.
	 * @throws SQLException            Se ocorrer um erro durante a operação SQL.
	 * @throws ClassNotFoundException Se a classe não for encontrada.
	 */
	public Aula listarAlunoAula(int idConteudo) throws SQLException, ClassNotFoundException 
	{
		GenericDao gDao = new GenericDao();
		AulaDao aDao = new AulaDao(gDao);
		Aula aula = aDao.listarAlunoAula(idConteudo);
		
		return aula;
	}
	
	/**
	 * Seleciona a chamada para uma determinada disciplina e conteúdo.
	 *
	 * @param set                    Um conjunto contendo as entradas das listas de presença dos alunos.
	 * @param codigoDisciplinaConteudo O código da disciplina e conteúdo relacionado à chamada.
	 * @throws SQLException            Se ocorrer um erro durante a operação SQL.
	 * @throws ClassNotFoundException Se a classe não for encontrada.
	 */
	public void selecionarChamada(List<Entry<String, List<Integer>>> set, int codigoDisciplinaConteudo) throws SQLException, ClassNotFoundException 
	{
		GenericDao gDao = new GenericDao();
		AulaDao aDao = new AulaDao(gDao);
		aDao.selecionarChamada(set, codigoDisciplinaConteudo);
	}
}
