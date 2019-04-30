package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import entities.Profesional;

public class DaoProfesional {
	public List<Profesional> getAll() throws ErrorDeConexionException, NoExisteException{
		List<Profesional> ret = new ArrayList<Profesional>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Profesional");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				long puntuacionAcum = rs.getLong("puntuacionAcum");
				int cantPuntuaciones = rs.getInt("cantPuntuaciones");
				double puntuacion = cantPuntuaciones == 0 ? 0 : (puntuacionAcum / ((double)cantPuntuaciones));
				ret.add(new Profesional(rs.getInt("id"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("titulo"),rs.getString("especialidad"),puntuacion,rs.getString("foto"),new DaoCentroMedico().getById(rs.getInt("idCentroMedico"))));
			}
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todos los profesionales", e);
		}

	}
	
	public List<String> getAllEspecialidades() throws ErrorDeConexionException{
		List<String> ret = new ArrayList<String>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Especialidad");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ret.add(rs.getString("nombre"));
			}
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todas las especialidades", e);
		}

	}
	
	public List<Profesional> getAllByEspecialidad(String especialidad) throws ErrorDeConexionException, NoExisteException{
		List<Profesional> ret = new ArrayList<Profesional>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Profesional WHERE especialidad = ?");
			ps.setString(1, especialidad);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				long puntuacionAcum = rs.getLong("puntuacionAcum");
				int cantPuntuaciones = rs.getInt("cantPuntuaciones");
				double puntuacion = cantPuntuaciones == 0 ? 0 : (puntuacionAcum / ((double)cantPuntuaciones));
				ret.add(new Profesional(rs.getInt("id"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("titulo"),rs.getString("especialidad"),puntuacion,rs.getString("foto"),new DaoCentroMedico().getById(rs.getInt("idCentroMedico"))));
			}
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todos los profesionales", e);
		}

	}
	public Profesional getByNombreApellido(String nombre, String apellido) throws ErrorDeConexionException, NoExisteException {
		Profesional ret = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Profesional WHERE nombre = ? AND apellido = ?");
			ps.setString(1, nombre);
			ps.setString(2, apellido);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				long puntuacionAcum = rs.getLong("puntuacionAcum");
				int cantPuntuaciones = rs.getInt("cantPuntuaciones");
				double puntuacion = cantPuntuaciones == 0 ? 0 : (puntuacionAcum / ((double)cantPuntuaciones));
				ret = new Profesional(rs.getInt("id"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("titulo"),rs.getString("especialidad"),puntuacion,rs.getString("foto"),new DaoCentroMedico().getById(rs.getInt("idCentroMedico")));
			}
			ps.close();
			conn.close();
			if(ret == null) {
				throw new NoExisteException("No existe un Centro Médico con el ID ingresado");
			}
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todos los profesionales", e);
		}
	}
	public double setNewScore(String nombre, String apellido, double puntaje) throws ErrorDeConexionException, NoExisteException {
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE Profesional SET puntuacionAcum = puntuacionAcum + ?, cantPuntuaciones = cantPuntuaciones + 1 WHERE nombre = ? AND apellido = ?");
			ps.setDouble(1, puntaje);
			ps.setString(2, nombre);
			ps.setString(3, apellido);
			
			int affected = ps.executeUpdate();
			ps.close();
			if(affected == 0)
			{
				conn.close();
				return -1;
			}
			Profesional p = getByNombreApellido(nombre, apellido);
			conn.close();
			return p.puntuacion;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todos los profesionales", e);
		}
	}
	public Profesional getById(int id) throws ErrorDeConexionException, NoExisteException {
		Profesional ret = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Profesional WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				long puntuacionAcum = rs.getLong("puntuacionAcum");
				int cantPuntuaciones = rs.getInt("cantPuntuaciones");
				double puntuacion = cantPuntuaciones == 0 ? 0 : (puntuacionAcum / ((double)cantPuntuaciones));
				ret = new Profesional(rs.getInt("id"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("titulo"),rs.getString("especialidad"),puntuacion,rs.getString("foto"),new DaoCentroMedico().getById(rs.getInt("idCentroMedico")));
			}
			ps.close();
			conn.close();
			if(ret == null) {
				throw new NoExisteException("No existe un profesional con el ID ingresado");
			}
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todos los profesionales", e);
		}
	}
}
