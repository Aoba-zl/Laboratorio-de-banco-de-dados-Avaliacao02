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
	private Matricula matricula;
	private Conteudo conteudo;
	private LocalDate dia;
	private List<Presenca> presenca;
}
