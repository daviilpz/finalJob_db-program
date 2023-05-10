package application;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

public class Ranking extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Ranking frame = new Ranking();
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
     * @throws SQLException 
     */
    public Ranking() throws SQLException {
    	setEnabled(true);
		setResizable(false);
		setTitle("Ranking");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 655, 550);
		contentPane = new JPanel();
		setLocationRelativeTo(null);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0), 0));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(90, 82, 501, 152);
        lblNewLabel.setForeground(Color.ORANGE);
		lblNewLabel.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 17));
        contentPane.add(lblNewLabel);
        
       
        JLabel lblNewLabel_1 = new JLabel("RANKING");
		lblNewLabel_1.setForeground(new Color(255, 140, 0));
		lblNewLabel_1.setFont(new Font("Verdana", Font.BOLD, 50));
		lblNewLabel_1.setBounds(184, 25, 380, 49);
		contentPane.add(lblNewLabel_1);
        
        final String DB_URL = "jdbc:mysql://localhost/registro?serverTimezoneUTC";
        final String USERNAME = "root";
        final String PASS = "";
        
        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
        Statement stmt = conn.createStatement();
        
        String sql = "SELECT usuario, puntuacion FROM ranking ORDER BY puntuacion DESC";
        
        ResultSet rs = stmt.executeQuery(sql);
        
        String texto = "<html>";
        
        while (rs.next()) {
            String usuario = rs.getString("usuario");
            String score = rs.getString("puntuacion");
            texto += "Usuario: " + usuario + " Puntuación: " + score + " puntos" + "<br/>";
        }
        
        texto += "</html>";
        lblNewLabel.setText(texto);
        
        JLabel fondo = new JLabel("New label");
		fondo.setBounds(0, 0, 686, 513);
		fondo.setIcon(new ImageIcon(new ImageIcon("src/imagenes/fondo_menu.jpg").getImage().getScaledInstance(700, 550, getDefaultCloseOperation())));	    
		contentPane.add(fondo);
            
        rs.close();
        stmt.close();
        conn.close();
    }
}
