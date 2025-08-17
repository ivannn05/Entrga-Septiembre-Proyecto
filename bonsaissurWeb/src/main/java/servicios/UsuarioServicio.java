package servicios;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import dtos.Usuario;

import utilidades.ConexionBD;
import utilidades.Util;
/**
 * Servicio encargado de contener la logica de los metodos de la pagina
 */
public class UsuarioServicio {
	public UsuarioServicio() {
	}
/**
 * Metodo para traer a todos los usuarios 
 * @param session
 */
	public static void getAllUsu(HttpSession session) {
		List<Usuario> usuarios = new ArrayList<>();

		String sql = "SELECT * FROM usuarios";

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getLong("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setCorreo(rs.getString("correo"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setTelefono(rs.getString("telefono"));
				usuario.setContrasena(rs.getString("contrasena"));
				usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
				usuario.setRol(rs.getString("rol"));
				usuarios.add(usuario);
			}

			session.setAttribute("listaUsuarios", usuarios);
			for (Usuario usuario : usuarios) {
				Util.ficheroLog(usuario.toString() + "\n");
			}
		} catch (Exception e) {
			System.out.println("Error al obtener todos los usuarios: " + e.getMessage());
			Util.ficheroLog("Error en getAllUsu BD: " + e.getMessage());
		}
	}
/**
 * Mettodo encargado del login de usuairos 
 * @param correo
 * @param contrasena
 * @return
 */
	public Usuario login(String correo, String contrasena) {
		Usuario usuario = null;

		String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";

		try (Connection conn = ConexionBD.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, correo);
			stmt.setString(2, contrasena);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				usuario = new Usuario();
				usuario.setId(rs.getLong("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setCorreo(rs.getString("correo"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setTelefono(rs.getString("telefono"));
				usuario.setContrasena(rs.getString("contrasena"));
				usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
				usuario.setRol(rs.getString("rol"));
				usuario.setToken(rs.getString("token"));
				usuario.setFechaToken(rs.getTimestamp("fecha_token"));
				usuario.setFotoUsu(rs.getBytes("foto_usu")); // campo tipo BLOB o bytea

			}

		} catch (Exception e) {
			Util.ficheroLog("Ocurrió un error en UsuarioServicio.login: " + e.getMessage());
			e.printStackTrace();
		}

		return usuario;
	}

	private static final String REMITENTE = "bonsaissur@gmail.com";
	private static final String CONTRASENA = "msprjeksnbhekmjc"; // Contraseña de aplicación de Gmail
/**
 * Metodo encargado de configurar el metodo de enviar correos 
 * @return
 */
	private Session configurarServidorSMTP() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		// Opcionalmente fuerza TLSv1.2 si tu Java lo soporta
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(REMITENTE, CONTRASENA);
			}
		});
	}

	/**
	 * Método para enviar correo de confirmación
	 * @param correoDestinatario
	 * @param asunto
	 * @param token
	 * @throws MessagingException
	 */
	public void enviarCorreoConfirUsu(String correoDestinatario, String asunto, String token)
			throws MessagingException {
		try {
			Util.ficheroLog("Entro en enviarCorreoConfirUsu\n");

			Session session = configurarServidorSMTP(); // Asegúrate de implementar este método
			Util.ficheroLog("Despues de SMTP\n");
			MimeMessage mimeMessage = new MimeMessage(session);
			String mensaje = "https://mitienda.iloposa.eu/confirmarUsu?token=";

			mimeMessage.setFrom(new InternetAddress(REMITENTE)); // REMITENTE debe estar definido
			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestinatario));

			mimeMessage.setSubject(asunto);
			mimeMessage.setText(mensaje + token, "utf-8");

			Transport.send(mimeMessage);
			Util.ficheroLog("[Correo enviado a " + correoDestinatario + " ]\n");
		} catch (Exception e) {

			Util.ficheroLog("Ocurrió un error en enviarCorreoConfirUsu: " + e.getMessage() + "\n");

			// Traza completa del error (stack trace)
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw); // Escribe toda la traza en el PrintWriter

			// Guarda el contenido en el log
			Util.ficheroLog(sw.toString());
		}
	}

	// Mapa donde se guardan usuarios pendientes de confirmación
	private static final ConcurrentHashMap<String, Usuario> usuariosPendientes = new ConcurrentHashMap<>();
