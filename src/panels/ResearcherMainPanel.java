package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
		scrollPane.setBounds(15, 155, 420, 300);
		add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		table.setRowSelectionAllowed(true);
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent e) {
        		if (e.getValueIsAdjusting()) {
		        	if (((String)comboBox.getSelectedItem()).equals("Display all labs")) {	
		        		String selectedData = null;
				        int[] selectedRow = table.getSelectedRows();
				        selectedData = (String) table.getValueAt(selectedRow[0], 0);
				        selectedData = selectedData.trim();
				        QueryBuilder.setLabID(selectedData);
				        LabProfilePanel labProfilePanel = new LabProfilePanel(cl, mainPanel, con, Constant.RESEARCHERMAIN);
				        mainPanel.add(labProfilePanel, Constant.LAB);
				        cl.show(mainPanel, Constant.LAB);
			        } else if (((String)comboBox.getSelectedItem()).equals("Display all projects")) {
			        	String selectedData = null;
				        int[] selectedRow = table.getSelectedRows();
				        selectedData = (String) table.getValueAt(selectedRow[0], 0);
				        selectedData = selectedData.trim();
				        QueryBuilder.setProjectName(selectedData);
				        ProjectProfilePanel projectProfilePanel = new ProjectProfilePanel(cl, mainPanel, con, Constant.RESEARCHERMAIN);
				        mainPanel.add(projectProfilePanel, Constant.PROJECT);
				        cl.show(mainPanel, Constant.PROJECT);
			        }
        		}
	        }
	    });
		
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
		comboBox.setBounds(15, 120, 290, 30);
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
		
		comboBoxOptions.setBounds(15, 495, 290, 30);
		add(comboBoxOptions);
		
		JLabel lblSelectAnOperation = new JLabel("Select an operation...");
		lblSelectAnOperation.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSelectAnOperation.setBounds(15, 85, 290, 30);
		add(lblSelectAnOperation);
		
		JLabel hours = new JLabel("hours");
		hours.setText("My Total Weekly Hours: " + getWeeklyHours(con));
		hours.setFont(new Font("Arial", Font.PLAIN, 20));
		hours.setBounds(15, 50, 290, 30);
		add(hours);
		
		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOptions.setBounds(15, 460, 69, 30);
		add(lblOptions);
		
		scrollPane.setVisible(false);
		comboBoxOptions.setVisible(false);
		lblOptions.setVisible(false);
	}
	
	private int getWeeklyHours(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		int name = 0;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.calculateWeeklyHours();
			rs = stmt.executeQuery(query);
			rs.next();
			name = rs.getInt("total");
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
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
				comboBoxOptions.setSelectedItem("All");
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
