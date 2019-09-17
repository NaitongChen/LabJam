package panels;

public class DBAPanelConstants {
	public static final String NAME = "Name";
	public static final String AMOUNT = "Amount";
	public static final String PROJ_NAME = "Project Name";
	public static final String STATUS = "Status";
	public static final String DATE_APP = "Date applied";
	public static final String ZERO = "0";
	public static final String LM_ID = "LM ID";
	public static final String LM_EDUC = "LM Education";
	public static final String LM_NAME = "LM Name";
	public static final String COLAB_ID = "COLAB ID";
	public static final String UPDATE_LAB_MEM_NAME = "lab_change_name";
	public static final String UPDATE_LAB_MEM_EDUC = "lab_change_educ";
	
/** Below are DBAdmin related tasks */
	
	public static String getAllOpenGrants() {
		return "select * from Applies_OpenGrant_Date";
	}
	
	public static String getAllApprovedGrants() {
		return "select * from Fund_ApprovedGrant";
	}
	
	public static String updateLabMemName()
	{
		return "update Contains_LabMember\r\n" + 
				"set name = ?" + 
				"where id = ?";
	}
	
	public static String updateLabMemEduc()
	{
		return "update Contains_LabMember set education = ? where id = ?";
	}
	
	public static String deleteCollaborator() {
		return "delete from Assigned_Collaborators_Id\r\n" + 
				"where id = ?";
	}

	public static String deleteApprovedGrant() {
		return "delete from Applies_OpenGrant_Date	where status = 'approved'";
	}
	
	public static String approveSpecificGrant() {
		return "delete from Applies_OpenGrant_Date	where  = ?";
	}
}

