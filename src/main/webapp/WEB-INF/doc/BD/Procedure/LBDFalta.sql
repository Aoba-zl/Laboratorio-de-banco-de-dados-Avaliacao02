USE matricula

CREATE PROCEDURE sp_falta (@ra CHAR(9)) AS
	DECLARE @falta TABLE (
		falta	INT
	)
	DECLARE @matricula INT,
			@disciplina INT,
			@status		VARCHAR(50),
			@faltas		INT
	DECLARE c CURSOR FOR
			SELECT m.id,md.codigo_disciplina, md.status
			FROM matricula m, aluno a, conteudo c, matricula_disciplina md, disciplina d
			WHERE
			md.codigo_disciplina = d.codigo
			AND c.codigo_disciplina = d.codigo
			AND m.id = md.id_matricula
			AND m.ra_aluno = a.ra
			AND a.ra = @ra
			GROUP BY m.id,md.codigo_disciplina, md.status
	OPEN c
	FETCH NEXT FROM c INTO @matricula, @disciplina, @status
	WHILE @@FETCH_STATUS = 0 BEGIN
		IF(@status = 'Dispensado.') BEGIN
			INSERT INTO @falta VALUES (0)
		END
		IF (@status = 'Aprovado.') BEGIN
		SELECT @faltas = faltas
			FROM (
				SELECT COUNT(p.id_matricula) as faltas
				FROM matricula m, aula au, presenca p, conteudo c, disciplina d
				WHERE au.id_matricula = m.id 
				AND au.id_conteudo = c.id 
				AND c.codigo_disciplina = d.codigo
				AND p.status = 0
				AND m.id = @matricula
				GROUP BY p.id_matricula
			) AS faltas
			IF (@faltas IS NULL) BEGIN
				SET @faltas = 0
			END
			INSERT INTO @falta VALUES (@faltas)
		END
		FETCH NEXT FROM c INTO @matricula, @disciplina, @status
	END
	SELECT * FROM @falta
	CLOSE c
	DEALLOCATE c