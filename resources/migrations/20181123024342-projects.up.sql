CREATE TABLE projects
(id SERIAL PRIMARY KEY,
user_id integer references users(id),
proj_desc VARCHAR(30));
