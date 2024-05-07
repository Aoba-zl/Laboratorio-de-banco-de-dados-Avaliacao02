USE matricula

CREATE PROCEDURE sp_dispensar (@ra CHAR(9), @codigo_disciplina INT)
AS
	UPDATE matricula_disciplina
	SET status = 'Dispensado.'
	FROM matricula_disciplina md, matricula m, aluno a
	WHERE md.id_matricula = m.id
	AND m.ra_aluno = a.ra
	AND a.ra = @ra
	AND md.codigo_disciplina = @codigo_disciplina