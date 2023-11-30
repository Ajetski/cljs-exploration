(ns router.todo
  (:require
   [compojure.core :refer [context defroutes DELETE GET POST]]
   [db.todo]
   [ring.util.response :refer [redirect]]
   [utils :refer [extract-body render render-page]]
   [view.todo :refer [todo-list todo-page]]))

(def url-prefix "/todo")

(def user-id 1)

(defn create-todo [req]
  (let [name (-> req extract-body :name)]
    (db.todo/insert<! {:name name, :user 1})
    (render todo-list (db.todo/get-all-by-user-id {:user_id user-id}))))

(defn toggle-todo [id req]
  (let [filtered? (:filtered (extract-body req))]
    (db.todo/toggle-done-by-id! {:id id})
    (render todo-list (db.todo/get-all-by-user-id {:user_id user-id}) filtered?)))

(defn delete-done-todos! [req]
  (db.todo/delete-done-by-user-id! {:user_id user-id})
  (todo-list req))

(defroutes todo-routes
  (GET "/" [] (redirect (str url-prefix)))
  (context url-prefix []
    (GET "/" [] #(render-page todo-page %))
    (GET "/list" [] #(render todo-list %))
    (POST "/" [] create-todo)
    (POST "/toggle/:id{[0-9]+}" [id] #(toggle-todo (Integer/parseInt id) %))
    (DELETE "/complete" [] #(render delete-done-todos! %))))

(comment
  (todo-page {:filtered true})
  (todo-list (db.todo/get-all-by-user-id {:user_id user-id})))
