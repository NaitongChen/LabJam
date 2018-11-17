package queries;
public class QueryBuilder {
	public static String collaboratorID;
	public static String researcherID;
	public static String labManagerID;
	public static String PIID;
	public static String labID;
	public static String projectName;
	
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
	
	public static void reset() {
		collaboratorID = null;
		researcherID = null;
		labManagerID = null;
		PIID = null;
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
				"where sw.piid = cl.id and pm.name = sw.projectName";
	}
}
