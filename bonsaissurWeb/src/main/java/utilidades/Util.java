package utilidades;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * Clase que contiene los metodos de utilidades
 */
public class Util {
	/**
	 * Metodo encargado de encriptar las contraseñas
	 * @param password
	 * @return
	 */
	public static String encriptarContrasena(String password) {
		try {
			// Creamos una instancia de MessageDigest con el algoritmo SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Convertimos la contraseña a bytes y generamos el hash
			byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

			// Convertimos los bytes a una cadena hexadecimal
			StringBuilder hexString = new StringBuilder();
			for (byte b : encodedhash) {
				String hex = String.format("%02x", b);
				hexString.append(hex);
			}

			// Retornamos el hash en formato de String
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			ficheroLog("Ocurrio un error en encriptarcontraseña");
			return password;
		}
	}
/**
 * Metodo encargado de crear el nombre del fichero log
 * @return
 */
	public static String creacionNombreFichero() {
		String fecha;
		/// Para poner un formato a una fecha con DateTime
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("ddMMyy");
		LocalDate fechaActual = LocalDate.now();
		fecha = fechaActual.format(formato);
		return fecha;
	}

	/**
	 * Metodo encargado del fichero log
	 * 
	 * @param mensaje
	 * @param usu
	 */
	public static void ficheroLog(String mensaje) {
	    try {
	        String rutaDirectorio = "/opt/tomcat/webapps/ROOT/logs";
	        File dir = new File(rutaDirectorio);

	        if (!dir.exists()) {
	            if (dir.mkdirs()) {
	                System.out.println("Directorio creado: " + rutaDirectorio);
	            } else {
	                System.out.println("No se pudo crear el directorio: " + rutaDirectorio);
	            }
	        }

	        String rutaCompletaLog = rutaDirectorio + File.separator + "log-" + creacionNombreFichero() + ".txt";

	        try (BufferedWriter escribe = new BufferedWriter(new FileWriter(rutaCompletaLog, true))) {
	            escribe.write(mensaje);
	            escribe.newLine();
	        }

	    } catch (IOException e) {
	        System.out.println("Hubo un error en el fichero log  Error:001");
	        e.printStackTrace();
	    }
	}

}
