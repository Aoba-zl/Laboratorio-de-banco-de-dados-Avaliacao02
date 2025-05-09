USE master

DROP DATABASE matricula


CREATE DATABASE matricula
GO
USE matricula
GO
CREATE TABLE aluno
(
	ra								CHAR(9) 				NOT NULL,
	cpf								CHAR(11) 				NOT NULL,
	nome							VARCHAR(100) 			NOT NULL,
	nome_social						VARCHAR(100)			NULL,
	dt_nascimento					DATE					NOT NULL,
	email_pessoal					VARCHAR(100) 			NOT NULL, 
	email_corporativo				VARCHAR(100)			NULL,
	dt_conclusao_seg_grau			DATE					NOT NULL,
	instituicao_conclusao_seg_grau	VARCHAR(100)			NOT NULL
	PRIMARY KEY (ra)
)
GO
CREATE UNIQUE NONCLUSTERED INDEX idx_email_corporativo_notnull
ON aluno(email_corporativo)
WHERE email_corporativo IS NOT NULL
GO
CREATE TABLE telefone
(
	ra_aluno						CHAR(9) 				NOT NULL,
	numero							CHAR(9)					NOT NULL
	PRIMARY KEY (ra_aluno, numero)
	FOREIGN KEY (ra_aluno) REFERENCES aluno (ra)
)
GO
CREATE TABLE vestibular
(
	ra_aluno						CHAR(9)					NOT NULL,
	pontuacao						DECIMAL(7, 2)			NOT NULL,
	posicao							INT						NOT NULL
	PRIMARY KEY (ra_aluno)
	FOREIGN KEY (ra_aluno) REFERENCES aluno (ra)
)
GO
CREATE TABLE curso
(
	codigo							INT	IDENTITY(1, 1)		NOT NULL,
	nome							VARCHAR(50)				NOT NULL,
	carga_horaria					INT						NOT NULL,
	sigla							VARCHAR(30)				NOT NULL,
	ult_nota_participacao_enade		DECIMAL(7, 1)			NOT NULL
	PRIMARY KEY (codigo)
)
GO
CREATE TABLE matricula
(
	id								INT	IDENTITY(1, 1)		NOT NULL,
	ra_aluno						CHAR(9)					NOT NULL,
	codigo_curso					INT						NOT NULL,
	semestre						INT						NOT NULL,
	semestre_ingresso				INT						NOT NULL,
	ano_limite_graduacao			INT						NOT NULL,
	ano_ingresso					INT						NOT NULL,
	status							VARCHAR(25)				NOT NULL
	PRIMARY KEY (id)
	FOREIGN KEY (ra_aluno) REFERENCES aluno (ra),
	FOREIGN KEY (codigo_curso) REFERENCES curso (codigo)
)
GO
CREATE TABLE professor
(
	id								INT	IDENTITY(1, 1)	NOT NULL,
	nome							VARCHAR(100)			NOT NULL
	PRIMARY KEY (id)
)
GO
CREATE TABLE disciplina
(
	codigo							INT	IDENTITY(1001, 1)	NOT NULL,
	codigo_curso					INT						NOT NULL,
	nome							VARCHAR(50)				NOT NULL,
	qntd_hora_semanais				INT						NOT NULL,
	dia_aula						INT						NOT NULL,
	horario_inicio					TIME					NOT NULL,
	horario_fim						TIME					NOT NULL,
	id_professor					INT						NOT NULL
	PRIMARY KEY (codigo)
	FOREIGN KEY (codigo_curso) REFERENCES curso (codigo),
	FOREIGN KEY (id_professor) REFERENCES professor (id)
)
GO
CREATE TABLE conteudo
(
	id								INT	IDENTITY(1, 1)		NOT NULL,
	codigo_disciplina				INT						NOT NULL,
	nome							VARCHAR(50)				NOT NULL,
	descricao						VARCHAR(255)			NOT NULL
	PRIMARY KEY (id)
	FOREIGN KEY (codigo_disciplina) REFERENCES disciplina (codigo)
)
GO
CREATE TABLE matricula_disciplina 
(
	id_matricula					INT						NOT NULL,
	codigo_disciplina				INT						NOT NULL,
	status							VARCHAR(20)				NOT NULL
	PRIMARY KEY (id_matricula, codigo_disciplina)
	FOREIGN KEY (id_matricula) REFERENCES matricula (id),
	FOREIGN KEY (codigo_disciplina) REFERENCES disciplina (codigo)
)
GO
CREATE TABLE aula
(
	id_matricula					INT 					NOT NULL,
	id_conteudo						INT						NOT NULL,
	dia								DATE					NOT NULL
	PRIMARY KEY (id_matricula, id_conteudo)
	FOREIGN KEY (id_matricula) REFERENCES matricula (id),
	FOREIGN KEY (id_conteudo) REFERENCES conteudo (id)
)
GO
CREATE TABLE presenca 
(
	id								INT						NOT NULL,
	id_matricula					INT						NOT NULL,
	id_conteudo						INT						NOT NULL,
	status							BIT						NOT NULL
	PRIMARY KEY (id, id_matricula, id_conteudo)
	FOREIGN KEY (id_matricula) REFERENCES matricula (id),
	FOREIGN KEY (id_conteudo) REFERENCES conteudo (id)
)
GO
CREATE TABLE nota
(
	id								INT						NOT NULL,
	id_matricula					INT						NOT NULL,
	codigo_disciplina				INT						NOT NULL,
	nota							DECIMAL(7, 2)			NOT NULL
	PRIMARY KEY (id, id_matricula, codigo_disciplina)
	FOREIGN KEY (id_matricula) REFERENCES matricula (id),
	FOREIGN KEY (codigo_disciplina) REFERENCES disciplina (codigo)
)

