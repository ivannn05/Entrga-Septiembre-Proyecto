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

@WebServlet(name = "CambiarContrasenaServlet", urlPatterns = {"/escribirContrasena"})
/**
 * Controaldor encargado de actualizar la contraseña del usuario 
 */
public class CambiarContrasenaServlet extends HttpServlet {

    private UsuarioServicio service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new UsuarioServicio();
    }

    @Override
    /**
     * Metodo encargado de recibir la nueva contrseña y mandar al metodo de actualizar contraseña
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nuevaContrasena = request.getParameter("nuevaContrasena");
        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        try {
            Util.ficheroLog("Usuario entro en escribir Contraseña");
            String respuesta = service.actualizarContrasena(nuevaContrasena, token);

            if ("Usuario actualizado".equals(respuesta)) {
                // Redirige a loginUsu.jsp
                response.sendRedirect(request.getContextPath() + "/loginUsu.jsp");
            } else {
                // Redirige a index.jsp con error
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } catch (Exception e) {
            Util.ficheroLog("Ocurrió un error en escribir contraseña nueva: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}

