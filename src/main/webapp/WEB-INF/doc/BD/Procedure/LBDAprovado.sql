USE matricula

CREATE PROCEDURE sp_aprovado (@ra CHAR(9)) AS
	SELECT d.codigo, d.nome, p.nome AS professor
	FROM disciplina d, matricula_disciplina md, matricula m, curso c,professor p
	WHERE d.codigo = md.codigo_disciplina
	AND m.id = md.id_matricula
	AND m.ra_aluno = @ra
	AND c.codigo = m.codigo_curso
	AND p.id = d.id_professor
	AND (md.status = 'Aprovado.'
	OR md.status = 'Dispensado.')