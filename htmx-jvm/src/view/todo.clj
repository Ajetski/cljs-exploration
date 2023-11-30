(ns view.todo
  (:require
   [db.todo]
   [utils :refer [extract-query style]]))

(def url-prefix "/todo")

(def user-id 1)

(defn todo-list
  ([req]
   (todo-list (db.todo/get-all-by-user-id {:user_id user-id})
              (-> req extract-query :filtered)))
  ([all-todos filtered?]
   (map
    (fn [todo]
      (let [{id :id
             name :name} todo
            done? (= (:done todo) 1)]
        [:p {:style (style {:text-decoration (when done? "line-through solid black 2px")
                            :cursor "pointer"})
             :hx-post (str url-prefix "/toggle/" id)
             :hx-target "#todos"
             :hx-include "#filtered"}
         name]))
    (if filtered?
      (filter (comp not #(= 1 %) :done) all-todos)
      all-todos))))

(defn todo-page [req]
  (let [todos (db.todo/get-all-by-user-id {:user_id user-id})
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
      [:br]]
     [:form {:hx-get (str url-prefix "/list")
             :hx-target "#todos"}
      [:label {:for "filtered"} "Filter by incomplete"]
      [:input {:type "checkbox"
               :name "filtered"
               :checked (if filtered? "true" "false")
               :_ "on click send submit to closest <form/>"}]]
     [:form {:hx-delete (str url-prefix "/complete")
             :hx-target "#todos"}
      [:button "Click to delete completed todos"]]]))

