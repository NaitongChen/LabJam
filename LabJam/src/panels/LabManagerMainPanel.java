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

import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.text.ParseException;

public class LabManagerMainPanel extends JPanel {
	private JLabel lblNewLabel;
	private JTable table;
	private JComboBox<String> comboBox;
	private JScrollPane scrollPane;
	private JComboBox<String> comboBoxOptions;
	private JComboBox<String> comboBoxProjects;
	private JLabel lblOptions;
	private JLabel lblProjects;
	private JLabel lblSid;
	private JTextField sid_field;
	private JLabel lblSubName;
	private JTextField subname_field;
	private JLabel lblAvailability;
	private JTextField availability_field;
	private JButton btnInsertSubjects;
	private JLabel lblCid;
	private JTextField cid_field;
	private JLabel lblCollabName;
	private JTextField collabname_field;
	private JLabel lblCollabEdu;
	private JTextField collabedu_field;
	private JButton btnInsertCollaborators;
	private JLabel lblPartNumber;
	private JTextField partnumber_field;
	private JLabel lblLength;
	private JTextField length_field;
	private JLabel lblTestCondition;
	private JTextField testcondition_field;
	private JButton btnInsertBookings;
	private JLabel lblPartDate;
	private JTextField partdate_field;
	private JLabel lblStartTime;
	private JTextField starttime_field;
	private JLabel lblSid2;
	private JTextField sid2_field;
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
				        LabProfilePanel labProfilePanel = new LabProfilePanel(cl, mainPanel, con, Constant.LABMANAGERMAIN);
				        cl.show(mainPanel, Constant.LAB);
			        } else if (((String)comboBox.getSelectedItem()).equals("Display all projects")) {
			        	String selectedData = null;
				        int[] selectedRow = table.getSelectedRows();
				        selectedData = (String) table.getValueAt(selectedRow[0], 0);
				        selectedData = selectedData.trim();
				        QueryBuilder.setProjectName(selectedData);
				        ProjectProfilePanel projectProfilePanel = new ProjectProfilePanel(cl, mainPanel, con, Constant.LABMANAGERMAIN);
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
		comboBox.addItem("Display all subjects");
		comboBox.addItem("Display all subjects participating in projects");
		comboBox.addItem("Insert subjects");
		comboBox.addItem("Insert collaborators");
		comboBox.addItem("Insert bookings");
		
		
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

		comboBoxProjects = new JComboBox<String>();
		populateProjectBox(con);

		comboBoxProjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String actionString = (String)comboBox.getSelectedItem();
				showTable(actionString, con);
			}
		});

		comboBoxProjects.setBounds(15, 495, 290, 30);
		add(comboBoxProjects);
		
		JLabel lblSelectAnOperation = new JLabel("Select an operation...");
		lblSelectAnOperation.setFont(new Font("Arial", Font.PLAIN, 20));
		lblSelectAnOperation.setBounds(15, 50, 290, 30);
		add(lblSelectAnOperation);
		
		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOptions.setBounds(15, 460, 69, 30);
		add(lblOptions);

		lblProjects = new JLabel("Projects");
		lblProjects.setFont(new Font("Arial", Font.PLAIN, 20));
		lblProjects.setBounds(15, 460, 100, 30);
		add(lblProjects);

		//fields for insertSubjects

		lblSid = new JLabel("Subject ID");
		lblSid.setFont(new Font("Arial", Font.PLAIN, 12));
		lblSid.setBounds(78, 160, 290, 30);
		add(lblSid);

		sid_field = new JTextField();
		sid_field.setBounds(78, 182, 146, 26);
		add(sid_field);
		sid_field.setColumns(10);

		lblSubName = new JLabel("Name");
		lblSubName.setFont(new Font("Arial", Font.PLAIN, 12));
		lblSubName.setBounds(78, 202, 290, 30);
		add(lblSubName);
		
		subname_field = new JTextField();
		subname_field.setBounds(78, 225, 146, 26);
		add(subname_field);
		subname_field.setColumns(10);

		lblAvailability = new JLabel("Availability");
		lblAvailability.setFont(new Font("Arial", Font.PLAIN, 12));
		lblAvailability.setBounds(78, 247, 290, 30);
		add(lblAvailability);
		
		availability_field = new JTextField();
		availability_field.setBounds(78, 270, 146, 26);
		add(availability_field);
		availability_field.setColumns(10);
		
		btnInsertSubjects = new JButton("Insert Subject");
		btnInsertSubjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sid = sid_field.getText();
				String name = subname_field.getText();
				String availability = availability_field.getText();
				if (!sid.isEmpty() && !name.isEmpty() && !availability.isEmpty()) {
					insertSubjects(con, sid, name, availability);
				}
			}
		});
		btnInsertSubjects.setBounds(78, 315, 146, 29);
		add(btnInsertSubjects);

		//fields for insertCollaborators - need to add to assigned_collaborators AND name_education_projectname
		
		lblCid = new JLabel("Collaborator ID");
		lblCid.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCid.setBounds(78, 160, 290, 30);
		add(lblCid);
		
		cid_field = new JTextField();
		cid_field.setBounds(78, 182, 146, 26);
		add(cid_field);
		cid_field.setColumns(10);

		lblCollabName = new JLabel("Name");
		lblCollabName.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCollabName.setBounds(78, 202, 290, 30);
		add(lblCollabName);
		
		collabname_field = new JTextField();
		collabname_field.setBounds(78, 225, 146, 26);
		add(collabname_field);
		collabname_field.setColumns(10);

		lblCollabEdu = new JLabel("Education");
		lblCollabEdu.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCollabEdu.setBounds(78, 247, 290, 30);
		add(lblCollabEdu);
		
		collabedu_field = new JTextField();
		collabedu_field.setBounds(78, 270, 146, 26);
		add(collabedu_field);
		collabedu_field.setColumns(10);
		
		btnInsertCollaborators = new JButton("Insert Collaborator");
		btnInsertCollaborators.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cid = cid_field.getText();
				String name = collabname_field.getText();
				String education = collabedu_field.getText();
				String projectName = (String) comboBoxProjects.getSelectedItem();
				if (!cid.isEmpty() && !name.isEmpty() && !education.isEmpty() && !projectName.isEmpty()) {
					insertAssignedCollaborators(con, cid, name, education);
					insertNameEduProjName(con, name, education, projectName);
				}
			}
		});
		btnInsertCollaborators.setBounds(78, 315, 146, 29);
		add(btnInsertCollaborators);

		//fields for insertBooking
		
		lblPartNumber = new JLabel("Participant Number");
		lblPartNumber.setFont(new Font("Arial", Font.PLAIN, 12));
		lblPartNumber.setBounds(78, 160, 290, 30);
		add(lblPartNumber);
		
		partnumber_field = new JTextField();
		partnumber_field.setBounds(78, 182, 146, 26);
		add(partnumber_field);
		partnumber_field.setColumns(10);

		lblLength = new JLabel("Booking Length (Hours)");
		lblLength.setFont(new Font("Arial", Font.PLAIN, 12));
		lblLength.setBounds(78, 202, 290, 30);
		add(lblLength);
		
		length_field = new JTextField();
		length_field.setBounds(78, 225, 146, 26);
		add(length_field);
		length_field.setColumns(10);

		lblTestCondition = new JLabel("Test Condition");
		lblTestCondition.setFont(new Font("Arial", Font.PLAIN, 12));
		lblTestCondition.setBounds(78, 247, 290, 30);
		add(lblTestCondition);
		
		testcondition_field = new JTextField();
		testcondition_field.setBounds(78, 270, 146, 26);
		add(testcondition_field);
		testcondition_field.setColumns(10);

		lblPartDate = new JLabel("Participation Date (DD/MM/YYYY)");
		lblPartDate.setFont(new Font("Arial", Font.PLAIN, 12));
		lblPartDate.setBounds(78, 292, 290, 30);
		add(lblPartDate);
		
		partdate_field = new JTextField();
		partdate_field.setBounds(78, 313, 146, 26);
		add(partdate_field);
		partdate_field.setColumns(10);

		lblStartTime = new JLabel("Start Time");
		lblStartTime.setFont(new Font("Arial", Font.PLAIN, 12));
		lblStartTime.setBounds(78, 335, 290, 30);
		add(lblStartTime);
		
		starttime_field = new JTextField();
		starttime_field.setBounds(78, 358, 146, 26);
		add(starttime_field);
		starttime_field.setColumns(10);

		lblSid2 = new JLabel("Subject ID");
		lblSid2.setFont(new Font("Arial", Font.PLAIN, 12));
		lblSid2.setBounds(78, 380, 290, 30);
		add(lblSid2);
		
		sid2_field = new JTextField();
		sid2_field.setBounds(78, 403, 146, 26);
		add(sid2_field);
		sid2_field.setColumns(10);
		
		btnInsertBookings = new JButton("Insert Booking");
		btnInsertBookings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String partNumber = partnumber_field.getText();
				String length = length_field.getText();
				String testCondition = testcondition_field.getText();
				String partDate = partdate_field.getText();
				String startTime = starttime_field.getText();
				String sid = sid2_field.getText();
				String projectName = (String) comboBoxProjects.getSelectedItem();
				if (!partNumber.isEmpty() && !length.isEmpty() && !testCondition.isEmpty() && !projectName.isEmpty()){
					insertTakesBooking(con, partNumber, length, testCondition, projectName);
				} 
				if (!partNumber.isEmpty() && !projectName.isEmpty()) {
					String lmid = QueryBuilder.labManagerID;
					insertMakesBooking(con, partNumber, projectName, lmid);
				}
				if (!partNumber.isEmpty() && !partDate.isEmpty() && !startTime.isEmpty() && !sid.isEmpty() && !projectName.isEmpty()) {
					insertParticipates(con, partNumber, partDate, startTime, sid, projectName);
				}
			}
		});
		btnInsertBookings.setBounds(78, 448, 146, 29);
		add(btnInsertBookings);

		scrollPane.setVisible(false);
		comboBoxOptions.setVisible(false);
		comboBoxProjects.setVisible(false);
		lblOptions.setVisible(false);
		lblProjects.setVisible(false);
		btnInsertSubjects.setVisible(false);
		availability_field.setVisible(false);
		subname_field.setVisible(false);
		sid_field.setVisible(false);
		btnInsertCollaborators.setVisible(false);
		cid_field.setVisible(false);
		collabname_field.setVisible(false);
		collabedu_field.setVisible(false);
		lblAvailability.setVisible(false);
		lblSubName.setVisible(false);
		lblSid.setVisible(false);
		lblCid.setVisible(false);
		lblCollabName.setVisible(false);
		lblCollabEdu.setVisible(false);
		lblPartNumber.setVisible(false);
		partnumber_field.setVisible(false);
		lblLength.setVisible(false);
		length_field.setVisible(false);
		lblTestCondition.setVisible(false);
		testcondition_field.setVisible(false);
		btnInsertBookings.setVisible(false);
		lblPartDate.setVisible(false);
		partdate_field.setVisible(false);
		lblStartTime.setVisible(false);
		starttime_field.setVisible(false);
		lblSid2.setVisible(false);
		sid2_field.setVisible(false);
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
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllLabs();
				break;
			case "Display all lab members":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				comboBoxOptions.setSelectedItem("All");
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllLabMembers();
				break;
			case "Display all projects":
				table.clearSelection();
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllProjects();
				break;
				
			case "Display all open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllOpenGrants();
				break;

			case "Display approved open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getApprovedOpenGrants();
				break;

			case "Display rejected open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getRejectedOpenGrants();
				break;
			
			case "Display applied open grants":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAppliedOpenGrants();
				break;

			case "Display grants for given project":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(true);
				lblOptions.setVisible(false);
				lblProjects.setVisible(true);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getGrantsForProject((String)comboBoxProjects.getSelectedItem());
				break;
			
			case "Display all subjects":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllSubjects();
				break;

			case "Display all subjects participating in projects":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllParticipants();
				break;
			
			case "Insert subjects":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(true);
				availability_field.setVisible(true);
				subname_field.setVisible(true);
				sid_field.setVisible(true);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(true);
				lblSubName.setVisible(true);
				lblSid.setVisible(true);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				break;
			
			case "Insert collaborators":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(true);
				lblOptions.setVisible(false);
				lblProjects.setVisible(true);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(true);
				cid_field.setVisible(true);
				collabname_field.setVisible(true);
				collabedu_field.setVisible(true);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(true);
				lblCollabName.setVisible(true);
				lblCollabEdu.setVisible(true);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				break;

			case "Insert bookings":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(true);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(true);
				partnumber_field.setVisible(true);
				lblLength.setVisible(true);
				length_field.setVisible(true);
				lblTestCondition.setVisible(true);
				testcondition_field.setVisible(true);
				btnInsertBookings.setVisible(true);
				lblPartDate.setVisible(true);
				partdate_field.setVisible(true);
				lblStartTime.setVisible(true);
				starttime_field.setVisible(true);
				lblSid2.setVisible(true);
				sid2_field.setVisible(true);
				break;

			case "":
				scrollPane.setVisible(false);
				comboBoxOptions.setVisible(false);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(false);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				break;

			case "Researcher":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllResearchers();
				break;

			case "All":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllLabMembers();
				break;

			case "Lab Manager":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
				query = QueryBuilder.getAllLabManagers();
				break;

			case "PI":
				scrollPane.setVisible(true);
				comboBoxOptions.setVisible(true);
				comboBoxProjects.setVisible(false);
				lblOptions.setVisible(true);
				lblProjects.setVisible(false);
				btnInsertSubjects.setVisible(false);
				availability_field.setVisible(false);
				subname_field.setVisible(false);
				sid_field.setVisible(false);
				btnInsertCollaborators.setVisible(false);
				cid_field.setVisible(false);
				collabname_field.setVisible(false);
				collabedu_field.setVisible(false);
				lblAvailability.setVisible(false);
				lblSubName.setVisible(false);
				lblSid.setVisible(false);
				lblCid.setVisible(false);
				lblCollabName.setVisible(false);
				lblCollabEdu.setVisible(false);
				lblPartNumber.setVisible(false);
				partnumber_field.setVisible(false);
				lblLength.setVisible(false);
				length_field.setVisible(false);
				lblTestCondition.setVisible(false);
				testcondition_field.setVisible(false);
				btnInsertBookings.setVisible(false);
				lblPartDate.setVisible(false);
				partdate_field.setVisible(false);
				lblStartTime.setVisible(false);
				starttime_field.setVisible(false);
				lblSid2.setVisible(false);
				sid2_field.setVisible(false);
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

	private void insertSubjects(Connection con, String sid, String name, String availability) {
		PreparedStatement ps;

		try{
			ps = con.prepareStatement("insert into Subject VALUES (?,?,?)");
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

	private void insertAssignedCollaborators(Connection con, String cid, String name, String education) {
		PreparedStatement ps;

		try{
			ps = con.prepareStatement("insert into Assigned_Collaborators_Id VALUES (?,?,?)");
			ps.setString(1, cid);
		    ps.setString(2, name);
		    ps.setString(3, education);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertNameEduProjName(Connection con, String name, String education, String projectName) {
		PreparedStatement ps;

		try{
			ps = con.prepareStatement("insert into Name_Education_ProjectName VALUES (?,?,?)");
			ps.setString(1, name);
		    ps.setString(2, education);
		    ps.setString(3, projectName);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertTakesBooking(Connection con, String partNumber, String length, String testCondition, String projectName) {
		PreparedStatement ps;
		Integer length_int = Integer.parseInt(length) * 100;
		Integer partNumber_int = Integer.parseInt(partNumber);

		try{
			ps = con.prepareStatement("insert into Takes_Booking VALUES (?,?,?,?)");
			ps.setString(1, projectName);
		    ps.setInt(2, partNumber_int);
		    ps.setInt(3, length_int);
			ps.setString(4, testCondition);

		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertMakesBooking(Connection con, String partNumber, String projectName, String lmid) {
		PreparedStatement ps;
		Integer partNumber_int = Integer.parseInt(partNumber);

		try{
			ps = con.prepareStatement("insert into Makes_Booking VALUES (?,?,?)");
			ps.setString(1, projectName);
		    ps.setInt(2, partNumber_int);
		    ps.setString(3, lmid);
		    
		    ps.executeUpdate();

		    // commit work 
		    con.commit();

		    ps.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void insertParticipates(Connection con, String partNumber, String partDate, String startTime, String sid, String projectName) {
		PreparedStatement ps;
		Integer partNumber_int = Integer.parseInt(partNumber);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
		try{
			Date partDate_dateJava = formatter.parse(partDate);
			Instant instant = partDate_dateJava.toInstant();
			ZoneId zoneId = ZoneId.of ( "America/Montreal" );
			ZonedDateTime zdt = ZonedDateTime.ofInstant (instant , zoneId);
			LocalDate partDate_localDate = zdt.toLocalDate();
			java.sql.Date partDate_dateSQL = java.sql.Date.valueOf(partDate_localDate);
			
			try{
				ps = con.prepareStatement("insert into Participates VALUES (?,?,?,?,?)");
				ps.setString(1, sid);
				ps.setString(2, projectName);
				ps.setInt(3, partNumber_int);
				ps.setDate(4, partDate_dateSQL);
				ps.setString(5, startTime);
	
				ps.executeUpdate();
	
				// commit work 
				con.commit();
	
				ps.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (ParseException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}
	
}
