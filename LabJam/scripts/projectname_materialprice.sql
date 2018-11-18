CREATE VIEW ProjectName_MaterialPrice AS 
Select pm.name, cast(SUM(mm.materialPrice) as float) price 
From Project_MaterialType pm, MaterialType_MaterialPrice mm 
Where pm.materialType = mm.materialType 
Group by pm.name;