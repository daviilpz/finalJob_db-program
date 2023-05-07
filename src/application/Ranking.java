package application;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(59, 36, 314, 181);
        contentPane.add(lblNewLabel);
        
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
            texto += "usuario: " + usuario + ", puntuacion: " + score + "<br/>";
        }
        
        texto += "</html>";
        lblNewLabel.setText(texto);
            
        rs.close();
        stmt.close();
        conn.close();
    }
}
