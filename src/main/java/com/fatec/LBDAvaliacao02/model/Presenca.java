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
public class Presenca 
{
	private int id;
	private Matricula matricula;
	private Conteudo conteudo;
	private boolean status;
}
