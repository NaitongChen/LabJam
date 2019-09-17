package panels;

import java.util.ArrayList;

import javax.swing.JTextField;

public class DBAMainPanelData {
	private JTextField name_input;
	private JTextField amout_input;
	private JTextField projectName_input;
	private JTextField date_applied_input;
	private JTextField status_input;
	private JTextField lab_member_id_input;
	private JTextField lab_member_educ_input;
	private JTextField lab_member_name_input;
	private JTextField collaborator_id_input;
	static ArrayList<JTextField> sql_command_inputs = new ArrayList<JTextField>();
 
	public DBAMainPanelData() {
		construct_lm_inputs();
		construct_grant_inputs();
		
	}
	
	public static ArrayList<JTextField> get_sql_command_inputs() {
		return sql_command_inputs;
	}
	
	private void construct_lm_inputs() {
		collaborator_id_input = new JTextField();
		collaborator_id_input.setBounds(250, 135, 146, 26);
		collaborator_id_input.setText(DBAPanelConstants.COLAB_ID);
		sql_command_inputs.add(collaborator_id_input);
		collaborator_id_input.setColumns(10);
		
		
		lab_member_id_input = new JTextField();
		lab_member_id_input.setBounds(250, 135, 146, 26);
		lab_member_id_input.setText(DBAPanelConstants.LM_ID);
		sql_command_inputs.add(lab_member_id_input);
		lab_member_id_input.setColumns(10);
		
		lab_member_educ_input = new JTextField();
		lab_member_educ_input.setText(DBAPanelConstants.LM_EDUC);
		lab_member_educ_input.setBounds(250, 170, 146, 26);
		sql_command_inputs.add(lab_member_educ_input);
		lab_member_educ_input.setColumns(10);
		
		lab_member_name_input = new JTextField();
		lab_member_name_input.setText(DBAPanelConstants.LM_NAME);
		lab_member_name_input.setBounds(250, 202, 146, 26);
		sql_command_inputs.add(lab_member_name_input);
		lab_member_name_input.setColumns(10);
	}
	
	private void construct_grant_inputs() {
		name_input = new JTextField();
		name_input.setText(DBAPanelConstants.NAME);
		name_input.setBounds(250, 235, 146, 26);
		sql_command_inputs.add(name_input);
		name_input.setColumns(10);

		amout_input = new JTextField();
		amout_input.setBounds(250, 270, 146, 26);
		amout_input.setText(DBAPanelConstants.AMOUNT);
		sql_command_inputs.add(amout_input);
		amout_input.setColumns(10);

		projectName_input = new JTextField();
		projectName_input.setBounds(250, 202, 146, 26);
		projectName_input.setText(DBAPanelConstants.PROJ_NAME);
		sql_command_inputs.add(projectName_input);
		projectName_input.setColumns(10);
		
		status_input = new JTextField();
		status_input.setBounds(250, 305, 146, 26);
		status_input.setText(DBAPanelConstants.STATUS);
		sql_command_inputs.add(status_input);
		status_input.setColumns(10);

		date_applied_input = new JTextField();
		date_applied_input.setBounds(250, 170, 146, 26);
		date_applied_input.setText(DBAPanelConstants.DATE_APP);
		sql_command_inputs.add(date_applied_input);
		date_applied_input.setColumns(10);

	}

	public JTextField getName_input() {
		return name_input;
	}

	public JTextField getAmout_input() {
		return amout_input;
	}

	public JTextField getProjectName_input() {
		return projectName_input;
	}

	public JTextField getDate_applied_input() {
		return date_applied_input;
	}

	public JTextField getStatus_input() {
		return status_input;
	}

	public JTextField getLab_member_id_input() {
		return lab_member_id_input;
	}

	public JTextField getLab_member_educ_input() {
		return lab_member_educ_input;
	}

	public JTextField getLab_member_name_input() {
		return lab_member_name_input;
	}

	public JTextField getCollaborator_id_input() {
		return collaborator_id_input;
	}


}