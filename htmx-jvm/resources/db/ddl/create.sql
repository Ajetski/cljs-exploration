-- name: create-user-table!
CREATE TABLE IF NOT EXISTS User (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username VARCHAR(50) NOT NULL,
  hash VARCHAR(20) NOT NULL
);

-- name: create-todo-table!
CREATE TABLE IF NOT EXISTS Todo (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(100) NOT NULL,
  done BOOLEAN DEFAULT FALSE,
  user_id INTEGER REFERENCES User(id)
);

-- name: list-tables
SELECT 
    name
FROM 
    sqlite_schema
WHERE 
    type ='table' AND 
    name NOT LIKE 'sqlite_%';

