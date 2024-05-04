<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<!-- Bootstrap -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

<!-- Script -->
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<link rel="stylesheet" type="text/css" href='<c:url value="./resources/css/style.css"/>'>

<title>Historico</title>
</head>
<body>
<div class="bg-black custom">
	<div class="container bg-white scroll" id="nav-container">
		<nav class="navbar navbar-expand">
			<div class="container-fluid">
				<div class="m-auto">
					<div class="row">
						<div class="col-auto p-0 text-center" style="width: 160px;">
							<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='./'">home</button>
						</div>
						<div class="col-auto p-0 text-center" style="width: 160px;">
							<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='./aluno'">Pagina Aluno</button>
						</div>
						<div class="col-auto p-0 text-center" style="width: 160px;">
							<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='./curso'">Ver Cursos</button>
						</div>
						<div class="col-auto p-0 text-center" style="width: 160px;">
							<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='./lista-chamada'">Lista de chamada</button>
						</div>
					</div>
				</div>
			</div>
		</nav>
			<nav class="navbar navbar-expand">
				<div class="container-fluid">
					<div class="m-auto">
						<div class="row">
							<div class="col-auto p-0 text-center" style="width: 190px;">
								<button class="btn btn-outline-primary" style="width: 180px" OnClick="window.location.href='./aluno'">Aluno</button>
							</div>
							<div class="col-auto p-0 text-center" style="width: 190px;">
								<button class="btn btn-outline-primary" style="width: 180px" OnClick="window.location.href='./disciplina-dispensado'">Disciplina Dispensada</button>
							</div>
							<div class="col-auto p-0 text-center" style="width: 190px;">
								<button class="btn btn-outline-primary" style="width: 180px" OnClick="window.location.href='./disciplina'">Disciplina</button>
							</div>
							<div class="col-auto p-0 text-center" style="width: 190px;">
								<button class="btn btn-outline-primary" style="width: 180px" OnClick="window.location.href='./historico'">Historico</button>
							</div>
						</div>
					</div>
				</div>
			</nav>
		<main>
			<form action="historico" method="post" name="formHistorico">
				<div class="rounded-4 border border-primary form-container m-auto mb-3">
					<div class="form-floating d-flex mb-3">
						<input type=text class="form-control input-height" id="floatingInput" placeholder="RA" name="ra" maxlength="9" oninput="this.value = this.value.replace(/[^0-9]/g, '')" value='<c:out value="${ra}"></c:out>'>
						<label for="floatingInput" class="font-text">RA</label>
						<button class="btn btn-outline-secondary" name="botao" value="Buscar">Buscar</button>
					</div>
				</div>
				<div>
					<c:if test="${not empty erro}">
						<h2 class="text-center"><b><c:out value="${erro}"/></b></h2>
					</c:if>
				</div>
				<div>
					<c:if test="${not empty saida}">
						<h2 class="text-center"><b><c:out value="${saida}"/></b></h2>
					</c:if>
				</div>
				<div class="form-container m-auto border border-primary rounded-4 mb-3" style="max-width: 900px; max-height: 690px;">
					<div class="form-container m-auto" style="max-width: 900px; max-height: 600px; overflow-y: scroll;">
						<table class="table table-striped" id="tabela-historico">
							<thead>
								<tr>
									<th class="col">RA</th>
									<th class="col">Nome</th>
									<th class="col">Curso</th>
									<th class="col" style="min-width: 120px;">Data da Matricula</th>
									<th class="col">Pontuação do Vestibular</th>
									<th class="col">Posição no Vestibular</th>
								</tr>
							</thead>
								<c:if test="${not empty aluno}">
										<tr>
											<td><c:out value="${aluno.ra}"/></td>
											<td><c:out value="${aluno.nome}"/></td>
											<td><c:out value="${aluno.matricula.curso.nome}"/></td>
											<td><c:out value="${aluno.matricula.anoIngresso}"/></td>
											<td><c:out value="${aluno.vestibular.pontuacao}"/></td>
											<td><c:out value="${aluno.vestibular.posicao}"/></td>
										</tr>
								</c:if>
						</table>
					</div>
				</div>
				<div class="form-container m-auto border border-primary rounded-4 mb-3" style="max-width: 900px; max-height: 690px;">
					<div class="form-container m-auto" style="max-width: 900px; max-height: 600px; overflow-y: scroll;">
						<table class="table table-striped" id="tabela-historico">
							<thead>
								<tr>
									<th class="col">Codigo da Disciplina</th>
									<th class="col">Nome da Disciplina</th>
									<th class="col">Nome do Professor</th>
									<th class="col">Nota Final</th>
									<th class="col">Quantidade de Faltas</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${not empty disciplinas}">
									<c:forEach var="d" items="${disciplinas}">
										<tr>
											<td> <c:out value="${d.codigo}"/></td>
											<td><c:out value="${d.nome}"/> </td>
											<td><c:out value="${d.professor.nome}"/></td>
											<td><c:out value="${medias.get(0)}"/></td>
											<td><c:out value="${faltas.get(0)}"/></td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</form>
		</main>
	</div>
</div>
</body>
</html>
