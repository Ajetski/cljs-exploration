(ns routers.home
  (:require
   [compojure.core :refer [context defroutes GET POST]]
   [utils :refer [extract-body render-page style]]))

(def get-handler
  [:div {:style (style {:border "1px solid white"
                        :padding-top "1rem"
                        :padding-left "1rem"})}
   [:form {:hx-post "/home"}
    [:label {:for "name"} "Name: "]
    [:input {:name "name", :type "text"}]
    [:button "Say Hi!"]]])

(defn post-handler [req]
  (let [body (extract-body req)]
    (str "Hello, " (:name body))))

(defroutes home-routes
  (GET "/" [] (render-page get-handler))
  (context "/home" []
    (GET "/" [] (render-page get-handler))
    (POST "/" [] post-handler)))
