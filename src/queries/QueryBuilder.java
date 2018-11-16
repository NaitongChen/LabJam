package queries;
public class QueryBuilder {
	public static String collaboratorID;
	public static String researcherID;
	public static String labManagerID;
	public static String PIID;
	
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
}
