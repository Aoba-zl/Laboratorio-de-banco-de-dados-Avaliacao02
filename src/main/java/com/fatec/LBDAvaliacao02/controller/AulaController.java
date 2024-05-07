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
	public Aula listarAlunoAula(int idConteudo) throws SQLException, ClassNotFoundException 
	{
		GenericDao gDao = new GenericDao();
		AulaDao aDao = new AulaDao(gDao);
		Aula aula = aDao.listarAlunoAula(idConteudo);
		
		return aula;
	}

	public void selecionarChamada(List<Entry<String, List<Integer>>> set, int codigoDisciplinaConteudo) throws SQLException, ClassNotFoundException 
	{
		GenericDao gDao = new GenericDao();
		AulaDao aDao = new AulaDao(gDao);
		aDao.selecionarChamada(set, codigoDisciplinaConteudo);
	}
}
