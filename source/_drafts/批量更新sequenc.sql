declare 
cursor tabCursor is 
select cu.table_name,cu.column_name from user_cons_columns cu, user_constraints au  where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' and (cu.table_name like 'WMS%' OR cu.table_name like 'SGAI%'); 
maxIdStr varchar2(10) ;--当前编号的最大值 
sqlStr varchar2(200);--sql语句 
seqName varchar2(30);--sequence名字 
exist varchar2(1);--sequence是否存在，0:不存在，1：存在 
pkType varchar2(10);-- PK的类型 

begin 
for tab in tabCursor loop 

maxIdStr:=NULL; 
--sequence命名规则为：“SEQ_”+表名 
seqName:='SEQ_'||tab.table_name; 

--先取得当前表的最大id+1 

SELECT data_type into pkType FROM User_Tab_Cols s WHERE s.column_name=UPPER(tab.column_name) AND s.table_name=UPPER(tab.table_name); 

if pkType='NUMBER' then --PK为数字 

sqlStr:='select to_char(max(t.'||tab.column_name||')+1) from '||tab.table_name ||' t'; 
execute immediate sqlStr into maxIdStr; 

--判断该sequence是否存在, 
sqlStr:='select COUNT(*) from seq where SEQUENCE_NAME = '''||seqName||''''; 
execute immediate sqlStr into exist ; 

if maxIdStr IS NOT NULL and exist='1'  then 

--更新sequence 

--先删除 
sqlStr:='DROP SEQUENCE '||seqName; 
execute immediate sqlStr; 
--再创建 
sqlStr:='CREATE SEQUENCE '||seqName||' MINVALUE 1 MAXVALUE 1.00000000000000E+27 INCREMENT BY 1 START WITH '||maxIdStr||' CACHE 20 NOORDER  NOCYCLE' ; 
execute immediate sqlStr; 

end if;--存在seq 

end if;--pk是数字 

end loop; 
commit; 
end;