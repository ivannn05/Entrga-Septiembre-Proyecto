package controladores;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servicios.UsuarioServicio;
import utilidades.Util;

@WebServlet(name = "EliminarUsuarioServlet", urlPatterns = { "/eliminar" })
/**
 * Clas eencargada de recibir la llamada del action para elimianr usuario 
 */
public class EliminarUsuarioServlet extends HttpServlet {

	private UsuarioServicio service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = new UsuarioServicio();
	}

	@Override
	/**
	 * Metodo encargado de mandar el correo del usuario a eliminar 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String correo = request.getParameter("correo");
		HttpSession session = request.getSession();

		try {
			Util.ficheroLog("Usuario entró en eliminar usuario");

			// Obtener lista actualizada de usuarios y guardarla en sesión
			UsuarioServicio.getAllUsu(session);

			// Intentar eliminar el usuario
			String respuesta = service.Eliminar(correo);
			 UsuarioServicio.getAllUsu(session);
			if ("Usuario Eliminado".equals(respuesta)) {
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			} else {
				// Enviar un mensaje de error o redirigir según convenga
				session.setAttribute("errorEliminar", respuesta); // Puedes mostrar este mensaje en el JSP
				response.sendRedirect(request.getContextPath() + "/login.jsp"); // O donde estés gestionando usuarios
			}
		} catch (Exception e) {
			Util.ficheroLog("Ocurrió un error en EliminarUsuarioServlet: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/login.jsp"); // Redirección a página de error
		}
	}
}
