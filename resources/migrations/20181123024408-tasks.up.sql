CREATE TABLE tasks
(id SERIAL PRIMARY KEY,
proj_id integer references projects(id),
task_desc VARCHAR(30));
