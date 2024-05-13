package com.fatec.LBDAvaliacao02.model;

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
public class Conteudo 
{
	private int id;
	private String nome;
	private String descricao;
	private Disciplina disciplina;
	List<Aula> aula;
}
