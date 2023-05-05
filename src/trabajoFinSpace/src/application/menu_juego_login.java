package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.db4o.User;




public class menu_juego_login extends JFrame {

	private JPanel contentPane;
	private JTextField tfUsuario;
	private JPasswordField pfPassword;
	public static User usuarioFinal = new User();
	public static ConexionMySQL conection = new ConexionMySQL("usuario", "pass", "bd");
	public static int scoreFinal;
	/**
	 * Launch the application.
	 */
	public class main {
	    public static void main(String[] args) {
	        menu_juego_login login_usuarios = new menu_juego_login();
	        User user = login_usuarios.user;
	        usuarioFinal = user;
	        
	        

	        // System.out.println(user.nombre);

	        if (user != null) {
	            System.out.println("Autenticado correctamente con el usuario: " + user.name);

	        } else {
	            System.out.println("Autenticación fallida");
	        }

	    }
	}
	
	public User user;
    private User getAuthenticatedUser(String usuario, String password){
        User user = null;


        // Conectando a Database. Preparamos el INSERT
        final String DB_URL = "jdbc:mysql://localhost/registro?serverTimezoneUTC";
        final String USERNAME = "root";
        final String PASS = "";


        try {
            // Abrimos conexión
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
            Statement stmt = conn.createStatement();
            System.out.printf("Conexión establecida \n");

            // Preparamos la consulta
            String sql = "SELECT * FROM usuarios WHERE usuario=? AND contrasena=?";

            // Pasamos los datos a la consulta
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,usuario);
            preparedStatement.setString(2, password);
            System.out.printf("Consulta creada \n");

            // Ejecutamos la consulta
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.name = resultSet.getString("Usuario");
                user.password = resultSet.getString("Contrasena");

                System.out.printf("Consulta realizada con éxito \n");
            }
            else {
                System.out.println("ERROR");
            }

            stmt.close();
            System.out.println("Consulta cerrada con éxito \n");
            conn.close();
            System.out.printf("Conexión cerrada con éxito \n");

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }
    //Recibimos la puntuacion final
    public static void recibirscore(int score) 
    {
    	 scoreFinal = score;
   
    }
    
    public static void GuardarScoreBBDD() 
    {	// Conectando a Database. Preparamos el INSERT
        final String DB_URL = "jdbc:mysql://localhost/registro?serverTimezoneUTC";
        final String USERNAME = "root";
        final String PASS = "";

        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
        Statement stmt = conn.createStatement();
        String sql = "INSERT INTO Ranking (usuario, puntuacion)" +
                " VALUES (?,?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, usuarioFinal.name);
        preparedStatement.setInt(2, scoreFinal);

        // Insertamos fila dentro de la tabla
        int addedRows = preparedStatement.executeUpdate();
        if (addedRows > 0) {
        	user = new User();
			user.name = nombre;
			user.password = contrasena;
        }
    	/*final String DB_URL = "jdbc:mysql://localhost/registro?serverTimezoneUTC";
		final String USERNAME = "root";
		final String PASS = "";
		
	//	Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
//		Statement stmt = conn.createStatement();

    	// Creamos la consulta SQL con los parámetros como interrogantes (?)
		String sql = "INSERT INTO usuarios (usuario, puntuacion)" + " VALUES (?,?)";
		
        // Creamos un objeto PreparedStatement para preparar la consulta y asignar los valores de los parámetros
        PreparedStatement statement = conection.ejecutarInsertDeleteUpdate(sql);
		//PreparedStatement statement = conn.prepareStatement(sql);

        statement.setString(1, usuarioFinal.name);
        statement.setInt(2, scoreFinal);
    	try {
			conection.ejecutarInsertDeleteUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    

	/**
	 * Create the frame.
	 */
	public menu_juego_login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 550);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(128, 128, 192));
		contentPane.setBorder(new TitledBorder(null, "Iniciar sesi\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(41, 164, 88, 19);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblNewLabel);
		
		tfUsuario = new JTextField();
		tfUsuario.setBounds(169, 163, 150, 26);
		contentPane.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(41, 227, 118, 26);
		contentPane.add(lblNewLabel_1);
		
		pfPassword = new JPasswordField();
		pfPassword.setBounds(169, 230, 150, 26);
		contentPane.add(pfPassword);
		
		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(106, 295, 133, 26);
		
		btnNewButton.addActionListener(new ActionListener() {
			@Override
		public void actionPerformed(ActionEvent e) {
			
			
			 String usuario = tfUsuario.getText();
             String password = String.valueOf(pfPassword.getPassword());

            user = getAuthenticatedUser(usuario, password);

             if (user != null) {
                 dispose();
                 System.out.println("Autenticado correctamente con el usuario: " + user.name);}
             else {
                 JOptionPane.showMessageDialog(menu_juego_login.this,
                         "Usuario o Contraseña incorrecto",
                         "Prueba de nuevo",
                         JOptionPane.ERROR_MESSAGE);

             }

         }
     });
		
		contentPane.add(btnNewButton);
		
		
		JButton btnNewButton_1 = new JButton("Registrate");
		btnNewButton_1.setBounds(388, 473, 123, 21);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				menu_juego_register a = new menu_juego_register();
				a.setVisible(true);
				
			}
		}
				);
		
		JButton btnNewButton_2 = new JButton("Cancelar");
		btnNewButton_2.setBounds(106, 345, 133, 26);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton_2.setBounds(262, 295, 133, 26);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_2 = new JLabel("¿Todavía no estás registrado? Clic en el botón ->");
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(14, 472, 386, 19);
		contentPane.add(lblNewLabel_2);
	
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setBounds(0, 0, 700, 550);
		lblNewLabel_3.setIcon(new ImageIcon(new ImageIcon("src/imagenes/fondo_menu.jpg").getImage().getScaledInstance(700, 550, getDefaultCloseOperation())));
		contentPane.add(lblNewLabel_3);
	}
		
}