/**
 * Metodo encargado de guardar los datos del usuario 
 * @param nombre
 * @param apellidos
 * @param correo
 * @param direccion
 * @param telefono
 * @param contrasena
 * @param rol
 * @return
 */
	public String DatosRegistro(String nombre, String apellidos, String correo, String direccion, String telefono,
			String contrasena, String rol) {
		try {
			Util.ficheroLog("Entro en DatosRegistro+\n");

			Usuario usuario = new Usuario();
			usuario.setNombre(nombre);
			usuario.setApellidos(apellidos);
			usuario.setCorreo(correo);
			usuario.setDireccion(direccion);
			usuario.setTelefono(telefono);
			usuario.setContrasena(contrasena);
			usuario.setFechaRegistro(Timestamp.from(Instant.now()));
			usuario.setRol(rol);

			String token = UUID.randomUUID().toString();
			usuario.setTokenConfirmacionUsu(token);
			usuario.setActivo(false);
			Util.ficheroLog("Este es el token:" + token + "\n");
			usuariosPendientes.put(token, usuario);

			Util.ficheroLog("Se guardo el token en USUPendientes+\n");

			enviarCorreoConfirUsu(correo, "Dele a este enlace para verificar su cuenta:", token);

			Util.ficheroLog("Se envio el correo+\n");
			return "Enviado";

		} catch (Exception e) {
			// Aquí puedes poner tu log de error
			return "Ocurrio un error";
		}
	}
/**
 * Metodo encargado de confirmar la cuenta del usuario 
 * @param token
 * @return
 */
	public String confirmarCuenta(String token) {
		Util.ficheroLog("Este es el token en confirmar:" + token + "\n");
		Usuario usuario = usuariosPendientes.get(token);

		if (usuario == null) {
			Util.ficheroLog("Token inválido o ya confirmado \n");
			return "Token inválido o ya confirmado.";
		}

		usuario.setActivo(true);
		usuario.setTokenConfirmacionUsu(null);
		Util.ficheroLog("Token confirmado \n");

		// Guardar usuario en base de datos (implementa tu método POST)
		Post(usuario.getNombre(), usuario.getApellidos(), usuario.getCorreo(), usuario.getDireccion(),
				usuario.getTelefono(), usuario.getContrasena(), usuario.getRol());
		Util.ficheroLog("Despues de Metod Post \n");
		usuariosPendientes.remove(token);

		return "Cuenta activada con éxito.";
	}
/**
 * Metodo encagado de mandar el Post de enviar el registro a la base de datos 
 * @param nombre
 * @param apellidos
 * @param correo
 * @param direccion
 * @param telefono
 * @param contrasena
 * @param rol
 * @return
 */
	public String Post(String nombre, String apellidos, String correo, String direccion, String telefono,
			String contrasena, String rol) {
		Util.ficheroLog("Entre en Post \n");
		String sql = "INSERT INTO usuarios (nombre, apellidos, correo, direccion, telefono, contrasena, fecha_registro, rol) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = ConexionBD.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nombre);
			stmt.setString(2, apellidos);
			stmt.setString(3, correo);
			stmt.setString(4, direccion);
			stmt.setString(5, telefono);
			stmt.setString(6, contrasena); // puedes usar aquí Util.encriptarContrasena(contrasena) si quieres cifrar
			stmt.setTimestamp(7, Timestamp.from(Instant.now()));
			stmt.setString(8, rol);

			int filasInsertadas = stmt.executeUpdate();

			if (filasInsertadas > 0) {
				Util.ficheroLog("Registro exitoso \n");
				return "Registro exitoso";
			} else {
				Util.ficheroLog("Registro fallido \n");
				return "Registro fallido";
			}

		} catch (Exception e) {
			Util.ficheroLog("Ocurrió un error en UsuarioServicio.registrarUsuario: " + e.getMessage() + "\n");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw); // Escribe toda la traza en el PrintWriter

			// Guarda el contenido en el log
			Util.ficheroLog(sw.toString());
			return "Registro erróneo";
		}
	}