SELECT d.codigo, d.nome AS nome_disciplina, d.qntd_hora_semanais, d.dia_aula, d.horario_inicio, d.horario_fim, md.status
FROM disciplina d, matricula_disciplina md, matricula m, aluno a 
WHERE d.codigo = md.codigo_disciplina 
	AND m.id = md.id_matricula
  AND m.ra_aluno = a.ra 
  AND a.ra = '202416649'


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
GO

CREATE PROCEDURE sp_iud_aluno
(
	@op CHAR(1), 
	@ra CHAR(9), 
	@cpf CHAR(11), 
	@nome VARCHAR(100), 
	@nome_social VARCHAR(100), 
	@dt_nascimento DATE, 
	@email_pessoal VARCHAR(100), 
	@email_corporativo VARCHAR(100), 
	@dt_conclusao_seg_grau DATE, 
	@instituicao_conclusao_seg_grau VARCHAR(100),
	@pontuacao_vestibular DECIMAL(7, 2),
	@posicao_vestibular INT,
	@codigo_curso INT,
	@saida VARCHAR(100) OUTPUT,
	@ra_novo CHAR(9) OUTPUT
)
AS
	DECLARE @cpf_valido INT,
			@idade_valido BIT,
			@ra_existe	CHAR(9)
	SET @ra_existe = (SELECT ra FROM aluno WHERE ra = @ra)
	EXEC sp_verifica_cpf @cpf, @cpf_valido OUTPUT
	IF (@cpf_valido = 0)
	BEGIN
		DECLARE	@cpf_existe CHAR(11)
			SET @cpf_existe = (SELECT cpf FROM aluno WHERE cpf = @cpf)
		IF (UPPER(@op) = 'I')
		BEGIN
			IF (@cpf_existe IS NULL)
			BEGIN 
				EXEC sp_verifica_idade @dt_nascimento, @idade_valido OUTPUT
				IF (@idade_valido = 1)
				BEGIN
					EXEC sp_ra_aluno @ra_novo OUTPUT
					INSERT INTO aluno (ra, cpf, nome, nome_social, dt_nascimento, email_pessoal, email_corporativo, dt_conclusao_seg_grau, instituicao_conclusao_seg_grau)
					VALUES 
					(@ra_novo, @cpf, @nome, @nome_social, @dt_nascimento, @email_pessoal, @email_corporativo, @dt_conclusao_seg_grau, @instituicao_conclusao_seg_grau)
					INSERT INTO vestibular (ra_aluno, pontuacao, posicao) 
					VALUES
					(@ra_novo, @pontuacao_vestibular, @posicao_vestibular)
					EXEC sp_matricula_aluno @ra_novo, @codigo_curso 
					EXEC sp_id_matricula_disciplina @op, @ra_novo 
					SET @saida = 'Aluno cadastrado!'
				END
				ELSE
				BEGIN 
					SET @saida = 'Tem que ser maior de 15 anos!'
				END
			END
			ELSE IF (@cpf_existe IS NOT NULL)
			BEGIN 
				SET @saida = 'CPF já está cadastrado!'
			END
		END
		ELSE IF (UPPER(@op) = 'U')
		BEGIN
			IF (@ra_existe IS NOT NULL)
			BEGIN
				EXEC sp_verifica_idade @dt_nascimento, @idade_valido OUTPUT
				IF (@idade_valido = 1)
				BEGIN
					IF ((SELECT cpf FROM aluno WHERE ra = @ra AND cpf = @cpf) IS NOT NULL OR (SELECT cpf FROM aluno WHERE cpf = @cpf) IS NULL)
					BEGIN
						UPDATE aluno 
						SET 
						cpf = @cpf, 
						nome = @nome, 
						nome_social = @nome_social, 
						dt_nascimento = @dt_nascimento, 
						email_pessoal = @email_pessoal, 
						email_corporativo = @email_corporativo, 
						dt_conclusao_seg_grau = @dt_conclusao_seg_grau, 
						instituicao_conclusao_seg_grau = @instituicao_conclusao_seg_grau
						WHERE ra = @ra
						UPDATE vestibular 
						SET 
						pontuacao = @pontuacao_vestibular,
						posicao = @posicao_vestibular
						WHERE ra_aluno = @ra
						SET @saida = 'Aluno atualizado!'
						SET @ra_novo = @ra
					END
					ELSE
					BEGIN 
						SET @saida = 'CPF já existe no sistema!'
					END
				END
				ELSE
				BEGIN 
					SET @saida = 'Tem que ser maior de 15 anos!'
				END
			END
			ELSE 
			BEGIN 
				SET @saida = 'Esse RA não existe!'
			END
		END
	END
	ELSE IF (@cpf_valido = 1)
	BEGIN 
		SET @saida = 'CPF inválido!'
	END
	ELSE IF (@cpf_valido = 2)
	BEGIN 
		SET @saida = 'CPF não pode ter todos os numeros iguais!'
	END
	IF (UPPER(@op) = 'D')
	BEGIN
		IF (@ra_existe IS NOT NULL)
		BEGIN 
			UPDATE matricula 
			SET status = 'Trancado.'
			WHERE ra_aluno = @ra
            SET @saida = 'Matricula trancada!'
		END
		ELSE 
		BEGIN 
			SET @saida = 'Esse RA não existe!'
		END
	END
