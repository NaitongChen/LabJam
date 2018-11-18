package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;

public class LabProfilePanel extends JPanel {

	private JLabel lblNewLabel;
	private JLabel lblPI;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JLabel count;
	/**
	 * Create the panel.
	 */
	public LabProfilePanel(CardLayout cl, JPanel mainPanel, Connection con, String from) {
		setLayout(null);
		
		String labName = getLabName(con);
		lblNewLabel = new JLabel(labName);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNewLabel.setBounds(15, 15, 420, 30);
		add(lblNewLabel);
		
		String piName = getPIName(con);
		lblPI = new JLabel("PI: " + piName);
		lblPI.setFont(new Font("Arial", Font.PLAIN, 20));
		lblPI.setBounds(15, 50, 420, 30);
		add(lblPI);
		
		JButton btnLogout = new JButton("Back");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(mainPanel, from);
				QueryBuilder.resetProject();
			}
		});
		btnLogout.setBounds(320, 565, 115, 30);
		add(btnLogout);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 120, 420, 300);
		add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);

		comboBox = new JComboBox<String>();
		comboBox.addItem("");
		comboBox.addItem("Display all projects in the lab");
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String action = (String)comboBox.getSelectedItem();
				showTable(action, con);
			}
		});
		comboBox.setBounds(15, 85, 290, 30);
		add(comboBox);
		
		count = new JLabel("Count:");
		count.setFont(new Font("Arial", Font.PLAIN, 20));
		count.setBounds(15, 460, 400, 30);
		add(count);
		
		scrollPane.setVisible(false);
		count.setVisible(false);
	}

	private String getPIName(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		String name = null;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getPINameByLabID();
			rs = stmt.executeQuery(query);
			rs.next();
			name = rs.getString("name");
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
	}
	
	private String getLabName(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		String name = null;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getLabNameByLabID();
			rs = stmt.executeQuery(query);
			rs.next();
			name = rs.getString("name");
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
	}
	
	private void showTable(String actionString, Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		
		switch (actionString) {
			case "Display all projects in the lab":
				int numResearcher = getNumProject(con);
				count.setText("Count: " + numResearcher);
				scrollPane.setVisible(true);
				count.setVisible(true);
				query = QueryBuilder.getProjectsGivenLab();
				System.out.println(query);
				break;
			case "":
				scrollPane.setVisible(false);
				count.setVisible(false);
				break;
		}
		
		try{
			if (query != null) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				table.setModel(DbUtils.resultSetToTableModel(rs));
				stmt.close();
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	private int getNumProject(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = QueryBuilder.countNumProject();
		int count = 0;
		try{
			if (query != null) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				rs.next();
				count = rs.getInt("count");
				stmt.close();
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return count;
	}
}
