USE matricula

CREATE PROCEDURE sp_media (@ra CHAR(9)) AS
	DECLARE @tabela TABLE (
	nota VARCHAR(20)
	)
	DECLARE
	@status VARCHAR(50),
	@disciplina INT,
	@matricula INT,
	@nota VARCHAR(10)
	DECLARE c CURSOR FOR
		SELECT md.status,md.codigo_disciplina,md.id_matricula, AVG(n.nota) AS nota
		FROM disciplina d, matricula_disciplina md, matricula m, aluno a,nota n
		WHERE d.codigo = md.codigo_disciplina
		AND n.id_matricula = md.id_matricula
		AND n.codigo_disciplina = md.codigo_disciplina
		AND m.id = md.id_matricula
		AND m.ra_aluno = a.ra
		AND a.ra = @ra
		GROUP BY md.status,md.codigo_disciplina,md.id_matricula
	OPEN c
	FETCH NEXT FROM c INTO @status, @disciplina, @matricula, @nota
	WHILE @@FETCH_STATUS = 0 BEGIN
		IF (@status = 'Dispensado.') BEGIN
			INSERT INTO @tabela VALUES ('D')
		END
		IF (@status = 'Aprovado.') BEGIN
			INSERT INTO @tabela VALUES (@nota)
		END
		FETCH NEXT FROM c INTO @status, @disciplina, @matricula, @nota
	END
	SELECT * FROM @tabela
	CLOSE c
	DEALLOCATE c