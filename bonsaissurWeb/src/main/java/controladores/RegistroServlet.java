package controladores;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servicios.UsuarioServicio;
import utilidades.Util;

@WebServlet("/registro")
/**
 * Controador encargado del registro del usuario 
 */
public class RegistroServlet extends HttpServlet {
	private UsuarioServicio usuarioServicio;

	@Override
	public void init() throws ServletException {
		usuarioServicio = new UsuarioServicio();
	}

	@Override
	/**
	 * Metodo el cual le pasa los valores de los usuarios
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		String correo = request.getParameter("correo");
		String contrasena = request.getParameter("contrasena");
		String direccion = request.getParameter("direccion");
		String telefono = request.getParameter("telefono");
		String rol = request.getParameter("rol");

		try {
			String respuesta = usuarioServicio.DatosRegistro(
				nombre, apellidos, correo, direccion, telefono,
				Util.encriptarContrasena(contrasena), rol
			);

			if ("Enviado".equals(respuesta)) {
				response.sendRedirect(request.getContextPath() + "/mirarCorreo.jsp");
			} else {
				response.sendRedirect(request.getContextPath() + "/errorRegistro.jsp");
			}
		} catch (Exception e) {
			Util.ficheroLog("Ocurrió un error en registro: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/errorRegistro.jsp");
		}
	}
}
