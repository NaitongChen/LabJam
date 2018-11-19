package queries;
import java.sql.Date;

public class QueryBuilder {
	public static String collaboratorID;
	public static String researcherID;
	public static String labManagerID;
	public static String PIID;
	public static String labID;
	public static String projectName;
	public static boolean purchased = false;
	public static float totalFunding = 0;
	
	public static void setCollaboratorID(String cID) {
		collaboratorID = cID;
	}
	
	public static void setResearcherID(String rID) {
		researcherID = rID;
	}
	
	public static void setLabManagerID(String lmID) {
		labManagerID = lmID;
	}
	
	public static void setPIID(String piID) {
		PIID = piID;
	}
	
	public static void setLabID(String lID) {
		labID = lID;
	}
	
	public static void setProjectName(String pName) {
		projectName = pName;
	}
	
	public static void setTotalFunding(float funding) {
		totalFunding = funding;
	}
	
	public static void reset() {
		collaboratorID = null;
		researcherID = null;
		labManagerID = null;
		PIID = null;
		labID = null;
		projectName = null;
		purchased = false;
		totalFunding = 0;
	}
	
	public static void resetProject() {
		projectName = null;
		purchased = false;
		totalFunding = 0;
	}
	
	public static String getResearcherName() {
		return "Select name from Contains_LabMember cl, Researcher r " + 
				"where r.id = cl.id and r.id = '" + 
				researcherID + "'";
	}
	
	public static String getAllLabs() {
		return "Select * from Lab order by field";
	}
	
	public static String getAllLabMembers() {
		return "Select * from Contains_LabMember order by education";
	}

	public static String getAllResearchers() {
		return "Select * from Contains_LabMember cl, Researcher r where r.id = cl.id " +
				"order by researchertype";
	}

	public static String getAllLabManagers() {
		return "Select * from Contains_LabMember cl, LabManager lm where lm.id = cl.id " +
				"order by employmenttype";
	}

	public static String getAllPIs() {
		return "Select * from Contains_LabMember cl, PI pi where pi.id = cl.id " +
				"order by fieldofexpertise";
	}

	public static String getAllProjects() {
		return "select unique sw.projectName, pm.category, cl.name as PI " +
				"from Supervises_WorksOn sw, Contains_LabMember cl, Project_MaterialType pm " +
				"where sw.piid = cl.id and pm.name = sw.projectName order by pm.category";
	}
	
	public static String getPIName() {
		return "select cl.name from Supervises_WorksOn sw, Contains_LabMember cl " + 
				"where sw.piid = cl.id and sw.projectName = '" + projectName + "'";
	}
	
	public static String getLabName() {
		return "select unique l.name from Supervises_WorksOn sw, Contains_LabMember cl, Lab l " +
				"where sw.piid = cl.id and sw.projectName = '" + projectName + "' and cl.labId = l.id";
	}
	
	public static String getLabNameByLabID() {
		return "select l.name from Lab l " +
				"where l.id = '" + labID + "'";
	}
	
	public static String getPINameByLabID() {
		return "select cl.name from Lab l, Contains_LabMember cl, PI p " + 
				"where p.id = cl.id and cl.labId = l.id and l.id = '" + labID + "'";
	}
	
	public static String getAllResearchersForProject() {
		return "select * from researcher r, Supervises_WorksOn sw " + 
				"where sw.rid = r.id and projectName = '" + projectName + "' " + 
				"order by r.researcherType";
	}
	
	public static String countResearchersForProject() {
		return "select CAST(count(*) as INT) as count from researcher r, Supervises_WorksOn sw " + 
				"where sw.rid = r.id and projectName = '" + projectName + "'";
	}

	public static String getAllCollaborators() {
		return "select aci.name, aci.education " +
				"From Assigned_Collaborators_Id aci, Name_Education_ProjectName nep " +
				"Where aci.name = nep.name and nep.projectName = '" + projectName + "' " +
				"order by aci.education";
	}
	
	public static String getMaterial() {
		return "select * from Project_MaterialType pm, MaterialType_MaterialPrice mm " +
				"where pm.materialType = mm.materialType and " +
				"pm.name = '" + projectName + "'";
	}
	
	public static String getAllBookings() {
		return "Select * from Takes_Booking where projectName = '" + projectName + "' " +
				"order by participanttestcondition";
	}
	
	public static String countControl() {
		return "Select cast(count(*) as int) as count from Takes_Booking where projectName = '" + 
				projectName + "' and " + "participanttestcondition LIKE '%Control%'";
	}
	
