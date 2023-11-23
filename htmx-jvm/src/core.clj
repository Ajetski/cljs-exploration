(ns core
  (:require
   [clojure.string :as string]
   [clojure.walk :refer [keywordize-keys]]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [hiccup2.core :as h]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.util.codec :refer [form-decode]]))

(defn render
  "renders hiccup vector or function returning hiccup vector into html string"
  [input & args]
  (str (h/html (if (vector? input)
                 input
                 (apply input args)))))

(defn style [s]
  (string/join ";" (map #(str (name %) ":" ((keyword %) s)) (keys s))))

(def home-page
  [:html
   [:head
    [:script {:src "https://unpkg.com/htmx.org@1.9.9"}]]
   [:body
    [:div {:style (style {:border "1px solid white"
                          :padding-top "1rem"
                          :padding-left "1rem"})}
     [:form {:hx-post "/greet"}
      [:label {:for "name"} "Name: "]
      [:input {:id "name"
               :name "name"
               :type "text"}]
      [:button "Say Hi!"]]]]])

(def not-found-page
  [:h1 "Page not found"])

(defn handle-greet [req]
  (let [body (-> req
                 :body
                 slurp
                 form-decode
                 keywordize-keys)]
    (str "Hello, " (:name body))))

(defroutes app
  (GET "/" [] (render home-page))
  (GET "/home" [] (render home-page))
  (POST "/greet" [] handle-greet)
  (route/resources "/assets")
  (route/not-found (render not-found-page)))

(defn -main
  [& args]
  (run-jetty app {:port 3000
                  :join? false}))

(comment
  (do
    ; for development, eval do block to restart server
    (.stop server)
    #_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
    (def server (-main))))
