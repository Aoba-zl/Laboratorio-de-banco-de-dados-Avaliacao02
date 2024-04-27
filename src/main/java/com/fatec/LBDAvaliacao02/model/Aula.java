package com.fatec.LBDAvaliacao02.model;

import java.time.LocalDate;
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
public class Aula 
{
	private int id;
	private Matricula matricula;
	private Disciplina disciplina;
	private LocalDate dia;
	private List<Presenca> presencas;
}