	public static String countExperimental() {
		return "Select cast(count(*) as int) as count from Takes_Booking where projectName = '" + 
				projectName + "' and " + "participanttestcondition LIKE '%Experimental Group%'";
	}
	
	public static String calculateTotalFunding() {
		return "Select cast(SUM(amount) as float) as total " +
				"From Fund_ApprovedGrant f " +
				"Where f.projectName = '" + projectName + "'";
	}
	
	public static String calculateRemainingFunding() {
		return "Select projectName, (pf.total - pm.price) as remaining " + 
				"from ProjectName_Funding pf, ProjectName_MaterialPrice pm " + 
				"where pf.projectName = pm.name and pm.name = '" + projectName + "'";
	}
	
	public static String calculateWeeklyHours() {
		return "Select cast(sum(weeklyHoursAllocated) as int) as total " +
				"From Supervises_WorksOn sw, Role_WeeklyHoursAllocated rw " +
				"Where sw.rid = '" + researcherID + "' and rw.role = sw.role " +
				"Group by sw.rid";
	}
	
	public static String getAllGrants() {
		return "Select * " +
				"From Fund_ApprovedGrant f " +
				"Where f.projectName = '" + projectName + "'";
	}

	public static String countNumProject() {
		return "select cast(count(*) as int) as count from Project_MaterialType p,Supervises_WorksOn sw, " + 
				"Contains_LabMember cl where p.name = sw.projectName and sw.piid = cl.id and "
				+ "cl.labId = '" + labID + "'";
	}

	public static String getProjectsGivenLab() {
		return "select * from Project_MaterialType p,Supervises_WorksOn sw, " + 
				"Contains_LabMember cl where p.name = sw.projectName and sw.piid = cl.id and "
				+ "cl.labId = '" + labID + "'";
	}
	
	public static String getPIName_simple() {
		return "Select cl.name from Contains_LabMember cl, PI p , Supervises_WorksOn sw " + 
					"where p.id = cl.id and p.id = '" + 
					PIID + "'";
	}
	
	public static String getLabManagerName() {
		return "Select name from Contains_LabMember cl, LabManager lm " + 
				"where lm.id = cl.id and lm.id = '" + 
				labManagerID + "'";
	}
	
	public static String getAllOpenGrants() {
		return "Select * from Applies_OpenGrant_Date " +
				"order by status";
	}
	
	public static String getApprovedOpenGrants() {
		return "Select * from Applies_OpenGrant_Date where status = 'approved'";
	}

	public static String getRejectedOpenGrants() {
		return "Select * from Applies_OpenGrant_Date where status = 'rejected'";
	}

	public static String getAppliedOpenGrants() {
		return "Select * from Applies_OpenGrant_Date where status = 'applied'";
	}
	
	public static String getGrantsForProject(String projectName_input) {
		return "select unique name, amount " +
				"from Fund_ApprovedGrant fa " +
				"where fa.projectName = '" + 
				projectName_input + "'";
	}

	public static String getAllSubjects() {
		return "Select * from Subject order by id";
	}

	public static String getAllParticipants() {
		return "Select * from Participates order by sid";
	}
	
	public static String getAllProjectNames() {
		return "select unique sw.projectName " +
				"from Supervises_WorksOn sw, Contains_LabMember cl, Project_MaterialType pm " +
				"where sw.piid = cl.id and pm.name = sw.projectName";
	}

	public static String getCollaboratorProjects() {
		return "Select projectName as \"Project Name\"" +
				"from Assigned_Collaborators_Id aci, Name_Education_ProjectName nep " +
				"where aci.name = nep.name and aci.id ='" + 
				collaboratorID + "'";
	}

	public static String getAvailableSubjects(String projectName_input, Date dateParticipated_input, Integer startTime_input, Integer length_input) {
		return "select * from Subject s " +
		"where availability = 'Y' AND s.id NOT IN (" +
		getOverLapSubjects(projectName_input, dateParticipated_input, startTime_input)
		+ ")"; 
		
	}
	public static String getOverLapSubjects(String projectName_input, Date dateParticipated_input, Integer startTime_input) {
		return "select p.sid from Participates p, Takes_Booking b " +
		"where b.projectName = '" + projectName_input + "' AND p.dateParticipated = " +
		dateParticipated_input + " AND (" + startTime_input + " <= p.startTime AND p.startTime <= (" + startTime_input + "+ b.length))";
	}
}
