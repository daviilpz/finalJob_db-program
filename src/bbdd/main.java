package bbdd;

import java.sql.SQLException;

public class main {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		ConexionMySQL connect = new ConexionMySQL("root", "", "juego");
		connect.conectar();
		String txt = "CREATE TABLE Estudiantes (   ID INT PRIMARY KEY,   Nombre VARCHAR(50) NOT NULL,   Apellido VARCHAR(50) NOT NULL,   Edad INT,   CorreoElectronico VARCHAR(100));";
		connect.ejecutarInsertDeleteUpdate(txt);
	}

}
