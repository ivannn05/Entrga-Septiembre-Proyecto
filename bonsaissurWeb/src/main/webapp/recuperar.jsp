<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña</title>
    <link rel="icon" href="imagenes/LogoTienda.jpg" type="image/jpg">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center vh-100 bg-light">

    <div class="card p-4 shadow-lg" style="max-width: 400px; width: 100%;">
        <h4 class="text-center mb-3">Recuperar Contraseña</h4>
        <p class="text-muted text-center">Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.</p>
        
        <form action="<%=request.getContextPath()%>/correoRecuperar" method="POST">
            <div class="mb-3">
                <label for="correoRecuperar" class="form-label">Correo Electrónico</label>
                <input type="email" class="form-control" name="correo" id="correo" placeholder="tuemail@example.com" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Enviar Enlace</button>
        </form>

        <div class="text-center mt-3">
            <a href="<%=request.getContextPath()%>/index.jsp" class="text-decoration-none">Volver al inicio de sesión</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
