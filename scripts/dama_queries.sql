-- untested fancy queries claimed from the google doc

-- query list of available Subjects with requested dateParticipated, startTime and Booking length: ONLY LAB MANAGERS
select *
from Subjects s
where availability = 'Y' AND s.id NOT IN (select p.id
                                         from Participates p, Takes_Booking b
                                         where p.dateParticipated = dateParticipated_input AND startTime_input <= p.startTime <= (startTime_input + b.length));
                                         
-- get how much funding is left in a project after materials are purchased (assuming we only purchase once, at the start of the project)
(TOTAL_GRANT_MONEY - select m.materialPrice
                    from Project_MaterialType pm, MaterialType_MaterialPrice m
                    where pm.projectName = projectName_input AND pm.materialType = m.materialType;)

-- get number of participants in each condition for a project
select participantTestCondition, COUNT(*)
from Takes_Booking
group by participantTestCondition;

-- query the expertise of researchers in each project

                                          
-- query the number of researchers with a specific expertise given a project
