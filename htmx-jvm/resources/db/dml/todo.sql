-- name: get-all
SELECT *
FROM Todo
WHERE user_id = :user;

-- name: get-by-id
SELECT *
FROM Todo
WHERE id = :id;

-- name: get-count
SELECT COUNT(*) AS count
FROM Todo;

-- name: insert<!
INSERT INTO Todo (name, user_id) VALUES
(:name, :user);

-- name: toggle-done-by-id!
UPDATE Todo SET
done = CASE WHEN done = 0 THEN 1 ELSE 0 END
WHERE id = :id;

-- name: delete-by-id!
DELETE FROM Todo
WHERE id = ?;

-- name: delete-done-by-user!
DELETE FROM Todo
WHERE user_id = :user
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

