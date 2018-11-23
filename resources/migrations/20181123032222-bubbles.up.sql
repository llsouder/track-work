CREATE TABLE bubbles
(id SERIAL PRIMARY KEY,
task_id integer references tasks(id),
bubble timestamp default now());
