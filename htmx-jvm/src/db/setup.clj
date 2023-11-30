(ns db.setup
  (:require [db.connection :refer [db-spec]]
            [db.todo]
            [db.user]
            [yesql.core :refer [defqueries]]))

(defn create-tables! []
  (let [queries (defqueries "db/ddl/create.sql"
                  {:connection db-spec})]
    (doseq [create-query queries]
      (create-query))))

(defn seed-tables! []
  (db.user/seed!)
  (db.todo/seed!))

(defn reset-db! []
  (let [queries (defqueries "db/ddl/drop.sql"
                  {:connection db-spec})]
    (doseq [drop-query queries]
      (drop-query)))
  (create-tables!)
  (seed-tables!))

(comment
  (declare list-tables)
  (list-tables)

  (create-tables!)
  (reset-db!))
