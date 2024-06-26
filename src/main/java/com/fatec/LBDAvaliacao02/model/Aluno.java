package com.fatec.LBDAvaliacao02.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class Aluno 
{
	private String ra;
	private String cpf;
	private String Nome;
	private String nomeSocial;
	private LocalDate dtNascimento;
	private String emailPessoal;
	private String emailCorporativo;
	private LocalDate dtConclusaoSegGrau;
	private String instituicaoConclusaoSegGrau;
	private Matricula matricula = new Matricula();
	private List<Telefone> telefone = new ArrayList<>();
	private Vestibular vestibular = new Vestibular();
	
	/**
	 * Obtém a data de nascimento formatada como uma string.
	 *
	 * @return A data de nascimento formatada no formato "dd/MM/yyyy".
	 */
	public String getDtNascimentoFormat()
    {
        DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dtNascimento.format(formatacao);
    }
	/**
	 * Obtém a data de conclusão do segundo grau formatada como uma string.
	 *
	 * @return A data de conclusão do segundo grau formatada no formato "dd/MM/yyyy".
	 */
    public String getDtConclusaoSegGrauFormat()
    {
        DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dtConclusaoSegGrau.format(formatacao);
    }
}
