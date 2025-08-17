package controladores;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servicios.UsuarioServicio;
import utilidades.Util;

@WebServlet(name = "RecuperarContrasenaServlet", urlPatterns = {"/correoRecuperar"})
/**
 * Controlador encargado de recuperar la contraseña del action 
 */
public class RecuperarContrasenaServlet extends HttpServlet {

    private UsuarioServicio service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new UsuarioServicio();
    }

    @Override
    /**
     * Metodo encargado de pasar al metodo el correro del ususario
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");

        try {
            Util.ficheroLog("Usuario entro en recuperar contraseña");
            Util.ficheroLog("Correo de usu="+correo);
            String respuesta = service.recuperarContrasena(correo);
        
            if ("Correo enviado".equals(respuesta)) {
                response.sendRedirect(request.getContextPath() + "/mirarCorreo.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        } catch (Exception e) {
            Util.ficheroLog("Ocurrio un error en recuperar contraseña:" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
