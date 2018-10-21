CREATE TABLE users
(id VARCHAR(20) PRIMARY KEY,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 admin BOOLEAN,
 last_login TIMESTAMP,
 is_active BOOLEAN,
 pass VARCHAR(300));

insert into users (id, first_name, last_name) values (1, 'Super', 'Man');
insert into users (id, first_name, last_name) values (2, 'Bat', 'Man');
