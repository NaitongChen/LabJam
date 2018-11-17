-- Select * from open Grants
Select * from Applies_OpenGrant_Date;

-- Select * from approved Grants
Select * from Fund_ApprovedGrant;

-- Insert into Open_Grants
insert into Applies_OpenGrant_Date(name, amount, date_applied)
values (name_input, amout_input, date_applied_input);

-- DELETE FROM Applies_Open_Grants WHERE status = “approved”;
delete from Applies_OpenGrant_Date 
where status = 'approved'

-- Insert into Fund_ApprovedGrant
insert into Fund_ApprovedGrant(name,amount,projectName)
values (name_input,amout_input,projectName_input);

-- Insert & delete and from fund grant

-- Delete from Applies_Open_Grants 
delete from Applies_OpenGrant_Date
where name = name_input and amount = amout_input;

-- Delete from Collaborators 
delete from Assigned_Collaborators_Id
where id = id_input;

-- Update LabMember Set name
update Contains_LabMember
set name = name_input
where id = toBeChanged_id_input;

-- Update LabMemner Set ID
--update Contains_LabMember
--set id = id_input
--where id = toBeChanged_id_input;

-- Update LabMember Set education
update Contains_LabMember
set education = education_input
where id = toBeChanged_id_input;
