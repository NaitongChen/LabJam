-- untested fancy queries claimed from the google doc

-- query list of available Subjects with requested dateParticipated, startTime and Booking length - times can't overlap with existing bookings and need to be within established working hours: ONLY LAB MANAGERS
select *
from Subjects s
where availability = 'Y' AND s.id NOT IN (select p.id
                                         from Participates p, Takes_Booking b
                                         where b.projectName = projectName_input AND p.dateParticipated = dateParticipated_input AND (startTime_input <= p.startTime <= (startTime_input + b.length))
                                         ) AND startTime_input + length_input <= 1800;
                                         
-- get how much funding is left in a project after materials are purchased (assuming we only purchase once, at the start of the project)
(TOTAL_GRANT_MONEY - select m.materialPrice
                    from Project_MaterialType pm, MaterialType_MaterialPrice m
                    where pm.projectName = projectName_input AND pm.materialType = m.materialType);

-- get number of participants in each condition for a project
select participantTestCondition, COUNT(*)
from Takes_Booking
group by participantTestCondition;

-- query the expertise/researcherType of researchers in a given project
select researcherType
from Researcher r, Supervises_WorksOn w
where r.id = w.rid, w.projectName = projectName_input;
                                          
-- query the number of researchers with a specific expertise given a project
select researcherType, SUM(*)
from Researcher r, Supervises_WorksOn w
group by researcherType
having r.id = w.rid, w.projectName = projectName_input;
