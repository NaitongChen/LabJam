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
	private JLabel lblProjectName;
	private JLabel lblCategory;
	private JLabel lblMaterial;
	
	private JTextField cid_field;
	private JTextField name_field;
	private JTextField edu_field;
	
	private JTextField pname_field;
	private JTextField category_field;
	private JTextField material_field;
	private JTextField colab_pname_field;
	
	private JTextField update_pname_field;
	private JTextField update_category_field;
	private JTextField update_material_field;
	
	private JButton btnInsert;
	private JButton btnInsert2;
	private JButton btnUpdateCategory;
	
	
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
		scrollPane.setBounds(15, 119, 452, 300);
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
			        ProjectProfilePanel projectProfilePanel = new ProjectProfilePanel(cl, mainPanel, con,Constant.PIMAIN);
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
		comboBox.addItem("Display all collaborators");
		comboBox.addItem("Insert Collaborators");
		comboBox.addItem("Insert Projects");
		comboBox.addItem("Update Project Material Type");
		comboBox.addItem("Update Project Category");

		
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
		cid_field.setBounds(332, 435, 146, 26);
		add(cid_field);
		cid_field.setColumns(10);
		
		name_field = new JTextField();
		name_field.setBounds(332, 462, 146, 26);
		add(name_field);
		name_field.setColumns(10);
		
		edu_field = new JTextField();
		edu_field.setBounds(332, 490, 146, 26);
		add(edu_field);
		edu_field.setColumns(10);
		
		colab_pname_field=new JTextField();
		colab_pname_field.setBounds(332, 518, 146, 26);
		add(colab_pname_field);
		colab_pname_field.setColumns(10);
		
		btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cid = cid_field.getText();
				String cname = name_field.getText();
				String edu = edu_field.getText();
				String colab_pname = colab_pname_field.getText();
				if (!cid.isEmpty() && !cname.isEmpty() && !edu.isEmpty() && !colab_pname.isEmpty()) {
					insert(con,cid, cname, edu,colab_pname);
				}
			}
		});
		btnInsert.setBounds(352, 571, 115, 29);
		add(btnInsert);
		
		// update Project category & Material
		// just two JTextField
		
		update_pname_field = new JTextField();
		update_pname_field.setBounds(176, 193, 146, 26);
		add(update_pname_field);
		update_pname_field.setColumns(10);
		
		update_category_field = new JTextField();
		update_category_field.setBounds(176, 274, 146, 26);
		add(update_category_field);
		category_field.setColumns(10);
		
		update_material_field = new JTextField();
		update_material_field.setBounds(176, 274, 146, 26);
		add(update_material_field);
		update_material_field.setColumns(10);
		
		lblProjectName = new JLabel("Project Name");
		lblProjectName.setBounds(176, 151, 129, 20);
		add(lblProjectName);
		
		lblCategory = new JLabel("Category you wish to update to");
		lblCategory.setBounds(176, 235, 234, 20);
		add(lblCategory);
		
		lblMaterial = new JLabel("Material you wish to update to");
		lblMaterial.setBounds(176, 235, 234, 20);
		add(lblMaterial);
		
		btnUpdateCategory = new JButton("Update Category");
		btnUpdateCategory.setBounds(119, 335, 186, 30);
		btnUpdateCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pname = update_pname_field.getText();
				System.out.println("from action: " + pname);
				String category = update_category_field.getText();
				if (!pname.isEmpty() && !category.isEmpty()) {
					updateCat(con,pname,category);
				}
			}
		});
		btnUpdateCategory.setBounds(352, 571, 115, 29);
		add(btnUpdateCategory);
		
		lblProjectName.setVisible(false);
		lblCategory.setVisible(false);
		lblMaterial.setVisible(false);
		update_pname_field.setVisible(false);
		update_category_field.setVisible(false);
		update_material_field.setVisible(false);
		btnUpdateCategory.setVisible(false);
		
		
		comboBoxOptions.setVisible(false);
		lblOptions.setVisible(false);
		
		cid_field.setVisible(false);
		name_field.setVisible(false);
		edu_field.setVisible(false);
		colab_pname_field.setVisible(false);
		pname_field.setVisible(false);
		category_field.setVisible(false);
		material_field.setVisible(false);
		btnInsert2.setVisible(false);
		
	}
	private void updateCat(Connection con, String pname,String category) {
			
			PreparedStatement ps;
			
			try{
				String query = QueryBuilder.updateProjectCategory(pname, category);
			    ps = con.prepareStatement(query);
				int value = ps.executeUpdate();
			    con.commit();
			    ps.close();
			    System.out.println(value);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	
	private void insert(Connection con, String cid, String cname, String edu,String projectName) {
		int cid_int = Integer.parseInt(cid);
		PreparedStatement ps;
		PreparedStatement ps2;
		
		try{
			ps = con.prepareStatement("INSERT INTO Assigned_Collaborators_Id VALUES (?,?,?)");
			ps2 = con.prepareStatement("INSERT INTO Name_Education_ProjectName VALUES (?,?,?)");
			ps.setInt(1, cid_int);
		    ps.setString(2, cname);
		    ps.setString(3, edu);
		    
		    ps2.setString(1,cname);
		    ps2.setString(2,edu);
		    ps2.setString(3,projectName);
		    ps.executeUpdate();
		    ps2.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		    ps2.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void insert2(Connection con, String projectName,String category,String materialType) {
		
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("INSERT INTO Project_MaterialType VALUES (?,?,?)");
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
			case "Update Project Category": 
				table.clearSelection();
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				lblMaterial.setVisible(false);
				update_pname_field.setVisible(true);
				update_category_field.setVisible(true);
				update_material_field.setVisible(false);
				btnUpdateCategory.setVisible(true);
			
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				colab_pname_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(true);
				lblCategory.setVisible(true);
				// update button does query
				break;
			case "Display all collaborators":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				btnUpdateCategory.setVisible(false);
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				colab_pname_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				
				query = QueryBuilder.getAllCollaborators_simple();
				break;
				
			case "Display all labs":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				colab_pname_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				
				query = QueryBuilder.getAllLabs();
				break;
			case "Display all lab members":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				colab_pname_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				
				comboBoxOptions.setSelectedItem("All");
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Display all projects":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				colab_pname_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				query = QueryBuilder.onlyProjects();
				break;
			case "Insert Collaborators":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				colab_pname_field.setVisible(true);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(true);
				btnInsert2.setVisible(false);
				
				cid_field.setVisible(true);
				name_field.setVisible(true);
				edu_field.setVisible(true);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				query = QueryBuilder.getAllCollaborators();
				break;
			case "Insert Projects":
				table.clearSelection();
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				colab_pname_field.setVisible(false);
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				
				btnInsert.setVisible(false);
				btnInsert2.setVisible(true);
				
				scrollPane.setVisible(true);
				pname_field.setVisible(true);
				category_field.setVisible(true);
				material_field.setVisible(true);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				break;
			case "":
				table.clearSelection();
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				lblOptions.setVisible(false);
				colab_pname_field.setVisible(false);
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				break;
			case "Researcher":	
				table.clearSelection();
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				colab_pname_field.setVisible(false);
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				query = QueryBuilder.getAllResearchers();
				break;
			case "All":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				colab_pname_field.setVisible(false);
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Lab Manager":
				table.clearSelection();
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				colab_pname_field.setVisible(false);
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
				query = QueryBuilder.getAllLabManagers();
				break;
				
			case "PI":
				table.clearSelection();
				cid_field.setVisible(false);
				name_field.setVisible(false);
				edu_field.setVisible(false);
				pname_field.setVisible(false);
				category_field.setVisible(false);
				material_field.setVisible(false);
				btnInsert.setVisible(false);
				btnInsert2.setVisible(false);
				colab_pname_field.setVisible(false);
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				lblOptions.setVisible(true);
				lblProjectName.setVisible(false);
				lblCategory.setVisible(false);
				btnUpdateCategory.setVisible(false);
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