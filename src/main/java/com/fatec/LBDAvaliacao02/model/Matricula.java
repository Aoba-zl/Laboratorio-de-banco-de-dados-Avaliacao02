package com.fatec.LBDAvaliacao02.model;

import java.time.LocalDate;
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
public class Matricula 
{
	private int id;
	private Aluno aluno;
	private Curso curso;
	private String semestre;
	private String semestreIngresso;
	private LocalDate anoLimiteGraduacao;
	private int anoIngresso;
	private List<MatriculaDisciplina> md;
	private String status;
	private List<Aula> aula;
}
