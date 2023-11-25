(ns routers.todo
  (:require
   [compojure.core :refer [context defroutes GET POST]]
   [utils :refer [extract-body render render* style]]))

(def url-prefix "/todo")

(defonce todos-db (atom [{:name "Get oil change"
                          :done? false}
                         {:name "Buy apples"
                          :done? true}]))

(defn todo-list [todos]
  (map-indexed
   (fn [idx todo]
     [:p {:style (style {:text-decoration (when (:done? todo) "line-through solid black 2px")
                         :cursor "pointer"})
          :hx-post (str url-prefix "/toggle/" idx)
          :hx-target "#todos"}
      (:name todo)])
   todos))

(defn get-handler []
  [:div {:style (style {:border "1px solid white"
                        :padding-top "1rem"
                        :padding-left "1rem"})}
   [:div {:id "todos"} (todo-list @todos-db)]
   [:form {:hx-post url-prefix
           :hx-target "#todos"
           :_ "on htmx:afterRequest reset() me"}
    [:label {:for "name"}]
    [:input {:name "name"}]
    [:button "Add todo"]]])

(comment
  todos-db
  (get-handler)

  ; reset db
  (swap! todos-db (fn [_]
                    [{:name "Get oil change"
                      :done? false}
                     {:name "Buy apples"
                      :done? true}])))

(defn create-todo [req]
  (let [{name :name} (extract-body req)]
    (swap! todos-db conj {:name name, :done? false})
    (render* todo-list @todos-db)))

(defn toggle-todo [idx]
  (swap! todos-db update (Integer/parseInt idx) update :done? not)
  (render* todo-list @todos-db))

(defroutes todo-routes
  (context url-prefix []
    (GET "/" [] (render get-handler))
    (POST "/" [] create-todo)
    (POST "/toggle/:id" [id] (toggle-todo id))))

