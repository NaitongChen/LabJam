package panels;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;

import net.proteanit.sql.DbUtils;
import queries.QueryBuilder;

public class DBAMainPanel extends JPanel {
	protected JLabel lblNewLabel;
	protected JTable table;
	protected JButton btnLogout;
	protected JComboBox<String> comboBox;
	protected JComboBox<String> openGrantOpenBox;
	protected JScrollPane scrollPane;
	protected JComboBox<String> comboBoxOptions;
	protected JLabel lblOptions;

	protected JTextField name_input;
	protected JTextField amout_input;
	protected JTextField projectName_input;
	protected JTextField date_applied_input;
	protected JTextField status_input;

	protected JTextField lab_member_id_input;
	protected JTextField lab_member_educ_input;
	protected JTextField lab_member_name_input;
	
	private DBASqlCommands dba_sql_command;
	
	private JTextField collaborator_id_input;
	
	JButton delete_colab;
	JButton delete_approved_grant;
	JButton insert_open_grant;
	JButton insert_approved_grant;
	
	JButton update_lm_educ;
	JButton update_lm_name;
	JButton update_open_grant;
	
	String selected_open_grant_input = null;	
	
	ArrayList<JButton> sql_command_buttons = new ArrayList<JButton>();
	ArrayList<JTextField> sql_command_inputs = new ArrayList<JTextField>();

	/**
	 * Create the panel.
	 */

	public DBAMainPanel(CardLayout cl, JPanel mainPanel, Connection con) {
		setLayout(null);
		DBAPanelSetUp dba_basic_panel = new DBAPanelSetUp(con, cl, mainPanel);
		lblNewLabel = dba_basic_panel.get_label();
		comboBox = dba_basic_panel.get_combo_box();
		comboBoxOptions = dba_basic_panel.get_researcher_subcombo();
		openGrantOpenBox = dba_basic_panel.get_grant_combo_box();
		btnLogout = dba_basic_panel.get_btn_log_out();
		btnLogout.setBounds(320, 17, 115, 30);
		add(btnLogout);
		add(lblNewLabel);

		
		dba_sql_command = new DBASqlCommands(con);
		

		List<String> queryList = new ArrayList<String>();
		queryList.add("Display all labs");
		queryList.add("Display all lab members");

		construct_grant_inputs();
		construct_lm_inputs();
		construct_buttons();
		
		
		
		
		
		

		/** Above are DBAdmin related tasks */

		comboBoxOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String actionString = (String) comboBoxOptions.getSelectedItem();
				showTable(actionString, con);
			}
		});
		
		openGrantOpenBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected_open_grant_input = (String) openGrantOpenBox.getSelectedItem();
			}
		});

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String action = (String) comboBox.getSelectedItem();
				showTable(action, con);
			}
		});
		comboBox.setBounds(15, 85, 290, 30);
		add(comboBox);

		comboBoxOptions.setBounds(15, 460, 290, 30);
		add(comboBoxOptions);
		
		openGrantOpenBox.setBounds(78, 420, 313, 29);
		add(openGrantOpenBox);

		JLabel lblSelectAnOperation = new JLabel("Select an operation...");
		lblSelectAnOperation.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSelectAnOperation.setBounds(15, 50, 290, 30);
		add(lblSelectAnOperation);

		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOptions.setBounds(15, 425, 69, 30);
		add(lblOptions);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 120, 420, 300);
		add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		
		table.setRowSelectionAllowed(true);
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
		comboBoxOptions.setVisible(false);
		lblOptions.setVisible(false);
		openGrantOpenBox.setVisible(false);
		
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting() && table.getSelectedRows().length > 0) {
					if (((String) comboBox.getSelectedItem()).equals("Display all labs")) {
						String selectedData = null;
						int[] selectedRow = table.getSelectedRows();
						selectedData = (String) table.getValueAt(selectedRow[0], 0);
						QueryBuilder.setLabID(selectedData);
						LabProfilePanel labProfilePanel = new LabProfilePanel(cl, mainPanel, con, Constant.DBAMAIN);
						mainPanel.add(labProfilePanel, Constant.LAB);
						cl.show(mainPanel, Constant.LAB);
					} else if (((String) comboBox.getSelectedItem()).equals("Display all projects")) {
						String selectedData = null;
						int[] selectedRow = table.getSelectedRows();
						selectedData = (String) table.getValueAt(selectedRow[0], 0);
						QueryBuilder.setProjectName(selectedData);
						ProjectProfilePanel projectProfilePanel = new ProjectProfilePanel(cl, mainPanel, con, Constant.DBAMAIN);
						mainPanel.add(projectProfilePanel, Constant.PROJECT);
						cl.show(mainPanel, Constant.PROJECT);
					}
				}
			}
		});
		hide_all_buttons();
	}
	
	
	private void construct_buttons() {
		delete_colab = new JButton("Delete Colab");
		delete_colab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String collaborator_id = collaborator_id_input.getText();
				dba_sql_command.delete_collaborator(collaborator_id);
			}}
		);
		button_set_default_bounds(delete_colab);
		add(delete_colab);
		delete_colab.setVisible(false);

		delete_approved_grant = new JButton("Delete Approved Grant from Open Grant");
		delete_approved_grant.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete_approved_grant");
				dba_sql_command.delete_approved_grant();
			}
		});
		button_set_default_bounds(delete_approved_grant);
		add(delete_approved_grant);
		delete_approved_grant.setVisible(false);
		
		insert_open_grant = new JButton("Insert");
		insert_open_grant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_input.getText();
				String amount = amout_input.getText();
				String date_applied = date_applied_input.getText();
				String status = status_input.getText();

				if (amount.equals(DBAPanelConstants.ZERO)) {
					return;
				}


				if (!name.equals(DBAPanelConstants.NAME)) {
					dba_sql_command.insert_open_grant(name, amount, date_applied, status);
				}
			}
		});
		
