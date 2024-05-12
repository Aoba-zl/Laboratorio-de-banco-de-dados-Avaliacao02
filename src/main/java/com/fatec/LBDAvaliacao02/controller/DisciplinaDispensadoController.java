package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;

import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Disciplina;
import com.fatec.LBDAvaliacao02.persistence.DisciplinaDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

@Controller
public class DisciplinaDispensadoController {
    /**
     * Método para dispensar uma disciplina para um aluno.
     *
     * @param ra         O RA do aluno.
     * @param disciplina O código da disciplina a ser dispensada.
     * @return Uma mensagem indicando se a dispensa da disciplina foi bem-sucedida ou não.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     */
	public String dispensar (String ra, int disciplina) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		DisciplinaDao dDao = new DisciplinaDao(gDao);
		return dDao.dispensarDisciplina(ra, disciplina);
	}
}
