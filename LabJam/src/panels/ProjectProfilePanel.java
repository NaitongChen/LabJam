package panels;

import java.awt.CardLayout;
import java.sql.Connection;

import javax.swing.JPanel;

import queries.QueryBuilder;

public class ProjectProfilePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ProjectProfilePanel(CardLayout cl, JPanel mainPanel, Connection con) {
		System.out.println(QueryBuilder.projectName);
	}

}
