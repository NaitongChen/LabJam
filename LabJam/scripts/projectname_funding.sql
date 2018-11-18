CREATE VIEW ProjectName_Funding AS
Select f.projectName, cast(SUM(amount) as float) as total 
From Fund_ApprovedGrant f
Group by f.projectName;

commit;