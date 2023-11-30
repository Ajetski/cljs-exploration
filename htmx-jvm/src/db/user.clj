(ns db.user
  (:require
   [db.connection :refer [db-spec]]
   [yesql.core :refer [defqueries]]))

(defqueries "db/dml/user.sql"
  {:connection db-spec})

(comment
  (db.user/seed!)
  (db.user/get-all)
  (db.user/get-count))
