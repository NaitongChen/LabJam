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
from Project_MaterialType p;

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
