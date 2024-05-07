USE matricula

CREATE PROCEDURE sp_cabecalho (@ra CHAR(9)) AS
	SELECT a.ra, a.nome,c.nome as curso ,m.ano_ingresso, v.pontuacao,v.posicao
	FROM disciplina d, matricula_disciplina md, matricula m, aluno a, vestibular v, curso c
	WHERE d.codigo = md.codigo_disciplina
	AND v.ra_aluno = a.ra
	AND m.id = md.id_matricula
	AND m.ra_aluno = a.ra
	AND c.codigo = m.codigo_curso
	AND a.ra = @ra
	GROUP BY a.nome, a.ra, c.nome, m.ano_ingresso,v.pontuacao,v.posicao