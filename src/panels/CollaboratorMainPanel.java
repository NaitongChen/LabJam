package panels;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;

import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;

public class CollaboratorMainPanel extends JPanel {
	JTable table;
	/**
	 * Create the panel.
	 */
	public CollaboratorMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		JLabel lblCollab = new JLabel("Collaborator Dashboard");
		lblCollab.setFont(new Font("Arial", Font.PLAIN, 16));
		lblCollab.setBounds(300, 20, 250, 66);
		add(lblCollab);

		table = new JTable();
		table.setBounds(44, 300, 349, 164);
		add(table);

		JButton btnBackToLogin = new JButton ("Back to Log-In Page");
		btnBackToLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(mainPanel, "3");
			}
		});
		btnBackToLogin.setBounds(600, 20, 180, 29);
		add(btnBackToLogin);
		
		JButton btnSeeProjects = new JButton("See My Projects");
		btnSeeProjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMyProjects(con);
			}
		});
		btnSeeProjects.setBounds(50, 120, 180, 29);
		add(btnSeeProjects);
	}

	private void selectMyProjects(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		   
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Project_MaterialType");
			table.setModel(DbUtils.resultSetToTableModel(rs));
			// close the statement; 
			// the ResultSet will also be closed
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

}