-- name: get-count
-- Returns the total number of users
Select COUNT(*) as count
FROM USER;

-- name: get-all
SELECT *
FROM USER;

-- name: seed!
INSERT INTO USER (username, hash) VALUES
("jack", "abc"),
("jill", "def"),
("peter", "parker");
