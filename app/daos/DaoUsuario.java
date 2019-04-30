package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import daos.exceptions.YaExisteException;
import entities.Usuario;

public class DaoUsuario {
	public int addUsuario(String nombre, String apellido, String email, String password, String documento, String telefono) throws ErrorDeConexionException, YaExisteException
	{
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps;
			
			ps = conn.prepareStatement("SELECT 1 FROM Usuario WHERE UPPER(email) = ?");
			ps.setString(1,email.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			ps.close();			
			if(existe)
			{
				conn.close();
				throw new YaExisteException("Ya existe un usuario con el email = '"+email+"'");
			}

			
			ps = conn.prepareStatement("SELECT 1 FROM Usuario WHERE UPPER(telefono) = ?");
			ps.setString(1,telefono.toUpperCase());
			rs = ps.executeQuery();
			existe = rs.next();
			ps.close();			
			if(existe)
			{
				conn.close();
				throw new YaExisteException("Ya existe un usuario con el telefono = '"+telefono+"'");
			}
			
			ps = conn.prepareStatement("INSERT INTO Usuario (nombre, apellido, email, password, documento, telefono) values (?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1,nombre);
			ps.setString(2,apellido);
			ps.setString(3,email);
			ps.setString(4,password);
			ps.setString(5,documento);
			ps.setString(6,telefono);
			int res = ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			rs.next();
			int idUsuario = rs.getInt(1);
			ps.close();
			conn.close();
			if(res == 0)
				throw new ErrorDeConexionException("Se produjo un error al tratar de agregar un Usuario. No se modificó a la BD.");
			else
				return idUsuario;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de registrar un usuario con email = '"+email+"'", e);
		}
	}
	
	public int attemptToLogin(String email, String password) throws ErrorDeConexionException{
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT id FROM Usuario WHERE UPPER(email) = ? and UPPER(password) = ?");
			ps.setString(1,email.toUpperCase());
			ps.setString(2,password.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			int idUsuario = -1;
			if(existe)
				idUsuario = rs.getInt(1);
			conn.close();
			return idUsuario;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de realizar un login con email = '"+email+"'", e);
		}
	}

	public Usuario getUsuario(String email) throws NoExisteException, ErrorDeConexionException {
		Connection conn = new ConnectionsPool().getConnection();
		try {
			Usuario ret = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Usuario WHERE UPPER(email) = ?");
			ps.setString(1,email.toUpperCase());
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			if(existe)
				ret = new Usuario(rs.getInt("id"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("email"),"*****",rs.getString("documento"),rs.getString("telefono"),rs.getBoolean("activo"));
			else {
				conn.close();
				throw new NoExisteException("No existe un usuario de email = '"+email+"'");
			}
			conn.close();
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de realizar un login con email = '"+email+"'", e);
		}
	}

	public Usuario getById(int id) throws ErrorDeConexionException, NoExisteException {
		Connection conn = new ConnectionsPool().getConnection();
		try {
			Usuario ret = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Usuario WHERE id = ?");
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			boolean existe = rs.next();
			if(existe)
				ret = new Usuario(rs.getInt("id"), rs.getString("nombre"),rs.getString("apellido"),rs.getString("email"),"*****",rs.getString("documento"),rs.getString("telefono"),rs.getBoolean("activo"));
			else {
				conn.close();
				throw new NoExisteException("No existe un usuario de id = '"+id+"'");
			}
			conn.close();
			return ret;
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener al usuario de id = '"+id+"'", e);
		}
	}
}
