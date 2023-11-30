-- name: get-all-by-user-id
SELECT *
FROM Todo
WHERE user_id = :user_id;

-- name: get-by-id
SELECT *
FROM Todo
WHERE id = :id;

-- name: get-count-by-user-id
SELECT COUNT(*) AS count
FROM Todo
WHERE user_id = :user_id;

-- name: insert<!
INSERT INTO Todo (name, user_id) VALUES
(:name, :user);

-- name: toggle-done-by-id!
UPDATE Todo SET
done = CASE WHEN done = 0 THEN 1 ELSE 0 END
WHERE id = :idi;

-- name: delete-by-id!
DELETE FROM Todo
WHERE id = :id;

-- name: delete-done-by-user-id!
DELETE FROM Todo
WHERE user_id = :user_id 
AND done = 1;

-- name: seed!
INSERT INTO Todo (name, user_id) VALUES
("Write cdoe", 1),
("Stage changes", 1),
("Make commit", 1),
("Push changes", 1),
("Fix typos", 1),
("Rinse", 1),
("Repeat", 1);

