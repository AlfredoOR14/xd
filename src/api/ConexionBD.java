package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
        
         public ConexionBD(){
    
}
    public static Connection getConnection() throws SQLException{
        Connection cn = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String urlDB = "jdbc:mysql://localhost:3306/alfredo";
        String user = "root";
        String pass = "";
        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
            cn = DriverManager.getConnection(urlDB,user,pass);
            System.out.println("Se conecto");
        } catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw new SQLException("No se encontró el driver de la base de datos.");
		} catch (Exception e) {
			throw new SQLException("No se puede establecer la conexión con la BD.");
		}
        return cn;
    }
    
    
    public static void main(String[] args) throws SQLException {
        
     ConexionBD.getConnection();
                     
    }
}


