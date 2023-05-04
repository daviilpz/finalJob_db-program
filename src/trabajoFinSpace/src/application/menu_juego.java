package application;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class menu_juego extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menu_juego frame = new menu_juego();
				    frame.setIconImage(Toolkit.getDefaultToolkit().createImage("src/imagenes/icon.png"));
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
	public menu_juego() {
		setEnabled(true);
		setResizable(false);
		setTitle("Menú Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 655, 550);
		contentPane = new JPanel();
		setLocationRelativeTo(null);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 0));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRegistrarse = new JButton("Iniciar Sesión");
		btnRegistrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				Login a = new Login();
				a.setVisible(true);
				
			}
		}
			);
		btnRegistrarse.setFont(new Font("Arial", Font.BOLD, 12));
		btnRegistrarse.setBounds(510, 10, 172, 60);
		contentPane.add(btnRegistrarse);
		
		JButton btnNewButton_1 = new JButton("Nueva Partida");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpaceInvaders.launch(SpaceInvaders.class);
			}
		});
		
		
		btnNewButton_1.setFont(new Font("Arial", Font.BOLD, 20));
		btnNewButton_1.setBounds(184, 82, 288, 84);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Salir del juego");
		btnNewButton_2.setFont(new Font("Arial", Font.BOLD, 20));
		//addActionListener e -> dispose para que se cierre la ventana 
		btnNewButton_2.addActionListener(e -> dispose());
		btnNewButton_2.setBounds(184, 356, 288, 84);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Ranking");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		
		btnNewButton_3.setFont(new Font("Arial", Font.BOLD, 20));
		btnNewButton_3.setBounds(184, 211, 288, 84);
		contentPane.add(btnNewButton_3);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(0, 0, 686, 513);
		lblNewLabel.setIcon(new ImageIcon(new ImageIcon("src/imagenes/fondo_menu.jpg").getImage().getScaledInstance(700, 550, getDefaultCloseOperation())));	    
		contentPane.add(lblNewLabel);
		

	}
}
