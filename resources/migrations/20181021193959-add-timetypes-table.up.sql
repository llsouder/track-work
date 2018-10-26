CREATE TABLE time_types
(id SERIAL PRIMARY KEY,
 time_type VARCHAR(30),
 description VARCHAR(30) );
--;;
insert into time_types (time_type, description) values ('TGF', 'The main task.');
