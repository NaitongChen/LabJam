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
import javax.swing.JTextField;
import java.sql.PreparedStatement;

public class LabManagerMainPanel extends JPanel {
	private JLabel lblNewLabel;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JComboBox<String> comboBoxOptions;
	private JComboBox<String> comboBoxProjects;
	private JLabel lblOptions;
	private JLabel lblProjects;
	private JTextField sid_field;
	private JTextField name_field;
	private JTextField availability_field;
	private JButton btnInsert;
	/**
	 * Create the panel.
	 */
	public LabManagerMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		String labManagerName = getLabManagerName(con);
		lblNewLabel = new JLabel("Hello, " + labManagerName);
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
		comboBox.addItem("Display all open grants");
		comboBox.addItem("Display approved open grants");
		comboBox.addItem("Display rejected open grants");
		comboBox.addItem("Display applied open grants");
		comboBox.addItem("Display grants for given project");
		comboBox.addItem("Insert subjects");
		
		
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

		comboBoxProjects = new JComboBox<String>();
		populateProjectBox(con);

		comboBoxProjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String actionString = (String)comboBox.getSelectedItem();
				showTable(actionString, con);
			}
		});

		comboBoxProjects.setBounds(15, 460, 290, 30);
		add(comboBoxProjects);
		
		JLabel lblSelectAnOperation = new JLabel("Select an operation...");
		lblSelectAnOperation.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSelectAnOperation.setBounds(15, 50, 290, 30);
		add(lblSelectAnOperation);
		
		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOptions.setBounds(15, 425, 69, 30);
		add(lblOptions);

		lblProjects = new JLabel("Projects");
		lblProjects.setFont(new Font("Arial", Font.PLAIN, 20));
		lblProjects.setBounds(15, 425, 100, 30);
		add(lblProjects);

		//fields for insert
		sid_field = new JTextField();
		sid_field.setBounds(68, 145, 146, 26);
		add(sid_field);
		sid_field.setColumns(10);
		
		name_field = new JTextField();
		name_field.setBounds(68, 187, 146, 26);
		add(name_field);
		name_field.setColumns(10);
		
		availability_field = new JTextField();
		availability_field.setBounds(68, 232, 146, 26);
		add(availability_field);
		availability_field.setColumns(10);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sid = sid_field.getText();
				String name = name_field.getText();
				String availability = availability_field.getText();
				if (!sid.isEmpty() && !name.isEmpty() && !availability.isEmpty()) {
					insert(con, sid, name, availability);
				}
			}
		});
		btnInsert.setBounds(78, 285, 115, 29);
		add(btnInsert);

		scrollPane.setVisible(false);
		comboBoxOptions.setVisible(false);
		comboBoxProjects.setVisible(false);
		lblOptions.setVisible(false);
		lblProjects.setVisible(false);
		btnInsert.setVisible(false);
		availability_field.setVisible(false);
		name_field.setVisible(false);
		sid_field.setVisible(false);
	}
	
	private String getLabManagerName(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		String name = null;
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getLabManagerName();
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
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getAllLabs();
				break;
			case "Display all lab members":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				comboBoxOptions.setSelectedItem("All");
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Display all projects":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getAllProjects();
				break;
				
			case "Display all open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getAllOpenGrants();
				break;

			case "Display approved open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getApprovedOpenGrants();
				break;

			case "Display rejected open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getRejectedOpenGrants();
				break;
			
			case "Display applied open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getAppliedOpenGrants();
				break;

			case "Display grants for given project":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(true);
				lblOptions.setVisible(false);
				lblProjects.setVisible(true);
				btnInsert.setVisible(false);
				availability_field.setVisible(false);
				name_field.setVisible(false);
				sid_field.setVisible(false);
				query = QueryBuilder.getGrantsForProject((String)comboBoxProjects.getSelectedItem());
				break;
			
			case "Insert subjects":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsert.setVisible(true);
				availability_field.setVisible(true);
				name_field.setVisible(true);
				sid_field.setVisible(true);
				// query = insert(con, sid, name, availability); // add values here!! - or maybe don't need it at all
				break;

			case "":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				break;

			case "Researcher":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				query = QueryBuilder.getAllResearchers();
				break;

			case "All":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				query = QueryBuilder.getAllLabMembers();
				break;

			case "Lab Manager":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				query = QueryBuilder.getAllLabManagers();
				break;

			case "PI":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
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

	private void populateProjectBox(Connection con) {
		Statement stmt;
		ResultSet rs = null;
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery(QueryBuilder.getAllProjectNames());
			while (rs.next()) {
				String projectName = rs.getString("projectName");
				comboBoxProjects.addItem(projectName);
			}
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	private void insert(Connection con, String sid, String name, String availability) {
		PreparedStatement ps;

		try{
			ps = con.prepareStatement("insert into Subjects");
			ps.setString(1, sid);
		    ps.setString(2, name);
		    ps.setString(3, availability);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
