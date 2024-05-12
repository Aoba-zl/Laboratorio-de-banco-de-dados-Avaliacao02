package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;
import java.util.List;
import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.persistence.AlunoDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

public class HistoricoController {
	
    /**
     * Método para listar as informações de uma aula para um aluno.
     *
     * @param idConteudo O ID do conteúdo da aula.
     * @return Um objeto Aula contendo as informações da aula para o aluno.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public Aluno buscarCabeçalho(Aluno aluno) throws ClassNotFoundException, SQLException {
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		return aDao.buscarCabeçalho(aluno);
		
	}
	
    /**
     * Método para selecionar a chamada para uma determinada disciplina e conteúdo.
     *
     * @param set                    Um conjunto contendo as entradas das listas de presença dos alunos.
     * @param codigoDisciplinaConteudo O código da disciplina e conteúdo relacionado à chamada.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public List[] buscarHistorico(Aluno aluno, List[] aprovado) throws ClassNotFoundException, SQLException{
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		return aDao.buscarHistorico(aluno,aprovado);
		
	}
}
