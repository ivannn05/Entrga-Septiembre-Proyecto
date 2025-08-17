package dtos;


import java.sql.Timestamp;
/**
 * Clase encagada de los datos de los usuarios 
 */
public class Usuario {

 
	private Long id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String direccion;
    private String telefono;
    private String contrasena;
    private Timestamp fechaRegistro;
    private String rol;
    private String token;
    private Timestamp fechaToken;
    private byte[] fotoUsu;
    private boolean activo;
    private String tokenConfirmacionUsu;

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String apellidos, String correo, String telefono, String direccion,
                   String contrasena, Timestamp fechaRegistro, String rol, String token, Timestamp fechaToken,
                   byte[] fotoUsu, boolean activo, String tokenConfirmacionUsu) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.contrasena = contrasena;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
        this.token = token;
        this.fechaToken = fechaToken;
        this.fotoUsu = fotoUsu;
        this.activo = activo;
        this.tokenConfirmacionUsu = tokenConfirmacionUsu;
    }

    // Getters y setters 
    public Long getId() {
 		return id;
 	}

 	public void setId(Long id) {
 		this.id = id;
 	}

 	public String getNombre() {
 		return nombre;
 	}

 	public void setNombre(String nombre) {
 		this.nombre = nombre;
 	}

 	public String getApellidos() {
 		return apellidos;
 	}

 	public void setApellidos(String apellidos) {
 		this.apellidos = apellidos;
 	}

 	public String getCorreo() {
 		return correo;
 	}

 	public void setCorreo(String correo) {
 		this.correo = correo;
 	}

 	public String getDireccion() {
 		return direccion;
 	}

 	public void setDireccion(String direccion) {
 		this.direccion = direccion;
 	}

 	public String getTelefono() {
 		return telefono;
 	}

 	public void setTelefono(String telefono) {
 		this.telefono = telefono;
 	}

 	public String getContrasena() {
 		return contrasena;
 	}

 	public void setContrasena(String contrasena) {
 		this.contrasena = contrasena;
 	}

 	public Timestamp getFechaRegistro() {
 		return fechaRegistro;
 	}

 	public void setFechaRegistro(Timestamp fechaRegistro) {
 		this.fechaRegistro = fechaRegistro;
 	}

 	public String getRol() {
 		return rol;
 	}

 	public void setRol(String rol) {
 		this.rol = rol;
 	}

 	public String getToken() {
 		return token;
 	}

 	public void setToken(String token) {
 		this.token = token;
 	}

 	public Timestamp getFechaToken() {
 		return fechaToken;
 	}

 	public void setFechaToken(Timestamp fechaToken) {
 		this.fechaToken = fechaToken;
 	}

 	public byte[] getFotoUsu() {
 		return fotoUsu;
 	}

 	public void setFotoUsu(byte[] fotoUsu) {
 		this.fotoUsu = fotoUsu;
 	}

 	public boolean isActivo() {
 		return activo;
 	}

 	public void setActivo(boolean activo) {
 		this.activo = activo;
 	}

 	public String getTokenConfirmacionUsu() {
 		return tokenConfirmacionUsu;
 	}

 	public void setTokenConfirmacionUsu(String tokenConfirmacionUsu) {
 		this.tokenConfirmacionUsu = tokenConfirmacionUsu;
 	}

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correo='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", rol='" + rol + '\'' +
                ", token='" + token + '\'' +
                ", fechaToken=" + fechaToken +
                ", fotoUsu=" + (fotoUsu != null ? fotoUsu.length : 0) + " bytes" +
                ", activo=" + activo +
                ", tokenConfirmacionUsu='" + tokenConfirmacionUsu + '\'' +
                '}';
    }

    // ... (resto de getters y setters)
}