package utilidades;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
/**
 * Clase encargada de la conexion con la base de datos 
 */
public class ConexionBD {

    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try {
            // Cargar archivo properties desde el classpath
            Properties props = new Properties();
            InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties");

            if (input == null) {
                throw new RuntimeException("No se encontró el archivo db.properties en el classpath");
            }

            props.load(input);

            // Asignar valores de conexión
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driver = props.getProperty("db.driver");

            // Cargar el driver JDBC
            Class.forName(driver);

            System.out.println("[DEBUG] db.properties cargado correctamente");
            System.out.println("[DEBUG] URL: " + url);

        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo cargar la configuración de la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConexion() throws Exception {
        return DriverManager.getConnection(url, username, password);
    }
}
