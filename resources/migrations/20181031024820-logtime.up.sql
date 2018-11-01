CREATE TABLE time_log
(id SERIAL PRIMARY KEY,
 user_id integer references users(id),
 type_id integer references time_types(id),
 task_time integer);
--;;
CREATE TABLE notes
(log_id integer references time_log(id),
 note VARCHAR(30));
--;;
CREATE TABLE current_user_states
(user_id integer primary key references users(id),
click_time timestamp,
note VARCHAR(30));