/**
 * Metodo encargado de actualizar el usuario 
 * @param nombre
 * @param apellidos
 * @param correo
 * @param direccion
 * @param telefono
 * @return
 */
	public String Actualizar(String nombre, String apellidos, String correo, String direccion, String telefono) {
		Util.ficheroLog("Entré en método PUT directo a BD\n");

		String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, direccion = ?, telefono = ? WHERE correo = ?";

		try (Connection conn = ConexionBD.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nombre);
			stmt.setString(2, apellidos);
			stmt.setString(3, direccion);
			stmt.setString(4, telefono);
			stmt.setString(5, correo); // identificador

			int filasActualizadas = stmt.executeUpdate();

			if (filasActualizadas > 0) {
				Util.ficheroLog("Usuario actualizado correctamente\n");
				return "Usuario actualizado";
			} else {
				Util.ficheroLog("No se encontró ningún usuario con ese correo para actualizar\n");
				return "Usuario no encontrado";
			}

		} catch (Exception e) {
			Util.ficheroLog("Ocurrió un error en UsuarioServicio.Put (actualizar): " + e.getMessage() + "\n");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			Util.ficheroLog(sw.toString());
			return "Error al actualizar";
		}
	}
/**
 * Metodo encagado de eliminar los usuarios 
 * @param correo
 * @return
 */
	public String Eliminar(String correo) {
		String respuesta = "";

		String sqlSelect = "SELECT rol FROM usuarios WHERE correo = ?";
		String sqlDelete = "DELETE FROM usuarios WHERE correo = ?";

		try (Connection conn = ConexionBD.getConexion();
				PreparedStatement selectStmt = conn.prepareStatement(sqlSelect);
				PreparedStatement deleteStmt = conn.prepareStatement(sqlDelete)) {

			// Verificar el rol del usuario
			selectStmt.setString(1, correo);
			ResultSet rs = selectStmt.executeQuery();

			if (rs.next()) {
				String rol = rs.getString("rol");

				if ("Administrador".equalsIgnoreCase(rol)) {
					System.out.println("No se puede eliminar un usuario con rol Administrador.");
					respuesta = "No se puede eliminar un Administrador";
				} else {
					// Eliminar usuario
					deleteStmt.setString(1, correo);
					int filas = deleteStmt.executeUpdate();

					if (filas > 0) {
						System.out.println("Usuario eliminado correctamente.");
						respuesta = "Usuario Eliminado";
					} else {
						System.out.println("No se pudo eliminar el usuario.");
						respuesta = "Error al eliminar";
					}
				}
			} else {
				System.out.println("Usuario no encontrado.");
				respuesta = "Usuario no encontrado";
			}

		} catch (Exception e) {
			System.out.println("Error en Eliminar: " + e.getMessage());
			Util.ficheroLog("Error en Eliminar: " + e.getMessage());
			respuesta = "Error al eliminar";
		}

		return respuesta;
	}
