package panels;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import queries.QueryBuilder;

public class DBAMainPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public DBAMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		JButton btnSeeMyProjects = new JButton("See my Projects");
		btnSeeMyProjects.setBounds(48, 71, 115, 29);
		add(btnSeeMyProjects);
		String researcherID = QueryBuilder.researcherID;
		JLabel lblNewLabel = new JLabel(researcherID);
		lblNewLabel.setBounds(219, 144, 69, 20);
		add(lblNewLabel);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryBuilder.reset();
				mainPanel.add(new LogIn(cl, mainPanel, con), "3");
				cl.show(mainPanel, "3");
			}
		});
		btnLogout.setBounds(70, 161, 115, 29);
		add(btnLogout);
	}

}
