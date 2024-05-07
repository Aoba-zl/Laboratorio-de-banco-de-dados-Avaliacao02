USE matricula

CREATE PROCEDURE sp_id_matricula_disciplina(@op CHAR(1), @ra CHAR(9))
AS
	DECLARE @cont INT
	IF (@op = 'I')
	BEGIN
		INSERT INTO matricula_disciplina 
			SELECT m.id, d.codigo, 'Não cursando.' AS status
			FROM aluno a, matricula m, curso c, disciplina d 
			WHERE a.ra = m.ra_aluno
				AND m.codigo_curso = c.codigo 
				AND c.codigo = d.codigo_curso 
				AND a.ra = @ra
		EXEC sp_gera_nota @ra
	END

	ELSE IF (@op = 'D')
	BEGIN
		DELETE nota
		WHERE id_matricula = (
		SELECT TOP 1 id 		
		FROM matricula_disciplina md, matricula m, aluno a
		WHERE md.id_matricula = m.id
			AND m.ra_aluno = a.ra
			AND a.ra = @ra
		)
		DELETE md
		FROM matricula_disciplina md, matricula m, aluno a
		WHERE md.id_matricula = m.id
			AND m.ra_aluno = a.ra
			AND a.ra = @ra
	END

CREATE PROCEDURE sp_gera_nota (@ra AS CHAR(9)) 
AS
	DECLARE @matriculaid		INT,
			@disciplinacodigo	INT,
			@cont				INT
	DECLARE c CURSOR FOR
		SELECT md.codigo_disciplina, md.id_matricula
		FROM matricula_disciplina md, matricula m, aluno a
		WHERE md.id_matricula = m.id
			AND m.ra_aluno = a.ra
			AND a.ra = @ra
	OPEN c
	FETCH NEXT FROM c INTO @disciplinacodigo, @matriculaid
	WHILE @@FETCH_STATUS = 0 BEGIN
		SET @cont = 0
		WHILE (@cont != 3) BEGIN
			INSERT INTO nota VALUES(@cont+1,@matriculaid, @disciplinacodigo, CAST(RAND()*11 AS INT))
			SET @cont = @cont + 1
		END
		FETCH NEXT FROM c INTO @disciplinacodigo, @matriculaid
	END
	CLOSE c
	DEALLOCATE c