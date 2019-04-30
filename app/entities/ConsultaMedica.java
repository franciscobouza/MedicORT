package entities;

import java.util.Date;

public class ConsultaMedica {
	private int id;
	public Date fechaHora;
	public Usuario usuario;
	public Profesional profesional;
	public CentroMedico centroMedico;

	public ConsultaMedica(Date fechaHora, Usuario usuario, Profesional profesional,
			CentroMedico centroMedico) {
		this.fechaHora = fechaHora;
		this.usuario = usuario;
		this.profesional = profesional;
		this.centroMedico = centroMedico;
	}
	public ConsultaMedica(int id, Date fechaHora, Usuario usuario, Profesional profesional,
			CentroMedico centroMedico) {
		this.id = id;
		this.fechaHora = fechaHora;
		this.usuario = usuario;
		this.profesional = profesional;
		this.centroMedico = centroMedico;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	

}
