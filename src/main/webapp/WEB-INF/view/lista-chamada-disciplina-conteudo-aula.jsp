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

<link rel="stylesheet" type="text/css" href="/LBDAvaliacao02/resources/css/style.css">

<title>Aula</title>
</head>
<body>
	<div class="bg-black custom">
		<div class="container bg-white scroll" id="nav-container">
			<nav class="navbar navbar-expand">
				<div class="container-fluid">
					<div class="m-auto">
						<div class="row">
							<div class="col-auto p-0 text-center" style="width: 160px;">
								<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='/LBDAvaliacao02/'">home</button>
							</div>
							<div class="col-auto p-0 text-center" style="width: 160px;">
								<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='/LBDAvaliacao02/aluno'">Pagina Aluno</button>
							</div>
							<div class="col-auto p-0 text-center" style="width: 160px;">
								<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='/LBDAvaliacao02/curso'">Ver Cursos</button>
							</div>
							<div class="col-auto p-0 text-center" style="width: 160px;">
								<button class="btn btn-outline-primary" style="width: 150px" OnClick="window.location.href='/LBDAvaliacao02/lista-chamada'">Lista de chamada</button>
							</div>
						</div>
					</div>
				</div>
			</nav>
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
			<form action="${codigoDisciplinaConteudo}" method="post">
				<div class="form-container m-auto border border-primary rounded-4" style="max-width: 650px;">
					<table class="table table-striped">
						<thead>
							<tr>
								<th class="col">Selecionar Presença</th>
								<th class="col">RA</th>
								<th class="col">Nome do aluno</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty aula.presenca}">
								<c:forEach var="p" items="${aula.presenca}">
									<tr>
										<td>
											<div class="d-flex">
												<c:forEach var="i" begin="1" end="${p.id}">
													<div class="me-2">
														<input type="checkbox" class="form-check-input checkbox-disciplina" name="checkboxAulaAluno" value="${i}-${p.matricula.aluno.ra}">
													</div>
												</c:forEach>
											</div>
										</td>
										<td><c:out value="${p.matricula.aluno.ra}"/></td>
										<td scope="row"><c:out value="${p.matricula.aluno.nome}"/></td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					<div class="form-container m-auto d-flex" style="max-width: 900px;">
						<button class="btn btn-success ms-auto" name="botao" value="confirmarChamada">Confirmar</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