GO

CREATE PROCEDURE sp_verifica_cpf 
(
	@cpf CHAR(11), 
	@cpf_valido INT OUTPUT
)
AS
	DECLARE @cpf_num_iguais CHAR(11),
			@cpf_count INT = 10,
			@cpf_soma INT = 0,
			@cpf_unidade INT = 1,
			@cpf_ult_digito VARCHAR(2) = ''
	SET @cpf_num_iguais = CONCAT(SUBSTRING(@cpf, 1, 1), REPLACE(@cpf, SUBSTRING(@cpf, 1, 1), ''))
	IF (LEN(@cpf_num_iguais) != 1)
	BEGIN
		WHILE (@cpf_count >= 2)
		BEGIN
			SET @cpf_soma = @cpf_soma + (@cpf_count * CAST(SUBSTRING(@cpf, @cpf_unidade, 1) AS INT))
			SET @cpf_count = @cpf_count - 1
			SET @cpf_unidade = @cpf_unidade + 1
		END
		IF (@cpf_soma % 11 = 1 OR @cpf_soma % 11 = 0)
		BEGIN 
			SET @cpf_ult_digito = CONCAT(@cpf_ult_digito, '0')
		END
		ELSE 
		BEGIN 
			SET @cpf_ult_digito = CONCAT(@cpf_ult_digito, CAST((11 - (@cpf_soma % 11)) AS VARCHAR))
		END
		SET @cpf_count = 11
		SET @cpf_soma = 0
		SET @cpf_unidade = 1
		WHILE (@cpf_count >= 2)
		BEGIN
			IF (@cpf_unidade <= 9)
			BEGIN 
				SET @cpf_soma = @cpf_soma + (@cpf_count * CAST(SUBSTRING(@cpf, @cpf_unidade, 1) AS INT))
			END
			ELSE 
			BEGIN 
				SET @cpf_soma = @cpf_soma + (@cpf_count * CAST(@cpf_ult_digito AS INT))
			END
			SET @cpf_count = @cpf_count - 1
			SET @cpf_unidade = @cpf_unidade + 1
		END
		IF (@cpf_soma % 11 = 1 OR @cpf_soma % 11 = 0)
		BEGIN 
			SET @cpf_ult_digito = CONCAT(@cpf_ult_digito, '0')
		END
		ELSE 
		BEGIN 
			SET @cpf_ult_digito = CONCAT(@cpf_ult_digito, CAST((11 - (@cpf_soma % 11)) AS VARCHAR))
		END
		IF (SUBSTRING(@cpf, 10, 2) = @cpf_ult_digito)
		BEGIN 
			SET @cpf_valido = 0 --cpf valido
		END
		ELSE 
		BEGIN 
			SET @cpf_valido = 1 --cpf invalido
		END
	END
	ELSE
	BEGIN
		SET @cpf_valido = 2 --cpf não pode com numeros iguais
	END
