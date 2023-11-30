(ns db.todo
  (:require
   [db.connection :refer [db-spec]]
   [yesql.core :refer [defqueries]]))

(defqueries "db/dml/todo.sql"
  {:connection db-spec})

(comment
  (require '[clojure.repl :refer [doc]])
  (db.todo/seed!)

  (db.todo/get-all-by-user-id {:user_id 1})
  (doc db.todo/get-all-by-user-id)

  (db.todo/get-count-by-user-id {:user_id 1})

  (db.todo/get-by-id {:id 2} {:result-set-fn first})
  (db.todo/get-by-id {:id 9001} {:result-set-fn first})

  (db.todo/insert<! {:name "test", :user 3})
  (doc db.todo/insert<!)

  (doc db.todo/toggle-done-by-id!)
  (db.todo/toggle-done-by-id! {:id 2})

  (db.todo/delete-by-id! {:id 8})

  (db.todo/delete-done-by-user! {:user 1}))
