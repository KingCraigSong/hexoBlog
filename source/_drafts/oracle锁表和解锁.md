select * from v$session t1, v$locked_object t2 where t1.sid = t2.SESSION_ID; 

alter system kill session '28,26421'; 
