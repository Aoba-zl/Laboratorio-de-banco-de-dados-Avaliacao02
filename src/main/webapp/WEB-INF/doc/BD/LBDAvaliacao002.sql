USE master

DROP DATABASE matricula


CREATE DATABASE matricula
GO
USE matricula
GO
CREATE TABLE aluno
(
	ra								CHAR(9) 				NOT NULL,
	cpf								CHAR(11) 				NOT NULL UNIQUE,
	nome							VARCHAR(100) 			NOT NULL,
	nome_social						VARCHAR(100)			NULL,
	dt_nascimento					DATE					NOT NULL,
	email_pessoal					VARCHAR(100) 			NOT NULL UNIQUE,
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