//		insert_open_grant.setBounds(320, 50, 115, 29);
		button_set_default_bounds(insert_open_grant);
		add(insert_open_grant);
		insert_open_grant.setVisible(false);
		
		update_lm_educ = new JButton("Update");
		update_lm_educ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lm_id = lab_member_id_input.getText();
				String lm_educ = lab_member_educ_input.getText();

				if (lm_id.equals(DBAPanelConstants.ZERO)) {
					return;
				}

				int lm_id_int = Integer.parseInt(lm_id);

				if (!lm_educ.equals(DBAPanelConstants.LM_EDUC)) {
						dba_sql_command.update_lab_member(DBAPanelConstants.UPDATE_LAB_MEM_EDUC, lm_educ, lm_id_int);
					}
				}
		});
		button_set_default_bounds(update_lm_educ);
		add(update_lm_educ);
		
		insert_approved_grant = new JButton("Insert");
		insert_approved_grant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = name_input.getText();
				String amount = amout_input.getText();
				String projName = projectName_input.getText();
				

				if (amount.equals(DBAPanelConstants.ZERO)) {
					return;
				}

				int amount_int = Integer.parseInt(amount);

				if (!name.equals(DBAPanelConstants.NAME) && amount_int > 0 && !projName.equals(DBAPanelConstants.PROJ_NAME)) {
						dba_sql_command.insert_approved_grant(name, amount, projName);
					}
				}
		});
		
		button_set_default_bounds(insert_approved_grant);
		add(insert_approved_grant);
		
		
		update_lm_name = new JButton("Update");
		update_lm_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String lm_id = lab_member_id_input.getText();
				String lm_name = lab_member_name_input.getText();

				if (lm_id.equals(DBAPanelConstants.ZERO)) {
					return;
				}

				int lm_id_int = Integer.parseInt(lm_id);

				if (!lm_name.equals(DBAPanelConstants.LM_NAME)) {
						dba_sql_command.update_lab_member(DBAPanelConstants.UPDATE_LAB_MEM_NAME, lm_name, lm_id_int);
					}
				}
		});
		
		button_set_default_bounds(update_lm_name);
		add(update_lm_name);
		update_lm_name.setVisible(false);
		
		update_open_grant = new JButton("Update");
		update_open_grant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String amount = amout_input.getText();
				String status = status_input.getText();

				if (amount.equals(DBAPanelConstants.ZERO)) {
					return;
				}


				if (!selected_open_grant_input.isEmpty() && !status.equals(DBAPanelConstants.STATUS)) {
					dba_sql_command.update_open_grant_status(selected_open_grant_input, amount, status);
				}
			}
		});
		
		update_open_grant.setBounds(78, 450, 313, 29);
		add(update_open_grant);
		update_open_grant.setVisible(false);
	}
	
	private void construct_grant_inputs() {
		name_input = new JTextField();
		name_input.setText(DBAPanelConstants.NAME);
		name_input.setBounds(250, 235, 146, 26);
		add(name_input);
		name_input.setColumns(10);

		amout_input = new JTextField();
		amout_input.setBounds(250, 270, 146, 26);
		amout_input.setText(DBAPanelConstants.AMOUNT);
		add(amout_input);
		amout_input.setColumns(10);

		projectName_input = new JTextField();
		projectName_input.setBounds(250, 202, 146, 26);
		projectName_input.setText(DBAPanelConstants.PROJ_NAME);
		add(projectName_input);
		projectName_input.setColumns(10);
		
		status_input = new JTextField();
		status_input.setBounds(250, 305, 146, 26);
		status_input.setText(DBAPanelConstants.STATUS);
		add(status_input);
		status_input.setColumns(10);

		date_applied_input = new JTextField();
		date_applied_input.setBounds(250, 170, 146, 26);
		date_applied_input.setText(DBAPanelConstants.DATE_APP);
		add(date_applied_input);
		date_applied_input.setColumns(10);

	}
	
	private void construct_lm_inputs() {
		collaborator_id_input = new JTextField();
		collaborator_id_input.setBounds(250, 135, 146, 26);
		collaborator_id_input.setText(DBAPanelConstants.COLAB_ID);
		add(collaborator_id_input);
		collaborator_id_input.setColumns(10);
		
		
		lab_member_id_input = new JTextField();
		lab_member_id_input.setBounds(250, 135, 146, 26);
		lab_member_id_input.setText(DBAPanelConstants.LM_ID);
		add(lab_member_id_input);
		lab_member_id_input.setColumns(10);
		
		lab_member_educ_input = new JTextField();
		lab_member_educ_input.setText(DBAPanelConstants.LM_EDUC);
		lab_member_educ_input.setBounds(250, 170, 146, 26);
		add(lab_member_educ_input);
		lab_member_educ_input.setColumns(10);
		
		lab_member_name_input = new JTextField();
		lab_member_name_input.setText(DBAPanelConstants.LM_NAME);
		lab_member_name_input.setBounds(250, 202, 146, 26);
		add(lab_member_name_input);
		lab_member_name_input.setColumns(10);
	}
	
	private void hide_all_buttons() {
		collaborator_id_input.setVisible(false);
		name_input.setVisible(false);
		amout_input.setVisible(false);
		projectName_input.setVisible(false);
		status_input.setVisible(false);
		date_applied_input.setVisible(false);
		delete_colab.setVisible(false);
		insert_approved_grant.setVisible(false);
		insert_open_grant.setVisible(false);
		
		lab_member_id_input.setVisible(false);
		lab_member_educ_input.setVisible(false);
		lab_member_name_input.setVisible(false);
		scrollPane.setVisible(false);
		comboBoxOptions.setVisible(false);
		openGrantOpenBox.setVisible(false);
		lblOptions.setVisible(false);
		
		delete_approved_grant.setVisible(false);
		update_open_grant.setVisible(false);
		
		
		update_lm_name.setVisible(false);
		update_lm_educ.setVisible(false);
	}
	
	private void button_set_default_bounds(JButton btn) {
		btn.setBounds(78, 450, 313, 29);
	}

	private void showTable(String actionString, Connection con) {
		Statement stmt;
		ResultSet rs = null;
		String query = null;
		hide_all_buttons();
		switch (actionString) {
		case "Display all labs":
			scrollPane.setVisible(true);
			query = QueryBuilder.getAllLabs();
			break;
		case "Display all lab members":
			scrollPane.setVisible(true);
			comboBoxOptions.setVisible(true);
			lblOptions.setVisible(true);
			comboBoxOptions.setSelectedItem("All");
			query = QueryBuilder.getAllLabMembers();
			break;

		/** Following are DBAdmin related tasks */
			
			
			
		case "See all open grants":
			scrollPane.setVisible(true);
			query = DBAPanelConstants.getAllOpenGrants();
			break;

		case "See all approved grants":
			scrollPane.setVisible(true);
			query = DBAPanelConstants.getAllApprovedGrants();
			break;
			
		case "Update lab member name":
			lab_member_id_input.setVisible(true);
			lab_member_name_input.setVisible(true);
			update_lm_name.setVisible(true);	
			break;
		
		case "Update lab member education":
			
			lab_member_id_input.setVisible(true);
			lab_member_educ_input.setVisible(true);
			update_lm_educ.setVisible(true);
			break;
		
		case "Insert open grant":
			name_input.setVisible(true);
			amout_input.setVisible(true);
			date_applied_input.setVisible(false);
			status_input.setVisible(true);
			insert_open_grant.setVisible(true);
			break;
		
		case "Insert approved grant":
			name_input.setVisible(true);
			amout_input.setVisible(true);
			projectName_input.setVisible(true);
			
			insert_approved_grant.setVisible(true);
			break;
			
			
		case "Delete collaborator":
			delete_colab.setVisible(true);
			collaborator_id_input.setVisible(true);
			break;
			
		case "Remove all approved grants from open grants":
			delete_approved_grant.setVisible(true);
			break;
			
		case "Update specific open grant":
			openGrantOpenBox.setVisible(true);
			amout_input.setVisible(true);
			status_input.setVisible(true);
			update_open_grant.setVisible(true);
			break;
		


		/** Above are DBAdmin related tasks */

		case "Display all projects":
			scrollPane.setVisible(true);
			query = QueryBuilder.getAllProjects();
			break;
		case "":
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

		try {
			if (query != null) {
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				rs.next();
				table.setModel(DbUtils.resultSetToTableModel(rs));
				stmt.close();
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}


}
