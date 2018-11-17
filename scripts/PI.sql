-- For PI:
--     We take PI id when they login
--     We show all projects in their lab, lab they are in, their lab members, etc.
--     Show tables of all labs, all lab members, all lab managers, all researchers, all pi's.
--     When they click on a lab, we enter lab profile panel.
--     This is where we display all lab specific info (pi, researchers, projects, etc.)
--     Clicking on a project will take user to the project info panel described in the collaborator sql file.

--     All queries from researcher.sql are available to PI too

-- Select All Grants
select *
from Fund_ApprovedGrant;

-- see all open grants
select *
from Applies_OpenGrant_Date;

-- see open grants that have been approved (but still open)
select *
from Applies_OpenGrant_Date
where status = "approved";

-- see open grants that have been declined
select *
from Applies_OpenGrant_Date
where status = "declined";

-- see open grants that have been applied to
select *
from Applies_OpenGrant_Date
where status = "applied";

-- see all collaborators
select * 
from Assigned_Collaborators;


-- Input is piid
-- select all grant in the PI's lab
--select fag.name,fag.amount,fag.projectName
--from Fund_ApprovedGrant fag, Supervises_WorksOn sw
--where  sw.piid=piid_input and
--        fag.projectName = sw.projectName;

-- see all grants for a given project
select *
from Fund_ApprovedGrant
where projectName = projectName_input;

-- Update Project Set projectName
Update Project_MaterialType
set name = new_projectName_input
where name = toBeChanged_projectName_input;

-- Select category from Project
select category
from Project_MaterialType;

-- update project set materialType INPUT: project name and new materialType
Update Project_MaterialType
set materialType = new_MaterialType_input
where name = toBeChanged_projectName_input;

-- insert collaborators - need to add to assigned_collaborators AND name_education_projectname
insert into Assigned_Collaborators (id, name, education)
values (id_input, name_input, education_input);

-- insert into Fund_ApprovedGrant
insert into Fund_ApprovedGrant(name,amount,projectName)
values (name_input,amout_input,projectName_input);

-- insert into Project_MaterialType
-- creating a new project
insert into Project_MaterialType(name,category,materialType)
values (name_input,category_input,materialType_input);
