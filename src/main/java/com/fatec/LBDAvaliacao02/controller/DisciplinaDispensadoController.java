package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;

import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Disciplina;
import com.fatec.LBDAvaliacao02.persistence.DisciplinaDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

@Controller
public class DisciplinaDispensadoController {
	public String dispensar (String ra, int disciplina) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		DisciplinaDao dDao = new DisciplinaDao(gDao);
		return dDao.dispensarDisciplina(ra, disciplina);
	}
}
