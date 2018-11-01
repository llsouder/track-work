CREATE TABLE users
(id SERIAL PRIMARY KEY,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 admin BOOLEAN,
 last_login TIMESTAMP,
 is_active BOOLEAN,
 pass VARCHAR(300));
--;;
insert into users (first_name, last_name) values ('Super', 'Man');
--;;
insert into users (first_name, last_name) values ('Bat', 'Man');
