-- For researchers:
--     We take researcher id when they login
--     We show the projects that they are working on, lab they are in, their PI, etc.
--     Show tables of all labs, all lab members, all lab managers, all researchers, all pi's.
--     When they click on a lab, we enter lab profile panel.
--     This is where we display all lab specific info (pi, researchers, projects, etc.)
--     Clicking on a project will take user to the project info panel described in the collaborator sql file.

-- getting all lab names and fields
Select *
from Lab;

-- getting all information of all lab members
Select *
from Contains_LabMember;

-- getting all information of all lab members from a particular lab
Select *
from Contains_LabMember
where labId = labId_input;

-- getting all information of all lab managers
Select *
from Contains_LabMember cl, LabManager lm
where lm.id = cl.id;

-- select lab manager for a particular lab
Select *
from Contains_LabMember cl, LabManager lm
where lm.id = cl.id and
    cl.labId = labId_input;

-- select all PI
Select *
from Contains_LabMember cl, PI pi
where pi.id = cl.id;

-- select PI of one lab
Select *
from Contains_LabMember cl, PI pi
where pi.id = cl.id and
    cl.labId = labId_input;

-- select all researchers
Select *
from Contains_LabMember cl, Researcher r
where r.id = cl.id;

-- select all researchers of one lab
Select *
from Contains_LabMember cl, Researcher r
where r.id = cl.id and
    cl.labId = labId_input;

-- select all projects (Chloe)
select *
from Project_MaterialType;

-- select Project of one lab INPUT: LAB ID (Chloe)
select *
from Project_MaterialType p,Supervises_WorksOn sw,Contains_LabMember cl
where p.name = sw.projectName and
        sw.piid = cl.id and 
        cl.labId = labId_input;

-- select all Supervises_WorksOn (Chloe)
select *
from Supervises_WorksOn;

-- Select Supervises_WorksOn for one project INPUT: PROJECT NAME (Chloe)
select *
from Supervises_WorksOn sw
where sw.projectName = projectName_input;

-- Naitong's comment: I think a more reasonable query would be the project and pi pairs.
-- The query that gets all researchers or the PI given a project is covered already.

-- select all project name and pi pairs
select sw.projectName, cl.name
from Supervises_WorksOn sw, Contains_LabMember cl
where sw.piid = cl.id;

-- Select all Assigned_Collaborators
select *
from Assigned_Collaborators;

-- Select assigned_collabs for one project INPUT: PROJECT NAME
Select aci.name, aci.education
From Assigned_Collaborators_Id aci, Name_Education_ProjectName nep
Where aci.name = nep.name and
	nep.projectName = projectName_input;

-- Select all booking records given project name
-- Would it be of much help if we show all booking records for 
-- all projects a researcher is working on? Redundancy of project names.
-- I added a project name input. This could be a part of the project info panel.
Select *
from Takes_Booking
where projectName = projectName_input;

-- Select all Makes_Booking Tables
-- I added project name input
Select *
from Makes_Booking
where projectName = projectName_input;

-- Select makes_booking of a particular takes_booking INPUT: PROJECT NAME + PARTICIPANT NUMBER
-- Essentially identifying which lab manager made the booking?
Select *
from Makes_Booking
where projectName = projectName_input and
    participantNumber =participantNumber_input;

-- Update project name
Update Project_MaterialType
set name = new_projectName_input
where name = toBeChanged_projectName_input;