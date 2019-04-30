package daos;

import java.sql.Connection;
import java.sql.DriverManager;

import daos.exceptions.ErrorDeConexionException;

public class ConnectionsPool {

	public Connection getConnection() throws ErrorDeConexionException{
		try{
			String url = "jdbc:mysql://localhost:3306/ort_tt_2018t2";
			Class.forName ("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(url,"root","tribus1234");
			return conn;
		}catch(Exception ex){
			throw new ErrorDeConexionException("Se produjo un error al tratar de crear la conexión con la base de datos", ex);
		}
	}
	
}