GO

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
GO

CREATE PROCEDURE sp_dispensar (@ra CHAR(9), @codigo_disciplina INT)
AS
	UPDATE matricula_disciplina
	SET status = 'Dispensado.'
	FROM matricula_disciplina md, matricula m, aluno a
	WHERE md.id_matricula = m.id
	AND m.ra_aluno = a.ra
	AND a.ra = @ra
	AND md.codigo_disciplina = @codigo_disciplina
GO

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
GO

CREATE PROCEDURE sp_verifica_idade (@dt_nascimento AS DATE, @valida BIT OUTPUT)
AS
	DECLARE @idade INT
	SET @idade = CASE 
					WHEN (MONTH(@dt_nascimento) - MONTH(GETDATE())) = 0
					THEN CASE 
							WHEN (DAY(@dt_nascimento) - DAY(GETDATE())) <= 0
							THEN DATEDIFF(YEAR, @dt_nascimento, GETDATE())
							ELSE DATEDIFF(YEAR, @dt_nascimento, GETDATE()) - 1
						 END
					ELSE DATEDIFF(YEAR, @dt_nascimento, GETDATE()) - 1
				 END
	IF (@idade >= 16)
	BEGIN 
		SET @valida = 1
	END
	ELSE
	BEGIN
		SET @valida = 0
	END
GO

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
GO

CREATE PROCEDURE sp_matricula_aluno (@ra CHAR(9), @codigo_curso INT)
AS 
	DECLARE @semestre INT,
			@semestre_ingresso INT,
			@ano_limite_graduacao INT,
			@ano_ingresso INT
	SET @semestre = 1
	SET @semestre_ingresso = CASE 
								WHEN MONTH(GETDATE()) <= 6 THEN 1
								ELSE 2
							 END
	SET @ano_limite_graduacao = YEAR(GETDATE()) + 5 
	SET @ano_ingresso = YEAR(GETDATE())
	INSERT INTO matricula (ra_aluno, codigo_curso, semestre, semestre_ingresso, ano_limite_graduacao, ano_ingresso, status)
	VALUES
	(@ra, @codigo_curso, @semestre, @semestre_ingresso, @ano_limite_graduacao, @ano_ingresso, 'Cursando.')
GO

CREATE PROCEDURE sp_matricula_disciplina (@matricula CHAR(11), @codigo INT, @status VARCHAR(50), @saida VARCHAR(50) OUTPUT)
AS
	DECLARE @matricula_existe CHAR(11)
	SET @matricula_existe = (SELECT id FROM matricula WHERE id = @matricula)
	IF (@matricula_existe IS NOT NULL)
	BEGIN 
		UPDATE matricula_disciplina 
		SET status = @status
		WHERE id_matricula = @matricula AND codigo_disciplina = @codigo
		SET @saida = 'Matricula em andamento!'
	END
	ELSE
	BEGIN
		SET @saida = 'Matricula não existe!'
	END
GO

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
GO

CREATE PROCEDURE sp_gera_ra (@ra CHAR(9) OUTPUT)
AS
    DECLARE @r VARCHAR (20),
            @semestre INT,
            @data    CHAR (4)
    SET @r = CAST(CAST(RAND()*10 AS INT)AS varchar)+''+CAST(CAST(RAND()*10 AS INT)AS varchar)+''+CAST(CAST(RAND()*10 AS INT)AS varchar)+''+CAST(CAST(RAND()*10 AS INT)AS varchar)
    SET @data = SUBSTRING(CAST(GETDATE() as varchar),8,4)
    IF (MONTH(GETDATE()) <= 6)
        BEGIN
            SET @semestre = 1
        END
    ELSE
        BEGIN
            SET @semestre = 2
        END
    SET @ra = @data+''+CAST(@semestre as varchar)+''+@r

