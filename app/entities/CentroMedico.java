package entities;

public class CentroMedico {

	private int id;
	public String nombre;
	public String direccion;
	public Localizacion localizacion;
	public CentroMedico(String nombre, String direccion, Localizacion localizacion) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.localizacion = localizacion;
	}
	
	public CentroMedico(int id, String nombre, String direccion, Localizacion localizacion) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.localizacion = localizacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
