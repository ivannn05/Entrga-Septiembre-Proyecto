package controladores;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utilidades.Util;
import servicios.UsuarioServicio;

@WebServlet(name = "ConfirmarUsuarioServlet", urlPatterns = { "/confirmarUsu" })
/**
 * Controaldor encargado de confirma el usuario en el registro
 */
public class ConfirmarUsuarioServlet extends HttpServlet {

	private UsuarioServicio service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = new UsuarioServicio();
	}

	@Override
	/**
	 * Claseencargada de mandar el token para la confirmacion del usuario 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String token = request.getParameter("token");

		try {
			Util.ficheroLog("Entre en Controaldor confirmar USU \n");
			String respuesta = service.confirmarCuenta(token);
			Util.ficheroLog("Respuesta de  confirmar USU" + respuesta + " \n");
			if ("Cuenta activada con éxito.".equals(respuesta)) {
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			} else {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
			}
		} catch (Exception e) {
			Util.ficheroLog("Ocurrio un error en confirmar usuario:" + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}
	}
}