-- Verifica de RA
GO
CREATE PROCEDURE sp_ra_aluno @ra CHAR(9) OUTPUT
AS
	    EXEC sp_gera_ra @ra OUTPUT
	WHILE EXISTS (SELECT * FROM aluno WHERE aluno.ra = @ra) 
	BEGIN
	    EXEC sp_gera_ra @ra OUTPUT
	END
GO

CREATE PROCEDURE sp_telefone_aluno (@op CHAR(1), @ra AS CHAR(9), @numero AS CHAR(9),@num_velho AS CHAR(9), @saida AS VARCHAR(50) OUTPUT) -- mudado
AS
    IF (@op = 'I')
    BEGIN
        IF ((SELECT numero FROM telefone WHERE numero = @numero AND ra_aluno = @ra) IS NULL)
        BEGIN 
            INSERT INTO telefone
            VALUES (@ra, @numero)
            SET @saida = 'cadastrado'
        END
        ELSE
        BEGIN
            SET @saida = 'erro'
        END
    END
    ELSE IF (@op = 'U')
    BEGIN
            IF(@numero IS NOT NULL OR @num_velho IS NOT NULL) BEGIN
                IF (@numero IS NOT NULL) BEGIN
                    IF(@num_velho IS NOT NULL) BEGIN
                        IF (NOT EXISTS(SELECT * FROM telefone WHERE numero = @numero AND ra_aluno = @ra  )) BEGIN
                            PRINT 'update'
                            UPDATE telefone 
                            SET numero = @numero
                            WHERE ra_aluno = @ra  AND numero = @num_velho
                            SET @saida = 'atualizado'
                        END ELSE BEGIN
                            SET @saida = 'erro'
                        END
                    END ELSE BEGIN
                        EXEC sp_telefone_aluno 'I', @ra, @numero, NULL,@saida OUTPUT
                    END
                END ELSE BEGIN
                    EXEC sp_telefone_aluno 'D', @ra, NULL, @num_velho,@saida OUTPUT
                END
            END ELSE BEGIN
                SET @saida = 'erro'
            END
    END
    ELSE IF (@op = 'D')
    BEGIN
        IF (@num_velho IS NULL) BEGIN
            DELETE telefone WHERE ra_aluno = @ra
        END
        ELSE BEGIN
            DELETE telefone WHERE ra_aluno = @ra AND numero = @num_velho
            SET @saida = 'excluido'
        END
    END
GO

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
		SELECT COUNT(p.id), p.id_matricula, p.id_conteudo, m.ra_aluno, a2.nome, MAX(CASE p.status WHEN 1 THEN 0 ELSE 0 END) AS status  FROM presenca p, aula a, matricula m, aluno a2
		WHERE p.id_matricula = a.id_matricula 
			AND p.id_conteudo = a.id_conteudo 
			AND a.id_matricula = m.id 
			AND m.ra_aluno = a2.ra
			AND p.id_conteudo = @id
		GROUP BY p.id_matricula, p.id_conteudo, m.ra_aluno, a2.nome
	RETURN
END
GO

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
GO

INSERT INTO curso (nome, carga_horaria, sigla, ult_nota_participacao_enade)
VALUES 
('Engenharia Civil', 240, 'ENG-CIV', 7.8),
('Psicologia', 240, 'PSI', 8.7),
('Arquitetura e Urbanismo', 240, 'ARQ', 7.5),
('Letras', 240, 'LET', 7.2)

INSERT INTO professor VALUES 
('Satoshi'),
('Colevate'),
('Vendramel'),
('Cristina'),
('Wellington Pinto')