/**
 * Metodo encargado de recuperar la contraseña del usuario 
 * @param correoDestinatario
 * @return
 */
	public String recuperarContrasena(String correoDestinatario) {
		String resp = "";
		Util.ficheroLog("Correo  para recuperación: [" + correoDestinatario + "]\n");
		try (Connection conn = ConexionBD.getConexion()) {

			String sqlSelect = "SELECT * FROM usuarios WHERE correo = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
				String correoLimpio = correoDestinatario.trim().toLowerCase();
				Util.ficheroLog("Correo recibido para recuperación: [" + correoLimpio + "]\n");
				stmt.setString(1, correoLimpio);

				ResultSet rs = stmt.executeQuery();
				Util.ficheroLog("Antes de if \n");
				if (rs.next()) {
					Util.ficheroLog("Dentro  de if \\n");
					Usuario usuario = new Usuario();
					usuario.setId(rs.getLong("id_usuario"));
					usuario.setCorreo(rs.getString("correo"));
					usuario.setNombre(rs.getString("nombre"));

					// Generar token y fecha
					String token = UUID.randomUUID().toString();
					Timestamp fechaToken = new Timestamp(System.currentTimeMillis() + 3600000); // 1 hora

					// Guardar token y fecha en BD
					String sqlUpdate = "UPDATE usuarios SET token = ?, fecha_token = ? WHERE correo = ?";
					try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
						updateStmt.setString(1, token);
						updateStmt.setTimestamp(2, fechaToken);
						updateStmt.setString(3, correoDestinatario);

						int filas = updateStmt.executeUpdate();
						if (filas > 0) {
							Util.ficheroLog("Token de recuperación generado para: " + correoDestinatario);
							enviarCorreoToken(correoDestinatario, "Recuperación de contraseña", token);
							resp = "Correo enviado";
						} else {
							Util.ficheroLog("No se pudo actualizar el token para: " + correoDestinatario);
							resp = "Error al actualizar usuario";
						}
					}

				} else {
					Util.ficheroLog("No se encontró el usuario con correo: " + correoDestinatario);
					resp = "Usuario no encontrado";
				}
			}

		} catch (Exception e) {
			Util.ficheroLog("Error en recuperarContrasena: " + e.getMessage());
			resp = "Error general";
		}

		return resp;
	}
/**
 * Metodo encargado de actualizar la contraseña del usuario 
 * @param contrasena
 * @param token
 * @return
 */
	public String actualizarContrasena(String contrasena, String token) {
		String resp = "";

		try (Connection conn = ConexionBD.getConexion()) {
			String sqlSelect = "SELECT * FROM usuarios WHERE token = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
				stmt.setString(1, token);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					Timestamp fechaToken = rs.getTimestamp("fecha_token");

					if (fechaToken != null && fechaToken.toLocalDateTime().toLocalDate().isEqual(LocalDate.now())) {

						// El token es válido
						String nuevaContrasena = Util.encriptarContrasena(contrasena);
						String sqlUpdate = "UPDATE usuarios SET contrasena = ?, token = NULL, fecha_token = NULL WHERE token = ?";
						try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
							updateStmt.setString(1, nuevaContrasena);
							updateStmt.setString(2, token);

							int filas = updateStmt.executeUpdate();
							if (filas > 0) {
								Util.ficheroLog("Contraseña actualizada correctamente");
								resp = "Usuario actualizado";
							} else {
								resp = "Error al actualizar contraseña";
							}
						}
					} else {
						Util.ficheroLog("Token expirado o inválido");
						resp = "Token expirado";
					}

				} else {
					Util.ficheroLog("No se encontró ningún usuario con el token proporcionado");
					resp = "Usuario no encontrado";
				}
			}

		} catch (Exception e) {
			Util.ficheroLog("Error en actualizarContrasena: " + e.getMessage());
			resp = "Error general";
		}

		return resp;
	}
/**
 * Metodo encagado de enviar el token de la contraseña por correo
 * @param correoDestinatario
 * @param asunto
 * @param token
 */
	public void enviarCorreoToken(String correoDestinatario, String asunto, String token) {
	    try {
	        Util.ficheroLog("Entró en enviarCorreoToken\n");

	        Session session = configurarServidorSMTP();
	        Util.ficheroLog("Sesión SMTP configurada correctamente\n");

	        MimeMessage mimeMessage = new MimeMessage(session);

	        String enlace = "https://mitienda.iloposa.eu/nuevaContrasena.jsp?token=" + token;

	        mimeMessage.setFrom(new InternetAddress(REMITENTE));
	        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestinatario));
	        mimeMessage.setSubject(asunto);
	        mimeMessage.setText(enlace, "utf-8");

	        Transport.send(mimeMessage);

	        Util.ficheroLog("[Correo de recuperación enviado a " + correoDestinatario + "]\n");

	    } catch (Exception e) {
	        Util.ficheroLog("Ocurrió un error en enviarCorreoToken: " + e.getMessage() + "\n");

	        // Guardar stack trace completo en el log
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        Util.ficheroLog(sw.toString());
	    }
	}


}
