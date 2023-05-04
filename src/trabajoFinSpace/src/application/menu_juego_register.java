package application;

import javax.swing.*;

import com.db4o.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.Dialog.ModalityType;

public class menu_juego_register extends JFrame {


	public menu_juego_register() {
		setResizable(false);
		setTitle("Crear una nueva cuenta");
		setBounds(100, 100, 700, 550);
		setLocationRelativeTo(null);		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnEnviar.setBounds(158, 371, 106, 37);
		getContentPane().add(btnEnviar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCancelar.setBounds(330, 371, 106, 37);
		getContentPane().add(btnCancelar);
		
		campousuario = new JTextField();
		campousuario.setBounds(362, 127, 200, 30);
		getContentPane().add(campousuario);
		campousuario.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Introduzca su nombre de usuario");
		lblNewLabel.setForeground(new Color(255, 128, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(54, 125, 219, 30);
		getContentPane().add(lblNewLabel);
		
		JLabel lblIntroduzcaSuContrasea = new JLabel("Introduzca su contraseña");
		lblIntroduzcaSuContrasea.setForeground(new Color(255, 128, 0));
		lblIntroduzcaSuContrasea.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblIntroduzcaSuContrasea.setBounds(54, 204, 219, 30);
		getContentPane().add(lblIntroduzcaSuContrasea);
		
		campopass = new JPasswordField();
		campopass.setBounds(362, 206, 200, 30);
		getContentPane().add(campopass);
		
		JLabel lblConfirmeSuContrasea = new JLabel("Confirme su contraseña");
		lblConfirmeSuContrasea.setForeground(new Color(255, 128, 0));
		lblConfirmeSuContrasea.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblConfirmeSuContrasea.setBounds(54, 277, 219, 30);
		getContentPane().add(lblConfirmeSuContrasea);
		
		campoconfirmarpass = new JPasswordField();
		campoconfirmarpass.setBounds(362, 279, 200, 30);
		getContentPane().add(campoconfirmarpass);
		
		JLabel lblIntroduzcaSuNombre = new JLabel("Introduzca su nombre");
		lblIntroduzcaSuNombre.setForeground(new Color(255, 128, 0));
		lblIntroduzcaSuNombre.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblIntroduzcaSuNombre.setBounds(54, 58, 219, 30);
		getContentPane().add(lblIntroduzcaSuNombre);
		
		camponombre = new JTextField();
		camponombre.setColumns(10);
		camponombre.setBounds(362, 60, 200, 30);
		getContentPane().add(camponombre);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(0, 0, 700, 550);
		lblNewLabel_1.setIcon(new ImageIcon(new ImageIcon("src/imagenes/image.jpg").getImage().getScaledInstance(700, 550, getDefaultCloseOperation())));
		getContentPane().add(lblNewLabel_1);
		
		
// Llamada a la Acción del Botón Registrar
		btnEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					registerUser();
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
			}
		});
		
		
// Llamada a la acción del botón Cancelar
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		setVisible(true);
	}

// Capturamos los datos del formulario
	private void registerUser() throws SQLException {
		String nombre = camponombre.getText();
		String usuario = campousuario.getText();
		String pass = String.valueOf(campopass.getPassword());
		String confirmarpass = String.valueOf(campoconfirmarpass.getPassword());

		
// Comprobamos que estén todos rellenos
		if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty() || confirmarpass.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Por favor introduzca todos los campos", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

// Comprobamos que la contraseña es igual en ambos campos
		if (!pass.equals(confirmarpass)) {
			JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden ", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	
	
		// Pasar los datos recibidos, en la primera condición 
		//si el usuario se registra correctamente se le informará al usuario, 
		//en caso de error, informará al usuario que vuelva a intentarlo.
		
		//NOTA: PARA QUE SALTE EL MENSAJE DE ERROR EN CASO DE NO PODER REGISTRARSE
		//EL USER, DEBEMOS PROBAR EN ESTAS LÍNEAS UN MÉTODO USANDO UN TRY/CATCH.
		user = addUserToDatabase(nombre, usuario, pass);
		if (user != null) {
			JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "ERROR al insertar el Usuario", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	
// Creamos user del tipo User que declaramos a continuación
	public User user;
	private JTextField campousuario;
	private JPasswordField campopass;
	private JPasswordField campoconfirmarpass;
	private JTextField camponombre;

	public User addUserToDatabase(String nombre, String usuario, String contrasena) throws SQLException {
		User user = null;
		// Conectando a Database. Preparamos el INSERT
		final String DB_URL = "jdbc:mysql://localhost/registro?serverTimezoneUTC";
		final String USERNAME = "root";
		final String PASS = "";
		Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO usuarios (nombre, usuario, contrasena)" + " VALUES (?,?,?)";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, nombre);
		preparedStatement.setString(2, usuario);
		preparedStatement.setString(3, contrasena);
		// Insertamos fila dentro de la tabla
		int addedRows = preparedStatement.executeUpdate();
		if (addedRows > 0) {
			user = new User();
			user.name = nombre;
			user.password = contrasena;
		}
		// Cerramos conexión
		stmt.close();
		conn.close();
		return user;
	}
}
