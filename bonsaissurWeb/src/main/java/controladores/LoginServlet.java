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
import utilidades.Util;

@WebServlet("/login")
/**
 * Controlador encargado del login del usuario 
 */
public class LoginServlet extends HttpServlet {

    private UsuarioServicio usuarioServicio;

    @Override
    public void init() throws ServletException {
        usuarioServicio = new UsuarioServicio();
    }

    @Override
    /**
     * Metodo encargado de pasar los valores del login
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("Estoy en servlet login");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        HttpSession session = request.getSession();

        try {
        	
        
        	Util.ficheroLog("Usuario con correo:" + correo + " entro en login");
            Usuario usu = usuarioServicio.login(correo, Util.encriptarContrasena(contrasena));
           
            if (usu != null && usu.getNombre() != null) {
                session.setAttribute("Usuario", usu);
                UsuarioServicio.getAllUsu(session);
                if ("Administrador".equals(usu.getRol())) {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/loginUsu.jsp");
            }
        } catch (Exception e) {
        	Util.ficheroLog("Ocurrió un error en login: " + e.getMessage());
           
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

}
