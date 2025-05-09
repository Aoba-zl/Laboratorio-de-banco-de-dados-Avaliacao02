package com.fatec.LBDAvaliacao02.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Curso;
import com.fatec.LBDAvaliacao02.model.Telefone;
import com.fatec.LBDAvaliacao02.model.Vestibular;
import com.fatec.LBDAvaliacao02.persistence.AlunoDao;
import com.fatec.LBDAvaliacao02.persistence.GenericDao;

@Controller
public class AlunoController 
{
    /**
     * Método para cadastrar um novo aluno.
     *
     * @param aluno O objeto Aluno a ser cadastrado.
     * @param curso O curso ao qual o aluno está associado.
     * @return Uma mensagem indicando se o cadastro foi bem-sucedido ou não.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public String cadastrar (Aluno aluno,Curso curso) throws SQLException, ClassNotFoundException {
		String saida = checkAluno(aluno,curso);
		if (saida.contains("correto")) {
			GenericDao gDao = new GenericDao();
			AlunoDao aDao = new AlunoDao(gDao);
			saida = aDao.iud("I", aluno,curso);
		}
		return saida;
	}
    /**
     * Método para alterar informações de um aluno existente.
     *
     * @param aluno O objeto Aluno com as informações atualizadas.
     * @param curso O curso ao qual o aluno está associado.
     * @return Uma mensagem indicando se a alteração foi bem-sucedida ou não.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public String alterar(Aluno aluno,Curso curso) throws SQLException, ClassNotFoundException {
		String saida = checkAluno(aluno,curso);
		if (saida.contains("correto")) {
			GenericDao gDao = new GenericDao();
			AlunoDao aDao = new AlunoDao(gDao);
			saida = aDao.iud("U", aluno,curso);
		}
		return saida;
	}

    /**
     * Método para excluir um aluno.
     *
     * @param aluno O objeto Aluno a ser excluído.
     * @return Uma mensagem indicando se a exclusão foi bem-sucedida ou não.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public String excluir(Aluno aluno) throws SQLException, ClassNotFoundException {
		String saida;
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		Curso curso = new Curso();
		Vestibular v = new Vestibular();
		v.setPontuacao(0);
		v.setPosicao(0);
		aluno.setVestibular(v);
		saida = aDao.iud("D", aluno,curso);
		return saida;
	}

    /**
     * Método para buscar um aluno pelo CPF.
     *
     * @param aluno O objeto Aluno com o CPF a ser buscado.
     * @return O objeto Aluno encontrado ou null se não for encontrado.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public Aluno buscar(Aluno aluno) throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		aluno = aDao.consultar(aluno);
		return aluno;
	}
    /**
     * Método para listar todos os alunos cadastrados.
     *
     * @param alunos Uma lista de alunos para serem preenchidos com os dados.
     * @return Uma lista contendo todos os alunos cadastrados.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public List<Aluno> listar(List<Aluno> alunos) throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		AlunoDao aDao = new AlunoDao(gDao);
		return alunos = aDao.listar();
	}

	private String checkTel (Aluno aluno) {
		String todo="";
		if (aluno.getTelefone() == null) {return "Ao menos 1 telefone deve ser preenchido";}
		for (Telefone t : aluno.getTelefone()) {
			if (t.getNumero().length() < 8 && t.getNumero() != "" ) {return "Telefone invalido";}
			if (t.getNumero().length() > 9) {return "Telefone invalido";}
			todo = todo + t.getNumero();
		}
		if (todo.length() <1) {return "Ao menos 1 telefone deve ser preenchido";}
		return "correto";
	}
	private String checkAluno(Aluno aluno) {
		if (aluno.getCpf().length() != 11) 	{return "CPF invalido!";}
		if (aluno.getNome().length() > 100 || aluno.getNome() == "") {return "Nome invalido!";}
		if (aluno.getNomeSocial().length() > 100) {return "Nome Social invalido!";}
		if (aluno.getEmailPessoal().length() > 100 || aluno.getEmailPessoal() == "" || !aluno.getEmailPessoal().contains("@") ||!aluno.getEmailPessoal().contains(".com")) {return "Email Pessoal invalido!";}
		if (aluno.getEmailCorporativo() != null) {
			if (aluno.getEmailCorporativo().length() > 100) {return "Email Corporativo invalido!";}
		}
		if (aluno.getInstituicaoConclusaoSegGrau().length() > 100 || aluno.getInstituicaoConclusaoSegGrau() == "") {return "Nome da Instituição invalido!";}
		if (aluno.getDtNascimento() == null) {return "Data de nascimento invalido!";}
		if (aluno.getDtNascimento().isAfter(LocalDate.now())) {return "Data de nascimento invalido!";}
		if (Period.between(aluno.getDtNascimento(), LocalDate.now()).getYears() < 15) {return "Tem que ser maior de 15 anos!";}
		if (aluno.getDtConclusaoSegGrau() == null) {return "Data de conclusão invalido!";}
		if (aluno.getDtConclusaoSegGrau().isAfter(LocalDate.now())) {return "Data de conclusão invalido!";}
		if (aluno.getVestibular().getPontuacao() < 0) {return "Pontuação invalida!";}	
		if (aluno.getVestibular().getPontuacao() >= 1000) {return "Pontuação invalida!";}	
		if (aluno.getVestibular().getPosicao() < 1) {return "Posição invalida!";}
		if (aluno.getVestibular().getPosicao() > 99999999) {return "Posição invalida!";}
		String saida = checkTel(aluno);
		if (saida != "correto") {return saida;}
		return "correto";
	}
	private String checkAluno(Aluno aluno, Curso curso) {
		String saida;
		saida = checkAluno(aluno);
		if (saida.contains("correto") && curso.getCodigo() == -1) {
			saida = "Curso não selecionado";
		}
		return saida;
	}

}
