-- Collection of all fancy queries 

-- Query list of Subjects in each Project 
SELECT SID FROM Participates WHERE ProjectName = projectName_input;

-- Query list of available Subjects with requested startTime and Booking length 
SELECT s.id, s.name
FROM Subjects s, Participates p, Takes_Booking t
WHERE s.id = p.sid AND startTime = startTime_input AND t.length = length_input;


-- Query which grants each project has

Select name, projectName from Fund_ApprovedGrant where projectName = projectName_input;

-- Query Researchers in each Project

Select rid from Supervises_WorksOn where projectName = projectName_input;

--Query collaborators in each Project	

Select id from collaborators where projectName = projectName_input;

-- Query which grants each project has

Select name, projectName from Fund_ApprovedGrant WHERE projectName = projectName_input;

-- Count total # of projects

Select COUNT(projectName) from Supervises_WorksOn;

-- Get total $ grant received for project

Select SUM(amount), projectName 
From Fund_ApprovedGrant f
Where f.projectName = projectName_input
Group by projectName;


-- Get how much funding is left in a project after materials are purchased (assuming we only purchase once, at the start of the project)

With TOTAL_GRANT_MONEY as 
(Select SUM(amount), projectName 
From Fund_ApprovedGrant f
Where f.projectName = projectName_input
Group by projectName),

DOLLAR_SPENT as 
(select m.materialPrice
from Project_MaterialType pm, MaterialType_MaterialPrice m
where pm.projectName = projectName_input AND pm.materialType = m.materialType), 

SELECT TOTAL_GRANT_MONEY - DOLLAR_SPENT;


-- Get number of participants in each condition for a project
select participantTestCondition, projectName, COUNT(*)
from Takes_Booking
where projectName = projectName_input
group by participantTestCondition, projectName;


-- Query the expertise of researchers in a givenproject - I think by experise we meant researcherType
select researcherType
from Researcher r, Supervises_WorksOn w
where r.id = w.rid and w.projectName = projectName_input;


-- Query the number of researchers with each expertise/researchType given a project 
select count(*)
from Researcher r, Supervises_WorksOn w
where r.id = w.rid and w.projectName = projectName_input and researcherType = researcherType_input;



-- Query total number of weekly hours allocated by a given researcher 
Select sum(weeklyHoursAllocated), rid
From Supervises_WorksOn s
Where s.rid = rid_input
Group by rid_input;
