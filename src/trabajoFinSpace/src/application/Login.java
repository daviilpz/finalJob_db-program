package application;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import com.db4o.User;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
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
		
		textField = new JTextField();
		textField.setBounds(169, 163, 150, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(41, 227, 118, 26);
		contentPane.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(169, 230, 150, 26);
		contentPane.add(passwordField);
		
		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(106, 295, 133, 26);
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
	
	public class LoginForm extends JDialog {
	    private JTextField tfUsuario;
	    private JPasswordField pfPassword;
	    private JButton btAceptar;
	    private JButton btCancelar;
	    private JPanel loginPanel;

	      public LoginForm(JFrame parent) {
	        super(parent);
	        setTitle("Login");
	        setContentPane(loginPanel);
	        setMinimumSize(new Dimension(450,474));
	        setModal(true);
	        setLocationRelativeTo(parent);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


	        // Botones

	        btAceptar.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String usuario = tfUsuario.getText();
	                String password = String.valueOf(pfPassword.getPassword());

	                user = getAuthenticatedUser(usuario, password);

	                if (user != null) {
	                    dispose();
	                    // System.out.println(user.nombre);
	                }
	                else {
	                    JOptionPane.showMessageDialog(LoginForm.this,
	                            "Usuario o Contraseña incorrecto",
	                            "Prueba de nuevo",
	                            JOptionPane.ERROR_MESSAGE);

	                }

	            }
	        });

	        setVisible(true);
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
	            String sql = "SELECT * FROM usuarios WHERE Usuario=? AND Contrasena=?";

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
	}	
}
