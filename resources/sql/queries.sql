-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name get-users :?
-- :doc retrieves all user records
SELECT * FROM users

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name get-timetypes :?
-- :doc retrieves all timetype records
SELECT * FROM time_types

-- :name create-timetype! :! :n
-- :doc creates a new timetype record
INSERT INTO time_types
(time_type, description)
VALUES (:time_type, :description)

-- :name get-user-state :? :1
-- :doc retrieves a user state given the id
SELECT * FROM current_user_states
WHERE user_id = :user_id

-- :name update-user-state! :! :n
-- :doc updates current user state.
INSERT INTO current_user_states (user_id, click_time, note)
VALUES (:user_id, :click_time, :note)
ON CONFLICT (user_id) DO UPDATE 
  SET click_time = excluded.click_time,
      note = excluded.note;
