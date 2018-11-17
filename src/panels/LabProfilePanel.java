package panels;

import java.awt.CardLayout;
import java.sql.Connection;

import javax.swing.JPanel;

import queries.QueryBuilder;

public class LabProfilePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public LabProfilePanel(CardLayout cl, JPanel mainPanel, Connection con) {
		System.out.println(QueryBuilder.labID);
	}

}
