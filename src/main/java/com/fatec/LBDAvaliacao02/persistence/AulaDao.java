package com.fatec.LBDAvaliacao02.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.fatec.LBDAvaliacao02.model.Aluno;
import com.fatec.LBDAvaliacao02.model.Aula;
import com.fatec.LBDAvaliacao02.model.Conteudo;
import com.fatec.LBDAvaliacao02.model.Matricula;
import com.fatec.LBDAvaliacao02.model.Presenca;

@Repository
public class AulaDao 
{
	private GenericDao gDao = new GenericDao();
	
	public AulaDao(GenericDao gDao)
	{
		this.gDao = gDao;
	}
	
	
	/**
	 * Método responsável por criar um objeto do tipo Aula contendo o Objeto Aluno para mostrar a chamada.
	 * 
	 * @param idConteudo contendo o id de um conteudo
	 * @return Um objeto do tipo Aula
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public Aula listarAlunoAula(int idConteudo) throws SQLException, ClassNotFoundException
	{
		List<Presenca> presencas = new ArrayList<Presenca>();
		Aula aula = new Aula();
		Connection c = gDao.getConnection();
		String sql = "SELECT id, id_matricula, id_conteudo, ra, nome, status FROM fn_aula_presenca(?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, idConteudo);
		ResultSet rs = ps.executeQuery();
		
		
		while(rs.next())
		{
			Presenca p = new Presenca();
			Matricula m = new Matricula();
			Aluno a = new Aluno();
			Conteudo co = new Conteudo();
			
			p.setId(rs.getInt("id"));
			
			m.setId(rs.getInt("id_matricula"));
			a.setRa(rs.getString("ra"));
			a.setNome(rs.getString("nome"));
			m.setAluno(a);
			p.setMatricula(m);
			
			co.setId(rs.getInt("id_conteudo"));
			p.setConteudo(co);
			
			p.setStatus(rs.getBoolean("status"));
			
			presencas.add(p);
		}
		
		aula.setPresenca(presencas);
		
		rs.close();
		ps.close();
		c.close();
		
		return aula;
	}
	
	/**
	 * Método responsável por atualizar no banco de dados a lista de chamada de Alunos que estiveram presente na Aula.
	 * 
	 * @param set contendo uma lista de Entry e List, onde o String do Entry contém o RA como key e a List contém o número de Aula
	 * @param codigoDisciplinaConteudo contendo o codigo do conteudo
	 * @throws SQLException Exceção lançada se houver problema com SQL
	 * @throws ClassNotFoundException Exceção lançada se houver erro ao tentar encontrar a classe
	 */
	public void selecionarChamada(List<Entry<String, List<Integer>>> set, int codigoDisciplinaConteudo) throws SQLException, ClassNotFoundException 
	{
		
		Connection c = gDao.getConnection();
		String sql = """
				UPDATE presenca 
				SET status = 1 
				FROM aula a, matricula m 
				WHERE presenca.id_matricula = a.id_matricula 
					AND presenca.id_conteudo = a.id_conteudo 
					AND a.id_matricula = m.id 
					AND	presenca.id_conteudo = ? 
					AND m.ra_aluno = ? 
					AND presenca.id = ? 
				""";
		PreparedStatement ps = c.prepareStatement(sql);
		
		for(Entry<String, List<Integer>> s : set)
		{
			ps.setInt(1, codigoDisciplinaConteudo);
			ps.setString(2, s.getKey());
			
			List<Integer> idList = s.getValue();
			
			for(int id : idList)
			{
				ps.setInt(3, id);
				ps.execute();
			}
		}
		
		ps.close();
		c.close();
	}
}
