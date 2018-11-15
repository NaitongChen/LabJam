-- For lab managers:
--     We take lab manager id when they login
--     We show all projects in their lab, lab they are in, their PI, etc.
--     Show tables of all labs, all lab members, all lab managers, all researchers, all pi's.
--     When they click on a lab, we enter lab profile panel.
--     This is where we display all lab specific info (pi, researchers, projects, etc.)
--     Clicking on a project will take user to the project info panel described in the collaborator sql file.

--     All queries from researcher.sql are available to lab manager too

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

-- see all grants for a given project
select * 
from Fund_ApprovedGrant
where projectName = projectName_input;

-- insert subjects
insert into Subjects (id, name, availability)
values (id_input, name_input, availability_input);

-- update subject name
update Subjects
set name = name_input
where id = id_input;

-- update subject availability
update Subjects
set availability - availability_input
where id = id_input;

-- insert collaborators - need to add to assigned_collaborators AND name_education_projectname
insert into Assigned_Collaborators (id, name, education)
values (id_input, name_input, education_input);

insert into Name_Education_ProjectName (name, education, projectName)
values (name_input, education_input, projectName_input);

-- insert bookings (need a project associated w/ them) - whenever a booking is inserted, need to insert a Participates tuple
insert into Takes_Booking (projectName, participantNumber, length, participantTestCondition)
values (projectName_input, participantNumber_input, length_input, participantTestCondition_input);

insert into Participates (sid, projectName, participantNumber, dateParticipated, startTime)
values (sid_input, projectName_input, participantNumber_input, dateParticipated_input, startTime_input);

-- insert into makes_booking - projName and partNumber should come from the query above ^^ lmid should be lmid of Lab Manager that ran the query above ^^
insert into Makes_Booking (projectName, participantNumber, lmid)
values (projectName_input, participantNumber_input, lmid);

-- update booking - only length and participantTestCondition are updateable
update Takes_Booking
set length = length_input, participantTestCondition = participantTestCondition_input
where projectName = projectName_input and participantNumber = participantNumber_input;

-- update open grant status - should only be changed to applied, approved, declined, not applied
update Applies_OpenGrant_Date
set status = status_input
where name = name_input and amount = amount_input;

-- see all subjects
select * 
from Subjects;

-- see all subjects participating in projects
select *
from Participates;
