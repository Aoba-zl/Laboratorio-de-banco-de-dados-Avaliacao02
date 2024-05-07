USE matricula

CREATE FUNCTION fn_aula_presenca(@id INT)
RETURNS @table TABLE
(
	id				INT,
	id_matricula	INT,
	id_conteudo		INT,
	ra				CHAR(9),
	nome			VARCHAR(100),
	status			BIT
)
BEGIN
	INSERT INTO @table
		SELECT COUNT(p.id), p.id_matricula, p.id_conteudo, m.ra_aluno, a2.nome, p.status FROM presenca p, aula a, matricula m, aluno a2
		WHERE p.id_matricula = a.id_matricula 
			AND p.id_conteudo = a.id_conteudo 
			AND a.id_matricula = m.id 
			AND m.ra_aluno = a2.ra
			AND p.id_conteudo = @id
		GROUP BY p.id_matricula, p.id_conteudo, m.ra_aluno, a2.nome, p.status
	RETURN
END