INSERT INTO disciplina (codigo_curso, nome, qntd_hora_semanais, dia_aula, horario_inicio, horario_fim, id_professor)
VALUES
(1, 'Mecânica dos Sólidos', 4, 1, '13:00:00', '16:30:00', 1),
(1, 'Desenho Técnico', 2, 1, '13:00:00', '14:40:00', 2),
(1, 'Cálculo I', 4, 2, '14:50:00', '18:20:00', 3),
(1, 'Física I', 4, 3, '13:00:00', '16:30:00', 4),
(1, 'Química Geral', 2, 4, '13:00:00', '14:40:00', 5),
(1, 'Introdução à Engenharia Civil', 4, 4, '14:50:00', '18:20:00', 1),
(1, 'Álgebra Linear', 4, 5, '14:50:00', '18:20:00', 2),
(2, 'Introdução à Psicologia', 4, 1, '13:00:00', '16:30:00', 3),
(2, 'Neuropsicologia', 2, 1, '13:00:00', '14:40:00', 4),
(2, 'Psicologia do Desenvolvimento', 4, 2, '14:50:00', '18:20:00', 5),
(2, 'Psicopatologia', 4, 3, '13:00:00', '16:30:00', 1),
(2, 'Psicologia Organizacional', 2, 3, '13:00:00', '14:40:00', 2),
(2, 'Teorias da Personalidade', 4, 4, '14:50:00', '18:20:00', 3),
(2, 'Métodos de Pesquisa em Psicologia', 4, 5, '14:50:00', '18:20:00', 4),
(3, 'Introdução à Arquitetura', 4, 1, '13:00:00', '16:30:00', 5),
(3, 'Tecnologias da Construção Sustentável', 2, 1, '13:00:00', '14:40:00', 1),
(3, 'Desenho Arquitetônico', 4, 2, '14:50:00', '18:20:00', 2),
(3, 'História da Arquitetura', 4, 3, '13:00:00', '16:30:00', 3),
(3, 'Paisagismo', 2, 3, '13:00:00', '14:40:00', 4),
(3, 'Planejamento Urbano', 4, 4, '14:50:00', '18:20:00', 5),
(3, 'Construção Civil', 4, 5, '14:50:00', '18:20:00', 1),
(4, 'Língua Portuguesa', 4, 1, '13:00:00', '16:30:00', 2),
(4, 'Teoria Literária', 2, 1, '13:00:00', '14:40:00', 3),
(4, 'Literatura Brasileira', 4, 2, '14:50:00', '18:20:00', 4),
(4, 'Literatura Estrangeira', 4, 3, '13:00:00', '16:30:00', 5),
(4, 'Linguística', 2, 3, '13:00:00', '14:40:00', 1),
(4, 'Gramática', 4, 4, '14:50:00', '18:20:00', 3),
(4, 'Fonética e Fonologia', 4, 5, '14:50:00', '18:20:00', 5)


--INSERT para conteudo

DECLARE @cont 				INT,
		@id_disciplina		INT,
		@ult_id_disciplina	INT,
		@nome				VARCHAR(50),
		@descricao			VARCHAR(100)
SET @cont = 0
SET @id_disciplina = (SELECT TOP 1 codigo FROM disciplina)
SET @ult_id_disciplina = (SELECT TOP 1 codigo FROM disciplina ORDER BY codigo DESC)
SET @nome = 'conteudo'
SET @descricao = 'descricao'
WHILE (@id_disciplina <= @ult_id_disciplina)
BEGIN
	SET @cont = @cont + 1
	
	INSERT INTO conteudo (codigo_disciplina, nome, descricao)
	VALUES
	(@id_disciplina, @nome + ' ' + CAST(@cont AS VARCHAR(5)), @descricao + ' ' + cast(@cont AS VARCHAR(5)))
	
	IF (@cont = 10)
	BEGIN
		SET @cont = 0
		SET @id_disciplina = @id_disciplina + 1
	END
END

GO

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

Select *
From matricula

Select a.ra, m.id_matricula
From matricula_disciplina m, aluno a

SELECT *
FROM aluno

DELETE vestibular
GO
DELETE telefone
GO
DELETE nota
GO
DELETE matricula_disciplina
GO
DELETE matricula
GO
DELETE aluno

DECLARE @ra 				CHAR(9),
		@matricula			INT,
		@cpf				CHAR(11)

		SET @cpf = '85735171070'
		SET @ra = (SELECT a.ra FROM aluno a WHERE a.cpf = @cpf)
		SET @matricula = (SELECT m.id FROM matricula m WHERE m.ra_aluno = @ra)
	DELETE FROM vestibular WHERE ra_aluno = @ra
	DELETE FROM telefone WHERE ra_aluno = @ra
	DELETE FROM nota WHERE id_matricula = @matricula
	DELETE FROM matricula_disciplina WHERE id_matricula = @matricula
	DELETE FROM matricula WHERE ra_aluno = @ra
	DELETE FROM aluno WHERE cpf = @cpf