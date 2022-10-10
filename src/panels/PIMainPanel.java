package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
import javax.swing.JTextPane;
import java.sql.PreparedStatement;


public class PIMainPanel extends JPanel {
	/**
	 * 
	 */
	private JLabel lblNewLabel;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JComboBox<String> comboBoxOptions;
	private JLabel lblOptions;
	
	private JTextField cid_field;
	private JTextField name_field;
	private JTextField edu_field;
	
	private JTextField pname_field;
	private JTextField category_field;
	private JTextField material_field;
	private JButton btnInsert;
	private JButton btnInsert2;
	/**
	 * Create the panel.
	 */

	public PIMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		
		String PIName = getPIName(con);
		lblNewLabel = new JLabel("Hello, " + PIName);
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
		btnLogout.setBounds(320, 16, 115, 29);
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
		        if (((String)comboBox.getSelectedItem()).equals("Display all labs")) {	
	        		String selectedData = null;
			        int[] selectedRow = table.getSelectedRows();
			        selectedData = (String) table.getValueAt(selectedRow[0], 0);
			        QueryBuilder.setLabID(selectedData);
			        LabProfilePanel labProfilePanel = new LabProfilePanel(cl, mainPanel, con, Constant.PIMAIN);
			        mainPanel.add(labProfilePanel, Constant.LAB);
			        cl.show(mainPanel, Constant.LAB);
		        } else if (((String)comboBox.getSelectedItem()).equals("Display all projects")) {
		        	String selectedData = null;
			        int[] selectedRow = table.getSelectedRows();
			        selectedData = (String) table.getValueAt(selectedRow[0], 0);
			        QueryBuilder.setProjectName(selectedData);
			        ProjectProfilePanel projectProfilePanel = new ProjectProfilePanel(cl, mainPanel, con, Constant.PIMAIN);
			        mainPanel.add(projectProfilePanel, Constant.PROJECT);
			        cl.show(mainPanel, Constant.PROJECT);
		        }
	        }
	     });
		
		
		List<String> queryList = new ArrayList<String>();
		queryList.add("Display all labs");
		queryList.add("Display all lab members");
		
		comboBox = new JComboBox<String>();
		comboBox.addItem("");
		comboBox.addItem("Display all labs");
		comboBox.addItem("Display all lab members");
		comboBox.addItem("Display all projects");
		comboBox.addItem("Insert Collaborators");
		comboBox.addItem("Insert Projects");

		
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
		
		// add project
		pname_field = new JTextField();
		pname_field.setBounds(15, 502, 146, 26);
		add(pname_field);
		pname_field.setColumns(10);
		
		category_field = new JTextField();
		category_field.setBounds(15, 530, 146, 26);
		add(category_field);
		category_field.setColumns(10);
		
		material_field = new JTextField();
		material_field.setBounds(15, 557, 146, 26);
		add(material_field);
		material_field.setColumns(10);
		
		btnInsert2 = new JButton("Insert");
		btnInsert2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String projectName = pname_field.getText();
				String category = category_field.getText();
				String materialType = material_field.getText();
				if (!projectName.isEmpty() && !category.isEmpty() && !materialType.isEmpty()) {
					insert2(con,projectName, category, materialType);
				}
			}
		});
		btnInsert2.setBounds(176, 529, 115, 29);
		add(btnInsert2);
		
		
		
		// add Collab
		cid_field = new JTextField();
		cid_field.setBounds(332, 446, 146, 26);
		add(cid_field);
		cid_field.setColumns(10);
		
		name_field = new JTextField();
		name_field.setBounds(332, 488, 146, 26);
		add(name_field);
		name_field.setColumns(10);
		
		edu_field = new JTextField();
		edu_field.setBounds(332, 530, 146, 26);
		add(edu_field);
		edu_field.setColumns(10);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cid = cid_field.getText();
				String cname = name_field.getText();
				String edu = edu_field.getText();
				if (!cid.isEmpty() && !cname.isEmpty() && !edu.isEmpty()) {
					insert(con,cid, cname, edu);
				}
			}
		});
		btnInsert.setBounds(352, 571, 115, 29);
		add(btnInsert);
		
	
		scrollPane.setVisible(false);
		comboBoxOptions.setVisible(false);
		lblOptions.setVisible(false);
		
		cid_field.setVisible(false);
		name_field.setVisible(false);
		edu_field.setVisible(false);
		pname_field.setVisible(false);
		category_field.setVisible(false);
		material_field.setVisible(false);
		btnInsert.setVisible(false);
		btnInsert2.setVisible(false);
		
	}
	private void insert(Connection con, String cid, String cname, String edu) {
		int cid_int = Integer.parseInt(cid);
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("INSERT INTO Collaborators VALUES (?,?,?)");
			ps.setInt(1, cid_int);
		    ps.setString(2, cname);
		    ps.setString(3, edu);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void insert2(Connection con, String projectName,String category,String materialType) {
		
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("INSERT INTO Projects VALUES (?,?,?)");
			ps.setString(1, projectName);
		    ps.setString(2, category);
		    ps.setString(3, materialType);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private String getPIName(Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query;
		String name = null;
		
		
		try{
			stmt = con.createStatement();
			query = QueryBuilder.getPIName_simple();
			rs = stmt.executeQuery(query);
			rs.next();
			name = rs.getString("name");
			stmt.close();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		return name;
	}
	
	//private void selectAll(Connection con) {
	//	Statement  stmt;
	//	ResultSet  rs = null;
	//	String query = QueryBuilder.getAllCollaborators();
		   
//		try{
//			stmt = con.createStatement();
//			rs= stmt.executeQuery(query);
//			table.setModel(DbUtils.resultSetToTableModel(rs));
//			stmt.close();
			
//		} catch (SQLException ex) {
//			System.out.println("Message: " + ex.getMessage());
//		}
//	}
	
	
	private void showTable(String actionString, Connection con) {
		Statement  stmt;
		ResultSet  rs = null;
		String query = null;
		
		switch (actionString) {
			case "Display all labs":
				System.out.println("wtf");
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
				query = QueryBuilder.getAllLabs();
				break;
			case "Display all lab members":
				System.out.println("wtf member");
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
				comboBoxOptions.setSelectedItem("All");
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Display all projects":
				System.out.println("proj");
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
				query = QueryBuilder.getAllProjects();
				break;
			case "Insert Collaborators":
				System.out.println("wtf collab");
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
			
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(true);
				btnInsert2.setVisible(false);
				
				cid_field.setVisible(true);
				name_field.setVisible(true);
				edu_field.setVisible(true);
				//query = QueryBuilder.getAllCollaborators();
				break;
			case "Insert Projects":
				System.out.println("wtf insert porj");
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				
				btnInsert.setVisible(false);
				btnInsert2.setVisible(true);
				
				scrollPane.setVisible(true);
				pname_field.setVisible(true);
				category_field.setVisible(true);
				material_field.setVisible(true);
				//query = QueryBuilder.getAllProjects();
				break;
			case "":
				System.out.println("wtf nothing");
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				break;
			case "Researcher":	
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				query = QueryBuilder.getAllResearchers();
				break;
			case "All":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Lab Manager":
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				
				query = QueryBuilder.getAllLabManagers();
				break;
				
			case "PI":
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				
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