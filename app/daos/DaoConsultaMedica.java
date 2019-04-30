package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import daos.exceptions.ErrorDeConexionException;
import daos.exceptions.NoExisteException;
import entities.ConsultaMedica;

public class DaoConsultaMedica {
	
	public List<ConsultaMedica> getAllByEmail(String email) throws ErrorDeConexionException, NoExisteException{
		List<ConsultaMedica> ret = new ArrayList<ConsultaMedica>();
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT cm.* FROM Consulta_Medica cm, Usuario u where cm.idUsuario = u.id && u.email = ?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ret.add(new ConsultaMedica(rs.getInt("id"), rs.getTimestamp("fechaHora"), new DaoUsuario().getById(rs.getInt("idUsuario")), new DaoProfesional().getById(rs.getInt("idProfesional")), new DaoCentroMedico().getById(rs.getInt("idCentroMedico"))));
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
	
//	public CentroMedico getById(int id) throws ErrorDeConexionException, NoExisteException{
//		CentroMedico ret = null;
//		Connection conn = new ConnectionsPool().getConnection();
//		try {
//			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Centro_Medico WHERE id = ?");
//			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery();
//			if(rs.next()) {
//				ret = new CentroMedico(rs.getInt("id"), rs.getString("nombre"),rs.getString("direccion"),new Localizacion(rs.getDouble("latitud"),rs.getDouble("longitud")));
//			}
//			ps.close();
//			conn.close();
//			if(ret == null) {
//				throw new NoExisteException("No existe un Centro Médico con el ID ingresado");
//			}
//			return ret;
//		} catch (SQLException e) {
//			try{
//				conn.close();
//			}catch(Exception ex){}
//			throw new ErrorDeConexionException("Se produjo un error al tratar de obtener el céntro médico con id = '"+id+"'", e);
//		}
//
//	}

	public void addNewConsultaMedica(ConsultaMedica consultaMedica) throws ErrorDeConexionException {
		Connection conn = new ConnectionsPool().getConnection();
		try {
			PreparedStatement ps;
			
			ps = conn.prepareStatement("INSERT INTO consulta_medica (fechaHora, idUsuario, idProfesional, idCentroMedico) values (?,?,?,?)");
			ps.setTimestamp(1,new java.sql.Timestamp(consultaMedica.fechaHora.getTime()));
			ps.setInt(2,consultaMedica.usuario.getId());
			ps.setInt(3,consultaMedica.profesional.getId());
			ps.setInt(4,consultaMedica.centroMedico.getId());
			int res = ps.executeUpdate();
			ps.close();
			conn.close();
			if(res == 0)
				throw new ErrorDeConexionException("Se produjo un error al tratar de fijar una consulta médica.");
		} catch (SQLException e) {
			try{
				conn.close();
			}catch(Exception ex){}
			throw new ErrorDeConexionException("Se produjo un error al tratar de fijar una consulta médica.", e);
		}
	}
}
