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

<title>Disciplina-dispensado</title>
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
						</div>
					</div>
				</div>
			</nav>
		<main>
			<form action="disciplina-dispensado" method="post" name="formDisciplina-dispensado">
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
						<table class="table table-striped" id="tabela-disciplinas">
							<thead>
								<tr>
									<th class="col">Dispensar Disciplina</th>
									<th class="col">Nome</th>
									<th class="col">Quantidade de Horas Semanais</th>
									<th class="col" style="min-width: 120px;">Dia de aula</th>
									<th class="col">Horario de Inicio</th>
									<th class="col">Horario de Término</th>
									<th class="col" style="min-width: 130px">Status</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${not empty disciplinas}">
									<c:forEach var="d" items="${disciplinas}">
										<tr ref="${d.diaAula}">
											<td>
												<div>
													<button class="btn btn-danger"  name="botao" value="Dispensar${d.codigo}">Dispensar </button>
												</div>
											</td>
											<th scope="row"><c:out value="${d.nome}"/></th>
											<td><c:out value="${d.qntdHoraSemanais}"/> Horas</td>
											<td><c:out value="${d.diaAula}"/></td>
											<td><c:out value="${d.horarioInicio}"/></td>
											<td><c:out value="${d.horarioFim}"/></td>
											<td><c:out value="${d.umMatriculaDisciplina.status}"/></td>
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
