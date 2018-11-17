package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;

public class ProjectProfilePanel extends JPanel {

	private JLabel lblNewLabel;
	private JLabel lblPI;
	private JLabel lblLab;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JComboBox<String> comboBoxOptions;
	private JLabel count;
	private JButton btnPurchase;
	private JLabel totalFundingLabel;
	private JLabel remainingFundingLabel;
	/**
	 * Create the panel.
	 */
	public ProjectProfilePanel(CardLayout cl, JPanel mainPanel, Connection con, String from) {
		setLayout(null);
		int funding = getTotalFunding(con);
		QueryBuilder.setTotalFunding(funding);
		float materialPrice = getMaterialPrice(con);
		QueryBuilder.setMaterialPrice(materialPrice);
		
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
		comboBox.addItem("Display all grants for the project");
		comboBox.addItem("Display all participants for the project");
		
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
		
		btnPurchase = new JButton("Purchase");
		if (QueryBuilder.purchased) {
			btnPurchase.setEnabled(false);
		}
		
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryBuilder.purchased = true;
				btnPurchase.setEnabled(false);
			}
		});
		btnPurchase.setBounds(15, 460, 115, 30);
		add(btnPurchase);
		
		btnPurchase.setVisible(false);
		scrollPane.setVisible(false);
		count.setVisible(false);
		totalFundingLabel.setVisible(false);
		remainingFundingLabel.setVisible(false);
	}

	private int getTotalFunding(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		int name = 0;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.calculateTotalFunding();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				name = rs.getInt("total");
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
			rs.next();
			name = rs.getInt("count");
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
			query = QueryBuilder.getLabName();
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
			case "Display all researchers in the project":
				int numResearcher = getNumResearcher(con);
				count.setText("Count: " + numResearcher);
				scrollPane.setVisible(true);
				count.setVisible(true);
				btnPurchase.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getAllResearchersForProject();
				break;
			case "Display all collaborators in the project":
				scrollPane.setVisible(true);
				count.setVisible(false);
				btnPurchase.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getAllCollaborators();
				break;
			case "Display material and price":
				scrollPane.setVisible(true);
				count.setVisible(false);
				btnPurchase.setVisible(true);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getMaterial();
				break;
			case "":
				scrollPane.setVisible(false);
				count.setVisible(false);
				btnPurchase.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				break;
			case "Display all grants for the project":
				float remain = QueryBuilder.totalFunding;
				if(QueryBuilder.purchased) {
					float original = (float)QueryBuilder.totalFunding/1;
					remain = original - QueryBuilder.materialPrice;
				}
				remainingFundingLabel.setText("Remaining funding: " + remain);
				scrollPane.setVisible(true);
				btnPurchase.setVisible(false);
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
				btnPurchase.setVisible(false);
				totalFundingLabel.setVisible(false);
				remainingFundingLabel.setVisible(false);
				query = QueryBuilder.getAllBookings();
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

	private int[] countControlExperimental(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		int[] counts = new int[2]; 
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.countControlExperimental();
			rs = stmt.executeQuery(query);
			int i = 0;
			while(rs.next()) {
				counts[i] = rs.getInt("count");
				i++;
			}
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return counts;
	}
	
	private float getMaterialPrice(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		float price = 0; 
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getMaterial();
			rs = stmt.executeQuery(query);
			rs.next();
			price = rs.getFloat("materialPrice");
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return price;
	}
}
