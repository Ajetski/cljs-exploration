(ns routers.todo
  (:require
   [compojure.core :refer [context defroutes GET POST]]
   [db.todo]
   [utils :refer [extract-body extract-query render render-page style]]))

(def url-prefix "/todo")

(def user-id 1)

(defn todo-list
  ([req]
   (todo-list (db.todo/get-all {:user user-id})
              (-> req extract-query :filtered)))
  ([all-todos filtered?]
   (map
    (fn [todo]
      [:p {:style (style {:text-decoration (when (= 1 (:done todo)) "line-through solid black 2px")
                          :cursor "pointer"})
           :hx-post (str url-prefix "/toggle/" (:id todo))
           :hx-target "#todos"
           :hx-include "#filtered"}
       (:name todo)])
    (if filtered?
      (filter (comp not #(= 1 %) :done) all-todos)
      all-todos))))

(defn todo-page [req]
  (let [todos (db.todo/get-all {:user user-id})
        query (extract-query req)
        filtered? (or (= "true" (:filtered query))
                      (= "on" (:filtered query)))]
    [:div {:id "root"
           :style (style {:border "1px solid white"
                          :padding-top "1rem"
                          :padding-left "1rem"})}
     [:div {:id "todos"} (todo-list todos filtered?)]
     [:form {:hx-post url-prefix
             :hx-target "#todos"
             :_ "on htmx:afterRequest reset() me"}
      [:label {:for "name"} "Todo Name: "]
      [:input {:name "name"}]
      [:button "Add todo"]
      [:br]
      [:label {:for "filtered"} "Filter by incomplete"]
      [:input {:id "filtered"
               :type "checkbox"
               :name "filtered"
               :checked (if filtered? "true" "false")
               :_ "on click toggle [@checked] on me
                   then send filter to me"
               :hx-target "#todos"
               :hx-get (str url-prefix "/list")
               :hx-trigger "filter"}]]]))

(defn create-todo [req]
  (let [name (-> req extract-body :name)]
    (db.todo/insert<! {:name name, :user 1})
    (render todo-list (db.todo/get-all {:user user-id}))))

(defn toggle-todo [id req]
  (let [filtered? (:filtered (extract-body req))]
    (db.todo/toggle-done-by-id! {:id id})
    (render todo-list (db.todo/get-all {:user user-id}) filtered?)))

(defroutes todo-routes
  (context url-prefix []
    (GET "/" [] #(render-page todo-page %))
    (GET "/list" [] #(render todo-list %))
    (POST "/" [] create-todo)
    (POST "/toggle/:id{[0-9]+}" [id] #(toggle-todo (Integer/parseInt id) %))))

(comment
  (todo-page {:filtered true})
  (todo-list (db.todo/get-all {:user user-id})))
