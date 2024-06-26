package com.fatec.LBDAvaliacao02.model;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Disciplina 
{
	private int codigo;
	private String nome;
	private int qntdHoraSemanais;
	private String diaAula;
	private LocalTime horarioInicio;
	private LocalTime horarioFim;
	private List<Conteudo> conteudo = new ArrayList<>();
	private List<MatriculaDisciplina> matriculaDisciplina;
	private Curso curso;
	private Professor professor;
	
	/**
	 * Método responsável por fazer um Set de apenas uma matricula da lista de matricula.
	 * 
	 * @param md contendo um objeto do tipo MatriculaDisciplina
	 */
	public void setUmMatriculaDisciplina (MatriculaDisciplina md)
	{
		this.matriculaDisciplina = new ArrayList<>();
		this.matriculaDisciplina.add(md);
	}
	
	/**
	 * Método responsável por fazer um Get de apenas uma matricula da lista de matricula.
	 * 
	 * @return Um objeto do tipo MatriculaDisciplina
	 */

	public MatriculaDisciplina getUmMatriculaDisciplina()
	{
		MatriculaDisciplina md = new MatriculaDisciplina();
		md = this.matriculaDisciplina.get(0);
		
		return md;
	}
}
