/*第1步：创建临时表空间  */
CREATE TEMPORARY TABLESPACE CATSIC_TEMP
TEMPFILE 'catsic_temp.dbf' 
SIZE 1024m  
AUTOEXTEND ON
NEXT 1024M MAXSIZE 2048M
EXTENT MANAGEMENT LOCAL;
 
/*第2步：创建数据表空间  */
CREATE TABLESPACE CATSIC_DATA
DATAFILE 'catsic_data.dbf' 
SIZE 1024m  
AUTOEXTEND ON
NEXT 1024M MAXSIZE UNLIMITED
ONLINE
LOGGING
FORCE LOGGING
EXTENT MANAGEMENT LOCAL;
 
/*第3步：创建用户并指定表空间  */
CREATE USER catsic IDENTIFIED BY catsic
DEFAULT TABLESPACE CATSIC_DATA
TEMPORARY TABLESPACE CATSIC_TEMP;
 
/*第4步：给用户授予权限  */
GRANT CONNECT,RESOURCE,DROP ANY TABLE,CREATE TABLESPACE,DROP TABLESPACE TO catsic;
--GRANT CREATE TABLESPACE,DROP TABLESPACE TO catsic;
GRANT INSERT,UPDATE,SELECT,DELETE ON TABLE TO CATSIC_ACCESS;

/**
--步骤一：  删除user
DROP USER CATSIC CASCADE;
--说明： 删除了user，只是删除了该user下的schema objects，是不会删除相应的tablespace的。
--步骤二： 删除tablespace
DROP TABLESPACE CATSIC_TEMP INCLUDING CONTENTS AND DATAFILES;

DROP TABLESPACE CATSIC_DATA INCLUDING CONTENTS AND DATAFILES;

CREATE DATABASE CATSIC;*/