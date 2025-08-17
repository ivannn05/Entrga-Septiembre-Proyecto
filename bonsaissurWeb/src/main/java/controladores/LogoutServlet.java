package controladores;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import utilidades.Util;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/cerrarSesion"})
/**
 * Controaldor encargado de cerrar sesion
 */
public class LogoutServlet extends HttpServlet {

    @Override
    /**
     * Clase encargada de cerrar la sesion 
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        try {
            Util.ficheroLog("Usuario entro en cerrar sesion");
            if (session != null) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        } catch (Exception e) {
        	Util.ficheroLog("Ocurrio un error en cerrarSesion :" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}