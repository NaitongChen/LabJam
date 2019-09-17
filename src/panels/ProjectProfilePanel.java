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

public class ProjectProfilePanel extends JPanel {

	private JLabel lblNewLabel;
	private JLabel lblPI;
	private JLabel lblLab;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JLabel count;
	private JLabel totalFundingLabel;
	private JLabel remainingFundingLabel;
	/**
	 * Create the panel.
	 */
	public ProjectProfilePanel(CardLayout cl, JPanel mainPanel, Connection con, String from) {
		setLayout(null);
		float funding = getTotalFunding(con);
		QueryBuilder.setTotalFunding(funding);
		
		String projectName = QueryBuilder.projectName;
		lblNewLabel = new JLabel(projectName);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNewLabel.setBounds(15, 15, 420, 30);
		add(lblNewLabel);
		
		String piName = getPIName(con);
		lblPI = new JLabel("PI: " + piName);
		lblPI.setFont(new Font("Arial", Font.PLAIN, 20));
		lblPI.setBounds(15, 50, 420, 30);
		add(lblPI);
		
		String labName = getLabName(con);
		lblLab = new JLabel("Lab: " + labName);
		lblLab.setFont(new Font("Arial", Font.PLAIN, 20));
		lblLab.setBounds(15, 85, 420, 30);
		add(lblLab);
		
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
		scrollPane.setBounds(15, 155, 420, 300);
		add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);

		comboBox = new JComboBox<String>();
		comboBox.addItem("");
		comboBox.addItem("Display all researchers in the project");
		comboBox.addItem("Display all collaborators in the project");
		comboBox.addItem("Display material and price");
		if (QueryBuilder.researcherID == null && QueryBuilder.collaboratorID == null) {
			comboBox.addItem("Display all grants for the project");
		}
		comboBox.addItem("Display all participants for the project");
		comboBox.addItem("Display researcher counts by type");
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String action = (String)comboBox.getSelectedItem();
				showTable(action, con);
			}
		});
		comboBox.setBounds(15, 120, 290, 30);
		add(comboBox);
		
		count = new JLabel("Count:");
		count.setFont(new Font("Arial", Font.PLAIN, 20));
		count.setBounds(15, 460, 400, 30);
		add(count);
		
		totalFundingLabel = new JLabel("Total funding: " + QueryBuilder.totalFunding);
		totalFundingLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		totalFundingLabel.setBounds(15, 460, 400, 30);
		add(totalFundingLabel);
		
		remainingFundingLabel = new JLabel("Remaining funding: " + QueryBuilder.totalFunding);
		remainingFundingLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		remainingFundingLabel.setBounds(15, 495, 400, 30);
		add(remainingFundingLabel);
		
		scrollPane.setVisible(false);
		count.setVisible(false);
		totalFundingLabel.setVisible(false);
		remainingFundingLabel.setVisible(false);
	}

	private float getTotalFunding(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		float name = 0;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.calculateTotalFunding();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				name = rs.getFloat("total");
			}
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
	}

	private int getNumResearcher(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		int name = 0;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.countResearchersForProject();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				name = rs.getInt("count");
			}
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
	}

	private String getPIName(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		String name = null;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getPIName();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				name = rs.getString("name");
			}
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
			query = QueryBuilder.getLabName();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				name = rs.getString("name");
			}
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
			case "Display all researchers in the project":
				int numResearcher = getNumResearcher(con);
				count.setText("Count: " + numResearcher);
				scrollPane.setVisible(true);
				count.setVisible(true);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getAllResearchersForProject();
				break;
			case "Display all collaborators in the project":
				scrollPane.setVisible(true);
				count.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getAllCollaborators();
				break;
			case "Display material and price":
				scrollPane.setVisible(true);
				count.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getMaterial();
				break;
			case "":
				scrollPane.setVisible(false);
				count.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				break;
			case "Display all grants for the project":
				float remain = getRemainingFunding(con);
				remainingFundingLabel.setText("Remaining funding: " + remain);
				scrollPane.setVisible(true);
				totalFundingLabel.setVisible(true);
				remainingFundingLabel.setVisible(true);
				count.setVisible(false);
				query = QueryBuilder.getAllGrants();
				break;
			case "Display all participants for the project":
				int[] counts = countControlExperimental(con);
				count.setText("Control Count: " + counts[0] + " Experimental Count: " + counts[1]);
				scrollPane.setVisible(true);
				count.setVisible(true);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getAllBookings();
				break;
			case "Display researcher counts by type":
				scrollPane.setVisible(true);
				count.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getCountsByResearcherType();
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

	private float getRemainingFunding(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		float remain = 0; 
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.calculateRemainingFunding();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				remain = rs.getFloat("remaining");
			}
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return remain;
	}

	private int[] countControlExperimental(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		int[] counts = new int[2]; 
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.countControl();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				counts[0] = rs.getInt("count");
			} else {
				counts[0] = 0;
			}
			stmt.close();
			stmt = con.createStatement();
			query = QueryBuilder.countExperimental();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				counts[1] = rs.getInt("count");
			} else {
				counts[0] = 0;
			}
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return counts;
	}
}
