package com.fatec.LBDAvaliacao02.model;

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
public class MatriculaDisciplina 
{
	private Matricula matricula;
	private Disciplina disciplina;
	private String status;
}
