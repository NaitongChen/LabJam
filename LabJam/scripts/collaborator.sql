-- For collaborators:
--     All input data is indicated by having "_input" at the end
--     The first query returns all project names that the specific collaborator is involved in
--     We could turn that into a dropdown menu and take the project that they select as the input project name
--     Then with the project name input, we return the relevant information
--	   Still liking the idea of having a panel for project info. This could be one panel and we feed project name to it

-- getting project names
Select projectName 
from Assigned_Collaborators_Id aci, Name_Education_ProjectName nep
where aci.name = nep.name and
	aci.id = collaboratorId_input;

-- getting project name, category, material type and material price
Select name, category, p.materialType, materialPrice
From Project_MaterialType p, MaterialType_MaterialPrice m
where p.materialType = m.materialType and
	name = projectName_input;

-- get name of PI of project, PI’s field of expertise, lab the project is under, and the lab’s field
Select cl.name, pi.fieldOfExpertise, lab.name, lab.Field
From Supervises_WorksOn sw, PI pi, Contains_LabMember cl, Lab lab
Where sw.piid = pi.id and
	pi.id = cl.id and
	cl.labId = lab.id and
sw.projectName = projectName_input;

-- get researcher name, type, and hours allocated for the projects
Select cl.name, r.researcherType
From Supervises_WorksOn sw, Contains_LabMember cl, Researcher r, Role_WeeklyHoursAllocated rw
Where sw.projectName = projectName_input and
	sw.rid = r.id and
	r.researcherType = rw.weeklyHoursAllocated and
	r.id = cl.id;

-- get names of other collaborators for the project
Select aci.name, aci.education
From Assigned_Collaborators_Id aci, Name_Education_ProjectName nep
Where aci.name = nep.name and
	nep.projectName = projectName_input;
