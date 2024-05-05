package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;
import java.util.List;
import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.persistence.AlunoDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

public class HistoricoController {
	public Aluno buscarCabeçalho(Aluno aluno) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		return aDao.buscarCabeçalho(aluno);
		
	}
	public List[] buscarHistorico(Aluno aluno, List[] aprovado) throws ClassNotFoundException, SQLException{
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		return aDao.buscarHistorico(aluno,aprovado);
		
	}
}
