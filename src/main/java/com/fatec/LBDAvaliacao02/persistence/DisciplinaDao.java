package com.fatec.LBDAvaliacao02.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Conteudo;
import com.fatec.LBDAvaliacao02.model.Disciplina;
import com.fatec.LBDAvaliacao02.model.MatriculaDisciplina;

/**
 * Essa Classe é responsável por mexer diretamente no banco de dados da entidade Disciplina.
 */
@Repository
public class DisciplinaDao implements ICrudDao<Disciplina>
{
	private GenericDao gDao;
	
	/**
	 * Método responsável por obter a conexão do banco de dados.
	 * 
	 * @param gDao Um objeto do tipo GenericDao com o tipo de conexão
	 */
	public DisciplinaDao(GenericDao gDao)
	{
		this.gDao = gDao;
	}
	
	/**
	 * Método responsável por retornar uma lista de disciplina, buscado por códigos SQL no banco de dados, através do RA do aluno.
	 * 
	 * @param ra Uma String contendo o RA do aluno
	 * @return Uma lista de objetos de disciplina
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public List<Disciplina> consultarAlunoDisciplina(String ra) throws SQLException, ClassNotFoundException
	{
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		Connection c = gDao.getConnection();
		String sql = "SELECT d.codigo, d.nome AS nome_disciplina, d.qntd_hora_semanais, d.dia_aula, d.horario_inicio, d.horario_fim, md.status, md.id_matricula "
				   + "FROM disciplina d, matricula_disciplina md, matricula m, aluno a "
				   + "WHERE d.codigo = md.codigo_disciplina "
				   + "	AND m.id = md.id_matricula"
				   + "  AND m.ra_aluno = a.ra "
				   + "  AND a.ra = ? "
				   + "ORDER BY md.status ASC";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, ra);
		ResultSet rs = ps.executeQuery();
		
		
		while(rs.next())
		{
			Disciplina d = new Disciplina();
			MatriculaDisciplina md = new MatriculaDisciplina();
			d.setCodigo(rs.getInt("codigo"));
			d.setNome(rs.getString("nome_disciplina"));
			d.setQntdHoraSemanais(rs.getInt("qntd_hora_semanais"));
			String diaSemana = "";
			switch (rs.getInt("dia_aula"))
			{
				case 1:
					diaSemana = "Segunda-feira";
					break;
				case 2:
					diaSemana = "Terça-feira";
					break;
				case 3:
					diaSemana = "Quarta-feira";
					break;
				case 4:
					diaSemana = "Quinta-feira";
					break;
				case 5:
					diaSemana = "Sexta-feira";
					break;
			}
			d.setDiaAula(diaSemana);
			d.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
			d.setHorarioFim(rs.getTime("horario_fim").toLocalTime());
			md.setStatus(rs.getString("status"));
			md.setIdMatricula(rs.getString("id_matricula"));
			d.setUmMatriculaDisciplina(md);
			
			disciplinas.add(d);
		}
		
		rs.close();
		ps.close();
		c.close();
		
		return disciplinas;
	}
	
	/**
	 * Método responsável por consultar um objeto do tipo Disciplina.
	 * 
	 * @param d contendo o objeto de Disciplina
	 * @return Um objeto do tipo Disciplina
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	@Override
	public Disciplina consultar(Disciplina d) throws SQLException, ClassNotFoundException //não feito por ser apenas crud do aluno
	{
		Connection c = gDao.getConnection();
		String sql = "SELECT d.nome AS nome_disciplina, d.qntd_hora_semanais "
				   + "FROM disciplina d, curso c "
				   + "WHERE d.codigo_curso = c.codigo "
				   + "	AND codigo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, d.getCodigo());
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
		{
			d.setCodigo(rs.getInt("codigo"));
			d.setCodigoCurso(rs.getInt("codigo_curso"));
			d.setNome(rs.getString("nome_disciplina"));
			d.setQntdHoraSemanais(rs.getInt("qntd_hora_semanais"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		return d;
	}
	
	/**
	 * Método responsável por listar objetos do tipo Disciplina.
	 * 
	 * @return Uma lista de objeto do tipo Disciplina
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	@Override
	public List<Disciplina> listar() throws SQLException, ClassNotFoundException //Não utilizado por não ser necessário, há coisas a ser atualizada
	{
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		Connection c = gDao.getConnection();
		String sql = "SELECT d.codigo, d.nome AS nome_disciplina, d.qntd_hora_semanais, d.dia_aula, d.horario_inicio, d.horario_fim "
				   + "FROM disciplina d, curso c "
				   + "WHERE d.codigo_curso = c.codigo";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Disciplina d = new Disciplina();
			d.setCodigo(rs.getInt("codigo"));
			d.setNome(rs.getString("nome_disciplina"));
			d.setQntdHoraSemanais(rs.getInt("qntd_hora_semanais"));
			String diaSemana = "";
			switch (rs.getInt("dia_aula"))
			{
				case 1:
					diaSemana = "Segunda-feira";
					break;
				case 2:
					diaSemana = "Terça-feira";
					break;
				case 3:
					diaSemana = "Quarta-feira";
					break;
				case 4:
					diaSemana = "Quinta-feira";
					break;
				case 5:
					diaSemana = "Sexta-feira";
					break;
			}
			d.setDiaAula(diaSemana);
			d.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
			d.setHorarioFim(rs.getTime("horario_fim").toLocalTime());
			
			disciplinas.add(d);
		}
		
		rs.close();
		ps.close();
		c.close();
		
		return disciplinas;
	}
	
	/**
	 * Método responsável por listar objetos do tipo Disciplina que contém o código do curso.
	 * 
	 * @param codigo contendo o codigo do curso
	 * @return Um objeto do tipo Disciplina
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public List<Disciplina> listarDisciplinaCurso(int codigo) throws SQLException, ClassNotFoundException
	{
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		Connection c = gDao.getConnection();
		String sql = """
				SELECT d.codigo, d.nome AS nome_disciplina 
				FROM disciplina d, curso c 
				WHERE d.codigo_curso = c.codigo 
					AND c.codigo = ?
				""";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, codigo);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Disciplina d = new Disciplina();
			d.setCodigo(rs.getInt("codigo"));
			d.setNome(rs.getString("nome_disciplina"));
			
			disciplinas.add(d);
		}
		
		rs.close();
		ps.close();
		c.close();
		
		return disciplinas;
	}

	/**
	 * Método responsável por consultar um objeto do tipo Disciplina que contém uma lista de conteudo.
	 * 
	 * @param codigoDisciplina contendo o codigo da Disciplina
	 * @return Um objeto do tipo Disciplina
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public Disciplina listarDisciplinaConteudo(int codigoDisciplina) throws SQLException, ClassNotFoundException
	{
		Disciplina disciplina = new Disciplina();
		List<Conteudo> conteudos = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, codigo_disciplina, nome, descricao FROM fn_disciplina_conteudo(?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, codigoDisciplina);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Conteudo conteudo = new Conteudo();
			conteudo.setId(rs.getInt("id"));
			conteudo.setCodigoDisciplina(rs.getInt("codigo_disciplina"));
			conteudo.setNome(rs.getString("nome"));
			conteudo.setDescricao(rs.getString("descricao"));
			
			conteudos.add(conteudo);
		}
		
		disciplina.setConteudo(conteudos);
		
		rs.close();
		ps.close();
		c.close();
		
		return disciplina;
	}
	
	/**
	 * Método responsável por atualizar as disciplinas escolhidas pelo usuários, colocando-os com "Em andamento." nos status da tabela do banco de dados.
	 * 
	 * @param mdList Lista de objetos do tipo disciplina
	 * @return Uma String de saída para o tipo de resposta que o banco de dados retornou
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public String escolheDisciplina(List<MatriculaDisciplina> mdList) throws SQLException, ClassNotFoundException 
	{
		Connection c = gDao.getConnection();
		String sql = "";
		String saida = "";
		sql = "SELECT id_matricula FROM matricula_disciplina WHERE id_matricula = ? AND status = 'Em andamento.'";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, mdList.get(0).getIdMatricula());
		ResultSet rs = ps.executeQuery();
		
		if(!rs.next())
		{
			sql = "{CALL sp_matricula_disciplina (?, ?, ?, ?)}";
			CallableStatement cs = c.prepareCall(sql);
			for(MatriculaDisciplina md : mdList)
			{
				cs.setString(1, md.getIdMatricula());
				cs.setString(2, md.getCodigoDisciplina());
				cs.setString(3, md.getStatus());
				cs.registerOutParameter(4, Types.VARCHAR);
				cs.execute();
			}
			saida = cs.getString(4);			
			cs.close();
		}
		else
		{
			saida = "Matricula já possui disciplina em andamento!";
		}
		
		ps.close();
		rs.close();
		c.close();
		
		return saida;
		
	}
	
	/**
	 * Dispensa uma disciplina para um aluno.
	 *
	 * @param ra         O RA do aluno.
	 * @param disciplina O código da disciplina a ser dispensada.
	 * @return Uma mensagem indicando se a dispensa da disciplina foi bem-sucedida ou não.
	 * @throws ClassNotFoundException Se a classe não for encontrada.
	 * @throws SQLException            Se ocorrer um erro durante a operação SQL.
	 */
	public String dispensarDisciplina(String ra, int disciplina) throws ClassNotFoundException, SQLException {
		Connection connection = gDao.getConnection();
		String querySql;
		querySql = "EXEC sp_dispensar ?,?";
		CallableStatement cs = connection.prepareCall(querySql);
		cs.setString(1, ra);
		cs.setInt(2, disciplina);
		cs.execute();
		cs.close();
		return "Disciplina dispensada";
		
	}
}
