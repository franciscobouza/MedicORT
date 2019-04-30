package entities;

public class Usuario {
	private int id;
	public String nombre;
	public String apellido;
	public String email;
	public String password;
	public String documento;
	public String telefono;
	public boolean activo;
	
	public Usuario(String nombre, String apellido, String email, String password, String documento, String telefono, boolean activo) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.documento = documento;
		this.telefono = telefono;
		this.activo = activo;
	}
	
	public Usuario(int id, String nombre, String apellido, String email, String password, String documento, String telefono, boolean activo) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.password = password;
		this.documento = documento;
		this.telefono = telefono;
		this.activo = activo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
