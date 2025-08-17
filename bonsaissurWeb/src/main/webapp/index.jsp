<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dtos.Usuario"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Bonsai Sur</title>
<link rel="icon" href="imagenes/LogoTienda.jpg" type="image/jpg">
<link rel="stylesheet" href="estilo.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet">
</head>
<body>

	<!-- Barra de Navegación -->
	<nav class="navbar navbar-expand-lg navbar-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="#"> <img
				src="imagenes/LogoTienda.jpg" alt="Logo Bonsai Sur"> BONSAI
				SUR
			</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
					<li class="nav-item"><a class="nav-link" href="index.jsp">INICIO</a></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="arbolesDropdown"
						role="button" data-bs-toggle="dropdown">ARBOLES</a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="arboles.jsp">Bonsáis</a></li>
							<li><a class="dropdown-item" href="arboles.jsp">Prebonsáis</a></li>
						</ul></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="macetasDropdown"
						role="button" data-bs-toggle="dropdown">MACETAS</a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="macetas.jsp">Cerámica</a></li>
							<li><a class="dropdown-item" href="macetas.jsp">Plástico</a></li>
						</ul></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="abonosDropdown"
						role="button" data-bs-toggle="dropdown">ABONOS</a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="abonos.jsp">Orgánicos</a></li>
							<li><a class="dropdown-item" href="abonos.jsp">Químicos</a></li>
						</ul></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#"
						id="herramientasDropdown" role="button" data-bs-toggle="dropdown">HERRAMIENTAS</a>
						<ul class="dropdown-menu">
							<li><a class="dropdown-item" href="herramientas.jsp">Corte</a></li>
							<li><a class="dropdown-item" href="herramientas.jsp">Mantenimiento</a></li>
						</ul></li>
				</ul>
				<!-- Barra de búsqueda -->
				<form class="d-flex me-3">
					<input class="form-control me-2" type="search"
						placeholder="Buscar...">
					<button class="btn btn-outline-light" type="submit">
						<i class="bi bi-search"></i>
					</button>
				</form>
				<!-- Íconos -->
				<a  class="me-3 icono"><i class="bi bi-cart"></i></a>
				<%
				Usuario usuario = (Usuario) session.getAttribute("Usuario");
				if (usuario != null) {
					if ("Administrador".equals(usuario.getRol())) {
				%>
				<a href="login.jsp" class="icono"><i class="bi bi-person"></i></a>
				<%
				} else {
				%>
				<a href="loginUsu.jsp" class="icono"><i class="bi bi-person"></i></a>
				<%
				}
				} else {
				%>
				<a href="loginUsu.jsp" class="icono"><i class="bi bi-person"></i></a>
				<%
				}
				%>
			</div>
		</div>
	</nav>

	<!-- Contenido principal -->
	<div class="container my-4">
		<div class="content-block">
			<div class="left-image agrandar">
				<a href="herramientas.jsp"><img src="imagenes/Herramientas/Herramienta.jpg"
					alt="Herramientas"></a>
			</div>
			<div class="center-images">
				<a class="agrandar" href="arboles.jsp"><img
					src="imagenes/Arboles/Azalea.jpg" alt="Bonsái"></a> <a
					class="agrandar" href="macetas.jsp"><img
					src="imagenes/Macetas/Macetas.jpg" alt="Macetas"></a>
			</div>
			<div class="right-image agrandar">
				<a href="abonos.jsp"><img src="imagenes/Abonos/Abono.jpg" alt="Abonos"></a>
			</div>
		</div>
	</div>

	<!-- Sección "¿Quiénes somos?" -->
	<div class="quienes-somos-section position-relative">
		<img src="imagenes/Repisas.png" alt="Fondo" class="w-100">
		<div
			class="position-absolute top-50 start-50 translate-middle text-white bg-dark bg-opacity-50 p-4 rounded">
			<h1 class="fw-bold" align="center">¿QUIÉNES SOMOS?</h1>
			<p>Esta tienda es un proyecto de fin de grado donde se muestran
				imágenes de árboles en disponibilidad.</p>
		</div>
	</div>

	<!-- Pie de página -->
	<footer class="bg-dark text-white text-center py-3 mt-4">
		<p class="m-0">© 2024 Bonsai Sur. Todos los derechos reservados.</p>
	</footer>

	<!-- Scripts -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script>
        document.addEventListener("DOMContentLoaded", function () {
            const currentPath = window.location.pathname.split("/").pop();
            document.querySelectorAll(".nav-link").forEach(link => {
                if (link.getAttribute("href") === currentPath) {
                    link.classList.add("active");
                }
            });
        });
    </script>
</body>
</html>
