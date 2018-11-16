package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class ResearcherMainPanel extends JPanel {
	private JLabel lblNewLabel;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JComboBox<String> comboBoxOptions;
	private JLabel lblOptions;
	/**
	 * Create the panel.
	 */
	public ResearcherMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		String researcherName = getResearcherName(con);
		lblNewLabel = new JLabel("Hello, " + researcherName);
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		lblNewLabel.setBounds(15, 15, 290, 30);
		add(lblNewLabel);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryBuilder.reset();
				mainPanel.add(new LogIn(cl, mainPanel, con), Constant.LOGIN);
				cl.show(mainPanel, Constant.LOGIN);
			}
		});
		btnLogout.setBounds(320, 17, 115, 30);
		add(btnLogout);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 120, 420, 300);
		add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		List<String> queryList = new ArrayList<String>();
		queryList.add("Display all labs");
		queryList.add("Display all lab members");
		
		comboBox = new JComboBox<String>();
		comboBox.addItem("");
		comboBox.addItem("Display all labs");
		comboBox.addItem("Display all lab members");
		comboBox.addItem("Display all projects");
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String action = (String)comboBox.getSelectedItem();
				showTable(action, con);
			}
		});
		comboBox.setBounds(15, 85, 290, 30);
		add(comboBox);
		
		comboBoxOptions = new JComboBox<String>();
		comboBoxOptions.addItem("All");
		comboBoxOptions.addItem("Researcher");
		comboBoxOptions.addItem("Lab Manager");
		comboBoxOptions.addItem("PI");
		
		comboBoxOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String actionString = (String)comboBoxOptions.getSelectedItem();
				showTable(actionString, con);
			}
		});
		
		comboBoxOptions.setBounds(15, 460, 290, 30);
		add(comboBoxOptions);
		
		JLabel lblSelectAnOperation = new JLabel("Select an operation...");
		lblSelectAnOperation.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSelectAnOperation.setBounds(15, 50, 290, 30);
		add(lblSelectAnOperation);
		
		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOptions.setBounds(15, 425, 69, 30);
		add(lblOptions);
		
		scrollPane.setVisible(false);
		comboBoxOptions.setVisible(false);
		lblOptions.setVisible(false);
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
	
	private void showTable(String actionString, Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		
		switch (actionString) {
			case "Display all labs":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				query = QueryBuilder.getAllLabs();
				break;
			case "Display all lab members":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Display all projects":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				query = QueryBuilder.getAllProjects();
				break;
			case "":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				break;
			case "Researcher":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				query = QueryBuilder.getAllResearchers();
				break;
			case "All":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Lab Manager":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				query = QueryBuilder.getAllLabManagers();
				break;
			case "PI":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				query = QueryBuilder.getAllPIs();
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
}
