(ns routers.todo
  (:require
   [compojure.core :refer [context defroutes GET POST]]
   [utils :refer [extract-body extract-query render render* style]]))

(def url-prefix "/todo")

(defonce todos-db (atom [{:name "Get oil change"
                          :done? false}
                         {:name "Buy apples"
                          :done? true}]))

(defn todo-list
  ([req]
   (todo-list @todos-db (:filtered (extract-query req))))
  ([todos filtered?]
   (->> (if filtered?
          (filter (comp not :done?) todos)
          todos)
        (map-indexed
         (fn [idx todo]
           [:p {:style (style {:text-decoration (when (:done? todo) "line-through solid black 2px")
                               :cursor "pointer"})
                :hx-post (str url-prefix "/toggle/" idx)
                :hx-target "#todos"
                :hx-include "#filtered"}
            (:name todo)])))))

(defn get-handler [req]
  (let [todos @todos-db
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
                   then send gotime to me"
               :hx-target "#todos"
               :hx-get (str url-prefix "/list")
               :hx-trigger "gotime"}]]]))

(comment
  @todos-db
  (get-handler {:filtered true})

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

(defn toggle-todo [idx req]
  (let [filtered? (:filtered (extract-body req))]
    (println filtered?)
    (swap! todos-db update idx update :done? not)
    (render* todo-list @todos-db filtered?)))

(defroutes todo-routes
  (context url-prefix []
    (GET "/" [] #(render get-handler %))
    (GET "/list" [] #(render* todo-list %))
    (POST "/" [] create-todo)
    (POST "/toggle/:id{[0-9]+}" [id] #(toggle-todo (Integer/parseInt id) %))))

