package panels;

import java.awt.CardLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;

import javax.swing.JPanel;

import queries.QueryBuilder;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ResearcherMainPanel extends JPanel {
	
	/**
	 * Create the panel.
	 */
	public ResearcherMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		JButton btnSeeMyProjects = new JButton("See my Projects");
		btnSeeMyProjects.setBounds(48, 71, 115, 29);
		add(btnSeeMyProjects);
		String researcherID = QueryBuilder.researcherID;
		JLabel lblNewLabel = new JLabel(researcherID);
		lblNewLabel.setBounds(219, 144, 69, 20);
		add(lblNewLabel);
	}
}
