USE matricula

CREATE FUNCTION fn_disciplina_conteudo (@codigo INT)
RETURNS @table TABLE
(
	id						INT,
	codigo_disciplina		INT,
	nome					VARCHAR(50),
	descricao				VARCHAR(255)
)
BEGIN
	INSERT INTO @table
		SELECT id, codigo_disciplina, nome, descricao FROM conteudo WHERE codigo_disciplina = @codigo
		
	RETURN
END

SELECT * FROM matricula_disciplina