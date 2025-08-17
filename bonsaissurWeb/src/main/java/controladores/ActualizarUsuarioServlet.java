package controladores;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dtos.Usuario;
import servicios.UsuarioServicio;
import utilidades.*;

@WebServlet(name = "ActualizarUsuarioServlet", urlPatterns = { "/actualizar" })
/**
 * Controaldor encargado de actualizar el usuario 
 */
public class ActualizarUsuarioServlet extends HttpServlet {

	private UsuarioServicio service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = new UsuarioServicio();
	}

	@Override
	/**
	 * Metodo que recibe el action del form de Actualizar
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		String direccion = request.getParameter("direccion");
		String telefono = request.getParameter("telefono");

		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("Usuario");

		try {
			Util.ficheroLog("Usuario entro en actualizar usuario");
			String respuesta = service.Actualizar(nombre, apellidos, usuario.getCorreo(), direccion, telefono);
			if ("Usuario actualizado".equals(respuesta)) {
				// Recargar el usuario actualizado desde la BD
				Usuario usuarioActualizado = service.login(usuario.getCorreo(), usuario.getContrasena()); 
				
				// Reemplazar el objeto en sesión
				session.setAttribute("Usuario", usuarioActualizado);

				// Redirigir a la misma página o a donde desees
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			} else {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
			}
		} catch (Exception e) {
			Util.ficheroLog("Ocurrio un error en actualizar un usuario:" + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}
	}
}
