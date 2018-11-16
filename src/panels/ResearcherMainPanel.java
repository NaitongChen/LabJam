package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JPanel;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ResearcherMainPanel extends JPanel {
	JLabel lblNewLabel;
	/**
	 * Create the panel.
	 */
	public ResearcherMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		JButton btnSeeMyProjects = new JButton("View Project Detail");
		btnSeeMyProjects.setBounds(15, 60, 200, 30);
		add(btnSeeMyProjects);
		String researcherName = getResearcherName(con);
		lblNewLabel = new JLabel("Hello, " + researcherName);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNewLabel.setBounds(15, 15, 290, 30);
		add(lblNewLabel);
		
		JButton btnLogout = new JButton("Logoutttt");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryBuilder.reset();
				mainPanel.add(new LogIn(cl, mainPanel, con), Constant.LOGIN);
				cl.show(mainPanel, Constant.LOGIN);
			}
		});
		btnLogout.setBounds(320, 17, 115, 29);
		add(btnLogout);
	}
	
	private String getResearcherName(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		String name = null;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getResearcherName();
			rs = stmt.executeQuery(query);
			rs.next();
			name = rs.getString("name");
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
	}
}
