package entities;

public class Profesional {
	private int id;
	public String nombre;
	public String apellido;
	public String titulo;
	public String especialidad;
	public double puntuacion;
	public String foto;
	public CentroMedico centroMedico;
	
	public Profesional(String nombre, String apellido, String titulo, String especialidad, double puntuacion,
			String foto, CentroMedico centroMedico) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.titulo = titulo;
		this.especialidad = especialidad;
		this.puntuacion = puntuacion;
		this.foto = foto;
		this.centroMedico = centroMedico;
	}
	
	public Profesional(int id, String nombre, String apellido, String titulo, String especialidad, double puntuacion,
			String foto, CentroMedico centroMedico) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.titulo = titulo;
		this.especialidad = especialidad;
		this.puntuacion = puntuacion;
		this.foto = foto;
		this.centroMedico = centroMedico;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
