package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import entities.CentroMedico;
import entities.Localizacion;

public class DaoCentroMedico {
	
	public List<CentroMedico> getAll() throws ErrorDeConexionException{
		List<CentroMedico> ret = new ArrayList<CentroMedico>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Centro_Medico");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ret.add(new CentroMedico(rs.getInt("id"), rs.getString("nombre"),rs.getString("direccion"),new Localizacion(rs.getDouble("latitud"),rs.getDouble("longitud"))));
			}
			ps.close();
			conn.close();
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener todos los centros médicos", e);
		}

	}

	public CentroMedico getById(int id) throws ErrorDeConexionException, NoExisteException{
		CentroMedico ret = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Centro_Medico WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				ret = new CentroMedico(rs.getInt("id"), rs.getString("nombre"),rs.getString("direccion"),new Localizacion(rs.getDouble("latitud"),rs.getDouble("longitud")));
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
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener el céntro médico con id = '"+id+"'", e);
		}

	}

	public CentroMedico getByNombre(String nombre) throws ErrorDeConexionException, NoExisteException {
		CentroMedico ret = null;
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Centro_Medico WHERE nombre = ?");
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				ret = new CentroMedico(rs.getInt("id"), rs.getString("nombre"),rs.getString("direccion"),new Localizacion(rs.getDouble("latitud"),rs.getDouble("longitud")));
			}
			ps.close();
			conn.close();
			if(ret == null) {
				throw new NoExisteException("No existe un céntro médico con nombre = '"+nombre+"'");
			}
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener el céntro médico con nombre = '"+nombre+"'", e);
		}
	}
}
