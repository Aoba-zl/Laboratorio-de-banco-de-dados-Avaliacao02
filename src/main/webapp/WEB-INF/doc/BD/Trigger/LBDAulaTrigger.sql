USE matricula

CREATE TRIGGER t_aula ON matricula_disciplina
AFTER UPDATE
AS
BEGIN
	
	IF ((SELECT status FROM INSERTED) = 'Dispensado.')
	BEGIN
		RETURN
	END
	
	DECLARE @id_matricula 		INT,
			@id_conteudo 		INT,
			@ano				INT,
			@dia				INT,
			@semestre			INT,
			@data_semestre		DATE,
			@hora_aula			INT,
			@cont				INT
	
	SELECT @dia = d.dia_aula, @hora_aula = d.qntd_hora_semanais FROM INSERTED i, disciplina d 
	WHERE i.codigo_disciplina  = d.codigo
	
	SET @ano = YEAR(GETDATE())
	
	SELECT @semestre = m.semestre FROM INSERTED i, matricula m WHERE i.id_matricula = m.id
	
	IF (@semestre = 1)
	BEGIN 
		SET @data_semestre = CAST(@ano AS VARCHAR(4)) + '-02-15'
	END
	ELSE IF (@semestre = 2)
	BEGIN 
		SET @data_semestre = CAST(@ano AS VARCHAR(4)) + '-08-08' 
	END
	
	SELECT @data_semestre = 
	CASE @dia
	WHEN 1
	THEN DATEADD(DAY, 4, @data_semestre)
	WHEN 2
	THEN DATEADD(DAY, 5, @data_semestre)
	WHEN 3
	THEN DATEADD(DAY, 6, @data_semestre)
	WHEN 4
	THEN DATEADD(DAY, 0, @data_semestre)
	WHEN 5
	THEN DATEADD(DAY, 1, @data_semestre)
	END
	
	DECLARE c CURSOR FOR
			SELECT i.id_matricula, c.id FROM INSERTED i, disciplina d, conteudo c
		WHERE i.codigo_disciplina = d.codigo
		AND d.codigo = c.codigo_disciplina
	OPEN c
	FETCH NEXT FROM c
			INTO @id_matricula, @id_conteudo
	WHILE @@FETCH_STATUS = 0
	BEGIN
		INSERT INTO aula VALUES
		(@id_matricula, @id_conteudo, @data_semestre)
		
		SET @cont = 1
		
		SET @data_semestre = DATEADD(DAY, 7, @data_semestre)
		
		WHILE (@cont < @hora_aula + 1)
		BEGIN 
			INSERT INTO presenca 
			VALUES
			(@cont, @id_matricula, @id_conteudo, 0)
			
			SET @cont = @cont + 1
		END
		
		FETCH NEXT FROM c
			INTO @id_matricula, @id_conteudo
	END
	CLOSE c
	DEALLOCATE c
END
