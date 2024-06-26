package com.fatec.LBDAvaliacao02.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Curso;
import com.fatec.LBDAvaliacao02.model.Disciplina;
import com.fatec.LBDAvaliacao02.model.Professor;
import com.fatec.LBDAvaliacao02.model.Telefone;
import com.fatec.LBDAvaliacao02.model.Vestibular;

@Repository
public class AlunoDao implements ICrudDao<Aluno>, ICrudIud<Aluno,Curso>
{
	private GenericDao gDao;
	
    /**
     * Construtor que recebe um objeto GenericDao como parâmetro.
     *
     * @param gDao O objeto GenericDao a ser utilizado para operações de acesso a dados.
     */
	public AlunoDao(GenericDao gDao)
	{	
		this.gDao = gDao;
	}
	
    /**
     * Consulta informações de um aluno.
     *
     * @param a O objeto Aluno a ser consultado.
     * @return O objeto Aluno com as informações consultadas.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	@Override
	public Aluno consultar(Aluno a) throws SQLException, ClassNotFoundException 
	{
		Connection connection = gDao.getConnection();
		String querySql = """ 
		SELECT 
		a.ra, 
		a.cpf, 
		a.nome, 
		a.nome_social, 
		a.dt_nascimento, 
		a.email_pessoal, 
		a.email_corporativo, 
		a.dt_conclusao_seg_grau, 
		a.instituicao_conclusao_seg_grau,
		t.numero,
		v.pontuacao,
		v.posicao 
		FROM aluno a, telefone t, vestibular v
		WHERE a.ra = t.ra_aluno AND a.ra = v.ra_aluno AND a.ra = ?
				""";
		PreparedStatement preparedStatement = connection.prepareStatement(querySql);
		preparedStatement.setString(1, a.getRa());
		ResultSet result = preparedStatement.executeQuery();

		if (result.next()) {
			a.setCpf(result.getString("cpf"));
			a.setNome(result.getString("nome"));
			a.setNomeSocial(result.getString("nome_social"));
			a.setDtNascimento(toLocalDate(result.getDate("dt_nascimento")));
			a.setEmailPessoal(result.getString("email_pessoal"));
			a.setEmailCorporativo(result.getString("email_corporativo"));
			a.setDtConclusaoSegGrau(toLocalDate(result.getDate("dt_conclusao_seg_grau")));
			a.setInstituicaoConclusaoSegGrau(result.getString("instituicao_conclusao_seg_grau"));
			Vestibular v = new Vestibular();
			v.setPontuacao(result.getFloat("pontuacao"));
			v.setPosicao(result.getInt("posicao"));
			a.setVestibular(v);
			List<Telefone> telefones = new ArrayList<>();
			Telefone t = new Telefone(result.getString("numero"));
			telefones.add(t);
			for(int J=0;J<3;J++) {
				Telefone telefone = new Telefone();
				if (result.next()) {
					telefone.setNumero(result.getString("numero"));
				} else {
					telefone.setNumero("");
				}
				telefones.add(telefone);
			}
			a.setTelefone(telefones);
		} else {
			
		}
		preparedStatement.close();
		connection.close();
		return a;
	}

    /**
     * Lista todos os alunos.
     *
     * @return Uma lista de objetos Aluno contendo as informações de todos os alunos.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	@Override
	public List<Aluno> listar() throws SQLException, ClassNotFoundException 
	{
		Connection connection = gDao.getConnection();
		String querySql = """ 
				SELECT 
				a.ra, 
				a.cpf, 
				a.nome, 
				a.nome_social, 
				a.dt_nascimento, 
				a.email_pessoal, 
				a.dt_conclusao_seg_grau, 
				a.instituicao_conclusao_seg_grau,
				v.pontuacao,
				v.posicao 
				FROM aluno a,vestibular v
				WHERE a.ra = v.ra_aluno
				""";
		PreparedStatement preparedStatement = connection.prepareStatement(querySql);
		ResultSet result = preparedStatement.executeQuery();
		List<Aluno> alunos = new ArrayList<>();
		while (result.next()) {
			Aluno a = new Aluno();
			a.setRa(result.getString("ra"));
			a.setCpf(result.getString("cpf"));
			a.setNome(result.getString("nome"));
			a.setDtNascimento(toLocalDate(result.getDate("dt_nascimento")));
			a.setEmailPessoal(result.getString("email_pessoal"));
			a.setInstituicaoConclusaoSegGrau(result.getString("instituicao_conclusao_seg_grau"));
			a.setDtConclusaoSegGrau(toLocalDate(result.getDate("dt_conclusao_seg_grau")));
			Vestibular v = new Vestibular();
			v.setPontuacao(result.getFloat("pontuacao"));
			v.setPosicao(result.getInt("posicao"));
			a.setVestibular(v);
			alunos.add(a);
		}
		return alunos;
	}

    /**
     * Realiza a inserção, atualização ou exclusão de um aluno.
     *
     * @param acao  A ação a ser realizada: "I" para inserção, "U" para atualização ou "D" para exclusão.
     * @param aluno O objeto Aluno a ser manipulado.
     * @param curso O objeto Curso relacionado ao aluno.
     * @return Uma mensagem indicando o resultado da operação.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	@Override
	public String iud(String acao, Aluno aluno, Curso curso) throws SQLException, ClassNotFoundException 
	{
		Connection connection = gDao.getConnection();
		String querySql = "EXEC sp_iud_aluno ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
		CallableStatement cs = connection.prepareCall(querySql);
		cs.setString(1, acao);
		cs.setString(2, aluno.getRa());
		cs.setString(3,aluno.getCpf());
		cs.setString(4,aluno.getNome());
		cs.setString(5,aluno.getNomeSocial());
		cs.setDate(6,toSQLDate(aluno.getDtNascimento()));
		cs.setString(7,aluno.getEmailPessoal());	
		cs.setString(8,aluno.getEmailCorporativo());
		cs.setDate(9,toSQLDate(aluno.getDtConclusaoSegGrau()));
		cs.setString(10,aluno.getInstituicaoConclusaoSegGrau());
		cs.setFloat(11, aluno.getVestibular().getPontuacao());
		cs.setInt(12, aluno.getVestibular().getPosicao());
		cs.setInt(13, curso.getCodigo());
		cs.registerOutParameter(14, Types.VARCHAR);
		cs.registerOutParameter(15, Types.VARCHAR);
        cs.execute();
        String saida = cs.getString(14);
        String ra = cs.getString(15);
        cs.close();
        
        if(saida.contains("Aluno cadastrado")) {
	        querySql = "EXEC sp_telefone_aluno ?,?,?,?,?";
	        cs = connection.prepareCall(querySql);
	        for (Telefone t : aluno.getTelefone()) {
	        	if (t.getNumero() != "") {
		        	cs = connection.prepareCall(querySql);
			        cs.setString(1, acao);
			        cs.setString(2, ra);
			        cs.setString(3, t.getNumero());
			        cs.setString(4, null);
			        cs.registerOutParameter(5, Types.VARCHAR);
			        cs.execute();
	        	}
	        }
	        cs.close();
	        connection.close();
        }
        if (saida.contains("Aluno atualizado")) {
        	updateTel(aluno);
        }
		return saida;
	}
	private void updateTel(Aluno aluno) throws ClassNotFoundException, SQLException {
		Connection connection = gDao.getConnection();
		String querySql = "SELECT * FROM telefone WHERE ra_aluno = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(querySql);
		preparedStatement.setString(1, aluno.getRa());
		ResultSet result = preparedStatement.executeQuery();
		List<Telefone> chave = new ArrayList<>();
		while(result.next()) {
			Telefone tel = new Telefone(result.getString("numero"));
			chave.add(tel);
		}
		preparedStatement.close();

		String saida = "";
        int max = aluno.getTelefone().size();
        if (chave.size() > max) {
            max = chave.size();
        }
        int cont = 0;
        while(cont != max) {
        	cont = 0;
            for (int J=0;J<max;J++) {
            	String numeroAtual = aluno.getTelefone().get(J).getNumero();
	            querySql = "EXEC sp_telefone_aluno ?,?,?,?,?";
	            CallableStatement cs = connection.prepareCall(querySql);
	            cs.setString(1, "U");
	            cs.setString(2, aluno.getRa());
	            if (J+1 > aluno.getTelefone().size() || numeroAtual == "" ) {
	                cs.setString(3, null);
	                numeroAtual = null;
	            }else {
	                cs.setString(3, numeroAtual);
	            }
	            if (J+1 > chave.size()) {
	                cs.setString(4, null);
	            }else {
	                cs.setString(4, chave.get(J).getNumero());
	            }
	            cs.registerOutParameter(5, Types.VARCHAR);
		        cs.execute();
		        saida = cs.getString(5);
                switch (saida) {
                case "cadastrado" : chave.add(aluno.getTelefone().get(J)) ;
                break;
                case "excluido" : chave.remove(J);
                				  aluno.getTelefone().remove(J);
                                  max--;
                break;
                case "erro" : cont++;
                break;
                }
            }
        }
	}
	
    /**
     * Busca informações do cabeçalho de um aluno.
     *
     * @param aluno O objeto Aluno a ser consultado.
     * @return O objeto Aluno com as informações do cabeçalho.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public Aluno buscarCabeçalho(Aluno aluno) throws ClassNotFoundException, SQLException {
		Connection connection = gDao.getConnection();
		String querySql = "EXEC sp_cabecalho ?";
        CallableStatement cs = connection.prepareCall(querySql);
        cs.setString(1, aluno.getRa());
        ResultSet result = cs.executeQuery();
        if(result.next()) {
        	Curso c = new Curso();
	        aluno.setNome(result.getString("nome"));
	        c.setNome(result.getString("curso"));
	        aluno.getMatricula().setCurso(c);
	        aluno.getMatricula().setAnoIngresso(result.getInt("ano_ingresso"));
	        aluno.getVestibular().setPontuacao(result.getFloat("pontuacao"));
	        aluno.getVestibular().setPosicao(result.getInt("posicao"));
        }
        cs.close();
		return aluno;
		
	}
	
    /**
     * Busca o histórico de um aluno.
     *
     * @param aluno    O objeto Aluno a ser consultado.
     * @param aprovado Um array de Listas contendo as informações do histórico.
     *                 Na posição 0: Lista de Disciplinas aprovadas.
     *                 Na posição 1: Lista de médias obtidas pelo aluno.
     *                 Na posição 2: Lista de faltas do aluno.
     * @return Um array de Listas contendo as informações do histórico.
     * @throws SQLException            Se ocorrer um erro durante a operação SQL.
     * @throws ClassNotFoundException Se a classe não for encontrada.
     */
	public List[] buscarHistorico(Aluno aluno, List[] aprovado) throws ClassNotFoundException, SQLException {
		List<Disciplina> disciplinas = new ArrayList<>();
		List<String> medias = new ArrayList<>();
		List<Integer> faltas = new ArrayList<>();
		Connection connection = gDao.getConnection();
		String querySql = "EXEC sp_aprovado ?";
        CallableStatement cs = connection.prepareCall(querySql);
        cs.setString(1, aluno.getRa());
        ResultSet result = cs.executeQuery();
        while(result.next()) {
        	Disciplina disciplina = new Disciplina();
        	disciplina.setCodigo(result.getInt("codigo"));
        	disciplina.setNome(result.getString("nome"));
        	Professor p = new Professor();
        	p.setNome(result.getString("professor"));
        	disciplina.setProfessor(p);
        	disciplinas.add(disciplina);
        }
        querySql = "EXEC sp_media ?";
        cs = connection.prepareCall(querySql);
        cs.setString(1, aluno.getRa());
        result = cs.executeQuery();
        while(result.next()) {
        	String media;
        	media = result.getString("nota");
        	medias.add(media);
        }
        querySql = "EXEC sp_falta ?";
        cs = connection.prepareCall(querySql);
        cs.setString(1, aluno.getRa());
        result = cs.executeQuery();
        while(result.next()) {
        	int falta;
        	falta = result.getInt("falta");
        	faltas.add(falta);
        }
        aprovado[0] = disciplinas;
        aprovado[1] = medias;
        aprovado[2] = faltas;
		return aprovado;
	}
	
	private java.sql.Date toSQLDate(LocalDate data){
		if (data != null) {
			java.sql.Date sqlDate = java.sql.Date.valueOf(data);
			return sqlDate;
		}
		return null;
	}
	private LocalDate toLocalDate (Date date) {
        return Optional.ofNullable(date).map(Date::toLocalDate).orElse(null);
	}
	private LocalDate toLocalDate(int date) {
		LocalDate localDate = LocalDate.of(date, date, date);
		return null;
	}
}
