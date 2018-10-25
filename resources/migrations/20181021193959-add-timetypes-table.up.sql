CREATE TABLE timetypes
(id SERIAL PRIMARY KEY,
 name VARCHAR(30),
 description VARCHAR(30) );
--;;
insert into timetypes (name, description) values ('TGF', 'The main task.');
